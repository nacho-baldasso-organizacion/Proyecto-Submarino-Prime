package submarino

import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Circle
import javafx.scene.shape.Polygon
import javafx.scene.shape.Rectangle

/**
 * Construye la capa de fondo lejano con gradiente, rayos y burbujas.
 *
 * Responsabilidad única (SRP): geometría estática de la capa más lejana.
 */
class CapaFondoParallax(
    private val anchoMundo: Double,
    private val altoMundo: Double
) {

    fun crear(): Pane {
        val capa = crearCapaMundo()

        val base = Rectangle(0.0, 0.0, anchoMundo, altoMundo).apply {
            fill = Color.web(COLOR_FONDO)
        }
        val gradiente = Rectangle(0.0, 0.0, anchoMundo, altoMundo).apply {
            fill = LinearGradient(
                0.0,
                0.0,
                0.0,
                1.0,
                true,
                CycleMethod.NO_CYCLE,
                Stop(0.0, Color.web("#0B3D4F", 0.55)),
                Stop(0.45, Color.web(COLOR_FONDO, 0.0)),
                Stop(1.0, Color.web("#010D13", 0.85))
            )
        }
        capa.children.addAll(base, gradiente)

        for (i in 0 until RAYOS) {
            val x = (i + 0.5) * anchoMundo / RAYOS
            val anchoRayo = 80.0 + (i % 3) * 40.0
            val rayo = Polygon(
                x, 0.0,
                x + anchoRayo, 0.0,
                x + anchoRayo * 1.8, altoMundo,
                x + anchoRayo * 0.8, altoMundo
            ).apply {
                fill = Color.web("#7FE3F0", 0.025 + (i % 3) * 0.008)
            }
            capa.children.add(rayo)
        }

        for (i in 0 until BURBUJAS_FONDO) {
            val x = (i * 197.0) % anchoMundo
            val y = (i * 131.0) % altoMundo
            val radio = 1.5 + (i % 4) * 1.2
            capa.children.add(
                Circle(x, y, radio).apply {
                    fill = Color.web("#B8F3FF", 0.12 + (i % 3) * 0.05)
                }
            )
        }

        return capa
    }

    private fun crearCapaMundo(): Pane = Pane().apply {
        prefWidth = anchoMundo
        prefHeight = altoMundo
        minWidth = anchoMundo
        minHeight = altoMundo
        maxWidth = anchoMundo
        maxHeight = altoMundo
        isPickOnBounds = false
    }

    companion object {
        const val COLOR_FONDO = "#031926"
        private const val RAYOS = 5
        private const val BURBUJAS_FONDO = 40
    }
}
