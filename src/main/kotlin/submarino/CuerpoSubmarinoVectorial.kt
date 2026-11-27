package submarino

import javafx.scene.Group
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Circle
import javafx.scene.shape.Ellipse
import javafx.scene.shape.Polygon
import javafx.scene.paint.RadialGradient
import javafx.scene.shape.Rectangle

internal object CuerpoSubmarinoVectorial {

    private val COLOR_CONTORNO = Color.web("#08232E")

    private fun crearAletaSuperior(): Polygon {
        return Polygon(
            -22.0, -20.0,
            -38.0, -48.0,
            -4.0, -20.0
        ).apply {
            fill = Color.web("#D99A25")
            stroke = COLOR_CONTORNO
            strokeWidth = 2.0
        }
    }

    private fun crearAletaInferior(): Polygon {
        return Polygon(
            -22.0, 20.0,
            -38.0, 48.0,
            -4.0, 20.0
        ).apply {
            fill = Color.web("#D99A25")
            stroke = COLOR_CONTORNO
            strokeWidth = 2.0
        }
    }

    private fun crearCasco(): Ellipse {
        return Ellipse(0.0, 0.0, 62.0, 24.0).apply {
            fill = LinearGradient(
                0.0, 0.0, 0.0, 1.0,
                true,
                CycleMethod.NO_CYCLE,
                Stop(0.0, Color.web("#FFF0B0")),
                Stop(0.35, Color.web("#FFD75A")),
                Stop(0.72, Color.web("#E7A928")),
                Stop(1.0, Color.web("#A86D12"))
            )
            stroke = COLOR_CONTORNO
            strokeWidth = 3.0
            effect = DropShadow(18.0, Color.web("#7FE3F0", 0.38))
        }
    }

    private fun crearFranja(): Rectangle {
        return Rectangle(-46.0, 9.0, 92.0, 5.0).apply {
            arcWidth = 5.0
            arcHeight = 5.0
            fill = Color.web("#A86D12", 0.75)
        }
    }

    private fun crearVela(): Rectangle {
        return Rectangle(-18.0, -42.0, 36.0, 20.0).apply {
            arcWidth = 10.0
            arcHeight = 10.0
            fill = LinearGradient(
                0.0, 0.0, 0.0, 1.0,
                true,
                CycleMethod.NO_CYCLE,
                Stop(0.0, Color.web("#FFE08A")),
                Stop(1.0, Color.web("#C98F1E"))
            )
            stroke = COLOR_CONTORNO
            strokeWidth = 2.5
        }
    }

    private fun crearPeriscopio(): Rectangle {
        return Rectangle(-9.0, -56.0, 7.0, 16.0).apply {
            fill = Color.web("#D99A25")
            stroke = COLOR_CONTORNO
            strokeWidth = 2.0
        }
    }

    private fun crearAntenaeryLuz(): Pair<Rectangle, Circle> {
        val antena = Rectangle(-7.0, -68.0, 3.0, 14.0).apply {
            fill = Color.web("#B9821E")
        }
        val luzAntena = Circle(-5.5, -70.0, 3.0).apply {
            fill = Color.web("#FF6B6B")
            effect = DropShadow(8.0, Color.web("#FF6B6B", 0.9))
        }
        return Pair(antena, luzAntena)
    }

    private fun crearEscotilla(): Circle {
        return Circle(18.0, 0.0, 12.0).apply {
            fill = RadialGradient(
                0.35, 0.30, 0.65, 0.70, 0.75,
                true,
                CycleMethod.NO_CYCLE,
                Stop(0.0, Color.web("#F2FFFF")),
                Stop(0.35, Color.web("#9EF3FF")),
                Stop(0.75, Color.web("#2DA9C9")),
                Stop(1.0, Color.web("#0A4C63"))
            )
            stroke = Color.web("#06232E")
            strokeWidth = 2.0
            effect = DropShadow(14.0, Color.web("#7FE3F0", 0.95))
        }
    }

    private fun crearAroEscotilla(): Circle {
        return Circle(18.0, 0.0, 14.5).apply {
            fill = null
            stroke = Color.web("#C8FAFF", 0.65)
            strokeWidth = 1.5
        }
    }

    private fun crearVentanaSecundaria(): Circle {
        return Circle(-8.0, 0.0, 5.5).apply {
            fill = Color.web("#7FE3F0")
            stroke = COLOR_CONTORNO
            strokeWidth = 1.5
            effect = DropShadow(7.0, Color.web("#7FE3F0", 0.6))
        }
    }

    private fun crearVentanaTrasera(): Circle {
        return Circle(-30.0, 0.0, 4.5).apply {
            fill = Color.web("#5EC9DD")
            stroke = COLOR_CONTORNO
            strokeWidth = 1.2
        }
    }

    private fun crearFoco(): Circle {
        return Circle(60.0, 0.0, 4.5).apply {
            fill = Color.web("#E8FFFF")
            effect = DropShadow(12.0, Color.web("#C8FAFF", 1.0))
        }
    }

    fun crear(helice: Group): Group {
        val group = Group().apply {
            isPickOnBounds = false
            children.addAll(HazProyectorSubmarinoVectorial.crear())
            children.addAll(
                crearAletaSuperior(),
                crearAletaInferior(),
                crearCasco(),
                crearFranja(),
                crearVela(),
                crearPeriscopio(),
                crearAntenaeryLuz().first,
                crearAntenaeryLuz().second,
                crearEscotilla(),
                crearAroEscotilla(),
                crearVentanaSecundaria(),
                crearVentanaTrasera(),
                crearFoco()
            )
        }
        return group
    }
}