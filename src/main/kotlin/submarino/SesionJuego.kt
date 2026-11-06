package submarino

import kotlin.math.roundToInt

/**
 * Modelo de la partida, completamente independiente de JavaFX.
 *
 * Coordina el submarino del backend, la recolección, el acople, el comercio y
 * la tienda de mejoras. La vista únicamente consume instantáneas inmutables.
 */
class SesionJuego(
    private val anchoMapa: Double = ConfiguracionSesion.ANCHO_MAPA,
    private val altoMapa: Double = ConfiguracionSesion.ALTO_MAPA,
    private val semilla: Long = ConfiguracionSesion.SEMILLA_DEFAULT
) {

    init {
        require(anchoMapa > 0.0) { "El ancho del mapa debe ser positivo: $anchoMapa" }
        require(altoMapa > 0.0) { "La altura del mapa debe ser positiva: $altoMapa" }
    }

    private val bodega: Bodega =
        FabricaDominio.crearBodega(ConfiguracionSesion.CAPACIDAD_INICIAL_BODEGA)
    private val billetera: Monedero =
        FabricaDominio.crearMonedero(ConfiguracionSesion.SALDO_INICIAL)
    private val casco: ModuloCasco =
        FabricaDominio.crearModuloCasco(ConfiguracionSesion.PRESION_MAXIMA_INICIAL)
    private val gestorDanio: GestorDanioSubmarino =
        FabricaDominio.crearGestorDanioSubmarino(casco)
    private val partida: EstadoPartida =
        FabricaDominio.crearEstadoPartida(anchoMapa, altoMapa, gestorDanio, casco)
    private val submarino: Submarino = FabricaDominio.crearSubmarino(
        posX = ConfiguracionSesion.ESTACION_X,
        posY = ConfiguracionSesion.PROFUNDIDAD_INICIAL,
        velocidad = ConfiguracionSesion.VELOCIDAD_INICIAL,
        casco = casco,
        pesoBase = ConfiguracionSesion.PESO_BASE,
        bodega = bodega,
        velocidadAscensoBase = ConfiguracionSesion.VELOCIDAD_ASCENSO_INICIAL,
        billetera = billetera,
        notificador = NotificadorSilencioso
    )

    private val zonaAcople: ZonaAcoplePlataforma = FabricaDominio.crearZonaAcoplePlataforma(
        xMin = ConfiguracionSesion.ESTACION_X - ConfiguracionSesion.ESTACION_ANCHO / 2.0,
        xMax = ConfiguracionSesion.ESTACION_X + ConfiguracionSesion.ESTACION_ANCHO / 2.0,
        profundidadMaxima = ConfiguracionSesion.PROFUNDIDAD_ZONA_ACOPLE
    )
    private val estacion: EstacionAcople = FabricaDominio.crearEstacionAcople(zonaAcople)
    private val servicioRecoleccion: ServicioRecoleccionContrato =
        FabricaDominio.crearServicioRecoleccion(
            almacenamiento = bodega,
            detectorColisiones =
                FabricaDominio.crearDetectorColisionesRadio(ConfiguracionSesion.RADIO_INTERACCION)
        )
    private val servicioComercio: ServicioComercioContrato = FabricaDominio.crearServicioComercio()
    private val catalogoMejoras: CatalogoMejorasSesion = FabricaDominio.crearCatalogoMejorasSesion()
    private val catalogo: CatalogoUpgrades = crearCatalogo()
    private val tienda: TiendaMejoras = FabricaDominio.crearTiendaMejoras(
        catalogo = catalogo,
        procesadorCompras = FabricaDominio.crearServicioUpgrades(catalogo)
    )
    private val economia: EconomiaEstacion = FabricaDominio.crearEconomiaEstacion(
        billetera = billetera,
        casco = casco,
        submarino = submarino,
        servicioComercio = servicioComercio,
        tienda = tienda
    )

    private val terreno: TerrenoSubmarino = TerrenoSubmarino.crear(semilla, anchoMapa, altoMapa)
    private val generadorNodos: GeneradorNodosMinerales =
        FabricaDominio.crearGeneradorNodosMinerales(terreno, anchoMapa, altoMapa, semilla)
    private val nodos: List<NodoMineral> = generadorNodos.generar()
    private val minerales: List<Recolectable> = nodos.map { it.mineral }

    /**
     * Indica si la partida terminó por asfixia o por colapso del casco.
     */
    fun estaDestruida(): Boolean = partida.destruido

    /**
     * Actualiza la navegación, el oxígeno y el desgaste del casco.
     */
    fun actualizar(deltaSegundos: Double, direccionX: Double, direccionY: Double) {
        partida.actualizar(
            deltaSegundos = deltaSegundos,
            direccionX = direccionX,
            direccionY = direccionY,
            estaAcoplado = submarino.estaAcoplado,
            velocidadNave = submarino.velocidad
        )
    }

    /**
     * Intenta minar el nodo más cercano dentro del radio de interacción.
     */
    fun minar(): ResultadoRecoleccion {
        if (partida.destruido) {
            partida.actualizarMensaje("El submarino está destruido. No puedes minar.")
            return ResultadoRecoleccion.SinColision
        }
        if (submarino.estaAcoplado) {
            partida.actualizarMensaje(
                "Estás acoplado en la estación. Desacopla para volver a minar."
            )
            return ResultadoRecoleccion.SinColision
        }

        val posicion = FabricaDominio.crearPosicion(
            partida.posicionX.roundToInt(),
            partida.posicionY.roundToInt()
        )
        val resultado = servicioRecoleccion.procesarRecoleccion(posicion, minerales)
        partida.actualizarMensaje(resultado.obtenerMensajeJugador())
        return resultado
    }

    /**
     * Acopla la nave si se encuentra dentro de la zona de la estación base.
     */
    fun acoplar(): Boolean {
        if (partida.destruido) {
            partida.actualizarMensaje("El submarino está destruido. No puedes acoplar.")
            return false
        }
        if (submarino.estaAcoplado) {
            return true
        }
        val exito = estacion.acoplar(submarino, partida.posicionX, partida.posicionY)
        partida.actualizarMensaje(
            if (exito) {
                "Acoplado a la Estación Base. Tienda de mejoras disponible."
            } else {
                "Acércate a la Estación Base en la superficie (Y ≈ 0) para acoplar."
            }
        )
        return exito
    }

    /**
     * Libera la nave de la estación y reanuda la navegación.
     */
    fun desacoplar(): Boolean {
        val exito = estacion.desacoplar()
        if (exito) {
            partida.actualizarMensaje("Desacoplado. Continúa la exploración abisal.")
        }
        return exito
    }

    /**
     * Vende el contenido completo de la bodega mientras la nave está acoplada.
     */
    fun venderMinerales(): ResultadoComercio {
        val resultado = economia.venderMinerales()
        partida.actualizarMensaje(resultado.obtenerResumen())
        return resultado
    }

    /**
     * Recarga el oxígeno al máximo descontando monedas de la billetera.
     */
    fun recargarOxigeno(): String {
        val texto = economia.recargarOxigeno(
            partida.oxigeno,
            partida.oxigenoMaximo,
            partida.destruido,
            partida::establecerOxigeno
        )
        partida.actualizarMensaje(texto)
        return texto
    }

    /**
     * Repara la integridad del casco al máximo descontando monedas.
     */
    fun repararCasco(): String {
        val texto = economia.repararCasco(partida.destruido)
        partida.actualizarMensaje(texto)
        return texto
    }

    /**
     * Compra una mejora del catálogo y la aplica al submarino.
     */
    fun comprarMejora(idMejora: String): ResultadoCompra {
        val resultado = economia.comprarMejora(idMejora)
        partida.actualizarMensaje(resultado.obtenerMensaje())
        return resultado
    }

    /**
     * Instantánea inmutable para el HUD y la vista del mundo.
     */
    fun estado(): EstadoJuego = FabricaDominio.crearEstadoJuego(
        posicionX = partida.posicionX,
        profundidad = partida.posicionY,
        oxigeno = partida.oxigeno,
        capacidadOxigeno = partida.oxigenoMaximo,
        integridadCasco = casco.integridad,
        monedas = billetera.saldo,
        pesoBodega = bodega.pesoActual,
        capacidadBodega = bodega.capacidadMaxima,
        cantidadItems = bodega.cantidadItems,
        nodosDisponibles = nodos.count { !it.recolectado },
        enZonaAcople = estacion.estaEnZonaDeAcople(partida.posicionX, partida.posicionY),
        acoplado = submarino.estaAcoplado,
        velocidad = submarino.velocidad,
        direccionHorizontal = partida.direccionHorizontal,
        mensaje = partida.mensaje,
        destruido = partida.destruido,
        causaDestruccion = partida.causaDestruccion,
        presionActual = gestorDanio.presionActual(partida.posicionY),
        presionMaxima = casco.presionMaximaPa
    )

    fun nodos(): List<NodoMineral> = nodos

    fun terreno(): TerrenoSubmarino = terreno

    fun catalogo(): List<Mejora> = catalogo.obtenerTodas()

    private fun crearCatalogo(): CatalogoUpgrades =
        catalogoMejoras.crear(partida::aumentarOxigenoMaximo)

    companion object {
        const val ANCHO_MAPA = ConfiguracionSesion.ANCHO_MAPA
        const val ALTO_MAPA = ConfiguracionSesion.ALTO_MAPA
        const val ESTACION_X = ConfiguracionSesion.ESTACION_X
        const val ESTACION_ANCHO = ConfiguracionSesion.ESTACION_ANCHO
        const val PROFUNDIDAD_ZONA_ACOPLE = ConfiguracionSesion.PROFUNDIDAD_ZONA_ACOPLE
        const val RADIO_INTERACCION = ConfiguracionSesion.RADIO_INTERACCION

        const val COSTO_RECARGA_OXIGENO = EconomiaEstacion.COSTO_RECARGA_OXIGENO
        const val COSTO_REPARACION_CASCO = EconomiaEstacion.COSTO_REPARACION_CASCO
        const val ID_MEJORA_OXIGENO = CatalogoMejorasSesion.ID_MEJORA_OXIGENO
        const val DISTANCIA_MINIMA_ENTRE_NODOS = GeneradorNodosMinerales.DISTANCIA_MINIMA_ENTRE_NODOS
    }
}
