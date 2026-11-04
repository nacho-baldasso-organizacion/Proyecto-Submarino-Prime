package submarino

import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.util.Duration
import kotlin.math.PI
import kotlin.random.Random

internal class ChispaExplosionSubmarino private constructor(
    val nodo: Circle,
    val angulo: Double,
    val distancia: Double,
    val duracion: Duration
) {
    companion object {
        fun crear(random: Random): ChispaExplosionSubmarino {
            val angulo = random.nextDouble(0.0, PI * 2.0)
            val distancia = 55.0 + random.nextDouble() * 250.0
            val chispa = Circle(
                0.0,
                0.0,
                1.8 + random.nextDouble() * 4.2
            ).apply {
                fill = when (random.nextInt(3)) {
                    0 -> Color.web("#FFE08A")
                    1 -> Color.web("#FF9F1C")
                    else -> Color.web("#FF5A2A")
                }
                effect = DropShadow(12.0, Color.web("#FF9F1C", 0.85))
            }
            val duracion = Duration.seconds(0.65 + random.nextDouble() * 0.85)
            return ChispaExplosionSubmarino(chispa, angulo, distancia, duracion)
        }
    }
}
