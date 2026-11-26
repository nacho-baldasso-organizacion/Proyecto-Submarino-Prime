package submarino

import kotlin.random.Random

object FabricaRecursosDelegate {

    fun crearFabricaNavesSimulacion(): FabricaNavesSimulacion = FabricaNavesSimulacion()

    fun crearFabricaRecolectablesMineral(): FabricaRecolectablesMineral =
        FabricaEconomíaYServiciosDelegate.crearFabricaRecolectablesMineral()

    fun crearCatalogoMejorasSesion(): CatalogoMejorasSesion = CatalogoMejorasSesion()

    fun crearDetectorColisionesRadio(radio: Double): DetectorColisionesRadio = DetectorColisionesRadio(radio = radio)

    fun crearEstacionAcople(
        zonaAcople: ZonaAcople = FabricaSubmarinoDelegate.crearZonaSuperficie()
    ): EstacionAcople = EstacionAcople(zonaAcople = zonaAcople)

    fun crearZonaSuperficie(profundidadMaxima: Double = 0.0): ZonaSuperficie =
        FabricaSubmarinoDelegate.crearZonaSuperficie(profundidadMaxima = profundidadMaxima)

    fun crearZonaAcoplePlataforma(
        xMin: Double,
        xMax: Double,
        profundidadMaxima: Double = 0.0
    ): ZonaAcoplePlataforma = ZonaAcoplePlataforma(
        xMin = xMin,
        xMax = xMax,
        profundidadMaxima = profundidadMaxima
    )

    fun crearEstadoNavegacion(
        posicionX: Double,
        posicionY: Double,
        coordenadaX: Double,
        profundidad: Double,
        oxigeno: Double,
        estadoOxigeno: EstadoOxigeno
    ): EstadoNavegacion = EstadoNavegacion(
        posicionX = posicionX,
        posicionY = posicionY,
        coordenadaX = coordenadaX,
        profundidad = profundidad,
        oxigeno = oxigeno,
        estadoOxigeno = estadoOxigeno
    )

    fun crearGeneradorParedes(
        random: Random = Random.Default,
        fabricaRecolectables: FabricaRecolectables = FabricaEconomíaYServiciosDelegate.crearFabricaRecolectablesMineral()
    ): GeneradorParedes = GeneradorParedes(
        random = random,
        fabricaRecolectables = fabricaRecolectables
    )

    fun crearMineral(
        id: String,
        tipo: TipoMineral,
        posX: Int,
        posY: Int,
        recolectado: Boolean = false
    ): Mineral = Mineral(
        id = id,
        tipo = tipo,
        posX = posX,
        posY = posY,
        recolectado = recolectado
    )

    fun crearNodoMineral(
        id: String,
        tipo: TipoNodoMineral,
        mineral: Mineral,
        radio: Double = SesionJuego.RADIO_INTERACCION
    ): NodoMineral = NodoMineral(
        id = id,
        tipo = tipo,
        mineral = mineral,
        radio = radio
    )

    fun crearEfectoOxigeno(
        incremento: Double,
        aumentarOxigenoMaximo: (Double) -> Unit
    ): EfectoMejora = object : EfectoBase(incremento) {
        override fun aplicar(submarino: SubmarinoMejorable) {
            aumentarOxigenoMaximo(magnitud)
        }
    }

    fun crearMejora(
        id: String,
        nombre: String,
        descripcion: String,
        nivelInicial: Int = 1,
        nivelMaximo: Int = 5,
        efecto: EfectoMejora,
        estrategiaCosto: EstrategiaCosto
    ): Mejora = Mejora(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        nivelInicial = nivelInicial,
        nivelMaximo = nivelMaximo,
        efecto = efecto,
        estrategiaCosto = estrategiaCosto
    )
}