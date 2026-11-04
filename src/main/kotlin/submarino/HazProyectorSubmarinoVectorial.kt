package submarino

import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.shape.Polygon

internal object HazProyectorSubmarinoVectorial {

    fun crear(): List<Node> {
        val hazExterior = Polygon(
            58.0, -10.0,
            215.0, -52.0,
            215.0, 52.0,
            58.0, 10.0
        ).apply {
            fill = Color.web("#7FE3F0", 0.08)
        }

        val hazInterior = Polygon(
            58.0, -5.0,
            185.0, -26.0,
            185.0, 26.0,
            58.0, 5.0
        ).apply {
            fill = Color.web("#C8FAFF", 0.10)
        }

        return listOf(hazExterior, hazInterior)
    }
}
