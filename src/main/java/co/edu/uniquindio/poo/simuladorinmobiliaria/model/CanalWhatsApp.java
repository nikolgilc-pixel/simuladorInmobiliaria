package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

public class CanalWhatsApp implements ICanalNotificacion {

    @Override
    public void enviarNotificacion(Usuario destinatario, Notificacion notificacion) {
        System.out.println("[WHATSAPP] Para: " + destinatario.getTelefono());
        System.out.println("  Mensaje: " + notificacion.getContenido());
    }
}
