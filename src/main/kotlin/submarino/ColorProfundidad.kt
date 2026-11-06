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
        return FabricaDominio.crearColorRgb(
            r = (r + (otro.r - r) * f).roundToInt(),
            g = (g + (otro.g - g) * f).roundToInt(),
            b = (b + (otro.b - b) * f).roundToInt()
        )
    }
}

private const val PROFUNDIDAD_MINIMA_M = 0.0
private const val PROFUNDIDAD_LIMITE_SUPERFICIE_M = 200.0
private const val PROFUNDIDAD_LIMITE_FOTICA_M = 1000.0

/**
 * Zonas de profundidad con su color y rango asociados.
 *
 * Cada zona conoce su propio intervalo, de modo que agregar una zona nueva no
 * obliga a modificar el calculador (OCP).
 */
enum class ColorProfundidad(
    private val r: Int,
    private val g: Int,
    private val b: Int,
    private val profundidadMinima: Double,
    private val profundidadMaxima: Double,
    private val incluyeMaximo: Boolean
) {
    SUPERFICIE(
        255,
        200,
        150,
        PROFUNDIDAD_MINIMA_M,
        PROFUNDIDAD_LIMITE_SUPERFICIE_M,
        true
    ),
    FOTICA(
        100,
        150,
        255,
        PROFUNDIDAD_LIMITE_SUPERFICIE_M,
        PROFUNDIDAD_LIMITE_FOTICA_M,
        false
    ),
    ABISAL(
        0,
        0,
        80,
        PROFUNDIDAD_LIMITE_FOTICA_M,
        Double.POSITIVE_INFINITY,
        true
    );

    val color: ColorRgb
        get() = FabricaDominio.crearColorRgb(r, g, b)

    /**
     * Indica si esta zona es la que corresponde a la [profundidad] indicada.
     */
    fun correspondeA(profundidad: Double): Boolean =
        CalculadorColorProfundidad.calcularZona(profundidad) == this

    /**
     * Comprueba si la profundidad cae dentro de los límites propios de esta zona.
     */
    internal fun rangoContiene(profundidad: Double): Boolean =
        profundidad.isFinite() &&
            profundidad >= profundidadMinima &&
            (if (incluyeMaximo) profundidad <= profundidadMaxima else profundidad < profundidadMaxima)

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

    const val PROFUNDIDAD_MINIMA = PROFUNDIDAD_MINIMA_M
    const val LIMITE_SUPERFICIE = PROFUNDIDAD_LIMITE_SUPERFICIE_M
    const val LIMITE_ZONA_FOTICA = PROFUNDIDAD_LIMITE_FOTICA_M

    /**
     * Determina la zona de profundidad correspondiente.
     *
     * @param profundidad Profundidad en metros.
     * @return Zona activa para la profundidad indicada.
     */
    fun calcularZona(profundidad: Double): ColorProfundidad {
        if (!profundidad.isFinite() || profundidad < PROFUNDIDAD_MINIMA) {
            return ColorProfundidad.ABISAL
        }
        return ColorProfundidad.entries
            .firstOrNull { it.rangoContiene(profundidad) }
            ?: ColorProfundidad.ABISAL
    }

    /**
     * Calcula el color apropiado para una profundidad dada.
     *
     * @param profundidad Profundidad en metros.
     * @return Color RGB de la zona activa.
     */
    fun calcularColor(profundidad: Double): ColorRgb = calcularZona(profundidad).color
}
