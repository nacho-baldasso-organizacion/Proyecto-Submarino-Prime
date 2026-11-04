package submarino

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.VBox

/**
 * Fábrica de controles básicos del modal de la tienda.
 *
 * Centraliza la creación de etiquetas de valor, botones y tarjetas para que
 * los constructores de secciones no repitan estilos ni estructura.
 */
object ControlesTienda {

    fun crearValor(): Label = Label("--").apply {
        style = EstilosTienda.ESTILO_VALOR
    }

    fun crearBoton(texto: String, color: String): Button = Button(texto).apply {
        style = """
            -fx-background-color: $color;
            -fx-text-fill: #031926;
            -fx-font-family: 'Segoe UI';
            -fx-font-size: 11px;
            -fx-font-weight: bold;
            -fx-background-radius: 8;
            -fx-padding: 8 14 8 14;
            -fx-cursor: hand;
        """.trimIndent()
    }

    fun crearTarjeta(titulo: String, valor: Label): VBox = VBox(2.0).apply {
        style = EstilosTienda.ESTILO_TARJETA
        children.addAll(
            Label(titulo).apply { style = EstilosTienda.ESTILO_TITULO_PEQUENO },
            valor
        )
    }
}
