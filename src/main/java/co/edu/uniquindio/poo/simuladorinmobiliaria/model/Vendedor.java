package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoOferta;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Vendedor extends Usuario {
    private int totalInmueblesVendidos;
    private int totalInmueblesArrendados;
    private List<Inmueble> listaInmuebles;
    private List<Publicacion> listaPublicaciones;

    public Vendedor(String id, String nombreCompleto, String telefono, String email,
                    String password, LocalDate fechaRegistro) {
        super(id, nombreCompleto, telefono, email, password, fechaRegistro);
        this.totalInmueblesVendidos = 0;
        this.totalInmueblesArrendados = 0;
        this.listaInmuebles = new ArrayList<>();
        this.listaPublicaciones = new ArrayList<>();
    }

    // Retorna las ofertas PENDIENTES de un inmueble para que el vendedor las revise
    public List<Oferta> verOfertasPendientes(Inmueble inmueble) {
        List<Oferta> pendientes = new ArrayList<>();
        for (Oferta o : inmueble.getListaOfertas()) {
            if (o.getEstadoOferta() == EstadoOferta.PENDIENTE) {
                pendientes.add(o);
            }
        }
        return pendientes;
    }

    public void aceptarOferta(Oferta oferta) {
        oferta.actualizarEstado(EstadoOferta.ACEPTADA);
        totalInmueblesVendidos++;
    }

    public void rechazarOferta(Oferta oferta) {
        oferta.actualizarEstado(EstadoOferta.RECHAZADA);
    }

    // Llamado por InmoSmart al cerrar un arriendo
    public void registrarArriendo() {
        totalInmueblesArrendados++;
    }
}
