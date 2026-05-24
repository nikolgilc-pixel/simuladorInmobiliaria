package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoNotificacion;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
public class GestorNotificaciones {
    //Atributos

    //Relaciones
    private List<ICanalNotificacion> listaCanales;

    public GestorNotificaciones(ArrayList<ICanalNotificacion> listaCanales){
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
                        + i.getCodigo()
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
                        + i.getCodigo()
                        + " cambió de precio.",
                LocalDateTime.now(),
                TipoNotificacion.CAMBIO_PRECIO
        );

        enviar(destinatario, notificacion);

        return notificacion;
    }

    public Notificacion crearNotificacionInmuebleSimilar(Usuario destinatario, Inmueble i) {
        Notificacion notificacion = crearNotificacion(
            "Nuevo inmueble similar",
            "Encontramos un inmueble similar a "
                    + i.getCodigo(),
            LocalDateTime.now(),
            TipoNotificacion.INMUEBLE_SIMILAR
    );

        enviar(destinatario, notificacion);

        return notificacion;
    }
    public Notificacion crearNotificacionNuevaOferta(Usuario destinatario, Inmueble i, Comprador c, double monto) {
        Notificacion notificacion = crearNotificacion(
                "Nueva Oferta Recibida",
                "¡Atención! El comprador " + c.getNombreCompleto()
                        + " ha realizado una nueva oferta de $" + monto
                        + " para tu inmueble ubicado en " + i.getDireccion() + ".",
                LocalDateTime.now(),
                TipoNotificacion.NUEVA_OFERTA
        );

        // Enviamos la notificación por los canales activos (Correo, WhatsApp, SMS)
        enviar(destinatario, notificacion);

        return notificacion;
    }
    public void enviar(Usuario destinatario, Notificacion alerta){
        //verificamos si el usuario tiene una lista de notificaciones, si no tiene le creamos una para evitar errores
        if (destinatario.getListaNotificaciones() == null) {
            destinatario.setListaNotificaciones(new ArrayList<>());
        }
        //guardamos en el historial de notificaciones del usurio
        destinatario.getListaNotificaciones().add(alerta);

        for(ICanalNotificacion canal : listaCanales){
            canal.enviarNotificacion(destinatario, alerta);
        }
    }
}

