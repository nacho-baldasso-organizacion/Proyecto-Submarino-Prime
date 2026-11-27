package submarino

import javafx.animation.Animation
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import kotlin.random.Random

/**
 * Sub-fábrica delegada de creación de componentes visuales core del juego.
 * Contiene métodos de instanciación de nodos, formas, submarinos y elementos
 * visuales principales con menos de 8 tipos del proyecto.
 */
object FabricaVisualCoreDelegate {

    fun crearAvisosJuego(): AvisosJuego = AvisosJuego()

    fun crearEstacionVectorial(): EstacionVectorial = EstacionVectorial()

    fun crearSubmarinoVectorial(): SubmarinoVectorial = SubmarinoVectorial()

    fun crearExplosionSubmarino(): ExplosionSubmarino = ExplosionSubmarino()

    fun crearFondoAbisal(
        anchoMundo: Double,
        altoMundo: Double,
        semilla: Long = 7L,
        profundidadRayos: Double = altoMundo * 0.35
    ): FondoAbisal = FondoAbisal(anchoMundo, altoMundo, semilla, profundidadRayos)

    fun crearHUDJuego(): HUDJuego = HUDJuego()

    fun crearNodoMineralVectorial(modelo: NodoMineral): NodoMineralVectorial =
        NodoMineralVectorial(modelo)

    fun crearColorRgb(r: Int, g: Int, b: Int): ColorRgb = ColorRgb(r = r, g = g, b = b)
}