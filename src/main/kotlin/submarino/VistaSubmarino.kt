package submarino

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Parent
import javafx.scene.effect.DropShadow
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Circle
import javafx.scene.shape.Ellipse
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import javafx.scene.shape.Polygon
import javafx.scene.shape.QuadCurveTo
import javafx.scene.shape.Rectangle

/**
 * Vista principal del submarino.
 *
 * Compone las cuatro capas requeridas:
 * 0. fondo lejano (0.2x), 1. parallax intermedio (0.5x),
 * 2. entidades (1.0x) y 3. HUD fijo.
 *
 * Los nodos y colaboradores se mantienen privados; la escena solo expone
 * [obtenerRaiz] y el punto de refresco [actualizar].
 */
class VistaSubmarino(
    private val anchoEscena: Double,
    private val altoEscena: Double,
    private val hud: HUDSubmarino,
    capaFondoParallax: CapaParallax = CapaParallax(FACTOR_FONDO),
    capaIntermediaParallax: CapaParallax = CapaParallax(FACTOR_INTERMEDIO)
) {

    private val anchoMundo: Double = anchoEscena * ESCALA_MUNDO
    private val altoMundo: Double = altoEscena * ESCALA_MUNDO
    private val raiz: StackPane = StackPane()
    private val avatar: Group = crearAvatar()
    private val capaFondo: Pane = crearCapaFondo()
    private val capaIntermedia: Pane = crearCapaIntermedia()
    private val capaEntidades: Pane = crearCapaEntidades()
    private val parallaxFondo: CapaParallaxVisual
    private val parallaxIntermedio: CapaParallaxVisual

    init {
        raiz.style = "-fx-background-color: $COLOR_FONDO;"
        raiz.children.addAll(
            capaFondo,
            capaIntermedia,
            capaEntidades,
            hud.obtenerNodo()
        )
        StackPane.setAlignment(hud.obtenerNodo(), Pos.TOP_CENTER)
        StackPane.setMargin(hud.obtenerNodo(), Insets(18.0, 18.0, 0.0, 18.0))

        parallaxFondo = CapaParallaxVisual(
            contenedor = capaFondo,
            capa = capaFondoParallax,
            origenX = anchoEscena / 2.0,
            origenY = altoEscena / 2.0
        )
        parallaxIntermedio = CapaParallaxVisual(
            contenedor = capaIntermedia,
            capa = capaIntermediaParallax,
            origenX = anchoEscena / 2.0,
            origenY = altoEscena / 2.0
        )
    }

    /**
     * Nodo raíz que contiene todas las capas de la escena.
     */
    fun obtenerRaiz(): Parent = raiz

    /**
     * Refresca el avatar, las capas parallax y el HUD.
     */
    fun actualizar(estado: EstadoNavegacion) {
        avatar.layoutX = estado.posicionX
        avatar.layoutY = estado.posicionY
        parallaxFondo.actualizar(estado.posicionX, estado.posicionY)
        parallaxIntermedio.actualizar(estado.posicionX, estado.posicionY)
        hud.actualizar(estado)
    }

    private fun crearCapaFondo(): Pane {
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

    private fun crearCapaIntermedia(): Pane {
        val capa = crearCapaMundo()

        for (i in 0 until COLINAS) {
            val x = i * anchoMundo / COLINAS
            val ancho = anchoMundo / COLINAS * 1.4
            val alto = altoMundo * (0.18 + (i % 3) * 0.06)
            val color = when (i % 3) {
                0 -> Color.web("#062B3A")
                1 -> Color.web("#083545")
                else -> Color.web("#0A4152")
            }
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

    private fun crearCapaEntidades(): Pane = Pane().apply {
        prefWidth = anchoEscena
        prefHeight = altoEscena
        isPickOnBounds = false
        children.add(avatar)
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

    private fun crearAvatar(): Group {
        val haz = Polygon(
            46.0, -8.0,
            190.0, -36.0,
            190.0, 36.0,
            46.0, 8.0
        ).apply {
            fill = Color.web("#7FE3F0", 0.09)
        }

        val helice = Polygon(
            -52.0, -9.0,
            -64.0, -20.0,
            -60.0, 0.0,
            -64.0, 20.0,
            -52.0, 9.0
        ).apply {
            fill = Color.web("#B9821E")
            stroke = Color.web("#092A36")
            strokeWidth = 2.0
        }

        val aletaSuperior = Polygon(
            -18.0, -18.0,
            -30.0, -36.0,
            -4.0, -18.0
        ).apply {
            fill = Color.web("#D99A25")
            stroke = Color.web("#092A36")
            strokeWidth = 2.0
        }

        val aletaInferior = Polygon(
            -18.0, 18.0,
            -30.0, 36.0,
            -4.0, 18.0
        ).apply {
            fill = Color.web("#D99A25")
            stroke = Color.web("#092A36")
            strokeWidth = 2.0
        }

        val casco = Ellipse(0.0, 0.0, 46.0, 20.0).apply {
            fill = LinearGradient(
                0.0,
                0.0,
                0.0,
                1.0,
                true,
                CycleMethod.NO_CYCLE,
                Stop(0.0, Color.web("#FFE08A")),
                Stop(0.55, Color.web("#F4C542")),
                Stop(1.0, Color.web("#C98F1E"))
            )
            stroke = Color.web("#092A36")
            strokeWidth = 3.0
            effect = DropShadow(16.0, Color.web("#7FE3F0", 0.45))
        }

        val vela = Rectangle(-12.0, -36.0, 26.0, 18.0).apply {
            arcWidth = 8.0
            arcHeight = 8.0
            fill = Color.web("#E0A92F")
            stroke = Color.web("#092A36")
            strokeWidth = 2.5
        }

        val periscopio = Rectangle(-4.0, -54.0, 6.0, 18.0).apply {
            fill = Color.web("#D99A25")
            stroke = Color.web("#092A36")
            strokeWidth = 2.0
        }

        val ventana = Circle(18.0, 0.0, 8.0).apply {
            fill = Color.web("#9EF3FF")
            stroke = Color.web("#092A36")
            strokeWidth = 2.0
            effect = DropShadow(10.0, Color.web("#7FE3F0", 0.9))
        }

        val ventanaSecundaria = Circle(-4.0, 0.0, 5.0).apply {
            fill = Color.web("#7FE3F0")
            stroke = Color.web("#092A36")
            strokeWidth = 1.5
        }

        return Group().apply {
            children.addAll(
                haz,
                helice,
                aletaInferior,
                aletaSuperior,
                casco,
                vela,
                periscopio,
                ventana,
                ventanaSecundaria
            )
        }
    }

    companion object {
        private const val ESCALA_MUNDO = 2.0
        private const val FACTOR_FONDO = 0.2
        private const val FACTOR_INTERMEDIO = 0.5
        private const val COLOR_FONDO = "#031926"
        private const val RAYOS = 5
        private const val BURBUJAS_FONDO = 40
        private const val COLINAS = 8
        private const val ALGAS = 18
        private const val BURBUJAS_INTERMEDIAS = 22
    }
}
