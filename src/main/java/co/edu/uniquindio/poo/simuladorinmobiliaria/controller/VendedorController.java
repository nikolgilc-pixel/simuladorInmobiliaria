package co.edu.uniquindio.poo.simuladorinmobiliaria.controller;

import co.edu.uniquindio.poo.simuladorinmobiliaria.App;
import co.edu.uniquindio.poo.simuladorinmobiliaria.SesionGlobal;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.*;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoOperacion;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VendedorController {

    @FXML private Label lblNombreUsuario;

    // ── Tab 1: Mis Inmuebles ──────────────────────────────────────────────────
    @FXML private TableView<Inmueble> tablaInmuebles;
    @FXML private TableColumn<Inmueble, String> colInmDireccion;
    @FXML private TableColumn<Inmueble, String> colInmCiudad;
    @FXML private TableColumn<Inmueble, String> colInmTipo;
    @FXML private TableColumn<Inmueble, String> colInmPrecio;
    @FXML private TableColumn<Inmueble, String> colInmArea;
    @FXML private TableColumn<Inmueble, String> colInmEstado;

    // ── Tab 2: Publicar Inmueble ──────────────────────────────────────────────
    @FXML private ComboBox<Inmueble> comboInmueblePublicar;
    @FXML private TextArea txtDescripcionPublicacion;
    @FXML private Label lblMsgPublicacion;

    // Sub-sección: retirar publicación existente
    @FXML private ComboBox<Publicacion> comboPublicacionEliminar;
    @FXML private Label lblMsgEliminar;

    // ── Tab 3: Gestionar Ofertas ──────────────────────────────────────────────
    @FXML private ComboBox<Inmueble> comboInmuebleOfertas;
    @FXML private TableView<Oferta> tablaOfertas;
    @FXML private TableColumn<Oferta, String> colOfertaComprador;
    @FXML private TableColumn<Oferta, String> colOfertaMonto;
    @FXML private TableColumn<Oferta, String> colOfertaEstado;
    @FXML private TableColumn<Oferta, String> colOfertaFecha;
    @FXML private Label lblMsgOferta;

    // ── Tab 4: Mis Transacciones ──────────────────────────────────────────────
    @FXML private ComboBox<String> comboFiltroTrans;
    @FXML private TableView<Transaccion> tablaTransacciones;
    @FXML private TableColumn<Transaccion, String> colTransCodigo;
    @FXML private TableColumn<Transaccion, String> colTransComprador;
    @FXML private TableColumn<Transaccion, String> colTransInmueble;
    @FXML private TableColumn<Transaccion, String> colTransValor;
    @FXML private TableColumn<Transaccion, String> colTransTipo;

    // ── Tab 5: Notificaciones ─────────────────────────────────────────────────
    @FXML private TableView<Notificacion> tablaNotifVendedor;
    @FXML private TableColumn<Notificacion, String> colNotVTitulo;
    @FXML private TableColumn<Notificacion, String> colNotVContenido;
    @FXML private TableColumn<Notificacion, String> colNotVTipo;
    @FXML private TableColumn<Notificacion, String> colNotVFecha;

    // ── Tab 6: Mi Perfil ──────────────────────────────────────────────────────
    @FXML private TextField fVendPerfilNombre;
    @FXML private TextField fVendPerfilTelefono;
    @FXML private TextField fVendPerfilEmail;
    @FXML private PasswordField fVendPerfilPassword;
    @FXML private Label lblMsgVendPerfil;
    @FXML private Label lblVendidas;
    @FXML private Label lblArrendadas;

    private Vendedor vendedor;
    private InmoSmart inmoSmart;
    private final NumberFormat nf = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"));

    @FXML
    void initialize() {
        inmoSmart = SesionGlobal.getInmoSmart();
        vendedor = (Vendedor) SesionGlobal.getUsuarioActual();
        lblNombreUsuario.setText(vendedor.getNombreCompleto() + "  |  " + vendedor.getRangoUsuario().name());
        configurarColumnas();
        configurarCombos();
        cargarDatos();
        prefillPerfil();
    }

    private void configurarColumnas() {
        colInmDireccion.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDireccion()));
        colInmCiudad.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCiudad()));
        colInmTipo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTipoInmueble().name()));
        colInmPrecio.setCellValueFactory(d -> new SimpleStringProperty("$" + nf.format(d.getValue().getPrecio())));
        colInmArea.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getArea() + " m²"));
        colInmEstado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEstado().name()));

        colOfertaComprador.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getComprador().getNombreCompleto()));
        colOfertaMonto.setCellValueFactory(d -> new SimpleStringProperty("$" + nf.format(d.getValue().getValor())));
        colOfertaEstado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEstadoOferta().name()));
        colOfertaFecha.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFecha().toString()));

        colTransCodigo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().codigo().substring(0, 8) + "…"));
        colTransComprador.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().comprador().getNombreCompleto()));
        colTransInmueble.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().inmueble().getDireccion()));
        colTransValor.setCellValueFactory(d -> new SimpleStringProperty("$" + nf.format(d.getValue().valorFinal())));
        colTransTipo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().tipoOperacion().name()));

        colNotVTitulo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitulo()));
        colNotVContenido.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getContenido()));
        colNotVTipo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTipo().name()));
        colNotVFecha.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFecha().toString()));
    }

    private void configurarCombos() {
        StringConverter<Inmueble> convInm = new StringConverter<>() {
            @Override public String toString(Inmueble i) {
                return i == null ? "" : i.getDireccion() + " — " + i.getCiudad() + " (" + i.getEstado().name() + ")";
            }
            @Override public Inmueble fromString(String s) { return null; }
        };
        comboInmueblePublicar.setConverter(convInm);
        comboInmuebleOfertas.setConverter(convInm);

        StringConverter<Publicacion> convPub = new StringConverter<>() {
            @Override public String toString(Publicacion p) {
                return p == null ? "" : p.getInmueble().getDireccion() + " — " + p.getInmueble().getCiudad();
            }
            @Override public Publicacion fromString(String s) { return null; }
        };
        comboPublicacionEliminar.setConverter(convPub);
    }

    private void cargarDatos() {
        List<Inmueble> inmuebles = vendedor.getListaInmuebles();
        tablaInmuebles.setItems(FXCollections.observableArrayList(inmuebles));
        comboInmueblePublicar.setItems(FXCollections.observableArrayList(inmuebles));
        comboInmuebleOfertas.setItems(FXCollections.observableArrayList(inmuebles));
        comboPublicacionEliminar.setItems(FXCollections.observableArrayList(vendedor.getListaPublicaciones()));

        comboFiltroTrans.setItems(FXCollections.observableArrayList("TODAS", "VENTA", "ARRIENDO"));
        if (comboFiltroTrans.getValue() == null) comboFiltroTrans.setValue("TODAS");
        List<Transaccion> transacciones = inmoSmart.getGestorTransacciones()
                .listarTransaccionesPorVendedor(vendedor.getId());
        tablaTransacciones.setItems(FXCollections.observableArrayList(transacciones));

        tablaNotifVendedor.setItems(FXCollections.observableArrayList(vendedor.getListaNotificaciones()));
    }

    @FXML
    void mostrarDialogoRegistroAction() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Registrar Nuevo Inmueble");
        dialog.setHeaderText("Ingresa los datos del inmueble");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-padding: 20;");

        TextField fDireccion = new TextField(); fDireccion.setPromptText("Ej: Calle 50 # 20-10");
        TextField fCiudad    = new TextField(); fCiudad.setPromptText("Ej: Bogota");
        TextField fArea      = new TextField(); fArea.setPromptText("Ej: 80.5");
        TextField fPrecio    = new TextField(); fPrecio.setPromptText("Ej: 350000000");
        TextField fDesc      = new TextField(); fDesc.setPromptText("Descripcion breve");
        ComboBox<TipoInmueble> fTipo = new ComboBox<>();
        fTipo.setItems(FXCollections.observableArrayList(TipoInmueble.values()));

        List<File> imagenesSeleccionadas = new ArrayList<>();
        Label lblImagenesCount = new Label("Sin imagenes seleccionadas");
        lblImagenesCount.setStyle("-fx-font-size: 11; -fx-text-fill: #666;");

        FlowPane panelThumbs = new FlowPane(8, 8);
        panelThumbs.setPrefWidth(340);
        panelThumbs.setMinHeight(0);
        panelThumbs.setStyle("-fx-padding: 4 0 0 0;");

        Button btnImagenes = new Button("Seleccionar Fotos");
        btnImagenes.setStyle("-fx-background-color: #1565C0; -fx-text-fill: white; -fx-cursor: hand;");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagenes del inmueble");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imagenes", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        btnImagenes.setOnAction(ev -> {
            List<File> files = fileChooser.showOpenMultipleDialog(btnImagenes.getScene().getWindow());
            if (files != null) {
                imagenesSeleccionadas.clear();
                imagenesSeleccionadas.addAll(files);
                lblImagenesCount.setText(files.size() + " foto(s) seleccionada(s)");
                panelThumbs.getChildren().clear();
                for (File f : files) {
                    try {
                        ImageView thumb = new ImageView(
                                new Image(f.toURI().toString(), 88, 66, true, true));
                        thumb.setStyle(
                                "-fx-border-color: #ccc; -fx-border-width: 1;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 4, 0, 0, 2);");
                        panelThumbs.getChildren().add(thumb);
                    } catch (Exception ignore) {}
                }
                dialog.getDialogPane().getScene().getWindow().sizeToScene();
            }
        });

        grid.add(new Label("Direccion:"),    0, 0); grid.add(fDireccion, 1, 0);
        grid.add(new Label("Ciudad:"),       0, 1); grid.add(fCiudad,    1, 1);
        grid.add(new Label("Area (m2):"),    0, 2); grid.add(fArea,      1, 2);
        grid.add(new Label("Precio COP:"),   0, 3); grid.add(fPrecio,    1, 3);
        grid.add(new Label("Descripcion:"),  0, 4); grid.add(fDesc,      1, 4);
        grid.add(new Label("Tipo:"),         0, 5); grid.add(fTipo,      1, 5);
        grid.add(new Label("Fotos:"),        0, 6); grid.add(btnImagenes,    1, 6);
        grid.add(new Label(""),              0, 7); grid.add(lblImagenesCount, 1, 7);
        grid.add(new Label("Vista previa:"), 0, 8); grid.add(panelThumbs, 1, 8);

        dialog.setResizable(true);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            if (result != ButtonType.OK) return;
            try {
                String dir    = fDireccion.getText().trim();
                String ciudad = fCiudad.getText().trim();
                double area   = Double.parseDouble(fArea.getText().trim());
                double precio = Double.parseDouble(fPrecio.getText().trim());
                String desc   = fDesc.getText().trim();
                TipoInmueble tipo = fTipo.getValue();

                if (dir.isEmpty() || ciudad.isEmpty() || tipo == null) {
                    mostrarAlerta("Todos los campos son obligatorios.");
                    return;
                }
                Inmueble nuevo = inmoSmart.getGestorInmuebles()
                        .registrarNuevoInmueble(dir, ciudad, area, precio, desc, tipo);
                copiarImagenes(imagenesSeleccionadas, nuevo);
                inmoSmart.vincularInmuebleAVendedor(nuevo, vendedor);
                cargarDatos();
            } catch (NumberFormatException ex) {
                mostrarAlerta("Area y precio deben ser numeros validos.");
            }
        });
    }

    @FXML
    void cambiarPrecioAction() {
        Inmueble inmueble = tablaInmuebles.getSelectionModel().getSelectedItem();
        if (inmueble == null) {
            mostrarAlerta("Selecciona un inmueble de la tabla antes de cambiar el precio.");
            return;
        }
        TextInputDialog dialog = new TextInputDialog(String.valueOf((long) inmueble.getPrecio()));
        dialog.setTitle("Cambiar Precio");
        dialog.setHeaderText("Inmueble: " + inmueble.getDireccion() + ", " + inmueble.getCiudad());
        dialog.setContentText("Nuevo precio (COP):");
        dialog.showAndWait().ifPresent(input -> {
            try {
                double nuevoPrecio = Double.parseDouble(input.trim());
                String resultado = inmoSmart.actualizarPrecioInmueble(inmueble, nuevoPrecio);
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Precio Actualizado");
                info.setHeaderText(null);
                info.setContentText(resultado);
                info.showAndWait();
                cargarDatos();
            } catch (NumberFormatException ex) {
                mostrarAlerta("Ingresa un numero valido para el precio.");
            }
        });
    }

    private void copiarImagenes(List<File> archivos, Inmueble destino) {
        for (File f : archivos) {
            String nombre = System.currentTimeMillis() + "_" + f.getName();
            try {
                Files.copy(f.toPath(), GestorArchivos.getRutaImagen(nombre),
                        StandardCopyOption.REPLACE_EXISTING);
                destino.getNombresImagenes().add(nombre);
            } catch (IOException ex) {
                System.err.println("[VendedorController] No se pudo copiar: " + ex.getMessage());
            }
        }
    }

    @FXML
    void publicarInmuebleAction() {
        Inmueble inmueble   = comboInmueblePublicar.getValue();
        String descripcion  = txtDescripcionPublicacion.getText().trim();

        if (inmueble == null) {
            lblMsgPublicacion.setText("Selecciona un inmueble.");
            return;
        }
        if (descripcion.isEmpty()) {
            lblMsgPublicacion.setText("Escribe una descripcion para la publicacion.");
            return;
        }
        String resultado = inmoSmart.procesarSolicitudPublicacion(vendedor, inmueble, descripcion);
        lblMsgPublicacion.setText(resultado);
        txtDescripcionPublicacion.clear();
        cargarDatos();
    }

    @FXML
    void eliminarPublicacionAction() {
        Publicacion pub = comboPublicacionEliminar.getValue();
        if (pub == null) {
            lblMsgEliminar.setText("Selecciona una publicacion a retirar.");
            return;
        }
        String resultado = inmoSmart.procesarEliminacionPublicacion(vendedor, pub.getCodigo());
        lblMsgEliminar.setText(resultado);
        comboPublicacionEliminar.setValue(null);
        cargarDatos();
    }

    @FXML
    void cargarOfertasAction() {
        Inmueble inmueble = comboInmuebleOfertas.getValue();
        if (inmueble == null) return;
        List<Oferta> pendientes = vendedor.verOfertasPendientes(inmueble);
        tablaOfertas.setItems(FXCollections.observableArrayList(pendientes));
        lblMsgOferta.setText("");
    }

    @FXML
    void aceptarVentaAction() {
        procesarCierre(TipoOperacion.VENTA);
    }

    @FXML
    void aceptarArriendoAction() {
        procesarCierre(TipoOperacion.ARRIENDO);
    }

    private void procesarCierre(TipoOperacion tipo) {
        Oferta oferta = tablaOfertas.getSelectionModel().getSelectedItem();
        if (oferta == null) {
            lblMsgOferta.setText("Selecciona una oferta de la tabla primero.");
            return;
        }
        inmoSmart.procesarCierreOferta(oferta, tipo);
        lblMsgOferta.setText("Operacion de " + tipo.name() + " completada exitosamente.");
        cargarDatos();
        cargarOfertasAction();
    }

    @FXML
    void rechazarOfertaAction() {
        Oferta oferta = tablaOfertas.getSelectionModel().getSelectedItem();
        if (oferta == null) {
            lblMsgOferta.setText("Selecciona una oferta de la tabla primero.");
            return;
        }
        vendedor.rechazarOferta(oferta);
        lblMsgOferta.setText("Oferta rechazada.");
        cargarOfertasAction();
    }

    @FXML
    void refrescarNotificacionesVendAction() {
        tablaNotifVendedor.setItems(FXCollections.observableArrayList(vendedor.getListaNotificaciones()));
    }

    // ── Tab 4: filtrar transacciones ─────────────────────────────────────────

    @FXML
    void filtrarTransaccionesAction() {
        String filtro = comboFiltroTrans.getValue();
        List<Transaccion> todas = inmoSmart.getGestorTransacciones()
                .listarTransaccionesPorVendedor(vendedor.getId());
        List<Transaccion> resultado = new ArrayList<>();
        for (Transaccion t : todas) {
            if (filtro == null || filtro.equals("TODAS") || t.tipoOperacion().name().equals(filtro)) {
                resultado.add(t);
            }
        }
        tablaTransacciones.setItems(FXCollections.observableArrayList(resultado));
    }

    // ── Tab 6: Mi Perfil ──────────────────────────────────────────────────────

    private void prefillPerfil() {
        fVendPerfilNombre.setText(vendedor.getNombreCompleto());
        fVendPerfilTelefono.setText(vendedor.getTelefono());
        fVendPerfilEmail.setText(vendedor.getEmail());
        lblVendidas.setText("Inmuebles vendidos: " + vendedor.getTotalInmueblesVendidos());
        lblArrendadas.setText("Inmuebles arrendados: " + vendedor.getTotalInmueblesArrendados());
    }

    @FXML
    void guardarPerfilVendAction() {
        String nombre   = fVendPerfilNombre.getText().trim();
        String telefono = fVendPerfilTelefono.getText().trim();
        String email    = fVendPerfilEmail.getText().trim();
        String password = fVendPerfilPassword.getText().trim();

        String resultado = inmoSmart.actualizarDatosContacto(vendedor, nombre, telefono, email, password);
        if (resultado.startsWith("Error")) {
            lblMsgVendPerfil.setStyle("-fx-text-fill: #C62828;");
        } else {
            lblMsgVendPerfil.setStyle("-fx-text-fill: #2E7D32;");
            lblNombreUsuario.setText(vendedor.getNombreCompleto() + "  |  " + vendedor.getRangoUsuario().name());
            fVendPerfilPassword.clear();
            lblVendidas.setText("Inmuebles vendidos: " + vendedor.getTotalInmueblesVendidos());
            lblArrendadas.setText("Inmuebles arrendados: " + vendedor.getTotalInmueblesArrendados());
        }
        lblMsgVendPerfil.setText(resultado);
    }

    // ── Sesión ────────────────────────────────────────────────────────────────

    @FXML
    void cerrarSesionAction() {
        SesionGlobal.setUsuarioActual(null);
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
