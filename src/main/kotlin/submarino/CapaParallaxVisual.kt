package submarino

import javafx.scene.Node

/**
 * Adaptador visual de una capa de parallax.
 *
 * Traduce el desplazamiento de la cámara respecto de un origen en la
 * traslación de un [Node] de JavaFX, aplicando el factor de velocidad de
 * [CapaParallax].
 */
class CapaParallaxVisual(
    private val contenedor: Node,
    private val capa: CapaParallax,
    private val origenX: Double,
    private val origenY: Double
) {

    /**
     * Reposiciona la capa según la posición actual del submarino.
     */
    fun actualizar(posicionX: Double, posicionY: Double) {
        contenedor.translateX = -(posicionX - origenX) * capa.factorVelocidad
        contenedor.translateY = -(posicionY - origenY) * capa.factorVelocidad
    }
}
