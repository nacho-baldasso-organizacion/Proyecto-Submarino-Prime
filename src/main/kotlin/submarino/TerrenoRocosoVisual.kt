package submarino

import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

class TerrenoRocosoVisual(
    private val terreno: TerrenoSubmarino,
    anchoMundo: Double,
    altoMundo: Double,
    private val escala: Double,
    semilla: Long = SEMILLA_DEFAULT
) {

    val nodo: Canvas = Canvas(anchoMundo, altoMundo).apply {
        isMouseTransparent = true
    }

    private val gc: GraphicsContext = nodo.graphicsContext2D
    private val random = Random(semilla)

    init {
        dibujar()
    }

    private fun dibujar() {
        dibujarFormacion(terreno.sueloMarino, esSuelo = true)
        terreno.formaciones.forEach { dibujarFormacion(it, esSuelo = false) }
    }

    private fun dibujarFormacion(formacion: FormacionRocosa, esSuelo: Boolean) {
        val crestas = formacion.crestas
        if (crestas.size < 2) {
            return
        }

        val xs = DoubleArray(crestas.size + 2)
        val ys = DoubleArray(crestas.size + 2)
        xs[0] = crestas.first().x * escala
        ys[0] = formacion.baseY * escala
        crestas.forEachIndexed { indice, punto ->
            xs[indice + 1] = punto.x * escala
            ys[indice + 1] = punto.y * escala
        }
        xs[xs.lastIndex] = crestas.last().x * escala
        ys[ys.lastIndex] = formacion.baseY * escala

        val colorBase = colorBase(formacion.profundidadMinima, esSuelo)
        val colorClaro = colorBase.interpolate(Color.web("#9FE8DA"), if (esSuelo) 0.10 else 0.24)
        val colorOscuro = colorBase.interpolate(Color.web("#010A0E"), if (esSuelo) 0.55 else 0.42)
        val yCresta = crestas.minOf { it.y } * escala
        val yBase = formacion.baseY * escala

        gc.fill = LinearGradient(
            0.0, yCresta, 0.0, yBase,
            false,
            CycleMethod.NO_CYCLE,
            Stop(0.0, colorClaro),
            Stop(0.38, colorBase),
            Stop(1.0, colorOscuro)
        )
        gc.fillPolygon(xs, ys, xs.size)

        gc.save()
        gc.beginPath()
        gc.moveTo(xs[0], ys[0])
        for (indice in 1 until xs.size) {
            gc.lineTo(xs[indice], ys[indice])
        }
        gc.closePath()
        gc.clip()

        dibujarEstratos(formacion, yCresta, yBase)
        dibujarRocasInteriores(formacion)
        dibujarGrietas(formacion)
        dibujarSedimento(formacion)
        if (esSuelo) {
            dibujarDetallesSuelo(formacion)
        }
        gc.restore()

        if (!esSuelo) {
            dibujarCrestaSinuosa(formacion, colorClaro)
            dibujarSalientes(formacion, colorClaro, colorOscuro)
        } else {
            dibujarSalientes(formacion, colorClaro, colorOscuro)
            dibujarArrecifeDeFondo(formacion)
        }
        if (formacion.profundidadMinima < PROFUNDIDAD_ARRECIFE) {
            dibujarArrecifeDeCresta(formacion)
        }
    }

    private fun colorBase(profundidad: Double, esSuelo: Boolean): Color {
        if (esSuelo) {
            return Color.web("#082A31")
        }
        return when {
            profundidad < 850.0 -> Color.web("#1C6064")
            profundidad < 1500.0 -> Color.web("#14505A")
            profundidad < 2200.0 -> Color.web("#0C3C46")
            profundidad < 2700.0 -> Color.web("#083039")
            else -> Color.web("#05242C")
        }
    }

    private fun dibujarEstratos(formacion: FormacionRocosa, yCresta: Double, yBase: Double) {
        gc.lineWidth = max(1.0, 1.4 * escala)
        repeat(20) { indice ->
            val avance = numeroEntre(0.16, 0.96)
            val y = yCresta + (yBase - yCresta) * avance
            gc.stroke = if (indice % 4 == 0) {
                Color.web("#BFEFE4", 0.045)
            } else {
                Color.web("#031116", numeroEntre(0.08, 0.20))
            }
            gc.beginPath()
            var x = formacion.xMin * escala
            val fin = formacion.xMax * escala
            var primero = true
            while (x <= fin) {
                val onda = y + sin(x * 0.014 + indice * 1.3) * (5.5 * escala)
                if (primero) {
                    gc.moveTo(x, onda)
                    primero = false
                } else {
                    gc.lineTo(x, onda)
                }
                x += 16.0 * escala
            }
            gc.stroke()
        }
    }

    private fun numeroEntre(desde: Double, hasta: Double): Double {
        val minimo = min(desde, hasta)
        val maximo = max(desde, hasta)
        return if (minimo == maximo) minimo else random.nextDouble(minimo, maximo)
    }

    private fun dibujarRocasInteriores(formacion: FormacionRocosa) {
        repeat(30) {
            val x = numeroEntre(formacion.xMin, formacion.xMax)
            val superficie = formacion.superficieEn(x) ?: return@repeat
            val y = numeroEntre(superficie + 8.0, formacion.baseY - 4.0)
            val ancho = numeroEntre(5.0, 24.0)
            val alto = ancho * numeroEntre(0.45, 1.15)
            val luz = random.nextBoolean()
            gc.fill = if (luz) {
                Color.web("#8FD6C8", numeroEntre(0.025, 0.08))
            } else {
                Color.web("#011014", numeroEntre(0.10, 0.26))
            }
            gc.fillOval(
                (x - ancho * 0.5) * escala,
                (y - alto * 0.5) * escala,
                ancho * escala,
                alto * escala
            )
            if (luz) {
                gc.fill = Color.web("#D8FFF4", 0.05)
                gc.fillOval(
                    (x - ancho * 0.35) * escala,
                    (y - alto * 0.38) * escala,
                    ancho * 0.42 * escala,
                    alto * 0.28 * escala
                )
            }
        }
    }

    private fun dibujarGrietas(formacion: FormacionRocosa) {
        gc.lineWidth = max(1.0, 1.1 * escala)
        repeat(14) {
            val x = numeroEntre(formacion.xMin + 12.0, formacion.xMax - 12.0)
            val superficie = formacion.superficieEn(x) ?: return@repeat
            val longitud = numeroEntre(22.0, 150.0)
            val finY = min(superficie + longitud, formacion.baseY - 6.0)
            gc.stroke = Color.web("#01090D", numeroEntre(0.30, 0.55))
            gc.beginPath()
            gc.moveTo(x * escala, (superficie + 2.0) * escala)
            gc.lineTo((x + numeroEntre(-9.0, 9.0)) * escala, finY * escala)
            if (random.nextBoolean()) {
                gc.lineTo((x + numeroEntre(-15.0, 15.0)) * escala, numeroEntre(finY + 6.0, formacion.baseY) * escala)
            }
            gc.stroke()
        }
    }

    private fun dibujarSedimento(formacion: FormacionRocosa) {
        repeat(18) {
            val x = numeroEntre(formacion.xMin, formacion.xMax)
            val superficie = formacion.superficieEn(x) ?: return@repeat
            val zonaBase = formacion.baseY - (formacion.baseY - superficie) * numeroEntre(0.05, 0.42)
            val radio = numeroEntre(10.0, 46.0)
            gc.fill = if (random.nextBoolean()) {
                Color.web("#4A3C25", numeroEntre(0.06, 0.16))
            } else {
                Color.web("#2F2B1E", numeroEntre(0.08, 0.18))
            }
            gc.fillOval(
                (x - radio) * escala,
                (zonaBase - radio * 0.35) * escala,
                radio * 2.0 * escala,
                radio * 0.8 * escala
            )
        }
        repeat(26) {
            val x = numeroEntre(formacion.xMin, formacion.xMax)
            val superficie = formacion.superficieEn(x) ?: return@repeat
            val y = numeroEntre(superficie, formacion.baseY)
            val radio = numeroEntre(1.4, 4.6)
            gc.fill = if (random.nextBoolean()) {
                Color.web("#6A7563", numeroEntre(0.12, 0.28))
            } else {
                Color.web("#0A1A1D", numeroEntre(0.20, 0.38))
            }
            gc.fillOval((x - radio) * escala, (y - radio) * escala, radio * 2.0 * escala, radio * 2.0 * escala)
        }
    }

    private fun dibujarDetallesSuelo(formacion: FormacionRocosa) {
        repeat(40) {
            val x = numeroEntre(formacion.xMin, formacion.xMax)
            val superficie = formacion.superficieEn(x) ?: return@repeat
            val y = superficie + numeroEntre(4.0, 70.0)
            val ancho = numeroEntre(8.0, 34.0)
            gc.fill = Color.web("#0B2226", numeroEntre(0.25, 0.5))
            gc.fillOval((x - ancho) * escala, (y - ancho * 0.3) * escala, ancho * 2.0 * escala, ancho * 0.7 * escala)
            gc.stroke = Color.web("#7FAFA2", numeroEntre(0.05, 0.13))
            gc.lineWidth = max(1.0, 0.9 * escala)
            gc.strokeOval((x - ancho) * escala, (y - ancho * 0.3) * escala, ancho * 2.0 * escala, ancho * 0.7 * escala)
        }
    }

    private fun dibujarCrestaSinuosa(formacion: FormacionRocosa, colorClaro: Color) {
        val crestas = formacion.crestas
        gc.lineWidth = max(1.2, 1.9 * escala)
        gc.stroke = colorClaro.deriveColor(0.0, 0.8, 1.0, 0.20)
        gc.beginPath()
        gc.moveTo(crestas.first().x * escala, crestas.first().y * escala)
        for (indice in 1 until crestas.size) {
            gc.lineTo(crestas[indice].x * escala, crestas[indice].y * escala)
        }
        gc.stroke()

        gc.lineWidth = max(0.8, 1.0 * escala)
        gc.stroke = Color.web("#DFFFF7", 0.10)
        gc.beginPath()
        gc.moveTo(crestas.first().x * escala, (crestas.first().y - 1.5) * escala)
        for (indice in 1 until crestas.size) {
            gc.lineTo(crestas[indice].x * escala, (crestas[indice].y - 1.5) * escala)
        }
        gc.stroke()
    }

    private fun dibujarSalientes(formacion: FormacionRocosa, colorClaro: Color, colorOscuro: Color) {
        repeat(16) {
            val x = numeroEntre(formacion.xMin + 10.0, formacion.xMax - 10.0)
            val superficie = formacion.superficieEn(x) ?: return@repeat
            val radio = numeroEntre(4.0, 15.0)
            val centroY = superficie + radio * 0.15
            gc.fill = colorOscuro.deriveColor(0.0, 1.0, 1.0, 0.95)
            gc.fillOval(
                (x - radio) * escala,
                (centroY - radio * 0.55) * escala,
                radio * 2.0 * escala,
                radio * 1.25 * escala
            )
            gc.fill = colorClaro.deriveColor(0.0, 1.0, 1.0, 0.25)
            gc.fillOval(
                (x - radio * 0.75) * escala,
                (centroY - radio * 0.62) * escala,
                radio * 1.1 * escala,
                radio * 0.48 * escala
            )
        }
    }

    private fun dibujarArrecifeDeCresta(formacion: FormacionRocosa) {
        repeat(22) {
            val x = numeroEntre(formacion.xMin + 8.0, formacion.xMax - 8.0)
            val superficie = formacion.superficieEn(x) ?: return@repeat
            val altura = numeroEntre(8.0, 34.0)
            val color = when (random.nextInt(3)) {
                0 -> Color.web("#2E8C80", numeroEntre(0.25, 0.48))
                1 -> Color.web("#B56A6A", numeroEntre(0.20, 0.42))
                else -> Color.web("#6B8F4A", numeroEntre(0.22, 0.45))
            }
            gc.stroke = color
            gc.lineWidth = max(1.0, numeroEntre(1.0, 2.2) * escala)
            gc.beginPath()
            gc.moveTo(x * escala, (superficie + 2.0) * escala)
            gc.quadraticCurveTo(
                (x - altura * 0.35) * escala,
                (superficie - altura * 0.55) * escala,
                (x - altura * 0.15) * escala,
                (superficie - altura) * escala
            )
            gc.moveTo((x - altura * 0.1) * escala, (superficie - altura * 0.45) * escala)
            gc.lineTo((x + altura * 0.28) * escala, (superficie - altura * 0.78) * escala)
            gc.moveTo((x - altura * 0.08) * escala, (superficie - altura * 0.62) * escala)
            gc.lineTo((x + altura * 0.08) * escala, (superficie - altura * 0.95) * escala)
            gc.stroke()
        }
    }

    private fun dibujarArrecifeDeFondo(formacion: FormacionRocosa) {
        repeat(34) {
            val x = numeroEntre(formacion.xMin, formacion.xMax)
            val superficie = formacion.superficieEn(x) ?: return@repeat
            val altura = numeroEntre(10.0, 44.0)
            val color = if (random.nextBoolean()) {
                Color.web("#1E6C66", numeroEntre(0.18, 0.35))
            } else {
                Color.web("#6E4A4A", numeroEntre(0.14, 0.30))
            }
            gc.stroke = color
            gc.lineWidth = max(1.0, 1.6 * escala)
            gc.beginPath()
            gc.moveTo(x * escala, superficie * escala)
            gc.quadraticCurveTo(
                (x - 8.0) * escala,
                (superficie - altura * 0.55) * escala,
                (x + 5.0) * escala,
                (superficie - altura) * escala
            )
            gc.moveTo((x + 2.0) * escala, (superficie - altura * 0.35) * escala)
            gc.lineTo((x + altura * 0.32) * escala, (superficie - altura * 0.72) * escala)
            gc.stroke()
        }
    }

    companion object {
        private const val SEMILLA_DEFAULT = 53L
        private const val PROFUNDIDAD_ARRECIFE = 1650.0
    }
}
