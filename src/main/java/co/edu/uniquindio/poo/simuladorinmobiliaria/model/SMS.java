package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

public class SMS extends ICanalNotificacion{
    @Override
    public void enviarNotificacion(Usuario destinatario, Notificacion notificacion){
        System.out.println("Mensaje enviado a : "+ destinatario.getTelefono());
        System.out.println("Mensaje: "+ notificacion.getContenido());
    }

}


