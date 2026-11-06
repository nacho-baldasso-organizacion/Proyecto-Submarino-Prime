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
    ): ReglaAviso = ReglaAviso(condicion, texto)

    fun crearBarraHUD(
        ancho: Double,
        estiloInicial: String,
        progresoInicial: Double = 1.0
    ): BarraHUD = BarraHUD(ancho, estiloInicial, progresoInicial)

    fun crearIndicadorMetrosHUD(
        titulo: String,
        estiloTitulo: String,
        estiloValor: String,
        patron: String
    ): IndicadorMetrosHUD = IndicadorMetrosHUD(titulo, estiloTitulo, estiloValor, patron)

    fun crearIndicadorPresionHUD(
        estiloTitulo: String,
        estiloValor: String,
        anchoBarra: Double,
        estiloBarraEstable: String,
        estiloBarraPrecaucion: String,
        estiloBarraCritico: String
    ): IndicadorPresionHUD = IndicadorPresionHUD(
        estiloTitulo,
        estiloValor,
        anchoBarra,
        estiloBarraEstable,
        estiloBarraPrecaucion,
        estiloBarraCritico
    )

    fun crearIndicadorOxigenoJuegoHUD(
        estiloTitulo: String,
        estiloValor: String,
        anchoBarra: Double,
        estiloBarraEstable: String,
        estiloBarraPrecaucion: String,
        estiloBarraCritico: String
    ): IndicadorOxigenoJuegoHUD = IndicadorOxigenoJuegoHUD(
        estiloTitulo,
        estiloValor,
        anchoBarra,
        estiloBarraEstable,
        estiloBarraPrecaucion,
        estiloBarraCritico
    )

    fun crearIndicadorCascoHUD(
        estiloTitulo: String,
        estiloValor: String,
        anchoBarra: Double,
        estiloBarraEstable: String,
        estiloBarraPrecaucion: String,
        estiloBarraCritico: String
    ): IndicadorCascoHUD = IndicadorCascoHUD(
        estiloTitulo,
        estiloValor,
        anchoBarra,
        estiloBarraEstable,
        estiloBarraPrecaucion,
        estiloBarraCritico
    )

    fun crearIndicadorCargoHUD(
        estiloTitulo: String,
        estiloValor: String
    ): IndicadorCargoHUD = IndicadorCargoHUD(estiloTitulo, estiloValor)

    fun crearIndicadorMonedasHUD(
        estiloTitulo: String,
        estiloValor: String
    ): IndicadorMonedasHUD = IndicadorMonedasHUD(estiloTitulo, estiloValor)

    fun crearIndicadorNodosHUD(
        estiloTitulo: String,
        estiloValor: String
    ): IndicadorNodosHUD = IndicadorNodosHUD(estiloTitulo, estiloValor)

    fun crearIndicadorAcopleHUD(
        estiloTitulo: String,
        estiloValor: String
    ): IndicadorAcopleHUD = IndicadorAcopleHUD(estiloTitulo, estiloValor)

    fun crearSeparadorHUD(estilo: String): SeparadorHUD = SeparadorHUD(estilo)

    fun crearTextoAyudaHUD(texto: String, estilo: String): TextoAyudaHUD =
        TextoAyudaHUD(texto, estilo)

    fun crearIndicadorEstadoOxigenoHUD(
        estiloTitulo: String,
        estiloValor: String
    ): IndicadorEstadoOxigenoHUD = IndicadorEstadoOxigenoHUD(estiloTitulo, estiloValor)

    fun crearIndicadorOxigenoNavegacionHUD(
        estiloTitulo: String,
        estiloValor: String,
        anchoBarra: Double,
        estiloBarraEstable: String,
        estiloBarraPrecaucion: String,
        estiloBarraCritico: String,
        estiloBarraAgotado: String
    ): IndicadorOxigenoNavegacionHUD = IndicadorOxigenoNavegacionHUD(
        estiloTitulo,
        estiloValor,
        anchoBarra,
        estiloBarraEstable,
        estiloBarraPrecaucion,
        estiloBarraCritico,
        estiloBarraAgotado
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
    ): BarraUmbralHUD = BarraUmbralHUD(
        ancho,
        progresoInicial,
        estiloEstable,
        estiloPrecaucion,
        estiloCritico
    )

    fun crearBarraEstadoOxigenoHUD(
        ancho: Double,
        estiloEstable: String,
        estiloPrecaucion: String,
        estiloCritico: String,
        estiloAgotado: String
    ): BarraEstadoOxigenoHUD = BarraEstadoOxigenoHUD(
        ancho,
        estiloEstable,
        estiloPrecaucion,
        estiloCritico,
        estiloAgotado
    )

    fun crearBotonesMenuPrincipal(): BotonesMenuPrincipal = BotonesMenuPrincipal()

    fun crearContenidoMenuPrincipal(
        bloqueTexto: VBox,
        panelBotones: HBox,
        nodoSubmarino: Node
    ): ContenidoMenuPrincipal = ContenidoMenuPrincipal(bloqueTexto, panelBotones, nodoSubmarino)

    fun crearFondoMenuPrincipal(
        anchoEscena: Double,
        altoEscena: Double
    ): FondoMenuPrincipal = FondoMenuPrincipal(anchoEscena, altoEscena)

    fun crearPanelReglasMenuPrincipal(
        raiz: StackPane,
        botones: BotonesMenuPrincipal
    ): PanelReglasMenuPrincipal = PanelReglasMenuPrincipal(raiz, botones)

    fun crearSubmarinoVectorial(): SubmarinoVectorial = SubmarinoVectorial()

    fun crearTituloMenuPrincipal(): TituloMenuPrincipal = TituloMenuPrincipal()

    fun crearCompositorTienda(
        raiz: StackPane,
        constructorEncabezado: ConstructorEncabezadoTienda,
        constructorAcciones: ConstructorAccionesTienda,
        etiquetaMensaje: Label,
        contenedorMejoras: VBox
    ): CompositorTienda = CompositorTienda(
        raiz,
        constructorEncabezado,
        constructorAcciones,
        etiquetaMensaje,
        contenedorMejoras
    )

    fun crearConstructorAccionesTienda(
        sesion: SesionJuego,
        etiquetaMensaje: Label,
        alDesacoplar: () -> Unit,
        alRefrescar: () -> Unit
    ): ConstructorAccionesTienda = ConstructorAccionesTienda(
        sesion,
        etiquetaMensaje,
        alDesacoplar,
        alRefrescar
    )

    fun crearConstructorEncabezadoTienda(
        etiquetaMonedas: Label,
        etiquetaOxigeno: Label,
        etiquetaCasco: Label,
        etiquetaBodega: Label,
        etiquetaNodos: Label
    ): ConstructorEncabezadoTienda = ConstructorEncabezadoTienda(
        etiquetaMonedas,
        etiquetaOxigeno,
        etiquetaCasco,
        etiquetaBodega,
        etiquetaNodos
    )

    fun crearConstructorFilasMejoraTienda(
        sesion: SesionJuego,
        contenedor: VBox,
        alMensaje: (String) -> Unit,
        alRefrescar: () -> Unit
    ): ConstructorFilasMejoraTienda = ConstructorFilasMejoraTienda(
        sesion,
        contenedor,
        alMensaje,
        alRefrescar
    )

    internal fun crearGameOverPanel(
        alReintentar: () -> Unit,
        alVolverAlMenu: () -> Unit
    ): GameOverPanel = GameOverPanel(alReintentar, alVolverAlMenu)
}
