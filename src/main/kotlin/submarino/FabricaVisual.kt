package submarino

import javafx.animation.Animation
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Punto único de instanciación de los componentes visuales del juego.
 */
object FabricaVisual {

    fun crearAvisosJuego(): AvisosJuego = FabricaVisualCoreDelegate.crearAvisosJuego()

    fun crearCamaraJuego(
        anchoMundo: Double,
        altoMundo: Double,
        anchoEscena: Double,
        altoEscena: Double,
        escala: Double
    ): CamaraJuego = FabricaVisualSupportDelegate.crearCamaraJuego(
        anchoMundo = anchoMundo,
        altoMundo = altoMundo,
        anchoEscena = anchoEscena,
        altoEscena = altoEscena,
        escala = escala
    )

    fun crearCapaParallax(factorVelocidad: Double): CapaParallax = CapaParallax(factorVelocidad)

    fun crearCapaParallaxVisual(
        contenedor: Node,
        capa: CapaParallax,
        origenX: Double,
        origenY: Double
    ): CapaParallaxVisual = CapaParallaxVisual(contenedor, capa, origenX, origenY)

    fun crearEstacionVectorial(): EstacionVectorial = FabricaVisualCoreDelegate.crearEstacionVectorial()

    fun crearSubmarinoVectorial(): SubmarinoVectorial = FabricaVisualCoreDelegate.crearSubmarinoVectorial()

    fun crearExplosionSubmarino(): ExplosionSubmarino = FabricaVisualCoreDelegate.crearExplosionSubmarino()

    fun crearFondoAbisal(
        anchoMundo: Double,
        altoMundo: Double,
        semilla: Long = 7L,
        profundidadRayos: Double = altoMundo * 0.35
    ): FondoAbisal = FabricaVisualCoreDelegate.crearFondoAbisal(
        anchoMundo = anchoMundo,
        altoMundo = altoMundo,
        semilla = semilla,
        profundidadRayos = profundidadRayos
    )

    fun crearGameOverModal(
        alReintentar: () -> Unit,
        alVolverAlMenu: () -> Unit
    ): GameOverModal = FabricaVisualSupportDelegate.crearGameOverModal(
        alReintentar = alReintentar,
        alVolverAlMenu = alVolverAlMenu
    )

    fun crearHUDJuego(): HUDJuego = FabricaVisualCoreDelegate.crearHUDJuego()

    fun crearNodoMineralVectorial(modelo: NodoMineral): NodoMineralVectorial =
        FabricaVisualCoreDelegate.crearNodoMineralVectorial(modelo = modelo)

    fun crearParticulasMarinas(
        ancho: Double,
        alto: Double,
        semilla: Long = 33L
    ): ParticulasMarinas = ParticulasMarinas(ancho, alto, semilla)

    fun crearPecesMarinos(
        ancho: Double,
        alto: Double,
        semilla: Long = 91L,
        cantidad: Int = 14
    ): PecesMarinos = PecesMarinos(ancho, alto, semilla, cantidad)

    fun crearTerrenoRocosoVisual(
        terreno: TerrenoSubmarino,
        anchoMundo: Double,
        altoMundo: Double,
        escala: Double,
        semilla: Long = 53L
    ): TerrenoRocosoVisual = TerrenoRocosoVisual(terreno, anchoMundo, altoMundo, escala, semilla)

    fun crearTiendaModal(sesion: SesionJuego): TiendaModal = FabricaVisualSupportDelegate.crearTiendaModal(sesion = sesion)

    fun crearVeloAbisal(anchoEscena: Double, altoEscena: Double): VeloAbisal =
        FabricaVisualSupportDelegate.crearVeloAbisal(anchoEscena = anchoEscena, altoEscena = altoEscena)

    fun crearVeloPresion(anchoEscena: Double, altoEscena: Double): VeloPresion =
        FabricaVisualSupportDelegate.crearVeloPresion(anchoEscena = anchoEscena, altoEscena = altoEscena)

    fun crearAvatarSubmarino(): AvatarSubmarino = FabricaVisualSupportDelegate.crearAvatarSubmarino()

    fun crearCapaFondoParallax(anchoMundo: Double, altoMundo: Double): CapaFondoParallax =
        FabricaVisualSupportDelegate.crearCapaFondoParallax(anchoMundo = anchoMundo, altoMundo = altoMundo)

    fun crearCapaIntermediaParallax(anchoMundo: Double, altoMundo: Double): CapaIntermediaParallax =
        FabricaVisualSupportDelegate.crearCapaIntermediaParallax(anchoMundo = anchoMundo, altoMundo = altoMundo)

    fun crearBucleJuego(
        sesion: SesionJuego,
        vista: VistaJuego,
        controlador: ControladorTeclado
    ): BucleJuego = FabricaVisualSupportDelegate.crearBucleJuego(
        sesion = sesion,
        vista = vista,
        controlador = controlador
    )

    fun crearControladorTeclado(): ControladorTeclado = FabricaVisualSupportDelegate.crearControladorTeclado()

    fun crearMenuPrincipal(
        anchoEscena: Double,
        altoEscena: Double,
        alJugar: () -> Unit
    ): MenuPrincipal = FabricaVisualSupportDelegate.crearMenuPrincipal(
        anchoEscena = anchoEscena,
        altoEscena = altoEscena,
        alJugar = alJugar
    )

    fun crearSesionJuego(
        anchoMapa: Double = ConfiguracionSesion.ANCHO_MAPA,
        altoMapa: Double = ConfiguracionSesion.ALTO_MAPA,
        semilla: Long = ConfiguracionSesion.SEMILLA_DEFAULT
    ): SesionJuego = FabricaVisualSupportDelegate.crearSesionJuego(
        anchoMapa = anchoMapa,
        altoMapa = altoMapa,
        semilla = semilla
    )

    fun crearVistaJuego(
        anchoEscena: Double,
        altoEscena: Double,
        sesion: SesionJuego,
        hud: HUDJuego = crearHUDJuego(),
        alReintentar: () -> Unit = {},
        alVolverAlMenu: () -> Unit = {}
    ): VistaJuego = FabricaVisualSupportDelegate.crearVistaJuego(
        anchoEscena = anchoEscena,
        altoEscena = altoEscena,
        sesion = sesion,
        hud = hud,
        alReintentar = alReintentar,
        alVolverAlMenu = alVolverAlMenu
    )

    internal fun crearAnimacionHeliceSubmarino(): AnimacionHeliceSubmarino =
        FabricaVisualSupportDelegate.crearAnimacionHeliceSubmarino()

    internal fun crearAnimacionBalizaEstacion(): AnimacionBalizaEstacion =
        FabricaVisualSupportDelegate.crearAnimacionBalizaEstacion()

    fun crearPincelArrecifes(
        gc: GraphicsContext,
        random: Random,
        anchoMundo: Double,
        altoMundo: Double
    ): PincelArrecifes = FabricaVisualSupportDelegate.crearPincelArrecifes(
        gc = gc,
        random = random,
        anchoMundo = anchoMundo,
        altoMundo = altoMundo
    )

    fun crearPincelCordillera(
        gc: GraphicsContext,
        random: Random,
        anchoMundo: Double
    ): PincelCordillera = FabricaVisualSupportDelegate.crearPincelCordillera(
        gc = gc,
        random = random,
        anchoMundo = anchoMundo
    )

    fun crearPincelParticulas(
        gc: GraphicsContext,
        random: Random,
        anchoMundo: Double,
        altoMundo: Double,
        profundidadRayos: Double
    ): PincelParticulas = FabricaVisualSupportDelegate.crearPincelParticulas(
        gc = gc,
        random = random,
        anchoMundo = anchoMundo,
        altoMundo = altoMundo,
        profundidadRayos = profundidadRayos
    )

    fun crearDetallesCordillera(
        gc: GraphicsContext,
        random: Random,
        anchoMundo: Double,
        perfil: PerfilCordillera
    ): DetallesCordillera = FabricaVisualSupportDelegate.crearDetallesCordillera(
        gc = gc,
        random = random,
        anchoMundo = anchoMundo,
        perfil = perfil
    )

    fun crearPerfilCordillera(xs: DoubleArray, ys: DoubleArray): PerfilCordillera =
        FabricaVisualSupportDelegate.crearPerfilCordillera(xs = xs, ys = ys)

    internal fun crearActualizadorParticulasMarinas(
        ancho: Double,
        alto: Double,
        random: Random
    ): ActualizadorParticulasMarinas = FabricaVisualSupportDelegate.crearActualizadorParticulasMarinas(
        ancho = ancho,
        alto = alto,
        random = random
    )

    internal fun crearFabricaParticulasMarinas(
        random: Random,
        ancho: Double,
        alto: Double
    ): FabricaParticulasMarinas = FabricaVisualSupportDelegate.crearFabricaParticulasMarinas(
        random = random,
        ancho = ancho,
        alto = alto
    )

    internal fun crearPintorParticulasMarinas(gc: GraphicsContext): PintorParticulasMarinas =
        FabricaVisualSupportDelegate.crearPintorParticulasMarinas(gc = gc)

    internal fun crearParticulaMarina(
        x: Double,
        y: Double,
        radio: Double,
        velocidadX: Double,
        velocidadY: Double,
        alfa: Double,
        fase: Double
    ): ParticulaMarina = FabricaVisualSupportDelegate.crearParticulaMarina(
        x = x,
        y = y,
        radio = radio,
        velocidadX = velocidadX,
        velocidadY = velocidadY,
        alfa = alfa,
        fase = fase
    )

    internal fun crearActualizadorPecesMarinos(): ActualizadorPecesMarinos =
        FabricaVisualSupportDelegate.crearActualizadorPecesMarinos()

    internal fun crearFabricaPecesMarinos(
        random: Random,
        ancho: Double,
        alto: Double
    ): FabricaPecesMarinos = FabricaVisualSupportDelegate.crearFabricaPecesMarinos(
        random = random,
        ancho = ancho,
        alto = alto
    )

    internal fun crearPintorPecesMarinos(gc: GraphicsContext): PintorPecesMarinos =
        FabricaVisualSupportDelegate.crearPintorPecesMarinos(gc = gc)

    internal fun crearPezMarino(
        centroX: Double,
        centroY: Double,
        radioX: Double,
        radioY: Double,
        velocidadAngular: Double,
        angulo: Double,
        fase: Double,
        frecuencia: Double,
        amplitud: Double,
        tamano: Double,
        colorCuerpo: Color,
        colorAleta: Color,
        alfa: Double,
        x: Double = centroX + radioX * cos(angulo),
        y: Double = centroY + radioY * sin(angulo)
    ): PezMarino = PezMarino(
        centroX = centroX,
        centroY = centroY,
        radioX = radioX,
        radioY = radioY,
        velocidadAngular = velocidadAngular,
        angulo = angulo,
        fase = fase,
        frecuencia = frecuencia,
        amplitud = amplitud,
        tamano = tamano,
        colorCuerpo = colorCuerpo,
        colorAleta = colorAleta,
        alfa = alfa,
        x = x,
        y = y
    )

    fun crearDetallesRoca(
        gc: GraphicsContext,
        random: Random,
        escala: Double
    ): DetallesRoca = FabricaVisualSupportDelegate.crearDetallesRoca(
        gc = gc,
        random = random,
        escala = escala
    )

    fun crearPincelRocaExterior(
        gc: GraphicsContext,
        random: Random,
        escala: Double
    ): PincelRocaExterior = FabricaVisualSupportDelegate.crearPincelRocaExterior(
        gc = gc,
        random = random,
        escala = escala
    )

    fun crearPaletaTerrenoRocoso(): PaletaTerrenoRocoso = FabricaVisualSupportDelegate.crearPaletaTerrenoRocoso()

    fun crearPincelFormacionRocosa(
        gc: GraphicsContext,
        random: Random,
        escala: Double,
        paleta: PaletaTerrenoRocoso = crearPaletaTerrenoRocoso()
    ): PincelFormacionRocosa = FabricaVisualSupportDelegate.crearPincelFormacionRocosa(
        gc = gc,
        random = random,
        escala = escala,
        paleta = paleta
    )

    fun crearFondoParallaxState(
        offsetsY: List<Double>,
        colorActivo: ColorRgb
    ): FondoParallaxState = FabricaVisualSupportDelegate.crearFondoParallaxState(
        offsetsY = offsetsY,
        colorActivo = colorActivo
    )

    fun crearColorRgb(r: Int, g: Int, b: Int): ColorRgb = FabricaVisualCoreDelegate.crearColorRgb(r = r, g = g, b = b)

    fun crearTransicionesExplosion(
        destino: ObservableList<Node>,
        random: Random
    ): List<Animation> = FabricaVisualSupportDelegate.crearTransicionesExplosion(
        destino = destino,
        random = random
    )
}