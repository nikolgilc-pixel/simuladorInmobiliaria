package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoOferta;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class InmoSmart {

    //Atributos
    private String codigoComercio;

     // Relaciones
    private GestorNotificaciones gestorNotificaciones;
    private GestorUsuarios gestorUsuarios;
    private IServicioBusqueda iServicioBusqueda;
    private GestorReportes gestorReportes;
    private GestorTransacciones gestorTransacciones;
    private GestorInmuebles gestorInmuebles;
    private GestorPublicacion gestorPublicaciones;

    // Constructor
    public InmoSmart(String codigoComercio, IServicioBusqueda iServicioBusqueda) {
        this.codigoComercio = codigoComercio;
        this.gestorNotificaciones = new GestorNotificaciones(new ArrayList<>());
        this.gestorUsuarios = new GestorUsuarios();
        this.iServicioBusqueda = iServicioBusqueda;
        this.gestorReportes = new GestorReportes();
        this.gestorTransacciones = new GestorTransacciones();
        this.gestorInmuebles = new GestorInmuebles();
        this.gestorPublicaciones = new GestorPublicacion();
    }

    //metodo para buscar inmuebles con filtro
    public void buscarInmueble() {
        System.out.println("Catálogo comercial activo en InmoSmart:");
        if (gestorPublicaciones.getListaPublicaciones() == null || gestorPublicaciones.getListaPublicaciones().isEmpty()) {
            System.out.println("No hay publicaciones activas en este momento.");
            return;
        }
        for (Publicacion pub : gestorPublicaciones.getListaPublicaciones()) {
            if (pub.getInmueble() != null) {
                System.out.println("- [" + pub.getCodigo() + "] - Tipo: " + pub.getInmueble().getTipoInmueble()
                        + " en " + pub.getInmueble().getCiudad() + " | Precio: $" + pub.getInmueble().getPrecio());
            }
        }
    }

    //buscarInmuebleFiltro
    public List<Publicacion> buscarInmuebleFiltro(Comprador comprador, FiltroBusqueda filtroBusqueda) {
        if (comprador != null && filtroBusqueda != null) {
            comprador.setHistorialIntereses(filtroBusqueda);
        }
        List<Publicacion> listaPublicaciones = this.gestorPublicaciones.getListaPublicaciones();
        if (this.iServicioBusqueda != null) {
            return this.iServicioBusqueda.buscarPublicaciones(listaPublicaciones, filtroBusqueda);
        }
        return new ArrayList<>();
    }

    //consultarCatalogo
    public List<Publicacion> consultarCatalogo(FiltroBusqueda filtros) {
        if (this.iServicioBusqueda != null && filtros != null) {
            return this.iServicioBusqueda.buscarPublicaciones(this.gestorPublicaciones.getListaPublicaciones(), filtros);
        }
        return this.gestorPublicaciones.getListaPublicaciones();
    }

    //procesarSolicitudPublicacion
    public String procesarSolicitudPublicacion(Vendedor v, Inmueble i, String descripcion) {
        if (v == null || i == null) {
            return "Error: Vendedor o Inmueble inválidos.";
        }

        Optional<Publicacion> nuevaPub = gestorPublicaciones.crearPublicacion(v, i, descripcion);
        if (nuevaPub.isPresent()) {
            // Sincronizamos la publicación en la lista interna del vendedor
            if (v.getListaPublicaciones() != null && !v.getListaPublicaciones().contains(nuevaPub.get())) {
                v.getListaPublicaciones().add(nuevaPub.get());
            }
            return "Éxito: Publicación " + nuevaPub.get().getCodigo() + " creada correctamente.";
        }
        return "Error: El inmueble no está disponible para publicar (ya vendido o en proceso).";
    }

    //v
    public String procesarEliminacionPublicacion(Vendedor v, String codigoPublicacion) {
        boolean eliminada = gestorPublicaciones.eliminarPublicacion(codigoPublicacion);
        if (eliminada) {
            return "🗑️ Éxito: La publicación " + codigoPublicacion + " ha sido retirada.";
        }
        return "Error: No se encontró la publicación o no se pudo eliminar.";
    }

    //vincularInmuebleAVendedor
    public String vincularInmuebleAVendedor(Inmueble i, Vendedor v) {
        if (i == null || v == null) {
            return "Error: Datos nulos.";
        }
        i.setVendedorAsignado(v);
        if (v.getListaInmuebles() != null && !v.getListaInmuebles().contains(i)) {
            v.getListaInmuebles().add(i);
        }
        return "Éxito: Inmueble asignado al portafolio de " + v.getNombreCompleto();
    }

    //tramitarOferta
    public void tramitarOferta(Oferta o) {
        if (o == null || o.getOwnedByInmueble() == null) return;

        // Agregamos la oferta usando el gestor de inmuebles
        boolean agregada = gestorInmuebles.agregarOferta(o);

        if (agregada) {
            Inmueble inmueble = o.getOwnedByInmueble();
            Vendedor vendedor = inmueble.getVendedorAsignado();

            // Si el inmueble tiene un vendedor asignado se le envia la alerta automática
            if (vendedor != null && gestorNotificaciones != null) {
                gestorNotificaciones.crearNotificacionNuevaOferta(vendedor, inmueble, o.getComprador(), o.getValor());
            }
        }
    }

    //procesarCierreOferta
    public void procesarCierreOferta(Oferta aceptada) {
        if (aceptada == null || aceptada.getOwnedByInmueble() == null) return;

        Inmueble inmueble = aceptada.getOwnedByInmueble();
        Vendedor vendedor = inmueble.getVendedorAsignado();

        //modificamos el estado de la oferta
        aceptada.setEstadoOferta(EstadoOferta.ACEPTADA);

        // actualizamos la info con ek metodo creado en vendedor
        if (vendedor != null) {
            vendedor.aceptarOferta(aceptada);
        }

        // s notifica al comprador q su oferta fue aceptada
        if (aceptada.getComprador() != null && gestorNotificaciones != null) {
            gestorNotificaciones.crearNotificacionOfertaAceptada(aceptada.getComprador(), inmueble);
        }
        procesarTransaccion(aceptada);

        // se finaliza y elimina la publicacion
        if (inmueble.getPublicacion() != null) {
            gestorPublicaciones.finalizarPublicacion(inmueble.getPublicacion().getCodigo());
        }
    }

    //procesarTransaccion
    public void procesarTransaccion(Oferta oferta) {
        if (oferta == null || gestorTransacciones == null) return;

        Transaccion t;

        // Aquí interactuamos con el GestorTransacciones según los métodos definidos en tu diagrama XML
        // Si tu modelo maneja tipos de operación específicos, el gestor creará el registro correspondiente
        if (oferta.getOwnedByInmueble().getPrecio() > 0) {
            t = gestorTransacciones.registrarVenta(oferta);
        } else {
            t = gestorTransacciones.registrarArriendo(oferta);
        }

        if (t != null) {
            System.out.println("[Transacción Registrada] Código: " + t.codigo() + " | Total: $" + t.valorFinal());
        }
    }

    // segurencias
    public List<Publicacion> obtenerSugerencias(Comprador c) {
        List<Publicacion> sugerencias = new ArrayList<>();
        if (c == null || c.getHistorialIntereses() == null) {
            return sugerencias;
        }
        // se crea el catálogo aplicando el registro de FiltroBusqueda que el comprador tiene guardado
        return consultarCatalogo(c.getHistorialIntereses());
    }
}


