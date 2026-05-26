package co.edu.uniquindio.poo.simuladorinmobiliaria.viewController;

import co.edu.uniquindio.poo.simuladorinmobiliaria.App;
import co.edu.uniquindio.poo.simuladorinmobiliaria.controller.LoginController;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Comprador;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Usuario;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Vendedor;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginViewController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;

    private LoginController loginController;

    @FXML
    void initialize() {
        loginController = new LoginController(App.inmoSmart);
    }

    @FXML
    void ingresarAction() {
        String email    = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            mostrarError("Por favor completa todos los campos.");
            return;
        }

        Usuario usuario = loginController.autenticarUsuario(email, password);
        if (usuario == null) {
            mostrarError("Correo o contrasena incorrectos.");
            return;
        }

        App.setUsuarioActual(usuario);
        try {
            if (usuario instanceof Vendedor) {
                App.navegarA("dashboard-vendedor.fxml");
            } else if (usuario instanceof Comprador) {
                App.navegarA("dashboard-comprador.fxml");
            }
        } catch (Exception e) {
            mostrarError("Error al abrir el dashboard: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
    }
}
