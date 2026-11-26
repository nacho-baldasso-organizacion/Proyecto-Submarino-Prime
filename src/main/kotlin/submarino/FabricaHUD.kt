package submarino

import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox

/**
 * Fábrica centralizada de las clases concretas que componen el HUD, el menú
 * principal y los modales, para que sus consumidores dependan de abstracciones.
 */
object FabricaHUD {

    internal fun crearReglaAviso(
        condicion: (EstadoJuego) -> Boolean,
        texto: String
    ): ReglaAviso = FabricaHUDMenusDelegate.crearReglaAviso(condicion = condicion, texto = texto)

    fun crearBarraHUD(
        ancho: Double,
        estiloInicial: String,
        progresoInicial: Double = 1.0
    ): BarraHUD = FabricaHUDMenusDelegate.crearBarraHUD(
        ancho = ancho,
        estiloInicial = estiloInicial,
        progresoInicial = progresoInicial
    )

    fun crearIndicadorMetrosHUD(
        titulo: String,
        estiloTitulo: String,
        estiloValor: String,
        patron: String
    ): IndicadorMetrosHUD = FabricaHUDIndicatorsDelegate.crearIndicadorMetrosHUD(
        titulo = titulo,
        estiloTitulo = estiloTitulo,
        estiloValor = estiloValor,
        patron = patron
    )

    fun crearIndicadorPresionHUD(
        estiloTitulo: String,
        estiloValor: String,
        anchoBarra: Double,
        estiloBarraEstable: String,
        estiloBarraPrecaucion: String,
        estiloBarraCritico: String
    ): IndicadorPresionHUD = FabricaHUDIndicatorsDelegate.crearIndicadorPresionHUD(
        estiloTitulo = estiloTitulo,
        estiloValor = estiloValor,
        anchoBarra = anchoBarra,
        estiloBarraEstable = estiloBarraEstable,
        estiloBarraPrecaucion = estiloBarraPrecaucion,
        estiloBarraCritico = estiloBarraCritico
    )

    fun crearIndicadorOxigenoJuegoHUD(
        estiloTitulo: String,
        estiloValor: String,
        anchoBarra: Double,
        estiloBarraEstable: String,
        estiloBarraPrecaucion: String,
        estiloBarraCritico: String
    ): IndicadorOxigenoJuegoHUD = FabricaHUDIndicatorsDelegate.crearIndicadorOxigenoJuegoHUD(
        estiloTitulo = estiloTitulo,
        estiloValor = estiloValor,
        anchoBarra = anchoBarra,
        estiloBarraEstable = estiloBarraEstable,
        estiloBarraPrecaucion = estiloBarraPrecaucion,
        estiloBarraCritico = estiloBarraCritico
    )

    fun crearIndicadorCascoHUD(
        estiloTitulo: String,
        estiloValor: String,
        anchoBarra: Double,
        estiloBarraEstable: String,
        estiloBarraPrecaucion: String,
        estiloBarraCritico: String
    ): IndicadorCascoHUD = FabricaHUDIndicatorsDelegate.crearIndicadorCascoHUD(
        estiloTitulo = estiloTitulo,
        estiloValor = estiloValor,
        anchoBarra = anchoBarra,
        estiloBarraEstable = estiloBarraEstable,
        estiloBarraPrecaucion = estiloBarraPrecaucion,
        estiloBarraCritico = estiloBarraCritico
    )

    fun crearIndicadorCargoHUD(
        estiloTitulo: String,
        estiloValor: String
    ): IndicadorCargoHUD = FabricaHUDIndicatorsDelegate.crearIndicadorCargoHUD(
        estiloTitulo = estiloTitulo,
        estiloValor = estiloValor
    )

    fun crearIndicadorMonedasHUD(
        estiloTitulo: String,
        estiloValor: String
    ): IndicadorMonedasHUD = FabricaHUDIndicatorsDelegate.crearIndicadorMonedasHUD(
        estiloTitulo = estiloTitulo,
        estiloValor = estiloValor
    )

    fun crearIndicadorNodosHUD(
        estiloTitulo: String,
        estiloValor: String
    ): IndicadorNodosHUD = FabricaHUDIndicatorsDelegate.crearIndicadorNodosHUD(
        estiloTitulo = estiloTitulo,
        estiloValor = estiloValor
    )

    fun crearIndicadorAcopleHUD(
        estiloTitulo: String,
        estiloValor: String
    ): IndicadorAcopleHUD = FabricaHUDIndicatorsDelegate.crearIndicadorAcopleHUD(
        estiloTitulo = estiloTitulo,
        estiloValor = estiloValor
    )

    fun crearSeparadorHUD(estilo: String): SeparadorHUD = FabricaHUDIndicatorsDelegate.crearSeparadorHUD(estilo = estilo)

    fun crearTextoAyudaHUD(texto: String, estilo: String): TextoAyudaHUD =
        FabricaHUDIndicatorsDelegate.crearTextoAyudaHUD(texto = texto, estilo = estilo)

    fun crearIndicadorEstadoOxigenoHUD(
        estiloTitulo: String,
        estiloValor: String
    ): IndicadorEstadoOxigenoHUD = FabricaHUDIndicatorsDelegate.crearIndicadorEstadoOxigenoHUD(
        estiloTitulo = estiloTitulo,
        estiloValor = estiloValor
    )

    fun crearIndicadorOxigenoNavegacionHUD(
        estiloTitulo: String,
        estiloValor: String,
        anchoBarra: Double,
        estiloBarraEstable: String,
        estiloBarraPrecaucion: String,
        estiloBarraCritico: String,
        estiloBarraAgotado: String
    ): IndicadorOxigenoNavegacionHUD = FabricaHUDIndicatorsDelegate.crearIndicadorOxigenoNavegacionHUD(
        estiloTitulo = estiloTitulo,
        estiloValor = estiloValor,
        anchoBarra = anchoBarra,
        estiloBarraEstable = estiloBarraEstable,
        estiloBarraPrecaucion = estiloBarraPrecaucion,
        estiloBarraCritico = estiloBarraCritico,
        estiloBarraAgotado = estiloBarraAgotado
    )

    fun crearTarjetaHUD(
        titulo: String,
        estiloTitulo: String,
        estiloValorBase: String,
        barra: ProgressBar? = null
    ): TarjetaHUD = TarjetaHUD(titulo, estiloTitulo, estiloValorBase, barra)

    fun crearBarraUmbralHUD(
        ancho: Double,
        progresoInicial: Double,
        estiloEstable: String,
        estiloPrecaucion: String,
        estiloCritico: String
    ): BarraUmbralHUD = FabricaHUDIndicatorsDelegate.crearBarraUmbralHUD(
        ancho = ancho,
        progresoInicial = progresoInicial,
        estiloEstable = estiloEstable,
        estiloPrecaucion = estiloPrecaucion,
        estiloCritico = estiloCritico
    )

    fun crearBarraEstadoOxigenoHUD(
        ancho: Double,
        estiloEstable: String,
        estiloPrecaucion: String,
        estiloCritico: String,
        estiloAgotado: String
    ): BarraEstadoOxigenoHUD = FabricaHUDIndicatorsDelegate.crearBarraEstadoOxigenoHUD(
        ancho = ancho,
        estiloEstable = estiloEstable,
        estiloPrecaucion = estiloPrecaucion,
        estiloCritico = estiloCritico,
        estiloAgotado = estiloAgotado
    )

    fun crearBotonesMenuPrincipal(): BotonesMenuPrincipal = FabricaHUDMenusDelegate.crearBotonesMenuPrincipal()

    fun crearContenidoMenuPrincipal(
        bloqueTexto: VBox,
        panelBotones: HBox,
        nodoSubmarino: Node
    ): ContenidoMenuPrincipal = FabricaHUDMenusDelegate.crearContenidoMenuPrincipal(
        bloqueTexto = bloqueTexto,
        panelBotones = panelBotones,
        nodoSubmarino = nodoSubmarino
    )

    fun crearFondoMenuPrincipal(
        anchoEscena: Double,
        altoEscena: Double
    ): FondoMenuPrincipal = FabricaHUDMenusDelegate.crearFondoMenuPrincipal(
        anchoEscena = anchoEscena,
        altoEscena = altoEscena
    )

    fun crearPanelReglasMenuPrincipal(
        raiz: StackPane,
        botones: BotonesMenuPrincipal
    ): PanelReglasMenuPrincipal = FabricaHUDMenusDelegate.crearPanelReglasMenuPrincipal(
        raiz = raiz,
        botones = botones
    )

    fun crearSubmarinoVectorial(): SubmarinoVectorial = FabricaHUDMenusDelegate.crearSubmarinoVectorial()

    fun crearTituloMenuPrincipal(): TituloMenuPrincipal = FabricaHUDMenusDelegate.crearTituloMenuPrincipal()

    fun crearCompositorTienda(
        raiz: StackPane,
        constructorEncabezado: ConstructorEncabezadoTienda,
        constructorAcciones: ConstructorAccionesTienda,
        etiquetaMensaje: Label,
        contenedorMejoras: VBox
    ): CompositorTienda = FabricaHUDMenusDelegate.crearCompositorTienda(
        raiz = raiz,
        constructorEncabezado = constructorEncabezado,
        constructorAcciones = constructorAcciones,
        etiquetaMensaje = etiquetaMensaje,
        contenedorMejoras = contenedorMejoras
    )

    fun crearConstructorAccionesTienda(
        sesion: SesionJuego,
        etiquetaMensaje: Label,
        alDesacoplar: () -> Unit,
        alRefrescar: () -> Unit
    ): ConstructorAccionesTienda = FabricaHUDMenusDelegate.crearConstructorAccionesTienda(
        sesion = sesion,
        etiquetaMensaje = etiquetaMensaje,
        alDesacoplar = alDesacoplar,
        alRefrescar = alRefrescar
    )

    fun crearConstructorEncabezadoTienda(
        etiquetaMonedas: Label,
        etiquetaOxigeno: Label,
        etiquetaCasco: Label,
        etiquetaBodega: Label,
        etiquetaNodos: Label
    ): ConstructorEncabezadoTienda = FabricaHUDMenusDelegate.crearConstructorEncabezadoTienda(
        etiquetaMonedas = etiquetaMonedas,
        etiquetaOxigeno = etiquetaOxigeno,
        etiquetaCasco = etiquetaCasco,
        etiquetaBodega = etiquetaBodega,
        etiquetaNodos = etiquetaNodos
    )

    fun crearConstructorFilasMejoraTienda(
        sesion: SesionJuego,
        contenedor: VBox,
        alMensaje: (String) -> Unit,
        alRefrescar: () -> Unit
    ): ConstructorFilasMejoraTienda = FabricaHUDMenusDelegate.crearConstructorFilasMejoraTienda(
        sesion = sesion,
        contenedor = contenedor,
        alMensaje = alMensaje,
        alRefrescar = alRefrescar
    )

    internal fun crearGameOverPanel(
        alReintentar: () -> Unit,
        alVolverAlMenu: () -> Unit
    ): GameOverPanel = FabricaHUDMenusDelegate.crearGameOverPanel(
        alReintentar = alReintentar,
        alVolverAlMenu = alVolverAlMenu
    )
}
