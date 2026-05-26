package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Administrador extends Usuario {
    public Administrador(String id, String nombreCompleto, String telefono,
                         String email, String password, LocalDate fechaRegistro) {
        super(id, nombreCompleto, telefono, email, password, fechaRegistro);
    }
}
