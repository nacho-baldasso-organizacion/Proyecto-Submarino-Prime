package submarino

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

/**
 * Ensambla el contenido central del menú: título, botones, indicaciones y nave.
 */
class ContenidoMenuPrincipal(
    private val bloqueTexto: VBox,
    private val panelBotones: HBox,
    private val nodoSubmarino: Node
) {

    fun crear(): VBox {
        val indicaciones = Label("WASD / Flechas · ESPACIO: minar · E: acoplar").apply {
            style = ESTILO_INDICACIONES
        }

        return VBox(18.0).apply {
            alignment = Pos.CENTER
            translateY = -48.0
            children.addAll(
                bloqueTexto,
                panelBotones,
                indicaciones,
                nodoSubmarino
            )
            VBox.setMargin(nodoSubmarino, Insets(28.0, 0.0, 0.0, 0.0))
        }
    }

    companion object {
        private const val ESTILO_INDICACIONES =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #5E8896;"
    }
}
