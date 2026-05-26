package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoOperacion;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
public class GestorTransacciones {
    private List<Transaccion> listaTransacciones;

    public GestorTransacciones() {
        this.listaTransacciones = new ArrayList<>();
    }

    public Transaccion registrarVenta(Oferta oferta) {
        Transaccion t = new Transaccion(
                UUID.randomUUID().toString(),
                oferta.getComprador(),
                oferta.getInmueble().getVendedorAsignado(),
                oferta.getInmueble(),
                oferta.getValor(),
                TipoOperacion.VENTA,
                Instant.now()
        );
        añadirTransaccion(t);
        return t;
    }

    public Transaccion registrarArriendo(Oferta oferta) {
        Transaccion t = new Transaccion(
                UUID.randomUUID().toString(),
                oferta.getComprador(),
                oferta.getInmueble().getVendedorAsignado(),
                oferta.getInmueble(),
                oferta.getValor(),
                TipoOperacion.ARRIENDO,
                Instant.now()
        );
        añadirTransaccion(t);
        return t;
    }

    public void añadirTransaccion(Transaccion transaccion) {
        listaTransacciones.add(transaccion);
    }

    public Optional<Transaccion> buscarTransaccion(String codigoTransaccion) {
        for (Transaccion t : listaTransacciones) {
            if (t.codigo().equals(codigoTransaccion)) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    public List<Transaccion> listarTransacciones() {
        return listaTransacciones;
    }

    public List<Transaccion> listarTransaccionesVenta() {
        List<Transaccion> resultado = new ArrayList<>();
        for (Transaccion t : listaTransacciones) {
            if (t.tipoOperacion() == TipoOperacion.VENTA) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    public List<Transaccion> listarTransaccionesArriendo() {
        List<Transaccion> resultado = new ArrayList<>();
        for (Transaccion t : listaTransacciones) {
            if (t.tipoOperacion() == TipoOperacion.ARRIENDO) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    public List<Transaccion> listarTransaccionesPorVendedor(String idVendedor) {
        List<Transaccion> resultado = new ArrayList<>();
        for (Transaccion t : listaTransacciones) {
            if (t.vendedor().getId().equals(idVendedor)) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    public List<Transaccion> listarTransaccionesPorComprador(String idComprador) {
        List<Transaccion> resultado = new ArrayList<>();
        for (Transaccion t : listaTransacciones) {
            if (t.comprador().getId().equals(idComprador)) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    public void eliminarTransaccion(String codigoTransaccion) {
        Optional<Transaccion> opt = buscarTransaccion(codigoTransaccion);
        opt.ifPresent(listaTransacciones::remove);
    }
}
