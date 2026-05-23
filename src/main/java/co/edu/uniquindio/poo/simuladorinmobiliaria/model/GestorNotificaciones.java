package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoNotificacion;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
@Setter
public class GestorNotificaciones {
    //Atributos

    //Relaciones
    private ArrayList<ICanalNotificacion> listaCanales;

    public GestorNotificaciones(ArrayList<ICanalNotificacion> listaCanales) {
        this.listaCanales = listaCanales;
    }

    public Notificacion crearNotificacion(String titulo, String contenido, LocalDateTime fecha, TipoNotificacion tipo) {
        Notificacion notificacion = new Notificacion();
        notificacion.setTitulo(titulo);
        notificacion.setContenido(contenido);
        notificacion.setFecha(fecha);
        notificacion.setTipo(tipo);
        return notificacion;
    }


    public Notificacion crearNotificacionOfertaAceptada(Usuario destinatario, Inmueble i) {
        Notificacion notificacion = crearNotificacion(
                "Oferta aceptada",
                "Tu oferta para el inmueble "
                        + i.getNombre()
                        + " fue aceptada.",
                LocalDateTime.now(),
                TipoNotificacion.OFERTA_ACEPTADA
        );

        enviar(destinatario, notificacion);

        return notificacion;
    }

    public Notificacion crearNotificacionCambioPrecio(Usuario destinatario, Inmueble i) {
        Notificacion notificacion = crearNotificacion(
                "Cambio de precio",
                "El inmueble "
                        + i.getNombre()
                        + " cambió de precio.",
                LocalDateTime.now(),
                TipoNotificacion.CAMBIO_PRECIO
        );

        enviar(destinatario, notificacion);

        return notificacion;
    }

    public Notificacion crearNotificacionInmuebleSimilar(Usuario destinatario, Inmueble i) {Notificacion notificacion = crearNotificacion(
            "Nuevo inmueble similar",
            "Encontramos un inmueble similar a "
                    + i.getNombre(),
            LocalDateTime.now(),
            TipoNotificacion.INMUEBLE_SIMILAR
    );

        enviar(destinatario, notificacion);

        return notificacion;
    }
    public void enviar(Usuario destinatario, Notificacion alerta){
        destinatario.getListaNotificaciones()
                .add(alerta);

        for(ICanalNotificacion canal : listaCanales){

            canal.enviarNotificacion(
                    destinatario,
                    alerta.getContenido()
            );
        }

    }
}

