package submarino

import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

/**
 * Fondo marino procedural dibujado con [Canvas].
 *
 * La capa lejana contiene montañas y arrecifes difusos; la capa media añade
 * siluetas más cercanas de arrecifes, algas y burbujas. Ambas capas se
 * desplazan con factores de parallax distintos desde la vista.
 */
class FondoAbisal(
    private val anchoMundo: Double,
    private val altoMundo: Double,
    private val semilla: Long = SEMILLA_DEFAULT,
    private val profundidadRayos: Double = altoMundo * 0.35
) {

    val capaLejana: Canvas = Canvas(anchoMundo, altoMundo)
    val capaMedia: Canvas = Canvas(anchoMundo, altoMundo)

    init {
        dibujarCapaLejana(Random(semilla))
        dibujarCapaMedia(Random(semilla + 1L))
    }

    private fun dibujarCapaLejana(random: Random) {
        val gc = capaLejana.graphicsContext2D

        gc.fill = LinearGradient(
            0.0, 0.0, 0.0, 1.0,
            true,
            CycleMethod.NO_CYCLE,
            Stop(0.0, Color.web("#57C7E3")),
            Stop(0.10, Color.web("#2E9DBC")),
            Stop(0.28, Color.web("#12667F")),
            Stop(0.52, Color.web("#073A4E")),
            Stop(0.76, Color.web("#031E2B")),
            Stop(1.0, Color.web("#01070B"))
        )
        gc.fillRect(0.0, 0.0, anchoMundo, altoMundo)

        dibujarSuperficie(gc)
        dibujarRayos(gc)
        dibujarCordillera(gc, altoMundo * 0.46, altoMundo * 0.20, Color.web("#0B4C60", 0.62), random, 9, 0.16)
        dibujarCordillera(gc, altoMundo * 0.56, altoMundo * 0.24, Color.web("#083A4B", 0.74), random, 12, 0.14)
        dibujarCordillera(gc, altoMundo * 0.68, altoMundo * 0.22, Color.web("#052B39", 0.84), random, 16, 0.12)
        dibujarCordillera(gc, altoMundo * 0.81, altoMundo * 0.19, Color.web("#031E28", 0.90), random, 21, 0.10)
        dibujarCordillera(gc, altoMundo * 0.93, altoMundo * 0.16, Color.web("#02121A", 0.95), random, 27, 0.08)
        dibujarBurbujas(gc, random, 90, 1.0, 3.2, 0.10)
    }

    private fun dibujarCapaMedia(random: Random) {
        val gc = capaMedia.graphicsContext2D

        dibujarArrecifes(gc, random, altoMundo * 0.58, Color.web("#0A3C47", 0.78), 10)
        dibujarEstructurasRocosas(gc, random, altoMundo * 0.64, Color.web("#07313D", 0.88), 8)
        dibujarArrecifes(gc, random, altoMundo * 0.76, Color.web("#062B35", 0.86), 14)
        dibujarEstructurasRocosas(gc, random, altoMundo * 0.82, Color.web("#04232D", 0.92), 12)
        dibujarArrecifes(gc, random, altoMundo * 0.92, Color.web("#031B23", 0.94), 18)
        dibujarEstructurasRocosas(gc, random, altoMundo * 0.95, Color.web("#02151D", 0.96), 16)
        dibujarAlgas(gc, random, 34)
        dibujarCorales(gc, random, 22)
        dibujarBurbujas(gc, random, 55, 1.4, 4.2, 0.16)
    }

    private fun dibujarSuperficie(gc: GraphicsContext) {
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

    private fun dibujarRayos(gc: GraphicsContext) {
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

    private fun dibujarEstructurasRocosas(
        gc: GraphicsContext,
        random: Random,
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

    private fun dibujarCorales(gc: GraphicsContext, random: Random, cantidad: Int) {
        gc.setStroke(Color.web("#0B4B55", 0.75))
        gc.setLineWidth(2.2)
        for (i in 0 until cantidad) {
            val x = random.entre(0.0, anchoMundo)
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

    private fun dibujarCordillera(
        gc: GraphicsContext,
        baseY: Double,
        amplitud: Double,
        color: Color,
        random: Random,
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
        val fase = detalle.entre(0.0, PI * 2.0)

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
                detalle.entre(-1.0, 1.0) * amplitud * 0.018
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

        gc.save()
        gc.beginPath()
        gc.moveTo(xs[0], baseY)
        for (muestra in 0 until muestras) {
            gc.lineTo(xs[muestra], ys[muestra])
        }
        gc.lineTo(xs[muestras - 1], baseY)
        gc.closePath()
        gc.clip()

        dibujarEstratosCordillera(gc, random, xs, ys, baseY)
        dibujarRocasCordillera(gc, random, xs, ys, baseY, colorLuz, colorSombra)
        dibujarGrietasCordillera(gc, random, xs, ys, baseY)
        dibujarSedimentoCordillera(gc, random, xs, ys, baseY)
        gc.restore()

        gc.stroke = Color.web("#D6FFF6", (brillo * 1.35).coerceIn(0.05, 0.32))
        gc.lineWidth = 1.6
        gc.beginPath()
        gc.moveTo(xs[0], ys[0])
        for (muestra in 1 until muestras) {
            gc.lineTo(xs[muestra], ys[muestra])
        }
        gc.stroke()

        dibujarRocasSueltasCordillera(gc, random, xs, ys, baseY)
    }

    private fun alturaRidges(xs: DoubleArray, ys: DoubleArray, x: Double): Double {
        if (x <= xs.first()) {
            return ys.first()
        }
        if (x >= xs.last()) {
            return ys.last()
        }
        var bajo = 0
        var alto = xs.size - 1
        while (alto - bajo > 1) {
            val medio = (bajo + alto) / 2
            if (xs[medio] <= x) {
                bajo = medio
            } else {
                alto = medio
            }
        }
        val ancho = xs[alto] - xs[bajo]
        if (ancho <= 0.0) {
            return min(ys[bajo], ys[alto])
        }
        val avance = (x - xs[bajo]) / ancho
        return ys[bajo] + (ys[alto] - ys[bajo]) * avance
    }

    private fun dibujarEstratosCordillera(
        gc: GraphicsContext,
        random: Random,
        xs: DoubleArray,
        ys: DoubleArray,
        baseY: Double
    ) {
        gc.lineWidth = 1.3
        repeat(18) { indice ->
            val altura = random.entre(baseY * 0.18, baseY * 0.92)
            gc.stroke = if (indice % 4 == 0) {
                Color.web("#BFF6FF", 0.035)
            } else {
                Color.web("#011015", random.entre(0.07, 0.17))
            }
            gc.beginPath()
            var x = 0.0
            var primero = true
            while (x <= anchoMundo) {
                val superficie = alturaRidges(xs, ys, x)
                val y = max(superficie + 4.0, altura + sin(x * 0.011 + indice * 1.4) * 7.0)
                if (primero) {
                    gc.moveTo(x, y)
                    primero = false
                } else {
                    gc.lineTo(x, y)
                }
                x += 22.0
            }
            gc.stroke()
        }
    }

    private fun dibujarRocasCordillera(
        gc: GraphicsContext,
        random: Random,
        xs: DoubleArray,
        ys: DoubleArray,
        baseY: Double,
        colorLuz: Color,
        colorSombra: Color
    ) {
        repeat(34) {
            val x = random.entre(0.0, anchoMundo)
            val superficie = alturaRidges(xs, ys, x)
            val y = random.entre(superficie + 5.0, baseY)
            val ancho = random.entre(5.0, 28.0)
            val alto = ancho * random.entre(0.5, 1.3)
            val claro = random.nextBoolean()
            gc.fill = if (claro) {
                colorLuz.deriveColor(0.0, 1.0, 1.0, 0.08)
            } else {
                colorSombra.deriveColor(0.0, 1.0, 1.0, 0.28)
            }
            gc.fillOval(x, y, ancho, alto)
            if (claro) {
                gc.fill = Color.web("#E3FFF8", 0.045)
                gc.fillOval(x + ancho * 0.12, y + alto * 0.10, ancho * 0.44, alto * 0.30)
            }
        }
    }

    private fun dibujarGrietasCordillera(
        gc: GraphicsContext,
        random: Random,
        xs: DoubleArray,
        ys: DoubleArray,
        baseY: Double
    ) {
        gc.lineWidth = 1.1
        gc.stroke = Color.web("#01090D", 0.42)
        repeat(16) {
            val x = random.entre(0.0, anchoMundo)
            val superficie = alturaRidges(xs, ys, x)
            val fin = random.entre(superficie + 18.0, baseY)
            gc.beginPath()
            gc.moveTo(x, superficie + 2.0)
            gc.lineTo(x + random.entre(-14.0, 14.0), fin)
            gc.stroke()
        }
    }

    private fun dibujarSedimentoCordillera(
        gc: GraphicsContext,
        random: Random,
        xs: DoubleArray,
        ys: DoubleArray,
        baseY: Double
    ) {
        repeat(20) {
            val x = random.entre(0.0, anchoMundo)
            val superficie = alturaRidges(xs, ys, x)
            val y = random.entre(superficie + (baseY - superficie) * 0.55, baseY)
            val ancho = random.entre(12.0, 54.0)
            gc.fill = if (random.nextBoolean()) {
                Color.web("#453821", random.entre(0.05, 0.13))
            } else {
                Color.web("#2C2A1D", random.entre(0.07, 0.16))
            }
            gc.fillOval(x, y, ancho, ancho * 0.34)
        }
    }

    private fun dibujarRocasSueltasCordillera(
        gc: GraphicsContext,
        random: Random,
        xs: DoubleArray,
        ys: DoubleArray,
        baseY: Double
    ) {
        repeat(12) {
            val x = random.entre(0.0, anchoMundo)
            val superficie = alturaRidges(xs, ys, x)
            val radio = random.entre(4.0, 16.0)
            gc.fill = Color.web("#02141A", 0.82)
            gc.fillOval(x - radio, superficie - radio * 0.55, radio * 2.0, radio * 1.2)
            gc.fill = Color.web("#8FD6C8", 0.10)
            gc.fillOval(x - radio * 0.7, superficie - radio * 0.52, radio * 0.9, radio * 0.35)
        }
    }

    private fun dibujarArrecifes(
        gc: GraphicsContext,
        random: Random,
        baseY: Double,
        color: Color,
        cantidad: Int
    ) {
        gc.fill = color
        for (i in 0 until cantidad) {
            val x = anchoMundo * (i + 0.35 + random.nextDouble() * 0.3) / cantidad
            val radio = 28.0 + random.nextDouble() * 70.0
            val alto = radio * (0.7 + random.nextDouble() * 0.8)
            gc.fillOval(x - radio, baseY - alto, radio * 2.0, alto * 2.0)
            gc.fillOval(x - radio * 0.55, baseY - alto * 1.4, radio * 1.1, alto * 1.5)
        }
    }

    private fun dibujarAlgas(gc: GraphicsContext, random: Random, cantidad: Int) {
        gc.setStroke(Color.web("#0D4E57", 0.7))
        gc.setLineWidth(3.0)
        for (i in 0 until cantidad) {
            val x = random.entre(0.0, anchoMundo)
            val baseY = altoMundo * (0.62 + random.nextDouble() * 0.36)
            val altura = 55.0 + random.nextDouble() * 130.0
            gc.beginPath()
            gc.moveTo(x, baseY)
            gc.quadraticCurveTo(x - 24.0, baseY - altura * 0.5, x + 12.0, baseY - altura * 0.78)
            gc.quadraticCurveTo(x + 34.0, baseY - altura * 0.9, x + 4.0, baseY - altura)
            gc.stroke()
        }
    }

    private fun dibujarBurbujas(
        gc: GraphicsContext,
        random: Random,
        cantidad: Int,
        radioMin: Double,
        radioMax: Double,
        alfaBase: Double
    ) {
        for (i in 0 until cantidad) {
            val x = random.entre(0.0, anchoMundo)
            val y = random.entre(0.0, altoMundo)
            val radio = radioMin + random.nextDouble() * (radioMax - radioMin)
            gc.fill = Color.web("#B8F3FF", alfaBase + random.nextDouble() * 0.12)
            gc.fillOval(x, y, radio, radio)

        }
    }

    private fun Random.entre(desde: Double, hasta: Double): Double {
        val minimo = min(desde, hasta)
        val maximo = max(desde, hasta)
        return if (minimo == maximo) minimo else nextDouble(minimo, maximo)
    }

    companion object {
        private const val SEMILLA_DEFAULT = 7L
        private const val RAYOS = 6
    }
}
