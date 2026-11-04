package submarino

import javafx.scene.control.Label
import javafx.scene.layout.Region

/**
 * Separador vertical usado entre las tarjetas del HUD.
 */
class SeparadorHUD(estilo: String) {

    val nodo: Region = Region().apply {
        prefWidth = 1.0
        minWidth = 1.0
        maxWidth = 1.0
        prefHeight = 34.0
        style = estilo
    }

    fun ancho(): Double = nodo.prefWidth
}

/**
 * Texto de ayuda de controles mostrado al final del HUD.
 */
class TextoAyudaHUD(texto: String, estilo: String) {

    val nodo: Label = Label(texto).apply {
        style = estilo
    }

    fun contenido(): String = nodo.text
}
