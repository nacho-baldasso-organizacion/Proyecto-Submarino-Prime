package submarino

import kotlin.math.pow

/**
 * ============================================================================
 * ESTRATEGIAS DE COSTO Y ESCALABILIDAD (ISSUE #8)
 * PRINCIPIOS SOLID (OCP, SRP, DIP, DRY)
 * ============================================================================
 */

/**
 * Base abstracta para modelos de cálculo y progresión de costos por nivel.
 * Encapsula la validación de nivel y costo base para evitar código repetido (DRY).
 */
abstract class EstrategiaCostoBase(
    protected val costoBase: Int
) : EstrategiaCosto {

    init {
        require(costoBase > 0) {
            "El costo base debe ser estrictamente positivo: $costoBase"
        }
    }

    protected fun validarNivel(nivelActual: Int) {
        require(nivelActual >= 1) {
            "El nivel actual debe ser al menos 1: $nivelActual"
        }
    }
}

/**
 * Estrategia de costo con progresión lineal:
 * El costo crece de forma aditiva por cada nivel de la mejora.
 */
class EstrategiaCostoLineal(
    costoBase: Int,
    private val incrementoPorNivel: Int
) : EstrategiaCostoBase(costoBase) {

    init {
        require(incrementoPorNivel >= 0) {
            "El incremento por nivel no puede ser negativo: $incrementoPorNivel"
        }
    }

    override fun calcularCosto(nivelActual: Int): Int {
        validarNivel(nivelActual)
        return costoBase + ((nivelActual - 1) * incrementoPorNivel)
    }

    override fun toString(): String =
        "EstrategiaCostoLineal(base=$$costoBase, incremento=+$$incrementoPorNivel/nivel)"
}

/**
 * Estrategia de costo con progresión geométrica / exponencial:
 * El costo se multiplica geométricamente en función del nivel.
 */
class EstrategiaCostoExponencial(
    costoBase: Int,
    private val factorMultiplicador: Double = 1.5
) : EstrategiaCostoBase(costoBase) {

    init {
        require(factorMultiplicador > 1.0) {
            "El factor multiplicador debe ser estrictamente mayor a 1.0: $factorMultiplicador"
        }
    }

    override fun calcularCosto(nivelActual: Int): Int {
        validarNivel(nivelActual)
        val exponente = (nivelActual - 1).toDouble()
        val costoCalculado = costoBase * factorMultiplicador.pow(exponente)
        return costoCalculado.toInt()
    }

    override fun toString(): String =
        "EstrategiaCostoExponencial(base=$$costoBase, factor=x$factorMultiplicador)"
}

/**
 * Estrategia de costo constante:
 * El costo se mantiene invariable independientemente del nivel.
 */
class EstrategiaCostoFijo(
    costoFijo: Int
) : EstrategiaCostoBase(costoFijo) {

    override fun calcularCosto(nivelActual: Int): Int {
        validarNivel(nivelActual)
        return costoBase
    }

    override fun toString(): String = "EstrategiaCostoFijo(costo=$$costoBase)"
}
