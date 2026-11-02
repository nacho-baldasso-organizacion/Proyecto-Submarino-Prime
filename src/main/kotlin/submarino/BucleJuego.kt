package submarino

import javafx.animation.AnimationTimer

/**
 * Bucle de juego basado en [AnimationTimer].
 *
 * Calcula el delta de tiempo entre pulsos, procesa las acciones puntuales de
 * minado y acople, actualiza el modelo de partida y refresca la vista.
 */
class BucleJuego(
    private val sesion: SesionJuego,
    private val vista: VistaJuego,
    private val controlador: ControladorTeclado
) : AnimationTimer() {

    private var ultimoInstante: Long = 0L

    override fun handle(ahora: Long) {
        if (ultimoInstante == 0L) {
            ultimoInstante = ahora
            vista.actualizar(sesion.estado())
            return
        }

        val deltaSegundos = ((ahora - ultimoInstante) / NANOSEGUNDOS_POR_SEGUNDO)
            .coerceIn(0.0, DELTA_MAXIMO_SEGUNDOS)
        ultimoInstante = ahora

        if (sesion.estaDestruida()) {
            vista.actualizar(sesion.estado())
            return
        }

        procesarAcciones()
        sesion.actualizar(
            deltaSegundos = deltaSegundos,
            direccionX = controlador.direccionX(),
            direccionY = controlador.direccionY()
        )
        vista.actualizar(sesion.estado())
    }

    private fun procesarAcciones() {
        if (controlador.consumirAcoplar()) {
            if (sesion.acoplar()) {
                vista.abrirTienda()
            }
        }
        if (controlador.consumirMinar()) {
            sesion.minar()
        }
    }

    companion object {
        private const val NANOSEGUNDOS_POR_SEGUNDO = 1_000_000_000.0
        private const val DELTA_MAXIMO_SEGUNDOS = 0.05
    }
}
