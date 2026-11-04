package submarino

import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.RadialGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Rectangle

/**
 * Velo rojo radial que avisa de la presión crítica del casco.
 *
 * Responsabilidad única (SRP): calcular y aplicar la opacidad del aviso de presión.
 */
class VeloPresion(anchoEscena: Double, altoEscena: Double) {

    val nodo: Rectangle = Rectangle(anchoEscena, altoEscena).apply {
        fill = RadialGradient(
            0.5, 0.5, 0.5, 0.5, 0.78,
            true,
            CycleMethod.NO_CYCLE,
            Stop(0.0, Color.web("#FF3A00", 0.0)),
            Stop(0.62, Color.web("#FF3A00", 0.08)),
            Stop(0.86, Color.web("#E01B00", 0.30)),
            Stop(1.0, Color.web("#9E0000", 0.62))
        )
        opacity = 0.0
        isMouseTransparent = true
    }

    fun actualizar(presionActual: Double, presionMaxima: Double) {
        val maxima = presionMaxima.coerceAtLeast(1.0)
        val ratioPresion = (presionActual / maxima).coerceAtLeast(0.0)
        nodo.opacity = (
            (ratioPresion - UMBRAL_PRESION_AVISO) / (1.0 - UMBRAL_PRESION_AVISO)
            ).coerceIn(0.0, 1.0) * OPACIDAD_MAXIMA
    }

    companion object {
        private const val UMBRAL_PRESION_AVISO = 0.74
        private const val OPACIDAD_MAXIMA = 0.42
    }
}
