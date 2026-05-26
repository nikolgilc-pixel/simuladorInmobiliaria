package co.edu.uniquindio.poo.simuladorinmobiliaria.controller;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.*;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;

import java.util.List;
import java.util.Map;

public class AdminController {

    private final InmoSmart inmoSmart;

    public AdminController(InmoSmart inmoSmart) {
        this.inmoSmart = inmoSmart;
    }

    public String crearVendedor(String nombre, String telefono,
                                 String email, String password) {
        return inmoSmart.registrarVendedor(nombre, telefono, email, password);
    }

    public boolean eliminarVendedor(String idVendedor) {
        return inmoSmart.eliminarVendedor(idVendedor);
    }

    public String cambiarContrasenaAdmin(Usuario admin, String nueva) {
        return inmoSmart.cambiarContrasena(admin, nueva);
    }

    public Map<TipoInmueble, Integer> reporteTopInmuebles() {
        return inmoSmart.generarReporteTopInmuebles();
    }

    public Map<String, Integer> reporteDemandaCiudad() {
        return inmoSmart.generarReporteDemandaCiudad();
    }

    public List<Usuario> reporteCompradoresTop() {
        return inmoSmart.generarReporteCompradoresTop();
    }

    public List<Usuario> obtenerTodosUsuarios() {
        return inmoSmart.getGestorUsuarios().getListaUsuarios();
    }

    public List<Usuario> obtenerVendedores() {
        return inmoSmart.getGestorUsuarios().listarVendedores();
    }

    public List<Usuario> obtenerCompradores() {
        return inmoSmart.getGestorUsuarios().listarCompradores();
    }

    public List<Inmueble> obtenerInventarioDisponible() {
        return inmoSmart.getGestorInmuebles().listarInmueblesDisponibles();
    }
}
