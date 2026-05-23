package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class GestorPublicacion {
    //Atributos
    private int codigo;

    //relaciones
    private List<Publicacion> listaPublicaciones;
    private InmoSmart ownedByInmoSmart;

    public GestorPublicacion() {
        this.codigo= 1;
        this.listaPublicaciones= new ArrayList<>();
    }

    //crear publicacion

    public boolean crearPublicacion (Vendedor v, Inmueble i, String descripcion){

        if (validarDisponilibilidad(i)){
            this.codigo++;
            String nuevoCodigo= "PUB-" + this.codigo;
            Publicacion nueva = new Publicacion(nuevoCodigo, descripcion, v, i);

        }
        return false;
    }

    //validadDisponibilidad

    public boolean validarDisponilibilidad (Inmueble i){
        if (i==null){
            return false;

        }
        for (Publicacion p: this.listaPublicaciones){
            if (p.getInmueble() != null && p.getInmueble().equals(i)){
                return false;
            }
        }
        return true;
    }

    //Añadir publicacion
    public boolean añadirPublicacion (Publicacion p){
        if (p!= null){
            this.listaPublicaciones.add(p);
            return  true;
        }
        return false;
    }





}
