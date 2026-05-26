package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoInmueble;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Inmueble {
    private String codigo;
    private String direccion;
    private String ciudad;
    private double area;
    private double precio;
    private String descripcion;
    private LocalDate fechaRegistro;
    private TipoInmueble tipoInmueble;
    private EstadoInmueble estado;
    private Vendedor vendedorAsignado;
    private List<Oferta> listaOfertas;
    private Publicacion publicacion;
    private List<String> nombresImagenes;

    public Inmueble(String direccion, String ciudad, double area, double precio,
                    String descripcion, TipoInmueble tipoInmueble) {
        this.codigo = UUID.randomUUID().toString();
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.area = area;
        this.precio = precio;
        this.descripcion = descripcion;
        this.tipoInmueble = tipoInmueble;
        this.estado = EstadoInmueble.DISPONIBLE;
        this.fechaRegistro = LocalDate.now();
        this.listaOfertas = new ArrayList<>();
        this.nombresImagenes = new ArrayList<>();
    }

    public void actualizarEstadoInmueble(EstadoInmueble nuevoEstado) {
        this.estado = nuevoEstado;
    }
}
