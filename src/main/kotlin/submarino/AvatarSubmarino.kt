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
import javafx.scene.shape.Rectangle

/**
 * Construye el avatar vectorial del submarino.
 *
 * Responsabilidad única (SRP): componer las figuras JavaFX que representan la nave.
 */
class AvatarSubmarino {

    fun crear(): Group {
        val haz = Polygon(
            46.0, -8.0,
            190.0, -36.0,
            190.0, 36.0,
            46.0, 8.0
        ).apply {
            fill = Color.web("#7FE3F0", 0.09)
        }

        val helice = Polygon(
            -52.0, -9.0,
            -64.0, -20.0,
            -60.0, 0.0,
            -64.0, 20.0,
            -52.0, 9.0
        ).apply {
            fill = Color.web("#B9821E")
            stroke = Color.web("#092A36")
            strokeWidth = 2.0
        }

        val aletaSuperior = Polygon(
            -18.0, -18.0,
            -30.0, -36.0,
            -4.0, -18.0
        ).apply {
            fill = Color.web("#D99A25")
            stroke = Color.web("#092A36")
            strokeWidth = 2.0
        }

        val aletaInferior = Polygon(
            -18.0, 18.0,
            -30.0, 36.0,
            -4.0, 18.0
        ).apply {
            fill = Color.web("#D99A25")
            stroke = Color.web("#092A36")
            strokeWidth = 2.0
        }

        val casco = Ellipse(0.0, 0.0, 46.0, 20.0).apply {
            fill = LinearGradient(
                0.0,
                0.0,
                0.0,
                1.0,
                true,
                CycleMethod.NO_CYCLE,
                Stop(0.0, Color.web("#FFE08A")),
                Stop(0.55, Color.web("#F4C542")),
                Stop(1.0, Color.web("#C98F1E"))
            )
            stroke = Color.web("#092A36")
            strokeWidth = 3.0
            effect = DropShadow(16.0, Color.web("#7FE3F0", 0.45))
        }

        val vela = Rectangle(-12.0, -36.0, 26.0, 18.0).apply {
            arcWidth = 8.0
            arcHeight = 8.0
            fill = Color.web("#E0A92F")
            stroke = Color.web("#092A36")
            strokeWidth = 2.5
        }

        val periscopio = Rectangle(-4.0, -54.0, 6.0, 18.0).apply {
            fill = Color.web("#D99A25")
            stroke = Color.web("#092A36")
            strokeWidth = 2.0
        }

        val ventana = Circle(18.0, 0.0, 8.0).apply {
            fill = Color.web("#9EF3FF")
            stroke = Color.web("#092A36")
            strokeWidth = 2.0
            effect = DropShadow(10.0, Color.web("#7FE3F0", 0.9))
        }

        val ventanaSecundaria = Circle(-4.0, 0.0, 5.0).apply {
            fill = Color.web("#7FE3F0")
            stroke = Color.web("#092A36")
            strokeWidth = 1.5
        }

        return Group().apply {
            children.addAll(
                haz,
                helice,
                aletaInferior,
                aletaSuperior,
                casco,
                vela,
                periscopio,
                ventana,
                ventanaSecundaria
            )
        }
    }
}
