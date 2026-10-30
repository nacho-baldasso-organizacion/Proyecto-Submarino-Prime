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
 *
 * Encapsula la totalidad de sus campos con visibilidad privada o 'private set'.
 * Hereda de [EntidadFisica] y compone un [Casco] mediante interfaz (DIP, OCP).
 * Implementa [Navegable], [Pesable] y [Acoplable] respetando Segregación de Interfaces (ISP).
 */
class Submarino(
    posX: Double = 0.0,
    posY: Double = 0.0,
    velocidad: Double = 0.0,
    casco: Casco,
    pesoBase: Double = PESO_BASE_DEFAULT,
    bodega: Almacenamiento? = Bodega(capacidadMaxima = BODEGA_CAPACIDAD_DEFAULT),
    velocidadAscensoBase: Double = if (velocidad > 0.0) velocidad else VELOCIDAD_ASCENSO_DEFAULT,
    calculadorPeso: CalculadorPeso = CalculadorPesoSubmarino(),
    estrategiaAscenso: EstrategiaAscenso = EstrategiaAscensoLineal(),
    billetera: Billetera = Monedero(0),
    notificador: Notificador = NotificadorConsola
) : EntidadFisica(posX, posY, velocidad), Navegable, Pesable, Acoplable {

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

    var bodega: Almacenamiento? = bodega
        private set

    var billetera: Billetera = billetera
        private set

    // Dependency injection through abstractions - DIP
    // All collaborator types are interfaces, concretions provided by caller

    private val energia: SistemaEnergia = SistemaEnergia(100.0, CONSUMO_BATERIA_POR_METRO)
    private val acople: ControladorAcople = ControladorAcople()
    private val propulsion: SistemaPropulsion = SistemaPropulsion(velocidadAscensoBase)
    private val sistemaCasco: SistemaCasco = SistemaCasco(casco)
    private val gestorAscenso: GestorAscenso = GestorAscenso(calculadorPeso, estrategiaAscenso)
    private val gestorBodega: GestorBodega = GestorBodega()
    private val gestorVelocidad: GestorVelocidadNavegacion = GestorVelocidadNavegacion(
        velocidadActual = { this.velocidad },
        actualizarVelocidad = { actualizarVelocidad(it) }
    )
    private val movimiento: GestorMovimiento = GestorMovimiento(
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
        get() = posY <= 0.0

    override fun acoplar() {
        acople.acoplar(!estaDestruido)
    }

    override fun desacoplar() {
        acople.desacoplar()
    }

    fun estaEnZonaAcople(zona: ZonaAcople): Boolean = zona.estaEnZona(posX, posY)

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

    fun aumentarCapacidadBodega(incremento: Double) {
        gestorBodega.aumentarCapacidad(bodega, incremento)
    }

    fun aumentarVelocidadAscenso(incremento: Double) {
        propulsion.aumentarVelocidadAscenso(incremento)
    }

    fun aumentarResistenciaCasco(incrementoPa: Double) {
        sistemaCasco.reforzar(incrementoPa)
    }

    fun aumentarVelocidadPropulsion(incremento: Double) {
        gestorVelocidad.aumentarVelocidad(incremento)
    }

    override fun mover(deltaX: Double, deltaY: Double): Boolean =
        movimiento.mover(deltaX, deltaY)

    fun verificarIntegridad(presionActualPa: Double): Double =
        sistemaCasco.recibirPresion(presionActualPa)

    fun verificarIntegridadConCalculador(calculador: CalculadorPresion): Double =
        sistemaCasco.recibirPresionDesde(calculador, posY)

    override fun navegar(deltaX: Double): Boolean = mover(deltaX = deltaX, deltaY = 0.0)

    override fun sumergir(deltaY: Double): Boolean = mover(deltaX = 0.0, deltaY = deltaY)

    override fun ascender(deltaY: Double): Boolean {
        val desplazamientoEfectivo =
            gestorAscenso.desplazamientoAscenso(deltaY, pesoBase, bodega)
        return mover(deltaX = 0.0, deltaY = -desplazamientoEfectivo)
    }

    fun ascender(): Boolean = mover(deltaX = 0.0, deltaY = -velocidadAscenso)

    fun obtenerCasco(): Casco = casco

    fun obtenerBodega(): Almacenamiento? = bodega

    fun obtenerBilletera(): Billetera = billetera

    fun obtenerBateria(): Double = bateria

    override fun toString(): String {
        return "Submarino(posX=$posX, posY=$posY, pesoTotal=${"%.2f".format(pesoTotal)}kg, " +
                "velocidadAscenso=${"%.2f".format(velocidadAscenso)}m/s, saldo=$${billetera.saldo}, " +
                "estaAcoplado=$estaAcoplado, bateria=${"%.2f".format(bateria)}%, estaDestruido=$estaDestruido, casco=$casco)"
    }
}
