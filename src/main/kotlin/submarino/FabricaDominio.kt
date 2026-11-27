package submarino

import kotlin.random.Random

/**
 * Composition root del dominio: centraliza la creación de las clases concretas
 * definidas en este repositorio para que el resto del código dependa de
 * abstracciones y no instancie implementaciones directamente (DIP).
 */
object FabricaDominio {

    fun crearCalculadoraPresionFluido(
        densidadFluido: Double = 1025.0,
        gravedad: Double = 9.81,
        presionAtmosferica: Double = 101325.0
    ): CalculadoraPresionFluido = FabricaAmbienteDelegate.crearCalculadoraPresionFluido(
        densidadFluido = densidadFluido,
        gravedad = gravedad,
        presionAtmosferica = presionAtmosferica
    )

    fun crearCascoReforzado(
        presionMaximaPa: Double,
        integridad: Double = 100.0,
        factorAbsorcion: Double = 0.5,
        notificador: Notificador = NotificadorConsola
    ): CascoReforzado = FabricaSubmarinoDelegate.crearCascoReforzado(
        presionMaximaPa = presionMaximaPa,
        integridad = integridad,
        factorAbsorcion = factorAbsorcion,
        notificador = notificador
    )

    fun crearModuloCasco(
        presionMaximaPa: Double,
        integridad: Double = 100.0,
        notificador: Notificador = NotificadorConsola
    ): ModuloCasco = FabricaSubmarinoDelegate.crearModuloCasco(
        presionMaximaPa = presionMaximaPa,
        integridad = integridad,
        notificador = notificador
    )

    fun crearSubmarino(
        posX: Double = 0.0,
        posY: Double = 0.0,
        velocidad: Double = 0.0,
        casco: Casco,
        pesoBase: Double = Submarino.PESO_BASE_DEFAULT,
        bodega: Almacenamiento? = null,
        velocidadAscensoBase: Double =
            if (velocidad > 0.0) velocidad else Submarino.VELOCIDAD_ASCENSO_DEFAULT,
        calculadorPeso: CalculadorPeso? = null,
        estrategiaAscenso: EstrategiaAscenso? = null,
        billetera: Billetera? = null,
        notificador: Notificador = NotificadorConsola,
        factoria: FabricaSubsistemasSubmarino = FabricaSubsistemasEstandar
    ): Submarino = FabricaSubmarinoDelegate.crearSubmarino(
        posX = posX,
        posY = posY,
        velocidad = velocidad,
        casco = casco,
        pesoBase = pesoBase,
        bodega = bodega,
        velocidadAscensoBase = velocidadAscensoBase,
        calculadorPeso = calculadorPeso,
        estrategiaAscenso = estrategiaAscenso,
        billetera = billetera,
        notificador = notificador,
        factoria = factoria
    )

    fun crearReporteSimulacionSubmarino(): ReporteSimulacionSubmarino =
        FabricaReportesDelegate.crearReporteSimulacionSubmarino()

    fun crearReporteNavegacionSimulacion(): ReporteNavegacionSimulacion =
        FabricaReportesDelegate.crearReporteNavegacionSimulacion()

    fun crearReporteInmersionSimulacion(): ReporteInmersionSimulacion =
        FabricaReportesDelegate.crearReporteInmersionSimulacion()

    fun crearReporteSolidSimulacion(): ReporteSolidSimulacion =
        FabricaReportesDelegate.crearReporteSolidSimulacion()

    fun crearFabricaNavesSimulacion(): FabricaNavesSimulacion =
        FabricaRecursosDelegate.crearFabricaNavesSimulacion()

    fun crearEscenarioNavegacionSubmarino(
        reporte: ReporteNavegacionSimulacion = FabricaAmbienteDelegate.crearReporteNavegacionSimulacion()
    ): EscenarioNavegacionSubmarino = EscenarioNavegacionSubmarino(reporte = reporte)

    fun crearEscenarioInmersionSubmarino(
        reporte: ReporteInmersionSimulacion = FabricaAmbienteDelegate.crearReporteInmersionSimulacion()
    ): EscenarioInmersionSubmarino = EscenarioInmersionSubmarino(reporte = reporte)

    fun crearEscenarioSolidSubmarino(
        reporte: ReporteSolidSimulacion = FabricaAmbienteDelegate.crearReporteSolidSimulacion()
    ): EscenarioSolidSubmarino = EscenarioSolidSubmarino(reporte = reporte)

    fun crearSimuladorSubmarino(
        reporte: ReporteSimulacionSubmarino = FabricaAmbienteDelegate.crearReporteSimulacionSubmarino(),
        fabrica: FabricaNavesSimulacion = FabricaRecursosDelegate.crearFabricaNavesSimulacion()
    ): SimuladorSubmarino = SimuladorSubmarino(reporte = reporte, fabrica = fabrica)

    fun crearBodega(capacidadMaxima: Double): Bodega =
        FabricaSubmarinoDelegate.crearBodega(capacidadMaxima = capacidadMaxima)

    fun crearCatalogoMejorasSesion(): CatalogoMejorasSesion =
        FabricaRecursosDelegate.crearCatalogoMejorasSesion()

    fun crearDetectorColisionesRadio(radio: Double): DetectorColisionesRadio =
        FabricaRecursosDelegate.crearDetectorColisionesRadio(radio = radio)

    fun crearEconomiaEstacion(
        billetera: Monedero,
        casco: ModuloCasco,
        submarino: Submarino,
        servicioComercio: ServicioComercioContrato,
        tienda: TiendaMejoras
    ): EconomiaEstacion = FabricaEconomíaYServiciosDelegate.crearEconomiaEstacion(
        billetera = billetera,
        casco = casco,
        submarino = submarino,
        servicioComercio = servicioComercio,
        tienda = tienda
    )

    fun crearZonaSuperficie(profundidadMaxima: Double = 0.0): ZonaSuperficie =
        FabricaSubmarinoDelegate.crearZonaSuperficie(profundidadMaxima = profundidadMaxima)

    fun crearEstacionAcople(
        zonaAcople: ZonaAcople = FabricaRecursosDelegate.crearZonaSuperficie()
    ): EstacionAcople = EstacionAcople(zonaAcople = zonaAcople)

    fun crearEstadoJuego(
        posicionX: Double,
        profundidad: Double,
        oxigeno: Double,
        capacidadOxigeno: Double,
        integridadCasco: Double,
        monedas: Int,
        pesoBodega: Double,
        capacidadBodega: Double,
        cantidadItems: Int,
        nodosDisponibles: Int,
        enZonaAcople: Boolean,
        acoplado: Boolean,
        velocidad: Double,
        direccionHorizontal: Double,
        mensaje: String,
        destruido: Boolean = false,
        causaDestruccion: String = "",
        presionActual: Double = 0.0,
        presionMaxima: Double = 1.0
    ): EstadoJuego = FabricaEconomíaYServiciosDelegate.crearEstadoJuego(
        posicionX = posicionX,
        profundidad = profundidad,
        oxigeno = oxigeno,
        capacidadOxigeno = capacidadOxigeno,
        integridadCasco = integridadCasco,
        monedas = monedas,
        pesoBodega = pesoBodega,
        capacidadBodega = capacidadBodega,
        cantidadItems = cantidadItems,
        nodosDisponibles = nodosDisponibles,
        enZonaAcople = enZonaAcople,
        acoplado = acoplado,
        velocidad = velocidad,
        direccionHorizontal = direccionHorizontal,
        mensaje = mensaje,
        destruido = destruido,
        causaDestruccion = causaDestruccion,
        presionActual = presionActual,
        presionMaxima = presionMaxima
    )

    fun crearEstadoPartida(
        anchoMapa: Double,
        altoMapa: Double,
        gestorDanio: GestorDanioSubmarino,
        casco: ModuloCasco
    ): EstadoPartida = FabricaEconomíaYServiciosDelegate.crearEstadoPartida(
        anchoMapa = anchoMapa,
        altoMapa = altoMapa,
        gestorDanio = gestorDanio,
        casco = casco
    )

    fun crearGeneradorNodosMinerales(
        terreno: TerrenoSubmarino,
        anchoMapa: Double,
        altoMapa: Double,
        semilla: Long
    ): GeneradorNodosMinerales = FabricaEconomíaYServiciosDelegate.crearGeneradorNodosMinerales(
        terreno = terreno,
        anchoMapa = anchoMapa,
        altoMapa = altoMapa,
        semilla = semilla
    )

    fun crearGestorDanioSubmarino(casco: Casco): GestorDanioSubmarino =
        FabricaSubmarinoDelegate.crearGestorDanioSubmarino(casco = casco)

    fun crearMonedero(saldoInicial: Int = 0): Monedero =
        FabricaEconomíaYServiciosDelegate.crearMonedero(saldoInicial = saldoInicial)

    fun crearPosicion(posX: Int, posY: Int): Posicion = FabricaAmbienteDelegate.crearPosicion(posX = posX, posY = posY)

    fun crearServicioComercio(): ServicioComercio =
        FabricaEconomíaYServiciosDelegate.crearServicioComercio()

    fun crearServicioComercio(calculadoraPrecios: CalculadoraPrecios): ServicioComercio =
        FabricaEconomíaYServiciosDelegate.crearServicioComercio(calculadoraPrecios = calculadoraPrecios)

    fun crearServicioRecoleccion(
        almacenamiento: Almacenamiento,
        detectorColisiones: DetectorColisiones
    ): ServicioRecoleccion = FabricaEconomíaYServiciosDelegate.crearServicioRecoleccion(
        almacenamiento = almacenamiento,
        detectorColisiones = detectorColisiones
    )

    fun crearServicioUpgrades(catalogo: ConsultableCatalogo): ServicioUpgrades =
        FabricaEconomíaYServiciosDelegate.crearServicioUpgrades(catalogo = catalogo)

    fun crearTiendaMejoras(
        catalogo: ConsultableCatalogo,
        procesadorCompras: ProcesadorCompras
    ): TiendaMejoras = FabricaEconomíaYServiciosDelegate.crearTiendaMejoras(
        catalogo = catalogo,
        procesadorCompras = procesadorCompras
    )

    fun crearZonaAcoplePlataforma(
        xMin: Double,
        xMax: Double,
        profundidadMaxima: Double = 0.0
    ): ZonaAcoplePlataforma = FabricaRecursosDelegate.crearZonaAcoplePlataforma(
        xMin = xMin,
        xMax = xMax,
        profundidadMaxima = profundidadMaxima
    )

    fun crearSistemaOxigeno(
        oxigenoInicial: Double,
        maximoInicial: Double = oxigenoInicial,
        consumoBasePorSegundo: Double,
        consumoMovimientoPorSegundo: Double,
        maximoAbsoluto: Double = Double.MAX_VALUE,
        minimo: Double = 0.0
    ): SistemaOxigeno = FabricaEconomíaYServiciosDelegate.crearSistemaOxigeno(
        oxigenoInicial = oxigenoInicial,
        maximoInicial = maximoInicial,
        consumoBasePorSegundo = consumoBasePorSegundo,
        consumoMovimientoPorSegundo = consumoMovimientoPorSegundo,
        maximoAbsoluto = maximoAbsoluto,
        minimo = minimo
    )

    fun crearEstadoNavegacion(
        posicionX: Double,
        posicionY: Double,
        coordenadaX: Double,
        profundidad: Double,
        oxigeno: Double,
        estadoOxigeno: EstadoOxigeno
    ): EstadoNavegacion = FabricaRecursosDelegate.crearEstadoNavegacion(
        posicionX = posicionX,
        posicionY = posicionY,
        coordenadaX = coordenadaX,
        profundidad = profundidad,
        oxigeno = oxigeno,
        estadoOxigeno = estadoOxigeno
    )

    fun crearGeneradorParedes(
        random: Random = Random.Default,
        fabricaRecolectables: FabricaRecolectables = FabricaRecursosDelegate.crearFabricaRecolectablesMineral()
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
    ): Mineral = FabricaRecursosDelegate.crearMineral(
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
    ): NodoMineral = FabricaRecursosDelegate.crearNodoMineral(
        id = id,
        tipo = tipo,
        mineral = mineral,
        radio = radio
    )

    fun crearFabricaRecolectablesMineral(): FabricaRecolectablesMineral =
        FabricaEconomíaYServiciosDelegate.crearFabricaRecolectablesMineral()

    fun crearFormacionRocosa(
        id: String,
        puntos: List<PuntoCresta>,
        baseY: Double
    ): FormacionRocosa = FabricaEconomíaYServiciosDelegate.crearFormacionRocosa(
        id = id,
        puntos = puntos,
        baseY = baseY
    )

    fun crearPuntoCresta(x: Double, y: Double): PuntoCresta =
        FabricaEconomíaYServiciosDelegate.crearPuntoCresta(x = x, y = y)

    fun crearTerrenoSubmarino(
        formaciones: List<FormacionRocosa>,
        sueloMarino: FormacionRocosa
    ): TerrenoSubmarino = FabricaEconomíaYServiciosDelegate.crearTerrenoSubmarino(
        formaciones = formaciones,
        sueloMarino = sueloMarino
    )

    fun crearClasificacionRecurso(tipo: TipoMineral): ClasificacionRecurso =
        FabricaEconomíaYServiciosDelegate.crearClasificacionRecurso(tipo = tipo)

    fun crearEstadoRecoleccionMineral(
        inicialmenteRecolectado: Boolean = false
    ): EstadoRecoleccionMineral =
        FabricaEconomíaYServiciosDelegate.crearEstadoRecoleccionMineral(
            inicialmenteRecolectado = inicialmenteRecolectado
        )

    fun crearEfectoOxigeno(
        incremento: Double,
        aumentarOxigenoMaximo: (Double) -> Unit
    ): EfectoMejora = FabricaRecursosDelegate.crearEfectoOxigeno(
        incremento = incremento,
        aumentarOxigenoMaximo = aumentarOxigenoMaximo
    )

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

    fun crearEstrategiaCostoLineal(
        costoBase: Int,
        incrementoPorNivel: Int
    ): EstrategiaCostoLineal = FabricaEconomíaYServiciosDelegate.crearEstrategiaCostoLineal(
        costoBase = costoBase,
        incrementoPorNivel = incrementoPorNivel
    )

    fun crearControladorMovimientoTeclado(): ControladorMovimientoTeclado =
        FabricaEconomíaYServiciosDelegate.crearControladorMovimientoTeclado()

    fun crearControladorAccionesTeclado(): ControladorAccionesTeclado =
        FabricaEconomíaYServiciosDelegate.crearControladorAccionesTeclado()

    fun crearColorRgb(r: Int, g: Int, b: Int): ColorRgb =
        FabricaEconomíaYServiciosDelegate.crearColorRgb(r = r, g = g, b = b)
}