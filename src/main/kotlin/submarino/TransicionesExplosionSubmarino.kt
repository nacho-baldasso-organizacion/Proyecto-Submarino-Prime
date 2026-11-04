package submarino

import javafx.animation.FadeTransition
import javafx.animation.ScaleTransition
import javafx.scene.Node
import javafx.util.Duration

internal object TransicionesExplosionSubmarino {

    fun escala(
        nodoAnimado: Node,
        duracion: Duration,
        desde: Double,
        hasta: Double
    ): ScaleTransition = ScaleTransition(duracion, nodoAnimado).apply {
        fromX = desde
        fromY = desde
        toX = hasta
        toY = hasta
    }

    fun desvanecer(
        nodoAnimado: Node,
        duracion: Duration,
        desde: Double,
        hasta: Double
    ): FadeTransition = FadeTransition(duracion, nodoAnimado).apply {
        fromValue = desde
        toValue = hasta
    }
}
