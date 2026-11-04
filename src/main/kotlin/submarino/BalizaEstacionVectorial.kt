package submarino

import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

internal object BalizaEstacionVectorial {

    fun crear(): Circle = Circle(0.0, -112.0, 7.0).apply {
        fill = Color.web("#FF6B6B")
        effect = DropShadow(18.0, Color.web("#FF6B6B", 0.95))
    }
}
