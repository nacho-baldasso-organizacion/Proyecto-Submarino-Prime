package submarino

import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * Tipos de nodos mineros que aparecen de forma procedural en el mapa.
 *
 * La vista decide el color y la forma; el dominio solo conoce la equivalencia
 * con el catálogo de [TipoMineral] del backend.
 */
enum class TipoNodoMineral(
    val etiqueta: String,
    val tipoMineral: TipoMineral
) {
    COBRE("Cobre", TipoMineral.COBRE_MARINO),
    HIERRO("Hierro", TipoMineral.NODULO_DE_MANGANESO),
    ORO("Oro", TipoMineral.GEMA_DE_SALMUERA)
}

/**
 * Nodo minero del mapa. Mantiene el [Mineral] de dominio y su radio de
 * interacción, sin exponer campos mutables a la vista.
 */
data class NodoMineral(
    val id: String,
    val tipo: TipoNodoMineral,
    val mineral: Mineral,
    val radio: Double = SesionJuego.RADIO_INTERACCION
) {
    val x: Double
        get() = mineral.posX.toDouble()

    val y: Double
        get() = mineral.posY.toDouble()

    val recolectado: Boolean
        get() = mineral.recolectado
}

/**
 * Instantánea inmutable del estado de la partida que consume la capa JavaFX.
 */
data class EstadoJuego(
    val posicionX: Double,
    val profundidad: Double,
    val oxigeno: Double,
    val capacidadOxigeno: Double,
    val integridadCasco: Double,
    val monedas: Int,
    val pesoBodega: Double,
    val capacidadBodega: Double,
    val cantidadItems: Int,
    val nodosDisponibles: Int,
    val enZonaAcople: Boolean,
    val acoplado: Boolean,
    val velocidad: Double,
    val direccionHorizontal: Double,
    val mensaje: String,
    val destruido: Boolean = false,
    val causaDestruccion: String = "",
    val presionActual: Double = 0.0,
    val presionMaxima: Double = 1.0
)

/**
 * Modelo de la partida, completamente independiente de JavaFX.
 *
 * Coordina el submarino del backend, la recolección, el acople, el comercio y
 * la tienda de mejoras. La vista únicamente consume instantáneas inmutables.
 */
class SesionJuego(
    private val anchoMapa: Double = ANCHO_MAPA,
    private val altoMapa: Double = ALTO_MAPA,
    private val semilla: Long = SEMILLA_DEFAULT
) {

    init {
        require(anchoMapa > 0.0) { "El ancho del mapa debe ser positivo: $anchoMapa" }
        require(altoMapa > 0.0) { "La altura del mapa debe ser positiva: $altoMapa" }
    }

    private val bodega: Bodega = Bodega(CAPACIDAD_INICIAL_BODEGA)
    private val billetera: Monedero = Monedero(SALDO_INICIAL)
    private val casco: ModuloCasco = ModuloCasco(PRESION_MAXIMA_INICIAL)
    private val submarino: Submarino = Submarino(
        posX = ESTACION_X,
        posY = PROFUNDIDAD_INICIAL,
        velocidad = VELOCIDAD_INICIAL,
        casco = casco,
        pesoBase = PESO_BASE,
        bodega = bodega,
        velocidadAscensoBase = VELOCIDAD_ASCENSO_INICIAL,
        billetera = billetera,
        notificador = NotificadorSilencioso
    )

    private val zonaAcople: ZonaAcoplePlataforma = ZonaAcoplePlataforma(
        xMin = ESTACION_X - ESTACION_ANCHO / 2.0,
        xMax = ESTACION_X + ESTACION_ANCHO / 2.0,
        profundidadMaxima = PROFUNDIDAD_ZONA_ACOPLE
    )
    private val estacion: EstacionAcople = EstacionAcople(zonaAcople)
    private val servicioRecoleccion: ServicioRecoleccionContrato = ServicioRecoleccion(
        almacenamiento = bodega,
        detectorColisiones = DetectorColisionesRadio(RADIO_INTERACCION)
    )
    private val servicioComercio: ServicioComercioContrato = ServicioComercio()
    private val catalogo: CatalogoUpgrades = crearCatalogo()
    private val tienda: TiendaMejoras = TiendaMejoras(
        catalogo = catalogo,
        procesadorCompras = ServicioUpgrades(catalogo)
    )

    private val terreno: TerrenoSubmarino = TerrenoSubmarino.crear(semilla, anchoMapa, altoMapa)
    private val nodos: List<NodoMineral> = generarNodos()
    private val minerales: List<Recolectable> = nodos.map { it.mineral }

    private var posicionX: Double = ESTACION_X
    private var posicionY: Double = PROFUNDIDAD_INICIAL
    private var oxigenoMaximo: Double = OXIGENO_MAXIMO_INICIAL
    private var oxigeno: Double = OXIGENO_MAXIMO_INICIAL
    private var direccionHorizontal: Double = 1.0
    private var destruido: Boolean = false
    private var causaDestruccion: String = ""
    private var mensaje: String =
        "Explora el abismo: WASD / Flechas para moverte y ESPACIO para minar."

    /**
     * Indica si la partida terminó por asfixia o por colapso del casco.
     */
    fun estaDestruida(): Boolean = destruido

    /**
     * Actualiza la navegación, el oxígeno y el desgaste del casco.
     */
    fun actualizar(deltaSegundos: Double, direccionX: Double, direccionY: Double) {
        if (destruido) {
            return
        }
        if (!deltaSegundos.isFinite() || deltaSegundos <= 0.0) {
            return
        }
        if (submarino.estaAcoplado) {
            return
        }

        val dx = direccionX.coerceIn(-1.0, 1.0)
        val dy = direccionY.coerceIn(-1.0, 1.0)
        if (dx != 0.0) {
            direccionHorizontal = if (dx < 0.0) -1.0 else 1.0
        }

        val velocidad = submarino.velocidad.coerceAtLeast(VELOCIDAD_MINIMA)
        posicionX = (posicionX + dx * velocidad * deltaSegundos)
            .coerceIn(0.0, anchoMapa)
        posicionY = (posicionY + dy * velocidad * deltaSegundos)
            .coerceIn(0.0, altoMapa)

        val intensidadMovimiento = abs(dx) + abs(dy)
        val consumo = (
            CONSUMO_OXIGENO_BASE +
                CONSUMO_OXIGENO_MOVIMIENTO * intensidadMovimiento
            ) * deltaSegundos
        oxigeno = (oxigeno - consumo).coerceIn(OXIGENO_MINIMO, oxigenoMaximo)

        if (oxigeno <= OXIGENO_MINIMO) {
            aplicarDanioAsfixia(deltaSegundos)
            registrarDestruccion("Oxígeno agotado")
            return
        }

        aplicarDanioProfundidad(deltaSegundos)
        if (casco.integridad <= INTEGRIDAD_MINIMA) {
            registrarDestruccion("Casco colapsado por sobrepresión")
        }
    }

    /**
     * Intenta minar el nodo más cercano dentro del radio de interacción.
     */
    fun minar(): ResultadoRecoleccion {
        if (destruido) {
            mensaje = "El submarino está destruido. No puedes minar."
            return ResultadoRecoleccion.SinColision
        }
        if (submarino.estaAcoplado) {
            mensaje = "Estás acoplado en la estación. Desacopla para volver a minar."
            return ResultadoRecoleccion.SinColision
        }

        val posicion = Posicion(posicionX.roundToInt(), posicionY.roundToInt())
        val resultado = servicioRecoleccion.procesarRecoleccion(posicion, minerales)
        mensaje = if (resultado is ResultadoRecoleccion.SinColision) {
            "No hay minerales cerca. Acércate a un nodo y pulsa ESPACIO."
        } else {
            resultado.obtenerMensaje()
        }
        return resultado
    }

    /**
     * Acopla la nave si se encuentra dentro de la zona de la estación base.
     */
    fun acoplar(): Boolean {
        if (destruido) {
            mensaje = "El submarino está destruido. No puedes acoplar."
            return false
        }
        if (submarino.estaAcoplado) {
            return true
        }
        val exito = estacion.acoplar(submarino, posicionX, posicionY)
        mensaje = if (exito) {
            "Acoplado a la Estación Base. Tienda de mejoras disponible."
        } else {
            "Acércate a la Estación Base en la superficie (Y ≈ 0) para acoplar."
        }
        return exito
    }

    /**
     * Libera la nave de la estación y reanuda la navegación.
     */
    fun desacoplar(): Boolean {
        val exito = estacion.desacoplar()
        if (exito) {
            mensaje = "Desacoplado. Continúa la exploración abisal."
        }
        return exito
    }

    /**
     * Vende el contenido completo de la bodega mientras la nave está acoplada.
     */
    fun venderMinerales(): ResultadoComercio {
        val resultado = servicioComercio.venderBodegaSubmarino(submarino)
        mensaje = resultado.obtenerResumen()
        return resultado
    }

    /**
     * Recarga el oxígeno al máximo descontando monedas de la billetera.
     */
    fun recargarOxigeno(): String {
        if (destruido) {
            mensaje = "El submarino está destruido. La estación no puede recargarlo."
            return mensaje
        }
        if (oxigeno >= oxigenoMaximo - UMBRAL_COMPLETO) {
            mensaje = "El oxígeno ya está al máximo."
            return mensaje
        }
        if (!billetera.debitar(COSTO_RECARGA_OXIGENO)) {
            mensaje = "Monedas insuficientes. Recarga cuesta $$COSTO_RECARGA_OXIGENO y tienes $${billetera.saldo}."
            return mensaje
        }
        oxigeno = oxigenoMaximo
        mensaje = "Oxígeno recargado al máximo (-$$COSTO_RECARGA_OXIGENO)."
        return mensaje
    }

    /**
     * Repara la integridad del casco al máximo descontando monedas.
     */
    fun repararCasco(): String {
        if (destruido) {
            mensaje = "El casco está destruido. La estación no puede repararlo."
            return mensaje
        }
        if (casco.integridad >= CascoBase.INTEGRIDAD_MAXIMA - UMBRAL_COMPLETO) {
            mensaje = "El casco ya está en perfecto estado."
            return mensaje
        }
        if (!billetera.debitar(COSTO_REPARACION_CASCO)) {
            mensaje = "Monedas insuficientes. Reparación cuesta $$COSTO_REPARACION_CASCO y tienes $${billetera.saldo}."
            return mensaje
        }
        casco.reparar(CascoBase.INTEGRIDAD_MAXIMA)
        mensaje = "Casco reparado al 100% (-$$COSTO_REPARACION_CASCO)."
        return mensaje
    }

    /**
     * Compra una mejora del catálogo y la aplica al submarino.
     */
    fun comprarMejora(idMejora: String): ResultadoCompra {
        val resultado = tienda.comprarParaSubmarino(idMejora, submarino)
        mensaje = resultado.obtenerMensaje()
        return resultado
    }

    /**
     * Instantánea inmutable para el HUD y la vista del mundo.
     */
    fun estado(): EstadoJuego = EstadoJuego(
        posicionX = posicionX,
        profundidad = posicionY,
        oxigeno = oxigeno,
        capacidadOxigeno = oxigenoMaximo,
        integridadCasco = casco.integridad,
        monedas = billetera.saldo,
        pesoBodega = bodega.pesoActual,
        capacidadBodega = bodega.capacidadMaxima,
        cantidadItems = bodega.cantidadItems,
        nodosDisponibles = nodos.count { !it.recolectado },
        enZonaAcople = estacion.estaEnZonaDeAcople(posicionX, posicionY),
        acoplado = submarino.estaAcoplado,
        velocidad = submarino.velocidad,
        direccionHorizontal = direccionHorizontal,
        mensaje = mensaje,
        destruido = destruido,
        causaDestruccion = causaDestruccion,
        presionActual = presionActual(),
        presionMaxima = casco.presionMaximaPa
    )

    fun nodos(): List<NodoMineral> = nodos

    fun terreno(): TerrenoSubmarino = terreno

    fun catalogo(): List<Mejora> = catalogo.obtenerTodas()

    private fun crearCatalogo(): CatalogoUpgrades {
        val catalogo = FabricaMejoras.crearCatalogoEstandar()
        catalogo.eliminar(FabricaMejoras.ID_ASCENSO)
        catalogo.registrar(crearMejoraOxigeno())
        return catalogo
    }

    private fun crearMejoraOxigeno(): Mejora = Mejora(
        id = ID_MEJORA_OXIGENO,
        nombre = "Tanque de Oxígeno Abisal",
        descripcion = "Amplía la reserva de oxígeno en +${INCREMENTO_OXIGENO_POR_MEJORA.toInt()} puntos por nivel.",
        nivelInicial = 1,
        nivelMaximo = 5,
        efecto = object : EfectoBase(INCREMENTO_OXIGENO_POR_MEJORA) {
            override fun aplicar(submarino: SubmarinoMejorable) {
                aumentarOxigenoMaximo(magnitud)
            }
        },
        estrategiaCosto = EstrategiaCostoLineal(COSTO_BASE_OXIGENO, INCREMENTO_COSTO_OXIGENO)
    )

    private fun aumentarOxigenoMaximo(incremento: Double) {
        oxigenoMaximo = (oxigenoMaximo + incremento).coerceAtMost(OXIGENO_MAXIMO_ABSOLUTO)
        oxigeno = oxigeno.coerceAtMost(oxigenoMaximo)
    }

    private fun presionActual(): Double = PRESION_POR_METRO * posicionY

    private fun aplicarDanioProfundidad(deltaSegundos: Double) {
        val presionMaxima = casco.presionMaximaPa
        val presionActual = presionActual()
        if (presionActual <= presionMaxima) {
            return
        }
        val excesoRelativo = ((presionActual - presionMaxima) / presionMaxima)
            .coerceIn(0.0, 1.0)
        val danio = excesoRelativo * DANIO_POR_EXCESO * deltaSegundos
        if (danio > 0.0) {
            casco.recibirPresion(presionMaxima * (1.0 + danio))
        }
    }

    private fun aplicarDanioAsfixia(deltaSegundos: Double) {
        val danio = DANIO_POR_ASFIXIA * deltaSegundos
        casco.recibirPresion(casco.presionMaximaPa * (1.0 + danio))
    }

    private fun registrarDestruccion(causa: String) {
        if (destruido) {
            return
        }
        destruido = true
        causaDestruccion = causa
        mensaje = "GAME OVER · $causa. El submarino ha sido destruido."
    }

    private fun generarNodos(): List<NodoMineral> {
        val random = Random(semilla + 991L)
        val generados = mutableListOf<NodoMineral>()
        var indice = 0
        var intentos = 0

        generados.add(crearNodo(TipoNodoMineral.COBRE, 2500.0, 300.0, "INICIO-COBRE"))
        generados.add(crearNodo(TipoNodoMineral.COBRE, 2200.0, 650.0, "INICIO-COBRE-2"))
        generados.add(crearNodo(TipoNodoMineral.HIERRO, 2650.0, 1450.0, "INICIO-HIERRO"))

        val formaciones = terreno.todas
        while (generados.size < CANTIDAD_NODOS && intentos < MAX_INTENTOS_GENERACION) {
            intentos++
            val formacion = formaciones[random.nextInt(formaciones.size)]
            val margen = (formacion.xMax - formacion.xMin) * 0.03
            val x = random.nextDouble(formacion.xMin + margen, formacion.xMax - margen)
            val y = formacion.superficieEn(x) ?: continue
            if (y < PROFUNDIDAD_MINIMA_NODO || y > altoMapa - MINIMO_MARGEN_FONDO) {
                continue
            }
            if (estaCercaDeLaEstacion(x, y) || estaDemasiadoCerca(generados, x, y)) {
                continue
            }
            val tipo = tipoSegunProfundidad(y)
            generados.add(crearNodo(tipo, x, y, "NODO-${tipo.name}-${indice++}"))
        }

        return generados
    }

    private fun tipoSegunProfundidad(profundidad: Double): TipoNodoMineral = when {
        profundidad < LIMITE_COBRE -> TipoNodoMineral.COBRE
        profundidad < LIMITE_HIERRO -> TipoNodoMineral.HIERRO
        else -> TipoNodoMineral.ORO
    }

    private fun estaDemasiadoCerca(
        nodos: List<NodoMineral>,
        x: Double,
        y: Double
    ): Boolean = nodos.any {
        hypot(it.x - x, it.y - y) < DISTANCIA_MINIMA_ENTRE_NODOS
    }

    private fun crearNodo(
        tipo: TipoNodoMineral,
        x: Double,
        y: Double,
        id: String
    ): NodoMineral {
        val mineral = Mineral(
            id = id,
            tipo = tipo.tipoMineral,
            posX = x.roundToInt(),
            posY = y.roundToInt()
        )
        return NodoMineral(
            id = id,
            tipo = tipo,
            mineral = mineral
        )
    }

    private fun estaCercaDeLaEstacion(x: Double, y: Double): Boolean =
        x in (ESTACION_X - ESTACION_ANCHO / 2.0 - 150.0)..(ESTACION_X + ESTACION_ANCHO / 2.0 + 150.0) &&
            y < PROFUNDIDAD_ZONA_ACOPLE + 120.0

    companion object {
        const val ANCHO_MAPA = 5000.0
        const val ALTO_MAPA = 3000.0
        const val ESTACION_X = 2500.0
        const val ESTACION_ANCHO = 400.0
        const val PROFUNDIDAD_ZONA_ACOPLE = 120.0
        const val RADIO_INTERACCION = 65.0

        const val COSTO_RECARGA_OXIGENO = 60
        const val COSTO_REPARACION_CASCO = 100
        const val ID_MEJORA_OXIGENO = "UPG-OXIGENO"
        const val DISTANCIA_MINIMA_ENTRE_NODOS = 400.0

        private const val SEMILLA_DEFAULT = 2026L
        private const val CANTIDAD_NODOS = 36
        private const val MAX_INTENTOS_GENERACION = 20_000
        private const val PROFUNDIDAD_MINIMA_NODO = 120.0
        private const val MINIMO_MARGEN_FONDO = 25.0
        private const val LIMITE_COBRE = 1350.0
        private const val LIMITE_HIERRO = 2250.0
        private const val INTEGRIDAD_MINIMA = 0.0
        private const val PROFUNDIDAD_INICIAL = 220.0
        private const val VELOCIDAD_INICIAL = 240.0
        private const val VELOCIDAD_MINIMA = 80.0
        private const val VELOCIDAD_ASCENSO_INICIAL = 180.0
        private const val PESO_BASE = 1000.0
        private const val CAPACIDAD_INICIAL_BODEGA = 10.0
        private const val SALDO_INICIAL = 0
        private const val PRESION_MAXIMA_INICIAL = 20_000_000.0
        private const val PRESION_POR_METRO = 1025.0 * 9.81
        private const val DANIO_POR_EXCESO = 6.0
        private const val DANIO_POR_ASFIXIA = 0.5
        private const val CONSUMO_OXIGENO_BASE = 1.0
        private const val CONSUMO_OXIGENO_MOVIMIENTO = 0.8
        private const val OXIGENO_MAXIMO_INICIAL = 100.0
        private const val OXIGENO_MAXIMO_ABSOLUTO = 200.0
        private const val OXIGENO_MINIMO = 0.0
        private const val INCREMENTO_OXIGENO_POR_MEJORA = 20.0
        private const val COSTO_BASE_OXIGENO = 150
        private const val INCREMENTO_COSTO_OXIGENO = 85
        private const val UMBRAL_COMPLETO = 0.01
    }
}
