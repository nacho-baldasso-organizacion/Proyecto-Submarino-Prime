package submarino

import kotlin.math.hypot

/**
 * Tipos de nodos mineros que aparecen de forma procedural en el mapa.
 *
 * La vista decide el color y la forma; el dominio solo conoce la equivalencia
 * con el catálogo de [TipoMineral] del backend.
 */
private const val PROFUNDIDAD_LIMITE_HIERRO = 1350.0
private const val PROFUNDIDAD_LIMITE_ORO = 2250.0

enum class TipoNodoMineral(
    val etiqueta: String,
    val tipoMineral: TipoMineral,
    private val profundidadMinima: Double,
    private val profundidadMaxima: Double,
    private val incluyeMaximo: Boolean
) {
    COBRE(
        "Cobre",
        TipoMineral.COBRE_MARINO,
        Double.NEGATIVE_INFINITY,
        PROFUNDIDAD_LIMITE_HIERRO,
        false
    ),
    HIERRO(
        "Hierro",
        TipoMineral.NODULO_DE_MANGANESO,
        PROFUNDIDAD_LIMITE_HIERRO,
        PROFUNDIDAD_LIMITE_ORO,
        false
    ),
    ORO(
        "Oro",
        TipoMineral.GEMA_DE_SALMUERA,
        PROFUNDIDAD_LIMITE_ORO,
        Double.POSITIVE_INFINITY,
        true
    );

    fun correspondeA(mineral: Mineral): Boolean = mineral.tipo == tipoMineral

    fun correspondeAProfundidad(profundidad: Double): Boolean =
        profundidad >= profundidadMinima &&
            (if (incluyeMaximo) profundidad <= profundidadMaxima else profundidad < profundidadMaxima)

    companion object {
        const val LIMITE_HIERRO = PROFUNDIDAD_LIMITE_HIERRO
        const val LIMITE_ORO = PROFUNDIDAD_LIMITE_ORO
    }
}

/**
 * Nodo minero del mapa. Mantiene el [Mineral] de dominio y su radio de
 * interacción, sin exponer campos mutables a la vista.
 */
data class NodoMineral(
    val id: String,
    val tipo: TipoNodoMineral,
    val mineral: Mineral,
    val radio: Double = SesionJuego.RADIO_INTERACCION
) {
    val x: Double
        get() = mineral.posX.toDouble()

    val y: Double
        get() = mineral.posY.toDouble()

    val recolectado: Boolean
        get() = mineral.recolectado

    fun estaDisponible(): Boolean = !recolectado && mineral.estaDisponibleParaRecoleccion()

    fun distanciaA(px: Double, py: Double): Double = hypot(x - px, y - py)

    fun estaDentroDelRadio(px: Double, py: Double): Boolean = distanciaA(px, py) <= radio
}
