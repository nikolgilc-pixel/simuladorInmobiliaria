package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;

//ImportarFiltroBusqueda
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Comprador extends Usuario{
    //Atributos
    private double presupuesto;
    private String ciudadInteres;
    private TipoInmueble tipoInmuebleInteres;
    private double areaMinima;
    private List<Inmueble> inmueblesFavoritos;
    private List<Transaccion> historialTransacciones;
    private FiltroBusqueda historialIntereses;

    //Relaciones
    private List<Oferta> ofertas;



    public Comprador(String id, String nombreCompleto, String telefono, String email, String password, LocalDate fechaRegistro,
                     int puntosUsuario, GestorUsuarios ownedByGestorUsuario, double presupuesto, String ciudadInteres,
                     TipoInmueble tipoInmuebleInteres, double areaMinima) {
        super(id, nombreCompleto,telefono, email, password, fechaRegistro, puntosUsuario, ownedByGestorUsuario);
        this.presupuesto = presupuesto;
        this.ciudadInteres = ciudadInteres;
        this.tipoInmuebleInteres = tipoInmuebleInteres;
        this.areaMinima = areaMinima;
        this.ofertas = new ArrayList<>();
        this.inmueblesFavoritos = new ArrayList<>();
        this.historialTransacciones = new ArrayList<>();
        this.historialIntereses = new FiltroBusqueda(this.ciudadInteres,this.tipoInmuebleInteres, 0,
                this.presupuesto, this.areaMinima);
    }



    // metodos para hacer funcionar el filtro de busqueda y el historial, el cual este ultimo solo guardara los datos de la ultima busqueda
}
