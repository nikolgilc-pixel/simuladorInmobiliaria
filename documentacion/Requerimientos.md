Este es el momento donde el diseño conceptual se convierte en un contrato formal de ingeniería. Asumir el rol de Arquitecto Senior implica documentar el sistema de tal manera que cualquier equipo de desarrollo pueda tomar este documento, entender la visión y construir **InmoSmart** exactamente como lo hemos modelado, sin desviaciones.  
A continuación, presento el **Documento de Especificación Arquitectónica y de Requisitos (SRS)** de InmoSmart, depurado hasta su versión final.

# **Documento de Especificación Arquitectónica y de Requisitos**

**Proyecto:** InmoSmart \- Simulador Inmobiliario  
**Arquitectura:** Orientada a Objetos, Patrón Facade, Inyección de Dependencias  
**Entorno de Interfaz:** JavaFX (Patrón MVC)

## **1\. Visión General de la Arquitectura**

El sistema adopta una arquitectura basada en componentes (Gestores de Dominio) orquestados por una **Fachada Central (**InmoSmart**)**. Esto garantiza un bajo acoplamiento y alta cohesión. Al igual que en la parametrización de variables complejas en simulaciones hidrológicas (donde los filtros y criterios de evaluación se inyectan en el modelo sin alterar su estructura base), el sistema permite inyectar motores de búsqueda y canales de notificación sin modificar la lógica interna de los gestores.

## **2\. Requisitos Funcionales (RF)**

* **RF01 \- Gestión de Catálogo de Inmuebles:** El sistema debe permitir la publicación y el retiro de propiedades (Apartamentos, Casas, Lotes, Locales, Oficinas) asociadas obligatoriamente a un Vendedor registrado.  
* **RF02 \- Motor de Búsqueda Desacoplado:** El sistema debe proveer una funcionalidad de búsqueda basada en el empaquetamiento de criterios (FiltroBusqueda), evaluando variables combinadas (Ciudad, Precio minimo, Precio Máximo, TipoInmueble, areaMinima). La creación del filtro debe hacerla el Comprador, la ejecución de esta búsqueda debe delegarse a una interfaz (IServicioBusqueda) que recibe, mediante InmoSmart, el FiltroBusqueda y una lista de todas las publicaciones, busca entre ellas las que sean similares a los criterios de FiltroBusqueda, y las retorna. El Comprador podrá visualizar el resultado de la búsqueda.  
* **RF03 \- Radicación de Ofertas:** Un Comprador debe poder abrir una publicación resultante de una búsqueda, y hacer click sobre un botón “Realizar oferta”, en la interfaz de usuario, para realizar una propuesta económica por un inmueble asociado a la publicacion. El sistema debe verificar que el inmueble se encuentre Disponible y que la oferta sea mayor a 0, y en caso de que sí, instanciar un objeto Oferta y vincularlo obligatoriamente a la colección interna del inmueble objetivo, verificando que no exista ya en la lista de ofertas del Inmueble. Todo esto a través del gestor de Inmuebles. Además se debe notificar por todos los canales al Vendedor del Inmueble que hay una nueva oferta sobre el Inmueble.  
* **RF04 \- Cierre de Transacciones:** El sistema debe permitir al vendedor ver las ofertas pendientes sobre un inmueble y debe haber un boton para aceptar una oferta y otro para rechazarla. Al hacerlo, se debe desencadenar un proceso atómico que:  
  1. Transforme la oferta en un registro inmutable (Transaccion).  
  2. Retire el inmueble del catálogo público (finalizarPublicacion).  
  3. Actualice el estado del inmueble a vendido o arrendado  
* **RF05 \- Despacho Multi-Canal de Notificaciones:** El sistema debe generar alertas automáticas ante eventos clave (Nueva Oferta, Oferta Aceptada, Cambio de Precio de un inmueble, o cuando se publica un Inmueble con criterios similares al último historialIntereses del Comprador). Estas alertas deben persistir en la lista de notificaciones de la clase abstracta Usuario y despacharse a canales externos mediante la interfaz ICanalNotificacion.  
* **RF06 \- Generación de Analíticas:** El sistema debe consolidar las métricas de operación utilizando estructuras de datos clave-valor (Map\<K, V\>), específicamente para reportar el top de inmuebles vendidos, el nivel de demanda segmentado por ciudad, los compradores más activos, y los vendedores con más propiedades.

## **3\. Reglas de Negocio (RN)**

* **RN01 \- Dependencia de la Oferta:** Una Oferta no tiene existencia independiente en el sistema; su ciclo de vida está condicionado a la existencia de un Inmueble. Si el inmueble desaparece, las ofertas pendientes quedan invalidadas.  
* **RN02 \- Inmutabilidad Histórica:** Una vez generada una Transaccion, sus datos (comprador, inmueble, fecha, monto de cierre) no pueden ser modificados. No existirán métodos mutadores (*setters*) en esta entidad. El FiltroBusqueda también debe ser inmutable.  
* **RN03 \- Polimorfismo en la Recepción de Alertas:** El comportamiento ante una notificación entrante depende del rol. El Vendedor debe contar con un tratamiento prioritario en la interfaz de consola/GUI cuando la alerta corresponde a la recepción de una propuesta económica.  
* **RN04 \- Integridad del Catálogo:** Un inmueble sobre el cual se ha aceptado una oferta no puede recibir nuevas propuestas y deja de ser visible para las consultas de la interfaz IServicioBusqueda.

## **4\. Restricciones Técnicas y de Diseño (RT)**

* **RT01 \- Prohibición de "Clase Dios":** La clase InmoSmart tiene prohibido contener lógica atómica de negocio (agregar elementos a listas, instanciar notificaciones, filtrar datos). Su única responsabilidad es delegar la ejecución a los Gestores.  
* **RT02 \- Encapsulamiento Estricto de Colecciones:** Ningún gestor expondrá métodos para sobrescribir sus listas internas (setHistorico, setCatalogo). Las modificaciones de estado se realizarán exclusivamente a través de métodos de comportamiento (ej. registrarTransaccion()).  
* **RT03 \- Inyección de Dependencias:** Las dependencias hacia interfaces (IServicioBusqueda, ICanalNotificacion) deben resolverse mediante inyección por constructor o métodos de registro, nunca instanciando clases concretas directamente dentro de los gestores.  
* **RT04 \- Separación Frontend/Backend:** La interfaz gráfica desarrollada en JavaFX (.fxml) no debe contener lógica de negocio. Los controladores interactuarán exclusivamente con la instancia principal de la fachada InmoSmart.

## **5\. Especificación de Entidades Principales**

* **Usuario (Abstracta):** Define el contrato recibirNotificacion() y posee la colección protegida de alertas.  
* **Comprador / Vendedor:** Actores concretos. El vendedor administra una lista adicional de sus activos publicados.  
* **Inmueble:** Entidad central que custodia su propia lista de Ofertas recibidas, respetando el principio de *Experto en Información*.  
* **FiltroBusqueda:** Objeto de Transferencia de Datos (DTO temporal) encargado de autoevaluarse contra las propiedades del catálogo.

Como Arquitecto, valido que esta especificación cubre la totalidad del código y los diagramas que hemos consolidado. Cumple con los estándares más rigurosos esperados en un proyecto de Ingeniería de Sistemas y Computación.  
Para dejar este documento completamente blindado ante cualquier evaluación de requisitos, identifico una pequeña ambigüedad en el proceso de negocio sobre la cual necesito tu decisión: **Cuando el Vendedor acepta una oferta específica (RF04 / RN04) y el inmueble se retira del catálogo... ¿Qué debe hacer el sistema con las *otras* ofertas que habían llegado para ese mismo inmueble y que fueron rechazadas implícitamente?** ¿Se eliminan, se marcan como "rechazadas", o se notifica a esos compradores que el inmueble ya fue vendido?

OTRAS CONSIDERACIONES 

Funcionalidades Avanzadas  
Sistema de Reputación de Usuarios  
Los usuarios acumulan puntos de reputación dependiendo de su actividad. Usuario tendrá un método para sumar puntos que recibirá una AccionInmobiliaria, y con base en esta añadirá puntos al usuario de la siguiente manera:

Ejemplo:

Acción Puntos  
Publicar inmueble \+10  
Comprar inmueble \+50  
Realizar oferta \+5  
Completar transacción \+100

Clasificación de Usuarios

Dependiendo de los puntos acumulados:  
Rango Puntos  
Principiante 0 – 100  
Inversionista 101 – 500  
Experto Inmobiliario 501 – 2000  
Magnate Inmobiliario \+2000

El Usuario también tendrá un método para quitar puntos que recibirá una CausaPenalizacion, y con base en esta retirará puntos al usuario de la siguiente manera:  
Si el usuario pide eliminar la publicacion de un inmueble que no se ha vendido, se retirarán 10 puntos de puntosReputacion.

El comprador tiene un historialInteres de tipo FiltroBusqueda, que es inicializado con los datos que ingresa el comprador al registrarse en el sistema. Cuando el Comprador inicia sesión por primera vez el sistema le recomienda inmuebles con atributos similares a los contenidos en el historialIntereses.  
Además, cada que el comprador realiza una búsqueda crea un nuevo FiltroBusqueda, y este nuevo FiltroBusqueda se guarda automáticamente en historialIntereses, reemplazando al anterior. cuando el comprador da click en el botón “Ver recomendaciones” desde la interfaz gráfica el  sistema analiza el historialntereses actual del comprador y recomienda inmuebles similares con base en este. 

Ejemplo  
Si el comprador busca:  
● apartamentos  
● ciudad Armenia  
● precio entre 200M y 300M

El sistema recomienda propiedades similares.

Reportes del Sistema  
El sistema podrá generar reportes como:  
● inmuebles más vendidos  
● ciudades con mayor demanda  
● compradores más activos  
● vendedores con más propiedades

Alertas Automáticas  
El sistema enviará notificaciones cuando:  
● se realice una nueva oferta sobre un inmueble  
● una oferta sea aceptada  
● un inmueble cambie de precio  
● aparezca un inmueble similar al buscado  
