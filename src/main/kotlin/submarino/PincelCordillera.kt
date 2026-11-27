package submarino

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import kotlin.math.PI
import kotlin.random.Random

/**
 * Dibuja una cordillera completa con luz, sombra y detalles internos.
 *
 * Responsabilidad única (SRP): construir la siluete de una cordillera y
 * delegar el interior en [DetallesCordillera].
 */
class PincelCordillera(
    private val gc: GraphicsContext,
    private val random: Random,
    private val anchoMundo: Double
) {

    data class ParametrosCordillera(
        val baseY: Double,
        val amplitud: Double,
        val color: Color,
        val picos: Int,
        val brillo: Double = 0.12,
        val random: Random
    )

    fun dibujar(parametros: ParametrosCordillera) {
        val controles = calcularControles(parametros)
        val (xs, ys) = generarMuestras(parametros, controles)
        val perfil = PerfilCordillera(xs, ys)
        val detalles = DetallesCordillera(gc, random, anchoMundo, perfil)
        val colorLuz = parametros.color.interpolate(Color.web("#C8FBEF"), parametros.brillo)
        val colorSombra = parametros.color.interpolate(Color.web("#00060A"), 0.36)
        dibujarSiluetaBase(parametros, xs, ys, colorLuz, colorSombra)
        dibujarDetalles(detalles, parametros.baseY, colorLuz, colorSombra)
        dibujarBordeRocoso(xs, ys, parametros.brillo)
        dibujarRocasSueltas(detalles, parametros.baseY)
    }

    private fun calcularControles(parametros: ParametrosCordillera): List<Pair<Double, Double>> {
        val cantidad = parametros.picos.coerceAtLeast(3)
        return (0..cantidad).map { indice ->
            val x = anchoMundo * indice / cantidad
            val factor = if (indice == 0 || indice == cantidad) {
                0.04 + random.nextDouble() * 0.10
            } else {
                0.26 + random.nextDouble() * 0.74
            }
            x to (parametros.baseY - parametros.amplitud * factor)
        }
    }

    private fun generarMuestras(parametros: ParametrosCordillera, controles: List<Pair<Double, Double>>): Pair<DoubleArray, DoubleArray> {
        val cantidad = controlsSize(controles)
        val muestras = cantidad * 42
        val detalle = Random(parametros.random.nextLong())
        val xs = DoubleArray(muestras)
        val ys = DoubleArray(muestras)
        val fase = kotlin.math.PI * 2.0

        for (muestra in 0 until muestras) {
            val avance = muestra.toDouble() / (muestras - 1)
            val posicion = avance * cantidad
            val indice = posicion.toInt().coerceIn(0, cantidad - 1)
            val local = (posicion - indice).coerceIn(0.0, 1.0)
            val suave = (1.0 - kotlin.math.cos(local * PI)) * 0.5
            val inicio = controles[indice].second
            val fin = controles[indice + 1].second
            val base = inicio + (fin - inicio) * suave
            val rugosidad = kotlin.math.sin(avance * PI * cantidad * 7.0 + fase) * parametros.amplitud * 0.035 +
                entre(detalle, -1.0, 1.0) * parametros.amplitud * 0.018
            xs[muestra] = anchoMundo * avance
            ys[muestra] = (base + rugosidad).coerceIn(parametros.baseY - parametros.amplitud * 1.08, parametros.baseY)
        }
        return xs to ys
    }

    private fun controlsSize(controles: List<Pair<Double, Double>>): Int {
        return (controles.size - 1)
    }

    private fun dibujarSiluetaBase(parametros: ParametrosCordillera, xs: DoubleArray, ys: DoubleArray, colorLuz: Color, colorSombra: Color) {
        gc.fill = LinearGradient(
            0.0, parametros.baseY - parametros.amplitud, 0.0, parametros.baseY,
            false,
            CycleMethod.NO_CYCLE,
            Stop(0.0, colorLuz),
            Stop(0.34, parametros.color),
            Stop(1.0, colorSombra)
        )
        gc.beginPath()
        gc.moveTo(xs[0], parametros.baseY)
        for (muestra in 0 until xs.size) {
            gc.lineTo(xs[muestra], ys[muestra])
        }
        gc.lineTo(xs[xs.size - 1], parametros.baseY)
        gc.closePath()
        gc.fill()

        gc.save()
        gc.beginPath()
        gc.moveTo(xs[0], parametros.baseY)
        for (muestra in 0 until xs.size) {
            gc.lineTo(xs[muestra], ys[muestra])
        }
        gc.lineTo(xs[xs.size - 1], parametros.baseY)
        gc.closePath()
        gc.clip()
    }

    private fun dibujarDetalles(detalles: DetallesCordillera, baseY: Double, colorLuz: Color, colorSombra: Color) {
        detalles.dibujarEstratos(baseY)
        detalles.dibujarRocas(baseY, colorLuz, colorSombra)
        detalles.dibujarGrietas(baseY)
        detalles.dibujarSedimento(baseY)
    }

    private fun dibujarBordeRocoso(xs: DoubleArray, ys: DoubleArray, brillo: Double) {
        gc.stroke = Color.web("#D6FFF6", (brillo * 1.35).coerceIn(0.05, 0.32))
        gc.lineWidth = 1.6
        gc.beginPath()
        gc.moveTo(xs[0], ys[0])
        for (muestra in 1 until xs.size) {
            gc.lineTo(xs[muestra], ys[muestra])
        }
        gc.stroke()
    }

    private fun dibujarRocasSueltas(detalles: DetallesCordillera, baseY: Double) {
        detalles.dibujarRocasSueltas(baseY)
    }

    private fun entre(random: Random, desde: Double, hasta: Double): Double {
        val minimo = if (desde <= hasta) desde else hasta
        val maximo = if (desde >= hasta) desde else hasta
        return if (minimo == maximo) minimo else random.nextDouble(minimo, maximo)
    }
}