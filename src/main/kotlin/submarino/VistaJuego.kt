package submarino

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.RadialGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Rectangle

/**
 * Vista principal de la partida.
 *
 * Compone el fondo parallax en ambos ejes, la capa de entidades, el velo abisal,
 * el HUD fijo, los avisos contextuales y el modal de la tienda. La vista solo
 * consume [EstadoJuego]; no contiene lógica económica ni de navegación.
 */
class VistaJuego(
    private val anchoEscena: Double,
    private val altoEscena: Double,
    private val sesion: SesionJuego,
    private val hud: HUDJuego = HUDJuego(),
    private val alReintentar: () -> Unit = {},
    private val alVolverAlMenu: () -> Unit = {}
) {

    private val escala: Double = ESCALA_MUNDO
    private val anchoMundo: Double = SesionJuego.ANCHO_MAPA * escala
    private val altoMundo: Double = SesionJuego.ALTO_MAPA * escala

    private val fondo: FondoAbisal = FondoAbisal(
        anchoMundo + anchoEscena,
        altoMundo + altoEscena,
        SEMILLA_FONDO,
        PROFUNDIDAD_RAYOS * escala
    )
    private val capaEntidades: Group = Group()
    private val terreno: TerrenoRocosoVisual = TerrenoRocosoVisual(
        sesion.terreno(),
        anchoMundo,
        altoMundo,
        escala,
        SEMILLA_TERRENO
    )
    private val peces: PecesMarinos = PecesMarinos(
        anchoEscena,
        altoEscena,
        SEMILLA_PECES
    )
    private val submarino: SubmarinoVectorial = SubmarinoVectorial()
    private val estacion: EstacionVectorial = EstacionVectorial()
    private val particulas: ParticulasMarinas = ParticulasMarinas(
        anchoEscena,
        altoEscena,
        SEMILLA_PARTICULAS
    )
    private val explosion: ExplosionSubmarino = ExplosionSubmarino()
    private val gameOver: GameOverModal = GameOverModal(alReintentar, alVolverAlMenu)
    private var gameOverMostrado: Boolean = false
    private val visualesNodos: Map<NodoMineral, NodoMineralVectorial> =
        sesion.nodos().associateWith { NodoMineralVectorial(it) }
    private val overlayAbisal: Rectangle = Rectangle(anchoEscena, altoEscena).apply {
        fill = Color.BLACK
        opacity = OPACIDAD_MINIMA
        isMouseTransparent = true
    }
    private val overlayPresion: Rectangle = Rectangle(anchoEscena, altoEscena).apply {
        fill = RadialGradient(
            0.5, 0.5, 0.5, 0.5, 0.78,
            true,
            CycleMethod.NO_CYCLE,
            Stop(0.0, Color.web("#FF3A00", 0.0)),
            Stop(0.62, Color.web("#FF3A00", 0.08)),
            Stop(0.86, Color.web("#E01B00", 0.30)),
            Stop(1.0, Color.web("#9E0000", 0.62))
        )
        opacity = 0.0
        isMouseTransparent = true
    }
    private val tienda: TiendaModal = TiendaModal(sesion)
    private val notificacion: Label = crearNotificacion()
    private val mensaje: Label = crearMensaje()
    private val raiz: StackPane = StackPane()

    private val parallaxLejano: CapaParallaxVisual = CapaParallaxVisual(
        contenedor = fondo.capaLejana,
        capa = CapaParallax(FACTOR_LEJANO),
        origenX = 0.0,
        origenY = 0.0
    )
    private val parallaxMedio: CapaParallaxVisual = CapaParallaxVisual(
        contenedor = fondo.capaMedia,
        capa = CapaParallax(FACTOR_MEDIO),
        origenX = 0.0,
        origenY = 0.0
    )
    private val parallaxEntidades: CapaParallaxVisual = CapaParallaxVisual(
        contenedor = capaEntidades,
        capa = CapaParallax(FACTOR_ENTIDADES),
        origenX = 0.0,
        origenY = 0.0
    )

    init {
        raiz.style = "-fx-background-color: #01070B;"
        estacion.nodo.layoutX = SesionJuego.ESTACION_X * escala
        estacion.nodo.layoutY = 0.0
        estacion.nodo.scaleX = ESCALA_ESTACION
        estacion.nodo.scaleY = ESCALA_ESTACION

        visualesNodos.forEach { (modelo, visual) ->
            visual.nodo.layoutX = modelo.x * escala
            visual.nodo.layoutY = modelo.y * escala
            visual.nodo.scaleX = ESCALA_NODO
            visual.nodo.scaleY = ESCALA_NODO
        }

        capaEntidades.children.add(terreno.nodo)
        capaEntidades.children.add(estacion.nodo)
        visualesNodos.values.forEach { capaEntidades.children.add(it.nodo) }
        capaEntidades.children.add(submarino.nodo)
        capaEntidades.children.add(explosion.nodo)

        val panelMensajes = VBox(6.0).apply {
            alignment = Pos.CENTER
            isMouseTransparent = true
            children.addAll(notificacion, mensaje)
        }

        raiz.children.addAll(
            fondo.capaLejana,
            fondo.capaMedia,
            peces.nodo,
            capaEntidades,
            overlayAbisal,
            overlayPresion,
            particulas.nodo,
            hud.obtenerNodo(),
            panelMensajes,
            tienda.obtenerNodo(),
            gameOver.obtenerNodo()
        )

        StackPane.setAlignment(fondo.capaLejana, Pos.TOP_LEFT)
        StackPane.setAlignment(fondo.capaMedia, Pos.TOP_LEFT)
        StackPane.setAlignment(peces.nodo, Pos.TOP_LEFT)
        StackPane.setAlignment(capaEntidades, Pos.TOP_LEFT)
        StackPane.setAlignment(overlayAbisal, Pos.TOP_LEFT)
        StackPane.setAlignment(overlayPresion, Pos.TOP_LEFT)
        StackPane.setAlignment(particulas.nodo, Pos.TOP_LEFT)
        StackPane.setAlignment(hud.obtenerNodo(), Pos.TOP_CENTER)
        StackPane.setMargin(hud.obtenerNodo(), Insets(14.0, 14.0, 0.0, 14.0))
        StackPane.setAlignment(panelMensajes, Pos.BOTTOM_CENTER)
        StackPane.setMargin(panelMensajes, Insets(0.0, 20.0, 20.0, 20.0))

        particulas.iniciar()
        peces.iniciar()
    }

    fun obtenerRaiz(): Parent = raiz

    /**
     * Detiene las animaciones de las entidades al abandonar la partida.
     */
    fun detenerAnimaciones() {
        submarino.detenerAnimaciones()
        estacion.detenerAnimaciones()
        particulas.detener()
        peces.detener()
        explosion.detenerAnimaciones()
    }

    /**
     * Refresca cámara, parallax, entidades, velo abisal, HUD y avisos.
     */
    fun actualizar(estado: EstadoJuego) {
        val camaraX = calcularCamaraX(estado.posicionX)
        val camaraY = calcularCamaraY(estado.profundidad)

        parallaxLejano.actualizar(camaraX, camaraY)
        parallaxMedio.actualizar(camaraX, camaraY)
        parallaxEntidades.actualizar(camaraX, camaraY)

        submarino.nodo.layoutX = estado.posicionX * escala
        submarino.nodo.layoutY = estado.profundidad * escala
        submarino.actualizarDireccion(estado.direccionHorizontal)

        visualesNodos.values.forEach { it.actualizar() }

        overlayAbisal.opacity = (
            OPACIDAD_MINIMA +
                (OPACIDAD_MAXIMA - OPACIDAD_MINIMA) *
                (estado.profundidad / SesionJuego.ALTO_MAPA)
            ).coerceIn(OPACIDAD_MINIMA, OPACIDAD_MAXIMA)

        val presionMaxima = estado.presionMaxima.coerceAtLeast(1.0)
        val ratioPresion = (estado.presionActual / presionMaxima).coerceAtLeast(0.0)
        overlayPresion.opacity = (
            (ratioPresion - UMBRAL_PRESION_AVISO) / (1.0 - UMBRAL_PRESION_AVISO)
            ).coerceIn(0.0, 1.0) * OPACIDAD_MAXIMA_PRESION

        hud.actualizar(estado)
        actualizarAvisos(estado)

        if (estado.destruido && !gameOverMostrado) {
            gameOverMostrado = true
            explosion.detonar(estado.posicionX * escala, estado.profundidad * escala)
            gameOver.abrir(estado.causaDestruccion)
        }
    }

    /**
     * Abre la tienda modal sobre la partida.
     */
    fun abrirTienda() {
        if (!gameOver.estaAbierto()) {
            tienda.abrir()
        }
    }

    fun estaTiendaAbierta(): Boolean = tienda.estaAbierto()

    private fun actualizarAvisos(estado: EstadoJuego) {
        val presionCritica = estado.presionActual > estado.presionMaxima
        val oxigenoBajo = estado.oxigeno <= estado.capacidadOxigeno * UMBRAL_OXIGENO_BAJO
        val aviso = when {
            estado.destruido -> ""
            presionCritica -> "¡PRESIÓN CRÍTICA! Asciende o el casco colapsará"
            oxigenoBajo -> "¡OXÍGENO BAJO! Regresa a la estación para recargar"
            estado.acoplado -> "Acoplado · pulsa DESACOPLAR en la tienda para continuar"
            estado.enZonaAcople -> "Presiona E para acoplar"
            else -> ""
        }
        notificacion.text = aviso
        notificacion.style = if (presionCritica || oxigenoBajo) {
            ESTILO_NOTIFICACION_CRITICA
        } else {
            ESTILO_NOTIFICACION
        }
        notificacion.isVisible = aviso.isNotEmpty()
        notificacion.isManaged = aviso.isNotEmpty()

        if (mensaje.text != estado.mensaje) {
            mensaje.text = estado.mensaje
        }
    }

    private fun calcularCamaraX(posicionX: Double): Double {
        val maximo = (anchoMundo - anchoEscena).coerceAtLeast(0.0)
        return (posicionX * escala - anchoEscena / 2.0).coerceIn(0.0, maximo)
    }

    private fun calcularCamaraY(profundidad: Double): Double {
        val maximo = (altoMundo - altoEscena).coerceAtLeast(0.0)
        return (profundidad * escala - altoEscena / 2.0)
            .coerceIn(CAMARA_Y_MINIMA, maximo)
    }

    private fun crearNotificacion(): Label = Label("").apply {
        style = ESTILO_NOTIFICACION
        isMouseTransparent = true
        isVisible = false
        isManaged = false
    }

    private fun crearMensaje(): Label = Label("").apply {
        style = ESTILO_MENSAJE
        isMouseTransparent = true
        isWrapText = true
        maxWidth = 760.0
        alignment = Pos.CENTER
    }

    companion object {
        private const val ESCALA_MUNDO = 0.55
        private const val ESCALA_ESTACION = 0.9
        private const val ESCALA_NODO = 0.9
        private const val SEMILLA_FONDO = 21L
        private const val SEMILLA_TERRENO = 53L
        private const val SEMILLA_PARTICULAS = 47L
        private const val SEMILLA_PECES = 91L
        private const val PROFUNDIDAD_RAYOS = 300.0
        private const val FACTOR_LEJANO = 0.18
        private const val FACTOR_MEDIO = 0.45
        private const val FACTOR_ENTIDADES = 1.0
        private const val CAMARA_Y_MINIMA = -180.0
        private const val OPACIDAD_MINIMA = 0.05
        private const val OPACIDAD_MAXIMA = 0.68
        private const val UMBRAL_PRESION_AVISO = 0.74
        private const val OPACIDAD_MAXIMA_PRESION = 0.42
        private const val UMBRAL_OXIGENO_BAJO = 0.18

        private const val ESTILO_NOTIFICACION =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #031926;" +
                "-fx-background-color: #FFD75A;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 8 18 8 18;" +
                "-fx-effect: dropshadow(gaussian, rgba(255, 215, 90, 0.55), 18, 0.3, 0, 4);"

        private const val ESTILO_MENSAJE =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 11px;" +
                "-fx-text-fill: #B7D8E1;" +
                "-fx-background-color: rgba(2, 20, 28, 0.72);" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 6 14 6 14;"

        private const val ESTILO_NOTIFICACION_CRITICA =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #FFF5F0;" +
                "-fx-background-color: #D6290B;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 8 18 8 18;" +
                "-fx-effect: dropshadow(gaussian, rgba(255, 74, 20, 0.85), 24, 0.4, 0, 4);"
    }
}
