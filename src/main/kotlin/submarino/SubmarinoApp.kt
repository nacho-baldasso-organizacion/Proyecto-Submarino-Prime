package submarino

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.paint.Color
import javafx.stage.Stage

/**
 * Punto de entrada de la interfaz gráfica del submarino.
 *
 * Inicia siempre en el menú principal y compone, bajo demanda, la sesión de
 * juego, la vista, el controlador de teclado y el bucle de animación.
 */
class SubmarinoApp : Application() {

    private var escenario: Stage? = null
    private var bucleJuego: BucleJuego? = null
    private var controladorTeclado: ControladorTeclado? = null
    private var escenaActual: Scene? = null
    private var menuActual: MenuPrincipal? = null
    private var vistaActual: VistaJuego? = null

    override fun start(escenario: Stage) {
        this.escenario = escenario
        escenario.title = TITULO
        escenario.isResizable = false
        mostrarMenu()
        escenario.show()
    }

    override fun stop() {
        detenerJuego()
        escenario = null
    }

    private fun mostrarMenu() {
        detenerJuego()
        val menu = MenuPrincipal(
            anchoEscena = ANCHO_ESCENA,
            altoEscena = ALTO_ESCENA,
            alJugar = { iniciarJuego() }
        )
        val escena = Scene(
            menu.obtenerRaiz(),
            ANCHO_ESCENA,
            ALTO_ESCENA,
            Color.web(COLOR_FONDO)
        )
        menuActual = menu
        escenaActual = escena
        escenario?.scene = escena
    }

    private fun iniciarJuego() {
        detenerJuego()

        val sesion = SesionJuego()
        val vista = VistaJuego(
            anchoEscena = ANCHO_ESCENA,
            altoEscena = ALTO_ESCENA,
            sesion = sesion,
            alReintentar = { iniciarJuego() },
            alVolverAlMenu = { mostrarMenu() }
        )
        val controlador = ControladorTeclado()
        val escena = Scene(
            vista.obtenerRaiz(),
            ANCHO_ESCENA,
            ALTO_ESCENA,
            Color.web(COLOR_FONDO)
        )
        controlador.conectar(escena)

        val bucle = BucleJuego(
            sesion = sesion,
            vista = vista,
            controlador = controlador
        )
        vista.actualizar(sesion.estado())
        bucle.start()

        escenaActual = escena
        controladorTeclado = controlador
        bucleJuego = bucle
        vistaActual = vista
        escenario?.scene = escena
    }

    private fun detenerJuego() {
        bucleJuego?.stop()
        escenaActual?.let { controladorTeclado?.desconectar(it) }
        vistaActual?.detenerAnimaciones()
        menuActual?.detenerAnimaciones()
        vistaActual = null
        menuActual = null
        bucleJuego = null
        controladorTeclado = null
        escenaActual = null
    }

    companion object {
        private const val ANCHO_ESCENA = 1280.0
        private const val ALTO_ESCENA = 720.0
        private const val COLOR_FONDO = "#031926"
        private const val TITULO = "Submarino Prime - Exploración Abisal"

        @JvmStatic
        fun main(args: Array<String>) {
            Application.launch(SubmarinoApp::class.java, *args)
        }
    }
}
