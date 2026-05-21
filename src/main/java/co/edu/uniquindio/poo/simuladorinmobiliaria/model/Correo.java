package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

public class Correo implements ICanalNotificacion{

    @Override
    public void enviarNotificacion(Usuario destinatario, Notificacion notificacion){
        System.out.println("Correo enviado a : "+ destinatario.getEmail);
        System.out.println("Mensaje: "+ notificacion.getContenido());
    }

}
