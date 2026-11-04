package submarino

import javafx.scene.paint.Color
import kotlin.math.cos
import kotlin.math.sin

/**
 * Modelo de un pez con su órbita, balanceo y apariencia.
 */
internal data class PezMarino(
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
