package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoInmueble;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoOferta;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Vendedor extends Usuario{

    //Atriburos
    private int totalInmueblesVendidos;
    private int totalInmueblesArrendados;

    //Relaciones
    private List<Inmueble> listaInmuebles;
    private List<Publicacion> listaPublicaciones;

    public Vendedor(String id, String nombreCompleto, String telefono, String email, String password, LocalDate fechaRegistro,
                    int puntosUsuario, GestorUsuarios ownedByGestorUsuario) {
        super(id, nombreCompleto,telefono, email, password, fechaRegistro, puntosUsuario, ownedByGestorUsuario);
        this.totalInmueblesVendidos = 0;
        this.totalInmueblesArrendados = 0;
        this.listaInmuebles= new ArrayList<>();
        this.listaNotificaciones = new ArrayList<>();
    }

    //--------------------------------MÉTODOS------------------------------------------------------------------------//

    //Método para ver ofertas
    public void verOfertasPendientes(Inmueble inmueble){
        List<Oferta> ofertasPendientes = buscarOfertasPendientes(inmueble);
        for (Oferta o: ofertasPendientes){
            mostrarMensaje(o.toString());
        }
    }

    //Método para buscar las ofertas pendientes de un inmueble
    public List<Oferta> buscarOfertasPendientes(Inmueble inmueble){
        List<Oferta> ofertasPendientes = new ArrayList<>();
        for(Oferta o: inmueble.getListaOfertas()){
            if(o.getEstadoOferta()== EstadoOferta.PENDIENTE){
                ofertasPendientes.add(o);
            }
        }
    }

    //Método para gestionar una oferta específica
    public void gestionarOferta(Oferta oferta, boolean aceptada){
        if(aceptada){
            aceptarOferta(oferta);
            mostrarMensaje("Oferta aceptada. El inmueble ha sido vendido/arrendado.");
        } else{
            rechazarOferta(oferta);
            mostrarMensaje("Oferta rechazada. El inmueble sigue disponible.");
        }
    }

    public void aceptarOferta(Oferta oferta){
        oferta.actualizarEstado(EstadoOferta.ACEPTADA);
        Inmueble inmueble = oferta.getOwnedByInmueble();
        inmueble.setEstado(EstadoInmueble.VENDIDO);
        this.totalInmueblesVendidos++;
        //llamar a sumar puntos
    }

    public void rechazarOferta(Oferta oferta){
        oferta.actualizarEstado(EstadoOferta.RECHAZADA);
        Inmueble inmueble = oferta.getOwnedByInmueble();
        inmueble.setEstado(EstadoInmueble.DISPONIBLE);
    }

    //------------------------------UTILIDADES-----------------------------------------------------------------------//

    public void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(null, mensaje);
    }
}
