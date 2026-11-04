package submarino

import javafx.geometry.Pos
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Rectangle

/**
 * Compone y anima el fondo abisal del menú principal.
 */
class FondoMenuPrincipal(anchoEscena: Double, altoEscena: Double) {

    private val fondo: FondoAbisal = FondoAbisal(anchoEscena, altoEscena, SEMILLA_FONDO)
    private val peces: PecesMarinos = PecesMarinos(anchoEscena, altoEscena, SEMILLA_PECES)
    private val velo = Rectangle(anchoEscena, altoEscena).apply {
        fill = LinearGradient(
            0.0, 0.0, 0.0, 1.0,
            true,
            CycleMethod.NO_CYCLE,
            Stop(0.0, Color.web("#02121A", 0.10)),
            Stop(0.55, Color.web("#02121A", 0.22)),
            Stop(1.0, Color.web("#01070B", 0.72))
        )
        isMouseTransparent = true
    }

    fun incorporarEn(raiz: StackPane) {
        raiz.children.addAll(
            fondo.capaLejana,
            fondo.capaMedia,
            peces.nodo,
            velo
        )
        StackPane.setAlignment(fondo.capaLejana, Pos.TOP_LEFT)
        StackPane.setAlignment(fondo.capaMedia, Pos.TOP_LEFT)
        StackPane.setAlignment(peces.nodo, Pos.TOP_LEFT)
        StackPane.setAlignment(velo, Pos.TOP_LEFT)
    }

    fun iniciar() {
        peces.iniciar()
    }

    fun detener() {
        peces.detener()
    }

    companion object {
        private const val SEMILLA_FONDO = 11L
        private const val SEMILLA_PECES = 91L
    }
}
