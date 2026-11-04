package submarino

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 * Pinta la cresta, los salientes y los arrecifes exteriores de una formación.
 *
 * Responsabilidad única (SRP): acabado exterior y vegetación adherida a la roca.
 */
class PincelRocaExterior(
    private val gc: GraphicsContext,
    private val random: Random,
    private val escala: Double
) {

    fun dibujarCrestaSinuosa(formacion: FormacionRocosa, colorClaro: Color) {
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

    fun dibujarSalientes(formacion: FormacionRocosa, colorClaro: Color, colorOscuro: Color) {
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

    fun dibujarArrecifeDeCresta(formacion: FormacionRocosa) {
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

    fun dibujarArrecifeDeFondo(formacion: FormacionRocosa) {
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

    private fun numeroEntre(desde: Double, hasta: Double): Double {
        val minimo = min(desde, hasta)
        val maximo = max(desde, hasta)
        return if (minimo == maximo) minimo else random.nextDouble(minimo, maximo)
    }
}
