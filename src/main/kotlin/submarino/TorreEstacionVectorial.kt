package submarino

import javafx.scene.Node
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Polygon
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text

internal object TorreEstacionVectorial {

    fun crear(): List<Node> {
        val torre = Rectangle(-46.0, -104.0, 92.0, 70.0).apply {
            arcWidth = 12.0
            arcHeight = 12.0
            fill = LinearGradient(
                0.0, 0.0, 0.0, 1.0,
                true,
                CycleMethod.NO_CYCLE,
                Stop(0.0, Color.web("#37697B")),
                Stop(1.0, Color.web("#102D39"))
            )
            stroke = Color.web("#7FE3F0", 0.65)
            strokeWidth = 2.0
        }

        val techo = Polygon(
            -58.0, -104.0,
            0.0, -128.0,
            58.0, -104.0
        ).apply {
            fill = Color.web("#1E4C5C")
            stroke = Color.web("#7FE3F0", 0.6)
            strokeWidth = 2.0
        }

        val ventanaIzquierda = Rectangle(-32.0, -82.0, 20.0, 16.0).apply {
            arcWidth = 4.0
            arcHeight = 4.0
            fill = Color.web("#9EF3FF", 0.9)
            effect = DropShadow(8.0, Color.web("#7FE3F0", 0.8))
        }
        val ventanaDerecha = Rectangle(12.0, -82.0, 20.0, 16.0).apply {
            arcWidth = 4.0
            arcHeight = 4.0
            fill = Color.web("#9EF3FF", 0.9)
            effect = DropShadow(8.0, Color.web("#7FE3F0", 0.8))
        }

        val puerta = Rectangle(-14.0, -54.0, 28.0, 20.0).apply {
            arcWidth = 6.0
            arcHeight = 6.0
            fill = Color.web("#0A4C63")
            stroke = Color.web("#7FE3F0", 0.55)
            strokeWidth = 1.5
        }

        val antena = Rectangle(-2.0, -152.0, 4.0, 30.0).apply {
            fill = Color.web("#7FA8B5")
        }
        val soporteAntena = Rectangle(-16.0, -122.0, 32.0, 4.0).apply {
            fill = Color.web("#7FA8B5")
        }

        val cartel = Text(0.0, -70.0, "ESTACIÓN BASE").apply {
            font = Font.font("Segoe UI", FontWeight.BOLD, 13.0)
            fill = Color.web("#C8FAFF", 0.9)
            translateX = -50.0
        }

        return listOf(
            torre,
            techo,
            ventanaIzquierda,
            ventanaDerecha,
            puerta,
            antena,
            soporteAntena,
            cartel
        )
    }
}
