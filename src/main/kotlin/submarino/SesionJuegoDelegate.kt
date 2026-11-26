package submarino

/**
 * Sub-fábrica delegada de creación de subsistemas de sesión.
 * Concentra métodos de instanciación de los componentes del SesionJuego
 * con visibilidad pública, reduciendo el acoplamiento (CBO) de la clase principal.
 * Cada método encapsula la lógica de creación usando FabricaDominio y
 * otros delegates existentes del códigobase.
 */
object SesionJuegoDelegate {

    fun crearBodega(): Bodega = FabricaDominio.crearBodega(ConfiguracionSesion.CAPACIDAD_INICIAL_BODEGA)

    fun crearMonedero(): Monedero = FabricaDominio.crearMonedero(ConfiguracionSesion.SALDO_INICIAL)

    fun crearModuloCasco(presionMaximaPa: Double = ConfiguracionSesion.PRESION_MAXIMA_INICIAL): ModuloCasco =
        FabricaDominio.crearModuloCasco(presionMaximaPa)

    fun crearGestorDanioSubmarino(casco: Casco): GestorDanioSubmarino =
        FabricaDominio.crearGestorDanioSubmarino(casco)

    fun crearEstadoPartida(
        anchoMapa: Double,
        altoMapa: Double,
        gestorDanio: GestorDanioSubmarino,
        casco: ModuloCasco
    ): EstadoPartida = FabricaDominio.crearEstadoPartida(anchoMapa, altoMapa, gestorDanio, casco)

    fun crearSubmarino(
        posX: Double,
        posY: Double,
        velocidad: Double,
        casco: Casco,
        pesoBase: Double,
        bodega: Bodega,
        velocidadAscensoBase: Double,
        billetera: Monedero,
        notificador: Notificador,
        factoria: FabricaSubsistemasSubmarino
    ): Submarino = FabricaDominio.crearSubmarino(
        posX = posX,
        posY = posY,
        velocidad = velocidad,
        casco = casco,
        pesoBase = pesoBase,
        bodega = bodega,
        velocidadAscensoBase = velocidadAscensoBase,
        billetera = billetera,
        notificador = notificador,
        factoria = factoria
    )

    fun crearZonaAcoplePlataforma(
        xMin: Double,
        xMax: Double,
        profundidadMaxima: Double
    ): ZonaAcoplePlataforma = FabricaDominio.crearZonaAcoplePlataforma(
        xMin = xMin,
        xMax = xMax,
        profundidadMaxima = profundidadMaxima
    )

    fun crearEstacionAcople(zonaAcople: ZonaAcoplePlataforma): EstacionAcople =
        FabricaDominio.crearEstacionAcople(zonaAcople)

    fun crearServicioRecoleccion(almacenamiento: Almacenamiento): ServicioRecoleccionContrato = ServicioRecoleccion(
        almacenamiento = almacenamiento,
        detectorColisiones = FabricaDominio.crearDetectorColisionesRadio(ConfiguracionSesion.RADIO_INTERACCION)
    )

    fun crearServicioComercio(): ServicioComercioContrato = FabricaEconomíaYServiciosDelegate.crearServicioComercio()

    fun crearCatalogo(): CatalogoUpgrades = FabricaDominio.crearCatalogoMejorasSesion().crear { _ -> }

    fun crearTiendaMejoras(): TiendaMejoras = FabricaEconomíaYServiciosDelegate.crearTiendaMejoras(
        catalogo = crearCatalogo(),
        procesadorCompras = FabricaServiciosMejoraYEstrategiasDelegate.crearFabricaProcesadorComprasEstandar().crear(crearCatalogo())
    )

    fun crearEconomiaEstacion(
        billetera: Monedero,
        casco: ModuloCasco,
        submarino: Submarino,
        servicioComercio: ServicioComercioContrato,
        tienda: TiendaMejoras
    ): EconomiaEstacion = FabricaDominio.crearEconomiaEstacion(
        billetera = billetera,
        casco = casco,
        submarino = submarino,
        servicioComercio = servicioComercio,
        tienda = tienda
    )

    fun crearGeneradorNodosMinerales(
        terreno: TerrenoSubmarino,
        anchoMapa: Double,
        altoMapa: Double,
        semilla: Long
    ): GeneradorNodosMinerales = FabricaDominio.crearGeneradorNodosMinerales(terreno, anchoMapa, altoMapa, semilla)

    fun crearPosicion(posX: Int, posY: Int): Posicion = FabricaDominio.crearPosicion(posX = posX, posY = posY)

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
    ): EstadoJuego = FabricaDominio.crearEstadoJuego(
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
}