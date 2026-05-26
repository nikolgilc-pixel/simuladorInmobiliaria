package co.edu.uniquindio.poo.simuladorinmobiliaria.controller;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.*;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoOperacion;

import java.util.List;

public class VendedorController {

    private final InmoSmart inmoSmart;

    public VendedorController(InmoSmart inmoSmart) {
        this.inmoSmart = inmoSmart;
    }

    public List<Inmueble> obtenerInmueblesVendedor(Vendedor vendedor) {
        return vendedor.getListaInmuebles();
    }

    public List<Transaccion> obtenerTransaccionesVendedor(String idVendedor) {
        return inmoSmart.getGestorTransacciones().listarTransaccionesPorVendedor(idVendedor);
    }

    public Inmueble registrarInmueble(Vendedor vendedor, String direccion, String ciudad,
                                      double area, double precio, String descripcion,
                                      TipoInmueble tipo) {
        Inmueble nuevo = inmoSmart.getGestorInmuebles()
                .registrarNuevoInmueble(direccion, ciudad, area, precio, descripcion, tipo);
        inmoSmart.vincularInmuebleAVendedor(nuevo, vendedor);
        return nuevo;
    }

    public String publicarInmueble(Vendedor vendedor, Inmueble inmueble, String descripcion) {
        return inmoSmart.procesarSolicitudPublicacion(vendedor, inmueble, descripcion);
    }

    public String retirarPublicacion(Vendedor vendedor, String codigoPublicacion) {
        return inmoSmart.procesarEliminacionPublicacion(vendedor, codigoPublicacion);
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

    public String actualizarPrecio(Inmueble inmueble, double nuevoPrecio) {
        return inmoSmart.actualizarPrecioInmueble(inmueble, nuevoPrecio);
    }

    public String actualizarPerfil(Vendedor vendedor, String nombre, String telefono,
                                   String email, String password) {
        return inmoSmart.actualizarDatosContacto(vendedor, nombre, telefono, email, password);
    }
}
