package submarino

/**
 * ============================================================================
 * FÍSICA HIDROSTÁTICA - PRINCIPIOS SOLID (ISP, DIP, OCP)
 * ============================================================================
 */

/**
 * Segregación de Interfaces (ISP) e Inversión de Dependencias (DIP):
 * Contrato que define el cálculo de presiones y profundidades máximas
 * en cualquier medio fluido.
 */
interface CalculadorPresion {
    val densidadFluido: Double
    val gravedad: Double
    val presionAtmosferica: Double

    /**
     * Calcula la presión hidrostática en Pascales a una profundidad dada.
     *
     * @param profundidad Profundidad en metros (eje Y)
     * @return Presión total en Pascales
     */
    fun calcularPresion(profundidad: Double): Double

    /**
     * Calcula la profundidad máxima admisible en metros antes de colapso de un casco.
     *
     * @param presionMaximaPa Presión máxima que tolera la estructura
     * @return Profundidad límite en metros
     */
    fun calcularProfundidadMaxima(presionMaximaPa: Double): Double
}

/**
 * Principio Abierto/Cerrado (OCP):
 * Implementación abierta y configurable para cualquier fluido y entorno gravitacional.
 * Permite instanciar física de agua dulce, salada o atmósferas extraterrestres sin modificar la clase.
 *
 * Fórmula: P = P0 + (ρ * g * h)
 */
open class CalculadoraPresionFluido(
    override val densidadFluido: Double = 1025.0,    // kg/m³
    override val gravedad: Double = 9.81,            // m/s²
    override val presionAtmosferica: Double = 101325.0 // Pa
) : CalculadorPresion {

    override fun calcularPresion(profundidad: Double): Double {
        val h = maxOf(profundidad, 0.0) // no hay presiones inferiores a la de superficie
        return presionAtmosferica + (densidadFluido * gravedad * h)
    }

    override fun calcularProfundidadMaxima(presionMaximaPa: Double): Double {
        val presionHidrostaticaMaxima = presionMaximaPa - presionAtmosferica
        return maxOf(presionHidrostaticaMaxima / (densidadFluido * gravedad), 0.0)
    }
}

/**
 * Objeto singleton que encapsula la física hidrostática estándar para agua de mar en la Tierra.
 * Extiende [CalculadoraPresionFluido] preservando retrocompatibilidad total con el código existente.
 */
object CalculadoraHidrostatica : CalculadoraPresionFluido(
    densidadFluido = 1025.0,
    gravedad = 9.81,
    presionAtmosferica = 101325.0
) {
    // Constantes físicas universales para compatibilidad directa
    const val DENSIDAD_AGUA_MAR: Double = 1025.0    // kg/m³
    const val GRAVEDAD: Double = 9.81                // m/s²
    const val PRESION_ATMOSFERICA: Double = 101325.0 // Pa
}
