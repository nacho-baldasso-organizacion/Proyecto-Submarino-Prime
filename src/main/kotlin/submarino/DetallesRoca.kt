package submarino

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

/**
 * Pinta el interior de una formación rocosa: estratos, rocas, grietas,
 * sedimento y detalles del suelo.
 *
 * Responsabilidad única (SRP): detalle interno de una silueta ya recortada.
 */
class DetallesRoca(
    private val gc: GraphicsContext,
    private val random: Random,
    private val escala: Double
) {

    fun dibujarEstratos(formacion: FormacionRocosa, yCresta: Double, yBase: Double) {
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

    fun dibujarRocasInteriores(formacion: FormacionRocosa) {
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

    fun dibujarGrietas(formacion: FormacionRocosa) {
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
                gc.lineTo(
                    (x + numeroEntre(-15.0, 15.0)) * escala,
                    numeroEntre(finY + 6.0, formacion.baseY) * escala
                )
            }
            gc.stroke()
        }
    }

    fun dibujarSedimento(formacion: FormacionRocosa) {
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
            gc.fillOval(
                (x - radio) * escala,
                (y - radio) * escala,
                radio * 2.0 * escala,
                radio * 2.0 * escala
            )
        }
    }

    fun dibujarDetallesSuelo(formacion: FormacionRocosa) {
        repeat(40) {
            val x = numeroEntre(formacion.xMin, formacion.xMax)
            val superficie = formacion.superficieEn(x) ?: return@repeat
            val y = superficie + numeroEntre(4.0, 70.0)
            val ancho = numeroEntre(8.0, 34.0)
            gc.fill = Color.web("#0B2226", numeroEntre(0.25, 0.5))
            gc.fillOval(
                (x - ancho) * escala,
                (y - ancho * 0.3) * escala,
                ancho * 2.0 * escala,
                ancho * 0.7 * escala
            )
            gc.stroke = Color.web("#7FAFA2", numeroEntre(0.05, 0.13))
            gc.lineWidth = max(1.0, 0.9 * escala)
            gc.strokeOval(
                (x - ancho) * escala,
                (y - ancho * 0.3) * escala,
                ancho * 2.0 * escala,
                ancho * 0.7 * escala
            )
        }
    }

    private fun numeroEntre(desde: Double, hasta: Double): Double {
        val minimo = min(desde, hasta)
        val maximo = max(desde, hasta)
        return if (minimo == maximo) minimo else random.nextDouble(minimo, maximo)
    }
}
