package submarino

import kotlin.math.pow

/**
 * ============================================================================
 * ESTRATEGIAS DE COSTO Y ESCALABILIDAD (ISSUE #8)
 * PRINCIPIOS SOLID (OCP = 0 violaciones, SRP = 0 violaciones, DIP = 0 violaciones)
 * ============================================================================
 */

/**
 * Estrategia de costo con progresión lineal:
 * El costo crece de forma aditiva por cada nivel de la mejora.
 *
 * Fórmula:
 * costo = costoBase + ((nivelActual - 1) * incrementoPorNivel)
 * Para nivel 1 -> costoBase
 * Para nivel 2 -> costoBase + incrementoPorNivel
 *
 * @param costoBase Costo monetario para el primer ascenso de nivel (debe ser > 0).
 * @param incrementoPorNivel Cantidad adicional de monedas sumadas por cada nivel subsiguiente (debe ser >= 0).
 */
class EstrategiaCostoLineal(
    val costoBase: Int,
    val incrementoPorNivel: Int
) : EstrategiaCosto {

    init {
        require(costoBase > 0) {
            "El costo base debe ser estrictamente positivo: $costoBase"
        }
        require(incrementoPorNivel >= 0) {
            "El incremento por nivel no puede ser negativo: $incrementoPorNivel"
        }
    }

    override fun calcularCosto(nivelActual: Int): Int {
        require(nivelActual >= 1) {
            "El nivel actual debe ser al menos 1: $nivelActual"
        }
        return costoBase + ((nivelActual - 1) * incrementoPorNivel)
    }

    override fun toString(): String =
        "EstrategiaCostoLineal(base=$$costoBase, incremento=+$$incrementoPorNivel/nivel)"
}

/**
 * Estrategia de costo con progresión geométrica / exponencial:
 * El costo se multiplica geométricamente en función del nivel.
 *
 * Fórmula:
 * costo = costoBase * (factorMultiplicador ^ (nivelActual - 1))
 *
 * @param costoBase Costo monetario para el nivel base (debe ser > 0).
 * @param factorMultiplicador Tasa multiplicativa de escalamiento por nivel (debe ser > 1.0).
 */
class EstrategiaCostoExponencial(
    val costoBase: Int,
    val factorMultiplicador: Double = 1.5
) : EstrategiaCosto {

    init {
        require(costoBase > 0) {
            "El costo base debe ser estrictamente positivo: $costoBase"
        }
        require(factorMultiplicador > 1.0) {
            "El factor multiplicador debe ser estrictamente mayor a 1.0: $factorMultiplicador"
        }
    }

    override fun calcularCosto(nivelActual: Int): Int {
        require(nivelActual >= 1) {
            "El nivel actual debe ser al menos 1: $nivelActual"
        }
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
 *
 * @param costoFijo Monto monetario fijo requerido para cada subida de nivel.
 */
class EstrategiaCostoFijo(
    val costoFijo: Int
) : EstrategiaCosto {

    init {
        require(costoFijo > 0) {
            "El costo fijo debe ser estrictamente positivo: $costoFijo"
        }
    }

    override fun calcularCosto(nivelActual: Int): Int {
        require(nivelActual >= 1) {
            "El nivel actual debe ser al menos 1: $nivelActual"
        }
        return costoFijo
    }

    override fun toString(): String = "EstrategiaCostoFijo(costo=$$costoFijo)"
}
