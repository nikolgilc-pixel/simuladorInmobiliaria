package co.edu.uniquindio.poo.simuladorinmobiliaria.controller;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.InmoSmart;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Usuario;

public class LoginController {

    private InmoSmart inmoSmart;

    public LoginController(InmoSmart inmoSmart) {
        this.inmoSmart = inmoSmart;
    }

    public Usuario autenticarUsuario(String email, String password) {
        return inmoSmart.autenticarUsuario(email, password);
    }
}
