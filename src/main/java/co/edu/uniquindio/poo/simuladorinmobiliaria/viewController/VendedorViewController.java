package co.edu.uniquindio.poo.simuladorinmobiliaria.viewController;

import co.edu.uniquindio.poo.simuladorinmobiliaria.App;
import co.edu.uniquindio.poo.simuladorinmobiliaria.controller.VendedorController;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.*;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoOperacion;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class VendedorViewController {

    @FXML private Label lblNombreUsuario;

    // Tab Mis Inmuebles
    @FXML private TableView<Inmueble> tablaInmuebles;
    @FXML private TableColumn<Inmueble, String> colInmDireccion;
    @FXML private TableColumn<Inmueble, String> colInmCiudad;
    @FXML private TableColumn<Inmueble, String> colInmTipo;
    @FXML private TableColumn<Inmueble, String> colInmPrecio;
    @FXML private TableColumn<Inmueble, String> colInmArea;
    @FXML private TableColumn<Inmueble, String> colInmEstado;

    // Tab Publicar
    @FXML private ComboBox<Inmueble> comboInmueblePublicar;
    @FXML private TextArea txtDescripcionPublicacion;
    @FXML private Label lblMsgPublicacion;

    // Tab Gestionar Ofertas
    @FXML private ComboBox<Inmueble> comboInmuebleOfertas;
    @FXML private TableView<Oferta> tablaOfertas;
    @FXML private TableColumn<Oferta, String> colOfertaComprador;
    @FXML private TableColumn<Oferta, String> colOfertaMonto;
    @FXML private TableColumn<Oferta, String> colOfertaEstado;
    @FXML private TableColumn<Oferta, String> colOfertaFecha;
    @FXML private Label lblMsgOferta;

    // Tab Transacciones
    @FXML private TableView<Transaccion> tablaTransacciones;
    @FXML private TableColumn<Transaccion, String> colTransCodigo;
    @FXML private TableColumn<Transaccion, String> colTransComprador;
    @FXML private TableColumn<Transaccion, String> colTransInmueble;
    @FXML private TableColumn<Transaccion, String> colTransValor;
    @FXML private TableColumn<Transaccion, String> colTransTipo;

    private VendedorController vendedorController;
    private Vendedor vendedor;
    private final NumberFormat nf = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"));

    @FXML
    void initialize() {
        vendedor = (Vendedor) App.getUsuarioActual();
        vendedorController = new VendedorController(App.inmoSmart);
        lblNombreUsuario.setText(vendedor.getNombreCompleto() + "  |  " + vendedor.getRangoUsuario().name());
        configurarColumnas();
        configurarCombos();
        cargarDatos();
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
    }

    private void configurarCombos() {
        StringConverter<Inmueble> conv = new StringConverter<>() {
            @Override public String toString(Inmueble i) {
                return i == null ? "" : i.getDireccion() + " — " + i.getCiudad() + " (" + i.getEstado().name() + ")";
            }
            @Override public Inmueble fromString(String s) { return null; }
        };
        comboInmueblePublicar.setConverter(conv);
        comboInmuebleOfertas.setConverter(conv);
    }

    private void cargarDatos() {
        List<Inmueble> inmuebles = vendedorController.obtenerInmueblesVendedor(vendedor);
        tablaInmuebles.setItems(FXCollections.observableArrayList(inmuebles));
        comboInmueblePublicar.setItems(FXCollections.observableArrayList(inmuebles));
        comboInmuebleOfertas.setItems(FXCollections.observableArrayList(inmuebles));

        List<Transaccion> transacciones = vendedorController.obtenerTransaccionesVendedor(vendedor.getId());
        tablaTransacciones.setItems(FXCollections.observableArrayList(transacciones));
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

        grid.add(new Label("Direccion:"),   0, 0); grid.add(fDireccion, 1, 0);
        grid.add(new Label("Ciudad:"),      0, 1); grid.add(fCiudad,    1, 1);
        grid.add(new Label("Area (m2):"),   0, 2); grid.add(fArea,      1, 2);
        grid.add(new Label("Precio COP:"),  0, 3); grid.add(fPrecio,    1, 3);
        grid.add(new Label("Descripcion:"), 0, 4); grid.add(fDesc,      1, 4);
        grid.add(new Label("Tipo:"),        0, 5); grid.add(fTipo,      1, 5);

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
                vendedorController.registrarInmueble(vendedor, dir, ciudad, area, precio, desc, tipo);
                cargarDatos();
            } catch (NumberFormatException ex) {
                mostrarAlerta("Area y precio deben ser numeros validos.");
            }
        });
    }

    @FXML
    void publicarInmuebleAction() {
        Inmueble inmueble    = comboInmueblePublicar.getValue();
        String descripcion   = txtDescripcionPublicacion.getText().trim();

        if (inmueble == null) {
            lblMsgPublicacion.setText("Selecciona un inmueble.");
            return;
        }
        if (descripcion.isEmpty()) {
            lblMsgPublicacion.setText("Escribe una descripcion para la publicacion.");
            return;
        }
        String resultado = vendedorController.publicarInmueble(vendedor, inmueble, descripcion);
        lblMsgPublicacion.setText(resultado);
        txtDescripcionPublicacion.clear();
        cargarDatos();
    }

    @FXML
    void cargarOfertasAction() {
        Inmueble inmueble = comboInmuebleOfertas.getValue();
        if (inmueble == null) return;
        List<Oferta> pendientes = vendedorController.obtenerOfertasPendientes(vendedor, inmueble);
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
        vendedorController.cerrarOferta(oferta, tipo);
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
        vendedorController.rechazarOferta(vendedor, oferta);
        lblMsgOferta.setText("Oferta rechazada.");
        cargarOfertasAction();
    }

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
