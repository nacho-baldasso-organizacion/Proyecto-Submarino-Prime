package submarino

/**
 * Implementación inmutable de [Posicionable] para representar coordenadas en el plano 2D.
 * Encapsula la posición espacial bidimensional y provee operaciones geométricas elementales.
 */
data class Posicion(
    override val posX: Int,
    override val posY: Int
) : Posicionable {

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
}

