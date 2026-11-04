package submarino

import javafx.scene.paint.Color
import javafx.scene.shape.Circle

internal object AnilloExplosionSubmarino {

    private const val RADIO_ANILLO = 24.0

    fun crear(indice: Int): Circle = Circle(0.0, 0.0, RADIO_ANILLO + indice * 9.0).apply {
        fill = null
        stroke = if (indice % 2 == 0) {
            Color.web("#FFC65C", 0.92)
        } else {
            Color.web("#FF5A2A", 0.86)
        }
        strokeWidth = 7.0 - indice
    }
}
