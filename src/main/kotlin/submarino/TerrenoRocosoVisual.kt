package submarino

import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import kotlin.random.Random

/**
 * Capa visual del terreno rocoso dibujada una sola vez sobre un [Canvas].
 *
 * Responsabilidad única (SRP): recorrer las formaciones y delegar cada trazo
 * en [PincelFormacionRocosa].
 */
class TerrenoRocosoVisual(
    private val terreno: TerrenoSubmarino,
    anchoMundo: Double,
    altoMundo: Double,
    private val escala: Double,
    semilla: Long = SEMILLA_DEFAULT
) {

    val nodo: Canvas = Canvas(anchoMundo, altoMundo).apply {
        isMouseTransparent = true
    }

    private val gc: GraphicsContext = nodo.graphicsContext2D
    private val random = Random(semilla)
    private val pincel = PincelFormacionRocosa(gc, random, escala)

    init {
        dibujar()
    }

    private fun dibujar() {
        pincel.dibujar(terreno.sueloMarino, esSuelo = true)
        terreno.formaciones.forEach { pincel.dibujar(it, esSuelo = false) }
    }

    companion object {
        private const val SEMILLA_DEFAULT = 53L
    }
}
