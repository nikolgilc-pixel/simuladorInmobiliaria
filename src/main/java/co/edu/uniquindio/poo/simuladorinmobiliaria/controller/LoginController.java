package co.edu.uniquindio.poo.simuladorinmobiliaria.controller;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.InmoSmart;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Usuario;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;

public class LoginController {

    private final InmoSmart inmoSmart;

    public LoginController(InmoSmart inmoSmart) {
        this.inmoSmart = inmoSmart;
    }

    public Usuario autenticarUsuario(String email, String password) {
        return inmoSmart.autenticarUsuario(email, password);
    }

    public String registrarComprador(String nombre, String telefono, String email,
                                     String password, double presupuesto, String ciudad,
                                     TipoInmueble tipo, double areaMin) {
        return inmoSmart.registrarComprador(nombre, telefono, email, password,
                presupuesto, ciudad, tipo, areaMin);
    }
}
