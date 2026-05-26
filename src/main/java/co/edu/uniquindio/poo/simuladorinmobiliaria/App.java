package co.edu.uniquindio.poo.simuladorinmobiliaria;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.*;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class App extends Application {

    private static Stage primaryStage;

    // Accesible globalmente por los viewControllers (igual que 'empresa' en el documento)
    public static InmoSmart inmoSmart;
    private static Usuario usuarioActual;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        inmoSmart = new InmoSmart("INMO-2025", new BuscadorInmueblesPublicados());
        cargarDatosSemilla();
        primaryStage.setTitle("InmoSmart — Simulador Inmobiliario");
        navegarA("login.fxml");
    }

    // Los FXML están en resources/co/edu/uniquindio/poo/simuladorinmobiliaria/
    // App.class está en el mismo paquete, así que getResource("nombre.fxml") los encuentra directamente
    public static void navegarA(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static void setUsuarioActual(Usuario usuario) {
        usuarioActual = usuario;
    }

    private void cargarDatosSemilla() {
        Vendedor vendedor = new Vendedor("V001", "Adriana Castaneda", "+573233513359",
                "adrianam.castanedal@uqvirtual.edu.co", "1234", LocalDate.of(2024, 1, 15));
        inmoSmart.getGestorUsuarios().añadirUsuario(vendedor);

        Comprador comprador = new Comprador("C001", "Maria Lopez", "+573177000763",
                "maria@test.com", "1234", LocalDate.of(2024, 3, 10),
                600_000_000, "Bogota", TipoInmueble.APARTAMENTO, 50.0);
        inmoSmart.getGestorUsuarios().añadirUsuario(comprador);

        Inmueble inm1 = inmoSmart.getGestorInmuebles().registrarNuevoInmueble(
                "Av. El Dorado 85-21", "Bogota", 80.0, 350_000_000,
                "Apto moderno con parqueadero y vigilancia", TipoInmueble.APARTAMENTO);
        inmoSmart.vincularInmuebleAVendedor(inm1, vendedor);

        Inmueble inm2 = inmoSmart.getGestorInmuebles().registrarNuevoInmueble(
                "Calle 100 # 15-20", "Bogota", 180.0, 780_000_000,
                "Casa amplia con jardin y piscina", TipoInmueble.CASA);
        inmoSmart.vincularInmuebleAVendedor(inm2, vendedor);

        Inmueble inm3 = inmoSmart.getGestorInmuebles().registrarNuevoInmueble(
                "Carrera 7 # 32-10", "Medellin", 42.0, 195_000_000,
                "Apartaestudio centrico ideal para ejecutivos", TipoInmueble.APARTAMENTO);
        inmoSmart.vincularInmuebleAVendedor(inm3, vendedor);

        inmoSmart.procesarSolicitudPublicacion(vendedor, inm1,
                "Hermoso apartamento en zona exclusiva de Bogota");
        inmoSmart.procesarSolicitudPublicacion(vendedor, inm2,
                "Casa familiar en barrio residencial tranquilo");
        inmoSmart.procesarSolicitudPublicacion(vendedor, inm3,
                "Apartaestudio ideal para arrendar en Medellin");
    }

    public static void main(String[] args) {
        launch();
    }
}
