package submarino

import javafx.scene.Group

/**
 * Representación vectorial de un nodo mineral.
 *
 * El color depende del tipo de recurso: cobrizo, gris metálico o dorado.
 */
class NodoMineralVectorial(
    private val modelo: NodoMineral
) {

    val nodo: Group = ConstructorNodoMineralVectorial.crear(modelo.tipo)

    init {
        actualizar()
    }

    /**
     * Oculta el nodo cuando ya fue recolectado.
     */
    fun actualizar() {
        nodo.isVisible = !modelo.recolectado
    }
}
