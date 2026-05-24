package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorReportes {
    private InmoSmart ownedByInmoSmart;

        // Del diagrama: generarReporteTopInmuebles(List<Transaccion>): Map<TipoInmueble, Integer>
        public Map<TipoInmueble, Integer> generarReporteTopInmuebles(List<Transaccion> listaTransacciones) {
            Map<TipoInmueble, Integer> reporte = new HashMap<>();
            for (Transaccion t : listaTransacciones) {
                TipoInmueble tipo = t.inmueble().getTipoInmueble();
                reporte.put(tipo, reporte.getOrDefault(tipo, 0) + 1);
            }
            return reporte;
        }

        // Del diagrama: generarReporteDemandaCiudad(List<Inmueble>): Map<String, Integer>
        public Map<String, Integer> generarReporteDemandaCiudad(List<Inmueble> listaInmuebles) {
            Map<String, Integer> reporte = new HashMap<>();
            for (Inmueble i : listaInmuebles) {
                String ciudad = i.getCiudad();
                reporte.put(ciudad, reporte.getOrDefault(ciudad, 0) + 1);
            }
            return reporte;
        }

        // Del diagrama: generarReporteCompradoresTop(List<Comprador>): void
        public void generarReporteCompradoresTop(List<Comprador> compradores) {
            compradores.sort((a, b) -> b.getPuntosUsuario() - a.getPuntosUsuario());
            System.out.println("=== TOP COMPRADORES ===");
            for (Comprador c : compradores)
                System.out.println(c.getNombreCompleto() + " - " + c.getPuntosUsuario() + " pts");
        }
    }



