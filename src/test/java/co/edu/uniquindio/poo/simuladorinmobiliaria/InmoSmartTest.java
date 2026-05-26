package co.edu.uniquindio.poo.simuladorinmobiliaria;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.*;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InmoSmartTest {

    private InmoSmart app;
    private Vendedor vendedor;
    private Comprador comprador;
    private Inmueble inmueble;

    @BeforeEach
    void setUp() {
        app = new InmoSmart("INMOSMART-TEST", new BuscadorInmueblesPublicados());

        vendedor = new Vendedor("V-01", "Carlos Vega", "3100000001",
                "cv@test.com", "pass123", LocalDate.now());

        comprador = new Comprador("C-01", "Ana Ríos", "3200000001",
                "ar@test.com", "pass123", LocalDate.now(),
                500_000_000.0, "Armenia", TipoInmueble.CASA, 80.0);

        inmueble = new Inmueble("Cra 10 #5-20", "Armenia", 120.0,
                300_000_000.0, "Casa amplia en Armenia", TipoInmueble.CASA);

        app.getGestorUsuarios().añadirUsuario(vendedor);
        app.getGestorUsuarios().añadirUsuario(comprador);
        app.vincularInmuebleAVendedor(inmueble, vendedor);
    }

    // ── Test 1 ─────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("RF02 - Publicar inmueble y encontrarlo con filtro coincidente")
    void testPublicarYBuscarInmueble() {
        app.procesarSolicitudPublicacion(vendedor, inmueble, "Casa disponible en Armenia");

        FiltroBusqueda filtro = new FiltroBusqueda(
                "Armenia", TipoInmueble.CASA, 0.0, 500_000_000.0, 60.0);

        List<Publicacion> resultados = app.consultarCatalogo(comprador, filtro);

        assertEquals(1, resultados.size(),
                "Debe existir exactamente una publicación que coincida con el filtro");
        assertEquals(inmueble.getCodigo(),
                resultados.get(0).getInmueble().getCodigo(),
                "El inmueble encontrado debe ser el mismo que se publicó");
        assertEquals(EstadoInmueble.DISPONIBLE,
                resultados.get(0).getInmueble().getEstado(),
                "El inmueble retornado debe estar DISPONIBLE");
    }

    // ── Test 2 ─────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("RF04 - Al aceptar una oferta, las demás quedan RECHAZADAS")
    void testAceptarOfertaRechazaHuerfanas() {
        Comprador comprador2 = new Comprador("C-02", "Pedro Gil", "3200000002",
                "pg@test.com", "pass123", LocalDate.now(),
                400_000_000.0, "Armenia", TipoInmueble.CASA, 80.0);
        app.getGestorUsuarios().añadirUsuario(comprador2);

        app.procesarSolicitudPublicacion(vendedor, inmueble, "Casa en venta");

        app.tramitarOferta(comprador, inmueble, 290_000_000.0);
        app.tramitarOferta(comprador2, inmueble, 295_000_000.0);

        assertEquals(2, inmueble.getListaOfertas().size(),
                "Deben registrarse exactamente 2 ofertas");

        Oferta oferta1 = inmueble.getListaOfertas().get(0);
        Oferta oferta2 = inmueble.getListaOfertas().get(1);

        app.procesarCierreOferta(oferta1, TipoOperacion.VENTA);

        assertEquals(EstadoOferta.ACEPTADA, oferta1.getEstadoOferta(),
                "La oferta seleccionada debe pasar a ACEPTADA");
        assertEquals(EstadoOferta.RECHAZADA, oferta2.getEstadoOferta(),
                "La oferta huérfana debe pasar automáticamente a RECHAZADA");
    }

    // ── Test 3 ─────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("RF-REP - Publicar y comprar acumulan puntos y actualizan el rango")
    void testAcumulacionPuntosReputacion() {
        // PUBLICAR otorga +10 al vendedor
        app.procesarSolicitudPublicacion(vendedor, inmueble, "Casa en venta");
        assertEquals(10, vendedor.getPuntosReputacion(),
                "Publicar debe sumar 10 puntos al vendedor");
        assertEquals(RangoUsuario.PRINCIPIANTE, vendedor.getRangoUsuario(),
                "Con 10 puntos el rango debe ser PRINCIPIANTE");

        // OFERTAR otorga +5 al comprador
        app.tramitarOferta(comprador, inmueble, 295_000_000.0);
        assertEquals(5, comprador.getPuntosReputacion(),
                "Tramitar oferta debe sumar 5 puntos al comprador");

        Oferta oferta = inmueble.getListaOfertas().get(0);
        app.procesarCierreOferta(oferta, TipoOperacion.VENTA);

        // Vendedor: 10 (PUBLICAR) + 100 (COMPLETAR_TRANSACCION) = 110 → INVERSIONISTA
        assertEquals(110, vendedor.getPuntosReputacion(),
                "Vendedor debe tener 110 puntos tras completar la venta");
        assertEquals(RangoUsuario.INVERSIONISTA, vendedor.getRangoUsuario(),
                "Con 110 puntos el rango del vendedor debe ser INVERSIONISTA");

        // Comprador: 5 (OFERTAR) + 100 (COMPLETAR_TRANSACCION) + 50 (COMPRAR) = 155 → INVERSIONISTA
        assertEquals(155, comprador.getPuntosReputacion(),
                "Comprador debe tener 155 puntos tras completar la compra");
        assertEquals(RangoUsuario.INVERSIONISTA, comprador.getRangoUsuario(),
                "Con 155 puntos el rango del comprador debe ser INVERSIONISTA");
    }

    // ── Validaciones obligatorias ──────────────────────────────────────────────

    @Test
    @DisplayName("RN - valor > 0: oferta con monto 0 no debe registrarse")
    void testTramitarOfertaValorCeroNoAgrega() {
        app.procesarSolicitudPublicacion(vendedor, inmueble, "Casa en venta");
        app.tramitarOferta(comprador, inmueble, 0.0);
        assertTrue(inmueble.getListaOfertas().isEmpty(),
                "Una oferta con valor 0 no debe agregarse al inmueble");
    }

    @Test
    @DisplayName("RN - inmueble disponible: no se acepta oferta sobre inmueble no disponible")
    void testTramitarOfertaInmuebleNoDisponibleNoAgrega() {
        inmueble.actualizarEstadoInmueble(EstadoInmueble.VENDIDO);
        app.tramitarOferta(comprador, inmueble, 200_000_000.0);
        assertTrue(inmueble.getListaOfertas().isEmpty(),
                "No debe registrarse una oferta sobre un inmueble que no está DISPONIBLE");
    }

    @Test
    @DisplayName("RN - usuario registrado: vendedor y comprador deben encontrarse en el sistema")
    void testRegistroVendedorYComprador() {
        Usuario usuarioVendedor = app.getGestorUsuarios().buscarUsuario("V-01");
        Usuario usuarioComprador = app.getGestorUsuarios().buscarUsuario("C-01");

        assertNotNull(usuarioVendedor, "El vendedor debe estar registrado en el sistema");
        assertNotNull(usuarioComprador, "El comprador debe estar registrado en el sistema");
        assertInstanceOf(Vendedor.class, usuarioVendedor,
                "El usuario V-01 debe ser instancia de Vendedor");
        assertInstanceOf(Comprador.class, usuarioComprador,
                "El usuario C-01 debe ser instancia de Comprador");
    }
}
