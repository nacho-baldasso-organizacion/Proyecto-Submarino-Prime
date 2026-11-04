package submarino

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

/**
 * Dibuja una cordillera completa con luz, sombra y detalles internos.
 *
 * Responsabilidad única (SRP): construir la silueta de una cordillera y
 * delegar el interior en [DetallesCordillera].
 */
class PincelCordillera(
    private val gc: GraphicsContext,
    private val random: Random,
    private val anchoMundo: Double
) {

    fun dibujar(
        baseY: Double,
        amplitud: Double,
        color: Color,
        picos: Int,
        brillo: Double = 0.12
    ) {
        val cantidad = picos.coerceAtLeast(3)
        val controles = (0..cantidad).map { indice ->
            val x = anchoMundo * indice / cantidad
            val factor = if (indice == 0 || indice == cantidad) {
                0.04 + random.nextDouble() * 0.10
            } else {
                0.26 + random.nextDouble() * 0.74
            }
            x to (baseY - amplitud * factor)
        }

        val muestras = cantidad * 42
        val detalle = Random(random.nextLong())
        val xs = DoubleArray(muestras)
        val ys = DoubleArray(muestras)
        val fase = entre(detalle, 0.0, PI * 2.0)

        for (muestra in 0 until muestras) {
            val avance = muestra.toDouble() / (muestras - 1)
            val posicion = avance * cantidad
            val indice = posicion.toInt().coerceIn(0, cantidad - 1)
            val local = (posicion - indice).coerceIn(0.0, 1.0)
            val suave = (1.0 - cos(local * PI)) * 0.5
            val inicio = controles[indice].second
            val fin = controles[indice + 1].second
            val base = inicio + (fin - inicio) * suave
            val rugosidad = sin(avance * PI * cantidad * 7.0 + fase) * amplitud * 0.035 +
                entre(detalle, -1.0, 1.0) * amplitud * 0.018
            xs[muestra] = anchoMundo * avance
            ys[muestra] = (base + rugosidad).coerceIn(baseY - amplitud * 1.08, baseY)
        }

        val colorLuz = color.interpolate(Color.web("#C8FBEF"), brillo)
        val colorSombra = color.interpolate(Color.web("#00060A"), 0.36)
        gc.fill = LinearGradient(
            0.0, baseY - amplitud, 0.0, baseY,
            false,
            CycleMethod.NO_CYCLE,
            Stop(0.0, colorLuz),
            Stop(0.34, color),
            Stop(1.0, colorSombra)
        )
        gc.beginPath()
        gc.moveTo(xs[0], baseY)
        for (muestra in 0 until muestras) {
            gc.lineTo(xs[muestra], ys[muestra])
        }
        gc.lineTo(xs[muestras - 1], baseY)
        gc.closePath()
        gc.fill()

        val detalles = DetallesCordillera(gc, random, anchoMundo, PerfilCordillera(xs, ys))
        gc.save()
        gc.beginPath()
        gc.moveTo(xs[0], baseY)
        for (muestra in 0 until muestras) {
            gc.lineTo(xs[muestra], ys[muestra])
        }
        gc.lineTo(xs[muestras - 1], baseY)
        gc.closePath()
        gc.clip()

        detalles.dibujarEstratos(baseY)
        detalles.dibujarRocas(baseY, colorLuz, colorSombra)
        detalles.dibujarGrietas(baseY)
        detalles.dibujarSedimento(baseY)
        gc.restore()

        gc.stroke = Color.web("#D6FFF6", (brillo * 1.35).coerceIn(0.05, 0.32))
        gc.lineWidth = 1.6
        gc.beginPath()
        gc.moveTo(xs[0], ys[0])
        for (muestra in 1 until muestras) {
            gc.lineTo(xs[muestra], ys[muestra])
        }
        gc.stroke()

        detalles.dibujarRocasSueltas(baseY)
    }

    private fun entre(random: Random, desde: Double, hasta: Double): Double {
        val minimo = min(desde, hasta)
        val maximo = kotlin.math.max(desde, hasta)
        return if (minimo == maximo) minimo else random.nextDouble(minimo, maximo)
    }
}
