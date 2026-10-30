/**
 * Clase inmutable que representa una capa de parallax vertical.
 * Cada capa tiene un factor de velocidad que determina qué tan rápido
 * se desplaza en relación con la profundidad actual.
 * 
 * UI-agnostic: no depende de ningún framework gráfico, solo usa representaciones matemáticas.
 */
data class CapaParallax(
    /** Factor de velocidad de la capa (0.0 = paralela, 1.0 = velocidad natural, 
      * >1.0 = más rápido que el fondo, <1.0 = más lento). */
    val factorVelocidad: Double
) {

    /**
     * Calcula el desplazamiento offset en el eje Y basado en la profundidad actual.
     * 
     * La fórmula es: offset = profundidad * factorVelocidad
     * 
     * @param profundidad Valor de profundidad actual en metros (puede ser Double.POSITIVE_INFINITY)
     * @return Desplazamiento en píxeles/units en el eje Y
     */
    fun calcularOffsetY(profundidad: Double): Double {
        if (profundidad.isNaN() || profundidad.isInfinite()) return 0.0
        return profundidad * factorVelocidad
    }
}