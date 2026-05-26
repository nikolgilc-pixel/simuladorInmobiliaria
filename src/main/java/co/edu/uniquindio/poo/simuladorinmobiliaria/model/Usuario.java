package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.AccionInmobiliaria;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.CausaPenalizacion;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.RangoUsuario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class Usuario {
    protected String id;
    protected String nombreCompleto;
    protected String telefono;
    protected String email;
    protected String password;
    protected LocalDate fechaRegistro;
    protected int puntosReputacion;
    protected RangoUsuario rangoUsuario;
    protected List<Notificacion> listaNotificaciones;

    public Usuario(String id, String nombreCompleto, String telefono, String email,
                   String password, LocalDate fechaRegistro) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.fechaRegistro = fechaRegistro;
        this.puntosReputacion = 0;
        this.rangoUsuario = RangoUsuario.PRINCIPIANTE;
        this.listaNotificaciones = new ArrayList<>();
    }

    public void sumarPuntos(AccionInmobiliaria accion) {
        switch (accion) {
            case PUBLICAR:
                puntosReputacion += 10;
                break;
            case OFERTAR:
                puntosReputacion += 5;
                break;
            case COMPRAR:
                puntosReputacion += 50;
                break;
            case COMPLETAR_TRANSACCION:
                puntosReputacion += 100;
                break;
        }
        actualizarRango();
    }

    public void quitarPuntos(CausaPenalizacion causa) {
        switch (causa) {
            case ELIMINAR_PUBLICACION_SIN_VENTA:
                puntosReputacion -= 10;
                break;
        }
        if (puntosReputacion < 0) {
            puntosReputacion = 0;
        }
        actualizarRango();
    }

    public void actualizarRango() {
        if (puntosReputacion <= 100) {
            rangoUsuario = RangoUsuario.PRINCIPIANTE;
        } else if (puntosReputacion <= 500) {
            rangoUsuario = RangoUsuario.INVERSIONISTA;
        } else if (puntosReputacion <= 2000) {
            rangoUsuario = RangoUsuario.EXPERTO_INMOBILIARIO;
        } else {
            rangoUsuario = RangoUsuario.MAGNATE_INMOBILIARIO;
        }
    }

    public void recibirNotificacion(Notificacion alerta) {
        listaNotificaciones.add(alerta);
    }
}
