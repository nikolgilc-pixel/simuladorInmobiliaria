package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoOperacion;

import java.time.Instant;

public record Transaccion(
        String codigo,
        Comprador comprador,
        Vendedor vendedor,
        Inmueble inmueble,
        double valorFinal,
        TipoOperacion tipoOperacion,
        Instant fecha
) {}
