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
        val menu = FabricaVisual.crearMenuPrincipal(
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

        val sesion = crearSesion()
        val vista = crearVista(sesion)
        val controlador = crearControlador()
        val escena = crearEscena(vista)
        controlador.conectar(escena)

        val bucle = crearBucle(sesion, vista, controlador)
        vista.actualizar(sesion.estado())
        bucle.start()

        actualizarReferencias(bucle, controlador, vista, escena)
    }

    private fun crearSesion(): SesionJuego = FabricaVisual.crearSesionJuego()

    private fun crearVista(sesion: SesionJuego): VistaJuego = FabricaVisual.crearVistaJuego(
        anchoEscena = ANCHO_ESCENA,
        altoEscena = ALTO_ESCENA,
        sesion = sesion,
        alReintentar = { iniciarJuego() },
        alVolverAlMenu = { mostrarMenu() }
    )

    private fun crearControlador(): ControladorTeclado = FabricaVisual.crearControladorTeclado()

    private fun crearEscena(vista: VistaJuego): Scene = Scene(
        vista.obtenerRaiz(),
        ANCHO_ESCENA,
        ALTO_ESCENA,
        Color.web(COLOR_FONDO)
    )

    private fun crearBucle(sesion: SesionJuego, vista: VistaJuego, controlador: ControladorTeclado): BucleJuego = FabricaVisual.crearBucleJuego(
        sesion = sesion,
        vista = vista,
        controlador = controlador
    )

    private fun actualizarReferencias(
        bucle: BucleJuego,
        controlador: ControladorTeclado,
        vista: VistaJuego,
        escena: Scene
    ) {
        vistaActual = vista
        controladorTeclado = controlador
        bucleJuego = bucle
        escenaActual = escena
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