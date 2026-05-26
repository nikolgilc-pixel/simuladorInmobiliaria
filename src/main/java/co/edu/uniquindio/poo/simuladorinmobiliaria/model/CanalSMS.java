package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class CanalSMS implements ICanalNotificacion {

    @Override
    public void enviarNotificacion(Usuario destinatario, Notificacion notificacion) {
        String accountSid = System.getenv("TWILIO_ACCOUNT_SID");
        String authToken  = System.getenv("TWILIO_AUTH_TOKEN");

        if (accountSid == null || authToken == null) {
            System.err.println("[SMS] Variables de entorno TWILIO_ACCOUNT_SID / TWILIO_AUTH_TOKEN no configuradas.");
            return;
        }

        String telefono = destinatario.getTelefono();
        if (telefono == null || telefono.isBlank()) {
            System.err.println("[SMS] El usuario " + destinatario.getNombreCompleto() + " no tiene teléfono registrado.");
            return;
        }
        if (!telefono.startsWith("+")) {
            telefono = "+57" + telefono;
        }

        try {
            Twilio.init(accountSid, authToken);

            Message.creator(
                    new PhoneNumber(telefono),
                    new PhoneNumber("+13613148266"),
                    "¡Hola, te contactamos desde InmoSmart Inmobiliaria! " + notificacion.getContenido()
            ).create();

            System.out.println("[SMS] Mensaje enviado exitosamente a: " + destinatario.getNombreCompleto());

        } catch (ApiException e) {
            System.err.println("Error al enviar SMS a " + destinatario.getNombreCompleto() + ": " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado al enviar SMS a " + destinatario.getNombreCompleto() + ": " + e.getMessage());
        }
    }
}
