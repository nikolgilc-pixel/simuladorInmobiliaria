package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoOperacion;

import java.time.LocalDate;

public record Transaccion(String codigo, Comprador comprador, Vendedor vendedor, Inmueble inmueble, double valorFinal, TipoOperacion tipoOperacion, LocalDate fecha, GestorTransacciones ownedByTransacciones) {
}
