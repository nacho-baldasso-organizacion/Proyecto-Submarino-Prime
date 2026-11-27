package submarino

import javafx.scene.Node
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle

internal object PlataformaEstacionVectorial {

    fun crear(): List<Node> {
        return listOf(
            crearPlataforma(),
            crearBase(),
            crearPilarIzquierdo(),
            crearPilarDerecho(),
            crearTravesano()
        )
    }

    private fun crearPlataforma(): Rectangle {
        return Rectangle(-150.0, -34.0, 300.0, 30.0).apply {
            arcWidth = 18.0
            arcHeight = 18.0
            fill = LinearGradient(
                0.0, 0.0, 0.0, 1.0,
                true,
                CycleMethod.NO_CYCLE,
                Stop(0.0, Color.web("#4F7F91")),
                Stop(0.45, Color.web("#285466")),
                Stop(1.0, Color.web("#112E3A"))
            )
            stroke = Color.web("#7FE3F0", 0.75)
            strokeWidth = 2.0
            effect = DropShadow(20.0, Color.web("#7FE3F0", 0.35))
        }
    }

    private fun crearBase(): Rectangle {
        return Rectangle(-92.0, -4.0, 184.0, 14.0).apply {
            arcWidth = 8.0
            arcHeight = 8.0
            fill = Color.web("#0C2631")
            stroke = Color.web("#3A7385")
            strokeWidth = 1.5
        }
    }

    private fun crearPilarIzquierdo(): Rectangle {
        return Rectangle(-104.0, -4.0, 12.0, 58.0).apply {
            fill = Color.web("#1A3E4C")
            stroke = Color.web("#3A7385")
            strokeWidth = 1.0
        }
    }

    private fun crearPilarDerecho(): Rectangle {
        return Rectangle(92.0, -4.0, 12.0, 58.0).apply {
            fill = Color.web("#1A3E4C")
            stroke = Color.web("#3A7385")
            strokeWidth = 1.0
        }
    }

    private fun crearTravesano(): Rectangle {
        return Rectangle(-112.0, 46.0, 224.0, 8.0).apply {
            arcWidth = 6.0
            arcHeight = 6.0
            fill = Color.web("#285466")
        }
    }

    fun crearLuces(): List<Circle> = listOf(-132.0, -104.0, 104.0, 132.0).map { x ->
        Circle(x, -36.0, 4.0).apply {
            fill = Color.web("#FFD75A")
            effect = DropShadow(10.0, Color.web("#FFD75A", 0.85))
        }
    }
}