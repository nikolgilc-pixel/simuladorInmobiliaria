package co.edu.uniquindio.poo.simuladorinmobiliaria.controller;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.*;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoOperacion;

import java.util.List;

public class VendedorController {

    private InmoSmart inmoSmart;

    public VendedorController(InmoSmart inmoSmart) {
        this.inmoSmart = inmoSmart;
    }

    public List<Inmueble> obtenerInmueblesVendedor(Vendedor vendedor) {
        return vendedor.getListaInmuebles();
    }

    public List<Transaccion> obtenerTransaccionesVendedor(String idVendedor) {
        return inmoSmart.getGestorTransacciones().listarTransaccionesPorVendedor(idVendedor);
    }

    public void registrarInmueble(Vendedor vendedor, String direccion, String ciudad,
                                   double area, double precio, String descripcion, TipoInmueble tipo) {
        Inmueble nuevo = inmoSmart.getGestorInmuebles()
                .registrarNuevoInmueble(direccion, ciudad, area, precio, descripcion, tipo);
        inmoSmart.vincularInmuebleAVendedor(nuevo, vendedor);
    }

    public String publicarInmueble(Vendedor vendedor, Inmueble inmueble, String descripcion) {
        return inmoSmart.procesarSolicitudPublicacion(vendedor, inmueble, descripcion);
    }

    public List<Oferta> obtenerOfertasPendientes(Vendedor vendedor, Inmueble inmueble) {
        return vendedor.verOfertasPendientes(inmueble);
    }

    public void cerrarOferta(Oferta oferta, TipoOperacion tipo) {
        inmoSmart.procesarCierreOferta(oferta, tipo);
    }

    public void rechazarOferta(Vendedor vendedor, Oferta oferta) {
        vendedor.rechazarOferta(oferta);
    }
}
