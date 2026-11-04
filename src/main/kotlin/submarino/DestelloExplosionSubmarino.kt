package submarino

import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.RadialGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Circle

internal object DestelloExplosionSubmarino {

    private const val RADIO_DESTELLO = 26.0

    fun crear(): Circle = Circle(0.0, 0.0, RADIO_DESTELLO).apply {
        fill = RadialGradient(
            0.35, 0.35, 0.5, 0.5, 0.75,
            true,
            CycleMethod.NO_CYCLE,
            Stop(0.0, Color.web("#FFF6C9")),
            Stop(0.28, Color.web("#FFD166")),
            Stop(0.62, Color.web("#FF7A1A")),
            Stop(1.0, Color.web("#D6290B", 0.0))
        )
        effect = DropShadow(38.0, Color.web("#FF8A1E", 0.95))
    }
}
