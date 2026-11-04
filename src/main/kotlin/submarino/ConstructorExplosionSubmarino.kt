package submarino

import javafx.animation.Animation
import javafx.animation.TranslateTransition
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.util.Duration
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

internal object ConstructorExplosionSubmarino {

    private const val ANILLOS = 3
    private const val CHISPAS = 34

    fun crear(destino: ObservableList<Node>, random: Random): List<Animation> {
        val transiciones = mutableListOf<Animation>()

        val destello = DestelloExplosionSubmarino.crear()
        destino.add(destello)
        transiciones.add(
            TransicionesExplosionSubmarino.escala(destello, Duration.seconds(0.85), 0.4, 7.5)
        )
        transiciones.add(
            TransicionesExplosionSubmarino.desvanecer(destello, Duration.seconds(0.85), 1.0, 0.0)
        )

        repeat(ANILLOS) { indice ->
            val anillo = AnilloExplosionSubmarino.crear(indice)
            destino.add(anillo)
            val duracion = Duration.seconds(1.1 + indice * 0.22)
            transiciones.add(
                TransicionesExplosionSubmarino.escala(anillo, duracion, 0.25, 4.8 + indice * 0.8)
            )
            transiciones.add(
                TransicionesExplosionSubmarino.desvanecer(anillo, duracion, 0.95, 0.0)
            )
        }

        repeat(CHISPAS) {
            val chispa = ChispaExplosionSubmarino.crear(random)
            destino.add(chispa.nodo)

            val desplazamiento = TranslateTransition(chispa.duracion, chispa.nodo).apply {
                byX = cos(chispa.angulo) * chispa.distancia
                byY = sin(chispa.angulo) * chispa.distancia
            }
            transiciones.add(desplazamiento)
            transiciones.add(
                TransicionesExplosionSubmarino.desvanecer(chispa.nodo, chispa.duracion, 1.0, 0.0)
            )
        }

        return transiciones
    }
}
