package submarino

import javafx.animation.AnimationTimer
import javafx.scene.canvas.Canvas
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
    semilla: Long = SEMILLA_DEFAULT,
    cantidad: Int = CANTIDAD_PECES
) {

    val nodo: Canvas = Canvas(ancho, alto).apply {
        isMouseTransparent = true
    }

    private val gc = nodo.graphicsContext2D
    private val random = Random(semilla)
    private val fabrica = FabricaPecesMarinos(random, ancho, alto)
    private val peces: List<PezMarino> = List(cantidad) { fabrica.crear(it) }
    private val actualizador = ActualizadorPecesMarinos()
    private val pintor = PintorPecesMarinos(gc)
    private var animacion: AnimationTimer? = null
    private var ultimoInstante: Long = 0L

    init {
        dibujar()
    }

    /**
     * Inicia el nado continuo de los peces.
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

    companion object {
        private const val SEMILLA_DEFAULT = 91L
        private const val CANTIDAD_PECES = 14
        private const val NANOSEGUNDOS_POR_SEGUNDO = 1_000_000_000.0
        private const val DELTA_MAXIMO = 0.05
    }
}
