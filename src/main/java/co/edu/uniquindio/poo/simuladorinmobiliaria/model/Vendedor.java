package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import lombok.Getter;
import lombok.Setter;

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
}
