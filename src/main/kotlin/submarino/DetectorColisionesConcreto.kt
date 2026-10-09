package submarino

/**
 * Detector concreto de colisiones en el plano 2D por coincidencia exacta de coordenadas (X, Y).
 *
 * Cumple con:
 * - Single Responsibility Principle (SRP): se encarga exclusivamente de resolver el contacto
 *   espacial entre la posición de una entidad y los objetos recolectables.
 * - Dependency Inversion Principle (DIP): implementa el contrato abstracto [DetectorColisiones].
 */
class DetectorColisionesConcreto : DetectorColisiones {

    /**
     * Verifica si la posición del submarino u objeto explorador coincide con las coordenadas
     * de algún elemento de la lista provista que todavía no haya sido recolectado.
     *
     * @param posicion Coordenadas del submarino u objeto que se desplaza.
     * @param elementos Colección de elementos recolectables en el mapa.
     * @return El primer [Recolectable] no recolectado en la misma posición, o `null` si no hay colisión.
     */
    override fun verificarColision(
        posicion: Posicionable,
        elementos: List<Recolectable>
    ): Recolectable? {
        return elementos.firstOrNull { item ->
            !item.recolectado && item.posX == posicion.posX && item.posY == posicion.posY
        }
    }
}
