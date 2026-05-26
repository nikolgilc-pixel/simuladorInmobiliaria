package co.edu.uniquindio.poo.simuladorinmobiliaria;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.*;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.GestorArchivos;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        GestorArchivos.inicializarDirectorios();
        SesionGlobal.setInmoSmart(new InmoSmart("INMO-2025", new BuscadorInmueblesPublicados()));
        cargarDatosSemilla();
        primaryStage.setTitle("InmoSmart — Simulador Inmobiliario");
        navegarA("login.fxml");
    }

    public static void navegarA(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml));
        Scene scene;
        if ("login.fxml".equals(fxml)) {
            scene = new Scene(loader.load(), 900, 600);
            primaryStage.setResizable(false);
        } else {
            scene = new Scene(loader.load());
            primaryStage.setResizable(true);
            primaryStage.sizeToScene();
        }
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void cargarDatosSemilla() {
        InmoSmart inmoSmart = SesionGlobal.getInmoSmart();

        // ── Administrador por defecto ─────────────────────────────────────────
        Administrador admin = new Administrador("A001", "Administrador InmoSmart",
                "+573000000000", "admin@inmosmart.com", "admin123",
                LocalDate.of(2024, 1, 1));
        inmoSmart.getGestorUsuarios().añadirUsuario(admin);

        // ── Vendedores ────────────────────────────────────────────────────────
        Vendedor vendedor = new Vendedor("V001", "Adriana Castañeda", "+573233513359",
                "carlos@test.com", "1234", LocalDate.of(2024, 1, 15));
        inmoSmart.getGestorUsuarios().añadirUsuario(vendedor);

        Vendedor vendedor2 = new Vendedor("V002", "Carlos Ramírez", "+573105559988",
                "carlos2@test.com", "1234", LocalDate.of(2024, 2, 20));
        inmoSmart.getGestorUsuarios().añadirUsuario(vendedor2);

        // ── Compradores ───────────────────────────────────────────────────────
        Comprador comprador = new Comprador("C001", "María López", "+573177000763",
                "maria@test.com", "1234", LocalDate.of(2024, 3, 10),
                600_000_000, "Bogotá", TipoInmueble.APARTAMENTO, 50.0);
        inmoSmart.getGestorUsuarios().añadirUsuario(comprador);

        Comprador comprador2 = new Comprador("C002", "Juan Pérez", "+573115554433",
                "juan@test.com", "1234", LocalDate.of(2024, 4, 5),
                900_000_000, "Medellín", TipoInmueble.CASA, 100.0);
        inmoSmart.getGestorUsuarios().añadirUsuario(comprador2);

        // ── Inmuebles vendedor 1 ──────────────────────────────────────────────
        Inmueble inm1 = inmoSmart.getGestorInmuebles().registrarNuevoInmueble(
                "Av. El Dorado 85-21", "Bogotá", 80.0, 350_000_000,
                "Apto moderno con parqueadero y vigilancia", TipoInmueble.APARTAMENTO);
        inmoSmart.vincularInmuebleAVendedor(inm1, vendedor);
        for (String n : GestorArchivos.generarImagenesEjemplo("inm1", 3)) inm1.getNombresImagenes().add(n);

        Inmueble inm2 = inmoSmart.getGestorInmuebles().registrarNuevoInmueble(
                "Calle 100 # 15-20", "Bogotá", 180.0, 780_000_000,
                "Casa amplia con jardín y piscina", TipoInmueble.CASA);
        inmoSmart.vincularInmuebleAVendedor(inm2, vendedor);
        for (String n : GestorArchivos.generarImagenesEjemplo("inm2", 4)) inm2.getNombresImagenes().add(n);

        Inmueble inm3 = inmoSmart.getGestorInmuebles().registrarNuevoInmueble(
                "Carrera 7 # 32-10", "Medellín", 42.0, 195_000_000,
                "Apartaestudio céntrico ideal para ejecutivos", TipoInmueble.APARTAMENTO);
        inmoSmart.vincularInmuebleAVendedor(inm3, vendedor);
        for (String n : GestorArchivos.generarImagenesEjemplo("inm3", 2)) inm3.getNombresImagenes().add(n);

        // ── Inmuebles vendedor 2 ──────────────────────────────────────────────
        Inmueble inm4 = inmoSmart.getGestorInmuebles().registrarNuevoInmueble(
                "Cra. 43A # 1-50", "Medellín", 210.0, 850_000_000,
                "Casa campestre con vista a la montaña", TipoInmueble.CASA);
        inmoSmart.vincularInmuebleAVendedor(inm4, vendedor2);
        for (String n : GestorArchivos.generarImagenesEjemplo("inm4", 5)) inm4.getNombresImagenes().add(n);

        Inmueble inm5 = inmoSmart.getGestorInmuebles().registrarNuevoInmueble(
                "Calle 72 # 10-30", "Bogotá", 60.0, 280_000_000,
                "Local comercial en zona empresarial", TipoInmueble.LOCAL);
        inmoSmart.vincularInmuebleAVendedor(inm5, vendedor2);
        for (String n : GestorArchivos.generarImagenesEjemplo("inm5", 2)) inm5.getNombresImagenes().add(n);

        // ── Publicaciones ─────────────────────────────────────────────────────
        inmoSmart.procesarSolicitudPublicacion(vendedor, inm1,
                "Hermoso apartamento en zona exclusiva de Bogotá");
        inmoSmart.procesarSolicitudPublicacion(vendedor, inm2,
                "Casa familiar en barrio residencial tranquilo");
        inmoSmart.procesarSolicitudPublicacion(vendedor, inm3,
                "Apartaestudio ideal para arrendar en Medellín");
        inmoSmart.procesarSolicitudPublicacion(vendedor2, inm4,
                "Finca raíz de lujo en El Poblado, Medellín");
        inmoSmart.procesarSolicitudPublicacion(vendedor2, inm5,
                "Local estratégico en Bogotá, excelente flujo peatonal");
    }

    public static void main(String[] args) {
        launch();
    }
}
