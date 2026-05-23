package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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
    public List<Publicacion> buscarInmuebleFiltro (Comprador comprador, FiltroBusqueda filtroBusqueda){
        //GuardarHistorialYaHecho
        if (comprador != null && filtroBusqueda != null){
            comprador.setHistorialIntereses(filtroBusqueda);
        }
        List<Publicacion> listaPublicaciones = this.gestorPublicaciones.getListaPublicaciones();
        if (this.iServicioBusqueda != null){
            return  this.iServicioBusqueda.buscarPublicaciones(listaPublicaciones, filtroBusqueda);
        }
        return new ArrayList<>();
    }

}
