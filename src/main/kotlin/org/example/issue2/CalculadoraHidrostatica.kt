package submarino

/**
 * Objeto singleton que encapsula la física hidrostática del submarino.
 *
 * Calcula la presión según la profundidad (eje Y) aplicando la fórmula:
 *      P = P0 + (ρ * g * h)
 * donde:
 *      P0 = presión atmosférica en la superficie (Pa)
 *      ρ  = densidad del agua de mar (kg/m³)
 *      g  = aceleración de la gravedad (m/s²)
 *      h  = profundidad (m)
 */
object CalculadoraHidrostatica {

    // Constantes físicas universales
    const val DENSIDAD_AGUA_MAR: Double = 1025.0    // kg/m³
    const val GRAVEDAD: Double = 9.81                // m/s²
    const val PRESION_ATMOSFERICA: Double = 101325.0 // Pa

    /**
     * Calcula la presión hidrostática en Pascales a una profundidad dada.
     *
     * @param profundidad Profundidad en metros a la que se encuentra el submarino (eje Y)
     * @return Presión total en Pascales (P0 + ρ*g*h)
     */
    fun calcularPresion(profundidad: Double): Double {
        val h = maxOf(profundidad, 0.0) // no hay presiones inferiores a la de superficie
        return PRESION_ATMOSFERICA + (DENSIDAD_AGUA_MAR * GRAVEDAD * h)
    }

    /**
     * Calcula la profundidad máxima en metros a la que un casco puede descender
     * antes de colapsar, a partir de su presión máxima soportada.
     *
     * Despejando h de la fórmula: h = (P_max - P0) / (ρ * g)
     *
     * @param presionMaximaPa Presión máxima en Pascales que soporta el casco
     * @return Profundidad límite en metros (0.0 si la presión máxima no supera la atmosférica)
     */
    fun calcularProfundidadMaxima(presionMaximaPa: Double): Double {
        val presionHidrostaticaMaxima = presionMaximaPa - PRESION_ATMOSFERICA
        return maxOf(presionHidrostaticaMaxima / (DENSIDAD_AGUA_MAR * GRAVEDAD), 0.0)
    }
}
