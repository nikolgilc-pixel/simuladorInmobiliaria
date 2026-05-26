# Modelo de Dominio — GestorInmobiliaria (InmoSmart)

> Representación textual estructurada del diagrama UML de clases.  
> Convenio de visibilidad: `+` public · `-` private · `#` protected  
> Convenio de estereotipos: `[abstract]` · `[record]` · `[enum]` · `[interface]`

---

## 1. JERARQUÍA DE USUARIOS

### 1.1 `Usuario` [abstract]

| Visibilidad | Nombre | Tipo |
|-------------|--------|------|
| `#` | `id` | `String` |
| `#` | `nombreCompleto` | `String` |
| `#` | `telefono` | `String` |
| `#` | `email` | `String` |
| `#` | `password` | `String` |
| `#` | `fechaRegistro` | `LocalDate` |
| `#` | `puntosReputacion` | `int` |
| `#` | `rangoUsuario` | `RangoUsuario` *(enum)* |
| `#` | `listaNotificaciones` | `List<Notificacion>` |

| Visibilidad | Firma del método | Descripción |
|-------------|-----------------|-------------|
| `+` | `sumarPuntos(AccionInmobiliaria a): void` | Switch que suma puntos según la acción; al final llama a `actualizarRango()` |
| `+` | `quitarPuntos(CausaPenalizacion c): void` | Resta puntos por penalización |
| `+` | `actualizarRango(): void` | If-else según `puntosReputacion`; asigna el `RangoUsuario` correspondiente |
| `+` | `recibirNotificacion(Notificacion alerta): void` | Recibe una notificación del sistema |

---

### 1.2 `Vendedor` *(extends `Usuario`)*

| Visibilidad | Nombre | Tipo |
|-------------|--------|------|
| `-` | `totalInmueblesVendidos` | `int` |
| `-` | `totalInmueblesArrendados` | `int` |
| `-` | `listaPublicaciones` | `List<Publicacion>` |
| `-` | `listaInmuebles` | `List<Inmueble>` |

| Visibilidad | Firma del método |
|-------------|-----------------|
| `+` | `registrarNuevoInmueble(): void` | entrega los datos necesarios para que el sistema realice el registro de un nuevo inmueble
| `+` | `solicitarPublicarInmueble(String codigoInm, String descrip): void` | le pasa al sistema los datos necesarios para realizar la publicacion de un inmueble existente
| `+` | `solicitarEliminarPublicacion(String codigoPublicacion): void` |
| `+` | `verOfertasPendientes(Inmueble i): void` |
| `+` | `gestionarOferta(Oferta o, boolean aceptada): void` |
| `+` | `aceptarOferta(Oferta o): void` |
| `+` | `rechazarOferta(Oferta o): void` |

---

### 1.3 `Comprador` *(extends `Usuario`)*

| Visibilidad | Nombre | Tipo |
|-------------|--------|------|
| `-` | `presupuesto` | `double` |
| `-` | `ciudadInteres` | `String` |
| `-` | `tipoInmuebleInteres` | `TipoInmueble` *(enum)* |
| `-` | `areaMinima` | `double` |
| `-` | `inmueblesFavoritos` | `List<Inmueble>` |
| `-` | `historialTransacciones` | `List<Transaccion>` |
| `-` | `listaOfertas` | `List<Oferta>` |
| `-` | `historialIntereses` | `FiltroBusqueda` *(record)* | se actualiza automaticamente cyuando se crea un nuevo filtro busqueda

| Visibilidad | Firma del método | Nota |
|-------------|-----------------|------|
| `+` | `realizarOferta(Inmueble i, double monto): void` | Al finalizar, suma puntos vía `sumarPuntos()` |
| `+` | `comprarInmueble(): void` | |
| `+` | `solicitarBusqueda(): void` | |
| `+` | `visualizarResultadosBusqueda(List<Publicacion> resultados): void` | |
| `+` | `actualizarHistorial(FiltroBusqueda nuevo): void` | |
| `+` | `verRecomendaciones(): void` | | obtiene publicaciones de inmuebles con los criterios del historialIntereses actual

---

## 2. ENTIDADES PRINCIPALES

### 2.1 `Inmueble`

| Visibilidad | Nombre | Tipo |
|-------------|--------|------|
| `-` | `codigo` | `String` | es unico y se genera cuando se crea el inmueble
| `-` | `direccion` | `String` |
| `-` | `ciudad` | `String` |
| `-` | `area` | `double` |
| `-` | `precio` | `double` |
| `-` | `description` | `String` |
| `-` | `fechaRegistro` | `LocalDate` | automatica cuando se crea el inmueble
| `-` | `tipoInmueble` | `TipoInmueble` *(enum)* |
| `-` | `estado` | `EstadoInmueble` *(enum)* |
| `-` | `vendedorAsignado` | `Vendedor` |
| `-` | `listaOfertas` | `List<Oferta>` |
| `-` | `publicacion` | `Publicacion` |

| Visibilidad | Firma del método |
|-------------|-----------------|
| `+` | `actualizarEstadoInmueble(EstadoInmueble e): void` |

---

### 2.2 `Publicacion`

| Visibilidad | Nombre | Tipo |
|-------------|--------|------|
| `-` | `codigo` | `String` |
| `-` | `fecha` | `LocalDate` |
| `-` | `hora` | `LocalTime` |
| `-` | `descripcion` | `String` |
| `-` | `inmueble` | `Inmueble`|
| `-` | `vendedor` | `Vendedor` *(referencia)* |

---

### 2.3 `Oferta`

| Visibilidad | Nombre | Tipo |
|-------------|--------|------|
| `-` | `codigo` | `String` |
| `-` | `valor` | `double` |
| `-` | `fecha` | `Instant` |
| `-` | `comprador` | `Comprador` |
| `-` | `estadoOferta` | `EstadoOferta` *(enum)* |

| Visibilidad | Firma del método |
|-------------|-----------------|
| `+` | `actualizarEstado(EstadoOferta e): void` |

---

### 2.4 `Transaccion` [record]

| Nombre | Tipo |
|--------|------|
| `codigo` | `String` |
| `comprador` | `Comprador` |
| `vendedor` | `Vendedor` |
| `inmueble` | `Inmueble` |
| `valorFinal` | `double` |
| `tipoOperacion` | `TipoOperacion` *(enum)* |
| `fecha` | `Instant` |

---

### 2.5 `Notificacion`

| Visibilidad | Nombre | Tipo |
|-------------|--------|------|
| `-` | `titulo` | `String` |
| `-` | `contenido` | `String` |
| `-` | `fecha` | `LocalDateTime` |
| `-` | `tipo` | `TipoNotificacion` *(enum)* |

---

### 2.6 `FiltroBusqueda` [record]

| Nombre | Tipo |
|--------|------|
| `ciudad` | `String` |
| `tipoInmueble` | `TipoInmueble` *(enum)* |
| `precioMinimo` | `double` |
| `precioMaximo` | `double` |
| `areaMinima` | `double` |

---

## 3. CLASE FACHADA DEL SISTEMA

### 3.1 `InmoSmart`

| Visibilidad | Nombre | Tipo |
|-------------|--------|------|
| `-` | `codigoComercio` | `String` |
| `-` | `gestorUsuarios` | `GestorUsuarios` *(composición)* |
| `-` | `gestorInmuebles` | `GestorInmuebles` *(composición)* |
| `-` | `gestorPublicacion` | `GestorPublicacion` *(composición)* |
| `-` | `gestorTransacciones` | `GestorTransacciones` *(composición)* |
| `-` | `gestorNotificaciones` | `GestorNotificaciones` *(composición)* |
| `-` | `gestorReportes` | `GestorReportes` *(composición)* |
| `-` | `iservicioBusqueda` | `IServicioBusqueda` *(composición)*|

| Visibilidad | Firma del método |
|-------------|-----------------|
| `+` | `buscarInmueble(): void` |
| `+` | `procesarTransaccion(Oferta oferta): void` |
| `+` | `procesarSolicitudPublicacion(Vendedor v, Inmueble i, String descripcion): String` |
| `+` | `procesarEliminacionPublicacion(Vendedor v, String codigoPublicacion): String` |
| `+` | `consultarCatalogo(FiltroBusqueda filtros): List<Publicacion>` |
| `+` | `obtenerSugerencias(Comprador c): List<Publicacion>` |
| `+` | `vincularInmuebleAVendedor(Inmueble i, Vendedor v): String` |
| `+` | `tramitarOferta(Oferta o): void` |
| `+` | `procesarCierreOferta(Oferta aceptada): void` |

---

## 4. GESTORES (CLASES DE SERVICIO / REPOSITORIO)

### 4.1 `GestorUsuarios`

| Visibilidad | Nombre | Tipo |
|-------------|--------|------|
| `-` | `listaUsuarios` | `List<Usuario>` |

| Visibilidad | Firma del método |
|-------------|-----------------|
| `+` | `añadirUsuario(): String` |
| `+` | `buscarUsuario(String idUsuario): Usuario` |
| `+` | `listarCompradores(): List<Usuario>` |
| `+` | `listarVendedores(): List<Usuario>` |
| `+` | `listarUsuarios(): List<Usuario>` |
| `+` | `eliminarUsuario(String idUsuario): boolean` |

---

### 4.2 `GestorInmuebles`

| Visibilidad | Nombre | Tipo |
|-------------|--------|------|
| `-` | `listaInmuebles` | `List<Inmueble>` |

| Visibilidad | Firma del método |
|-------------|-----------------|
| `+` | `registrarNuevoInmueble(): Inmueble` |
| `+` | `añadirInmueble(): boolean` |
| `+` | `buscarInmueblePorCodigo(String codigoInmueble): Inmueble` |
| `+` | `listarInmueblesDisponibles(): List<Inmueble>` |
| `+` | `listarInmueblesPorTipo(): List<Inmueble>` |
| `+` | `listarInmueblesPorVendedor(String idVendedor): List<Inmueble>` |
| `+` | `listarTodosLosInmuebles(): List<Inmueble>` |
| `+` | `eliminarInmueble(): boolean` |
| `+` | `agregarOferta(Oferta nueva): boolean` |

---

### 4.3 `GestorPublicacion`

| Visibilidad | Nombre | Tipo |
|-------------|--------|------|
| `-` | `listaPublicaciones` | `List<Publicacion>` |


| Visibilidad | Firma del método | Lógica relevante |
|-------------|-----------------|------------------|
| `+` | `crearPublicacion(Vendedor v, Inmueble i, String descrip): Publicacion` | Si `validarDisponibilidad` pasa, crea la instancia |
| `+` | `validarDisponibilidadInmueble(Inmueble i): boolean` | Verifica `inmueble.getEstado() == DISPONIBLE` |
| `+` | `añadirPublicacion(Publicacion p): boolean` | Verifica no-duplicado en lista propia y del vendedor; si OK: añade y llama `vendedor.sumarPuntos(PUBLICAR)` |
| `+` | `buscarPublicacion(String codigoPublicacion): Optional<Publicacion>` | |
| `+` | `finalizarPublicacion(String codigoPublicacion): boolean` | |
| `+` | `eliminarPublicacion(String codigoPublicacion): boolean` | 1. Buscar publicación → obtener vendedor. 2. Si `inmueble.estado != VENDIDO` → `v.quitarPuntos(ELIMINAR_PUBLICACION_SIN_VENTA)`. 3. Remover de `listaPublicaciones` y de `v.listaPublicaciones` |

---

### 4.4 `GestorTransacciones`

| Visibilidad | Nombre | Tipo |
|-------------|--------|------|
| `-` | `listaTransacciones` | `List<Transaccion>` |

| Visibilidad | Firma del método | Nota |
|-------------|-----------------|------|
| `+` | `registrarVenta(Oferta o): Transaccion` | |
| `+` | `registrarArriendo(Oferta o): Transaccion` | |
| `+` | `añadirTransaccion(Transaccion t): void` | |
| `+` | `buscarTransaccion(String codigoTransaccion): Optional<Transaccion>` | |
| `+` | `listarTransacciones(): List<Transaccion>` | |
| `+` | `listarTransaccionesVenta(): List<Transaccion>` | |
| `+` | `listarTransaccionesArriendo(): List<Transaccion>` | |
| `+` | `listarTransaccionesPorVendedor(String idVendedor): List<Transaccion>` | |
| `+` | `listarTransaccionesPorComprador(String idComprador): List<Transaccion>` | |
| `+` | `eliminarTransaccion(String codigoTransaccion): void` | |

> **Nota de comportamiento:** En `procesarCierreOferta()` (llamado desde `InmoSmart`), al finalizar se deben añadir puntos por `COMPLETAR_TRANSACCION` tanto al Vendedor como al Comprador. Si la transacción es de tipo `VENTA`, el Comprador también recibe puntos.

---

### 4.5 `GestorNotificaciones`

| Visibilidad | Nombre | Tipo |
|-------------|--------|------|
| `-` | `listaCanales` | `List<ICanalNotificacion>` |

| Visibilidad | Firma del método |
|-------------|-----------------|
| `+` | `crearNotificacionNuevaOferta(Usuario destinatario, Inmueble i): Notificacion` | crea la notificacion y la envia
| `+` | `crearNotificacionOfertaAceptada(Usuario destinatario, Inmueble i): Notificacion` | crea la notificacion y la envia
| `+` | `crearNotificacionCambioPrecio(Usuario destinatario, Inmueble i): Notificacion` | crea la notificacion y la envia
| `+` | `crearNotificacionInmuebleSimilar(Usuario destinatario, Inmueble i): Notificacion` | crea la notificacion y la envia
| `+` | `enviar(Usuario destinatario, Notificacion alerta): void` |

---

### 4.6 `GestorReportes`

*(Sin atributos propios explícitos en el diagrama)*

| Visibilidad | Firma del método |
|-------------|-----------------|
| `+` | `generarReporteTopInmuebles(List<Transaccion> listaTransacciones): Map<TipoInmueble, Integer>` |
| `+` | `generarReporteDemandaCiudad(List<Inmueble> listaInmuebles): Map<String, Integer>` |
| `+` | `generarReporteCompradoresTop(List<Comprador> compradores): void` |

---

## 5. BÚSQUEDA — ESTRATEGIA

### 5.1 `IServicioBusqueda` [interface]

| Visibilidad | Firma del método |
|-------------|-----------------|
| `+` | `buscarPublicaciones(List<Publicacion> todas, FiltroBusqueda f): List<Publicacion>` |

---

### 5.2 `BuscadorInmueblesPublicados` *(implements `IServicioBusqueda`)*

| Visibilidad | Firma del método |
|-------------|-----------------|
| `+` | `buscarPublicaciones(List<Publicacion> todas, FiltroBusqueda f): List<Publicacion>` |
| `+` | `cumpleConFiltros(Publicacion p, FiltroBusqueda f): boolean` |

---

## 6. NOTIFICACIONES — CANAL

### 6.1 `ICanalNotificacion` [interface]

| Visibilidad | Firma del método |
|-------------|-----------------|
| `+` | `enviarNotificacion(Usuario destinatario, Notificacion n): void` |

---

### 6.2 `CanalCorreo` *(implements `ICanalNotificacion`)*
*(Sin atributos ni métodos adicionales declarados en el diagrama)*

### 6.3 `CanalWhatsApp` *(implements `ICanalNotificacion`)*
*(Sin atributos ni métodos adicionales declarados en el diagrama)*
### 6.4 `CanalSMS` *(implements `ICanalNotificacion`)*
*(Sin atributos ni métodos adicionales declarados en el diagrama)*

---

## 7. ENUMERACIONES

### `RangoUsuario`
`PRINCIPIANTE` · `INVERSIONISTA` · `EXPERTO_INMOBILIARIO` · `MAGNATE_INMOBILIARIO`

### `TipoInmueble`
`CASA` · `APARTAMENTO` · `LOCAL` · `TERRENO`

### `EstadoInmueble`
`DISPONIBLE` · `VENDIDO` · `RESERVADO` · `ARRENDADO`

### `EstadoOferta`
`PENDIENTE` · `ACEPTADA` · `RECHAZADA`

### `TipoOperacion`
`VENTA` · `ARRIENDO`

### `AccionInmobiliaria`
`PUBLICAR` · `OFERTAR` · `COMPRAR` · `COMPLETAR_TRANSACCION`

### `CausaPenalizacion`
`ELIMINAR_PUBLICACION_SIN_VENTA`

### `TipoNotificacion`
`OFERTA_ACEPTADA` · `CAMBIO_PRECIO` · `INMUEBLE_SIMILAR` · `NUEVA_OFERTA`

---

## 8. RELACIONES DEL DIAGRAMA

| Origen | Tipo | Destino | Multiplicidad / Nota |
|--------|------|---------|----------------------|
| `Vendedor` | hereda de | `Usuario` | generalización |
| `Comprador` | hereda de | `Usuario` | generalización |
| `InmoSmart` | composición → | `GestorUsuarios` | 1 : 1 |
| `InmoSmart` | composición → | `GestorInmuebles` | 1 : 1 |
| `InmoSmart` | composición → | `GestorPublicacion` | 1 : 1 |
| `InmoSmart` | composición → | `GestorTransacciones` | 1 : 1 |
| `InmoSmart` | composición → | `GestorNotificaciones` | 1 : 1 |
| `InmoSmart` | composición → | `GestorReportes` | 1 : 1 |
| `GestorUsuarios` | tiene | `listaUsuarios: List<Usuario>` | 1 : N |
| `GestorInmuebles` | tiene | `listaInmuebles: List<Inmueble>` | 1 : N |
| `GestorPublicacion` | tiene | `listaPublicaciones: List<Publicacion>` | 1 : N |
| `GestorTransacciones` | tiene | `listaTransacciones: List<Transaccion>` | 1 : N |
| `GestorNotificaciones` | tiene | `listaNotificaciones: List<Notificacion>` | 1 : N |
| `GestorNotificaciones` | tiene | `listaCanales: List<ICanalNotificacion>` | 1 : N |
| `Vendedor` | tiene | `listaPublicaciones: List<Publicacion>` | 1 : N |
| `Publicacion` | referencia → | `Inmueble` | 1 : 1 |
| `Publicacion` | referencia → | `Vendedor` | N : 1 |
| `Inmueble` | composición -> | `listaOfertas: List<Oferta>` | 1 : N |
| `Inmueble` | referencia → | `Vendedor` (vendedorAsignado) | N : 1 |
| `Inmueble` | usa | `TipoInmueble` (enum) | — |
| `Inmueble` | usa | `EstadoInmueble` (enum) | — |
| `Oferta` | usa | `EstadoOferta` (enum) | — |
| `Usuario` | usa | `RangoUsuario` (enum) | — |
| `Transaccion` | usa | `TipoOperacion` (enum) | — |
| `Notificacion` | usa | `TipoNotificacion` (enum) | — |
| `Comprador` | usa | `FiltroBusqueda` (record) | — |
| `BuscadorInmueblesPublicados` | implementa | `IServicioBusqueda` | — |
| `Correo` | implementa | `ICanalNotificacion` | — |
| `WhatsApp` | implementa | `ICanalNotificacion` | — |

---

## 9. NOTAS DE COMPORTAMIENTO (LÓGICA DE NEGOCIO)

**`sumarPuntos` / `actualizarRango` (en `Usuario`)**
- `sumarPuntos` es un `switch` que incrementa `puntosReputacion` según el valor de `AccionInmobiliaria`.
- Al terminar, llama a `actualizarRango()`, que usa if-else para asignar el `RangoUsuario` según los puntos acumulados.
- `realizarOferta` en `Comprador` también llama a `sumarPuntos` al finalizar.

**`validarDisponibilidadInmueble` (en `GestorPublicacion`)**
1. Consulta que `inmueble.getEstado().equals("DISPONIBLE")`; si no, cancela la operación.

**`crearPublicacion` (en `GestorPublicacion`)**
1. Si `validarDisponibilidadInmueble` pasa → genera un codigo unico y crea instancia de `Publicacion`.
2. Si no, cancela la operación.

**`añadirPublicacion` (en `GestorPublicacion`)**
1. Verifica que la publicación no exista en la lista propia ni en la del vendedor; si existe, retorna `false`.
2. Añade la publicación a la lista propia y a la del vendedor.
3. Llama a `vendedor.sumarPuntos(AccionInmobiliaria.PUBLICAR)`.

**`eliminarPublicacion` (en `GestorPublicacion`)**
1. `Publicacion p = buscarPublicacion(codigoPublicacion)` — si `p.isPresent()` → `Vendedor v = p.getVendedor()`
2. Si `p.getInmueble().getEstado() != EstadoInmueble.VENDIDO` → `v.quitarPuntos(CausaPenalizacion.ELIMINAR_PUBLICACION_SIN_VENTA)`
3. `listaPublicaciones.remove(p)`
4. `v.listaPublicaciones.remove(p)`
5. Retorna `boolean true`.

**`procesarCierreOferta` (en `InmoSmart` / `GestorTransacciones`)**
- Al finalizar, se añaden puntos por `COMPLETAR_TRANSACCION` tanto al Vendedor como al Comprador.
- Si la transacción es de tipo `VENTA`, se añaden puntos también al Comprador.
