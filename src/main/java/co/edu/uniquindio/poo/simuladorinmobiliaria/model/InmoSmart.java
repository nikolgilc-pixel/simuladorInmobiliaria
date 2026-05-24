package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import lombok.Getter;

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
        this.gestorNotificaciones = new GestorNotificaciones(this);
        this.gestorUsuarios = new GestorUsuarios();
        this.iServicioBusqueda = new IServicioBusqueda() {};
        this.gestorReportes = new GestorReportes();
        this.gestorTransacciones = new GestorTransacciones();
        this.gestorInmuebles = new GestorInmuebles(this);
        this.gestorPublicaciones = new GestorPublicacion();
    }

    //------------------------------MÉTODOS-------------------------------------------------//
    //Método para tramitar una oferta
    public void tramitarOferta(Comprador c, Inmueble i, double monto){
        // EL OBJETO NACE AQUÍ
        Oferta nuevaOferta = new Oferta(c, i, monto);
        // Se agrega la oferta al inmueble
        gestorInmuebles.agregarOferta(nuevaOferta);

        //Se instancia una notificacion
        gestorNotificaciones.crearNotificacionNuevaOferta("Nueva")
    }
}
