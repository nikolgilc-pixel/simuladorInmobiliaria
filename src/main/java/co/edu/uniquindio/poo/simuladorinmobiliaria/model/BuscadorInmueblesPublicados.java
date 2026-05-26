package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoInmueble;

import java.util.ArrayList;
import java.util.List;

public class BuscadorInmueblesPublicados implements IServicioBusqueda {

    @Override
    public List<Publicacion> buscarPublicaciones(List<Publicacion> todas, FiltroBusqueda f) {
        List<Publicacion> resultados = new ArrayList<>();
        for (Publicacion p : todas) {
            if (cumpleConFiltros(p, f)) {
                resultados.add(p);
            }
        }
        return resultados;
    }

    public boolean cumpleConFiltros(Publicacion p, FiltroBusqueda f) {
        Inmueble i = p.getInmueble();

        // Solo se retornan inmuebles disponibles (RN04)
        if (i.getEstado() != EstadoInmueble.DISPONIBLE) {
            return false;
        }
        if (f.ciudad() != null && !f.ciudad().isEmpty()) {
            if (!i.getCiudad().equalsIgnoreCase(f.ciudad())) {
                return false;
            }
        }
        if (f.tipoInmueble() != null) {
            if (i.getTipoInmueble() != f.tipoInmueble()) {
                return false;
            }
        }
        if (i.getPrecio() < f.precioMinimo()) {
            return false;
        }
        if (f.precioMaximo() > 0 && i.getPrecio() > f.precioMaximo()) {
            return false;
        }
        if (i.getArea() < f.areaMinima()) {
            return false;
        }
        return true;
    }
}
