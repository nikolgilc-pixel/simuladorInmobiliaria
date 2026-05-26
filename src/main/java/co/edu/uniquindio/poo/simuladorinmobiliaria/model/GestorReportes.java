package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorReportes {

    public Map<TipoInmueble, Integer> generarReporteTopInmuebles(List<Transaccion> listaTransacciones) {
        Map<TipoInmueble, Integer> reporte = new HashMap<>();
        for (Transaccion t : listaTransacciones) {
            TipoInmueble tipo = t.inmueble().getTipoInmueble();
            if (reporte.containsKey(tipo)) {
                reporte.put(tipo, reporte.get(tipo) + 1);
            } else {
                reporte.put(tipo, 1);
            }
        }
        return reporte;
    }

    public Map<String, Integer> generarReporteDemandaCiudad(List<Inmueble> listaInmuebles) {
        Map<String, Integer> reporte = new HashMap<>();
        for (Inmueble i : listaInmuebles) {
            String ciudad = i.getCiudad();
            if (reporte.containsKey(ciudad)) {
                reporte.put(ciudad, reporte.get(ciudad) + 1);
            } else {
                reporte.put(ciudad, 1);
            }
        }
        return reporte;
    }

    public void generarReporteCompradoresTop(List<Usuario> compradores) {
        // Ordenamiento burbuja descendente por puntosReputacion
        for (int i = 0; i < compradores.size() - 1; i++) {
            for (int j = 0; j < compradores.size() - i - 1; j++) {
                if (compradores.get(j).getPuntosReputacion() < compradores.get(j + 1).getPuntosReputacion()) {
                    Usuario temp = compradores.get(j);
                    compradores.set(j, compradores.get(j + 1));
                    compradores.set(j + 1, temp);
                }
            }
        }
        for (Usuario u : compradores) {
            System.out.println(u.getNombreCompleto() + " | Puntos: " + u.getPuntosReputacion()
                    + " | Rango: " + u.getRangoUsuario());
        }
    }
}
