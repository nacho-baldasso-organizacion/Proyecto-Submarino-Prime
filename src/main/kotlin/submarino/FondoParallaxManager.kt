package submarino

/**
 * Value Object inmutable que representa el estado de un fondo marino con parallax.
 * Encapsula los desplazamientos de las capas y el color activo,-provee métodos de dominio
 * para consultas sobre el estado visual del fondo.
 * SRP: esta clase tiene una única responsabilidad: representar el estado de parallax
 * con sus datos y comportamiento asociado.
 */
data class FondoParallaxState(
    val offsetsY: List<Double>,
    val colorActivo: ColorRgb
) {

    /**
     * Determina si el fondo marino tiene capas configuradas.
     */
    val tieneCapas: Boolean
        get() = offsetsY.isNotEmpty()

    /**
     * Obtiene el offset vertical de una capa específica por índice.
     * @param indice Índice de la capa (0 = más lejana)
     * @return Offset vertical en metros, o null si el índice está fuera de rango.
     */
    fun offsetDeCapa(indice: Int): Double? =
        if (indice in 0 until offsetsY.size) offsetsY[indice] else null

    /**
     * Obtiene el número total de capas de parallax configuradas.
     */
    val cantidadCapas: Int
        get() = offsetsY.size

    /**
     * Determina si el color activo es de la zona abisal.
     */
    val esZonaAbisal: Boolean
        get() = colorActivo == ColorRgb(0, 0, 80)

    /**
     * Determina si el color activo es de la zona de superficie.
     */
    val esZonaSuperficie: Boolean
        get() = colorActivo == ColorRgb(255, 200, 150)

    /**
     * Determina si el color activo es de la zona ftica.
     */
    val esZonaFotica: Boolean
        get() = colorActivo == ColorRgb(100, 150, 255)
}

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