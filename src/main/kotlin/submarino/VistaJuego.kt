package submarino

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Parent
import javafx.scene.layout.StackPane

/**
 * Estado interno de la vista del juego, que concentra los campos
 * mutable compartidos por varios métodos para mejorar la cohesión (LCOM).
 */
internal class EstadoVistaJuego {
    var gameOverMostrado: Boolean = false
}

object VistaJuegoBuilderDelegate {

    fun crearFondo(anchoEscena: Double, altoEscena: Double, anchoMundo: Double, altoMundo: Double, semillaFondo: Long, profundidadRayos: Double): FondoAbisal =
        FabricaVisual.crearFondoAbisal(anchoMundo + anchoEscena, altoMundo + altoEscena, semillaFondo, profundidadRayos * VistaJuegoBuilderDelegate.ESCALA_MUNDO)

    fun crearTerreno(sesion: SesionJuego, anchoMundo: Double, altoMundo: Double, escala: Double, semillaTerreno: Long): TerrenoRocosoVisual =
        FabricaVisual.crearTerrenoRocosoVisual(sesion.terreno(), anchoMundo, altoMundo, escala, semillaTerreno)

    fun crearPeces(anchoEscena: Double, altoEscena: Double, semillaPeces: Long): PecesMarinos =
        FabricaVisual.crearPecesMarinos(anchoEscena, altoEscena, semillaPeces)

    fun crearSubmarino(): SubmarinoVectorial = FabricaVisual.crearSubmarinoVectorial()

    fun crearEstacion(): EstacionVectorial = FabricaVisual.crearEstacionVectorial()

    fun crearParticulas(anchoEscena: Double, altoEscena: Double, semillaParticulas: Long): ParticulasMarinas =
        FabricaVisual.crearParticulasMarinas(anchoEscena, altoEscena, semillaParticulas)

    fun crearExplosion(): ExplosionSubmarino = FabricaVisual.crearExplosionSubmarino()

    fun crearGameOver(alReintentar: () -> Unit, alVolverAlMenu: () -> Unit): GameOverModal =
        FabricaVisual.crearGameOverModal(alReintentar, alVolverAlMenu)

    fun crearVeloAbisal(anchoEscena: Double, altoEscena: Double): VeloAbisal =
        FabricaVisual.crearVeloAbisal(anchoEscena, altoEscena)

    fun crearVeloPresion(anchoEscena: Double, altoEscena: Double): VeloPresion =
        FabricaVisual.crearVeloPresion(anchoEscena, altoEscena)

    fun crearCamara(anchoMundo: Double, altoMundo: Double, anchoEscena: Double, altoEscena: Double, escala: Double): CamaraJuego =
        FabricaVisual.crearCamaraJuego(anchoMundo, altoMundo, anchoEscena, altoEscena, escala)

    fun crearAvisos(): AvisosJuego = FabricaVisual.crearAvisosJuego()

    fun crearTienda(sesion: SesionJuego): TiendaModal = FabricaVisual.crearTiendaModal(sesion)

    const val ESCALA_MUNDO = 0.55
    const val ESCALA_ESTACION = 0.9
    const val ESCALA_NODO = 0.9
    const val SEMILLA_FONDO = 21L
    const val SEMILLA_TERRENO = 53L
    const val SEMILLA_PARTICULAS = 47L
    const val SEMILLA_PECES = 91L
    const val PROFUNDIDAD_RAYOS = 300.0
    const val FACTOR_LEJANO = 0.18
    const val FACTOR_MEDIO = 0.45
    const val FACTOR_ENTIDADES = 1.0
}

class VistaJuego(
    private val anchoEscena: Double,
    private val altoEscena: Double,
    private val sesion: SesionJuego,
    private val hud: HUDJuego = FabricaVisual.crearHUDJuego(),
    private val alReintentar: () -> Unit = {},
    private val alVolverAlMenu: () -> Unit = {}

) {

    private val visualesNodos: Map<NodoMineral, NodoMineralVectorial> =
        sesion.nodos().associateWith { FabricaVisual.crearNodoMineralVectorial(it) }

    private val delegado = VistaJuegoBuilderDelegate

    private val escala: Double = VistaJuegoBuilderDelegate.ESCALA_MUNDO
    private val anchoMundo: Double = SesionJuego.ANCHO_MAPA * escala
    private val altoMundo: Double = SesionJuego.ALTO_MAPA * escala

    private val fondo: FondoAbisal = VistaJuegoBuilderDelegate.crearFondo(
        anchoEscena, altoEscena, anchoMundo, altoMundo,
        VistaJuegoBuilderDelegate.SEMILLA_FONDO, VistaJuegoBuilderDelegate.PROFUNDIDAD_RAYOS
    )
    private val capaEntidades: Group = Group()
    private val terreno: TerrenoRocosoVisual = VistaJuegoBuilderDelegate.crearTerreno(sesion, anchoMundo, altoMundo, escala, VistaJuegoBuilderDelegate.SEMILLA_TERRENO)
    private val peces: PecesMarinos = VistaJuegoBuilderDelegate.crearPeces(anchoEscena, altoEscena, VistaJuegoBuilderDelegate.SEMILLA_PECES)
    private val submarino: SubmarinoVectorial = VistaJuegoBuilderDelegate.crearSubmarino()
    private val estacion: EstacionVectorial = VistaJuegoBuilderDelegate.crearEstacion()
    private val particulas: ParticulasMarinas = VistaJuegoBuilderDelegate.crearParticulas(anchoEscena, altoEscena, VistaJuegoBuilderDelegate.SEMILLA_PARTICULAS)
    private val explosion: ExplosionSubmarino = VistaJuegoBuilderDelegate.crearExplosion()
    private val gameOver: GameOverModal = VistaJuegoBuilderDelegate.crearGameOver(alReintentar, alVolverAlMenu)
    private var estadoVista: EstadoVistaJuego = EstadoVistaJuego()
    private val veloAbisal: VeloAbisal = VistaJuegoBuilderDelegate.crearVeloAbisal(anchoEscena, altoEscena)
    private val veloPresion: VeloPresion = VistaJuegoBuilderDelegate.crearVeloPresion(anchoEscena, altoEscena)
    private val camara: CamaraJuego = VistaJuegoBuilderDelegate.crearCamara(anchoMundo, altoMundo, anchoEscena, altoEscena, escala)
    private val avisos: AvisosJuego = VistaJuegoBuilderDelegate.crearAvisos()
    private val tienda: TiendaModal = VistaJuegoBuilderDelegate.crearTienda(sesion)
    private val raiz: StackPane = StackPane()

    init {
        raiz.style = "-fx-background-color: #01070B;"
        estacion.nodo.layoutX = SesionJuego.ESTACION_X * escala
        estacion.nodo.layoutY = 0.0
        estacion.nodo.scaleX = VistaJuegoBuilderDelegate.ESCALA_ESTACION
        estacion.nodo.scaleY = VistaJuegoBuilderDelegate.ESCALA_ESTACION

        visualesNodos.forEach { (modelo, visual) ->
            visual.nodo.layoutX = modelo.x * escala
            visual.nodo.layoutY = modelo.y * escala
            visual.nodo.scaleX = VistaJuegoBuilderDelegate.ESCALA_NODO
            visual.nodo.scaleY = VistaJuegoBuilderDelegate.ESCALA_NODO
        }

        capaEntidades.children.add(terreno.nodo)
        capaEntidades.children.add(estacion.nodo)
        visualesNodos.values.forEach { capaEntidades.children.add(it.nodo) }
        capaEntidades.children.add(submarino.nodo)
        capaEntidades.children.add(explosion.nodo)

        raiz.children.addAll(
            fondo.capaLejana,
            fondo.capaMedia,
            peces.nodo,
            capaEntidades,
            veloAbisal.nodo,
            veloPresion.nodo,
            particulas.nodo,
            hud.obtenerNodo(),
            avisos.panel,
            tienda.obtenerNodo(),
            gameOver.obtenerNodo()
        )

        StackPane.setAlignment(fondo.capaLejana, Pos.TOP_LEFT)
        StackPane.setAlignment(fondo.capaMedia, Pos.TOP_LEFT)
        StackPane.setAlignment(peces.nodo, Pos.TOP_LEFT)
        StackPane.setAlignment(capaEntidades, Pos.TOP_LEFT)
        StackPane.setAlignment(veloAbisal.nodo, Pos.TOP_LEFT)
        StackPane.setAlignment(veloPresion.nodo, Pos.TOP_LEFT)
        StackPane.setAlignment(particulas.nodo, Pos.TOP_LEFT)
        StackPane.setAlignment(hud.obtenerNodo(), Pos.TOP_CENTER)
        StackPane.setMargin(hud.obtenerNodo(), Insets(14.0, 14.0, 0.0, 14.0))
        StackPane.setAlignment(avisos.panel, Pos.BOTTOM_CENTER)
        StackPane.setMargin(avisos.panel, Insets(0.0, 20.0, 20.0, 20.0))

        particulas.iniciar()
        peces.iniciar()
    }

    private val parallaxLejano: CapaParallaxVisual = FabricaVisual.crearCapaParallaxVisual(
        contenedor = fondo.capaLejana,
        capa = FabricaVisual.crearCapaParallax(VistaJuegoBuilderDelegate.FACTOR_LEJANO),
        origenX = 0.0,
        origenY = 0.0
    )
    private val parallaxMedio: CapaParallaxVisual = FabricaVisual.crearCapaParallaxVisual(
        contenedor = fondo.capaMedia,
        capa = FabricaVisual.crearCapaParallax(VistaJuegoBuilderDelegate.FACTOR_MEDIO),
        origenX = 0.0,
        origenY = 0.0
    )
    private val parallaxEntidades: CapaParallaxVisual = FabricaVisual.crearCapaParallaxVisual(
        contenedor = capaEntidades,
        capa = FabricaVisual.crearCapaParallax(VistaJuegoBuilderDelegate.FACTOR_ENTIDADES),
        origenX = 0.0,
        origenY = 0.0
    )

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
     * Refresca cámara, parallax, entidades, velos, HUD y avisos.
     */
    fun actualizar(estado: EstadoJuego) {
        val camaraX = camara.x(estado.posicionX)
        val camaraY = camara.y(estado.profundidad)

        parallaxLejano.actualizar(camaraX, camaraY)
        parallaxMedio.actualizar(camaraX, camaraY)
        parallaxEntidades.actualizar(camaraX, camaraY)

        submarino.nodo.layoutX = estado.posicionX * escala
        submarino.nodo.layoutY = estado.profundidad * escala
        submarino.actualizarDireccion(estado.direccionHorizontal)

        visualesNodos.values.forEach { it.actualizar() }

        veloAbisal.actualizar(estado.profundidad)
        veloPresion.actualizar(estado.presionActual, estado.presionMaxima)

        hud.actualizar(estado)
        avisos.actualizar(estado)

        if (estado.destruido && !estadoVista.gameOverMostrado) {
            estadoVista.gameOverMostrado = true
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

    companion object
}