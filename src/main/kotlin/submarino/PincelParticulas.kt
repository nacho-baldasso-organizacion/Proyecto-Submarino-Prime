package submarino

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

/**
 * Pinta la superficie del agua, los rayos de luz y las burbujas.
 *
 * Responsabilidad única (SRP): efectos atmosféricos y partículas del fondo.
 */
class PincelParticulas(
    private val gc: GraphicsContext,
    private val random: Random,
    private val anchoMundo: Double,
    private val altoMundo: Double,
    private val profundidadRayos: Double
) {

    fun dibujarSuperficie() {
        val altura = (altoMundo * 0.045).coerceAtLeast(18.0)
        gc.fill = LinearGradient(
            0.0, 0.0, 0.0, 1.0,
            true,
            CycleMethod.NO_CYCLE,
            Stop(0.0, Color.web("#D8FBFF", 0.42)),
            Stop(0.45, Color.web("#8DE9FF", 0.16)),
            Stop(1.0, Color.web("#57C7E3", 0.0))
        )
        gc.fillRect(0.0, 0.0, anchoMundo, altura)

        gc.lineWidth = 1.8
        for (capa in 0..2) {
            gc.stroke = Color.web("#DFFBFF", 0.24 - capa * 0.06)
            gc.beginPath()
            var x = 0.0
            while (x <= anchoMundo) {
                val y = altura * 0.55 + capa * 7.0 +
                    sin(x * 0.012 + capa * 1.7) * (3.0 + capa * 1.6)
                if (x == 0.0) {
                    gc.moveTo(x, y)
                } else {
                    gc.lineTo(x, y)
                }
                x += 14.0
            }
            gc.stroke()
        }
    }

    fun dibujarRayos() {
        val fin = profundidadRayos.coerceIn(1.0, altoMundo)
        val gradiente = LinearGradient(
            0.0, 0.0, 0.0, 1.0,
            true,
            CycleMethod.NO_CYCLE,
            Stop(0.0, Color.web("#E6FDFF", 0.20)),
            Stop(0.35, Color.web("#B6F4FF", 0.10)),
            Stop(0.72, Color.web("#8DE9FF", 0.035)),
            Stop(1.0, Color.web("#8DE9FF", 0.0))
        )
        gc.fill = gradiente
        for (i in 0 until RAYOS) {
            val x = (i + 0.35) * anchoMundo / RAYOS
            val anchoRayo = 70.0 + (i % 4) * 55.0
            val inclinacion = anchoRayo * (1.4 + (i % 3) * 0.35)
            gc.fillPolygon(
                doubleArrayOf(x, x + anchoRayo, x + anchoRayo + inclinacion, x + inclinacion),
                doubleArrayOf(0.0, 0.0, fin, fin),
                4
            )
        }
    }

    fun dibujarBurbujas(
        cantidad: Int,
        radioMin: Double,
        radioMax: Double,
        alfaBase: Double
    ) {
        for (i in 0 until cantidad) {
            val x = entre(0.0, anchoMundo)
            val y = entre(0.0, altoMundo)
            val radio = radioMin + random.nextDouble() * (radioMax - radioMin)
            gc.fill = Color.web("#B8F3FF", alfaBase + random.nextDouble() * 0.12)
            gc.fillOval(x, y, radio, radio)
        }
    }

    private fun entre(desde: Double, hasta: Double): Double {
        val minimo = min(desde, hasta)
        val maximo = max(desde, hasta)
        return if (minimo == maximo) minimo else random.nextDouble(minimo, maximo)
    }

    companion object {
        private const val RAYOS = 6
    }
}
