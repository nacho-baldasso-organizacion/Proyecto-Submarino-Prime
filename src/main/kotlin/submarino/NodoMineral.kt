package submarino

import kotlin.math.hypot

/**
 * Tipos de nodos mineros que aparecen de forma procedural en el mapa.
 *
 * La vista decide el color y la forma; el dominio solo conoce la equivalencia
 * con el catálogo de [TipoMineral] del backend.
 */
enum class TipoNodoMineral(
    val etiqueta: String,
    val tipoMineral: TipoMineral
) {
    COBRE("Cobre", TipoMineral.COBRE_MARINO),
    HIERRO("Hierro", TipoMineral.NODULO_DE_MANGANESO),
    ORO("Oro", TipoMineral.GEMA_DE_SALMUERA);

    fun correspondeA(mineral: Mineral): Boolean = mineral.tipo == tipoMineral

    fun correspondeAProfundidad(profundidad: Double): Boolean = when (this) {
        COBRE -> profundidad < LIMITE_HIERRO
        HIERRO -> profundidad >= LIMITE_HIERRO && profundidad < LIMITE_ORO
        ORO -> profundidad >= LIMITE_ORO
    }

    companion object {
        const val LIMITE_HIERRO = 1350.0
        const val LIMITE_ORO = 2250.0
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
