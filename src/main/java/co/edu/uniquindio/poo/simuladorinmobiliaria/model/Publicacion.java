package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Publicacion {

    //Atributos
    private String codigo;
    private LocalDate fecha;
    private LocalTime hora;
    private String descripcion;

    //Relaciones
    private Vendedor vendedor;
    private Inmueble inmueble;
    private GestorPublicacion gestorPublicacion;


    public Publicacion(String codigo, String descripcion, Vendedor vendedor, Inmueble inmueble) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.vendedor = vendedor;
        this.inmueble = inmueble;
        this. hora= LocalTime.now();
        this.fecha=LocalDate.now();
    }
}
