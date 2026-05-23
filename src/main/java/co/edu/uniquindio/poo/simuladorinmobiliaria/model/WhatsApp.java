package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

public class WhatsApp extends ICanalNotificacion{
    @Override
    public void enviarNotificacion(Usuario destinatario, Notificacion notificacion){
        System.out.println("WhatsApp enviado a : "+ destinatario.getTelefono());
        System.out.println("Mensaje: "+ notificacion.getContenido());
    }

}
