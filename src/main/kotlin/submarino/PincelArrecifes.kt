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
            dibujarRocaIndividual(i, cantidad, baseY, color)
        }
    }

    fun dibujarArrecifes(
        baseY: Double,
        color: Color,
        cantidad: Int
    ) {
        for (i in 0 until cantidad) {
            dibujarArrecifeIndividual(i, cantidad, baseY, color)
        }
    }

    fun dibujarAlgas(cantidad: Int) {
        for (i in 0 until cantidad) {
            dibujarAlgaIndividual(i, anchoMundo, altoMundo, random)
        }
    }

    fun dibujarCorales(cantidad: Int) {
        for (i in 0 until cantidad) {
            dibujarCoraloIndividual(i, anchoMundo, altoMundo, random)
        }
    }

    private fun dibujarRocaIndividual(i: Int, cantidad: Int, baseY: Double, color: Color) {
        val x = calcularX(i, cantidad)
        val (ancho, alto) = calcularDimensiones()
        val (lomo, sombra) = calcularColores(color)
        val xs = calcularXs(x, ancho)
        val ys = calcularYs(baseY, alto, random)
        gc.fill = crearGradienteLomo(baseY, alto, lomo, color, sombra)
        gc.fillPolygon(xs, ys, xs.size)
        gc.stroke = Color.web("#C9F4EA", 0.12)
        gc.lineWidth = 1.2
        gc.beginPath()
        gc.moveTo(xs[1], ys[1])
        for (indice in 2 until xs.size) {
            gc.lineTo(xs[indice], ys[indice])
        }
        gc.stroke()
    }

    private fun calcularX(i: Int, cantidad: Int): Double {
        return anchoMundo * (i + 0.2 + random.nextDouble() * 0.6) / cantidad
    }

    private fun calcularDimensiones(): Pair<Double, Double> {
        return Pair(
            110.0 + random.nextDouble() * 210.0,
            80.0 + random.nextDouble() * 230.0
        )
    }

    private fun calcularColores(color: Color): Pair<Color, Color> {
        return Pair(
            color.interpolate(Color.web("#B8E9DF"), 0.10),
            color.interpolate(Color.web("#00060A"), 0.45)
        )
    }

    private fun calcularXs(x: Double, ancho: Double): DoubleArray {
        return doubleArrayOf(
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
    }

    private fun calcularYs(baseY: Double, alto: Double, rand: Random): DoubleArray {
        return doubleArrayOf(
            baseY,
            baseY - alto * (0.18 + rand.nextDouble() * 0.12),
            baseY - alto * (0.38 + rand.nextDouble() * 0.14),
            baseY - alto * (0.58 + rand.nextDouble() * 0.20),
            baseY - alto * (0.42 + rand.nextDouble() * 0.18),
            baseY - alto * (0.72 + rand.nextDouble() * 0.24),
            baseY - alto * (0.30 + rand.nextDouble() * 0.18),
            baseY - alto * (0.12 + rand.nextDouble() * 0.12),
            baseY
        )
    }

    private fun crearGradienteLomo(baseY: Double, alto: Double, lomo: Color, color: Color, sombra: Color): LinearGradient {
        return LinearGradient(
            0.0, baseY - alto, 0.0, baseY,
            false,
            CycleMethod.NO_CYCLE,
            Stop(0.0, lomo),
            Stop(0.50, color),
            Stop(1.0, sombra)
        )
    }

    private fun dibujarArrecifeIndividual(i: Int, cantidad: Int, baseY: Double, color: Color) {
        val (ancho, alto) = calcularDimensiones()
        val (lomo, sombra) = calcularColores(color)
        val xs = calcularXs(calcularX(i, cantidad), ancho)
        val ys = calcularYs(baseY, alto, random)
        gc.fill = crearGradienteLomo(baseY, alto, lomo, color, sombra)
        gc.fillPolygon(xs, ys, xs.size)
        gc.stroke = Color.web("#C9F4EA", 0.12)
        gc.lineWidth = 1.2
        gc.beginPath()
        gc.moveTo(xs[1], ys[1])
        for (indice in 2 until xs.size) {
            gc.lineTo(xs[indice], ys[indice])
        }
        gc.stroke()
    }

    private fun dibujarAlgaIndividual(i: Int, anchoMundo: Double, altoMundo: Double, random: Random) {
        val x = random.nextDouble() * anchoMundo
        val altura = 55.0 + random.nextDouble() * 130.0
        gc.setStroke(Color.web("#0D4E57", 0.7))
        gc.setLineWidth(3.0)
        gc.beginPath()
        gc.moveTo(x, altoMundo * 0.62 + random.nextDouble() * 0.36)
        gc.quadraticCurveTo(x - 24.0, altoMundo * altura * 0.5, x + 12.0, altoMundo * altura * 0.78)
        gc.quadraticCurveTo(x + 34.0, altoMundo * altura * 0.9, x + 4.0, altoMundo * altura)
        gc.stroke()
    }

    private fun dibujarCoraloIndividual(i: Int, anchoMundo: Double, altoMundo: Double, random: Random) {
        val x = random.nextDouble() * anchoMundo
        val altura = 32.0 + random.nextDouble() * 80.0
        gc.setStroke(Color.web("#0B4B55", 0.75))
        gc.setLineWidth(2.2)
        gc.beginPath()
        gc.moveTo(x, altoMundo * 0.68 + random.nextDouble() * 0.30)
        gc.lineTo(x - altura * 0.22, altoMundo * altura * 0.55)
        gc.lineTo(x - altura * 0.38, altoMundo * altura * 0.82)
        gc.moveTo(x, altoMundo * altura * 0.35)
        gc.lineTo(x + altura * 0.24, altoMundo * altura * 0.7)
        gc.moveTo(x, altoMundo * altura * 0.55)
        gc.lineTo(x + altura * 0.12, altoMundo * altura)
        gc.stroke()
    }
}