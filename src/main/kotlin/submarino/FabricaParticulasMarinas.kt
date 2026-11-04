package submarino

import kotlin.random.Random

/**
 * Crea partículas con posición, tamaño y opacidad pseudoaleatorios.
 */
internal class FabricaParticulasMarinas(
    private val random: Random,
    private val ancho: Double,
    private val alto: Double
) {

    fun crear(): ParticulaMarina = ParticulaMarina(
        x = random.nextDouble(0.0, ancho),
        y = random.nextDouble(0.0, alto),
        radio = RADIO_MINIMO + random.nextDouble() * (RADIO_MAXIMO - RADIO_MINIMO),
        velocidadX = -4.0 + random.nextDouble() * 8.0,
        velocidadY = VELOCIDAD_MINIMA + random.nextDouble() * (VELOCIDAD_MAXIMA - VELOCIDAD_MINIMA),
        alfa = ALFA_MINIMA + random.nextDouble() * (ALFA_MAXIMA - ALFA_MINIMA),
        fase = random.nextDouble(0.0, Math.PI * 2.0)
    )

    private companion object {
        private const val RADIO_MINIMO = 0.7
        private const val RADIO_MAXIMO = 2.8
        private const val VELOCIDAD_MINIMA = 5.0
        private const val VELOCIDAD_MAXIMA = 22.0
        private const val ALFA_MINIMA = 0.10
        private const val ALFA_MAXIMA = 0.46
    }
}
