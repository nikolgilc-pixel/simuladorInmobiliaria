package co.edu.uniquindio.poo.simuladorinmobiliaria.viewController;

import co.edu.uniquindio.poo.simuladorinmobiliaria.App;
import co.edu.uniquindio.poo.simuladorinmobiliaria.controller.LoginController;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegistroCompradorViewController {

    @FXML private TextField     fNombre;
    @FXML private TextField     fTelefono;
    @FXML private TextField     fEmail;
    @FXML private PasswordField fPassword;
    @FXML private TextField     fPresupuesto;
    @FXML private TextField     fCiudadInteres;
    @FXML private ComboBox<TipoInmueble> comboTipoInteres;
    @FXML private TextField     fAreaMin;
    @FXML private Label         lblMsg;

    private LoginController loginController;

    @FXML
    void initialize() {
        loginController = new LoginController(App.inmoSmart);
        comboTipoInteres.setItems(FXCollections.observableArrayList(TipoInmueble.values()));
    }

    @FXML
    void registrarAction() {
        String nombre   = fNombre.getText().trim();
        String telefono = fTelefono.getText().trim();
        String email    = fEmail.getText().trim();
        String password = fPassword.getText().trim();
        String ciudad   = fCiudadInteres.getText().trim();
        TipoInmueble tipo = comboTipoInteres.getValue();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()
                || ciudad.isEmpty() || tipo == null) {
            lblMsg.setStyle("-fx-text-fill: #C62828;");
            lblMsg.setText("Nombre, correo, contrasena, ciudad de interes y tipo son obligatorios.");
            return;
        }

        double presupuesto = 0;
        double areaMin = 0;
        try {
            if (!fPresupuesto.getText().trim().isEmpty())
                presupuesto = Double.parseDouble(fPresupuesto.getText().trim());
            if (!fAreaMin.getText().trim().isEmpty())
                areaMin = Double.parseDouble(fAreaMin.getText().trim());
        } catch (NumberFormatException e) {
            lblMsg.setStyle("-fx-text-fill: #C62828;");
            lblMsg.setText("Presupuesto y area minima deben ser numeros validos.");
            return;
        }

        String resultado = loginController.registrarComprador(
                nombre, telefono, email, password, presupuesto, ciudad, tipo, areaMin);

        if (resultado.startsWith("Error")) {
            lblMsg.setStyle("-fx-text-fill: #C62828;");
            lblMsg.setText(resultado);
        } else {
            lblMsg.setStyle("-fx-text-fill: #2E7D32;");
            lblMsg.setText(resultado + " Volviendo al login...");
            try { App.navegarA("login.fxml"); } catch (Exception ex) { /* ignored */ }
        }
    }

    @FXML
    void volverAction() {
        try { App.navegarA("login.fxml"); }
        catch (Exception e) { lblMsg.setText("Error al volver: " + e.getMessage()); }
    }
}
