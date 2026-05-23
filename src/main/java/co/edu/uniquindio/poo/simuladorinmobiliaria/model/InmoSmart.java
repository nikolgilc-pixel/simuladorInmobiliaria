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
        this.gestorNotificaciones = new GestorNotificaciones();
        this.gestorUsuarios = new GestorUsuarios();
        this.iServicioBusqueda = new IServicioBusqueda() {};
        this.gestorReportes = new GestorReportes();
        this.gestorTransacciones = new GestorTransacciones();
        this.gestorInmuebles = new GestorInmuebles();
        this.gestorPublicaciones = new GestorPublicacion();
    }
}
