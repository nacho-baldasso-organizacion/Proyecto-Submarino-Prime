package submarino

import javafx.animation.AnimationTimer
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import kotlin.random.Random

/**
 * Capa animada de peces que recorren trayectorias elípticas por la escena.
 *
 * Cada pez orbita alrededor de un centro propio y orienta su cuerpo según la
 * tangente de la curva, con un ligero balanceo de la cola. La capa vive en
 * coordenadas de pantalla y no intercepta eventos del ratón.
 */
class PecesMarinos(
    private val ancho: Double,
    private val alto: Double,
    semilla: Long = 91L,
    cantidad: Int = 14
) {

    internal val nodo: Canvas = Canvas(ancho, alto).apply {
        isMouseTransparent = true
    }

    private val gc: GraphicsContext = nodo.graphicsContext2D
    private val random: Random = Random(semilla)
    private val fabrica: FabricaPecesMarinos = FabricaPecesMarinos(random, ancho, alto)
    private val peces: List<PezMarino> = List(cantidad) { fabrica.crear(it) }
    private val actualizador: ActualizadorPecesMarinos = ActualizadorPecesMarinos()
    private val pintor: PintorPecesMarinos = PintorPecesMarinos(gc)
    private var animacion: AnimationTimer? = null
    private var ultimoInstante: Long = 0L

    internal data class Estado(
        val ancho: Double,
        val alto: Double,
        val nodo: Canvas,
        val gc: GraphicsContext,
        val random: Random,
        val fabrica: FabricaPecesMarinos,
        val peces: List<PezMarino>,
        val actualizador: ActualizadorPecesMarinos,
        val pintor: PintorPecesMarinos,
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
        peces = peces,
        actualizador = actualizador,
        pintor = pintor,
        animacion = animacion,
        ultimoInstante = ultimoInstante
    )

    /**
     * Inicia el nado continuo de los peces.
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
     * Detiene la animación cuando la vista deja de estar activa.
     */
    fun detener() {
        animacion?.stop()
        animacion = null
        ultimoInstante = 0L
    }

    private fun actualizar(delta: Double) {
        actualizador.actualizar(peces, delta)
    }

    private fun dibujar() {
        gc.clearRect(0.0, 0.0, ancho, alto)
        pintor.dibujar(peces, actualizador.tiempo)
    }
}