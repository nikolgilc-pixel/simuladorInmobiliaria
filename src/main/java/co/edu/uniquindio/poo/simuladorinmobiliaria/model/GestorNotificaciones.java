package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoNotificacion;

import java.util.ArrayList;
import java.util.List;

public class GestorNotificaciones {
    private List<ICanalNotificacion> listaCanales;

    public GestorNotificaciones() {
        this.listaCanales = new ArrayList<>();
    }

    // Registra un canal de notificación (inyección vía método de registro — RT03)
    public void agregarCanal(ICanalNotificacion canal) {
        listaCanales.add(canal);
    }

    public Notificacion crearNotificacionNuevaOferta(Usuario destinatario, Inmueble inmueble) {
        Notificacion n = new Notificacion(
                "Nueva oferta recibida",
                "Tienes una nueva oferta sobre el inmueble en " + inmueble.getDireccion()
                        + ", " + inmueble.getCiudad() + ".",
                TipoNotificacion.NUEVA_OFERTA
        );
        enviar(destinatario, n);
        return n;
    }

    public Notificacion crearNotificacionOfertaAceptada(Usuario destinatario, Inmueble inmueble) {
        Notificacion n = new Notificacion(
                "Oferta aceptada",
                "Tu oferta para el inmueble en " + inmueble.getDireccion()
                        + ", " + inmueble.getCiudad() + " fue aceptada.",
                TipoNotificacion.OFERTA_ACEPTADA
        );
        enviar(destinatario, n);
        return n;
    }

    public Notificacion crearNotificacionCambioPrecio(Usuario destinatario, Inmueble inmueble) {
        Notificacion n = new Notificacion(
                "Cambio de precio",
                "El inmueble en " + inmueble.getDireccion()
                        + ", " + inmueble.getCiudad() + " cambió de precio.",
                TipoNotificacion.CAMBIO_PRECIO
        );
        enviar(destinatario, n);
        return n;
    }

    public Notificacion crearNotificacionInmuebleSimilar(Usuario destinatario, Inmueble inmueble) {
        Notificacion n = new Notificacion(
                "Inmueble similar disponible",
                "Encontramos un inmueble similar en " + inmueble.getDireccion()
                        + ", " + inmueble.getCiudad() + ".",
                TipoNotificacion.INMUEBLE_SIMILAR
        );
        enviar(destinatario, n);
        return n;
    }

    // Persiste la notificación en el usuario y la despacha por todos los canales
    public void enviar(Usuario destinatario, Notificacion alerta) {
        destinatario.recibirNotificacion(alerta);
        for (ICanalNotificacion canal : listaCanales) {
            canal.enviarNotificacion(destinatario, alerta);
        }
    }
}
