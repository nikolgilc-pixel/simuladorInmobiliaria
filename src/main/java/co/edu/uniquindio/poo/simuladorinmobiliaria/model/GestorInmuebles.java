package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoInmueble;

import java.util.ArrayList;
import java.util.List;

public class GestorInmuebles {
    //Atributos
    private List<Inmueble> listaInmuebles;

    // Relaciones
    private InmoSmart ownedByInmoSmart;

    public GestorInmuebles(InmoSmart ownedByInmoSmart) {
        this.listaInmuebles = new ArrayList<>();
        this.ownedByInmoSmart = ownedByInmoSmart;
    }

    //---------------------------------MÉTODOS----------------------------------------------------------------------//
    //Método para buscar un inmueble
    public Inmueble buscarInmueble(String codigoInmueble){
        Inmueble inmueble = null;
        for(Inmueble i:listaInmuebles){
            if(i.getCodigo().equals(codigoInmueble)){
                inmueble = i;
            }
        }
        return inmueble;
    }

    //Método para agregar una oferta a la lista de ofertas de un inmueble
    public boolean agregarOferta(Oferta oferta){
        boolean seAgrego = false;
        Inmueble i = buscarInmueble(oferta.getOwnedByInmueble().getCodigo());
        if(i != null && i.getEstado()== EstadoInmueble.DISPONIBLE){
            i.getListaOfertas().add(oferta);
            seAgrego = true;
        }
        return seAgrego;
    }
}
