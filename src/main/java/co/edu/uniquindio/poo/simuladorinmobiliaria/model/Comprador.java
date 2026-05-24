package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.AccionInmobiliaria;
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



    // metodos para hacer funcionar el filtro de busqueda y el historial, el cual este ultimo
    // solo guardara los datos de la ultima busqueda
    public boolean realizarOferta(Inmueble i, double monto) {
        if (i == null || monto <= 0) {
            return false;
        }
        //como cada oferta debe ser identificada le pondremos un codigo a cada una
        String codigoOferta = "OFR-" + this.getId() + "-" + (this.ofertas.size() + 1);
        Oferta nuevaOferta = new Oferta(codigoOferta, monto, this, i);

        // Guardamos en las listas correspondientes
        this.ofertas.add(nuevaOferta);
        if (i.getListaOfertas() != null) {
            i.getListaOfertas().add(nuevaOferta);
        }

        // Sistema de recompensas de tu proyecto
        this.sumarPuntos(AccionInmobiliaria.OFERTAR);
        System.out.println("Oferta realizada con éxito.");
        return true;
    }

    //aqui miramos si el comprador puede con el presupuesto q tiene comprar el inmueble
    public boolean comprarInmueble(Inmueble i) {
        if (i == null) {
            return false;
        }

        if (this.presupuesto < i.getPrecio()) {
            System.out.println("Presupuesto insuficiente para comprar este inmueble.");
            return false;
        }

        this.sumarPuntos(AccionInmobiliaria.COMPRAR);
        System.out.println("Fondos aprobados para la compra del inmueble.");
        return true;
    }

    //
    public List<Publicacion> solicitarBusqueda(IServicioBusqueda motorBusqueda, List<Publicacion> listaGeneral, FiltroBusqueda nuevoFiltro) {
        if (motorBusqueda == null || listaGeneral == null || nuevoFiltro == null) {
            return new ArrayList<>();
        }

        // Actualiza el historial (sobrescribe la búsqueda anterior)
        actualizarHistorial(nuevoFiltro);

        // Retorna las publicaciones filtradas usando tu BuscadorInmueblesPublicados
        return motorBusqueda.buscarPublicaciones(listaGeneral, nuevoFiltro);
    }

    //Aqui emplearemos el metodo, para q el sistema de recomendaciones solo use la ultima busqueda del usuario

    public void actualizarHistorial(FiltroBusqueda nuevo) {
        if (nuevo != null) {
            this.historialIntereses = nuevo;
        }
    }

    //Aqui se puede de ver las publicaciones resultadas para los filtros q da el usuario
    public void visualizarResultadosBusqueda(List<Publicacion> resultados) {
        System.out.println("\n=== RESULTADOS DE BÚSQUEDA PARA: " + this.getNombreCompleto().toUpperCase() + " ===");
        if (resultados == null || resultados.isEmpty()) {
            System.out.println("No se encontraron publicaciones con los filtros aplicados.");
            return;
        }

        for (Publicacion pub : resultados) {
            Inmueble inm = pub.getInmueble();
            if (inm != null) {
                System.out.println("[" + pub.getCodigo() + "] " + inm.getTipoInmueble() + " en " + inm.getCiudad() + " - $" + inm.getPrecio());
            }
        }
    }
    //metodo para ver las recomendaciones en base a la ultima busqueda del comprador

    public List<Publicacion> verRecomendaciones(List<Publicacion> carteleraGeneral) {
        List<Publicacion> recomendaciones = new ArrayList<>();

        // Si no hay historial de búsqueda o la cartelera está vacía, no recomendamos nada
        if (this.historialIntereses == null || carteleraGeneral == null) {
            return recomendaciones;
        }

        //sacamos todos los gustos de la ultima búsqueda del comprador
        String ciudadBuscada = this.historialIntereses.ciudad();
        TipoInmueble tipoBuscado = this.historialIntereses.tipoInmueble();
        double presupuestoMaximo = this.historialIntereses.precioMaximo();
        double areaMinimaBuscada = this.historialIntereses.areaMinima();
        //se revisa las publicaciones una por una con un for each
        for (Publicacion pub : carteleraGeneral) {
            Inmueble inm = pub.getInmueble();
            if (inm == null) continue;

            // aqui ya se empieza a validar los filtros
            boolean coincideCiudad = true;
            if (ciudadBuscada != null && !ciudadBuscada.isBlank()) {
                coincideCiudad = inm.getCiudad().equalsIgnoreCase(ciudadBuscada);
            }
            boolean coincideTipo = true;
            if (tipoBuscado != null) {
                coincideTipo = (inm.getTipoInmueble() == tipoBuscado);
            }
            boolean dentroDePrecio = true;
            if (presupuestoMaximo > 0) {
                dentroDePrecio = (inm.getPrecio() <= presupuestoMaximo);
            }
            boolean cumpleArea = true;
            if (areaMinimaBuscada > 0) {
                cumpleArea = (inm.getArea() >= areaMinimaBuscada);
            }

            if (coincideCiudad && coincideTipo && dentroDePrecio && cumpleArea) {
                recomendaciones.add(pub);
            }
        }

        return recomendaciones;
    }

}



