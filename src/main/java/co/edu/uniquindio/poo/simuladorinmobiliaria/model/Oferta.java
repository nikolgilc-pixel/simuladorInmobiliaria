package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoOferta;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class Oferta {
    private String codigo;
    private double valor;
    private Instant fecha;
    private EstadoOferta estadoOferta;
    private Inmueble inmueble;
    private Comprador comprador;

    public Oferta(Comprador comprador, Inmueble inmueble, double valor) {
        this.codigo = UUID.randomUUID().toString();
        this.valor = valor;
        this.fecha = Instant.now();
        this.estadoOferta = EstadoOferta.PENDIENTE;
        this.inmueble = inmueble;
        this.comprador = comprador;
    }

    public void actualizarEstado(EstadoOferta nuevoEstado) {
        this.estadoOferta = nuevoEstado;
    }

    @Override
    public String toString() {
        return "Oferta{codigo='" + codigo + "', valor=" + valor +
                ", estado=" + estadoOferta +
                ", inmueble=" + inmueble.getCodigo() +
                ", comprador=" + comprador.getId() + '}';
    }
}
