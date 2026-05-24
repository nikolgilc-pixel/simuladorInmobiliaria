package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import lombok.Setter;

import java.util.ArrayList;

@Setter
public class GestorUsuarios {
    //Atributos

    //Relaciones
    private ArrayList <Usuario> listaUsuarios;
    private InmoSmart ownedByInmoSmart;

    public GestorUsuarios () {
            this.listaUsuarios = new ArrayList<>();
    }

    //RegistrarUsuario
    public String registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            return "ERROR: El usuario no puede ser nulo.";
        }

        if (buscarUsuario(usuario.getId()) != null) {
            return "ERROR: Ya existe un usuario con la identificación " + usuario.getId() + ".";
        }

        this.listaUsuarios.add(usuario);
        return "ÉXITO: Usuario registrado correctamente.";
    }

    //buscar el usuario
    public Usuario buscarUsuario(String id) {
        if (id == null || id.isBlank()) return null;

        for (Usuario u : listaUsuarios) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }
    //listar compradores
    public ArrayList<Comprador> listarCompradores() {
        ArrayList<Comprador> compradores = new ArrayList<>();
        for (Usuario u : listaUsuarios) {
            if (u instanceof Comprador) {
                compradores.add((Comprador) u);
            }
        }
        return compradores;
    }

    //listarVendedores
    public ArrayList<Vendedor> listarVendedores() {
        ArrayList<Vendedor> vendedores = new ArrayList<>();
        for (Usuario u : listaUsuarios) {
            if (u instanceof Vendedor) {
                vendedores.add((Vendedor) u);
            }
        }
        return vendedores;
    }

    //listarUsuarios
    public ArrayList<Usuario> listarUsuarios() {
        return this.listaUsuarios;
    }

    //eliminarUsuario
    public void eliminarUsuario(String id) {
        Usuario encontrado = buscarUsuario(id);

        if (encontrado != null) {
            this.listaUsuarios.remove(encontrado);
            System.out.println("Usuario con ID " + id + " eliminado con éxito.");
        } else {
            System.out.println("Error: El usuario no existe en el sistema.");
        }
    }
}




