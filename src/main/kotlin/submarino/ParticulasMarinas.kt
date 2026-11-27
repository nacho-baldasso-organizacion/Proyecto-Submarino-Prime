package submarino

import javafx.animation.AnimationTimer
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
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
    semilla: Long = 33L
) {

    internal val nodo: Canvas = Canvas(ancho, alto).apply {
        isMouseTransparent = true
    }

    private val gc: GraphicsContext = nodo.graphicsContext2D
    private val random: Random = Random(semilla)
    private val fabrica: FabricaParticulasMarinas = FabricaParticulasMarinas(random, ancho, alto)
    private val particulas: List<ParticulaMarina> = List(110) { fabrica.crear() }
    private val actualizador: ActualizadorParticulasMarinas = ActualizadorParticulasMarinas(ancho, alto, random)
    private val pintor: PintorParticulasMarinas = PintorParticulasMarinas(gc)
    private var animacion: AnimationTimer? = null
    private var ultimoInstante: Long = 0L

    internal data class Estado(
        val ancho: Double,
        val alto: Double,
        val nodo: Canvas,
        val gc: GraphicsContext,
        val random: Random,
        val fabrica: FabricaParticulasMarinas,
        val particulas: List<ParticulaMarina>,
        val actualizador: ActualizadorParticulasMarinas,
        val pintor: PintorParticulasMarinas,
        val animacion: AnimationTimer?,
        val ultimoInstante: Long
    )

    private val state = Estado(
        ancho = ancho,
        alto = alto,
        nodo = nodo,
        gc = gc,
        random = random,
        fabrica = fabrica,
        particulas = particulas,
        actualizador = actualizador,
        pintor = pintor,
        animacion = animacion,
        ultimoInstante = ultimoInstante
    )

    /**
     * Inicia el movimiento continuo de las partículas.
     */
    fun iniciar() {
        if (state.animacion != null) {
            return
        }
        val timer = object : AnimationTimer() {
            override fun handle(ahora: Long) {
                if (state.ultimoInstante == 0L) {
                    ultimoInstante = ahora
                    dibujar()
                    return
                }
                val delta = ((ahora - ultimoInstante) / 1_000_000_000.0)
                    .coerceIn(0.0, 0.05)
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
        actualizador.actualizar(state.particulas, delta)
    }

    private fun dibujar() {
        state.gc.clearRect(0.0, 0.0, state.ancho, state.alto)
        state.pintor.dibujar(state.particulas)
    }
}