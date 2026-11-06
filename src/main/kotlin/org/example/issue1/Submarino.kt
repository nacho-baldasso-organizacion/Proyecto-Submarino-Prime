package submarino

/**
 * Clase orquestadora principal que representa al submarino.
 *
 * Delega cada responsabilidad secundaria en subsistemas especializados:
 * - [SistemaEnergia]: batería y consumo.
 * - [ControladorAcople]: estado de amarre.
 * - [SistemaPropulsion]: velocidad de ascenso.
 * - [SistemaCasco]: integridad y refuerzo del casco.
 * - [GestorAscenso]: peso y dinámica de ascenso.
 * - [GestorBodega]: ampliación de capacidad de carga.
 * - [GestorVelocidadNavegacion]: propulsión horizontal.
 * - [GestorMovimiento]: ejecución y validación del desplazamiento.
 * - [NavegadorSubmarino]: maniobras de navegación.
 * - [GestorMejorasSubmarino]: aplicación de mejoras.
 * - [FormateadorEstadoSubmarino]: representación textual del estado.
 *
 * Encapsula la totalidad de sus campos con visibilidad privada o 'private set'.
 * Hereda de [EntidadFisica] y compone un [Casco] mediante interfaz (DIP, OCP).
 * Implementa [Navegable], [Pesable], [Acoplable], [NaveComercial] y [SubmarinoMejorable]
 * respetando Segregación de Interfaces (ISP) e Inversión de Dependencias (DIP).
 */
class Submarino(
    posX: Double = 0.0,
    posY: Double = 0.0,
    velocidad: Double = 0.0,
    casco: Casco,
    pesoBase: Double = PESO_BASE_DEFAULT,
    bodega: Almacenamiento? = null,
    velocidadAscensoBase: Double = if (velocidad > 0.0) velocidad else VELOCIDAD_ASCENSO_DEFAULT,
    calculadorPeso: CalculadorPeso? = null,
    estrategiaAscenso: EstrategiaAscenso? = null,
    billetera: Billetera? = null,
    notificador: Notificador = NotificadorConsola,
    private val factoria: FabricaSubsistemasSubmarino = FabricaSubsistemasEstandar
) : EntidadFisica(posX, posY, velocidad), Navegable, Pesable, Acoplable, NaveComercial, SubmarinoMejorable {

    companion object {
        const val PESO_BASE_DEFAULT: Double = 1000.0
        const val BODEGA_CAPACIDAD_DEFAULT: Double = 50.0
        const val VELOCIDAD_ASCENSO_DEFAULT: Double = 5.0
        const val CONSUMO_BATERIA_POR_METRO: Double = 0.5
    }

    var casco: Casco = casco
        private set

    var pesoBase: Double = pesoBase
        private set

    override var bodega: Almacenamiento? = bodega
        private set

    override var billetera: Billetera = billetera ?: factoria.crearBilletera(0)
        private set

    // Dependency injection through abstractions - DIP
    // All collaborator types are interfaces, concretions provided by caller

    private val energia: SistemaEnergia = factoria.crearEnergia(100.0, CONSUMO_BATERIA_POR_METRO)
    private val acople: ControladorAcople = factoria.crearAcople()
    private val propulsion: SistemaPropulsion = factoria.crearPropulsion(velocidadAscensoBase)
    private val sistemaCasco: SistemaCasco = factoria.crearSistemaCasco(casco)
    private val gestorAscenso: GestorAscenso = factoria.crearGestorAscenso(
        calculadorPeso = calculadorPeso ?: factoria.crearCalculadorPeso(),
        estrategiaAscenso = estrategiaAscenso ?: factoria.crearEstrategiaAscenso()
    )
    private val gestorBodega: GestorBodega = factoria.crearGestorBodega()
    private val gestorVelocidad: GestorVelocidadNavegacion = factoria.crearGestorVelocidad(
        velocidadActual = { this.velocidad },
        actualizarVelocidad = { actualizarVelocidad(it) }
    )
    private val movimiento: GestorMovimiento = factoria.crearMovimiento(
        energia = energia,
        acople = acople,
        notificador = notificador,
        estaDestruido = { sistemaCasco.estaDestruido },
        posicionActual = { this.posX to this.posY },
        actualizarPosicion = { x, y ->
            actualizarPosX(x)
            actualizarPosY(y)
        }
    )
    private val navegador: NavegadorSubmarino = factoria.crearNavegador(
        movimiento = movimiento,
        gestorAscenso = gestorAscenso,
        posicionActual = { this.posX to this.posY },
        velocidadAscensoActual = { this.velocidadAscenso },
        pesoBaseActual = { this.pesoBase },
        bodegaActual = { this.bodega }
    )
    private val gestorMejoras: GestorMejorasSubmarino = factoria.crearGestorMejoras(
        propulsion = propulsion,
        sistemaCasco = sistemaCasco,
        gestorBodega = gestorBodega,
        gestorVelocidad = gestorVelocidad,
        bodegaActual = { this.bodega }
    )
    private val formateadorEstado: FormateadorEstadoSubmarino = factoria.crearFormateadorEstado()

    init {
        require(pesoBase >= 0.0) { "El peso base no puede ser negativo: $pesoBase" }
    }

    val velocidadAscensoBase: Double
        get() = propulsion.velocidadAscensoBase

    val bateria: Double
        get() = energia.bateria

    val estaDestruido: Boolean
        get() = sistemaCasco.estaDestruido

    override val estaAcoplado: Boolean
        get() = acople.estaAcoplado

    val estaEnSuperficie: Boolean
        get() = navegador.estaEnSuperficie()

    override fun acoplar() {
        acople.acoplar(!estaDestruido)
    }

    override fun desacoplar() {
        acople.desacoplar()
    }

    fun estaEnZonaAcople(zona: ZonaAcople): Boolean = navegador.estaEnZona(zona)

    val pesoTotal: Double
        get() = gestorAscenso.pesoTotal(pesoBase, bodega)

    override val peso: Double
        get() = pesoTotal

    val velocidadAscenso: Double
        get() = gestorAscenso.velocidadAscenso(velocidadAscensoBase, pesoBase, bodega)

    val factorAscenso: Double
        get() = gestorAscenso.factorAscenso(pesoBase, bodega)

    fun calcularPesoTotal(): Double = pesoTotal

    fun calcularVelocidadAscenso(): Double = velocidadAscenso

    fun obtenerEstadoAscenso(): EstadoAscenso =
        gestorAscenso.estadoAscenso(pesoBase, bodega, velocidadAscensoBase)

    override fun aumentarCapacidadBodega(incremento: Double) {
        gestorMejoras.aumentarCapacidadBodega(incremento)
    }

    override fun aumentarVelocidadAscenso(incremento: Double) {
        gestorMejoras.aumentarVelocidadAscenso(incremento)
    }

    override fun aumentarResistenciaCasco(incrementoPa: Double) {
        gestorMejoras.aumentarResistenciaCasco(incrementoPa)
    }

    override fun aumentarVelocidadPropulsion(incremento: Double) {
        gestorMejoras.aumentarVelocidadPropulsion(incremento)
    }

    override fun mover(deltaX: Double, deltaY: Double): Boolean =
        navegador.mover(deltaX, deltaY)

    fun verificarIntegridad(presionActualPa: Double): Double =
        sistemaCasco.recibirPresion(presionActualPa)

    fun verificarIntegridadConCalculador(calculador: CalculadorPresion): Double =
        sistemaCasco.recibirPresionDesde(calculador, posY)

    override fun navegar(deltaX: Double): Boolean = navegador.navegar(deltaX)

    override fun sumergir(deltaY: Double): Boolean = navegador.sumergir(deltaY)

    override fun ascender(deltaY: Double): Boolean = navegador.ascender(deltaY)

    fun ascender(): Boolean = navegador.ascender()

    fun obtenerCasco(): Casco = casco

    fun obtenerBodega(): Almacenamiento? = bodega

    fun obtenerBilletera(): Billetera = billetera

    fun obtenerBateria(): Double = bateria

    private fun estadoActual(): EstadoSubmarino = factoria.crearEstadoSubmarino(
        posX = posX,
        posY = posY,
        pesoTotal = pesoTotal,
        velocidadAscenso = velocidadAscenso,
        saldo = billetera.saldo,
        estaAcoplado = estaAcoplado,
        bateria = bateria,
        estaDestruido = estaDestruido,
        casco = casco
    )

    override fun toString(): String = formateadorEstado.formatear(estadoActual())
}
