package submarino

import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import javafx.scene.shape.Polygon
import javafx.scene.shape.QuadCurveTo

/**
 * Construye la capa intermedia con colinas, algas y burbujas.
 *
 * Responsabilidad única (SRP): geometría estática de la capa de parallax medio.
 */
class CapaIntermediaParallax(
    private val anchoMundo: Double,
    private val altoMundo: Double
) {

    fun crear(): Pane {
        val capa = crearCapaMundo()

        for (i in 0 until COLINAS) {
            val x = i * anchoMundo / COLINAS
            val ancho = anchoMundo / COLINAS * 1.4
            val alto = altoMundo * (0.18 + (i % 3) * 0.06)
            val color = COLORES_COLINA[i % COLORES_COLINA.size]
            capa.children.add(crearColina(x, ancho, alto, color))
        }

        for (i in 0 until ALGAS) {
            val x = (i + 0.5) * anchoMundo / ALGAS
            val altura = altoMundo * (0.12 + (i % 4) * 0.03)
            capa.children.add(crearAlga(x, altura, Color.web("#0E5A63", 0.55)))
        }

        for (i in 0 until BURBUJAS_INTERMEDIAS) {
            val x = (i * 283.0 + 90.0) % anchoMundo
            val y = (i * 173.0 + 60.0) % altoMundo
            val radio = 2.0 + (i % 5) * 1.3
            capa.children.add(
                Circle(x, y, radio).apply {
                    fill = Color.web("#9FE8F2", 0.16)
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

    private fun crearColina(x: Double, ancho: Double, alto: Double, color: Color): Polygon {
        val baseY = altoMundo
        return Polygon(
            x, baseY,
            x + ancho * 0.12, baseY - alto * 0.45,
            x + ancho * 0.30, baseY - alto * 0.28,
            x + ancho * 0.52, baseY - alto,
            x + ancho * 0.74, baseY - alto * 0.38,
            x + ancho * 0.90, baseY - alto * 0.55,
            x + ancho, baseY
        ).apply {
            fill = color
        }
    }

    private fun crearAlga(x: Double, altura: Double, color: Color): Path {
        val baseY = altoMundo
        return Path().apply {
            stroke = color
            strokeWidth = 3.0
            fill = null
            elements.addAll(
                MoveTo(x, baseY),
                QuadCurveTo(
                    x - 24.0,
                    baseY - altura * 0.45,
                    x + 10.0,
                    baseY - altura * 0.72
                ),
                QuadCurveTo(
                    x + 34.0,
                    baseY - altura * 0.88,
                    x + 4.0,
                    baseY - altura
                )
            )
        }
    }

    companion object {
        private const val COLINAS = 8
        private const val ALGAS = 18
        private const val BURBUJAS_INTERMEDIAS = 22

        private val COLORES_COLINA: List<Color> = listOf(
            Color.web("#062B3A"),
            Color.web("#083545"),
            Color.web("#0A4152")
        )
    }
}
