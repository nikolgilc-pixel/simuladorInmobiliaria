package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import java.util.List;

public interface IServicioBusqueda {
    List<Publicacion> buscarPublicaciones(List<Publicacion> listaPublicaciones, FiltroBusqueda f);

}
