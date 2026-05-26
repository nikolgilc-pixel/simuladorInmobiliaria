package co.edu.uniquindio.poo.simuladorinmobiliaria.viewController;

import co.edu.uniquindio.poo.simuladorinmobiliaria.App;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Administrador;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Comprador;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Inmueble;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.InmoSmart;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Usuario;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Vendedor;

import java.text.NumberFormat;
import java.util.Locale;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AdminViewController {

    // Ítem reutilizable para tablas de reportes (hasta 3 columnas)
    public static class ReporteItem {
        private final String a;
        private final String b;
        private final String c;

        public ReporteItem(String a, String b) { this(a, b, ""); }
        public ReporteItem(String a, String b, String c) {
            this.a = a; this.b = b; this.c = c;
        }
        public String getA() { return a; }
        public String getB() { return b; }
        public String getC() { return c; }
    }

    // ── Header ────────────────────────────────────────────────────────────────
    @FXML private Label lblNombreAdmin;

    // ── Tab 1: Crear Vendedor ─────────────────────────────────────────────────
    @FXML private TextField fNombreVendedor;
    @FXML private TextField fTelefonoVendedor;
    @FXML private TextField fEmailVendedor;
    @FXML private PasswordField fPasswordVendedor;
    @FXML private Label lblMsgVendedor;

    // ── Tab 2: Top Tipos de Inmueble ──────────────────────────────────────────
    @FXML private TableView<ReporteItem> tablaTopTipos;
    @FXML private TableColumn<ReporteItem, String> colTipoNombre;
    @FXML private TableColumn<ReporteItem, String> colTipoConteo;

    // ── Tab 3: Demanda por Ciudad ─────────────────────────────────────────────
    @FXML private TableView<ReporteItem> tablaDemandaCiudad;
    @FXML private TableColumn<ReporteItem, String> colDemCiudad;
    @FXML private TableColumn<ReporteItem, String> colDemConteo;

    // ── Tab 4: Top Compradores ────────────────────────────────────────────────
    @FXML private TableView<ReporteItem> tablaTopCompradores;
    @FXML private TableColumn<ReporteItem, String> colTopNombre;
    @FXML private TableColumn<ReporteItem, String> colTopPuntos;
    @FXML private TableColumn<ReporteItem, String> colTopRango;

    // ── Tab 5: Gestión de Usuarios — solo permite eliminar Vendedores ─────────
    @FXML private ComboBox<String> comboFiltroRol;
    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, String> colUsuNombre;
    @FXML private TableColumn<Usuario, String> colUsuEmail;
    @FXML private TableColumn<Usuario, String> colUsuPuntos;
    @FXML private Label lblMsgUsuarios;

    // ── Tab 6: Inventario Disponible ──────────────────────────────────────────
    @FXML private TableView<Inmueble> tablaInventario;
    @FXML private TableColumn<Inmueble, String> colInvDireccion;
    @FXML private TableColumn<Inmueble, String> colInvCiudad;
    @FXML private TableColumn<Inmueble, String> colInvTipo;
    @FXML private TableColumn<Inmueble, String> colInvPrecio;
    @FXML private TableColumn<Inmueble, String> colInvVendedor;

    // ── Tab 7: Mi Perfil — único lugar donde el Admin cambia su contraseña ────
    @FXML private PasswordField fAdminNuevaPassword;
    @FXML private PasswordField fAdminConfirmarPassword;
    @FXML private Label lblMsgPerfil;

    private InmoSmart inmoSmart;
    private Administrador adminActual;
    private final NumberFormat nf = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"));

    @FXML
    void initialize() {
        inmoSmart   = App.inmoSmart;
        adminActual = (Administrador) App.getUsuarioActual();
        lblNombreAdmin.setText(adminActual.getNombreCompleto());
        comboFiltroRol.setItems(FXCollections.observableArrayList("TODOS", "VENDEDORES", "COMPRADORES"));
        comboFiltroRol.setValue("TODOS");
        configurarColumnas();
        cargarReportes();
    }

    private void configurarColumnas() {
        colTipoNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getA()));
        colTipoConteo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getB()));

        colDemCiudad.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getA()));
        colDemConteo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getB()));

        colTopNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getA()));
        colTopPuntos.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getB()));
        colTopRango.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getC()));

        colInvDireccion.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDireccion()));
        colInvCiudad.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCiudad()));
        colInvTipo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTipoInmueble().name()));
        colInvPrecio.setCellValueFactory(d -> new SimpleStringProperty("$" + nf.format(d.getValue().getPrecio())));
        colInvVendedor.setCellValueFactory(d -> {
            Vendedor v = d.getValue().getVendedorAsignado();
            return new SimpleStringProperty(v != null ? v.getNombreCompleto() : "Sin asignar");
        });

        colUsuNombre.setCellValueFactory(d -> {
            Usuario u = d.getValue();
            String rol;
            if (u instanceof Administrador) rol = "ADMIN";
            else if (u instanceof Vendedor)  rol = "Vendedor";
            else if (u instanceof Comprador) rol = "Comprador";
            else rol = "?";
            return new SimpleStringProperty(u.getNombreCompleto() + "  [" + rol + "]");
        });
        colUsuEmail.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEmail()));
        colUsuPuntos.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getPuntosReputacion() + " pts — "
                        + d.getValue().getRangoUsuario().name()));
    }

    @FXML
    void actualizarReportesAction() {
        cargarReportes();
    }

    private void cargarReportes() {
        Map<co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble, Integer> topTipos =
                inmoSmart.generarReporteTopInmuebles();
        List<ReporteItem> itemsTipos = new ArrayList<>();
        topTipos.forEach((tipo, count) ->
                itemsTipos.add(new ReporteItem(tipo.name(), count + " transaccion(es)")));
        tablaTopTipos.setItems(FXCollections.observableArrayList(itemsTipos));

        Map<String, Integer> demanda = inmoSmart.generarReporteDemandaCiudad();
        List<ReporteItem> itemsDem = new ArrayList<>();
        demanda.forEach((ciudad, count) ->
                itemsDem.add(new ReporteItem(ciudad, count + " inmueble(s)")));
        tablaDemandaCiudad.setItems(FXCollections.observableArrayList(itemsDem));

        List<Usuario> compradoresTop = inmoSmart.generarReporteCompradoresTop();
        List<ReporteItem> itemsTop = new ArrayList<>();
        for (Usuario u : compradoresTop) {
            itemsTop.add(new ReporteItem(
                    u.getNombreCompleto(),
                    String.valueOf(u.getPuntosReputacion()),
                    u.getRangoUsuario().name()));
        }
        tablaTopCompradores.setItems(FXCollections.observableArrayList(itemsTop));

        filtrarUsuariosAction();
        cargarInventario();
    }

    // ── Tab 5: Filtrar usuarios por rol ──────────────────────────────────────

    @FXML
    void filtrarUsuariosAction() {
        String filtro = comboFiltroRol.getValue();
        List<Usuario> lista;
        if (filtro == null || filtro.equals("TODOS")) {
            lista = inmoSmart.getGestorUsuarios().getListaUsuarios();
        } else if (filtro.equals("VENDEDORES")) {
            lista = inmoSmart.getGestorUsuarios().listarVendedores();
        } else {
            lista = inmoSmart.getGestorUsuarios().listarCompradores();
        }
        tablaUsuarios.setItems(FXCollections.observableArrayList(lista));
        if (lblMsgUsuarios != null) lblMsgUsuarios.setText("");
    }

    // ── Tab 6: Inventario Disponible ─────────────────────────────────────────

    @FXML
    void actualizarInventarioAction() {
        cargarInventario();
    }

    private void cargarInventario() {
        List<Inmueble> disponibles = inmoSmart.getGestorInmuebles().listarInmueblesDisponibles();
        tablaInventario.setItems(FXCollections.observableArrayList(disponibles));
    }

    // ── Tab 1: Crear Vendedor ─────────────────────────────────────────────────

    @FXML
    void crearVendedorAction() {
        String nombre   = fNombreVendedor.getText().trim();
        String telefono = fTelefonoVendedor.getText().trim();
        String email    = fEmailVendedor.getText().trim();
        String password = fPasswordVendedor.getText().trim();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            lblMsgVendedor.setStyle("-fx-text-fill: #C62828;");
            lblMsgVendedor.setText("Nombre, correo y contrasena son obligatorios.");
            return;
        }

        String resultado = inmoSmart.registrarVendedor(nombre, telefono, email, password);
        if (resultado.startsWith("Error")) {
            lblMsgVendedor.setStyle("-fx-text-fill: #C62828;");
        } else {
            lblMsgVendedor.setStyle("-fx-text-fill: #2E7D32;");
            fNombreVendedor.clear();
            fTelefonoVendedor.clear();
            fEmailVendedor.clear();
            fPasswordVendedor.clear();
            cargarReportes();
        }
        lblMsgVendedor.setText(resultado);
    }

    // ── Tab 5: Eliminar Vendedor (única acción sobre otros usuarios) ──────────

    @FXML
    void eliminarVendedorAction() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            lblMsgUsuarios.setStyle("-fx-text-fill: #C62828;");
            lblMsgUsuarios.setText("Selecciona un usuario de la tabla primero.");
            return;
        }
        if (!(seleccionado instanceof Vendedor)) {
            lblMsgUsuarios.setStyle("-fx-text-fill: #C62828;");
            lblMsgUsuarios.setText("El usuario seleccionado no es un Vendedor.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminacion");
        confirmacion.setHeaderText("Eliminar Vendedor");
        confirmacion.setContentText("¿Seguro que deseas eliminar a "
                + seleccionado.getNombreCompleto() + "?\nEsta accion no se puede deshacer.");

        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            boolean eliminado = inmoSmart.eliminarVendedor(seleccionado.getId());
            if (eliminado) {
                lblMsgUsuarios.setStyle("-fx-text-fill: #2E7D32;");
                lblMsgUsuarios.setText("Vendedor eliminado: " + seleccionado.getNombreCompleto());
                cargarReportes();
            } else {
                lblMsgUsuarios.setStyle("-fx-text-fill: #C62828;");
                lblMsgUsuarios.setText("Error: no se pudo eliminar el vendedor.");
            }
        }
    }

    // ── Tab 6: Mi Perfil — el Admin solo cambia su propia contraseña ──────────

    @FXML
    void cambiarMiContrasenaAction() {
        String nueva     = fAdminNuevaPassword.getText().trim();
        String confirmar = fAdminConfirmarPassword.getText().trim();

        if (nueva.isEmpty()) {
            lblMsgPerfil.setStyle("-fx-text-fill: #C62828;");
            lblMsgPerfil.setText("La nueva contrasena no puede estar vacia.");
            return;
        }
        if (!nueva.equals(confirmar)) {
            lblMsgPerfil.setStyle("-fx-text-fill: #C62828;");
            lblMsgPerfil.setText("Las contrasenas no coinciden.");
            return;
        }

        // Se pasa el propio usuario de la sesión activa — nunca un ID externo
        String resultado = inmoSmart.cambiarContrasena(adminActual, nueva);
        if (resultado.startsWith("Error")) {
            lblMsgPerfil.setStyle("-fx-text-fill: #C62828;");
        } else {
            lblMsgPerfil.setStyle("-fx-text-fill: #2E7D32;");
            fAdminNuevaPassword.clear();
            fAdminConfirmarPassword.clear();
        }
        lblMsgPerfil.setText(resultado);
    }

    // ── Sesión ────────────────────────────────────────────────────────────────

    @FXML
    void cerrarSesionAction() {
        App.setUsuarioActual(null);
        try {
            App.navegarA("login.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error al cerrar sesion: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
