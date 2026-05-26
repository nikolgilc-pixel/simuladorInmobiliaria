package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.AccionInmobiliaria;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoInmueble;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoOferta;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoOperacion;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        for (Oferta o : inmueble.getListaOfertas()) {
            if (!o.getCodigo().equals(ofertaAceptada.getCodigo())
                    && o.getEstadoOferta() == EstadoOferta.PENDIENTE) {
                o.actualizarEstado(EstadoOferta.RECHAZADA);
                gestorNotificaciones.crearNotificacionOfertaAceptada(o.getComprador(), inmueble);
            }
        }

        ofertaAceptada.actualizarEstado(EstadoOferta.ACEPTADA);

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

        if (inmueble.getPublicacion() != null) {
            gestorPublicaciones.finalizarPublicacion(inmueble.getPublicacion().getCodigo());
        }

        vendedor.sumarPuntos(AccionInmobiliaria.COMPLETAR_TRANSACCION);
        comprador.sumarPuntos(AccionInmobiliaria.COMPLETAR_TRANSACCION);
        if (transaccion.tipoOperacion() == TipoOperacion.VENTA) {
            comprador.sumarPuntos(AccionInmobiliaria.COMPRAR);
        }

        comprador.getHistorialTransacciones().add(transaccion);
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

    // Publica un inmueble: crea la publicación, la agrega al catálogo y notifica compradores coincidentes
    public String procesarSolicitudPublicacion(Vendedor vendedor, Inmueble inmueble, String descripcion) {
        Publicacion p = gestorPublicaciones.crearPublicacion(vendedor, inmueble, descripcion);
        if (p == null) {
            return "Error: el inmueble no esta disponible para publicar.";
        }
        boolean añadida = gestorPublicaciones.añadirPublicacion(p);
        if (!añadida) {
            return "Error: la publicacion ya existe.";
        }
        for (Usuario u : gestorUsuarios.listarCompradores()) {
            Comprador c = (Comprador) u;
            if (inmuebleCoincideConIntereses(inmueble, c.getHistorialIntereses())) {
                gestorNotificaciones.crearNotificacionInmuebleSimilar(c, inmueble);
            }
        }
        return "Publicacion creada: " + p.getCodigo();
    }

    // Cambia el precio de un inmueble y notifica a todos los compradores con intereses coincidentes
    public String actualizarPrecioInmueble(Inmueble inmueble, double nuevoPrecio) {
        if (nuevoPrecio <= 0) {
            return "Error: el precio debe ser mayor a cero.";
        }
        double precioAnterior = inmueble.getPrecio();
        inmueble.setPrecio(nuevoPrecio);
        int notificados = 0;
        for (Usuario u : gestorUsuarios.listarCompradores()) {
            Comprador c = (Comprador) u;
            if (inmuebleCoincideConIntereses(inmueble, c.getHistorialIntereses())) {
                gestorNotificaciones.crearNotificacionCambioPrecio(c, inmueble);
                notificados++;
            }
        }
        return "Precio actualizado de $" + (long) precioAnterior + " a $" + (long) nuevoPrecio
                + ". Compradores notificados: " + notificados;
    }

    // Verifica si un inmueble satisface todos los criterios del filtro de un comprador
    private boolean inmuebleCoincideConIntereses(Inmueble inmueble, FiltroBusqueda filtro) {
        if (filtro == null) {
            return false;
        }
        if (filtro.ciudad() != null && !filtro.ciudad().isEmpty()
                && !inmueble.getCiudad().equalsIgnoreCase(filtro.ciudad())) {
            return false;
        }
        if (filtro.tipoInmueble() != null && inmueble.getTipoInmueble() != filtro.tipoInmueble()) {
            return false;
        }
        if (filtro.precioMaximo() > 0 && inmueble.getPrecio() > filtro.precioMaximo()) {
            return false;
        }
        if (inmueble.getPrecio() < filtro.precioMinimo()) {
            return false;
        }
        if (inmueble.getArea() < filtro.areaMinima()) {
            return false;
        }
        return true;
    }

    // Retira una publicación por solicitud del vendedor
    public String procesarEliminacionPublicacion(Vendedor vendedor, String codigoPublicacion) {
        boolean eliminada = gestorPublicaciones.eliminarPublicacion(codigoPublicacion);
        if (!eliminada) {
            return "Error: no se encontro la publicacion.";
        }
        return "Publicacion retirada correctamente.";
    }

    // Vincula un inmueble ya creado a un vendedor y lo registra en el sistema
    public String vincularInmuebleAVendedor(Inmueble inmueble, Vendedor vendedor) {
        inmueble.setVendedorAsignado(vendedor);
        vendedor.getListaInmuebles().add(inmueble);
        gestorInmuebles.añadirInmueble(inmueble);
        return "Inmueble " + inmueble.getCodigo() + " vinculado a " + vendedor.getNombreCompleto();
    }

    public Usuario autenticarUsuario(String email, String password) {
        for (Usuario u : gestorUsuarios.getListaUsuarios()) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    // ── Registro y eliminación de usuarios (delega a GestorUsuarios) ──────────

    public String registrarComprador(String nombre, String telefono, String email, String password,
                                     double presupuesto, String ciudad,
                                     TipoInmueble tipoInteres, double areaMin) {
        return gestorUsuarios.registrarComprador(nombre, telefono, email, password,
                presupuesto, ciudad, tipoInteres, areaMin);
    }

    // Solo el Administrador invoca este método desde su dashboard
    public String registrarVendedor(String nombre, String telefono, String email, String password) {
        return gestorUsuarios.registrarVendedor(nombre, telefono, email, password);
    }

    public boolean eliminarVendedor(String idVendedor) {
        return gestorUsuarios.eliminarVendedor(idVendedor);
    }

    public boolean eliminarComprador(String idComprador) {
        return gestorUsuarios.eliminarComprador(idComprador);
    }

    // El llamador debe pasar SesionGlobal.getUsuarioActual() para garantizar
    // que solo se modifica la credencial del usuario que está en sesión.
    public String cambiarContrasena(Usuario usuario, String nuevaContrasena) {
        return gestorUsuarios.cambiarContrasena(usuario, nuevaContrasena);
    }

    // ── Reportes (solo Administrador) ────────────────────────────────────────

    public Map<TipoInmueble, Integer> generarReporteTopInmuebles() {
        return gestorReportes.generarReporteTopInmuebles(gestorTransacciones.getListaTransacciones());
    }

    public Map<String, Integer> generarReporteDemandaCiudad() {
        return gestorReportes.generarReporteDemandaCiudad(gestorInmuebles.getListaInmuebles());
    }

    public List<Usuario> generarReporteCompradoresTop() {
        return gestorReportes.generarReporteCompradoresTop(gestorUsuarios.listarCompradores());
    }

    // ── Actualización de perfil ───────────────────────────────────────────────

    public String actualizarDatosContacto(Usuario usuario, String nuevoNombre, String nuevoTelefono,
                                          String nuevoEmail, String nuevaPassword) {
        if (!nuevoEmail.isEmpty() && !nuevoEmail.equalsIgnoreCase(usuario.getEmail())) {
            for (Usuario u : gestorUsuarios.getListaUsuarios()) {
                if (u.getEmail().equalsIgnoreCase(nuevoEmail)) {
                    return "Error: ese correo ya esta en uso.";
                }
            }
        }
        if (!nuevoNombre.isEmpty()) usuario.setNombreCompleto(nuevoNombre);
        if (!nuevoTelefono.isEmpty()) usuario.setTelefono(nuevoTelefono);
        if (!nuevoEmail.isEmpty()) usuario.setEmail(nuevoEmail);
        if (!nuevaPassword.isEmpty()) usuario.setPassword(nuevaPassword);
        return "Datos actualizados correctamente.";
    }

    // ── Utilidades de búsqueda ────────────────────────────────────────────────

    // Retorna las ciudades distintas con al menos una publicación activa
    public List<String> getCiudadesDisponibles() {
        List<String> ciudades = new ArrayList<>();
        for (Publicacion p : gestorPublicaciones.getListaPublicaciones()) {
            String ciudad = p.getInmueble().getCiudad();
            if (!ciudades.contains(ciudad)) {
                ciudades.add(ciudad);
            }
        }
        return ciudades;
    }

}
