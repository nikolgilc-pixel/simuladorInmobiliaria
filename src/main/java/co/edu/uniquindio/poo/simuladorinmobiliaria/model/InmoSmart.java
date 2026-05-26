package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.AccionInmobiliaria;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoInmueble;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoOferta;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoOperacion;
import lombok.Getter;

import java.util.List;

@Getter
public class InmoSmart {
    private String codigoComercio;
    private GestorUsuarios gestorUsuarios;
    private GestorInmuebles gestorInmuebles;
    private GestorPublicacion gestorPublicaciones;
    private GestorTransacciones gestorTransacciones;
    private GestorNotificaciones gestorNotificaciones;
    private GestorReportes gestorReportes;
    private IServicioBusqueda iServicioBusqueda;

    // iServicioBusqueda se inyecta por constructor (RT03); los canales se registran vía agregarCanal
    public InmoSmart(String codigoComercio, IServicioBusqueda iServicioBusqueda) {
        this.codigoComercio = codigoComercio;
        this.iServicioBusqueda = iServicioBusqueda;
        this.gestorUsuarios = new GestorUsuarios();
        this.gestorInmuebles = new GestorInmuebles();
        this.gestorPublicaciones = new GestorPublicacion();
        this.gestorTransacciones = new GestorTransacciones();
        this.gestorNotificaciones = new GestorNotificaciones();
        this.gestorReportes = new GestorReportes();
        // Registro de canales concretos (RT03: inyectados aquí, nunca dentro del gestor)
        gestorNotificaciones.agregarCanal(new CanalCorreo());
        gestorNotificaciones.agregarCanal(new CanalSMS());
        gestorNotificaciones.agregarCanal(new CanalWhatsApp());
    }

    // RF03: crea la oferta, la vincula al inmueble, notifica al vendedor y suma puntos
    public void tramitarOferta(Comprador comprador, Inmueble inmueble, double monto) {
        if (monto <= 0 || inmueble.getEstado() != EstadoInmueble.DISPONIBLE) {
            return;
        }
        Oferta nuevaOferta = new Oferta(comprador, inmueble, monto);
        boolean agregada = gestorInmuebles.agregarOferta(nuevaOferta);
        if (agregada) {
            comprador.getListaOfertas().add(nuevaOferta);
            comprador.sumarPuntos(AccionInmobiliaria.OFERTAR);
            gestorNotificaciones.crearNotificacionNuevaOferta(inmueble.getVendedorAsignado(), inmueble);
        }
    }

    // RF04: cierre atómico — acepta la oferta, rechaza las demás, crea transacción,
    //        actualiza el inmueble, retira la publicación y otorga puntos a ambas partes
    public void procesarCierreOferta(Oferta ofertaAceptada, TipoOperacion tipoOperacion) {
        Inmueble inmueble = ofertaAceptada.getInmueble();
        Comprador comprador = ofertaAceptada.getComprador();
        Vendedor vendedor = inmueble.getVendedorAsignado();

        // 1. Rechazar todas las demás ofertas pendientes y notificar a esos compradores
        for (Oferta o : inmueble.getListaOfertas()) {
            if (!o.getCodigo().equals(ofertaAceptada.getCodigo())
                    && o.getEstadoOferta() == EstadoOferta.PENDIENTE) {
                o.actualizarEstado(EstadoOferta.RECHAZADA);
                gestorNotificaciones.crearNotificacionOfertaAceptada(o.getComprador(), inmueble);
            }
        }

        // 2. Marcar la oferta como aceptada
        ofertaAceptada.actualizarEstado(EstadoOferta.ACEPTADA);

        // 3. Crear la transacción inmutable
        Transaccion transaccion;
        if (tipoOperacion == TipoOperacion.VENTA) {
            transaccion = gestorTransacciones.registrarVenta(ofertaAceptada);
            inmueble.actualizarEstadoInmueble(EstadoInmueble.VENDIDO);
            vendedor.aceptarOferta(ofertaAceptada);
        } else {
            transaccion = gestorTransacciones.registrarArriendo(ofertaAceptada);
            inmueble.actualizarEstadoInmueble(EstadoInmueble.ARRENDADO);
            vendedor.registrarArriendo();
        }

        // 4. Retirar del catálogo público
        if (inmueble.getPublicacion() != null) {
            gestorPublicaciones.finalizarPublicacion(inmueble.getPublicacion().getCodigo());
        }

        // 5. Sumar puntos a ambas partes
        vendedor.sumarPuntos(AccionInmobiliaria.COMPLETAR_TRANSACCION);
        comprador.sumarPuntos(AccionInmobiliaria.COMPLETAR_TRANSACCION);
        if (transaccion.tipoOperacion() == TipoOperacion.VENTA) {
            comprador.sumarPuntos(AccionInmobiliaria.COMPRAR);
        }

        // 6. Notificar al comprador ganador
        gestorNotificaciones.crearNotificacionOfertaAceptada(comprador, inmueble);
    }

    // RF02: delega la búsqueda al motor inyectado y actualiza el historial del comprador
    public List<Publicacion> consultarCatalogo(Comprador comprador, FiltroBusqueda filtros) {
        comprador.actualizarHistorial(filtros);
        return iServicioBusqueda.buscarPublicaciones(gestorPublicaciones.getListaPublicaciones(), filtros);
    }

    // RF05 (recomendaciones): usa el historialIntereses actual del comprador
    public List<Publicacion> obtenerSugerencias(Comprador comprador) {
        return iServicioBusqueda.buscarPublicaciones(
                gestorPublicaciones.getListaPublicaciones(),
                comprador.getHistorialIntereses()
        );
    }

    // Publica un inmueble: crea la publicación y la agrega al catálogo
    public String procesarSolicitudPublicacion(Vendedor vendedor, Inmueble inmueble, String descripcion) {
        Publicacion p = gestorPublicaciones.crearPublicacion(vendedor, inmueble, descripcion);
        if (p == null) {
            return "Error: el inmueble no está disponible para publicar.";
        }
        boolean añadida = gestorPublicaciones.añadirPublicacion(p);
        if (!añadida) {
            return "Error: la publicación ya existe.";
        }
        return "Publicación creada: " + p.getCodigo();
    }

    // Retira una publicación por solicitud del vendedor
    public String procesarEliminacionPublicacion(Vendedor vendedor, String codigoPublicacion) {
        boolean eliminada = gestorPublicaciones.eliminarPublicacion(codigoPublicacion);
        if (!eliminada) {
            return "Error: no se encontró la publicación.";
        }
        return "Publicación eliminada correctamente.";
    }

    // Vincula un inmueble ya creado a un vendedor y lo registra en el sistema
    public String vincularInmuebleAVendedor(Inmueble inmueble, Vendedor vendedor) {
        inmueble.setVendedorAsignado(vendedor);
        vendedor.getListaInmuebles().add(inmueble);
        gestorInmuebles.añadirInmueble(inmueble);
        return "Inmueble " + inmueble.getCodigo() + " vinculado a " + vendedor.getNombreCompleto();
    }

    // RF06: delega la generación de reportes al gestor correspondiente
    public void buscarInmueble() {
        // El controlador construye el FiltroBusqueda y llama a consultarCatalogo()
    }

    public void procesarTransaccion(Oferta oferta) {
        procesarCierreOferta(oferta, TipoOperacion.VENTA);
    }

    public Usuario autenticarUsuario(String email, String password) {
        for (Usuario u : gestorUsuarios.getListaUsuarios()) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
}
