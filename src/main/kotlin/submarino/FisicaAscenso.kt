package submarino

/**
 * ============================================================================
 * FÍSICA DE ASCENSO Y CÁLCULO DE MASA - PRINCIPIOS SOLID (SRP, OCP, ISP, DIP)
 * Issue #6: Cálculo de peso total y física de flotabilidad/ascenso del submarino
 * ============================================================================
 */

/**
 * Representa una instantánea del estado dinámico de peso y ascenso del submarino.
 * Cumple con POO idiomática en Kotlin, encapsulamiento estricto (campos con `private set`)
 * e incorpora métodos de dominio con comportamiento propio (no anémica).
 */
class EstadoAscenso(
    pesoBase: Double,
    pesoCarga: Double,
    pesoTotal: Double,
    capacidadMaximaBodega: Double,
    factorVelocidad: Double,
    velocidadNominal: Double,
    velocidadAscensoEfectiva: Double
) {
    var pesoBase: Double = pesoBase
        private set

    var pesoCarga: Double = pesoCarga
        private set

    var pesoTotal: Double = pesoTotal
        private set

    var capacidadMaximaBodega: Double = capacidadMaximaBodega
        private set

    var factorVelocidad: Double = factorVelocidad
        private set

    var velocidadNominal: Double = velocidadNominal
        private set

    var velocidadAscensoEfectiva: Double = velocidadAscensoEfectiva
        private set

    /**
     * Calcula el porcentaje de ocupación actual de la bodega (0.0% a 100.0%).
     */
    fun porcentajeOcupacion(): Double {
        if (capacidadMaximaBodega <= 0.0) return 0.0
        return ((pesoCarga / capacidadMaximaBodega) * 100.0).coerceIn(0.0, 100.0)
    }

    /**
     * Indica si la bodega ha alcanzado su capacidad máxima permitida.
     */
    fun esCargaCompleta(): Boolean = pesoCarga >= capacidadMaximaBodega

    /**
     * Retorna el porcentaje de reducción aplicado a la velocidad nominal.
     */
    fun porcentajeReduccionVelocidad(): Double = ((1.0 - factorVelocidad) * 100.0).coerceAtLeast(0.0)

    /**
     * Retorna la capacidad remanente en kilogramos.
     */
    fun capacidadDisponible(): Double = (capacidadMaximaBodega - pesoCarga).coerceAtLeast(0.0)

    /**
     * Evalúa si la bodega se encuentra en niveles de saturación crítica.
     */
    fun esCargaCritica(umbralPorcentaje: Double = 90.0): Boolean =
        porcentajeOcupacion() >= umbralPorcentaje

    /**
     * Estima el tiempo requerido en segundos para ascender una distancia vertical dada.
     */
    fun estimarTiempoAscenso(distanciaMetros: Double): Double {
        if (velocidadAscensoEfectiva <= 0.0) return Double.POSITIVE_INFINITY
        return distanciaMetros / velocidadAscensoEfectiva
    }

    operator fun component1(): Double = pesoBase
    operator fun component2(): Double = pesoCarga
    operator fun component3(): Double = pesoTotal
    operator fun component4(): Double = capacidadMaximaBodega
    operator fun component5(): Double = factorVelocidad
    operator fun component6(): Double = velocidadNominal
    operator fun component7(): Double = velocidadAscensoEfectiva

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EstadoAscenso) return false
        return pesoBase == other.pesoBase &&
                pesoCarga == other.pesoCarga &&
                pesoTotal == other.pesoTotal &&
                capacidadMaximaBodega == other.capacidadMaximaBodega &&
                factorVelocidad == other.factorVelocidad &&
                velocidadNominal == other.velocidadNominal &&
                velocidadAscensoEfectiva == other.velocidadAscensoEfectiva
    }

    override fun hashCode(): Int {
        var res = pesoBase.hashCode()
        res = 31 * res + pesoCarga.hashCode()
        res = 31 * res + pesoTotal.hashCode()
        res = 31 * res + capacidadMaximaBodega.hashCode()
        res = 31 * res + factorVelocidad.hashCode()
        res = 31 * res + velocidadNominal.hashCode()
        res = 31 * res + velocidadAscensoEfectiva.hashCode()
        return res
    }

    override fun toString(): String {
        return "EstadoAscenso(pesoBase=$pesoBase, pesoCarga=$pesoCarga, pesoTotal=$pesoTotal, " +
                "capacidadMaximaBodega=$capacidadMaximaBodega, factorVelocidad=$factorVelocidad, " +
                "velocidadNominal=$velocidadNominal, velocidadAscensoEfectiva=$velocidadAscensoEfectiva)"
    }
}

/**
 * Responsabilidad Única (SRP) e Inversión de Dependencias (DIP):
 * Abstracción responsable exclusivamente del cálculo de masa/peso total de una entidad.
 */
interface CalculadorPeso {
    fun calcularPesoTotal(pesoBase: Double, almacenamiento: Almacenamiento?): Double
}

/**
 * Implementación estándar de [CalculadorPeso] según la especificación de la Issue #6.
 */
class CalculadorPesoSubmarino : CalculadorPeso {

    override fun calcularPesoTotal(pesoBase: Double, almacenamiento: Almacenamiento?): Double {
        require(pesoBase >= 0.0) { "El peso base no puede ser negativo: $pesoBase" }
        val pesoCarga = almacenamiento?.pesoActual ?: 0.0
        return pesoBase + pesoCarga
    }

    /**
     * Sobrecarga que permite calcular el peso total a partir de una colección genérica de [Pesable].
     */
    fun calcularPesoTotal(pesoBase: Double, items: Collection<Pesable>): Double {
        require(pesoBase >= 0.0) { "El peso base no puede ser negativo: $pesoBase" }
        return pesoBase + items.sumOf { it.peso }
    }
}

/**
 * Principio Abierto/Cerrado (OCP) y Segregación de Interfaces (ISP):
 * Contrato que define la estrategia para determinar la dinámica de velocidad y desplazamiento
 * de ascenso en función de la masa total y la capacidad de carga.
 */
interface EstrategiaAscenso {

    fun calcularFactorVelocidad(
        pesoTotal: Double,
        pesoBase: Double,
        capacidadCargaMaxima: Double
    ): Double

    fun calcularVelocidadAscenso(
        velocidadBase: Double,
        pesoTotal: Double,
        pesoBase: Double,
        capacidadCargaMaxima: Double
    ): Double = CalculosFisica.aplicarFactorEscala(
        velocidadBase,
        calcularFactorVelocidad(pesoTotal, pesoBase, capacidadCargaMaxima)
    )

    fun calcularDesplazamientoAscenso(
        deltaYNominal: Double,
        pesoTotal: Double,
        pesoBase: Double,
        capacidadCargaMaxima: Double
    ): Double = CalculosFisica.aplicarFactorEscala(
        deltaYNominal,
        calcularFactorVelocidad(pesoTotal, pesoBase, capacidadCargaMaxima)
    )
}

/**
 * Estrategia de ascenso lineal estándar:
 * Aplica una reducción proporcional al porcentaje de capacidad ocupada en la bodega.
 */
class EstrategiaAscensoLineal(
    factorReduccionMaximo: Double = 0.5
) : EstrategiaAscenso {

    private var _factorReduccionMaximo: Double = factorReduccionMaximo

    init {
        require(factorReduccionMaximo in 0.0..1.0) {
            "El factor de reducción máximo debe estar entre 0.0 y 1.0: $factorReduccionMaximo"
        }
    }

    val factorReduccion: Double
        get() = _factorReduccionMaximo

    override fun calcularFactorVelocidad(
        pesoTotal: Double,
        pesoBase: Double,
        capacidadCargaMaxima: Double
    ): Double {
        val ratio = CalculosFisica.calcularRatioCarga(pesoTotal, pesoBase, capacidadCargaMaxima)
        return (1.0 - (_factorReduccionMaximo * ratio)).coerceIn(0.0, 1.0)
    }
}

/**
 * Estrategia de ascenso inercial por ratio de masa (Principio OCP):
 * Modela la desaceleración vertical en función directa de la masa total acumulada respecto a la base.
 */
class EstrategiaAscensoPorMasa : EstrategiaAscenso {

    override fun calcularFactorVelocidad(
        pesoTotal: Double,
        pesoBase: Double,
        capacidadCargaMaxima: Double
    ): Double {
        if (pesoTotal <= 0.0 || pesoBase <= 0.0) return 1.0
        return (pesoBase / pesoTotal).coerceIn(0.0, 1.0)
    }

    fun descripcionModelo(): String = "Modelo físico inercial: factor = masaBase / masaTotal"
}

/**
 * Estrategia física hidrostática y de flotabilidad de Arquímedes (Principio OCP).
 */
class EstrategiaFisicaFlotabilidad(
    private val calculadorPresion: CalculadorPresion = CalculadoraPresionFluido(),
    factorEmpujeMinimo: Double = 0.2
) : EstrategiaAscenso {

    private var _factorEmpujeMinimo: Double = factorEmpujeMinimo

    init {
        require(factorEmpujeMinimo in 0.0..1.0) {
            "El factor de empuje mínimo debe estar entre 0.0 y 1.0: $factorEmpujeMinimo"
        }
    }

    val empujeMinimo: Double
        get() = _factorEmpujeMinimo

    override fun calcularFactorVelocidad(
        pesoTotal: Double,
        pesoBase: Double,
        capacidadCargaMaxima: Double
    ): Double {
        val ratioCarga = CalculosFisica.calcularRatioCarga(pesoTotal, pesoBase, capacidadCargaMaxima)
        val factor = 1.0 - (ratioCarga * (1.0 - _factorEmpujeMinimo))
        return factor.coerceIn(_factorEmpujeMinimo, 1.0)
    }
}
