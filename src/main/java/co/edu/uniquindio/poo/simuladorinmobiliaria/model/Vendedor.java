package co.edu.uniquindio.poo.simuladorinmobiliaria.model;

import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.AccionInmobiliaria;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.CausaPenalizacion;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoInmueble;
import co.edu.uniquindio.poo.simuladorinmobiliaria.model.Enum.EstadoOferta;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Vendedor extends Usuario{

    //Atriburos
    private int totalInmueblesVendidos;
    private int totalInmueblesArrendados;

    //Relaciones
    private List<Inmueble> listaInmuebles;
    private List<Publicacion> listaPublicaciones;

    public Vendedor(String id, String nombreCompleto, String telefono, String email, String password, LocalDate fechaRegistro,
                    int puntosUsuario, GestorUsuarios ownedByGestorUsuario) {
        super(id, nombreCompleto,telefono, email, password, fechaRegistro, puntosUsuario, ownedByGestorUsuario);
        this.totalInmueblesVendidos = 0;
        this.totalInmueblesArrendados = 0;
        this.listaInmuebles= new ArrayList<>();
        this.listaPublicaciones = new ArrayList<>();
    }

    //aqui con este metodo se le permitira al vender registar un inmueble
    public void registrarNuevoInmueble(Inmueble nuevoInmueble) {
        if (nuevoInmueble != null && !this.listaInmuebles.contains(nuevoInmueble)) {
            this.listaInmuebles.add(nuevoInmueble);
            System.out.println("Inmueble guardado con éxito en el portafolio del vendedor.");
        }
    }

    // se verifica si existe el inmueble, y en caso q no se publica y y los puntos de vendedor suben
    public void solicitarPublicarInmueble(String codigoInm, String descrip) {
        Inmueble encontrado = null;
        for (Inmueble inm : listaInmuebles) {
            if (inm.getCodigo().equals(codigoInm)) {
                encontrado = inm;
                break;
            }
            if (encontrado != null) {
                // si la descripción está vacía o es puro espacio en blanco, se activa la penalización
                if (descrip == null || descrip.isBlank()) {
                    this.eliminarPuntos(CausaPenalizacion.ELIMINAR_PUBLICACION_SIN_VENTA);
                    return; //
                }

            }
        }

        if (encontrado != null) {
            this.sumarPuntos(AccionInmobiliaria.PUBLICAR);
            System.out.println("Publicación solicitada con éxito para el inmueble " + codigoInm);
        } else {
            System.out.println("Error: No puedes publicar un inmueble que no esté registrado a tu nombre.");
        }
    }

    // aqui se eliminara la publicacion y se penalizara si es eliminada
    public void solicitarEliminarPublicacion(String codigoPublicacion) {
        System.out.println("Eliminando del sistema la publicación: " + codigoPublicacion);
    }

    // se recorre la lista de ofertas y se muestran todas las ofertas hechas a un inmueble
    public void verOfertasPendientes(Inmueble i) {
        System.out.println("\n=== OFERTAS RECIBIDAS PARA EL INMUEBLE: " + i.getCodigo() + " ===");
        if (i.getListaOfertas() == null || i.getListaOfertas().isEmpty()) {
            System.out.println("Aún no tienes ofertas económicas para esta propiedad.");
            return;
        }

        for (Oferta ofr : i.getListaOfertas()) {
            System.out.println("- Código Oferta: " + ofr.getCodigo() +
                    " | Comprador: " + ofr.getComprador().getNombreCompleto() +
                    " | Monto Ofrecido: $" + ofr.getValor());
        }
    }


    // si el vendedor decide aceptar la oferta se hara de lo contrario no
    public void gestionarOferta(Oferta o, boolean aceptada) {
        if (o == null) return;

        if (aceptada) {
            this.aceptarOferta(o);
        } else {
            this.rechazarOferta(o);
        }
    }

    public void aceptarOferta(Oferta o) {
        if (o == null || o.getEstadoOferta() != EstadoOferta.PENDIENTE) return;

        System.out.println("¡Has aceptado la oferta " + o.getCodigo() + " por un valor de $" + o.getValor() + "!");

        Inmueble inmuebleAsociado = o.getOwnedByInmueble();

        if (inmuebleAsociado != null) {
            for (Oferta otraOferta : inmuebleAsociado.getListaOfertas()) {
                if (!otraOferta.getCodigo().equals(o.getCodigo())) {
                    otraOferta.setEstadoOferta(EstadoOferta.RECHAZADA);
                }
            }

            o.setEstadoOferta(EstadoOferta.ACEPTADA);
            inmuebleAsociado.setEstado(EstadoInmueble.VENDIDO);
        }

        if (inmuebleAsociado != null && inmuebleAsociado.getEstado() != null) {
            if (inmuebleAsociado.getEstado() == EstadoInmueble.ARRENDADO) {
                this.totalInmueblesArrendados++;
            } else {
                this.totalInmueblesVendidos++;
            }
        } else {
            this.totalInmueblesVendidos++;
        }
        this.sumarPuntos(AccionInmobiliaria.COMPLETAR_TRANSACCION);
    }

    // se rechazan las ofertas
    public void rechazarOferta(Oferta o) {
        if (o == null) return;
        System.out.println("La oferta " + o.getCodigo() + " del comprador "
                + o.getComprador().getNombreCompleto() + " ha sido rechazada por el vendedor.");
    }
}

