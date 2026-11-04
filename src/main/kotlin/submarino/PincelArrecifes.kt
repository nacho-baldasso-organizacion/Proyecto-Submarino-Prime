package submarino

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 * Pinta arrecifes, estructuras rocosas, algas y corales del fondo medio.
 *
 * Responsabilidad única (SRP): vegetación y relieves de la capa intermedia.
 */
class PincelArrecifes(
    private val gc: GraphicsContext,
    private val random: Random,
    private val anchoMundo: Double,
    private val altoMundo: Double
) {

    fun dibujarEstructurasRocosas(
        baseY: Double,
        color: Color,
        cantidad: Int
    ) {
        for (i in 0 until cantidad) {
            val x = anchoMundo * (i + 0.2 + random.nextDouble() * 0.6) / cantidad
            val ancho = 110.0 + random.nextDouble() * 210.0
            val alto = 80.0 + random.nextDouble() * 230.0
            val lomo = color.interpolate(Color.web("#B8E9DF"), 0.10)
            val sombra = color.interpolate(Color.web("#00060A"), 0.45)
            val xs = doubleArrayOf(
                x - ancho * 0.5,
                x - ancho * 0.36,
                x - ancho * 0.24,
                x - ancho * 0.09,
                x + ancho * 0.04,
                x + ancho * 0.18,
                x + ancho * 0.33,
                x + ancho * 0.46,
                x + ancho * 0.5
            )
            val ys = doubleArrayOf(
                baseY,
                baseY - alto * (0.18 + random.nextDouble() * 0.12),
                baseY - alto * (0.38 + random.nextDouble() * 0.14),
                baseY - alto * (0.58 + random.nextDouble() * 0.20),
                baseY - alto * (0.42 + random.nextDouble() * 0.18),
                baseY - alto * (0.72 + random.nextDouble() * 0.24),
                baseY - alto * (0.30 + random.nextDouble() * 0.18),
                baseY - alto * (0.12 + random.nextDouble() * 0.12),
                baseY
            )
            gc.fill = LinearGradient(
                0.0, baseY - alto, 0.0, baseY,
                false,
                CycleMethod.NO_CYCLE,
                Stop(0.0, lomo),
                Stop(0.50, color),
                Stop(1.0, sombra)
            )
            gc.fillPolygon(xs, ys, xs.size)

            gc.stroke = Color.web("#C9F4EA", 0.12)
            gc.lineWidth = 1.2
            gc.beginPath()
            for (indice in 1 until xs.size - 1) {
                if (indice == 1) {
                    gc.moveTo(xs[indice], ys[indice])
                } else {
                    gc.lineTo(xs[indice], ys[indice])
                }
            }
            gc.stroke()
        }
    }

    fun dibujarArrecifes(baseY: Double, color: Color, cantidad: Int) {
        gc.fill = color
        for (i in 0 until cantidad) {
            val x = anchoMundo * (i + 0.35 + random.nextDouble() * 0.3) / cantidad
            val radio = 28.0 + random.nextDouble() * 70.0
            val alto = radio * (0.7 + random.nextDouble() * 0.8)
            gc.fillOval(x - radio, baseY - alto, radio * 2.0, alto * 2.0)
            gc.fillOval(x - radio * 0.55, baseY - alto * 1.4, radio * 1.1, alto * 1.5)
        }
    }

    fun dibujarAlgas(cantidad: Int) {
        gc.setStroke(Color.web("#0D4E57", 0.7))
        gc.setLineWidth(3.0)
        for (i in 0 until cantidad) {
            val x = entre(0.0, anchoMundo)
            val baseY = altoMundo * (0.62 + random.nextDouble() * 0.36)
            val altura = 55.0 + random.nextDouble() * 130.0
            gc.beginPath()
            gc.moveTo(x, baseY)
            gc.quadraticCurveTo(x - 24.0, baseY - altura * 0.5, x + 12.0, baseY - altura * 0.78)
            gc.quadraticCurveTo(x + 34.0, baseY - altura * 0.9, x + 4.0, baseY - altura)
            gc.stroke()
        }
    }

    fun dibujarCorales(cantidad: Int) {
        gc.setStroke(Color.web("#0B4B55", 0.75))
        gc.setLineWidth(2.2)
        for (i in 0 until cantidad) {
            val x = entre(0.0, anchoMundo)
            val baseY = altoMundo * (0.68 + random.nextDouble() * 0.30)
            val altura = 32.0 + random.nextDouble() * 80.0
            gc.beginPath()
            gc.moveTo(x, baseY)
            gc.lineTo(x - altura * 0.22, baseY - altura * 0.55)
            gc.lineTo(x - altura * 0.38, baseY - altura * 0.82)
            gc.moveTo(x, baseY - altura * 0.35)
            gc.lineTo(x + altura * 0.24, baseY - altura * 0.7)
            gc.moveTo(x, baseY - altura * 0.55)
            gc.lineTo(x + altura * 0.12, baseY - altura)
            gc.stroke()
        }
    }

    private fun entre(desde: Double, hasta: Double): Double {
        val minimo = min(desde, hasta)
        val maximo = max(desde, hasta)
        return if (minimo == maximo) minimo else random.nextDouble(minimo, maximo)
    }
}
