package submarino

import javafx.animation.AnimationTimer
import javafx.scene.canvas.Canvas
import kotlin.random.Random

/**
 * Capa de partículas suspendidas que simula nieve marina y plancton.
 *
 * Las partículas viven en coordenadas de pantalla y se desplazan lentamente
 * hacia la superficie con una deriva lateral suave. La capa es transparente y
 * no intercepta eventos del ratón.
 */
class ParticulasMarinas(
    private val ancho: Double,
    private val alto: Double,
    semilla: Long = SEMILLA_DEFAULT
) {

    val nodo: Canvas = Canvas(ancho, alto).apply {
        isMouseTransparent = true
    }

    private val gc = nodo.graphicsContext2D
    private val random = Random(semilla)
    private val fabrica = FabricaParticulasMarinas(random, ancho, alto)
    private val particulas: List<ParticulaMarina> = List(CANTIDAD) { fabrica.crear() }
    private val actualizador = ActualizadorParticulasMarinas(ancho, alto, random)
    private val pintor = PintorParticulasMarinas(gc)
    private var animacion: AnimationTimer? = null
    private var ultimoInstante: Long = 0L

    init {
        dibujar()
    }

    /**
     * Inicia el movimiento continuo de las partículas.
     */
    fun iniciar() {
        if (animacion != null) {
            return
        }
        val timer = object : AnimationTimer() {
            override fun handle(ahora: Long) {
                if (ultimoInstante == 0L) {
                    ultimoInstante = ahora
                    dibujar()
                    return
                }
                val delta = ((ahora - ultimoInstante) / NANOSEGUNDOS_POR_SEGUNDO)
                    .coerceIn(0.0, DELTA_MAXIMO)
                ultimoInstante = ahora
                actualizar(delta)
                dibujar()
            }
        }
        animacion = timer
        timer.start()
    }

    /**
     * Detiene la animación al abandonar la partida.
     */
    fun detener() {
        animacion?.stop()
        animacion = null
        ultimoInstante = 0L
    }

    private fun actualizar(delta: Double) {
        actualizador.actualizar(particulas, delta)
    }

    private fun dibujar() {
        gc.clearRect(0.0, 0.0, ancho, alto)
        pintor.dibujar(particulas)
    }

    companion object {
        private const val SEMILLA_DEFAULT = 33L
        private const val CANTIDAD = 110
        private const val NANOSEGUNDOS_POR_SEGUNDO = 1_000_000_000.0
        private const val DELTA_MAXIMO = 0.05
    }
}
