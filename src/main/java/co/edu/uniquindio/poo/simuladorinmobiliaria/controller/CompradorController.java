package co.edu.uniquindio.poo.simuladorinmobiliaria.controller;

import co.edu.uniquindio.poo.simuladorinmobiliaria.App;
import co.edu.uniquindio.poo.simuladorinmobiliaria.SesionGlobal;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.*;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoInmueble;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class CompradorController {

    @FXML private Label lblNombreUsuario;

    // Catálogo — filtros
    @FXML private TextField txtCiudad;
    @FXML private ComboBox<TipoInmueble> comboTipo;
    @FXML private TextField txtPrecioMin;
    @FXML private TextField txtPrecioMax;
    @FXML private TextField txtAreaMin;
    @FXML private Label lblMsgBusqueda;

    // Catálogo — tabla
    @FXML private TableView<Publicacion> tablaCatalogo;
    @FXML private TableColumn<Publicacion, String> colCatCodigo;
    @FXML private TableColumn<Publicacion, String> colCatDireccion;
    @FXML private TableColumn<Publicacion, String> colCatCiudad;
    @FXML private TableColumn<Publicacion, String> colCatTipo;
    @FXML private TableColumn<Publicacion, String> colCatPrecio;
    @FXML private TableColumn<Publicacion, String> colCatArea;

    // Sugerencias
    @FXML private TableView<Publicacion> tablaSugerencias;
    @FXML private TableColumn<Publicacion, String> colSugDireccion;
    @FXML private TableColumn<Publicacion, String> colSugPrecio;
    @FXML private TableColumn<Publicacion, String> colSugTipo;

    // Oferta
    @FXML private TextField txtCodigoPublicacion;
    @FXML private TextField txtMontoOferta;
    @FXML private Label lblMsgOferta;

    private Comprador comprador;
    private InmoSmart inmoSmart;
    private final NumberFormat nf = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"));

    @FXML
    void initialize() {
        inmoSmart = SesionGlobal.getInmoSmart();
        comprador = (Comprador) SesionGlobal.getUsuarioActual();
        lblNombreUsuario.setText(comprador.getNombreCompleto() + "  |  " + comprador.getRangoUsuario().name());

        comboTipo.setItems(FXCollections.observableArrayList(TipoInmueble.values()));
        configurarColumnas();

        // Auto-fill del código al seleccionar una fila en el catálogo
        tablaCatalogo.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, selected) -> {
                    if (selected != null) {
                        txtCodigoPublicacion.setText(selected.getCodigo());
                    }
                });

        cargarSugerenciasAction();
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
    }

    @FXML
    void buscarAction() {
        try {
            String ciudad = txtCiudad.getText().trim().isEmpty() ? null : txtCiudad.getText().trim();
            TipoInmueble tipo = comboTipo.getValue();
            double precioMin = txtPrecioMin.getText().trim().isEmpty() ? 0
                    : Double.parseDouble(txtPrecioMin.getText().trim());
            // precioMaximo = 0 significa sin límite superior en BuscadorInmueblesPublicados
            double precioMax = txtPrecioMax.getText().trim().isEmpty() ? 0
                    : Double.parseDouble(txtPrecioMax.getText().trim());
            double areaMin = txtAreaMin.getText().trim().isEmpty() ? 0
                    : Double.parseDouble(txtAreaMin.getText().trim());

            FiltroBusqueda filtro = new FiltroBusqueda(ciudad, tipo, precioMin, precioMax, areaMin);
            List<Publicacion> resultados = inmoSmart.consultarCatalogo(comprador, filtro);
            tablaCatalogo.setItems(FXCollections.observableArrayList(resultados));
            lblMsgBusqueda.setText("Se encontraron " + resultados.size() + " publicación(es).");
        } catch (NumberFormatException e) {
            lblMsgBusqueda.setText("Error: ingresa valores numéricos válidos en precio y área.");
        }
    }

    @FXML
    void cargarSugerenciasAction() {
        List<Publicacion> sugerencias = inmoSmart.obtenerSugerencias(comprador);
        tablaSugerencias.setItems(FXCollections.observableArrayList(sugerencias));
    }

    @FXML
    void ofertarAction() {
        String codigoPub = txtCodigoPublicacion.getText().trim();
        String montoStr = txtMontoOferta.getText().trim();

        if (codigoPub.isEmpty() || montoStr.isEmpty()) {
            lblMsgOferta.setText("Completa el código de publicación y el monto.");
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(montoStr);
        } catch (NumberFormatException e) {
            lblMsgOferta.setText("El monto debe ser un número válido.");
            return;
        }

        // Buscar la publicación completa por código (puede ser UUID completo o prefijo de 8 chars)
        Optional<Publicacion> pubOpt = inmoSmart.getGestorPublicaciones().buscarPublicacion(codigoPub);
        if (pubOpt.isEmpty()) {
            lblMsgOferta.setText("No se encontró una publicación activa con ese código.");
            return;
        }

        Inmueble inmueble = pubOpt.get().getInmueble();
        if (inmueble.getEstado() != EstadoInmueble.DISPONIBLE) {
            lblMsgOferta.setText("Este inmueble ya no está disponible.");
            return;
        }
        if (monto <= 0) {
            lblMsgOferta.setText("El monto debe ser mayor a cero.");
            return;
        }

        inmoSmart.tramitarOferta(comprador, inmueble, monto);
        lblMsgOferta.setText("Oferta enviada: $" + nf.format(monto) + " por " + inmueble.getDireccion());
        txtMontoOferta.clear();
    }

    @FXML
    void cerrarSesionAction() {
        SesionGlobal.setUsuarioActual(null);
        try {
            App.navegarA("login.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error al cerrar sesión.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
