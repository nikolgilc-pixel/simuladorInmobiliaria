package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorReportes {

    public Map<TipoInmueble, Integer> generarReporteTopInmuebles(List<Transaccion> listaTransacciones) {
        Map<TipoInmueble, Integer> reporte = new HashMap<>();
        for (Transaccion t : listaTransacciones) {
            TipoInmueble tipo = t.inmueble().getTipoInmueble();
            reporte.put(tipo, reporte.getOrDefault(tipo, 0) + 1);
        }
        return reporte;
    }

    public Map<String, Integer> generarReporteDemandaCiudad(List<Inmueble> listaInmuebles) {
        Map<String, Integer> reporte = new HashMap<>();
        for (Inmueble i : listaInmuebles) {
            String ciudad = i.getCiudad();
            reporte.put(ciudad, reporte.getOrDefault(ciudad, 0) + 1);
        }
        return reporte;
    }

    // Ordena una copia de la lista de compradores descendente por puntosReputacion
    public List<Usuario> generarReporteCompradoresTop(List<Usuario> compradores) {
        List<Usuario> copia = new ArrayList<>(compradores);
        for (int i = 0; i < copia.size() - 1; i++) {
            for (int j = 0; j < copia.size() - i - 1; j++) {
                if (copia.get(j).getPuntosReputacion() < copia.get(j + 1).getPuntosReputacion()) {
                    Usuario temp = copia.get(j);
                    copia.set(j, copia.get(j + 1));
                    copia.set(j + 1, temp);
                }
            }
        }
        return copia;
    }
}
