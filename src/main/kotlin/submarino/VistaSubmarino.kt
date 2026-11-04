package submarino

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Parent
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane

/**
 * Vista principal del submarino.
 *
 * Compone las cuatro capas requeridas:
 * 0. fondo lejano (0.2x), 1. parallax intermedio (0.5x),
 * 2. entidades (1.0x) y 3. HUD fijo.
 *
 * Los nodos y colaboradores se mantienen privados; la escena solo expone
 * [obtenerRaiz] y el punto de refresco [actualizar].
 *
 * Responsabilidad única (SRP): orquestar capas; la geometría se delega en
 * [AvatarSubmarino], [CapaFondoParallax] y [CapaIntermediaParallax].
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
    private val avatar: Group = AvatarSubmarino().crear()
    private val capaFondo: Pane = CapaFondoParallax(anchoMundo, altoMundo).crear()
    private val capaIntermedia: Pane = CapaIntermediaParallax(anchoMundo, altoMundo).crear()
    private val capaEntidades: Pane = crearCapaEntidades()
    private val parallaxFondo: CapaParallaxVisual
    private val parallaxIntermedio: CapaParallaxVisual

    init {
        raiz.style = "-fx-background-color: ${CapaFondoParallax.COLOR_FONDO};"
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

    private fun crearCapaEntidades(): Pane = Pane().apply {
        prefWidth = anchoEscena
        prefHeight = altoEscena
        isPickOnBounds = false
        children.add(avatar)
    }

    companion object {
        private const val ESCALA_MUNDO = 2.0
        private const val FACTOR_FONDO = 0.2
        private const val FACTOR_INTERMEDIO = 0.5
    }
}
