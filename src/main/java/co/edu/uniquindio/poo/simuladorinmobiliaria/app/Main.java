package co.edu.uniquindio.poo.simuladorinmobiliaria.app;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.*;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.TipoInmueble;

import java.time.LocalDate;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        // 1. CREAMOS LA EMPRESA Y LOS CANALES DE ALERTA
        // Inicializamos InmoSmart con un buscador por defecto
        BuscadorInmueblesPublicados buscador = new BuscadorInmueblesPublicados();
        InmoSmart empresa = new InmoSmart("INMO-UQ-2026", buscador);

        // Le agregamos canales de notificación reales al gestor (WhatsApp y Correo)
        ArrayList<ICanalNotificacion> canalesActivos = new ArrayList<>();
        canalesActivos.add(new WhatsApp()); // Metemos WhatsApp a la bolsa
        canalesActivos.add(new Correo());   // Metemos Correo a la bolsa

// 3. LE ENTREGAMOS LA BOLSA COMPLETA AL GESTOR
        empresa.getGestorNotificaciones().setListaCanales(canalesActivos);

        System.out.println("==========================================================");
        System.out.println("🤖 [SISTEMA] Iniciando simulación de base de datos...");
        System.out.println("==========================================================\n");

        // 2. CREAMOS LOS USUARIOS (Esto simula que ya se registraron)
        Vendedor vendedorReal = new Vendedor("123", "Nikol Gil", "311-555", "nikol@inmo.com", "pass123", LocalDate.now(), 0, empresa.getGestorUsuarios());
        Comprador compradorReal = new Comprador("456", "Carlos Pérez", "322-777", "carlos@mail.com", "hola456", LocalDate.now(), 0, empresa.getGestorUsuarios(), 500000000, "Armenia", TipoInmueble.APARTAMENTO, 60);

        // Los registramos en el Gestor de Usuarios
        empresa.getGestorUsuarios().registrarUsuario(vendedorReal);
        empresa.getGestorUsuarios().registrarUsuario(compradorReal);

        // 3. SIMULACIÓN DE INICIAR SESIÓN (Lo que luego hará tu botón de JavaFX)
        System.out.println("🔐 [LOGIN] Simulando intento de Inicio de Sesión...");
        String idDigitado = "123"; // Nikol digita su ID en la caja de texto
        String passwordDigitado = "pass123"; // Nikol digita su clave

        // El cerebro busca al usuario
        Usuario usuarioLogueado = empresa.getGestorUsuarios().buscarUsuario(idDigitado);

        if (usuarioLogueado != null && usuarioLogueado.getPassword().equals(passwordDigitado)) {
            System.out.println("🔓 [LOGIN ÉXITOSO] Bienvenido/a: " + usuarioLogueado.getNombreCompleto());

            // Validamos qué tipo de usuario es para saber qué pantalla mostrarle
            if (usuarioLogueado instanceof Vendedor) {
                System.out.println("💼 [INTERFAZ VENDEDOR] Cargando herramientas para Vendedores...\n");

                // El vendedor crea un inmueble y solicita publicarlo
                Inmueble apto = new Inmueble("INM-01", "Calle 14 Norte", "Armenia", 75, 250000000, "Hermoso apartamento", TipoInmueble.APARTAMENTO);
                empresa.vincularInmuebleAVendedor(apto, (Vendedor) usuarioLogueado);

                System.out.println("📢 [PUBLICACIÓN] Creando anuncio en cartelera comercial...");
                String resultadoPub = empresa.procesarSolicitudPublicacion((Vendedor) usuarioLogueado, apto, "¡Gran oportunidad en el norte de Armenia!");
                System.out.println("📝 Resultado del Servidor: " + resultadoPub + "\n");

            } else if (usuarioLogueado instanceof Comprador) {
                System.out.println("🛒 [INTERFAZ COMPRADOR] Cargando catálogo para Compradores...\n");
            }
        } else {
            System.out.println("❌ [LOGIN FALLIDO] Credenciales incorrectas.");
        }

        // 4. SIMULACIÓN DE COMPRADOR INTERACTUANDO
        System.out.println("🛒 [OPERACIÓN COMPRADOR] Carlos Pérez va a mirar el catálogo disponible:");
        empresa.buscarInmueble(); // Llama a nuestra pantalla compacta de catálogo

        // El comprador hace una oferta por la casa de Nikol
        System.out.println("✉️ [OFERTA] Carlos realiza una oferta de $245,000,000 por el apartamento...");
        // Buscamos el inmueble creado para simular la oferta
        Inmueble inmuebleEnVenta = vendedorReal.getListaInmuebles().get(0);
        Oferta ofertaCarlos = new Oferta("OFR-777", 245000000, compradorReal, inmuebleEnVenta);

        // Tramitamos la oferta (esto le enviará WhatsApp/Correo automático a Nikol)
        empresa.tramitarOferta(ofertaCarlos);

        System.out.println("\n🏁 [FIN DE LA SIMULACIÓN] Todo el flujo lógico corrió perfecto.");
        System.out.println("==========================================================");
    }
}