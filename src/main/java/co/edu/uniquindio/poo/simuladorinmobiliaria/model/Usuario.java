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
}
