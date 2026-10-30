package submarino

/**
 * Estado inmutable del fondo marino para una profundidad determinada.
 *
 * @property offsetsY Desplazamientos verticales de cada capa de parallax, en el
 * mismo orden en que fueron configuradas.
 * @property colorActivo Color correspondiente a la profundidad actual.
 */
data class FondoParallaxState(
    val offsetsY: List<Double>,
    val colorActivo: ColorRgb
)

/**
 * Componente que unifica el cálculo del estado del fondo marino.
 *
 * UI-agnostic: retorna únicamente DTOs puros, sin dependencias de frameworks
 * gráficos.
 */
object FondoParallaxManager {

    /** Factores de velocidad de las capas por defecto (de lejana a cercana). */
    private val capasPorDefecto = listOf(
        CapaParallax(0.2),
        CapaParallax(0.5),
        CapaParallax(1.0)
    )

    /**
     * Calcula el estado completo del fondo marino para una profundidad dada.
     *
     * @param profundidad Profundidad actual en metros.
     * @return Estructura inmutable con los offsets de las capas y el color activo.
     */
    fun calcularEstado(profundidad: Double): FondoParallaxState =
        calcularEstadoConCapas(capasPorDefecto, profundidad)

    /**
     * Calcula el estado usando una configuración personalizada de factores.
     *
     * @param factores Factores de velocidad de cada capa.
     * @param profundidad Profundidad actual en metros.
     * @return Estructura inmutable con los offsets y el color activo.
     */
    fun calcularEstadoConFactores(
        factores: List<Double>,
        profundidad: Double
    ): FondoParallaxState =
        calcularEstadoConCapas(factores.map(::CapaParallax), profundidad)

    private fun calcularEstadoConCapas(
        capas: List<CapaParallax>,
        profundidad: Double
    ): FondoParallaxState = FondoParallaxState(
        offsetsY = capas.map { it.calcularOffsetY(profundidad) },
        colorActivo = CalculadorColorProfundidad.calcularColor(profundidad)
    )
}
