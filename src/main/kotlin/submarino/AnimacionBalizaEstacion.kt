package submarino

import javafx.animation.Animation
import javafx.animation.FadeTransition
import javafx.scene.shape.Circle
import javafx.util.Duration

internal class AnimacionBalizaEstacion {

    private var animacion: FadeTransition? = null

    fun iniciar(baliza: Circle) {
        val parpadeo = FadeTransition(Duration.seconds(0.85), baliza)
        parpadeo.fromValue = 0.25
        parpadeo.toValue = 1.0
        parpadeo.setAutoReverse(true)
        parpadeo.cycleCount = Animation.INDEFINITE
        animacion = parpadeo
        parpadeo.play()
    }

    fun detener() {
        animacion?.stop()
        animacion = null
    }
}
