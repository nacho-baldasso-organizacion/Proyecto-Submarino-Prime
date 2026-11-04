package submarino

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

/**
 * Aplica los detalles internos (estratos, rocas, grietas y sedimento) de una
 * cordillera ya recortada en el canvas.
 *
 * Responsabilidad única (SRP): pintar el interior de una silueta conocida.
 */
class DetallesCordillera(
    private val gc: GraphicsContext,
    private val random: Random,
    private val anchoMundo: Double,
    private val perfil: PerfilCordillera
) {

    fun dibujarEstratos(baseY: Double) {
        gc.lineWidth = 1.3
        repeat(18) { indice ->
            val altura = entre(baseY * 0.18, baseY * 0.92)
            gc.stroke = if (indice % 4 == 0) {
                Color.web("#BFF6FF", 0.035)
            } else {
                Color.web("#011015", entre(0.07, 0.17))
            }
            gc.beginPath()
            var x = 0.0
            var primero = true
            while (x <= anchoMundo) {
                val superficie = perfil.alturaEn(x)
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

    fun dibujarRocas(baseY: Double, colorLuz: Color, colorSombra: Color) {
        repeat(34) {
            val x = entre(0.0, anchoMundo)
            val superficie = perfil.alturaEn(x)
            val y = entre(superficie + 5.0, baseY)
            val ancho = entre(5.0, 28.0)
            val alto = ancho * entre(0.5, 1.3)
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

    fun dibujarGrietas(baseY: Double) {
        gc.lineWidth = 1.1
        gc.stroke = Color.web("#01090D", 0.42)
        repeat(16) {
            val x = entre(0.0, anchoMundo)
            val superficie = perfil.alturaEn(x)
            val fin = entre(superficie + 18.0, baseY)
            gc.beginPath()
            gc.moveTo(x, superficie + 2.0)
            gc.lineTo(x + entre(-14.0, 14.0), fin)
            gc.stroke()
        }
    }

    fun dibujarSedimento(baseY: Double) {
        repeat(20) {
            val x = entre(0.0, anchoMundo)
            val superficie = perfil.alturaEn(x)
            val y = entre(superficie + (baseY - superficie) * 0.55, baseY)
            val ancho = entre(12.0, 54.0)
            gc.fill = if (random.nextBoolean()) {
                Color.web("#453821", entre(0.05, 0.13))
            } else {
                Color.web("#2C2A1D", entre(0.07, 0.16))
            }
            gc.fillOval(x, y, ancho, ancho * 0.34)
        }
    }

    fun dibujarRocasSueltas(baseY: Double) {
        repeat(12) {
            val x = entre(0.0, anchoMundo)
            val superficie = perfil.alturaEn(x)
            val radio = entre(4.0, 16.0)
            gc.fill = Color.web("#02141A", 0.82)
            gc.fillOval(x - radio, superficie - radio * 0.55, radio * 2.0, radio * 1.2)
            gc.fill = Color.web("#8FD6C8", 0.10)
            gc.fillOval(x - radio * 0.7, superficie - radio * 0.52, radio * 0.9, radio * 0.35)
        }
    }

    private fun entre(desde: Double, hasta: Double): Double {
        val minimo = min(desde, hasta)
        val maximo = max(desde, hasta)
        return if (minimo == maximo) minimo else random.nextDouble(minimo, maximo)
    }
}
