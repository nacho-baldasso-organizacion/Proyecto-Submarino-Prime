package submarino

import kotlin.math.hypot

/**
 * ============================================================================
 * SUBSISTEMAS ESPECIALIZADOS DEL SUBMARINO - PRINCIPIO DE RESPONSABILIDAD ÚNICA
 * ============================================================================
 * Cada colaborador encapsula una única responsabilidad y expone únicamente el
 * comportamiento necesario para que [Submarino] actúe como orquestador.
 */

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

    /**
     * Consume energía en función de la distancia euclidiana del desplazamiento.
     */
    fun consumirPorDesplazamiento(deltaX: Double, deltaY: Double): Double =
        consumirPorDistancia(hypot(deltaX, deltaY))

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
 * Subsistema especializado en la integridad y el refuerzo del casco (SRP).
 */
class SistemaCasco(
    private val casco: Casco
) {
    val estaDestruido: Boolean
        get() = casco.estaDestruido

    fun reforzar(incrementoPa: Double) {
        require(incrementoPa > 0.0) {
            "El incremento de resistencia debe ser positivo: $incrementoPa"
        }
        casco.reforzar(incrementoPa)
    }

    fun recibirPresion(presionActualPa: Double): Double = casco.recibirPresion(presionActualPa)

    fun recibirPresionDesde(calculador: CalculadorPresion, profundidad: Double): Double =
        recibirPresion(calculador.calcularPresion(profundidad))
}

/**
 * Subsistema especializado en el cálculo de peso y dinámica de ascenso (SRP).
 * Delega la física en [CalculadorPeso] y [EstrategiaAscenso] (DIP, OCP).
 */
class GestorAscenso(
    private val calculadorPeso: CalculadorPeso,
    private val estrategiaAscenso: EstrategiaAscenso
) {
    fun pesoTotal(pesoBase: Double, bodega: Almacenamiento?): Double =
        calculadorPeso.calcularPesoTotal(pesoBase, bodega)

    fun factorAscenso(pesoBase: Double, bodega: Almacenamiento?): Double =
        estrategiaAscenso.calcularFactorVelocidad(
            pesoTotal = pesoTotal(pesoBase, bodega),
            pesoBase = pesoBase,
            capacidadCargaMaxima = capacidadCarga(bodega)
        )

    fun velocidadAscenso(
        velocidadNominal: Double,
        pesoBase: Double,
        bodega: Almacenamiento?
    ): Double = estrategiaAscenso.calcularVelocidadAscenso(
        velocidadBase = velocidadNominal,
        pesoTotal = pesoTotal(pesoBase, bodega),
        pesoBase = pesoBase,
        capacidadCargaMaxima = capacidadCarga(bodega)
    )

    fun desplazamientoAscenso(
        deltaYNominal: Double,
        pesoBase: Double,
        bodega: Almacenamiento?
    ): Double = estrategiaAscenso.calcularDesplazamientoAscenso(
        deltaYNominal = deltaYNominal,
        pesoTotal = pesoTotal(pesoBase, bodega),
        pesoBase = pesoBase,
        capacidadCargaMaxima = capacidadCarga(bodega)
    )

    fun estadoAscenso(
        pesoBase: Double,
        bodega: Almacenamiento?,
        velocidadNominal: Double
    ): EstadoAscenso = EstadoAscenso(
        pesoBase = pesoBase,
        pesoCarga = bodega?.pesoActual ?: 0.0,
        pesoTotal = pesoTotal(pesoBase, bodega),
        capacidadMaximaBodega = capacidadCarga(bodega),
        factorVelocidad = factorAscenso(pesoBase, bodega),
        velocidadNominal = velocidadNominal,
        velocidadAscensoEfectiva = velocidadAscenso(velocidadNominal, pesoBase, bodega)
    )

    private fun capacidadCarga(bodega: Almacenamiento?): Double = bodega?.capacidadMaxima ?: 0.0
}

/**
 * Subsistema especializado en la ampliación de la capacidad de carga (SRP).
 */
class GestorBodega {
    fun aumentarCapacidad(bodega: Almacenamiento?, incremento: Double) {
        require(incremento > 0.0) {
            "El incremento de capacidad debe ser positivo: $incremento"
        }
        bodega?.aumentarCapacidad(incremento)
    }
}

/**
 * Subsistema especializado en el incremento de la velocidad de propulsión horizontal (SRP).
 */
class GestorVelocidadNavegacion(
    private val velocidadActual: () -> Double,
    private val actualizarVelocidad: (Double) -> Unit
) {
    fun aumentarVelocidad(incremento: Double) {
        require(incremento > 0.0) {
            "El incremento de velocidad de propulsión debe ser positivo: $incremento"
        }
        actualizarVelocidad(velocidadActual() + incremento)
    }
}

/**
 * Subsistema especializado en la ejecución y validación del movimiento (SRP).
 * Coordina energía, acople, notificaciones y actualización de posición.
 */
class GestorMovimiento(
    private val energia: SistemaEnergia,
    private val acople: ControladorAcople,
    private val notificador: Notificador,
    private val estaDestruido: () -> Boolean,
    private val posicionActual: () -> Pair<Double, Double>,
    private val actualizarPosicion: (Double, Double) -> Unit
) {
    fun mover(deltaX: Double, deltaY: Double): Boolean {
        if (estaDestruido()) {
            notificador.notificar("El submarino está destruido y no puede moverse.")
            return false
        }
        if (energia.estaAgotada) {
            notificador.notificar("Batería agotada. El submarino no puede moverse.")
            return false
        }

        energia.consumirPorDesplazamiento(deltaX, deltaY)

        val (x, y) = posicionActual()
        actualizarPosicion(x + deltaX, y + deltaY)

        acople.sincronizarConMovimiento(deltaX, deltaY)

        if (energia.estaAgotada) {
            notificador.notificar("¡Batería agotada tras el movimiento!")
        }

        return true
    }
}
