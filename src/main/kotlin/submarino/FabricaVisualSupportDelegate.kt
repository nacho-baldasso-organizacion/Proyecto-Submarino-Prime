package submarino

import javafx.animation.Animation
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import kotlin.random.Random

/**
 * Sub-fábrica delegada de creación de controles, pinceles, animaciones internas
 * y componentes de soporte visual. Menos de 8 tipos del proyecto.
 */
object FabricaVisualSupportDelegate {

    fun crearCamaraJuego(
        anchoMundo: Double,
        altoMundo: Double,
        anchoEscena: Double,
        altoEscena: Double,
        escala: Double
    ): CamaraJuego = CamaraJuego(anchoMundo, altoMundo, anchoEscena, altoEscena, escala)

    fun crearGameOverModal(
        alReintentar: () -> Unit,
        alVolverAlMenu: () -> Unit
    ): GameOverModal = GameOverModal(alReintentar, alVolverAlMenu)

    fun crearTiendaModal(sesion: SesionJuego): TiendaModal = TiendaModal(sesion)

    fun crearVeloAbisal(anchoEscena: Double, altoEscena: Double): VeloAbisal =
        VeloAbisal(anchoEscena, altoEscena)

    fun crearVeloPresion(anchoEscena: Double, altoEscena: Double): VeloPresion =
        VeloPresion(anchoEscena, altoEscena)

    fun crearAvatarSubmarino(): AvatarSubmarino = AvatarSubmarino()

    fun crearCapaFondoParallax(anchoMundo: Double, altoMundo: Double): CapaFondoParallax =
        CapaFondoParallax(anchoMundo, altoMundo)

    fun crearCapaIntermediaParallax(anchoMundo: Double, altoMundo: Double): CapaIntermediaParallax =
        CapaIntermediaParallax(anchoMundo, altoMundo)

    fun crearBucleJuego(
        sesion: SesionJuego,
        vista: VistaJuego,
        controlador: ControladorTeclado
    ): BucleJuego = BucleJuego(sesion, vista, controlador)

    fun crearControladorTeclado(): ControladorTeclado = ControladorTeclado()

    fun crearMenuPrincipal(
        anchoEscena: Double,
        altoEscena: Double,
        alJugar: () -> Unit
    ): MenuPrincipal = MenuPrincipal(anchoEscena, altoEscena, alJugar)

    fun crearSesionJuego(
        anchoMapa: Double = ConfiguracionSesion.ANCHO_MAPA,
        altoMapa: Double = ConfiguracionSesion.ALTO_MAPA,
        semilla: Long = ConfiguracionSesion.SEMILLA_DEFAULT
    ): SesionJuego = SesionJuego(anchoMapa, altoMapa, semilla)

    fun crearVistaJuego(
        anchoEscena: Double,
        altoEscena: Double,
        sesion: SesionJuego,
        hud: HUDJuego = HUDJuego(),
        alReintentar: () -> Unit = {},
        alVolverAlMenu: () -> Unit = {}
    ): VistaJuego = VistaJuego(anchoEscena, altoEscena, sesion, hud, alReintentar, alVolverAlMenu)

    internal fun crearAnimacionHeliceSubmarino(): AnimacionHeliceSubmarino = AnimacionHeliceSubmarino()

    internal fun crearAnimacionBalizaEstacion(): AnimacionBalizaEstacion = AnimacionBalizaEstacion()

    fun crearPincelArrecifes(
        gc: GraphicsContext,
        random: Random,
        anchoMundo: Double,
        altoMundo: Double
    ): PincelArrecifes = PincelArrecifes(gc, random, anchoMundo, altoMundo)

    fun crearPincelCordillera(
        gc: GraphicsContext,
        random: Random,
        anchoMundo: Double
    ): PincelCordillera = PincelCordillera(gc, random, anchoMundo)

    fun crearPincelParticulas(
        gc: GraphicsContext,
        random: Random,
        anchoMundo: Double,
        altoMundo: Double,
        profundidadRayos: Double
    ): PincelParticulas = PincelParticulas(gc, random, anchoMundo, altoMundo, profundidadRayos)

    fun crearDetallesCordillera(
        gc: GraphicsContext,
        random: Random,
        anchoMundo: Double,
        perfil: PerfilCordillera
    ): DetallesCordillera = DetallesCordillera(gc, random, anchoMundo, perfil)

    fun crearPerfilCordillera(xs: DoubleArray, ys: DoubleArray): PerfilCordillera =
        PerfilCordillera(xs, ys)

    internal fun crearActualizadorParticulasMarinas(
        ancho: Double,
        alto: Double,
        random: Random
    ): ActualizadorParticulasMarinas = ActualizadorParticulasMarinas(ancho, alto, random)

    internal fun crearFabricaParticulasMarinas(
        random: Random,
        ancho: Double,
        alto: Double
    ): FabricaParticulasMarinas = FabricaParticulasMarinas(random, ancho, alto)

    internal fun crearPintorParticulasMarinas(gc: GraphicsContext): PintorParticulasMarinas =
        PintorParticulasMarinas(gc)

    internal fun crearParticulaMarina(
        x: Double,
        y: Double,
        radio: Double,
        velocidadX: Double,
        velocidadY: Double,
        alfa: Double,
        fase: Double
    ): ParticulaMarina = ParticulaMarina(x, y, radio, velocidadX, velocidadY, alfa, fase)

    internal fun crearActualizadorPecesMarinos(): ActualizadorPecesMarinos = ActualizadorPecesMarinos()

    internal fun crearFabricaPecesMarinos(
        random: Random,
        ancho: Double,
        alto: Double
    ): FabricaPecesMarinos = FabricaPecesMarinos(random, ancho, alto)

    internal fun crearPintorPecesMarinos(gc: GraphicsContext): PintorPecesMarinos = PintorPecesMarinos(gc)

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
        colorCuerpo: Color,
        colorAleta: Color,
        alfa: Double,
        x: Double = centroX + radioX * Math.cos(angulo),
        y: Double = centroY + radioY * Math.sin(angulo)
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
        tamano = 1.0,
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
    ): DetallesRoca = DetallesRoca(gc, random, escala)

    fun crearPincelRocaExterior(
        gc: GraphicsContext,
        random: Random,
        escala: Double
    ): PincelRocaExterior = PincelRocaExterior(gc, random, escala)

    fun crearPaletaTerrenoRocoso(): PaletaTerrenoRocoso = PaletaTerrenoRocoso()

    fun crearPincelFormacionRocosa(
        gc: GraphicsContext,
        random: Random,
        escala: Double,
        paleta: PaletaTerrenoRocoso = crearPaletaTerrenoRocoso()
    ): PincelFormacionRocosa = PincelFormacionRocosa(gc, random, escala, paleta)

    fun crearFondoParallaxState(
        offsetsY: List<Double>,
        colorActivo: ColorRgb
    ): FondoParallaxState = FondoParallaxState(offsetsY, colorActivo)

    fun crearTransicionesExplosion(
        destino: ObservableList<Node>,
        random: Random
    ): List<Animation> = ConstructorExplosionSubmarino.crear(destino, random)
}