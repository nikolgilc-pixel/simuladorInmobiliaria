package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoInmueble;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Inmueble {
    //Atributos
    private String codigo;
    private String direccion;
    private String ciudad;
    private double area;
    private double precio;
    private String descripcion;
    private LocalDate fechaPublicacion;
    private TipoInmueble tipoInmueble;
    private EstadoInmueble estado;


    //Relaciones
    private Vendedor vendedorAsignado;
    private List<Oferta> listaOfertas;
    private Publicacion publicacion;
    private GestorInmuebles ownedByGestorInmuebles;


    public Inmueble(String codigo, String direccion, String ciudad, double area,
                    double precio, String descripcion, TipoInmueble tipoInmueble) {
        this.codigo = codigo;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.area = area;
        this.precio = precio;
        this.descripcion = descripcion;
        this.tipoInmueble = tipoInmueble;
        this.estado = EstadoInmueble.DISPONIBLE;
        this.fechaPublicacion = LocalDate.now();
        this.listaOfertas = new ArrayList<>();
    }


}
