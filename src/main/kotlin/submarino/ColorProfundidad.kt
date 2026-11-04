package submarino

import kotlin.math.roundToInt

/**
 * Representación inmutable de un color RGB con componentes enteros.
 *
 * UI-agnostic: esta estructura pura puede consumirse desde cualquier framework
 * gráfico o renderizador sin acoplarse a una biblioteca específica.
 */
data class ColorRgb(
    val r: Int,
    val g: Int,
    val b: Int
) {

    /**
     * Interpola linealmente cada componente hacia [otro] según [factor].
     * Un factor de 0.0 conserva este color y 1.0 devuelve [otro]; valores
     * fuera de ese rango se acotan.
     */
    fun interpolar(otro: ColorRgb, factor: Double): ColorRgb {
        val f = factor.coerceIn(0.0, 1.0)
        return ColorRgb(
            r = (r + (otro.r - r) * f).roundToInt(),
            g = (g + (otro.g - g) * f).roundToInt(),
            b = (b + (otro.b - b) * f).roundToInt()
        )
    }
}

/**
 * Zonas de profundidad con su color asociado.
 *
 * Los límites de cada zona viven en [CalculadorColorProfundidad] para evitar
 * números mágicos y mantener una única fuente de verdad.
 */
enum class ColorProfundidad(val color: ColorRgb) {
    SUPERFICIE(ColorRgb(r = 255, g = 200, b = 150)),
    FOTICA(ColorRgb(r = 100, g = 150, b = 255)),
    ABISAL(ColorRgb(r = 0, g = 0, b = 80));

    /**
     * Indica si esta zona es la que corresponde a la [profundidad] indicada.
     */
    fun correspondeA(profundidad: Double): Boolean =
        CalculadorColorProfundidad.calcularZona(profundidad) == this

    /**
     * Interpola el color de esta zona hacia el color de [otra] zona.
     */
    fun interpolarHacia(otra: ColorProfundidad, factor: Double): ColorRgb =
        color.interpolar(otra.color, factor)
}

/**
 * Calculador de color por profundidad.
 *
 * Rangos:
 * - [ColorProfundidad.SUPERFICIE]: desde [PROFUNDIDAD_MINIMA] hasta
 *   [LIMITE_SUPERFICIE] inclusive.
 * - [ColorProfundidad.FOTICA]: desde [LIMITE_SUPERFICIE] (exclusivo) hasta
 *   [LIMITE_ZONA_FOTICA] (exclusivo).
 * - [ColorProfundidad.ABISAL]: desde [LIMITE_ZONA_FOTICA] en adelante.
 *
 * Las profundidades negativas o no finitas se consideran abisales por defecto.
 */
object CalculadorColorProfundidad {

    const val PROFUNDIDAD_MINIMA = 0.0
    const val LIMITE_SUPERFICIE = 200.0
    const val LIMITE_ZONA_FOTICA = 1000.0

    /**
     * Determina la zona de profundidad correspondiente.
     *
     * @param profundidad Profundidad en metros.
     * @return Zona activa para la profundidad indicada.
     */
    fun calcularZona(profundidad: Double): ColorProfundidad = when {
        !profundidad.isFinite() -> ColorProfundidad.ABISAL
        profundidad < PROFUNDIDAD_MINIMA -> ColorProfundidad.ABISAL
        profundidad <= LIMITE_SUPERFICIE -> ColorProfundidad.SUPERFICIE
        profundidad < LIMITE_ZONA_FOTICA -> ColorProfundidad.FOTICA
        else -> ColorProfundidad.ABISAL
    }

    /**
     * Calcula el color apropiado para una profundidad dada.
     *
     * @param profundidad Profundidad en metros.
     * @return Color RGB de la zona activa.
     */
    fun calcularColor(profundidad: Double): ColorRgb = calcularZona(profundidad).color
}
