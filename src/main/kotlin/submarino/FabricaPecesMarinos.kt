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
        val tamano = TAMANO_MINIMO + random.nextDouble() * (TAMANO_MAXIMO - TAMANO_MINIMO)
        val radioX = 50.0 + random.nextDouble() * 210.0
        val radioY = 25.0 + random.nextDouble() * 95.0
        val margenX = radioX + tamano * 2.0
        val margenY = radioY + tamano * 2.0

        return PezMarino(
            centroX = if (ancho > margenX * 2.0) {
                random.nextDouble(margenX, ancho - margenX)
            } else {
                ancho / 2.0
            },
            centroY = if (alto > margenY * 2.0) {
                random.nextDouble(margenY, alto - margenY)
            } else {
                alto / 2.0
            },
            radioX = radioX,
            radioY = radioY,
            velocidadAngular = (0.22 + random.nextDouble() * 0.42) * if (indice % 2 == 0) 1.0 else -1.0,
            angulo = random.nextDouble(0.0, PI * 2.0),
            fase = random.nextDouble(0.0, PI * 2.0),
            frecuencia = 0.8 + random.nextDouble() * 1.4,
            amplitud = 4.0 + random.nextDouble() * 12.0,
            tamano = tamano,
            colorCuerpo = PALETA_CUERPO[indice % PALETA_CUERPO.size],
            colorAleta = PALETA_ALETA[indice % PALETA_ALETA.size],
            alfa = 0.55 + random.nextDouble() * 0.35
        )
    }

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
