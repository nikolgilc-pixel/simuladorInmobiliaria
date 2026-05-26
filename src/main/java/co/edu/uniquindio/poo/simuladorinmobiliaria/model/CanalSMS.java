package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import com.twilio.exception.ApiException;
import com.twilio.http.NetworkHttpClient;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class CanalSMS implements ICanalNotificacion {

    private static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    private static final String AUTH_TOKEN  = System.getenv("TWILIO_AUTH_TOKEN");
    private static final String REMITENTE   = System.getenv("TWILIO_REMITENTE");

    private static HttpClientBuilder buildTrustAllBuilder()
            throws NoSuchAlgorithmException, KeyManagementException {
        X509TrustManager trustAll = new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
            public void checkClientTrusted(X509Certificate[] c, String a) {}
            public void checkServerTrusted(X509Certificate[] c, String a) {}
        };
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, new TrustManager[]{trustAll}, new SecureRandom());
        SSLConnectionSocketFactory sslFactory =
                new SSLConnectionSocketFactory(sc, NoopHostnameVerifier.INSTANCE);
        return HttpClients.custom().setSSLSocketFactory(sslFactory);
    }

    @Override
    public void enviarNotificacion(Usuario destinatario, Notificacion notificacion) {
        if (ACCOUNT_SID == null || AUTH_TOKEN == null || REMITENTE == null) {
            System.err.println("[SMS] Variables de entorno Twilio no configuradas. Notificacion omitida.");
            return;
        }
        String telefono = destinatario.getTelefono();
        if (telefono == null || telefono.isBlank()) {
            System.err.println("[SMS] " + destinatario.getNombreCompleto() + " no tiene telefono registrado.");
            return;
        }
        if (!telefono.startsWith("+")) telefono = "+57" + telefono;
        try {
            NetworkHttpClient netClient = new NetworkHttpClient(buildTrustAllBuilder());
            TwilioRestClient restClient = new TwilioRestClient.Builder(ACCOUNT_SID, AUTH_TOKEN)
                    .httpClient(netClient).build();
            Message.creator(new PhoneNumber(telefono), new PhoneNumber(REMITENTE),
                    "InmoSmart: " + notificacion.getContenido()).create(restClient);
            System.out.println("[SMS] Enviado a: " + destinatario.getNombreCompleto());
        } catch (ApiException e) {
            System.err.println("[SMS] Error Twilio: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[SMS] Error inesperado: " + e.getMessage());
        }
    }
}