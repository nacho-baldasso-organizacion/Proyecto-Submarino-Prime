package submarino

import javafx.animation.Animation
import javafx.animation.RotateTransition
import javafx.scene.Group
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.RadialGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Circle
import javafx.scene.shape.Ellipse
import javafx.scene.shape.Polygon
import javafx.scene.shape.Rectangle
import javafx.util.Duration

/**
 * Submarino estilizado construido exclusivamente con nodos vectoriales.
 *
 * El cuerpo es dorado, la escotilla es azul brillante y el foco proyector se
 * representa con un haz semitransparente. No utiliza imágenes externas.
 */
class SubmarinoVectorial {

    private val helice: Group = crearHelice()
    private var animacionHelice: RotateTransition? = null
    val nodo: Group = crearSubmarino()

    init {
        iniciarAnimacionHelice()
    }

    /**
     * Orienta el vehículo según la dirección horizontal de navegación.
     */
    fun actualizarDireccion(direccionHorizontal: Double) {
        nodo.scaleX = if (direccionHorizontal < 0.0) -ESCALA else ESCALA
        nodo.scaleY = ESCALA
    }

    /**
     * Detiene la animación de la hélice cuando la vista deja de estar activa.
     */
    fun detenerAnimaciones() {
        animacionHelice?.stop()
        animacionHelice = null
    }

    private fun crearSubmarino(): Group {
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

        val aletaSuperior = Polygon(
            -22.0, -20.0,
            -38.0, -48.0,
            -4.0, -20.0
        ).apply {
            fill = Color.web("#D99A25")
            stroke = COLOR_CONTORNO
            strokeWidth = 2.0
        }

        val aletaInferior = Polygon(
            -22.0, 20.0,
            -38.0, 48.0,
            -4.0, 20.0
        ).apply {
            fill = Color.web("#D99A25")
            stroke = COLOR_CONTORNO
            strokeWidth = 2.0
        }

        val casco = Ellipse(0.0, 0.0, 62.0, 24.0).apply {
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

        val franja = Rectangle(-46.0, 9.0, 92.0, 5.0).apply {
            arcWidth = 5.0
            arcHeight = 5.0
            fill = Color.web("#A86D12", 0.75)
        }

        val vela = Rectangle(-18.0, -42.0, 36.0, 20.0).apply {
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

        val periscopio = Rectangle(-9.0, -56.0, 7.0, 16.0).apply {
            fill = Color.web("#D99A25")
            stroke = COLOR_CONTORNO
            strokeWidth = 2.0
        }

        val antena = Rectangle(-7.0, -68.0, 3.0, 14.0).apply {
            fill = Color.web("#B9821E")
        }
        val luzAntena = Circle(-5.5, -70.0, 3.0).apply {
            fill = Color.web("#FF6B6B")
            effect = DropShadow(8.0, Color.web("#FF6B6B", 0.9))
        }

        val escotilla = Circle(18.0, 0.0, 12.0).apply {
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

        val aroEscotilla = Circle(18.0, 0.0, 14.5).apply {
            fill = null
            stroke = Color.web("#C8FAFF", 0.65)
            strokeWidth = 1.5
        }

        val ventanaSecundaria = Circle(-8.0, 0.0, 5.5).apply {
            fill = Color.web("#7FE3F0")
            stroke = COLOR_CONTORNO
            strokeWidth = 1.5
            effect = DropShadow(7.0, Color.web("#7FE3F0", 0.6))
        }

        val ventanaTrasera = Circle(-30.0, 0.0, 4.5).apply {
            fill = Color.web("#5EC9DD")
            stroke = COLOR_CONTORNO
            strokeWidth = 1.2
        }

        val foco = Circle(60.0, 0.0, 4.5).apply {
            fill = Color.web("#E8FFFF")
            effect = DropShadow(12.0, Color.web("#C8FAFF", 1.0))
        }

        return Group().apply {
            isPickOnBounds = false
            children.addAll(
                hazExterior,
                hazInterior,
                helice,
                aletaInferior,
                aletaSuperior,
                casco,
                franja,
                vela,
                periscopio,
                antena,
                luzAntena,
                escotilla,
                aroEscotilla,
                ventanaSecundaria,
                ventanaTrasera,
                foco
            )
        }
    }

    private fun crearHelice(): Group {
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

    private fun iniciarAnimacionHelice() {
        val giro = RotateTransition(Duration.seconds(0.85), helice)
        giro.byAngle = 360.0
        giro.cycleCount = Animation.INDEFINITE
        animacionHelice = giro
        giro.play()
    }

    companion object {
        private const val ESCALA = 1.0
        private val COLOR_CONTORNO = Color.web("#08232E")
    }
}
