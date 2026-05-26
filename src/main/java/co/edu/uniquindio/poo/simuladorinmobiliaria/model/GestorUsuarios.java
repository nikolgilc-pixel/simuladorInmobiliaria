package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class GestorUsuarios {
    private List<Usuario> listaUsuarios;

    public GestorUsuarios() {
        this.listaUsuarios = new ArrayList<>();
    }

    // ---------- Operaciones genéricas ----------------------------------------------------------

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

    public Usuario buscarPorEmail(String email) {
        for (Usuario u : listaUsuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
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

    // ── CRUD Comprador ────────────────────────────────────────────────────────

    public String registrarComprador(String nombre, String telefono, String email,
                                     String password, double presupuesto, String ciudad,
                                     TipoInmueble tipoInteres, double areaMin) {
        if (buscarPorEmail(email) != null) {
            return "Error: ya existe un usuario con ese correo.";
        }
        String id = "C" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Comprador c = new Comprador(id, nombre, telefono, email, password,
                LocalDate.now(), presupuesto, ciudad, tipoInteres, areaMin);
        listaUsuarios.add(c);
        return "Registro exitoso. Ya puedes iniciar sesion.";
    }

    public boolean eliminarComprador(String idComprador) {
        for (Usuario u : listaUsuarios) {
            if (u.getId().equals(idComprador) && u instanceof Comprador) {
                listaUsuarios.remove(u);
                return true;
            }
        }
        return false;
    }

    // ── CRUD Vendedor ─────────────────────────────────────────────────────────

    public String registrarVendedor(String nombre, String telefono, String email, String password) {
        if (buscarPorEmail(email) != null) {
            return "Error: ya existe un usuario con ese correo.";
        }
        String id = "V" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Vendedor v = new Vendedor(id, nombre, telefono, email, password, LocalDate.now());
        listaUsuarios.add(v);
        return "Vendedor " + v.getNombreCompleto() + " creado correctamente.";
    }

    public boolean eliminarVendedor(String idVendedor) {
        for (Usuario u : listaUsuarios) {
            if (u.getId().equals(idVendedor) && u instanceof Vendedor) {
                listaUsuarios.remove(u);
                return true;
            }
        }
        return false;
    }

    // ── Gestión de contraseña ─────────────────────────────────────────────────

    // Recibe el objeto del propio usuario; solo el controlador del usuario activo
    // debe invocar este método, garantizando que nadie cambie claves ajenas.
    public String cambiarContrasena(Usuario usuario, String nuevaContrasena) {
        if (nuevaContrasena == null || nuevaContrasena.trim().isEmpty()) {
            return "Error: la nueva contrasena no puede estar vacia.";
        }
        usuario.setPassword(nuevaContrasena.trim());
        return "Contrasena actualizada correctamente.";
    }

}
