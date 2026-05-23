package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;

public record FiltroBusqueda(String ciudad, TipoInmueble tipoInmueble, double precioMinimo,
                             double precioMaximo, double areaMinima) {
}
