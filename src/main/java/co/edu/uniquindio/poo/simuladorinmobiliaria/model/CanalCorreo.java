package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

public class CanalCorreo implements ICanalNotificacion {

    @Override
    public void enviarNotificacion(Usuario destinatario, Notificacion notificacion) {
        System.out.println("[CORREO] Para: " + destinatario.getEmail());
        System.out.println("  Asunto: " + notificacion.getTitulo());
        System.out.println("  Mensaje: " + notificacion.getContenido());
    }
}
