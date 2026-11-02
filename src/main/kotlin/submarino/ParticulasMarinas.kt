package submarino

import javafx.animation.AnimationTimer
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import kotlin.math.sin
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
    private val particulas: List<Particula> = List(CANTIDAD) { crearParticula() }
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
        particulas.forEach { particula ->
            particula.y -= particula.velocidadY * delta
            particula.x += (
                particula.velocidadX +
                    sin(particula.fase + particula.y * ONDULACION) * DERIVA_LATERAL
                ) * delta
            if (particula.y < -particula.radio * 2.0) {
                particula.y = alto + particula.radio
                particula.x = random.nextDouble(0.0, ancho)
            }
            if (particula.x < -particula.radio) {
                particula.x = ancho + particula.radio
            } else if (particula.x > ancho + particula.radio) {
                particula.x = -particula.radio
            }
        }
    }

    private fun dibujar() {
        gc.clearRect(0.0, 0.0, ancho, alto)
        particulas.forEach { particula ->
            gc.fill = Color.web("#DFF8FF", particula.alfa)
            gc.fillOval(
                particula.x,
                particula.y,
                particula.radio * 2.0,
                particula.radio * 2.0
            )
        }
    }

    private fun crearParticula(): Particula = Particula(
        x = random.nextDouble(0.0, ancho),
        y = random.nextDouble(0.0, alto),
        radio = RADIO_MINIMO + random.nextDouble() * (RADIO_MAXIMO - RADIO_MINIMO),
        velocidadX = -4.0 + random.nextDouble() * 8.0,
        velocidadY = VELOCIDAD_MINIMA + random.nextDouble() * (VELOCIDAD_MAXIMA - VELOCIDAD_MINIMA),
        alfa = ALFA_MINIMA + random.nextDouble() * (ALFA_MAXIMA - ALFA_MINIMA),
        fase = random.nextDouble(0.0, Math.PI * 2.0)
    )

    private data class Particula(
        var x: Double,
        var y: Double,
        val radio: Double,
        val velocidadX: Double,
        val velocidadY: Double,
        val alfa: Double,
        val fase: Double
    )

    companion object {
        private const val SEMILLA_DEFAULT = 33L
        private const val CANTIDAD = 110
        private const val RADIO_MINIMO = 0.7
        private const val RADIO_MAXIMO = 2.8
        private const val VELOCIDAD_MINIMA = 5.0
        private const val VELOCIDAD_MAXIMA = 22.0
        private const val ALFA_MINIMA = 0.10
        private const val ALFA_MAXIMA = 0.46
        private const val DERIVA_LATERAL = 10.0
        private const val ONDULACION = 0.012
        private const val NANOSEGUNDOS_POR_SEGUNDO = 1_000_000_000.0
        private const val DELTA_MAXIMO = 0.05
    }
}
