package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GestorArchivos {

    private static final Path DIRECTORIO_IMAGENES =
            Paths.get(System.getProperty("user.home"), ".inmosmart", "imagenes");

    public static void inicializarDirectorios() {
        if (!Files.exists(DIRECTORIO_IMAGENES)) {
            try {
                Files.createDirectories(DIRECTORIO_IMAGENES);
            } catch (IOException e) {
                System.err.println("[GestorArchivos] No se pudo crear directorio: " + e.getMessage());
            }
        }
    }

    public static Path getRutaImagen(String nombreImagen) {
        return DIRECTORIO_IMAGENES.resolve(nombreImagen);
    }

    /**
     * Genera N imágenes placeholder con gradientes de color para la semilla de datos.
     * Las imágenes solo se crean si no existen (idempotente al reclonear el proyecto).
     */
    public static String[] generarImagenesEjemplo(String prefijo, int cantidad) {
        String[][] paletas = {
            {"#2C5F8A", "#5090C8", "SALA"},
            {"#27744D", "#4DB37A", "FACHADA"},
            {"#B05A20", "#E88040", "JARDIN"},
            {"#6B3FA0", "#9E70D0", "COCINA"},
            {"#8B2020", "#C94040", "TERRAZA"}
        };
        String[] nombres = new String[cantidad];
        for (int i = 0; i < cantidad; i++) {
            String[] pal = paletas[i % paletas.length];
            String nombre = prefijo + "_" + (i + 1) + ".png";
            Path destino = getRutaImagen(nombre);
            if (!Files.exists(destino)) {
                crearPlaceholder(destino, pal[0], pal[1], pal[2]);
            }
            nombres[i] = nombre;
        }
        return nombres;
    }

    private static void crearPlaceholder(Path destino, String hex1, String hex2, String etiqueta) {
        try {
            int w = 640, h = 400;
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = img.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            GradientPaint grad = new GradientPaint(0, 0, Color.decode(hex1), w, h, Color.decode(hex2));
            g.setPaint(grad);
            g.fillRect(0, 0, w, h);

            // Ventanas decorativas
            g.setColor(new Color(255, 255, 255, 55));
            g.fillRoundRect(w / 2 - 90, h / 2 - 70, 75, 95, 10, 10);
            g.fillRoundRect(w / 2 + 15, h / 2 - 70, 75, 95, 10, 10);
            g.fillRoundRect(w / 2 - 50, h / 2 + 35, 100, 12, 6, 6);

            // Marco exterior
            g.setColor(new Color(255, 255, 255, 100));
            g.setStroke(new BasicStroke(4f));
            g.drawRoundRect(14, 14, w - 28, h - 28, 18, 18);

            // Píldora con la etiqueta
            g.setColor(new Color(0, 0, 0, 140));
            g.fillRoundRect(w / 2 - 110, h - 68, 220, 46, 24, 24);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 24));
            FontMetrics fm = g.getFontMetrics();
            g.drawString(etiqueta, (w - fm.stringWidth(etiqueta)) / 2, h - 37);

            // Marca de agua
            g.setColor(new Color(255, 255, 255, 80));
            g.setFont(new Font("SansSerif", Font.PLAIN, 14));
            g.drawString("InmoSmart", 26, 44);

            g.dispose();
            ImageIO.write(img, "png", destino.toFile());
        } catch (IOException e) {
            System.err.println("[GestorArchivos] No se pudo generar placeholder: " + e.getMessage());
        }
    }
}
