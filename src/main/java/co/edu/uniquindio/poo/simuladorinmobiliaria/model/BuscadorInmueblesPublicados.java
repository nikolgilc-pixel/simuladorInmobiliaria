package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import java.util.ArrayList;
import java.util.List;

public class BuscadorInmueblesPublicados implements IServicioBusqueda{

    @Override
    public List<Publicacion> buscarPublicaciones(List<Publicacion> listaPublicaciones, FiltroBusqueda f) {
        List<Publicacion> resultados = new ArrayList<>();

        if (listaPublicaciones == null || f == null) {
            return resultados;
        }
        //for each para pasar por cada una de las publicaiones y tomar el inmueble
        for (Publicacion publicacion : listaPublicaciones) {
            Inmueble inmueble = publicacion.getInmueble();

            if (inmueble == null) {
                continue;
            }
            //ciudadDiferente?
            if (f.ciudad() != null && !f.ciudad().isBlank()) {
                if (!inmueble.getCiudad().equalsIgnoreCase(f.ciudad())) {
                    continue;
                }
            }

            if (f.tipoInmueble() != null) {
                if (inmueble.getTipoInmueble() != f.tipoInmueble()) {
                    continue;
                }
            }

            if (f.precioMinimo() > 0) {
                if (inmueble.getPrecio() < f.precioMinimo()) {
                    continue;
                }
            }

            //
            if (f.precioMaximo() > 0) {
                if (inmueble.getPrecio() > f.precioMaximo()) {
                    continue;
                }
            }

            if (f.areaMinima() > 0) {
                if (inmueble.getArea() < f.areaMinima()) {
                    continue;
                }
            }
            resultados.add(publicacion);
        }

        return resultados;
    }
}


