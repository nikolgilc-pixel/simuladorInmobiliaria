package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.AccionInmobiliaria;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.CausaPenalizacion;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoInmueble;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
public class GestorPublicacion {
    private List<Publicacion> listaPublicaciones;

    public GestorPublicacion() {
        this.listaPublicaciones = new ArrayList<>();
    }

    // Crea el objeto Publicacion si el inmueble está disponible
    public Publicacion crearPublicacion(Vendedor vendedor, Inmueble inmueble, String descripcion) {
        if (!validarDisponibilidadInmueble(inmueble)) {
            return null;
        }
        String codigo = UUID.randomUUID().toString();
        return new Publicacion(codigo, descripcion, vendedor, inmueble);
    }

    private boolean validarDisponibilidadInmueble(Inmueble inmueble) {
        return inmueble.getEstado() == EstadoInmueble.DISPONIBLE;
    }

    // Agrega la publicación si no existe ya; vincula inmueble y suma puntos al vendedor
    public boolean añadirPublicacion(Publicacion publicacion) {
        for (Publicacion p : listaPublicaciones) {
            if (p.getCodigo().equals(publicacion.getCodigo())) {
                return false;
            }
        }
        Vendedor vendedor = publicacion.getVendedor();
        for (Publicacion p : vendedor.getListaPublicaciones()) {
            if (p.getCodigo().equals(publicacion.getCodigo())) {
                return false;
            }
        }
        listaPublicaciones.add(publicacion);
        vendedor.getListaPublicaciones().add(publicacion);
        publicacion.getInmueble().setPublicacion(publicacion);
        vendedor.sumarPuntos(AccionInmobiliaria.PUBLICAR);
        return true;
    }

    public Optional<Publicacion> buscarPublicacion(String codigoPublicacion) {
        for (Publicacion p : listaPublicaciones) {
            if (p.getCodigo().equals(codigoPublicacion)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    // Retira la publicación del catálogo al cerrar una transacción (sin penalización)
    public boolean finalizarPublicacion(String codigoPublicacion) {
        Optional<Publicacion> opt = buscarPublicacion(codigoPublicacion);
        if (!opt.isPresent()) {
            return false;
        }
        Publicacion p = opt.get();
        listaPublicaciones.remove(p);
        p.getVendedor().getListaPublicaciones().remove(p);
        return true;
    }

    // Elimina la publicación por solicitud del vendedor; penaliza si el inmueble no está vendido
    public boolean eliminarPublicacion(String codigoPublicacion) {
        Optional<Publicacion> opt = buscarPublicacion(codigoPublicacion);
        if (!opt.isPresent()) {
            return false;
        }
        Publicacion p = opt.get();
        Vendedor vendedor = p.getVendedor();
        if (p.getInmueble().getEstado() != EstadoInmueble.VENDIDO) {
            vendedor.quitarPuntos(CausaPenalizacion.ELIMINAR_PUBLICACION_SIN_VENTA);
        }
        listaPublicaciones.remove(p);
        vendedor.getListaPublicaciones().remove(p);
        return true;
    }
}
