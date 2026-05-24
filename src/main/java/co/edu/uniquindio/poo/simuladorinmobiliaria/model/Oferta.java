package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoOferta;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class Oferta {
    //Atributos
    private String codigo;
    private double valor;
    private Instant fecha;
    private EstadoOferta estadoOferta;

    //Relaciones

    private Inmueble ownedByInmueble;
    private Comprador comprador;

    public Oferta(Comprador comprador, Inmueble ownedByInmueble, double valor) {
        this.codigo = generarCodigoUnico();
        this.valor = valor;
        this.fecha = Instant.now();
        this.estadoOferta = EstadoOferta.PENDIENTE;
        this.ownedByInmueble= ownedByInmueble;
        this.comprador= comprador;
    }

    //Se genera un código único para la oferta
    public String generarCodigoUnico(){
        return UUID.randomUUID().toString();
    }

    //Método para actualizar el estado de la Oferta
    public void actualizarEstado(EstadoOferta nuevoEstado){
        this.estadoOferta = nuevoEstado;
    }

    //Método toString

    @Override
    public String toString() {
        return "Oferta{" +
                "codigo='" + codigo + '\'' +
                ", valor=" + valor +
                ", fecha=" + fecha +
                ", estadoOferta=" + estadoOferta +
                ", ownedByInmueble=" + ownedByInmueble.getCodigo() +
                ", comprador=" + comprador.getId() +
                '}';
    }
}
