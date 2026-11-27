package submarino

import javafx.beans.value.ChangeListener
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.stage.Window

internal data class MovimientoTecladoState(val teclasPresionadas: MutableSet<KeyCode>)

/**
 * Controla el estado de las teclas de direccion mantenidas (SRP).
 */
class ControladorMovimientoTeclado {

    private val state: MovimientoTecladoState = MovimientoTecladoState(mutableSetOf())

    private val alPresionar = EventHandler<KeyEvent> { evento ->
        if (esTeclaDeControl(evento.code)) {
            state.teclasPresionadas.add(evento.code)
            evento.consume()
        }
    }

    private val alLiberar = EventHandler<KeyEvent> { evento ->
        if (esTeclaDeControl(evento.code)) {
            state.teclasPresionadas.remove(evento.code)
            evento.consume()
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

    fun conectar(escena: Scene) {
        escena.addEventHandler(KeyEvent.KEY_PRESSED, alPresionar)
        escena.addEventHandler(KeyEvent.KEY_RELEASED, alLiberar)
        escena.windowProperty().addListener(alCambiarVentana)
        escena.window?.focusedProperty()?.addListener(alCambiarFoco)
    }

    fun desconectar(escena: Scene) {
        escena.removeEventHandler(KeyEvent.KEY_PRESSED, alPresionar)
        escena.removeEventHandler(KeyEvent.KEY_RELEASED, alLiberar)
        escena.windowProperty().removeListener(alCambiarVentana)
        escena.window?.focusedProperty()?.removeListener(alCambiarFoco)
        limpiarEntrada()
    }

    /**
     * Direccion horizontal: -1.0 izquierda, 0.0 quieto, 1.0 derecha.
     */
    fun direccionX(): Double = direccion(
        negativo = estaActiva(KeyCode.LEFT, KeyCode.A),
        positivo = estaActiva(KeyCode.RIGHT, KeyCode.D)
    )

    /**
     * Direccion vertical: -1.0 arriba, 0.0 quieto, 1.0 abajo.
     */
    fun direccionY(): Double = direccion(
        negativo = estaActiva(KeyCode.UP, KeyCode.W),
        positivo = estaActiva(KeyCode.DOWN, KeyCode.S)
    )

    private fun direccion(negativo: Boolean, positivo: Boolean): Double = if (negativo == positivo) {
        0.0
    } else if (negativo) {
        -1.0
    } else {
        1.0
    }

    private fun estaActiva(vararg teclas: KeyCode): Boolean =
        teclas.any { state.teclasPresionadas.contains(it) }

    private fun esTeclaDeControl(tecla: KeyCode): Boolean = tecla in TECLAS_DE_CONTROL

    private fun limpiarEntrada() {
        state.teclasPresionadas.clear()
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

/**
 * Controla las acciones puntuales de minar y acoplar (SRP).
 */
internal data class AccionTecladoState(
    val teclasAccionPresionadas: MutableSet<KeyCode>,
    var minarSolicitado: Boolean,
    var acoplarSolicitado: Boolean
)

/**
 * Controla las acciones puntuales de minar y acoplar (SRP).
 */
class ControladorAccionesTeclado {

    private val state: AccionTecladoState = AccionTecladoState(mutableSetOf(), false, false)

    private val accionesPorTecla: Map<KeyCode, () -> Unit> = mapOf(
        KeyCode.SPACE to {
            if (state.teclasAccionPresionadas.add(KeyCode.SPACE)) {
                state.minarSolicitado = true
            }
        },
        KeyCode.E to {
            if (state.teclasAccionPresionadas.add(KeyCode.E)) {
                state.acoplarSolicitado = true
            }
        }
    )

    private val alPresionar = EventHandler<KeyEvent> { evento ->
        val accion = accionesPorTecla[evento.code]
        if (accion != null) {
            accion()
            evento.consume()
        }
    }

    private val alLiberar = EventHandler<KeyEvent> { evento ->
        if (evento.code == KeyCode.SPACE || evento.code == KeyCode.E) {
            state.teclasAccionPresionadas.remove(evento.code)
            evento.consume()
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

    fun conectar(escena: Scene) {
        escena.addEventHandler(KeyEvent.KEY_PRESSED, alPresionar)
        escena.addEventHandler(KeyEvent.KEY_RELEASED, alLiberar)
        escena.windowProperty().addListener(alCambiarVentana)
        escena.window?.focusedProperty()?.addListener(alCambiarFoco)
    }

    fun desconectar(escena: Scene) {
        escena.removeEventHandler(KeyEvent.KEY_PRESSED, alPresionar)
        escena.removeEventHandler(KeyEvent.KEY_RELEASED, alLiberar)
        escena.windowProperty().removeListener(alCambiarVentana)
        escena.window?.focusedProperty()?.removeListener(alCambiarFoco)
        limpiarEntrada()
    }

    /**
     * Consume la solicitud de minado acumulada desde la ultima consulta.
     */
    fun consumirMinar(): Boolean {
        val solicitado = state.minarSolicitado
        state.minarSolicitado = false
        return solicitado
    }

    /**
     * Consume la solicitud de acople acumulada desde la ultima consulta.
     */
    fun consumirAcoplar(): Boolean {
        val solicitado = state.acoplarSolicitado
        state.acoplarSolicitado = false
        return solicitado
    }

    private fun limpiarEntrada() {
        state.teclasAccionPresionadas.clear()
        state.minarSolicitado = false
        state.acoplarSolicitado = false
    }
}

/**
 * Fachada de entrada por teclado: compone el control de movimiento y el de
 * acciones puntuales para que el bucle de juego consulte una unica API (SRP).
 */
class ControladorTeclado(
    private val movimiento: ControladorMovimientoTeclado =
        FabricaDominio.crearControladorMovimientoTeclado(),
    private val acciones: ControladorAccionesTeclado =
        FabricaDominio.crearControladorAccionesTeclado()
) {

    fun conectar(escena: Scene) {
        movimiento.conectar(escena)
        acciones.conectar(escena)
    }

    fun desconectar(escena: Scene) {
        movimiento.desconectar(escena)
        acciones.desconectar(escena)
    }

    fun direccionX(): Double = movimiento.direccionX()

    fun direccionY(): Double = movimiento.direccionY()

    fun consumirMinar(): Boolean = acciones.consumirMinar()

    fun consumirAcoplar(): Boolean = acciones.consumirAcoplar()
}