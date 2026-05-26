package co.edu.uniquindio.poo.simuladorinmobiliaria.controller;

import co.edu.uniquindio.poo.simuladorinmobiliaria.App;
import co.edu.uniquindio.poo.simuladorinmobiliaria.SesionGlobal;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.*;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoInmueble;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class CompradorController {

    @FXML private Label lblNombreUsuario;

    // ── Tab 1: Buscar Catálogo — filtros ──────────────────────────────────────
    @FXML private ComboBox<String> comboCiudad;
    @FXML private ComboBox<TipoInmueble> comboTipo;
    @FXML private TextField txtPrecioMin;
    @FXML private TextField txtPrecioMax;
    @FXML private TextField txtAreaMin;
    @FXML private Label lblMsgBusqueda;

    // ── Tab 1: Catálogo — tabla ───────────────────────────────────────────────
    @FXML private TableView<Publicacion> tablaCatalogo;
    @FXML private TableColumn<Publicacion, String> colCatCodigo;
    @FXML private TableColumn<Publicacion, String> colCatDireccion;
    @FXML private TableColumn<Publicacion, String> colCatCiudad;
    @FXML private TableColumn<Publicacion, String> colCatTipo;
    @FXML private TableColumn<Publicacion, String> colCatPrecio;
    @FXML private TableColumn<Publicacion, String> colCatArea;

    // ── Tab 1: Sugerencias ────────────────────────────────────────────────────
    @FXML private TableView<Publicacion> tablaSugerencias;
    @FXML private TableColumn<Publicacion, String> colSugDireccion;
    @FXML private TableColumn<Publicacion, String> colSugPrecio;
    @FXML private TableColumn<Publicacion, String> colSugTipo;

    // ── Tab 1: Visor de imágenes ──────────────────────────────────────────────
    @FXML private ImageView imgVistaPrevia;
    @FXML private Label lblContadorImagenes;
    @FXML private HBox hboxThumbs;
    private List<String> imagenesActuales = new ArrayList<>();
    private int indiceImagen = 0;
    private Publicacion publicacionActual = null;

    // ── Tab 1: Realizar Oferta ────────────────────────────────────────────────
    @FXML private TextField txtCodigoPublicacion;
    @FXML private TextField txtMontoOferta;
    @FXML private Label lblMsgOferta;

    // ── Tab 2: Mis Ofertas ────────────────────────────────────────────────────
    @FXML private TableView<Oferta> tablaOfertasComprador;
    @FXML private TableColumn<Oferta, String> colMisOfInmueble;
    @FXML private TableColumn<Oferta, String> colMisOfCiudad;
    @FXML private TableColumn<Oferta, String> colMisOfMonto;
    @FXML private TableColumn<Oferta, String> colMisOfEstado;
    @FXML private TableColumn<Oferta, String> colMisOfFecha;

    // ── Tab 3: Notificaciones ─────────────────────────────────────────────────
    @FXML private TableView<Notificacion> tablaNotificComprador;
    @FXML private TableColumn<Notificacion, String> colNotTitulo;
    @FXML private TableColumn<Notificacion, String> colNotContenido;
    @FXML private TableColumn<Notificacion, String> colNotTipo;
    @FXML private TableColumn<Notificacion, String> colNotFecha;

    // ── Tab 4: Mis Transacciones ──────────────────────────────────────────────
    @FXML private TableView<Transaccion> tablaTransaccComprador;
    @FXML private TableColumn<Transaccion, String> colTransCInmueble;
    @FXML private TableColumn<Transaccion, String> colTransCCiudad;
    @FXML private TableColumn<Transaccion, String> colTransCValor;
    @FXML private TableColumn<Transaccion, String> colTransCTipo;
    @FXML private TableColumn<Transaccion, String> colTransCFecha;

    // ── Tab 5: Mis Favoritos ──────────────────────────────────────────────────
    @FXML private TableView<Inmueble> tablaFavoritos;
    @FXML private TableColumn<Inmueble, String> colFavDireccion;
    @FXML private TableColumn<Inmueble, String> colFavCiudad;
    @FXML private TableColumn<Inmueble, String> colFavPrecio;
    @FXML private TableColumn<Inmueble, String> colFavTipo;
    @FXML private Label lblMsgFavoritos;

    // ── Tab 6: Mi Perfil ──────────────────────────────────────────────────────
    @FXML private TextField fPerfilNombre;
    @FXML private TextField fPerfilTelefono;
    @FXML private TextField fPerfilEmail;
    @FXML private PasswordField fPerfilPassword;
    @FXML private Label lblMsgPerfil;

    private Comprador comprador;
    private InmoSmart inmoSmart;
    private final NumberFormat nf = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"));

    @FXML
    void initialize() {
        inmoSmart = SesionGlobal.getInmoSmart();
        comprador = (Comprador) SesionGlobal.getUsuarioActual();
        lblNombreUsuario.setText(comprador.getNombreCompleto() + "  |  " + comprador.getRangoUsuario().name());

        comboTipo.setItems(FXCollections.observableArrayList(TipoInmueble.values()));
        cargarCiudadesDisponibles();
        configurarColumnas();

        tablaCatalogo.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, selected) -> {
                    if (selected != null) {
                        publicacionActual = selected;
                        txtCodigoPublicacion.setText(selected.getCodigo());
                        mostrarImagenes(selected.getInmueble().getNombresImagenes());
                    }
                });

        cargarSugerenciasAction();
        cargarMisOfertas();
        cargarNotificaciones();
        cargarTransaccionesComprador();
        cargarFavoritos();
        prefillPerfil();
    }

    private void cargarCiudadesDisponibles() {
        List<String> ciudades = inmoSmart.getCiudadesDisponibles();
        comboCiudad.setItems(FXCollections.observableArrayList(ciudades));
    }

    private void configurarColumnas() {
        colCatCodigo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCodigo().substring(0, 8) + "…"));
        colCatDireccion.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getInmueble().getDireccion()));
        colCatCiudad.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getInmueble().getCiudad()));
        colCatTipo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getInmueble().getTipoInmueble().name()));
        colCatPrecio.setCellValueFactory(d -> new SimpleStringProperty("$" + nf.format(d.getValue().getInmueble().getPrecio())));
        colCatArea.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getInmueble().getArea() + " m²"));

        colSugDireccion.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getInmueble().getDireccion()));
        colSugPrecio.setCellValueFactory(d -> new SimpleStringProperty("$" + nf.format(d.getValue().getInmueble().getPrecio())));
        colSugTipo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getInmueble().getTipoInmueble().name()));

        colMisOfInmueble.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getInmueble().getDireccion()));
        colMisOfCiudad.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getInmueble().getCiudad()));
        colMisOfMonto.setCellValueFactory(d -> new SimpleStringProperty("$" + nf.format(d.getValue().getValor())));
        colMisOfEstado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEstadoOferta().name()));
        colMisOfFecha.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFecha().toString()));

        colNotTitulo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitulo()));
        colNotContenido.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getContenido()));
        colNotTipo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTipo().name()));
        colNotFecha.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFecha().toString()));

        colTransCInmueble.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().inmueble().getDireccion()));
        colTransCCiudad.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().inmueble().getCiudad()));
        colTransCValor.setCellValueFactory(d -> new SimpleStringProperty("$" + nf.format(d.getValue().valorFinal())));
        colTransCTipo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().tipoOperacion().name()));
        colTransCFecha.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().fecha().toString()));

        colFavDireccion.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDireccion()));
        colFavCiudad.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCiudad()));
        colFavPrecio.setCellValueFactory(d -> new SimpleStringProperty("$" + nf.format(d.getValue().getPrecio())));
        colFavTipo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTipoInmueble().name()));
    }

    // ── Visor de imágenes (carrusel inline) ──────────────────────────────────

    private void mostrarImagenes(List<String> nombres) {
        imagenesActuales = nombres != null ? nombres : new ArrayList<>();
        indiceImagen = 0;
        actualizarImagen();
        actualizarThumbs();
    }

    private void actualizarImagen() {
        if (imagenesActuales.isEmpty()) {
            imgVistaPrevia.setImage(null);
            lblContadorImagenes.setText("Sin fotos disponibles");
            return;
        }
        Path ruta = GestorArchivos.getRutaImagen(imagenesActuales.get(indiceImagen));
        imgVistaPrevia.setImage(Files.exists(ruta) ? new Image(ruta.toUri().toString()) : null);
        lblContadorImagenes.setText("Foto " + (indiceImagen + 1) + " de " + imagenesActuales.size());
    }

    private void actualizarThumbs() {
        hboxThumbs.getChildren().clear();
        int limite = Math.min(imagenesActuales.size(), 5);
        for (int i = 0; i < limite; i++) {
            final int fi = i;
            Path ruta = GestorArchivos.getRutaImagen(imagenesActuales.get(i));
            ImageView thumb = new ImageView();
            thumb.setFitWidth(52);
            thumb.setFitHeight(38);
            thumb.setPreserveRatio(true);
            if (Files.exists(ruta)) {
                thumb.setImage(new Image(ruta.toUri().toString(), 52, 38, true, true));
            }
            boolean activa = (i == indiceImagen);
            thumb.setStyle(activa
                    ? "-fx-cursor: hand; -fx-opacity: 1.0; -fx-border-color: #1B5E20; -fx-border-width: 2;"
                    : "-fx-cursor: hand; -fx-opacity: 0.65; -fx-border-color: transparent; -fx-border-width: 2;");
            thumb.setOnMouseClicked(e -> { indiceImagen = fi; actualizarImagen(); actualizarThumbs(); });
            hboxThumbs.getChildren().add(thumb);
        }
        if (imagenesActuales.size() > limite) {
            Label mas = new Label("+" + (imagenesActuales.size() - limite));
            mas.setStyle("-fx-font-size: 11; -fx-text-fill: #888; -fx-padding: 10 0 0 2;");
            hboxThumbs.getChildren().add(mas);
        }
    }

    @FXML
    void imagenAnteriorAction() {
        if (imagenesActuales.isEmpty()) return;
        indiceImagen = (indiceImagen - 1 + imagenesActuales.size()) % imagenesActuales.size();
        actualizarImagen();
        actualizarThumbs();
    }

    @FXML
    void imagenSiguienteAction() {
        if (imagenesActuales.isEmpty()) return;
        indiceImagen = (indiceImagen + 1) % imagenesActuales.size();
        actualizarImagen();
        actualizarThumbs();
    }

    // ── Galería modal de pantalla completa ────────────────────────────────────

    @FXML
    void verGaleriaAction() {
        if (imagenesActuales.isEmpty()) {
            mostrarAlerta("Este inmueble no tiene fotos disponibles.");
            return;
        }
        String titulo = publicacionActual != null
                ? publicacionActual.getInmueble().getDireccion() + " — " + publicacionActual.getInmueble().getCiudad()
                : "Galería";

        Stage gallery = new Stage();
        gallery.setTitle("Galería  |  " + titulo);
        gallery.initModality(Modality.APPLICATION_MODAL);

        // Imagen principal grande
        ImageView mainImg = new ImageView();
        mainImg.setFitWidth(680);
        mainImg.setFitHeight(430);
        mainImg.setPreserveRatio(true);
        mainImg.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.55), 14, 0, 0, 4);");

        Label lblGalCount = new Label();
        lblGalCount.setStyle("-fx-text-fill: #aaa; -fx-font-size: 13;");

        int[] idx = {indiceImagen};

        Runnable syncMain = () -> {
            Path ruta = GestorArchivos.getRutaImagen(imagenesActuales.get(idx[0]));
            mainImg.setImage(Files.exists(ruta) ? new Image(ruta.toUri().toString()) : null);
            lblGalCount.setText("Foto " + (idx[0] + 1) + " de " + imagenesActuales.size());
        };
        syncMain.run();

        Button prev = estiloBotonGal("❮");
        Button next = estiloBotonGal("❯");
        prev.setOnAction(e -> { idx[0] = (idx[0] - 1 + imagenesActuales.size()) % imagenesActuales.size(); syncMain.run(); });
        next.setOnAction(e -> { idx[0] = (idx[0] + 1) % imagenesActuales.size(); syncMain.run(); });

        HBox navRow = new HBox(12, prev, mainImg, next);
        navRow.setAlignment(Pos.CENTER);

        // Strip de thumbnails en la galería
        HBox thumbsRow = new HBox(8);
        thumbsRow.setAlignment(Pos.CENTER);
        for (int i = 0; i < imagenesActuales.size(); i++) {
            final int fi = i;
            Path ruta = GestorArchivos.getRutaImagen(imagenesActuales.get(i));
            ImageView th = new ImageView();
            th.setFitWidth(90);
            th.setFitHeight(62);
            th.setPreserveRatio(true);
            if (Files.exists(ruta)) th.setImage(new Image(ruta.toUri().toString(), 90, 62, true, true));
            th.setStyle("-fx-cursor: hand; -fx-opacity: 0.72;");
            th.setOnMouseClicked(e -> { idx[0] = fi; syncMain.run(); });
            th.setOnMouseEntered(e -> th.setStyle(
                    "-fx-cursor: hand; -fx-opacity: 1.0;" +
                    "-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.6), 8, 0.4, 0, 0);"));
            th.setOnMouseExited(e -> th.setStyle("-fx-cursor: hand; -fx-opacity: 0.72;"));
            thumbsRow.getChildren().add(th);
        }

        ScrollPane thumbsScroll = new ScrollPane(thumbsRow);
        thumbsScroll.setFitToHeight(true);
        thumbsScroll.setPrefHeight(86);
        thumbsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        thumbsScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        thumbsScroll.setStyle("-fx-background-color: transparent; -fx-background: #1a1a2e;");

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-text-fill: white; -fx-font-size: 16; -fx-font-weight: bold;");

        VBox root = new VBox(14, lblTitulo, navRow, lblGalCount, thumbsScroll);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(22));
        root.setStyle("-fx-background-color: #12122a;");

        gallery.setScene(new Scene(root, 830, 660));
        gallery.show();
    }

    private Button estiloBotonGal(String texto) {
        Button b = new Button(texto);
        b.setStyle(
                "-fx-background-color: rgba(255,255,255,0.12); -fx-text-fill: white;" +
                "-fx-font-size: 22; -fx-font-weight: bold; -fx-cursor: hand;" +
                "-fx-background-radius: 50%; -fx-pref-width: 50; -fx-pref-height: 50;");
        b.setOnMouseEntered(e -> b.setStyle(
                "-fx-background-color: rgba(255,255,255,0.28); -fx-text-fill: white;" +
                "-fx-font-size: 22; -fx-font-weight: bold; -fx-cursor: hand;" +
                "-fx-background-radius: 50%; -fx-pref-width: 50; -fx-pref-height: 50;"));
        b.setOnMouseExited(e -> b.setStyle(
                "-fx-background-color: rgba(255,255,255,0.12); -fx-text-fill: white;" +
                "-fx-font-size: 22; -fx-font-weight: bold; -fx-cursor: hand;" +
                "-fx-background-radius: 50%; -fx-pref-width: 50; -fx-pref-height: 50;"));
        return b;
    }

    @FXML
    void buscarAction() {
        try {
            String ciudad = comboCiudad.getValue();
            if (ciudad != null && ciudad.isBlank()) ciudad = null;

            TipoInmueble tipo = comboTipo.getValue();
            double precioMin = txtPrecioMin.getText().trim().isEmpty() ? 0
                    : Double.parseDouble(txtPrecioMin.getText().trim());
            double precioMax = txtPrecioMax.getText().trim().isEmpty() ? 0
                    : Double.parseDouble(txtPrecioMax.getText().trim());
            double areaMin = txtAreaMin.getText().trim().isEmpty() ? 0
                    : Double.parseDouble(txtAreaMin.getText().trim());

            FiltroBusqueda filtro = new FiltroBusqueda(ciudad, tipo, precioMin, precioMax, areaMin);
            List<Publicacion> resultados = inmoSmart.consultarCatalogo(comprador, filtro);
            tablaCatalogo.setItems(FXCollections.observableArrayList(resultados));
            lblMsgBusqueda.setText("Se encontraron " + resultados.size() + " publicacion(es).");

            // Refresca ciudades disponibles por si hubo cambios
            cargarCiudadesDisponibles();
        } catch (NumberFormatException e) {
            lblMsgBusqueda.setText("Error: ingresa valores numericos validos en precio y area.");
        }
    }

    @FXML
    void limpiarFiltrosAction() {
        comboCiudad.setValue(null);
        comboTipo.setValue(null);
        txtPrecioMin.clear();
        txtPrecioMax.clear();
        txtAreaMin.clear();
        lblMsgBusqueda.setText("");
    }

    @FXML
    void cargarSugerenciasAction() {
        List<Publicacion> sugerencias = inmoSmart.obtenerSugerencias(comprador);
        tablaSugerencias.setItems(FXCollections.observableArrayList(sugerencias));
    }

    @FXML
    void ofertarAction() {
        String codigoPub = txtCodigoPublicacion.getText().trim();
        String montoStr  = txtMontoOferta.getText().trim();

        if (codigoPub.isEmpty() || montoStr.isEmpty()) {
            lblMsgOferta.setText("Completa el codigo de publicacion y el monto.");
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(montoStr);
        } catch (NumberFormatException e) {
            lblMsgOferta.setText("El monto debe ser un numero valido.");
            return;
        }

        Optional<Publicacion> pubOpt = inmoSmart.getGestorPublicaciones().buscarPublicacion(codigoPub);
        if (pubOpt.isEmpty()) {
            lblMsgOferta.setText("No se encontro una publicacion activa con ese codigo.");
            return;
        }

        Inmueble inmueble = pubOpt.get().getInmueble();
        if (inmueble.getEstado() != EstadoInmueble.DISPONIBLE) {
            lblMsgOferta.setText("Este inmueble ya no esta disponible.");
            return;
        }
        if (monto <= 0) {
            lblMsgOferta.setText("El monto debe ser mayor a cero.");
            return;
        }

        inmoSmart.tramitarOferta(comprador, inmueble, monto);
        lblMsgOferta.setText("Oferta enviada: $" + nf.format(monto) + " por " + inmueble.getDireccion());
        txtMontoOferta.clear();
        cargarMisOfertas();
        cargarNotificaciones();
    }

    // ── Tab 2: Mis Ofertas ────────────────────────────────────────────────────

    @FXML
    void refrescarOfertasAction() {
        cargarMisOfertas();
    }

    private void cargarMisOfertas() {
        tablaOfertasComprador.setItems(FXCollections.observableArrayList(comprador.getListaOfertas()));
    }

    // ── Tab 3: Notificaciones ─────────────────────────────────────────────────

    @FXML
    void refrescarNotificacionesAction() {
        cargarNotificaciones();
    }

    private void cargarNotificaciones() {
        tablaNotificComprador.setItems(FXCollections.observableArrayList(comprador.getListaNotificaciones()));
    }

    // ── Tab 4: Mis Transacciones ──────────────────────────────────────────────

    @FXML
    void refrescarTransaccionesAction() {
        cargarTransaccionesComprador();
    }

    private void cargarTransaccionesComprador() {
        tablaTransaccComprador.setItems(FXCollections.observableArrayList(comprador.getHistorialTransacciones()));
    }

    // ── Tab 5: Mis Favoritos ──────────────────────────────────────────────────

    @FXML
    void agregarFavoritoAction() {
        Publicacion pub = tablaCatalogo.getSelectionModel().getSelectedItem();
        if (pub == null) {
            lblMsgFavoritos.setText("Selecciona un inmueble del catalogo primero.");
            return;
        }
        Inmueble inmueble = pub.getInmueble();
        if (comprador.getInmueblesFavoritos().contains(inmueble)) {
            lblMsgFavoritos.setText("Este inmueble ya esta en tus favoritos.");
            return;
        }
        comprador.getInmueblesFavoritos().add(inmueble);
        cargarFavoritos();
        lblMsgFavoritos.setText("Inmueble agregado a favoritos.");
    }

    @FXML
    void eliminarFavoritoAction() {
        Inmueble inmueble = tablaFavoritos.getSelectionModel().getSelectedItem();
        if (inmueble == null) {
            lblMsgFavoritos.setText("Selecciona un favorito para eliminarlo.");
            return;
        }
        comprador.getInmueblesFavoritos().remove(inmueble);
        cargarFavoritos();
        lblMsgFavoritos.setText("");
    }

    private void cargarFavoritos() {
        tablaFavoritos.setItems(FXCollections.observableArrayList(comprador.getInmueblesFavoritos()));
    }

    // ── Tab 6: Mi Perfil ──────────────────────────────────────────────────────

    private void prefillPerfil() {
        fPerfilNombre.setText(comprador.getNombreCompleto());
        fPerfilTelefono.setText(comprador.getTelefono());
        fPerfilEmail.setText(comprador.getEmail());
    }

    @FXML
    void guardarPerfilAction() {
        String nombre   = fPerfilNombre.getText().trim();
        String telefono = fPerfilTelefono.getText().trim();
        String email    = fPerfilEmail.getText().trim();
        String password = fPerfilPassword.getText().trim();

        String resultado = inmoSmart.actualizarDatosContacto(comprador, nombre, telefono, email, password);
        if (resultado.startsWith("Error")) {
            lblMsgPerfil.setStyle("-fx-text-fill: #C62828;");
        } else {
            lblMsgPerfil.setStyle("-fx-text-fill: #2E7D32;");
            lblNombreUsuario.setText(comprador.getNombreCompleto() + "  |  " + comprador.getRangoUsuario().name());
            fPerfilPassword.clear();
        }
        lblMsgPerfil.setText(resultado);
    }

    // ── Sesión ────────────────────────────────────────────────────────────────

    @FXML
    void cerrarSesionAction() {
        SesionGlobal.setUsuarioActual(null);
        try {
            App.navegarA("login.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error al cerrar sesion.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
