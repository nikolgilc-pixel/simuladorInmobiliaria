package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoNotificacion;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
@Getter
@Setter
public class Notificacion {
    private String titulo;
    private String contenido;
    private Instant fecha;
    private TipoNotificacion tipo;

    public Notificacion(String titulo, String contenido, TipoNotificacion tipo) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.fecha = Instant.now();
        this.tipo = tipo;
    }
}
