package submarino

object FabricaEconomíaYServiciosDelegate {

    fun crearEconomiaEstacion(
        billetera: Monedero,
        casco: ModuloCasco,
        submarino: Submarino,
        servicioComercio: ServicioComercioContrato,
        tienda: TiendaMejoras
    ): EconomiaEstacion = EconomiaEstacion(
        billetera = billetera,
        casco = casco,
        submarino = submarino,
        servicioComercio = servicioComercio,
        tienda = tienda
    )

    fun crearServicioComercio(): ServicioComercio = ServicioComercio()

    fun crearServicioComercio(calculadoraPrecios: CalculadoraPrecios): ServicioComercio =
        ServicioComercio(calculadoraPrecios = calculadoraPrecios)

    fun crearServicioRecoleccion(
        almacenamiento: Almacenamiento,
        detectorColisiones: DetectorColisiones
    ): ServicioRecoleccion = ServicioRecoleccion(
        almacenamiento = almacenamiento,
        detectorColisiones = detectorColisiones
    )

    fun crearServicioUpgrades(catalogo: ConsultableCatalogo): ServicioUpgrades =
        ServicioUpgrades(catalogo = catalogo)

    fun crearTiendaMejoras(
        catalogo: ConsultableCatalogo,
        procesadorCompras: ProcesadorCompras
    ): TiendaMejoras = TiendaMejoras(
        catalogo = catalogo,
        procesadorCompras = procesadorCompras
    )

    fun crearMonedero(saldoInicial: Int = 0): Monedero = Monedero(saldoInicial = saldoInicial)

    fun crearSistemaOxigeno(
        oxigenoInicial: Double,
        maximoInicial: Double = oxigenoInicial,
        consumoBasePorSegundo: Double,
        consumoMovimientoPorSegundo: Double,
        maximoAbsoluto: Double = Double.MAX_VALUE,
        minimo: Double = 0.0
    ): SistemaOxigeno = SistemaOxigeno(
        oxigenoInicial = oxigenoInicial,
        maximoInicial = maximoInicial,
        consumoBasePorSegundo = consumoBasePorSegundo,
        consumoMovimientoPorSegundo = consumoMovimientoPorSegundo,
        maximoAbsoluto = maximoAbsoluto,
        minimo = minimo
    )

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
    ): EstadoJuego = EstadoJuego(
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
    ): EstadoPartida = EstadoPartida(
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
    ): GeneradorNodosMinerales = GeneradorNodosMinerales(
        terreno = terreno,
        anchoMapa = anchoMapa,
        altoMapa = altoMapa,
        semilla = semilla
    )

    fun crearFormacionRocosa(
        id: String,
        puntos: List<PuntoCresta>,
        baseY: Double
    ): FormacionRocosa = FormacionRocosa(
        id = id,
        puntos = puntos,
        baseY = baseY
    )

    fun crearPuntoCresta(x: Double, y: Double): PuntoCresta = PuntoCresta(x = x, y = y)

    fun crearTerrenoSubmarino(
        formaciones: List<FormacionRocosa>,
        sueloMarino: FormacionRocosa
    ): TerrenoSubmarino = TerrenoSubmarino(
        formaciones = formaciones,
        sueloMarino = sueloMarino
    )

    fun crearClasificacionRecurso(tipo: TipoMineral): ClasificacionRecurso =
        ClasificacionRecurso(tipo = tipo)

    fun crearEstadoRecoleccionMineral(
        inicialmenteRecolectado: Boolean = false
    ): EstadoRecoleccionMineral =
        EstadoRecoleccionMineral(inicialmenteRecolectado = inicialmenteRecolectado)

    fun crearEstrategiaCostoLineal(
        costoBase: Int,
        incrementoPorNivel: Int
    ): EstrategiaCostoLineal = EstrategiaCostoLineal(
        costoBase = costoBase,
        incrementoPorNivel = incrementoPorNivel
    )

    fun crearControladorMovimientoTeclado(): ControladorMovimientoTeclado =
        ControladorMovimientoTeclado()

    fun crearControladorAccionesTeclado(): ControladorAccionesTeclado =
        ControladorAccionesTeclado()

    fun crearColorRgb(r: Int, g: Int, b: Int): ColorRgb = ColorRgb(r = r, g = g, b = b)

    fun crearFabricaRecolectablesMineral(): FabricaRecolectablesMineral =
        FabricaRecolectablesMineral()
}