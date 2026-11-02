package submarino

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

/**
 * Pantalla modal de fin de partida.
 *
 * Se muestra cuando el oxígeno o el casco llegan a cero. Ofrece reintentar la
 * partida desde la superficie o regresar al menú principal.
 */
class GameOverModal(
    private val alReintentar: () -> Unit,
    private val alVolverAlMenu: () -> Unit
) {

    private val raiz: StackPane = StackPane()
    private val etiquetaCausa: Label = Label("").apply {
        style = ESTILO_CAUSA
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
            style = ESTILO_TITULO
        }
        val subtitulo = Label("El submarino ha sido destruido en el abismo").apply {
            style = ESTILO_SUBTITULO
        }

        val reintentar = crearBoton("REINTENTAR", true).apply {
            setOnAction {
                cerrar()
                alReintentar()
            }
        }
        val menu = crearBoton("VOLVER AL MENÚ", false).apply {
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
            style = ESTILO_PANEL
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

    private fun crearBoton(texto: String, principal: Boolean): Button = Button(texto).apply {
        style = if (principal) ESTILO_BOTON_PRINCIPAL else ESTILO_BOTON_SECUNDARIO
        setOnMouseEntered {
            style = if (principal) ESTILO_BOTON_PRINCIPAL_HOVER else ESTILO_BOTON_SECUNDARIO_HOVER
        }
        setOnMouseExited {
            style = if (principal) ESTILO_BOTON_PRINCIPAL else ESTILO_BOTON_SECUNDARIO
        }
    }

    companion object {
        private const val ESTILO_PANEL =
            "-fx-background-color: rgba(28, 8, 8, 0.97);" +
                "-fx-background-radius: 18;" +
                "-fx-border-color: rgba(255, 122, 26, 0.65);" +
                "-fx-border-radius: 18;" +
                "-fx-padding: 28;" +
                "-fx-effect: dropshadow(gaussian, rgba(255, 74, 20, 0.45), 42, 0.35, 0, 0);"

        private const val ESTILO_TITULO =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 54px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #FF7A1A;" +
                "-fx-effect: dropshadow(gaussian, rgba(255, 90, 42, 0.85), 28, 0.4, 0, 0);"

        private const val ESTILO_SUBTITULO =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 15px;" +
                "-fx-text-fill: #F7C9B5;"

        private const val ESTILO_CAUSA =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #FFD75A;" +
                "-fx-background-color: rgba(255, 122, 26, 0.12);" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 8 16 8 16;"

        private const val ESTILO_BOTON_PRINCIPAL =
            "-fx-background-color: #FF7A1A;" +
                "-fx-text-fill: #1A0703;" +
                "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 11 32 11 32;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(255, 122, 26, 0.55), 18, 0.3, 0, 4);"

        private const val ESTILO_BOTON_PRINCIPAL_HOVER =
            "-fx-background-color: #FFA24D;" +
                "-fx-text-fill: #1A0703;" +
                "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 11 32 11 32;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(255, 122, 26, 0.85), 24, 0.35, 0, 4);"

        private const val ESTILO_BOTON_SECUNDARIO =
            "-fx-background-color: rgba(52, 20, 14, 0.9);" +
                "-fx-text-fill: #FFD7C2;" +
                "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: rgba(255, 160, 110, 0.75);" +
                "-fx-border-radius: 10;" +
                "-fx-padding: 11 32 11 32;" +
                "-fx-cursor: hand;"

        private const val ESTILO_BOTON_SECUNDARIO_HOVER =
            "-fx-background-color: rgba(96, 38, 22, 0.95);" +
                "-fx-text-fill: #FFF3EA;" +
                "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #FFB07A;" +
                "-fx-border-radius: 10;" +
                "-fx-padding: 11 32 11 32;" +
                "-fx-cursor: hand;"
    }
}
