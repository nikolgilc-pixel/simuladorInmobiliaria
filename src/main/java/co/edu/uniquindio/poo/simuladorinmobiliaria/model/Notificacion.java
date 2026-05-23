package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoNotificacion;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class Notificacion {
    private String titulo;
    private String contenido;
    private LocalDateTime fecha;
    private TipoNotificacion tipo;

}
