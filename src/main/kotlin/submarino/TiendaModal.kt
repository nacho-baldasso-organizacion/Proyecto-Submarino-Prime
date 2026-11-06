package submarino

import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import java.util.Locale

/**
 * Modal de la tienda de upgrades de la Estación Base.
 *
 * Se muestra como una capa semitransparente sobre la partida. Toda la lógica
 * económica y de mejoras se delega en [SesionJuego]; esta clase solo orquesta
 * los colaboradores que componen la vista y refleja el estado resultante.
 */
class TiendaModal(
    private val sesion: SesionJuego
) {

    private val raiz: StackPane = StackPane()
    private val etiquetaMonedas: Label = ControlesTienda.crearValor()
    private val etiquetaOxigeno: Label = ControlesTienda.crearValor()
    private val etiquetaCasco: Label = ControlesTienda.crearValor()
    private val etiquetaBodega: Label = ControlesTienda.crearValor()
    private val etiquetaNodos: Label = ControlesTienda.crearValor()
    private val etiquetaMensaje: Label = Label(
        "Vende minerales para financiar mejoras. Elige una mejora y pulsa COMPRAR."
    ).apply {
        style = EstilosTienda.estiloMensaje()
        isWrapText = true
        maxWidth = 760.0
    }
    private val contenedorMejoras: VBox = VBox(8.0).apply {
        style = "-fx-background-color: transparent;"
    }
    private val constructorFilasMejora: ConstructorFilasMejoraTienda = FabricaHUD.crearConstructorFilasMejoraTienda(
        sesion,
        contenedorMejoras,
        { texto -> etiquetaMensaje.text = texto },
        { refrescar() }
    )

    init {
        val compositor = FabricaHUD.crearCompositorTienda(
            raiz,
            FabricaHUD.crearConstructorEncabezadoTienda(
                etiquetaMonedas,
                etiquetaOxigeno,
                etiquetaCasco,
                etiquetaBodega,
                etiquetaNodos
            ),
            FabricaHUD.crearConstructorAccionesTienda(
                sesion,
                etiquetaMensaje,
                { cerrar() },
                { refrescar() }
            ),
            etiquetaMensaje,
            contenedorMejoras
        )
        compositor.componer()
    }

    fun obtenerNodo(): Parent = raiz

    fun abrir() {
        raiz.isVisible = true
        raiz.isMouseTransparent = false
        refrescar()
    }

    fun cerrar() {
        raiz.isVisible = false
        raiz.isMouseTransparent = true
    }

    fun estaAbierto(): Boolean = raiz.isVisible

    fun refrescar() {
        val estado = sesion.estado()
        etiquetaMonedas.text = "$ ${estado.monedas}"
        etiquetaOxigeno.text = String.format(Locale.US, "%.0f / %.0f", estado.oxigeno, estado.capacidadOxigeno)
        etiquetaCasco.text = String.format(Locale.US, "%.0f %%", estado.integridadCasco)
        etiquetaBodega.text = String.format(
            Locale.US,
            "%.1f / %.1f kg",
            estado.pesoBodega,
            estado.capacidadBodega
        )
        etiquetaNodos.text = estado.nodosDisponibles.toString()
        constructorFilasMejora.reconstruir()
    }
}
