package submarino

import javafx.animation.Animation
import javafx.animation.RotateTransition
import javafx.scene.Group
import javafx.util.Duration

internal class AnimacionHeliceSubmarino {

    private var animacion: RotateTransition? = null

    fun iniciar(helice: Group) {
        val giro = RotateTransition(Duration.seconds(0.85), helice)
        giro.byAngle = 360.0
        giro.cycleCount = Animation.INDEFINITE
        animacion = giro
        giro.play()
    }

    fun detener() {
        animacion?.stop()
        animacion = null
    }
}
