package submarino

import javafx.animation.AnimationTimer
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
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
    private val peces: List<Pez> = List(cantidad) { crearPez(it) }
    private var animacion: AnimationTimer? = null
    private var ultimoInstante: Long = 0L
    private var tiempo: Double = 0.0

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
        tiempo += delta
        peces.forEach { pez ->
            pez.angulo += pez.velocidadAngular * delta
            pez.x = pez.centroX + pez.radioX * cos(pez.angulo)
            pez.y = pez.centroY +
                pez.radioY * sin(pez.angulo) +
                sin(tiempo * pez.frecuencia + pez.fase) * pez.amplitud
        }
    }

    private fun dibujar() {
        gc.clearRect(0.0, 0.0, ancho, alto)
        peces.forEach { dibujarPez(it) }
    }

    private fun dibujarPez(pez: Pez) {
        val dx = -pez.radioX * sin(pez.angulo)
        val dy = pez.radioY * cos(pez.angulo)
        val rumbo = atan2(dy, dx)
        val tamano = pez.tamano
        val coleteo = sin(tiempo * VELOCIDAD_COLETEO + pez.fase) * tamano * 0.20

        gc.save()
        gc.translate(pez.x, pez.y)
        gc.rotate(Math.toDegrees(rumbo))
        if (cos(rumbo) < 0.0) {
            gc.scale(1.0, -1.0)
        }

        gc.fill = pez.colorAleta.deriveColor(0.0, 1.0, 1.0, pez.alfa)
        gc.fillPolygon(
            doubleArrayOf(-tamano * 0.42, -tamano * 0.92, -tamano * 0.82, -tamano * 0.42),
            doubleArrayOf(0.0, -tamano * 0.40 + coleteo, tamano * 0.40 + coleteo, 0.0),
            4
        )

        gc.fillPolygon(
            doubleArrayOf(-tamano * 0.12, tamano * 0.06, tamano * 0.22),
            doubleArrayOf(-tamano * 0.20, -tamano * 0.54, -tamano * 0.18),
            3
        )

        gc.fillPolygon(
            doubleArrayOf(-tamano * 0.10, tamano * 0.02, tamano * 0.18),
            doubleArrayOf(tamano * 0.22, tamano * 0.50, tamano * 0.20),
            3
        )

        gc.fill = LinearGradient(
            0.0, -tamano * 0.32, 0.0, tamano * 0.32,
            false,
            CycleMethod.NO_CYCLE,
            Stop(0.0, pez.colorCuerpo.interpolate(Color.web("#041A24"), 0.35).deriveColor(0.0, 1.0, 1.0, pez.alfa)),
            Stop(0.55, pez.colorCuerpo.deriveColor(0.0, 1.0, 1.0, pez.alfa)),
            Stop(1.0, pez.colorCuerpo.interpolate(Color.WHITE, 0.55).deriveColor(0.0, 1.0, 1.0, pez.alfa))
        )
        gc.fillOval(-tamano * 0.56, -tamano * 0.30, tamano * 1.12, tamano * 0.60)

        gc.fill = Color.web("#F4FEFF", pez.alfa * 0.95)
        gc.fillOval(tamano * 0.30, -tamano * 0.12, tamano * 0.16, tamano * 0.16)
        gc.fill = Color.web("#04212B", pez.alfa)
        gc.fillOval(tamano * 0.34, -tamano * 0.09, tamano * 0.09, tamano * 0.09)

        gc.restore()
    }

    private fun crearPez(indice: Int): Pez {
        val tamano = TAMANO_MINIMO + random.nextDouble() * (TAMANO_MAXIMO - TAMANO_MINIMO)
        val radioX = 50.0 + random.nextDouble() * 210.0
        val radioY = 25.0 + random.nextDouble() * 95.0
        val margenX = radioX + tamano * 2.0
        val margenY = radioY + tamano * 2.0

        return Pez(
            centroX = if (ancho > margenX * 2.0) {
                random.nextDouble(margenX, ancho - margenX)
            } else {
                ancho / 2.0
            },
            centroY = if (alto > margenY * 2.0) {
                random.nextDouble(margenY, alto - margenY)
            } else {
                alto / 2.0
            },
            radioX = radioX,
            radioY = radioY,
            velocidadAngular = (0.22 + random.nextDouble() * 0.42) * if (indice % 2 == 0) 1.0 else -1.0,
            angulo = random.nextDouble(0.0, PI * 2.0),
            fase = random.nextDouble(0.0, PI * 2.0),
            frecuencia = 0.8 + random.nextDouble() * 1.4,
            amplitud = 4.0 + random.nextDouble() * 12.0,
            tamano = tamano,
            colorCuerpo = PALETA_CUERPO[indice % PALETA_CUERPO.size],
            colorAleta = PALETA_ALETA[indice % PALETA_ALETA.size],
            alfa = 0.55 + random.nextDouble() * 0.35
        )
    }

    private data class Pez(
        val centroX: Double,
        val centroY: Double,
        val radioX: Double,
        val radioY: Double,
        val velocidadAngular: Double,
        var angulo: Double,
        val fase: Double,
        val frecuencia: Double,
        val amplitud: Double,
        val tamano: Double,
        val colorCuerpo: Color,
        val colorAleta: Color,
        val alfa: Double,
        var x: Double = centroX + radioX * cos(angulo),
        var y: Double = centroY + radioY * sin(angulo)
    )

    companion object {
        private const val SEMILLA_DEFAULT = 91L
        private const val CANTIDAD_PECES = 14
        private const val TAMANO_MINIMO = 9.0
        private const val TAMANO_MAXIMO = 26.0
        private const val VELOCIDAD_COLETEO = 7.0
        private const val NANOSEGUNDOS_POR_SEGUNDO = 1_000_000_000.0
        private const val DELTA_MAXIMO = 0.05

        private val PALETA_CUERPO = listOf(
            Color.web("#FFD75A"),
            Color.web("#7FE3F0"),
            Color.web("#F59E72"),
            Color.web("#8FE3B0"),
            Color.web("#B8C7FF"),
            Color.web("#E8D5A8")
        )

        private val PALETA_ALETA = listOf(
            Color.web("#D99A25"),
            Color.web("#3FB8CF"),
            Color.web("#D9774F"),
            Color.web("#4FB27A"),
            Color.web("#7E92E0"),
            Color.web("#C7AE7A")
        )
    }
}
