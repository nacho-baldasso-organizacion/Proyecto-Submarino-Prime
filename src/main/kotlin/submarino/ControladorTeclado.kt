package submarino

import javafx.beans.value.ChangeListener
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.stage.Window

/**
 * Controlador de entrada por teclado para la escena JavaFX.
 *
 * Mantiene el conjunto de teclas activas de forma privada y expone la dirección
 * normalizada que consume el bucle de juego. Las acciones puntuales (ESPACIO
 * para minar y E para acoplar) se consumen una sola vez por pulsación.
 */
class ControladorTeclado {

    private val teclasPresionadas: MutableSet<KeyCode> = mutableSetOf()
    private val teclasAccionPresionadas: MutableSet<KeyCode> = mutableSetOf()
    private var minarSolicitado: Boolean = false
    private var acoplarSolicitado: Boolean = false

    private val alPresionar = EventHandler<KeyEvent> { evento ->
        when {
            evento.code == KeyCode.SPACE -> {
                if (teclasAccionPresionadas.add(KeyCode.SPACE)) {
                    minarSolicitado = true
                }
                evento.consume()
            }
            evento.code == KeyCode.E -> {
                if (teclasAccionPresionadas.add(KeyCode.E)) {
                    acoplarSolicitado = true
                }
                evento.consume()
            }
            esTeclaDeControl(evento.code) -> {
                teclasPresionadas.add(evento.code)
                evento.consume()
            }
        }
    }

    private val alLiberar = EventHandler<KeyEvent> { evento ->
        when {
            evento.code == KeyCode.SPACE || evento.code == KeyCode.E -> {
                teclasAccionPresionadas.remove(evento.code)
                evento.consume()
            }
            esTeclaDeControl(evento.code) -> {
                teclasPresionadas.remove(evento.code)
                evento.consume()
            }
        }
    }

    private val alCambiarFoco = ChangeListener<Boolean> { _, _, enfocado ->
        if (!enfocado) {
            limpiarEntrada()
        }
    }

    private val alCambiarVentana = ChangeListener<Window> { _, anterior, nueva ->
        anterior?.focusedProperty()?.removeListener(alCambiarFoco)
        nueva?.focusedProperty()?.addListener(alCambiarFoco)
    }

    /**
     * Registra los manejadores de teclado sobre la escena indicada.
     */
    fun conectar(escena: Scene) {
        escena.addEventHandler(KeyEvent.KEY_PRESSED, alPresionar)
        escena.addEventHandler(KeyEvent.KEY_RELEASED, alLiberar)
        escena.windowProperty().addListener(alCambiarVentana)
        escena.window?.focusedProperty()?.addListener(alCambiarFoco)
    }

    /**
     * Quita los manejadores y limpia el estado de teclas activas.
     */
    fun desconectar(escena: Scene) {
        escena.removeEventHandler(KeyEvent.KEY_PRESSED, alPresionar)
        escena.removeEventHandler(KeyEvent.KEY_RELEASED, alLiberar)
        escena.windowProperty().removeListener(alCambiarVentana)
        escena.window?.focusedProperty()?.removeListener(alCambiarFoco)
        limpiarEntrada()
    }

    /**
     * Dirección horizontal: -1.0 izquierda, 0.0 quieto, 1.0 derecha.
     */
    fun direccionX(): Double = when {
        estaActiva(KeyCode.LEFT, KeyCode.A) && !estaActiva(KeyCode.RIGHT, KeyCode.D) -> -1.0
        estaActiva(KeyCode.RIGHT, KeyCode.D) && !estaActiva(KeyCode.LEFT, KeyCode.A) -> 1.0
        else -> 0.0
    }

    /**
     * Dirección vertical: -1.0 arriba, 0.0 quieto, 1.0 abajo.
     */
    fun direccionY(): Double = when {
        estaActiva(KeyCode.UP, KeyCode.W) && !estaActiva(KeyCode.DOWN, KeyCode.S) -> -1.0
        estaActiva(KeyCode.DOWN, KeyCode.S) && !estaActiva(KeyCode.UP, KeyCode.W) -> 1.0
        else -> 0.0
    }

    /**
     * Consume la solicitud de minado acumulada desde la última consulta.
     */
    fun consumirMinar(): Boolean {
        val solicitado = minarSolicitado
        minarSolicitado = false
        return solicitado
    }

    /**
     * Consume la solicitud de acople acumulada desde la última consulta.
     */
    fun consumirAcoplar(): Boolean {
        val solicitado = acoplarSolicitado
        acoplarSolicitado = false
        return solicitado
    }

    private fun estaActiva(vararg teclas: KeyCode): Boolean =
        teclas.any { teclasPresionadas.contains(it) }

    private fun esTeclaDeControl(tecla: KeyCode): Boolean = tecla in TECLAS_DE_CONTROL

    private fun limpiarEntrada() {
        teclasPresionadas.clear()
        teclasAccionPresionadas.clear()
        minarSolicitado = false
        acoplarSolicitado = false
    }

    companion object {
        private val TECLAS_DE_CONTROL = setOf(
            KeyCode.LEFT,
            KeyCode.RIGHT,
            KeyCode.UP,
            KeyCode.DOWN,
            KeyCode.A,
            KeyCode.D,
            KeyCode.W,
            KeyCode.S
        )
    }
}
