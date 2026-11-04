package submarino

import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

/**
 * Construye y controla el panel modal de reglas del menú principal.
 */
class PanelReglasMenuPrincipal(
    private val raiz: StackPane,
    private val botones: BotonesMenuPrincipal
) {

    private val overlay: StackPane = crearOverlayReglas()

    fun obtenerNodo(): StackPane = overlay

    fun mostrar(mostrar: Boolean) {
        overlay.isVisible = mostrar
        overlay.isMouseTransparent = !mostrar
    }

    private fun crearOverlayReglas(): StackPane {
        val fondoModal = Rectangle().apply {
            fill = Color.rgb(0, 0, 0, 0.72)
            widthProperty().bind(raiz.widthProperty())
            heightProperty().bind(raiz.heightProperty())
        }

        val titulo = Label("REGLAS DE EXPLORACIÓN").apply {
            style = ESTILO_TITULO_MODAL
        }
        val reglas = VBox(10.0).apply {
            children.addAll(
                crearRegla("WASD / FLECHAS", "Mover el submarino en los ejes X e Y."),
                crearRegla("ESPACIO", "Minar minerales cuando estés cerca de un nodo."),
                crearRegla("TECLA E", "Acoplarte en la Estación Base (superficie) para abrir la Tienda."),
                crearRegla("OBJETIVO", "Recolecta Cobre, Hierro y Oro; vende en superficie y mejora tu nave.")
            )
        }
        val cerrar = botones.crearBoton("VOLVER AL MENÚ", true).apply {
            setOnAction { mostrar(false) }
        }

        val panel = VBox(16.0).apply {
            alignment = Pos.CENTER_LEFT
            style = ESTILO_PANEL_MODAL
            maxWidth = 620.0
            children.addAll(titulo, reglas, cerrar)
        }

        return StackPane().apply {
            children.addAll(fondoModal, panel)
            StackPane.setAlignment(panel, Pos.CENTER)
            isVisible = false
            isMouseTransparent = true
        }
    }

    private fun crearRegla(tecla: String, texto: String): HBox {
        val etiquetaTecla = Label(tecla).apply {
            style = ESTILO_TECLA
            minWidth = 130.0
        }
        val etiquetaTexto = Label(texto).apply {
            style = ESTILO_REGLA
            isWrapText = true
            maxWidth = 420.0
        }
        return HBox(12.0).apply {
            alignment = Pos.CENTER_LEFT
            children.addAll(etiquetaTecla, etiquetaTexto)
        }
    }

    companion object {
        private const val ESTILO_PANEL_MODAL =
            "-fx-background-color: rgba(3, 25, 38, 0.97);" +
                "-fx-background-radius: 18;" +
                "-fx-border-color: rgba(127, 227, 240, 0.45);" +
                "-fx-border-radius: 18;" +
                "-fx-padding: 24;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.75), 36, 0.3, 0, 12);"

        private const val ESTILO_TITULO_MODAL =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #E6F7FA;"

        private const val ESTILO_TECLA =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #FFD75A;" +
                "-fx-background-color: rgba(255, 215, 90, 0.10);" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 5 8 5 8;"

        private const val ESTILO_REGLA =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #B7D8E1;"
    }
}
