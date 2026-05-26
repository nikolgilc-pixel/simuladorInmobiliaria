package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Comprador extends Usuario {
    private double presupuesto;
    private String ciudadInteres;
    private TipoInmueble tipoInmuebleInteres;
    private double areaMinima;
    private List<Inmueble> inmueblesFavoritos;
    private List<Transaccion> historialTransacciones;
    private List<Oferta> listaOfertas;
    private FiltroBusqueda historialIntereses;

    public Comprador(String id, String nombreCompleto, String telefono, String email,
                     String password, LocalDate fechaRegistro, double presupuesto,
                     String ciudadInteres, TipoInmueble tipoInmuebleInteres, double areaMinima) {
        super(id, nombreCompleto, telefono, email, password, fechaRegistro);
        this.presupuesto = presupuesto;
        this.ciudadInteres = ciudadInteres;
        this.tipoInmuebleInteres = tipoInmuebleInteres;
        this.areaMinima = areaMinima;
        this.listaOfertas = new ArrayList<>();
        this.inmueblesFavoritos = new ArrayList<>();
        this.historialTransacciones = new ArrayList<>();
        // El historialIntereses se inicializa con los datos de registro del comprador
        this.historialIntereses = new FiltroBusqueda(ciudadInteres, tipoInmuebleInteres, 0, presupuesto, areaMinima);
    }

    // Actualiza el historial de búsqueda; reemplaza (no acumula) el filtro anterior
    public void actualizarHistorial(FiltroBusqueda nuevo) {
        this.historialIntereses = nuevo;
    }

    // Métodos cuya lógica completa es orquestada por InmoSmart desde el controlador
    public void realizarOferta(Inmueble inmueble, double monto) {}
    public void comprarInmueble() {}
    public void solicitarBusqueda() {}
    public void visualizarResultadosBusqueda(List<Publicacion> resultados) {}
    public void verRecomendaciones() {}
}
