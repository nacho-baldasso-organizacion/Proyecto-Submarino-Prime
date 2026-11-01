package submarino

/**
 * Clase inmutable que representa una capa de parallax vertical.
 *
 * UI-agnostic: no depende de ningún framework gráfico, solo de matemática pura.
 *
 * @property factorVelocidad Factor de desplazamiento de la capa respecto de la
 * profundidad. Un valor de [FACTOR_ESTATICO] deja la capa fija, [FACTOR_NATURAL]
 * la mueve a la misma velocidad que el fondo y los valores intermedios producen
 * el efecto de profundidad esperado.
 */
data class CapaParallax(
    val factorVelocidad: Double
) {

    /**
     * Calcula el desplazamiento vertical de la capa para una profundidad dada.
     *
     * Las profundidades no finitas o negativas no producen desplazamiento.
     *
     * @param profundidad Profundidad actual en metros.
     * @return Offset en el eje Y, en las mismas unidades que la profundidad.
     */
    fun calcularOffsetY(profundidad: Double): Double {
        if (!profundidad.isFinite() || profundidad <= PROFUNDIDAD_MINIMA) {
            return SIN_DESPLAZAMIENTO
        }
        return profundidad * factorVelocidad
    }

    companion object {
        const val FACTOR_ESTATICO = 0.0
        const val FACTOR_NATURAL = 1.0
        const val PROFUNDIDAD_MINIMA = 0.0
        const val SIN_DESPLAZAMIENTO = 0.0
    }
}
