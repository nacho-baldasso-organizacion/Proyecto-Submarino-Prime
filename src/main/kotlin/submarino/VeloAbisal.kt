package submarino

import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

/**
 * Velo oscuro que refuerza la sensación de profundidad.
 *
 * Responsabilidad única (SRP): calcular y aplicar la opacidad del velo abisal.
 */
class VeloAbisal(anchoEscena: Double, altoEscena: Double) {

    val nodo: Rectangle = Rectangle(anchoEscena, altoEscena).apply {
        fill = Color.BLACK
        opacity = OPACIDAD_MINIMA
        isMouseTransparent = true
    }

    fun actualizar(profundidad: Double) {
        nodo.opacity = (
            OPACIDAD_MINIMA +
                (OPACIDAD_MAXIMA - OPACIDAD_MINIMA) *
                (profundidad / SesionJuego.ALTO_MAPA)
            ).coerceIn(OPACIDAD_MINIMA, OPACIDAD_MAXIMA)
    }

    companion object {
        private const val OPACIDAD_MINIMA = 0.05
        private const val OPACIDAD_MAXIMA = 0.68
    }
}
