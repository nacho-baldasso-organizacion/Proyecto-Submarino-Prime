package submarino

import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import kotlin.random.Random

/**
 * Fondo marino procedural dibujado con [Canvas].
 *
 * La capa lejana contiene montañas y arrecifes difusos; la capa media añade
 * siluetas más cercanas de arrecifes, algas y burbujas. Ambas capas se
 * desplazan con factores de parallax distintos desde la vista.
 *
 * Responsabilidad única (SRP): componer las dos capas delegando el trazo en
 * [PincelParticulas], [PincelCordillera] y [PincelArrecifes].
 */
class FondoAbisal(
    private val anchoMundo: Double,
    private val altoMundo: Double,
    private val semilla: Long = SEMILLA_DEFAULT,
    private val profundidadRayos: Double = altoMundo * 0.35
) {

    val capaLejana: Canvas = Canvas(anchoMundo, altoMundo)
    val capaMedia: Canvas = Canvas(anchoMundo, altoMundo)

    init {
        dibujarCapaLejana(Random(semilla))
        dibujarCapaMedia(Random(semilla + 1L))
    }

    private fun dibujarCapaLejana(random: Random) {
        val gc = capaLejana.graphicsContext2D

        gc.fill = LinearGradient(
            0.0, 0.0, 0.0, 1.0,
            true,
            CycleMethod.NO_CYCLE,
            Stop(0.0, Color.web("#57C7E3")),
            Stop(0.10, Color.web("#2E9DBC")),
            Stop(0.28, Color.web("#12667F")),
            Stop(0.52, Color.web("#073A4E")),
            Stop(0.76, Color.web("#031E2B")),
            Stop(1.0, Color.web("#01070B"))
        )
        gc.fillRect(0.0, 0.0, anchoMundo, altoMundo)

        val particulas = PincelParticulas(gc, random, anchoMundo, altoMundo, profundidadRayos)
        particulas.dibujarSuperficie()
        particulas.dibujarRayos()

        val cordillera = PincelCordillera(gc, random, anchoMundo)
        cordillera.dibujar(altoMundo * 0.46, altoMundo * 0.20, Color.web("#0B4C60", 0.62), 9, 0.16)
        cordillera.dibujar(altoMundo * 0.56, altoMundo * 0.24, Color.web("#083A4B", 0.74), 12, 0.14)
        cordillera.dibujar(altoMundo * 0.68, altoMundo * 0.22, Color.web("#052B39", 0.84), 16, 0.12)
        cordillera.dibujar(altoMundo * 0.81, altoMundo * 0.19, Color.web("#031E28", 0.90), 21, 0.10)
        cordillera.dibujar(altoMundo * 0.93, altoMundo * 0.16, Color.web("#02121A", 0.95), 27, 0.08)

        particulas.dibujarBurbujas(90, 1.0, 3.2, 0.10)
    }

    private fun dibujarCapaMedia(random: Random) {
        val gc = capaMedia.graphicsContext2D
        val arrecifes = PincelArrecifes(gc, random, anchoMundo, altoMundo)

        arrecifes.dibujarArrecifes(altoMundo * 0.58, Color.web("#0A3C47", 0.78), 10)
        arrecifes.dibujarEstructurasRocosas(altoMundo * 0.64, Color.web("#07313D", 0.88), 8)
        arrecifes.dibujarArrecifes(altoMundo * 0.76, Color.web("#062B35", 0.86), 14)
        arrecifes.dibujarEstructurasRocosas(altoMundo * 0.82, Color.web("#04232D", 0.92), 12)
        arrecifes.dibujarArrecifes(altoMundo * 0.92, Color.web("#031B23", 0.94), 18)
        arrecifes.dibujarEstructurasRocosas(altoMundo * 0.95, Color.web("#02151D", 0.96), 16)
        arrecifes.dibujarAlgas(34)
        arrecifes.dibujarCorales(22)

        PincelParticulas(gc, random, anchoMundo, altoMundo, profundidadRayos)
            .dibujarBurbujas(55, 1.4, 4.2, 0.16)
    }

    companion object {
        private const val SEMILLA_DEFAULT = 7L
    }
}
