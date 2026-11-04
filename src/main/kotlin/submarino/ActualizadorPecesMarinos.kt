package submarino

import kotlin.math.cos
import kotlin.math.sin

/**
 * Avanza la posición de los peces a lo largo de sus órbitas.
 */
internal class ActualizadorPecesMarinos {

    var tiempo: Double = 0.0
        private set

    fun actualizar(peces: List<PezMarino>, delta: Double) {
        tiempo += delta
        peces.forEach { pez ->
            pez.angulo += pez.velocidadAngular * delta
            pez.x = pez.centroX + pez.radioX * cos(pez.angulo)
            pez.y = pez.centroY +
                pez.radioY * sin(pez.angulo) +
                sin(tiempo * pez.frecuencia + pez.fase) * pez.amplitud
        }
    }
}
