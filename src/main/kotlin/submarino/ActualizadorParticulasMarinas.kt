package submarino

import kotlin.math.sin
import kotlin.random.Random

/**
 * Desplaza las partículas hacia la superficie y gestiona los bordes.
 */
internal class ActualizadorParticulasMarinas(
    private val ancho: Double,
    private val alto: Double,
    private val random: Random
) {

    fun actualizar(particulas: List<ParticulaMarina>, delta: Double) {
        particulas.forEach { particula ->
            particula.y -= particula.velocidadY * delta
            particula.x += (
                particula.velocidadX +
                    sin(particula.fase + particula.y * ONDULACION) * DERIVA_LATERAL
                ) * delta
            if (particula.y < -particula.radio * 2.0) {
                particula.y = alto + particula.radio
                particula.x = random.nextDouble(0.0, ancho)
            }
            if (particula.x < -particula.radio) {
                particula.x = ancho + particula.radio
            } else if (particula.x > ancho + particula.radio) {
                particula.x = -particula.radio
            }
        }
    }

    private companion object {
        private const val DERIVA_LATERAL = 10.0
        private const val ONDULACION = 0.012
    }
}
