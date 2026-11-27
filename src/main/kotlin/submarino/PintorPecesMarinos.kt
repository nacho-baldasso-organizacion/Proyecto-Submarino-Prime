package submarino

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * Dibuja cada pez orientado según la tangente de su órbita.
 */
internal class PintorPecesMarinos(private val gc: GraphicsContext) {

    fun dibujar(peces: List<PezMarino>, tiempo: Double) {
        peces.forEach { dibujarPez(it, tiempo) }
    }

    private fun dibujarPez(pez: PezMarino, tiempo: Double) {
        val tamano =pez.tamano
        val alfa =pez.alfa
        val coleteo = sin(tiempo * VELOCIDAD_COLETEO +pez.fase) * tamano * 0.20
        val dx = -pez.radioX * sin(pez.angulo)
        val dy =pez.radioY * cos(pez.angulo)
        val rumbo = atan2(dy, dx)

        gc.save()
        gc.translate(pez.x,pez.y)
        gc.rotate(Math.toDegrees(rumbo))
        if (cos(rumbo) < 0.0) {
            gc.scale(1.0, -1.0)
        }

        dibujarAletaCuerpo(pez, tamano, alfa, coleteo)
        gc.restore()
    }

    private fun dibujarAletaCuerpo(pez: PezMarino, tamano: Double, alfa: Double, coleteo: Double) {
        gc.fill =pez.colorAleta.deriveColor(0.0, 1.0, 1.0,alfa)
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
            Stop(0.0,pez.colorCuerpo.interpolate(Color.web("#041A24"), 0.35).deriveColor(0.0, 1.0, 1.0,alfa)),
            Stop(0.55,pez.colorCuerpo.deriveColor(0.0, 1.0, 1.0,alfa)),
            Stop(1.0,pez.colorCuerpo.interpolate(Color.WHITE, 0.55).deriveColor(0.0, 1.0, 1.0,alfa))
        )
        gc.fillOval(-tamano * 0.56, -tamano * 0.30, tamano * 1.12, tamano * 0.60)
    }

    private companion object {
        private const val VELOCIDAD_COLETEO = 7.0
    }
}