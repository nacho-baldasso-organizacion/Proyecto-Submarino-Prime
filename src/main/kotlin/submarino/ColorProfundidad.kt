/**
 * Representación inmutable de un color RGB con componentes enteros.
 * UI-agnostic: esta estructura pura puede consumirse por cualquier framework
 * gráfico o renderizador sin acoplamiento a bibliotecas específicas.
 */
data class ColorRgb(
    /** Componente rojo (0-255) */
    val r: Int,
    /** Componente verde (0-255) */
    val g: Int,
    /** Componente azul (0-255) */
    val b: Int
)

/**
 * Calculador de color por profundidad. Usa rangos claros sin 'magic numbers':
 * - Superficie: 0.0 - 200.0 m   (zona costera, tonalidades cálidas)
 * - Zona Fotica: 200.0 - 1000.0 m (zona de luz solar discontinua, tonalidades verdes/azules)
 * - Zona Abisal: 1000.0+ m     (oscuridad total, tonalidades azul oscuro)
 */
object CalculadorColorProfundidad {

    // Rangos definidos como constantes nombradas (sin magic numbers)
    private val RANGO_SUPERFICIE = ProfundidadRango(0.0, 200.0, ColorRgb(255, 200, 150))
    private val RANGO_FOTICA = ProfundidadRango(200.0, 1000.0, ColorRgb(100, 150, 255))
    private val RANGO_ABISAL = ProfundidadRango(1000.0, Double.POSITIVE_INFINITY, ColorRgb(0, 0, 80))

    private val rangos = listOf(RANGO_SUPERFICIE, RANGO_FOTICA, RANGO_ABISAL)

    /**
     * Calcula el color apropiado para una profundidad dada.
     * 
     * @param profundidad Profundidad en metros
     * @return ColorRgb correspondiente al rango de profundidad
     */
    fun calcularColor(profundidad: Double): ColorRgb {
        for (rango in rangos) {
            if (rango.enRango(profundidad)) {
                return rango.color
            }
        }
        // Default: zona abisal para profundidades extremas o negativas
        return RANGO_ABISAL.color
    }

    /** Nombre del rango para depuración/mostrar al usuario */
    data class NombreRango(val nombre: String, val min: Double, val max: Double)
}

/** Rango de profundidad con su color asociado y lógica de rango. */
private data class ProfundidadRango(
    val min: Double,
    val max: Double,
    val color: ColorRgb
) {
    fun enRango(profundidad: Double): Boolean {
        return profundidad >= min && profundidad < max
    }
}