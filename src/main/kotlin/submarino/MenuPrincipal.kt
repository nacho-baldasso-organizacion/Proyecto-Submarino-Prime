package submarino

import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.layout.StackPane

/**
 * Pantalla de inicio del juego.
 *
 * Orquesta el fondo abisal procedural, el título, los botones JUGAR/REGLAS y un
 * panel modal de reglas. No utiliza imágenes externas.
 */
class MenuPrincipal(
    private val anchoEscena: Double,
    private val altoEscena: Double,
    private val alJugar: () -> Unit
) {

    private val raiz: StackPane = StackPane()
    private val fondoMenu: FondoMenuPrincipal = FabricaHUD.crearFondoMenuPrincipal(anchoEscena, altoEscena)
    private val submarino: SubmarinoVectorial = FabricaHUD.crearSubmarinoVectorial().apply {
        nodo.scaleX = 1.55
        nodo.scaleY = 1.55
    }
    private val botones: BotonesMenuPrincipal = FabricaHUD.crearBotonesMenuPrincipal()
    private val overlayReglas: PanelReglasMenuPrincipal = FabricaHUD.crearPanelReglasMenuPrincipal(raiz, botones)

    init {
        val contenido = FabricaHUD.crearContenidoMenuPrincipal(
            bloqueTexto = FabricaHUD.crearTituloMenuPrincipal().crear(),
            panelBotones = botones.crearPanel(alJugar) { mostrarReglas(true) },
            nodoSubmarino = submarino.nodo
        ).crear()

        fondoMenu.incorporarEn(raiz)
        raiz.children.addAll(contenido, overlayReglas.obtenerNodo())
        StackPane.setAlignment(contenido, Pos.CENTER)
        StackPane.setAlignment(overlayReglas.obtenerNodo(), Pos.CENTER)
        fondoMenu.iniciar()
    }

    fun obtenerRaiz(): Parent = raiz

    /**
     * Libera las animaciones del menú al cambiar de pantalla.
     */
    fun detenerAnimaciones() {
        submarino.detenerAnimaciones()
        fondoMenu.detener()
    }

    private fun mostrarReglas(mostrar: Boolean) {
        overlayReglas.mostrar(mostrar)
    }
}
