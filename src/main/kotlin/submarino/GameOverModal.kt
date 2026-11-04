package submarino

import javafx.scene.Parent

/**
 * Pantalla modal de fin de partida.
 *
 * Se muestra cuando el oxígeno o el casco llegan a cero. Ofrece reintentar la
 * partida desde la superficie o regresar al menú principal.
 *
 * Orquesta el panel visual ([GameOverPanel]) y expone el ciclo de vida público
 * del modal. La composición y los estilos viven en colaboradores separados.
 */
class GameOverModal(
    alReintentar: () -> Unit,
    alVolverAlMenu: () -> Unit
) {

    private val panel: GameOverPanel = GameOverPanel(alReintentar, alVolverAlMenu)

    fun obtenerNodo(): Parent = panel.obtenerNodo()

    fun abrir(causa: String) {
        panel.abrir(causa)
    }

    fun cerrar() {
        panel.cerrar()
    }

    fun estaAbierto(): Boolean = panel.estaAbierto()
}
