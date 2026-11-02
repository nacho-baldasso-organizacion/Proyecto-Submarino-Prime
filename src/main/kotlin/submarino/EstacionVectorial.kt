package submarino

import javafx.animation.Animation
import javafx.animation.FadeTransition
import javafx.scene.Group
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Circle
import javafx.scene.shape.Polygon
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javafx.util.Duration

/**
 * Estación base flotante construida con formas vectoriales.
 *
 * Incluye plataforma, torre de control, luces de balizamiento y cartel.
 */
class EstacionVectorial {

    private val baliza: Circle = Circle(0.0, -112.0, 7.0).apply {
        fill = Color.web("#FF6B6B")
        effect = DropShadow(18.0, Color.web("#FF6B6B", 0.95))
    }
    private var animacionBaliza: FadeTransition? = null

    val nodo: Group = crearEstacion()

    init {
        iniciarBaliza()
    }

    /**
     * Detiene el parpadeo de la baliza cuando la vista deja de estar activa.
     */
    fun detenerAnimaciones() {
        animacionBaliza?.stop()
        animacionBaliza = null
    }

    private fun crearEstacion(): Group {
        val plataforma = Rectangle(-150.0, -34.0, 300.0, 30.0).apply {
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

        val base = Rectangle(-92.0, -4.0, 184.0, 14.0).apply {
            arcWidth = 8.0
            arcHeight = 8.0
            fill = Color.web("#0C2631")
            stroke = Color.web("#3A7385")
            strokeWidth = 1.5
        }

        val pilarIzquierdo = Rectangle(-104.0, -4.0, 12.0, 58.0).apply {
            fill = Color.web("#1A3E4C")
            stroke = Color.web("#3A7385")
            strokeWidth = 1.0
        }
        val pilarDerecho = Rectangle(92.0, -4.0, 12.0, 58.0).apply {
            fill = Color.web("#1A3E4C")
            stroke = Color.web("#3A7385")
            strokeWidth = 1.0
        }
        val travesano = Rectangle(-112.0, 46.0, 224.0, 8.0).apply {
            arcWidth = 6.0
            arcHeight = 6.0
            fill = Color.web("#285466")
        }

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

        val luces = listOf(-132.0, -104.0, 104.0, 132.0).map { x ->
            Circle(x, -36.0, 4.0).apply {
                fill = Color.web("#FFD75A")
                effect = DropShadow(10.0, Color.web("#FFD75A", 0.85))
            }
        }

        return Group().apply {
            isPickOnBounds = false
            children.addAll(
                pilarIzquierdo,
                pilarDerecho,
                travesano,
                base,
                plataforma,
                torre,
                techo,
                ventanaIzquierda,
                ventanaDerecha,
                puerta,
                antena,
                soporteAntena,
                cartel
            )
            children.addAll(luces)
            children.add(baliza)
        }
    }

    private fun iniciarBaliza() {
        val parpadeo = FadeTransition(Duration.seconds(0.85), baliza)
        parpadeo.fromValue = 0.25
        parpadeo.toValue = 1.0
        parpadeo.setAutoReverse(true)
        parpadeo.cycleCount = Animation.INDEFINITE
        animacionBaliza = parpadeo
        parpadeo.play()
    }
}
