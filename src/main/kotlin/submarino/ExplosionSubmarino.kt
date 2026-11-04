package submarino

import javafx.animation.Animation
import javafx.animation.ParallelTransition
import javafx.scene.Group
import kotlin.random.Random

/**
 * Efecto vectorial de explosión del submarino.
 *
 * Combina un destello central, anillos expansivos y chispas radiales. Se coloca
 * en coordenadas del mundo y queda oculto hasta que la nave es destruida.
 */
class ExplosionSubmarino {

    val nodo: Group = Group().apply {
        isVisible = false
        isMouseTransparent = true
        isPickOnBounds = false
    }

    private var animacion: Animation? = null

    /**
     * Lanza la explosión en la posición indicada del mundo.
     */
    fun detonar(x: Double, y: Double) {
        detenerAnimaciones()
        nodo.layoutX = x
        nodo.layoutY = y
        nodo.children.clear()

        val transiciones = ConstructorExplosionSubmarino.crear(
            nodo.children,
            Random(SEMILLA_CHISPAS)
        )

        val total = ParallelTransition(*transiciones.toTypedArray()).apply {
            setOnFinished {
                nodo.isVisible = false
                nodo.children.clear()
            }
        }
        animacion = total
        nodo.isVisible = true
        total.play()
    }

    /**
     * Cancela la animación y oculta el efecto.
     */
    fun detenerAnimaciones() {
        animacion?.stop()
        animacion = null
        nodo.isVisible = false
        nodo.children.clear()
    }

    companion object {
        private const val SEMILLA_CHISPAS = 77L
    }
}
