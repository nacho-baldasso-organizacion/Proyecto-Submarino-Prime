package submarino

/**
 * Implementación inmutable de [Posicionable] para representar coordenadas en el plano 2D.
 * Encapsula la posición espacial bidimensional (campos con `private set`) y provee operaciones geométricas elementales.
 */
class Posicion(
    posX: Int,
    posY: Int
) : Posicionable {

    override var posX: Int = posX
        private set

    override var posY: Int = posY
        private set

    /**
     * Calcula la distancia Euclidiana respecto de otra posición dada.
     */
    fun distanciaA(otra: Posicionable): Double =
        Math.hypot((posX - otra.posX).toDouble(), (posY - otra.posY).toDouble())

    /**
     * Determina si dos posiciones comparten exactamente las mismas coordenadas espaciales.
     */
    fun coincideCon(otra: Posicionable): Boolean =
        posX == otra.posX && posY == otra.posY

    /**
     * Genera una nueva posición desplazada por deltas relativos.
     */
    fun trasladar(deltaX: Int, deltaY: Int): Posicion =
        Posicion(posX + deltaX, posY + deltaY)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Posicion) return false
        return posX == other.posX && posY == other.posY
    }

    override fun hashCode(): Int = 31 * posX + posY

    override fun toString(): String = "Posicion(posX=$posX, posY=$posY)"
}
