package co.edu.uniquindio.poo.simuladorinmobiliaria.controller;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.*;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoInmueble;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;

import java.text.NumberFormat;
import java.util.List;
import java.util.Optional;

public class CompradorController {

    private InmoSmart inmoSmart;

    public CompradorController(InmoSmart inmoSmart) {
        this.inmoSmart = inmoSmart;
    }

    public List<Publicacion> buscarPublicaciones(Comprador comprador, String ciudad,
                                                  TipoInmueble tipo, double precioMin,
                                                  double precioMax, double areaMin) {
        FiltroBusqueda filtro = new FiltroBusqueda(ciudad, tipo, precioMin, precioMax, areaMin);
        return inmoSmart.consultarCatalogo(comprador, filtro);
    }

    public List<Publicacion> obtenerSugerencias(Comprador comprador) {
        return inmoSmart.obtenerSugerencias(comprador);
    }

    public String realizarOferta(Comprador comprador, String codigoPub, double monto, NumberFormat nf) {
        if (monto <= 0) {
            return "El monto debe ser mayor a cero.";
        }
        Optional<Publicacion> pubOpt = inmoSmart.getGestorPublicaciones().buscarPublicacion(codigoPub);
        if (pubOpt.isEmpty()) {
            return "No se encontro una publicacion activa con ese codigo.";
        }
        Inmueble inmueble = pubOpt.get().getInmueble();
        if (inmueble.getEstado() != EstadoInmueble.DISPONIBLE) {
            return "Este inmueble ya no esta disponible.";
        }
        inmoSmart.tramitarOferta(comprador, inmueble, monto);
        return "Oferta enviada: $" + nf.format(monto) + " por " + inmueble.getDireccion();
    }
}
