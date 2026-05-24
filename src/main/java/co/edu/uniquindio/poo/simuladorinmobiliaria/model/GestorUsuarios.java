package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import lombok.Setter;

import java.util.ArrayList;

@Setter
public class GestorUsuarios {
    //Atributos

    //Relaciones
    private ArrayList <Usuario> listaUsuarios;

    public GestorUsuarios () {
            this.listaUsuarios = new ArrayList<>();
    }

    //RegistrarUsuario
    public String registrarUsuario ( Usuario usuario){
        return "";
    }




}
