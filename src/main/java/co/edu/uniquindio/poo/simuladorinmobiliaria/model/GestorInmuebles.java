package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoInmueble;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;

import java.util.ArrayList;
import java.util.List;

public class GestorInmuebles {
    // Relaciones
    private List<Inmueble> listaInmuebles;
    private InmoSmart ownedByInmoSmart;

    // Constructor inicializando la lista vacía
    public GestorInmuebles() {
        this.listaInmuebles = new ArrayList<>();
    }

    //registrar Inmueble
    public Inmueble registrarNuevoInmueble(String codigo, String direccion, String ciudad, double area,
                                           double precio, String descripcion, TipoInmueble tipoInmueble) {

        // verificar si ya existe o no
        if (buscarInmueblePorCodigo(codigo) != null) {
            System.out.println("Error: Ya existe un inmueble registrado con el código " + codigo);
            return null;
        }

        Inmueble nuevoInmueble = new Inmueble(codigo, direccion, ciudad, area, precio, descripcion, tipoInmueble);

        // al crear el inmueble por primera vez se le asignara el estado de disponible
        nuevoInmueble.setEstado(EstadoInmueble.DISPONIBLE);
        nuevoInmueble.setOwnedByGestorInmuebles(this);
        añadirInmueble(nuevoInmueble);

        return nuevoInmueble;
    }

    //añadir inmueble
    public boolean añadirInmueble(Inmueble nuevo) {
        if (nuevo != null && !this.listaInmuebles.contains(nuevo)) {
            this.listaInmuebles.add(nuevo);
            return true;
        }
        return false;
    }

    // buscarInmueble
    public Inmueble buscarInmueblePorCodigo(String codigoInmueble) {
        if (codigoInmueble == null || codigoInmueble.isBlank()) {
            return null;
        }

        for (Inmueble inm : listaInmuebles) {
            if (inm.getCodigo().equalsIgnoreCase(codigoInmueble)) {
                return inm;
            }
        }
        return null;
    }

    // listarInmueblesDisponibles
    public List<Inmueble> listarInmueblesDisponibles() {
        List<Inmueble> disponibles = new ArrayList<>();
        for (Inmueble inm : listaInmuebles) {
            if (inm.getEstado() == EstadoInmueble.DISPONIBLE) {
                disponibles.add(inm);
            }
        }
        return disponibles;
    }

    //listarInmueblesPorTipo
    public List<Inmueble> listarInmueblesPorTipo(TipoInmueble tipo) {
        List<Inmueble> filtrados = new ArrayList<>();
        if (tipo == null) return filtrados;

        for (Inmueble inm : listaInmuebles) {
            if (inm.getTipoInmueble() == tipo) {
                filtrados.add(inm);
            }
        }
        return filtrados;
    }

    //listarInmueblesPorVendedor
    public List<Inmueble> listarInmueblesPorVendedor(String idVendedor) {
        List<Inmueble> porVendedor = new ArrayList<>();
        if (idVendedor == null || idVendedor.isBlank()) return porVendedor;

        for (Inmueble inm : listaInmuebles) {
            if (inm.getVendedorAsignado() != null && inm.getVendedorAsignado().getId().equals(idVendedor)) {
                porVendedor.add(inm);
            }
        }
        return porVendedor;
    }

    //listarTodosLosInmuebles
    public List<Inmueble> listarTodosLosInmuebles() {
        return this.listaInmuebles;
    }

    //eliminarInmueble
    public boolean eliminarInmueble(String codigoInmueble) {
        Inmueble encontrado = buscarInmueblePorCodigo(codigoInmueble);
        if (encontrado != null) {
            this.listaInmuebles.remove(encontrado);
            System.out.println("Inmueble " + codigoInmueble + " eliminado correctamente del sistema.");
            return true;
        }
        return false;
    }

    //agregarOferta
    public boolean agregarOferta(Oferta nueva) {
        if (nueva == null || nueva.getOwnedByInmueble() == null) {
            return false;
        }

        Inmueble inmuebleDestino = nueva.getOwnedByInmueble();

        // Inicializamos la lista de ofertas dentro del inmueble si llega a estar en nulo
        if (inmuebleDestino.getListaOfertas() == null) {
            inmuebleDestino.setListaOfertas(new ArrayList<>());
        }

        // Si la oferta no ha sido agregada todavía, la añadimos
        if (!inmuebleDestino.getListaOfertas().contains(nueva)) {
            inmuebleDestino.getListaOfertas().add(nueva);
            System.out.println("Nueva oferta vinculada exitosamente al inmueble: " + inmuebleDestino.getCodigo());
            return true;
        }
        return false;
    }
}


