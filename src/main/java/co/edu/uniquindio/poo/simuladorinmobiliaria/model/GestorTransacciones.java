package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoOperacion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GestorTransacciones {

    // Atributos internos
    private int contadorCodigos;

    // Relaciones
    private List<Transaccion> listaTransacciones;
    private InmoSmart ownedByInmoSmart;

    // Constructor inicializando las listas limpias
    public GestorTransacciones() {
        this.contadorCodigos = 0;
        this.listaTransacciones = new ArrayList<>();
    }

    //registarrVenta
    public Transaccion registrarVenta(Oferta o) {
        if (o == null || o.getComprador() == null || o.getOwnedByInmueble() == null) {
            System.out.println("⚠Error: No se puede registrar la venta. Oferta incompleta.");
            return null;
        }

        this.contadorCodigos++;
        String codigoTransaccion = "TR-" + this.contadorCodigos;

        // Creamos la transacción de tipo VENTA usando el record
        Transaccion nuevaVenta = new Transaccion(
                codigoTransaccion,
                o.getComprador(),
                o.getOwnedByInmueble().getVendedorAsignado(),
                o.getOwnedByInmueble(),
                o.getValor(),
                TipoOperacion.VENTA,
                LocalDate.now(),
                this);

        añadirTransaccion(nuevaVenta);

        // se añade la tranacicion a el historial de transaciiones del comprador
        if (o.getComprador().getHistorialTransacciones() == null) {
            o.getComprador().setHistorialTransacciones(new ArrayList<>());
        }
        o.getComprador().getHistorialTransacciones().add(nuevaVenta);

        System.out.println("Venta registrada con éxito: " + codigoTransaccion);
        return nuevaVenta;
    }

    //registrar Transaciion arriendo
    public Transaccion registrarArriendo(Oferta o) {
        if (o == null || o.getComprador() == null || o.getOwnedByInmueble() == null) {
            System.out.println("Error: No se puede registrar el arriendo. Oferta incompleta.");
            return null;
        }

        this.contadorCodigos++;
        String codigoTransaccion = "TR-" + this.contadorCodigos;

        // Creamos la transacción de tipo ARRIENDO usando el record
        Transaccion nuevoArriendo = new Transaccion(
                codigoTransaccion,
                o.getComprador(),
                o.getOwnedByInmueble().getVendedorAsignado(),
                o.getOwnedByInmueble(),
                o.getValor(),
                TipoOperacion.ARRIENDO,
                LocalDate.now(),
                this
        );

        añadirTransaccion(nuevoArriendo);

        // añadimos la transaccion a el historial de transacciones del comprador
        if (o.getComprador().getHistorialTransacciones() == null) {
            o.getComprador().setHistorialTransacciones(new ArrayList<>());
        }
        o.getComprador().getHistorialTransacciones().add(nuevoArriendo);

        System.out.println("Arriendo registrado con éxito: " + codigoTransaccion);
        return nuevoArriendo;
    }

    //Añadir transaccion
    public void añadirTransaccion(Transaccion t) {
        if (t != null && !this.listaTransacciones.contains(t)) {
            this.listaTransacciones.add(t);
        }
    }

    //buscar Transaccion
    public Optional<Transaccion> buscarTransaccion(String codigoTransaccion) {
        if (codigoTransaccion == null || codigoTransaccion.isBlank()) {
            return Optional.empty();
        }

        for (Transaccion t : listaTransacciones) {
            if (t.codigo().equalsIgnoreCase(codigoTransaccion)) { // .codigo() porque Transaccion es record
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    //listarTransacciones
    public List<Transaccion> listarTransacciones() {
        return this.listaTransacciones;
    }

    //listarTransacciones
    public List<Transaccion> listarTransaccionesVenta() {
        List<Transaccion> ventas = new ArrayList<>();
        for (Transaccion t : listaTransacciones) {
            if (t.tipoOperacion() == TipoOperacion.VENTA) {
                ventas.add(t);
            }
        }
        return ventas;
    }

    //listarTransaccionesArriendo
    public List<Transaccion> listarTransaccionesArriendo() {
        List<Transaccion> arriendos = new ArrayList<>();
        for (Transaccion t : listaTransacciones) {
            if (t.tipoOperacion() == TipoOperacion.ARRIENDO) {
                arriendos.add(t);
            }
        }
        return arriendos;
    }

    //listarTransaccionesPorVendedor
    public List<Transaccion> listarTransaccionesPorVendedor(String idVendedor) {
        List<Transaccion> filtradas = new ArrayList<>();
        if (idVendedor == null || idVendedor.isBlank()) return filtradas;

        for (Transaccion t : listaTransacciones) {
            if (t.vendedor() != null && t.vendedor().getId().equals(idVendedor)) {
                filtradas.add(t);
            }
        }
        return filtradas;
    }

    //listarTransaccionesPorComprador
    public List<Transaccion> listarTransaccionesPorComprador(String idComprador) {
        List<Transaccion> filtradas = new ArrayList<>();
        if (idComprador == null || idComprador.isBlank()) return filtradas;

        for (Transaccion t : listaTransacciones) {
            if (t.comprador() != null && t.comprador().getId().equals(idComprador)) {
                filtradas.add(t);
            }
        }
        return filtradas;
    }

    //Eliminar Transaccion
    public void eliminarTransaccion(String codigoTransaccion) {
        Optional<Transaccion> encontrada = buscarTransaccion(codigoTransaccion);
        if (encontrada.isPresent()) {
            this.listaTransacciones.remove(encontrada.get());
            System.out.println("Transacción " + codigoTransaccion + " eliminada correctamente.");
        } else {
            System.out.println("No se encontró la transacción con código: " + codigoTransaccion);
        }
    }
}
