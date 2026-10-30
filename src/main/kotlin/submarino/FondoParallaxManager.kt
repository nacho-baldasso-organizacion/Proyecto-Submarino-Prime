/**
 * Componente de gestión que unifica el cálculo del estado del fondo marino
 * dado un valor de profundidad. Proporciona los desplazamientos de todas
 * las capas de parallax y el color activo.
 * 
 * UI-agnostic: retorna DTOs puros sin dependencias de frameworks gráficos.
 */
data class FondoParallaxState(
    /** Lista de desplazamientos offsetY para cada capa de parallax, en orden
     *  de factor de velocidad (desde lento hasta rápido o según configuración). */
    val offsetsY: List<Double>,
    /** Color activo correspondiente a la profundidad actual. */
    val colorActivo: ColorRgb
)

object FondoParallaxManager {

    /** Factores de velocidad predefinidas para las capas de parallax.
     *  Valores típicos: capas cercanas tienen factor bajo (más lentas),
     *  capas lejanas tienen factor alto (más rápidas). */
    private val factoresVelocidad = listOf(0.2, 0.5, 1.0)

    /**
     * Calcula el estado completo del fondo marino para una profundidad dada.
     * 
     * @param profundidad Profundidad actual en metros
     * @return Estructura inmutable con offsetsY y colorActivo
     */
    fun calcularEstado(profundidad: Double): FondoParallaxState {
        val offsets = factoresVelocidad.map { CapaParallax(it).calcularOffsetY(profundidad) }
        val color = CalculadorColorProfundidad.calcularColor(profundidad)
        return FondoParallaxState(offsets, color)
    }

    /**
     * Calcula el estado con una configuración personalizada de factores de capa.
     * 
     * @param factores Lista personalizada de factores de velocidad
     * @param profundidad Profundidad actual en metros
     * @return Estructura inmutable con offsets y color
     */
    fun calcularEstadoConFactores(factores: List<Double>, profundidad: Double): FondoParallaxState {
        val offsets = factores.map { CapaParallax(it).calcularOffsetY(profundidad) }
        val color = CalculadorColorProfundidad.calcularColor(profundidad)
        return FondoParallaxState(offsets, color)
    }
}