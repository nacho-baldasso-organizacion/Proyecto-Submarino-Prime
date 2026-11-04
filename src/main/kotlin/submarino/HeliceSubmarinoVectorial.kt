package submarino

import javafx.scene.Group
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Polygon

internal object HeliceSubmarinoVectorial {

    private val COLOR_CONTORNO = Color.web("#08232E")

    fun crear(): Group {
        val palaSuperior = Polygon(
            0.0, 0.0,
            -8.0, -20.0,
            5.0, -20.0,
            10.0, 0.0
        ).apply {
            fill = Color.web("#B9821E")
            stroke = COLOR_CONTORNO
            strokeWidth = 1.5
        }
        val palaInferior = Polygon(
            0.0, 0.0,
            -8.0, 20.0,
            5.0, 20.0,
            10.0, 0.0
        ).apply {
            fill = Color.web("#D99A25")
            stroke = COLOR_CONTORNO
            strokeWidth = 1.5
        }
        val eje = Circle(0.0, 0.0, 5.0).apply {
            fill = Color.web("#6F4510")
            stroke = COLOR_CONTORNO
            strokeWidth = 1.5
        }

        return Group().apply {
            translateX = -68.0
            children.addAll(palaSuperior, palaInferior, eje)
        }
    }
}
