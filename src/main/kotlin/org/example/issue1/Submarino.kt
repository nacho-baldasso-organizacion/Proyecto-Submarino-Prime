package submarino

/**
 * Subsistema especializado en la gestión de energía y batería (SRP).
 */
class SistemaEnergia(
    bateriaInicial: Double = 100.0,
    consumoPorMetro: Double = 0.5
) {
    var bateria: Double = bateriaInicial
        private set

    private var tasaConsumo: Double = consumoPorMetro

    val estaAgotada: Boolean
        get() = bateria <= 0.0

    fun consumirPorDistancia(distancia: Double): Double {
        val consumo = distancia * tasaConsumo
        bateria = maxOf(bateria - consumo, 0.0)
        return bateria
    }

    fun recargar(cantidad: Double) {
        require(cantidad > 0.0) { "La recarga debe ser positiva: $cantidad" }
        bateria = minOf(bateria + cantidad, 100.0)
    }
}

/**
 * Subsistema especializado en el control de estado de amarre y acople (SRP).
 */
class ControladorAcople {
    var estaAcoplado: Boolean = false
        private set

    fun acoplar(puedeAcoplar: Boolean): Boolean {
        if (puedeAcoplar) {
            estaAcoplado = true
            return true
        }
        return false
    }

    fun desacoplar(): Boolean {
        if (estaAcoplado) {
            estaAcoplado = false
            return true
        }
        return false
    }

    fun sincronizarConMovimiento(deltaX: Double, deltaY: Double) {
        if (estaAcoplado && (deltaX != 0.0 || deltaY != 0.0)) {
            desacoplar()
        }
    }
}

/**
 * Subsistema especializado en el control de motores y velocidad de ascenso vertical (SRP).
 */
class SistemaPropulsion(
    velocidadAscensoBase: Double = 5.0
) {
    var velocidadAscensoBase: Double = velocidadAscensoBase
        private set

    init {
        require(velocidadAscensoBase >= 0.0) {
            "La velocidad de ascenso base no puede ser negativa: $velocidadAscensoBase"
        }
    }

    fun aumentarVelocidadAscenso(incremento: Double) {
        require(incremento > 0.0) {
            "El incremento de velocidad de ascenso debe ser positivo: $incremento"
        }
        velocidadAscensoBase += incremento
    }
}

/**
 * Clase orquestadora principal que representa al submarino.
 * Descompuesta en subsistemas especializados ([SistemaEnergia], [ControladorAcople], [SistemaPropulsion])
 * para cumplir estrictamente con SRP eliminando la God Class.
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

    private val notificador: Notificador = notificador
    private val calculadorPeso: CalculadorPeso = calculadorPeso
    private val estrategiaAscenso: EstrategiaAscenso = estrategiaAscenso

    private val energia: SistemaEnergia = SistemaEnergia(100.0, CONSUMO_BATERIA_POR_METRO)
    private val acople: ControladorAcople = ControladorAcople()
    private val propulsion: SistemaPropulsion = SistemaPropulsion(velocidadAscensoBase)

    init {
        require(pesoBase >= 0.0) { "El peso base no puede ser negativo: $pesoBase" }
    }

    val velocidadAscensoBase: Double
        get() = propulsion.velocidadAscensoBase

    val bateria: Double
        get() = energia.bateria

    val estaDestruido: Boolean
        get() = casco.estaDestruido

    override val estaAcoplado: Boolean
        get() = acople.estaAcoplado

    val estaEnSuperficie: Boolean
        get() = posY <= 0.0

    override fun acoplar() {
        if (!estaDestruido) {
            acople.acoplar(true)
        }
    }

    override fun desacoplar() {
        acople.desacoplar()
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
        propulsion.aumentarVelocidadAscenso(incremento)
    }

    fun aumentarResistenciaCasco(incrementoPa: Double) {
        require(incrementoPa > 0.0) { "El incremento de resistencia debe ser positivo: $incrementoPa" }
        casco.reforzar(incrementoPa)
    }

    fun aumentarVelocidadPropulsion(incremento: Double) {
        require(incremento > 0.0) { "El incremento de velocidad de propulsión debe ser positivo: $incremento" }
        actualizarVelocidad(velocidad + incremento)
    }

    override fun mover(deltaX: Double, deltaY: Double): Boolean {
        if (estaDestruido) {
            notificador.notificar("El submarino está destruido y no puede moverse.")
            return false
        }
        if (energia.estaAgotada) {
            notificador.notificar("Batería agotada. El submarino no puede moverse.")
            return false
        }

        val distancia = Math.hypot(deltaX, deltaY)
        energia.consumirPorDistancia(distancia)

        actualizarPosX(posX + deltaX)
        actualizarPosY(posY + deltaY)

        acople.sincronizarConMovimiento(deltaX, deltaY)

        if (energia.estaAgotada) {
            notificador.notificar("¡Batería agotada tras el movimiento!")
        }

        return true
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
