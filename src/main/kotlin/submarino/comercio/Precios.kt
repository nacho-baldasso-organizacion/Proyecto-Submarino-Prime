package submarino

/**
 * ============================================================================
 * VALORACIÓN Y CÁLCULO DE PRECIOS - PRINCIPIOS SOLID (SRP, OCP, DIP)
 * Issue #7: Venta de minerales y valoración comercial desacoplada
 * ============================================================================
 */

/**
 * Estrategia de valoración base:
 * Otorga el valor comercial estándar definido intrínsecamente en el ítem vendible.
 * Cumple con el Principio Abierto/Cerrado (OCP) y Sustitución de Liskov (LSP).
 */
class EstrategiaPreciosBase : EstrategiaPrecios {
    override fun calcularPrecio(item: Vendible): Int = item.valor
}

/**
 * Estrategia de valoración con factor multiplicador de bonificación o fluctuación de mercado:
 * Aplica un coeficiente sobre el valor nominal de cada ítem (por ejemplo, 1.25 para 25% de bonus).
 * Cumple con OCP al expandir las reglas de tasación sin alterar la lógica de negocio.
 * Encapsula su estado con 'private set'.
 *
 * @param factor Multiplicador a aplicar (debe ser >= 0.0).
 */
class EstrategiaPreciosMultiplicador(
    factor: Double
) : EstrategiaPrecios {

    init {
        require(factor >= 0.0) {
            "El factor multiplicador de precios no puede ser negativo: $factor"
        }
    }

    var factor: Double = factor
        private set

    override fun calcularPrecio(item: Vendible): Int = kotlin.math.round(item.valor * factor).toInt()
}

/**
 * Calculadora de precios estándar de la estación.
 *
 * Cumple estrictamente con:
 * - Single Responsibility Principle (SRP): se limita al cálculo matemático y valoración total.
 * - Dependency Inversion Principle (DIP): depende exclusivamente de la abstracción [EstrategiaPrecios]
 *   inyectada mediante constructor.
 * - Open/Closed Principle (OCP): admite cualquier estrategia de precios sin modificar su estructura interna.
 *
 * @param estrategia Abstracción de la estrategia de tasación a utilizar.
 */
class CalculadoraPreciosEstandar(
    private val estrategia: EstrategiaPrecios = EstrategiaPreciosBase()
) : CalculadoraPrecios {

    override fun calcularPrecio(item: Vendible): Int = estrategia.calcularPrecio(item)

    override fun calcularTotal(items: Collection<Vendible>): Int =
        items.sumOf { estrategia.calcularPrecio(it) }
}
