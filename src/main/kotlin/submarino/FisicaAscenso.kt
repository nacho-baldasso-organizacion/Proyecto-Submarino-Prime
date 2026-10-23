package submarino

/**
 * ============================================================================
 * FÍSICA DE ASCENSO Y CÁLCULO DE MASA - PRINCIPIOS SOLID (SRP, OCP, ISP, DIP)
 * Issue #6: Cálculo de peso total y física de flotabilidad/ascenso del submarino
 * ============================================================================
 */

/**
 * Representa una instantánea inmutable del estado dinámico de peso y ascenso del submarino.
 * Cumple con POO idiomática en Kotlin e incorpora métodos de consulta de dominio.
 */
data class EstadoAscenso(
    val pesoBase: Double,
    val pesoCarga: Double,
    val pesoTotal: Double,
    val capacidadMaximaBodega: Double,
    val factorVelocidad: Double,
    val velocidadNominal: Double,
    val velocidadAscensoEfectiva: Double
) {
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
    ): Double {
        val factor = calcularFactorVelocidad(pesoTotal, pesoBase, capacidadCargaMaxima)
        return maxOf(velocidadBase * factor, 0.0)
    }

    fun calcularDesplazamientoAscenso(
        deltaYNominal: Double,
        pesoTotal: Double,
        pesoBase: Double,
        capacidadCargaMaxima: Double
    ): Double {
        val factor = calcularFactorVelocidad(pesoTotal, pesoBase, capacidadCargaMaxima)
        return maxOf(deltaYNominal * factor, 0.0)
    }
}

/**
 * Estrategia de ascenso lineal estándar:
 * Aplica una reducción proporcional al porcentaje de capacidad ocupada en la bodega.
 */
class EstrategiaAscensoLineal(
    private val factorReduccionMaximo: Double = 0.5
) : EstrategiaAscenso {

    init {
        require(factorReduccionMaximo in 0.0..1.0) {
            "El factor de reducción máximo debe estar entre 0.0 y 1.0: $factorReduccionMaximo"
        }
    }

    val factorReduccion: Double
        get() = factorReduccionMaximo

    override fun calcularFactorVelocidad(
        pesoTotal: Double,
        pesoBase: Double,
        capacidadCargaMaxima: Double
    ): Double {
        if (capacidadCargaMaxima <= 0.0) return 1.0
        val pesoCarga = (pesoTotal - pesoBase).coerceAtLeast(0.0)
        val porcentajeOcupacion = (pesoCarga / capacidadCargaMaxima).coerceIn(0.0, 1.0)
        return (1.0 - (factorReduccionMaximo * porcentajeOcupacion)).coerceIn(0.0, 1.0)
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
    private val factorEmpujeMinimo: Double = 0.2
) : EstrategiaAscenso {

    init {
        require(factorEmpujeMinimo in 0.0..1.0) {
            "El factor de empuje mínimo debe estar entre 0.0 y 1.0: $factorEmpujeMinimo"
        }
    }

    val empujeMinimo: Double
        get() = factorEmpujeMinimo

    override fun calcularFactorVelocidad(
        pesoTotal: Double,
        pesoBase: Double,
        capacidadCargaMaxima: Double
    ): Double {
        if (capacidadCargaMaxima <= 0.0) return 1.0
        val pesoCarga = (pesoTotal - pesoBase).coerceAtLeast(0.0)
        val ratioCarga = (pesoCarga / capacidadCargaMaxima).coerceIn(0.0, 1.0)

        val factor = 1.0 - (ratioCarga * (1.0 - factorEmpujeMinimo))
        return factor.coerceIn(factorEmpujeMinimo, 1.0)
    }
}
