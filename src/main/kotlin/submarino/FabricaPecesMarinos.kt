package submarino

import javafx.scene.paint.Color
import kotlin.math.PI
import kotlin.random.Random

/**
 * Crea peces con órbitas, tamaños y colores pseudoaleatorios.
 */
internal class FabricaPecesMarinos(
    private val random: Random,
    private val ancho: Double,
    private val alto: Double
) {

    fun crear(indice: Int): PezMarino {
        val dimensiones = calcularDimensiones()
        val posicion = calcularPosicion(dimensiones)
        return PezMarino(
            centroX = posicion.centroX,
            centroY = posicion.centroY,
            radioX = dimensiones.radioX,
            radioY = dimensiones.radioY,
            velocidadAngular = calcularVelocidadAngular(indice),
            angulo = random.nextDouble(0.0, PI * 2.0),
            fase = random.nextDouble(0.0, PI * 2.0),
            frecuencia = calcularFrecuencia(),
            amplitud = calcularAmplitud(),
            tamano = dimensiones.tamano,
            colorCuerpo = PALETA_CUERPO[indice % PALETA_CUERPO.size],
            colorAleta = PALETA_ALETA[indice % PALETA_ALETA.size],
            alfa = calcularAlfa()
        )
    }

    private fun calcularDimensiones(): Dimensiones {
        val tamano = TAMANO_MINIMO + random.nextDouble() * (TAMANO_MAXIMO - TAMANO_MINIMO)
        val radioX = 50.0 + random.nextDouble() * 210.0
        val radioY = 25.0 + random.nextDouble() * 95.0
        return Dimensiones(tamano, radioX, radioY)
    }

    private fun calcularPosicion(dimensiones: Dimensiones): CentrarPosicion {
        val margenX = dimensiones.radioX + dimensiones.tamano * 2.0
        val margenY = dimensiones.radioY + dimensiones.tamano * 2.0
        val centroX = if (ancho > margenX * 2.0) {
            random.nextDouble(margenX, ancho - margenX)
        } else {
            ancho / 2.0
        }
        val centroY = if (alto > margenY * 2.0) {
            random.nextDouble(margenY, alto - margenY)
        } else {
            alto / 2.0
        }
        return CentrarPosicion(centroX, centroY)
    }

    private fun calcularVelocidadAngular(indice: Int): Double =
        (0.22 + random.nextDouble() * 0.42) * if (indice % 2 == 0) 1.0 else -1.0

    private fun calcularFrecuencia(): Double = 0.8 + random.nextDouble() * 1.4

    private fun calcularAmplitud(): Double = 4.0 + random.nextDouble() * 12.0

    private fun calcularAlfa(): Double = 0.55 + random.nextDouble() * 0.35

    private data class Dimensiones(
        val tamano: Double,
        val radioX: Double,
        val radioY: Double
    )

    private data class CentrarPosicion(
        val centroX: Double,
        val centroY: Double
    )

    private companion object {
        private const val TAMANO_MINIMO = 9.0
        private const val TAMANO_MAXIMO = 26.0

        private val PALETA_CUERPO = listOf(
            Color.web("#FFD75A"),
            Color.web("#7FE3F0"),
            Color.web("#F59E72"),
            Color.web("#8FE3B0"),
            Color.web("#B8C7FF"),
            Color.web("#E8D5A8")
        )

        private val PALETA_ALETA = listOf(
            Color.web("#D99A25"),
            Color.web("#3FB8CF"),
            Color.web("#D9774F"),
            Color.web("#4FB27A"),
            Color.web("#7E92E0"),
            Color.web("#C7AE7A")
        )
    }
}