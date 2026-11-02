package submarino

import javafx.animation.Animation
import javafx.animation.FadeTransition
import javafx.animation.ParallelTransition
import javafx.animation.ScaleTransition
import javafx.animation.TranslateTransition
import javafx.scene.Group
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.RadialGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Circle
import javafx.util.Duration
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Efecto vectorial de explosión del submarino.
 *
 * Combina un destello central, anillos expansivos y chispas radiales. Se coloca
 * en coordenadas del mundo y queda oculto hasta que la nave es destruida.
 */
class ExplosionSubmarino {

    val nodo: Group = Group().apply {
        isVisible = false
        isMouseTransparent = true
        isPickOnBounds = false
    }

    private var animacion: Animation? = null

    /**
     * Lanza la explosión en la posición indicada del mundo.
     */
    fun detonar(x: Double, y: Double) {
        detenerAnimaciones()
        nodo.layoutX = x
        nodo.layoutY = y
        nodo.children.clear()

        val transiciones = mutableListOf<Animation>()
        val random = Random(SEMILLA_CHISPAS)

        val destello = Circle(0.0, 0.0, RADIO_DESTELLO).apply {
            fill = RadialGradient(
                0.35, 0.35, 0.5, 0.5, 0.75,
                true,
                CycleMethod.NO_CYCLE,
                Stop(0.0, Color.web("#FFF6C9")),
                Stop(0.28, Color.web("#FFD166")),
                Stop(0.62, Color.web("#FF7A1A")),
                Stop(1.0, Color.web("#D6290B", 0.0))
            )
            effect = DropShadow(38.0, Color.web("#FF8A1E", 0.95))
        }
        nodo.children.add(destello)
        transiciones.add(escala(destello, Duration.seconds(0.85), 0.4, 7.5))
        transiciones.add(desvanecer(destello, Duration.seconds(0.85), 1.0, 0.0))

        repeat(ANILLOS) { indice ->
            val anillo = Circle(0.0, 0.0, RADIO_ANILLO + indice * 9.0).apply {
                fill = null
                stroke = if (indice % 2 == 0) {
                    Color.web("#FFC65C", 0.92)
                } else {
                    Color.web("#FF5A2A", 0.86)
                }
                strokeWidth = 7.0 - indice
            }
            nodo.children.add(anillo)
            val duracion = Duration.seconds(1.1 + indice * 0.22)
            transiciones.add(escala(anillo, duracion, 0.25, 4.8 + indice * 0.8))
            transiciones.add(desvanecer(anillo, duracion, 0.95, 0.0))
        }

        repeat(CHISPAS) {
            val angulo = random.nextDouble(0.0, PI * 2.0)
            val distancia = 55.0 + random.nextDouble() * 250.0
            val chispa = Circle(
                0.0,
                0.0,
                1.8 + random.nextDouble() * 4.2
            ).apply {
                fill = when (random.nextInt(3)) {
                    0 -> Color.web("#FFE08A")
                    1 -> Color.web("#FF9F1C")
                    else -> Color.web("#FF5A2A")
                }
                effect = DropShadow(12.0, Color.web("#FF9F1C", 0.85))
            }
            nodo.children.add(chispa)

            val duracion = Duration.seconds(0.65 + random.nextDouble() * 0.85)
            val desplazamiento = TranslateTransition(duracion, chispa).apply {
                byX = cos(angulo) * distancia
                byY = sin(angulo) * distancia
            }
            transiciones.add(desplazamiento)
            transiciones.add(desvanecer(chispa, duracion, 1.0, 0.0))
        }

        val total = ParallelTransition(*transiciones.toTypedArray()).apply {
            setOnFinished {
                nodo.isVisible = false
                nodo.children.clear()
            }
        }
        animacion = total
        nodo.isVisible = true
        total.play()
    }

    /**
     * Cancela la animación y oculta el efecto.
     */
    fun detenerAnimaciones() {
        animacion?.stop()
        animacion = null
        nodo.isVisible = false
        nodo.children.clear()
    }

    private fun escala(
        nodoAnimado: javafx.scene.Node,
        duracion: Duration,
        desde: Double,
        hasta: Double
    ): ScaleTransition = ScaleTransition(duracion, nodoAnimado).apply {
        fromX = desde
        fromY = desde
        toX = hasta
        toY = hasta
    }

    private fun desvanecer(
        nodoAnimado: javafx.scene.Node,
        duracion: Duration,
        desde: Double,
        hasta: Double
    ): FadeTransition = FadeTransition(duracion, nodoAnimado).apply {
        fromValue = desde
        toValue = hasta
    }

    companion object {
        private const val SEMILLA_CHISPAS = 77L
        private const val RADIO_DESTELLO = 26.0
        private const val RADIO_ANILLO = 24.0
        private const val ANILLOS = 3
        private const val CHISPAS = 34
    }
}

