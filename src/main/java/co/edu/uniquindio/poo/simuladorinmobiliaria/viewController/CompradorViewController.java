package co.edu.uniquindio.poo.simuladorinmobiliaria.viewController;

import co.edu.uniquindio.poo.simuladorinmobiliaria.App;
import co.edu.uniquindio.poo.simuladorinmobiliaria.controller.CompradorController;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.*;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CompradorViewController {

    @FXML private Label lblNombreUsuario;

    // Catalogo — filtros
    @FXML private TextField txtCiudad;
    @FXML private ComboBox<TipoInmueble> comboTipo;
    @FXML private TextField txtPrecioMin;
    @FXML private TextField txtPrecioMax;
    @FXML private TextField txtAreaMin;
    @FXML private Label lblMsgBusqueda;

    // Catalogo — tabla
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

    private CompradorController compradorController;
    private Comprador comprador;
    private final NumberFormat nf = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"));

    @FXML
    void initialize() {
        comprador = (Comprador) App.getUsuarioActual();
        compradorController = new CompradorController(App.inmoSmart);
        lblNombreUsuario.setText(comprador.getNombreCompleto() + "  |  " + comprador.getRangoUsuario().name());

        comboTipo.setItems(FXCollections.observableArrayList(TipoInmueble.values()));
        configurarColumnas();

        // Auto-fill del codigo al seleccionar fila en el catalogo
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
            String ciudad    = txtCiudad.getText().trim().isEmpty() ? null : txtCiudad.getText().trim();
            TipoInmueble tipo = comboTipo.getValue();
            double precioMin = txtPrecioMin.getText().trim().isEmpty() ? 0
                    : Double.parseDouble(txtPrecioMin.getText().trim());
            double precioMax = txtPrecioMax.getText().trim().isEmpty() ? 0
                    : Double.parseDouble(txtPrecioMax.getText().trim());
            double areaMin   = txtAreaMin.getText().trim().isEmpty() ? 0
                    : Double.parseDouble(txtAreaMin.getText().trim());

            List<Publicacion> resultados = compradorController.buscarPublicaciones(
                    comprador, ciudad, tipo, precioMin, precioMax, areaMin);
            tablaCatalogo.setItems(FXCollections.observableArrayList(resultados));
            lblMsgBusqueda.setText("Se encontraron " + resultados.size() + " publicacion(es).");
        } catch (NumberFormatException e) {
            lblMsgBusqueda.setText("Error: ingresa valores numericos validos en precio y area.");
        }
    }

    @FXML
    void cargarSugerenciasAction() {
        List<Publicacion> sugerencias = compradorController.obtenerSugerencias(comprador);
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

        String resultado = compradorController.realizarOferta(comprador, codigoPub, monto, nf);
        lblMsgOferta.setText(resultado);
        if (resultado.startsWith("Oferta enviada")) {
            txtMontoOferta.clear();
        }
    }

    @FXML
    void cerrarSesionAction() {
        App.setUsuarioActual(null);
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
