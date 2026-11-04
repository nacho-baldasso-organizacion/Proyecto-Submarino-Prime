package submarino

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

/**
 * Dibuja las partículas como pequeños puntos luminosos.
 */
internal class PintorParticulasMarinas(private val gc: GraphicsContext) {

    fun dibujar(particulas: List<ParticulaMarina>) {
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
}
