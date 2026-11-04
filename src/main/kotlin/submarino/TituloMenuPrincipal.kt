package submarino

import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.effect.DropShadow
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop

/**
 * Compone el bloque de título, subtítulo y descripción del menú principal.
 */
class TituloMenuPrincipal {

    fun crear(): VBox {
        val titulo = Label("SUBMARINO PRIME").apply {
            style = ESTILO_TITULO
            textFill = LinearGradient(
                0.0, 0.0, 0.0, 1.0,
                true,
                CycleMethod.NO_CYCLE,
                Stop(0.0, Color.web("#FFF7CC")),
                Stop(0.45, Color.web("#FFD75A")),
                Stop(1.0, Color.web("#D99A25"))
            )
            effect = DropShadow(24.0, Color.web("#7FE3F0", 0.55))
        }
        val subtitulo = Label("EXPLORACIÓN ABISAL").apply {
            style = ESTILO_SUBTITULO
        }
        val descripcion = Label("Mapa de 5000 m × 3000 m · Minería · Comercio · Upgrades").apply {
            style = ESTILO_DESCRIPCION
        }

        return VBox(4.0).apply {
            alignment = Pos.CENTER
            children.addAll(titulo, subtitulo, descripcion)
        }
    }

    companion object {
        private const val ESTILO_TITULO =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 58px;" +
                "-fx-font-weight: bold;"

        private const val ESTILO_SUBTITULO =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #C8FAFF;" +
                "-fx-effect: dropshadow(gaussian, rgba(127, 227, 240, 0.55), 14, 0.25, 0, 0);"

        private const val ESTILO_DESCRIPCION =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #8FB6C2;"
    }
}
