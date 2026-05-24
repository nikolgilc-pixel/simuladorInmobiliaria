package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.AccionInmobiliaria;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoInmueble;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class GestorPublicacion {
    //Atributos
    private int codigo;

    //relaciones
    private List<Publicacion> listaPublicaciones;
    private InmoSmart ownedByInmoSmart;

    public GestorPublicacion() {
        this.codigo= 1;
        this.listaPublicaciones= new ArrayList<>();
    }

    //crear publicacion

    public Optional<Publicacion> crearPublicacion (Vendedor v, Inmueble i, String descripcion){

        if (validarDisponilibilidad(i)){
            this.codigo++;
            String nuevoCodigo= "PUB-" + this.codigo;
            Publicacion nueva = new Publicacion(nuevoCodigo, descripcion, v, i);
            añadirPublicacion(nueva);
            return Optional.of(nueva);

        }
        return Optional.empty();
    }

    //validadDisponibilidad

    public boolean validarDisponilibilidad (Inmueble i){
        if (i==null)  return false;
        if (!i.getEstado().equals(EstadoInmueble.DISPONIBLE)){
                return false;
            }
        for (Publicacion p: this.listaPublicaciones){
            if (p.getInmueble() != null && p.getInmueble().equals(i)){
                return false;
            }
        }
        return true;
    }

    //Añadir publicacion
    public boolean añadirPublicacion (Publicacion p){
        if (p!= null|| p.getVendedor()==null) {
            return false;
        }
        Vendedor vendedor= p.getVendedor();

        boolean existeEnGestor = this.listaPublicaciones.contains(p);
        boolean existeEnVendedor = vendedor.getListaPublicaciones() != null &&
                vendedor.getListaPublicaciones().contains(p);

        if (existeEnGestor || existeEnVendedor) {
            return false; //
        }

        this.listaPublicaciones.add(p);
        if (vendedor.getListaPublicaciones() != null) {
            vendedor.getListaPublicaciones().add(p);
        }

        vendedor.sumarPuntos(AccionInmobiliaria.PUBLICAR);

        return true;
    }

    //buscar publicacion
    public Optional<Publicacion> buscarPublicacion(String codigoPublicacion) {


        if (codigoPublicacion == null || codigoPublicacion.isBlank()) {
            return Optional.empty(); // Devuelve una cajita vacía si no hay código valido
        }

        for (Publicacion p : this.listaPublicaciones) {
            if (p.getCodigo().equalsIgnoreCase(codigoPublicacion)) {
                return Optional.of(p); // ¡La encontramos! La metemos en la cajita y la retornamos
            }
        }
        return Optional.empty();
    }

    //Cerrar publicacion
    public boolean finalizarPublicacion(String codigoPublicacion) {
        Optional<Publicacion> encontrada = buscarPublicacion(codigoPublicacion);

        if (encontrada.isPresent()) {
            Publicacion p = encontrada.get();
            if (p.getInmueble() != null) {
                p.getInmueble().setEstado(EstadoInmueble.VENDIDO);
            }

            this.listaPublicaciones.remove(p);

            return true;
        }
        return false;
    }

    public boolean eliminarPublicacion(String codigoPublicacion) {
        Optional<Publicacion> encontrada = buscarPublicacion(codigoPublicacion);

        if (encontrada.isPresent()) {
            Publicacion p = encontrada.get();
            Vendedor vendedor = p.getVendedor();
            if (p.getInmueble() != null) {
                p.getInmueble().setEstado(EstadoInmueble.DISPONIBLE);
            }

            this.listaPublicaciones.remove(p);

            if (vendedor != null && vendedor.getListaPublicaciones() != null) {
                vendedor.getListaPublicaciones().remove(p);
            }
            return true;
        }
        return false;
    }






}
