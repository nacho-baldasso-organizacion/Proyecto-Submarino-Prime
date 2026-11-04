package submarino

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

/**
 * Panel visual del modal de fin de partida.
 *
 * Compone el fondo, los textos, la causa de destrucción y las acciones. No
 * conoce el ciclo de vida del juego: solo notifica los callbacks recibidos.
 */
internal class GameOverPanel(
    alReintentar: () -> Unit,
    alVolverAlMenu: () -> Unit
) {

    private val raiz: StackPane = StackPane()
    private val etiquetaCausa: Label = Label("").apply {
        style = GameOverEstilos.CAUSA
        isWrapText = true
        maxWidth = 560.0
    }

    init {
        val fondo = Rectangle().apply {
            fill = Color.rgb(0, 0, 0, 0.74)
            widthProperty().bind(raiz.widthProperty())
            heightProperty().bind(raiz.heightProperty())
        }

        val titulo = Label("GAME OVER").apply {
            style = GameOverEstilos.TITULO
        }
        val subtitulo = Label("El submarino ha sido destruido en el abismo").apply {
            style = GameOverEstilos.SUBTITULO
        }

        val reintentar = GameOverBotonFactory.crear("REINTENTAR", true).apply {
            setOnAction {
                cerrar()
                alReintentar()
            }
        }
        val menu = GameOverBotonFactory.crear("VOLVER AL MENÚ", false).apply {
            setOnAction {
                cerrar()
                alVolverAlMenu()
            }
        }
        val botones = HBox(14.0).apply {
            alignment = Pos.CENTER
            children.addAll(reintentar, menu)
        }

        val panel = VBox(14.0).apply {
            alignment = Pos.CENTER
            style = GameOverEstilos.PANEL
            maxWidth = 620.0
            children.addAll(titulo, subtitulo, etiquetaCausa, botones)
        }

        raiz.children.addAll(fondo, panel)
        StackPane.setAlignment(panel, Pos.CENTER)
        StackPane.setMargin(panel, Insets(24.0))
        raiz.isVisible = false
        raiz.isMouseTransparent = true
    }

    fun obtenerNodo(): Parent = raiz

    fun abrir(causa: String) {
        etiquetaCausa.text = causa
        raiz.isVisible = true
        raiz.isMouseTransparent = false
    }

    fun cerrar() {
        raiz.isVisible = false
        raiz.isMouseTransparent = true
    }

    fun estaAbierto(): Boolean = raiz.isVisible
}
