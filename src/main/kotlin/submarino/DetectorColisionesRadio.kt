package submarino

import kotlin.math.hypot

/**
 * Detector de colisiones por proximidad radial en el plano 2D.
 *
 * Devuelve el recolectable no recolectado más cercano que se encuentre dentro
 * del radio indicado. La distancia se calcula con las coordenadas enteras del
 * mapa, por lo que el radio se expresa en las mismas unidades del dominio.
 */
class DetectorColisionesRadio(
    private val radio: Double
) : DetectorColisiones {

    init {
        require(radio >= 0.0) { "El radio de colisión no puede ser negativo: $radio" }
    }

    override fun verificarColision(
        posicion: Posicionable,
        elementos: List<Recolectable>
    ): Recolectable? = elementos
        .asSequence()
        .filter { !it.recolectado }
        .map { elemento ->
            elemento to hypot(
                (elemento.posX - posicion.posX).toDouble(),
                (elemento.posY - posicion.posY).toDouble()
            )
        }
        .filter { (_, distancia) -> distancia <= radio }
        .minByOrNull { (_, distancia) -> distancia }
        ?.first
}
