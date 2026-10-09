package submarino

/**
 * ============================================================================
 * FÍSICA DE ASCENSO Y CÁLCULO DE MASA - PRINCIPIOS SOLID (SRP, OCP, ISP, DIP)
 * Issue #6: Cálculo de peso total y física de flotabilidad/ascenso del submarino
 * ============================================================================
 */

/**
 * Representa una instantánea inmutable del estado dinámico de peso y ascenso del submarino.
 * Cumple con POO idiomática en Kotlin mediante inmutabilidad estricta.
 *
 * @property pesoBase Masa estructural constante del submarino en vacío (kg).
 * @property pesoCarga Masa acumulada actual de la carga/minerales en bodega (kg).
 * @property pesoTotal Masa total combinada (pesoBase + pesoCarga) en kg.
 * @property capacidadMaximaBodega Capacidad máxima soportada por la bodega (kg).
 * @property factorVelocidad Multiplicador dinámico resultante de la carga (0.0 a 1.0).
 * @property velocidadNominal Velocidad base nominal de diseño (m/s).
 * @property velocidadAscensoEfectiva Velocidad dinámica efectiva resultante para el ascenso (m/s).
 */
data class EstadoAscenso(
    val pesoBase: Double,
    val pesoCarga: Double,
    val pesoTotal: Double,
    val capacidadMaximaBodega: Double,
    val factorVelocidad: Double,
    val velocidadNominal: Double,
    val velocidadAscensoEfectiva: Double
)

/**
 * Responsabilidad Única (SRP) e Inversión de Dependencias (DIP):
 * Abstracción responsable exclusivamente del cálculo de masa/peso total de una entidad
 * a partir de su peso base constante y su compartimento de carga.
 */
interface CalculadorPeso {
    /**
     * Calcula la masa total sumando el peso base y la carga acumulada en el almacenamiento.
     *
     * @param pesoBase Masa base constante estructural (kg).
     * @param almacenamiento Abstracción del contenedor o bodega (puede ser nulo si no posee bodega).
     * @return Masa/peso total en kilogramos (kg).
     */
    fun calcularPesoTotal(pesoBase: Double, almacenamiento: Almacenamiento?): Double
}

/**
 * Implementación estándar de [CalculadorPeso] según la especificación de la Issue #6.
 * Desacopla la lógica matemática del pesaje respecto de la entidad [Submarino] y de [Bodega].
 */
class CalculadorPesoSubmarino : CalculadorPeso {

    override fun calcularPesoTotal(pesoBase: Double, almacenamiento: Almacenamiento?): Double {
        require(pesoBase >= 0.0) { "El peso base no puede ser negativo: $pesoBase" }
        val pesoCarga = almacenamiento?.pesoActual ?: 0.0
        return pesoBase + pesoCarga
    }

    /**
     * Sobrecarga que permite calcular el peso total a partir de una colección genérica de [Pesable].
     * Demuestra OCP e ISP operando contra la abstracción elemental de peso.
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
 *
 * Permite incorporar diferentes modelos físicos (lineal, gravitacional, boyante/Arquímedes)
 * sin modificar la lógica interna de [Submarino].
 */
interface EstrategiaAscenso {

    /**
     * Calcula el factor multiplicador de velocidad y desplazamiento (entre 0.0 y 1.0)
     * dictado por el nivel de carga actual.
     * Con bodega vacía retorna 1.0; a mayor peso en bodega, menor es el factor resultante.
     *
     * @param pesoTotal Masa total actual del submarino (kg).
     * @param pesoBase Masa base estructural en vacío (kg).
     * @param capacidadCargaMaxima Capacidad máxima de la bodega (kg).
     * @return Factor multiplicador (entre 0.0 y 1.0).
     */
    fun calcularFactorVelocidad(
        pesoTotal: Double,
        pesoBase: Double,
        capacidadCargaMaxima: Double
    ): Double

    /**
     * Calcula la velocidad dinámica efectiva de ascenso en m/s.
     *
     * @param velocidadBase Velocidad nominal de diseño o motor (m/s).
     * @param pesoTotal Masa total actual del submarino (kg).
     * @param pesoBase Masa base estructural en vacío (kg).
     * @param capacidadCargaMaxima Capacidad máxima de la bodega (kg).
     * @return Velocidad dinámica efectiva en m/s (siempre >= 0.0).
     */
    fun calcularVelocidadAscenso(
        velocidadBase: Double,
        pesoTotal: Double,
        pesoBase: Double,
        capacidadCargaMaxima: Double
    ): Double {
        val factor = calcularFactorVelocidad(pesoTotal, pesoBase, capacidadCargaMaxima)
        return maxOf(velocidadBase * factor, 0.0)
    }

    /**
     * Calcula el desplazamiento vertical efectivo correspondiente a una solicitud de ascenso.
     *
     * @param deltaYNominal Desplazamiento vertical nominal solicitado en metros.
     * @param pesoTotal Masa total actual del submarino (kg).
     * @param pesoBase Masa base estructural en vacío (kg).
     * @param capacidadCargaMaxima Capacidad máxima de la bodega (kg).
     * @return Desplazamiento vertical efectivo en metros.
     */
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
 *
 * Fórmula:
 * factor = 1.0 - (factorReduccionMaximo * (pesoCarga / capacidadMaxima))
 *
 * Donde:
 * - Bodega vacía: factor = 1.0 (100% velocidad)
 * - Media carga: factor = 1.0 - (factorReduccionMaximo * 0.5) (e.g. 75% con default 0.5)
 * - Bodega llena: factor = 1.0 - factorReduccionMaximo (e.g. 50% con default 0.5)
 *
 * @param factorReduccionMaximo Porcentaje de desaceleración máxima cuando la bodega está 100% llena (0.0 a 1.0).
 */
class EstrategiaAscensoLineal(
    val factorReduccionMaximo: Double = 0.5
) : EstrategiaAscenso {

    init {
        require(factorReduccionMaximo in 0.0..1.0) {
            "El factor de reducción máximo debe estar entre 0.0 y 1.0: $factorReduccionMaximo"
        }
    }

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
 *
 * Fórmula:
 * factor = pesoBase / pesoTotal
 *
 * A mayor masa total respecto al peso base, menor es el factor de aceleración/velocidad alcanzable.
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
}

/**
 * Estrategia física hidrostática y de flotabilidad de Arquímedes (Principio OCP):
 * Integra la hidrostática del entorno fluido ([CalculadorPresion]) con el principio de flotabilidad
 * de Arquímedes y la gravedad.
 *
 * @param calculadorPresion Abstracción física del fluido (DIP).
 * @param factorEmpujeMinimo Umbral de propulsión mínima garantizada para evitar inmovilidad total con carga extrema.
 */
class EstrategiaFisicaFlotabilidad(
    val calculadorPresion: CalculadorPresion = CalculadoraHidrostatica,
    val factorEmpujeMinimo: Double = 0.2
) : EstrategiaAscenso {

    init {
        require(factorEmpujeMinimo in 0.0..1.0) {
            "El factor de empuje mínimo debe estar entre 0.0 y 1.0: $factorEmpujeMinimo"
        }
    }

    override fun calcularFactorVelocidad(
        pesoTotal: Double,
        pesoBase: Double,
        capacidadCargaMaxima: Double
    ): Double {
        if (capacidadCargaMaxima <= 0.0) return 1.0
        val pesoCarga = (pesoTotal - pesoBase).coerceAtLeast(0.0)
        val ratioCarga = (pesoCarga / capacidadCargaMaxima).coerceIn(0.0, 1.0)

        // Escalamiento fluido que decae desde 1.0 hasta factorEmpujeMinimo
        val factor = 1.0 - (ratioCarga * (1.0 - factorEmpujeMinimo))
        return factor.coerceIn(factorEmpujeMinimo, 1.0)
    }
}
