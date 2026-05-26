package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoInmueble;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoOferta;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GestorInmuebles {
    private List<Inmueble> listaInmuebles;

    public GestorInmuebles() {
        this.listaInmuebles = new ArrayList<>();
    }

    // Crea y registra un nuevo inmueble con código generado automáticamente (UUID)
    public Inmueble registrarNuevoInmueble(String direccion, String ciudad, double area,
                                           double precio, String descripcion, TipoInmueble tipo) {
        Inmueble nuevo = new Inmueble(direccion, ciudad, area, precio, descripcion, tipo);
        listaInmuebles.add(nuevo);
        return nuevo;
    }

    public boolean añadirInmueble(Inmueble inmueble) {
        for (Inmueble i : listaInmuebles) {
            if (i.getCodigo().equals(inmueble.getCodigo())) {
                return false;
            }
        }
        listaInmuebles.add(inmueble);
        return true;
    }

    public Inmueble buscarInmueblePorCodigo(String codigoInmueble) {
        for (Inmueble i : listaInmuebles) {
            if (i.getCodigo().equals(codigoInmueble)) {
                return i;
            }
        }
        return null;
    }

    public List<Inmueble> listarInmueblesDisponibles() {
        List<Inmueble> disponibles = new ArrayList<>();
        for (Inmueble i : listaInmuebles) {
            if (i.getEstado() == EstadoInmueble.DISPONIBLE) {
                disponibles.add(i);
            }
        }
        return disponibles;
    }

    public List<Inmueble> listarInmueblesPorTipo(TipoInmueble tipo) {
        List<Inmueble> resultado = new ArrayList<>();
        for (Inmueble i : listaInmuebles) {
            if (i.getTipoInmueble() == tipo) {
                resultado.add(i);
            }
        }
        return resultado;
    }

    public List<Inmueble> listarInmueblesPorVendedor(String idVendedor) {
        List<Inmueble> resultado = new ArrayList<>();
        for (Inmueble i : listaInmuebles) {
            if (i.getVendedorAsignado() != null &&
                    i.getVendedorAsignado().getId().equals(idVendedor)) {
                resultado.add(i);
            }
        }
        return resultado;
    }

    public List<Inmueble> listarTodosLosInmuebles() {
        return listaInmuebles;
    }

    public boolean eliminarInmueble(String codigoInmueble) {
        Inmueble inmueble = buscarInmueblePorCodigo(codigoInmueble);
        if (inmueble != null) {
            listaInmuebles.remove(inmueble);
            return true;
        }
        return false;
    }

    // Agrega una oferta al inmueble: verifica disponibilidad y no-duplicado (RF03)
    public boolean agregarOferta(Oferta oferta) {
        Inmueble inmueble = buscarInmueblePorCodigo(oferta.getInmueble().getCodigo());
        if (inmueble == null || inmueble.getEstado() != EstadoInmueble.DISPONIBLE) {
            return false;
        }
        for (Oferta o : inmueble.getListaOfertas()) {
            if (o.getCodigo().equals(oferta.getCodigo())) {
                return false;
            }
        }
        inmueble.getListaOfertas().add(oferta);
        return true;
    }
}
