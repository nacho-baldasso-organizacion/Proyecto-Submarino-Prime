package submarino

/**
 * Clase principal que representa al submarino.
 * Hereda de [EntidadFisica] y compone un [Casco] mediante interfaz (DIP, OCP).
 * Implementa [Navegable] cumpliendo con la Segregación de Interfaces (ISP).
 * Gestiona batería, desplazamiento y delega el estado estructural al casco.
 */
class Submarino(
    posX: Double = 0.0,
    posY: Double = 0.0,
    velocidad: Double = 0.0,
    val casco: Casco,
    val pesoBase: Double = PESO_BASE_DEFAULT,
    val bodega: Almacenamiento? = Bodega(capacidadMaxima = BODEGA_CAPACIDAD_DEFAULT),
    velocidadAscensoBase: Double = if (velocidad > 0.0) velocidad else VELOCIDAD_ASCENSO_DEFAULT,
    private val calculadorPeso: CalculadorPeso = CalculadorPesoSubmarino(),
    private val estrategiaAscenso: EstrategiaAscenso = EstrategiaAscensoLineal(),
    val billetera: Billetera = Monedero(0),
    private val notificador: Notificador = NotificadorConsola
) : EntidadFisica(posX, posY, velocidad), Navegable, Pesable, Acoplable {

    companion object {
        const val PESO_BASE_DEFAULT: Double = 1000.0
        const val BODEGA_CAPACIDAD_DEFAULT: Double = 50.0
        const val VELOCIDAD_ASCENSO_DEFAULT: Double = 5.0
        const val CONSUMO_BATERIA_POR_METRO: Double = 0.5
    }

    var velocidadAscensoBase: Double = velocidadAscensoBase
        private set

    init {
        require(pesoBase >= 0.0) { "El peso base no puede ser negativo: $pesoBase" }
        require(velocidadAscensoBase >= 0.0) { "La velocidad de ascenso base no puede ser negativa: $velocidadAscensoBase" }
    }

    var bateria: Double = 100.0
        private set

    val estaDestruido: Boolean
        get() = casco.estaDestruido

    override var estaAcoplado: Boolean = false
        private set

    val estaEnSuperficie: Boolean
        get() = posY <= 0.0

    override fun acoplar() {
        if (!estaDestruido) {
            estaAcoplado = true
        }
    }

    override fun desacoplar() {
        estaAcoplado = false
    }

    fun estaEnZonaAcople(zona: ZonaAcople): Boolean = zona.estaEnZona(posX, posY)

    val pesoTotal: Double
        get() = calculadorPeso.calcularPesoTotal(pesoBase, bodega)

    override val peso: Double
        get() = pesoTotal

    val velocidadAscenso: Double
        get() = estrategiaAscenso.calcularVelocidadAscenso(
            velocidadBase = velocidadAscensoBase,
            pesoTotal = pesoTotal,
            pesoBase = pesoBase,
            capacidadCargaMaxima = bodega?.capacidadMaxima ?: 0.0
        )

    val factorAscenso: Double
        get() = estrategiaAscenso.calcularFactorVelocidad(
            pesoTotal = pesoTotal,
            pesoBase = pesoBase,
            capacidadCargaMaxima = bodega?.capacidadMaxima ?: 0.0
        )

    fun calcularPesoTotal(): Double = pesoTotal

    fun calcularVelocidadAscenso(): Double = velocidadAscenso

    fun obtenerEstadoAscenso(): EstadoAscenso {
        val pesoCarga = bodega?.pesoActual ?: 0.0
        val capMax = bodega?.capacidadMaxima ?: 0.0
        return EstadoAscenso(
            pesoBase = pesoBase,
            pesoCarga = pesoCarga,
            pesoTotal = pesoTotal,
            capacidadMaximaBodega = capMax,
            factorVelocidad = factorAscenso,
            velocidadNominal = velocidadAscensoBase,
            velocidadAscensoEfectiva = velocidadAscenso
        )
    }

    fun aumentarCapacidadBodega(incremento: Double) {
        require(incremento > 0.0) { "El incremento de capacidad debe ser positivo: $incremento" }
        bodega?.aumentarCapacidad(incremento)
    }

    fun aumentarVelocidadAscenso(incremento: Double) {
        require(incremento > 0.0) { "El incremento de velocidad de ascenso debe ser positivo: $incremento" }
        velocidadAscensoBase += incremento
    }

    fun aumentarResistenciaCasco(incrementoPa: Double) {
        require(incrementoPa > 0.0) { "El incremento de resistencia debe ser positivo: $incrementoPa" }
        casco.reforzar(incrementoPa)
    }

    fun aumentarVelocidadPropulsion(incremento: Double) {
        require(incremento > 0.0) { "El incremento de velocidad de propulsión debe ser positivo: $incremento" }
        velocidad += incremento
    }

    override fun mover(deltaX: Double, deltaY: Double): Boolean {
        if (estaDestruido) {
            notificador.notificar("El submarino está destruido y no puede moverse.")
            return false
        }
        if (bateria <= 0.0) {
            notificador.notificar("Batería agotada. El submarino no puede moverse.")
            return false
        }

        val distancia = Math.hypot(deltaX, deltaY)
        actualizarBateria(distancia)

        actualizarPosX(posX + deltaX)
        actualizarPosY(posY + deltaY)

        if (estaAcoplado && (deltaX != 0.0 || deltaY != 0.0)) {
            desacoplar()
        }

        if (bateria <= 0.0) {
            notificador.notificar("¡Batería agotada tras el movimiento!")
        }

        return true
    }

    private fun actualizarBateria(distancia: Double) {
        val consumo = distancia * CONSUMO_BATERIA_POR_METRO
        bateria = maxOf(bateria - consumo, 0.0)
    }

    fun verificarIntegridad(presionActualPa: Double): Double = casco.recibirPresion(presionActualPa)

    fun verificarIntegridadConCalculador(calculador: CalculadorPresion): Double {
        val presion = calculador.calcularPresion(posY)
        return verificarIntegridad(presion)
    }

    override fun navegar(deltaX: Double): Boolean = mover(deltaX = deltaX, deltaY = 0.0)

    override fun sumergir(deltaY: Double): Boolean = mover(deltaX = 0.0, deltaY = deltaY)

    override fun ascender(deltaY: Double): Boolean {
        val desplazamientoEfectivo = estrategiaAscenso.calcularDesplazamientoAscenso(
            deltaYNominal = deltaY,
            pesoTotal = pesoTotal,
            pesoBase = pesoBase,
            capacidadCargaMaxima = bodega?.capacidadMaxima ?: 0.0
        )
        return mover(deltaX = 0.0, deltaY = -desplazamientoEfectivo)
    }

    fun ascender(): Boolean = mover(deltaX = 0.0, deltaY = -velocidadAscenso)

    override fun toString(): String {
        return "Submarino(posX=$posX, posY=$posY, pesoTotal=${"%.2f".format(pesoTotal)}kg, velocidadAscenso=${"%.2f".format(velocidadAscenso)}m/s, saldo=$${billetera.saldo}, estaAcoplado=$estaAcoplado, bateria=${"%.2f".format(bateria)}%, estaDestruido=$estaDestruido, casco=$casco)"
    }
}
