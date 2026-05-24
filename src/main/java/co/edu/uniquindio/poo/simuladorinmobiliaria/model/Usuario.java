package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.AccionInmobiliaria;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.CausaPenalizacion;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.RangoUsuario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@Setter
public abstract class Usuario {
    //Atributos

    protected String id;
    protected String nombreCompleto;
    protected String email;
    protected String telefono;
    protected String password;
    protected LocalDate fechaRegistro;
    protected int puntosUsuario;

    //Relaciones
    protected GestorUsuarios ownedByGestorUsuario;
    protected ArrayList<Notificacion> listaNotificaciones;
    protected RangoUsuario rangoUsuario;

    public Usuario(String id, String nombreCompleto,String telefono, String email, String password, LocalDate fechaRegistro, int puntosUsuario, GestorUsuarios ownedByGestorUsuario) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.telefono= telefono;
        this.email = email;
        this.password = password;
        this.fechaRegistro = fechaRegistro;
        this.puntosUsuario = 0;
        this.ownedByGestorUsuario = ownedByGestorUsuario;
        this.listaNotificaciones= new ArrayList<>();
        this.rangoUsuario= RangoUsuario.PRINCIPIANTE;

    }
    // aqui se hara el metodo para suamr puntos al usuario

    public void sumarPuntos (AccionInmobiliaria accionInmobiliaria){
        if ( accionInmobiliaria== null) return;
        switch (accionInmobiliaria){
            case OFERTAR -> this.puntosUsuario += 5;
            case PUBLICAR -> this.puntosUsuario += 10;
            case COMPRAR -> this.puntosUsuario += 50;
            case COMPLETAR_TRANSACCION -> this.puntosUsuario +=100;
        }
        actualizarRango();
    }

    public void eliminarPuntos (CausaPenalizacion causaPenalizacion){
        if (causaPenalizacion==null) return;
        if (causaPenalizacion== CausaPenalizacion.ELIMINAR_PUBLICACION_SIN_VENTA){
            this.puntosUsuario -= 50;
            System.out.println("Penalización aplicada por eliminar publicación sin venta.");
        }
        if ( this.puntosUsuario <0) {
            this.puntosUsuario =0;
        }
        actualizarRango();

    }


    public  void actualizarRango (){
        if (this.puntosUsuario <= 100){
            this.rangoUsuario= RangoUsuario.PRINCIPIANTE;
        }else if( this.puntosUsuario>101 || this.puntosUsuario<=500){
            this.rangoUsuario=RangoUsuario.INVERSIONISTA;
        } else if (this.puntosUsuario > 501 || this.puntosUsuario <=2000 ){
            this.rangoUsuario=RangoUsuario.EXPERTO_INMOBILIARIO;
        }else  if ( this.puntosUsuario > 2000){
            this.rangoUsuario=RangoUsuario.MAGNATE_INMOBILIARIO;
        }
    }
    public void recibirNotificacion(Notificacion notificacion) {
        if (notificacion != null) {
            this.listaNotificaciones.add(notificacion);
            System.out.println("[" + this.nombreCompleto + " recibió una alerta]: " + notificacion.getContenido());
        }
    }

}
