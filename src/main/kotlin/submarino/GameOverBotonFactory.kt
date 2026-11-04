package submarino

import javafx.scene.control.Button

/**
 * Fábrica de botones del modal de fin de partida.
 *
 * Centraliza la creación de botones y su comportamiento de hover para que el
 * panel no repita la lógica visual de cada acción.
 */
internal object GameOverBotonFactory {

    fun crear(texto: String, principal: Boolean): Button = Button(texto).apply {
        style = GameOverEstilos.estiloBoton(principal, hover = false)
        setOnMouseEntered {
            style = GameOverEstilos.estiloBoton(principal, hover = true)
        }
        setOnMouseExited {
            style = GameOverEstilos.estiloBoton(principal, hover = false)
        }
    }
}
