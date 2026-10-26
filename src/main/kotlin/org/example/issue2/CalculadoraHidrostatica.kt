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
 * Encapsula sus propiedades con 'private set' e incorpora comportamiento propio.
 *
 * Fórmula: P = P0 + (ρ * g * h)
 */
open class CalculadoraPresionFluido(
    densidadFluido: Double = 1025.0,      // kg/m³
    gravedad: Double = 9.81,              // m/s²
    presionAtmosferica: Double = 101325.0 // Pa
) : CalculadorPresion {

    final override var densidadFluido: Double = densidadFluido
        private set

    final override var gravedad: Double = gravedad
        private set

    final override var presionAtmosferica: Double = presionAtmosferica
        private set

    override fun calcularPresion(profundidad: Double): Double {
        val h = maxOf(profundidad, 0.0)
        return presionAtmosferica + (densidadFluido * gravedad * h)
    }

    override fun calcularProfundidadMaxima(presionMaximaPa: Double): Double {
        val presionHidrostaticaMaxima = presionMaximaPa - presionAtmosferica
        return maxOf(presionHidrostaticaMaxima / (densidadFluido * gravedad), 0.0)
    }

    /**
     * Retorna el incremento de presión por cada metro de descenso en el fluido (ρ * g).
     */
    fun calcularGradientePresion(): Double = densidadFluido * gravedad

    /**
     * Determina si la presión a una profundidad especificada no sobrepasa el límite del casco.
     */
    fun esProfundidadSegura(profundidad: Double, presionMaximaPa: Double): Boolean =
        calcularPresion(profundidad) <= presionMaximaPa
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
    const val DENSIDAD_AGUA_MAR: Double = 1025.0    // kg/m³
    const val GRAVEDAD: Double = 9.81                // m/s²
    const val PRESION_ATMOSFERICA: Double = 101325.0 // Pa

    /**
     * Retorna la presión de referencia en superficie marina (Y = 0.0 m).
     */
    fun calcularPresionSuperficie(): Double = calcularPresion(0.0)

    /**
     * Calcula el margen de seguridad de presión remanente antes del colapso del casco.
     */
    fun calcularMargenSeguridad(presionActualPa: Double, presionMaximaPa: Double): Double =
        maxOf(presionMaximaPa - presionActualPa, 0.0)
}
