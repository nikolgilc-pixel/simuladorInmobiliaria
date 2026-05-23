package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoOferta;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class Oferta {
    //Atributos
    private String codigo;
    private double valor;
    private LocalDate fecha;
    private EstadoOferta estadoOferta;

    //Relaciones

    private Inmueble ownedByInmueble;
    private Comprador comprador;

    public Oferta(String codigo, double valor,  Comprador comprador, Inmueble ownedByInmueble) {
        this.codigo = codigo;
        this.valor = valor;
        this.fecha = LocalDate.now();
        this.estadoOferta = EstadoOferta.PENDIENTE;
        this.ownedByInmueble= ownedByInmueble;
        this.comprador= comprador;

    }
}
