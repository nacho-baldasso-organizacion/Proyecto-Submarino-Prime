package submarino

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Parent
import javafx.scene.layout.StackPane

/**
 * Vista principal de la partida.
 *
 * Compone el fondo parallax en ambos ejes, la capa de entidades, los velos,
 * el HUD fijo, los avisos contextuales y el modal de la tienda. La vista solo
 * consume [EstadoJuego]; no contiene lógica económica ni de navegación.
 *
 * Responsabilidad única (SRP): orquestar la escena; cada efecto se delega en
 * [VeloAbisal], [VeloPresion], [CamaraJuego] y [AvisosJuego].
 */
class VistaJuego(
    private val anchoEscena: Double,
    private val altoEscena: Double,
    private val sesion: SesionJuego,
    private val hud: HUDJuego = FabricaVisual.crearHUDJuego(),
    private val alReintentar: () -> Unit = {},
    private val alVolverAlMenu: () -> Unit = {}
) {

    private val escala: Double = ESCALA_MUNDO
    private val anchoMundo: Double = SesionJuego.ANCHO_MAPA * escala
    private val altoMundo: Double = SesionJuego.ALTO_MAPA * escala

    private val fondo: FondoAbisal = FabricaVisual.crearFondoAbisal(
        anchoMundo + anchoEscena,
        altoMundo + altoEscena,
        SEMILLA_FONDO,
        PROFUNDIDAD_RAYOS * escala
    )
    private val capaEntidades: Group = Group()
    private val terreno: TerrenoRocosoVisual = FabricaVisual.crearTerrenoRocosoVisual(
        sesion.terreno(),
        anchoMundo,
        altoMundo,
        escala,
        SEMILLA_TERRENO
    )
    private val peces: PecesMarinos = FabricaVisual.crearPecesMarinos(
        anchoEscena,
        altoEscena,
        SEMILLA_PECES
    )
    private val submarino: SubmarinoVectorial = FabricaVisual.crearSubmarinoVectorial()
    private val estacion: EstacionVectorial = FabricaVisual.crearEstacionVectorial()
    private val particulas: ParticulasMarinas = FabricaVisual.crearParticulasMarinas(
        anchoEscena,
        altoEscena,
        SEMILLA_PARTICULAS
    )
    private val explosion: ExplosionSubmarino = FabricaVisual.crearExplosionSubmarino()
    private val gameOver: GameOverModal = FabricaVisual.crearGameOverModal(alReintentar, alVolverAlMenu)
    private var gameOverMostrado: Boolean = false
    private val visualesNodos: Map<NodoMineral, NodoMineralVectorial> =
        sesion.nodos().associateWith { FabricaVisual.crearNodoMineralVectorial(it) }
    private val veloAbisal: VeloAbisal = FabricaVisual.crearVeloAbisal(anchoEscena, altoEscena)
    private val veloPresion: VeloPresion = FabricaVisual.crearVeloPresion(anchoEscena, altoEscena)
    private val camara: CamaraJuego = FabricaVisual.crearCamaraJuego(
        anchoMundo,
        altoMundo,
        anchoEscena,
        altoEscena,
        escala
    )
    private val avisos: AvisosJuego = FabricaVisual.crearAvisosJuego()
    private val tienda: TiendaModal = FabricaVisual.crearTiendaModal(sesion)
    private val raiz: StackPane = StackPane()

    private val parallaxLejano: CapaParallaxVisual = FabricaVisual.crearCapaParallaxVisual(
        contenedor = fondo.capaLejana,
        capa = FabricaVisual.crearCapaParallax(FACTOR_LEJANO),
        origenX = 0.0,
        origenY = 0.0
    )
    private val parallaxMedio: CapaParallaxVisual = FabricaVisual.crearCapaParallaxVisual(
        contenedor = fondo.capaMedia,
        capa = FabricaVisual.crearCapaParallax(FACTOR_MEDIO),
        origenX = 0.0,
        origenY = 0.0
    )
    private val parallaxEntidades: CapaParallaxVisual = FabricaVisual.crearCapaParallaxVisual(
        contenedor = capaEntidades,
        capa = FabricaVisual.crearCapaParallax(FACTOR_ENTIDADES),
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
    }
}
