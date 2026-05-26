package co.edu.uniquindio.poo.simuladorinmobiliaria;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.InmoSmart;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Usuario;

public class SesionGlobal {
    private static InmoSmart inmoSmart;
    private static Usuario usuarioActual;

    public static InmoSmart getInmoSmart() { return inmoSmart; }
    public static void setInmoSmart(InmoSmart i) { inmoSmart = i; }
    public static Usuario getUsuarioActual() { return usuarioActual; }
    public static void setUsuarioActual(Usuario u) { usuarioActual = u; }
}
