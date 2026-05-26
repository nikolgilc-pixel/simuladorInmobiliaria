package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GestorUsuarios {
    private List<Usuario> listaUsuarios;

    public GestorUsuarios() {
        this.listaUsuarios = new ArrayList<>();
    }

    public String añadirUsuario(Usuario usuario) {
        for (Usuario u : listaUsuarios) {
            if (u.getId().equals(usuario.getId())) {
                return null;
            }
        }
        listaUsuarios.add(usuario);
        return usuario.getId();
    }

    public Usuario buscarUsuario(String idUsuario) {
        for (Usuario u : listaUsuarios) {
            if (u.getId().equals(idUsuario)) {
                return u;
            }
        }
        return null;
    }

    public List<Usuario> listarCompradores() {
        List<Usuario> compradores = new ArrayList<>();
        for (Usuario u : listaUsuarios) {
            if (u instanceof Comprador) {
                compradores.add(u);
            }
        }
        return compradores;
    }

    public List<Usuario> listarVendedores() {
        List<Usuario> vendedores = new ArrayList<>();
        for (Usuario u : listaUsuarios) {
            if (u instanceof Vendedor) {
                vendedores.add(u);
            }
        }
        return vendedores;
    }

    public List<Usuario> listarUsuarios() {
        return listaUsuarios;
    }

    public boolean eliminarUsuario(String idUsuario) {
        Usuario usuario = buscarUsuario(idUsuario);
        if (usuario != null) {
            listaUsuarios.remove(usuario);
            return true;
        }
        return false;
    }
}
