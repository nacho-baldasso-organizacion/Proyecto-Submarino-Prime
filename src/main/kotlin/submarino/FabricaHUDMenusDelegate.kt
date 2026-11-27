package submarino

import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import kotlin.random.Random

/**
 * Sub-fábrica delegada de creación de menús, constructores y componentes
 * HUX más complejos. Visibilidad pública, menos de 8 tipos del proyecto.
 */
object FabricaHUDMenusDelegate {

    internal fun crearReglaAviso(
        condicion: (EstadoJuego) -> Boolean,
        texto: String
    ): ReglaAviso = ReglaAviso(condicion, texto)

    fun crearBarraHUD(
        ancho: Double,
        estiloInicial: String,
        progresoInicial: Double = 1.0
    ): BarraHUD = BarraHUD(ancho, estiloInicial, progresoInicial)

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