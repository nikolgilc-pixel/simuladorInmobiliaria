package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoNotificacion;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Notificacion {
    private String titulo;
    private String contenido;
    private LocalDateTime fecha;
    private TipoNotificacion tipo;

    public Notificacion(String titulo, String contenido, TipoNotificacion tipo) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.fecha = LocalDateTime.now();
        this.tipo = tipo;
    }
}
