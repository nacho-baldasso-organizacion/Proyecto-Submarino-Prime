package submarino

import java.util.Locale

/**
 * Constants for HUD rendering formatting and thresholds.
 */
private object HUDFormatting {
    val usLocale: Locale = Locale.US

    // Pressure bar thresholds
    val pressureZeroThreshold: Double = 0.0
    val pressurePercentageStep: Double = 25.0
    val maxPressureBars: Int = 4

    // Weight display
    val weightZeroThreshold: Double = 0.0

    // Depth display
    val depthUnit: String = "m"
    val pressureUnit: String = "mmHg"
    val weightUnit: String = "kg"

    /**
     * Formatea [valor] con el [patron] indicado usando la configuración regional estadounidense.
     */
    fun formatear(valor: Double, patron: String): String =
        java.lang.String.format(usLocale, patron, valor)
}

/**
 * Value Object inmutable que representa la visualización de la cabeza-up display (HUD)
 * del submarino, conteniendo todos los indicadores relevantes para la interfaz.
 * Provee métodos de dominio para formateo y consulta de indicadores.
 * SRP: esta clase tiene una única responsabilidad: representar el estado HUD
 * con sus datos y comportamiento asociado.
 */
data class EstadoHUD(
    val presionActual: Double,
    val presionMaxima: Double,
    val profundidad: Double,
    val pesoBodega: Double,
    val pesoMaximoBodega: Double,
    val monedas: Int
) {

    /**
     * Determina si la presión está en rango seguro (por debajo del umbral máximo).
     */
    fun estaPresionSegura(): Boolean = presionMaxima > 0.0 && presionActual <= presionMaxima

    /**
     * Determina si la presión está críticamente alta (90% o más del máximo).
     */
    fun estaPresionCritica(): Boolean = presionActual >= presionMaxima * 0.9

    /**
     * Determina si la bodega está llena.
     */
    fun estaBodegaLlena(): Boolean = pesoMaximoBodega > 0.0 && pesoBodega >= pesoMaximoBodega

    /**
     * Calcula el porcentaje de presión respecto al máximo.
     * Devuelve 0.0 cuando no hay un máximo válido.
     */
    fun porcentajePresion(): Double =
        if (presionMaxima <= 0.0) 0.0 else (presionActual / presionMaxima) * 100.0

    /**
     * Calcula cuántos bloques de la barra de presión deben mostrarse llenos.
     */
    fun barrasPresionLlenas(pasoPorcentaje: Double, maximoBarras: Int): Int {
        if (pasoPorcentaje <= 0.0 || maximoBarras <= 0) {
            return 0
        }
        return (porcentajePresion() / pasoPorcentaje).toInt().coerceIn(0, maximoBarras)
    }

    /**
     * Calcula el porcentaje de ocupación de la bodega.
     * Devuelve 0.0 cuando no hay capacidad válida.
     */
    fun porcentajeBodega(): Double =
        if (pesoMaximoBodega <= 0.0) 0.0 else (pesoBodega / pesoMaximoBodega) * 100.0
}

/**
 * View responsable de renderizar la información del HUD del submarino en formato de texto.
 * Provee métodos para formatear cada indicador individualmente y un resumen completo.
 * Cumple con principio SRP: cada método tiene una responsabilidad única de formateo.
 */
class HUDView {

    private val formatting = HUDFormatting

    private fun formatDouble(pattern: String, value: Double): String {
        return formatting.formatear(value, pattern)
    }

    /**
     * Genera una representación visual de la barra de presión con porcentaje.
     * @param estado EstadoHUD con la presión actual y máxima.
     * @return Cadena con la barra de presión y porcentaje calculado.
     */
    fun renderizarBarraPresion(estado: EstadoHUD): String {
        if (estado.presionMaxima <= formatting.pressureZeroThreshold) {
            return "Presión: ${formatDouble("%.2f", estado.presionActual)} / ${formatDouble("%.2f", estado.presionMaxima)} ${formatting.pressureUnit} (${formatDouble("%.2f", estado.porcentajePresion())}%)"
        }
        val porcentaje = estado.porcentajePresion()
        // Cada paso representa ${formatting.pressurePercentageStep}%: 50% -> 2 bloques, 100% -> 4 bloques
        val barrasLlenas = estado.barrasPresionLlenas(
            formatting.pressurePercentageStep,
            formatting.maxPressureBars
        )
        val barraLlena = rellenarSimbolo("▇", barrasLlenas)
        val porcentajeFormateado = formatDouble("%.1f", porcentaje)
        return "Presión: ${formatDouble("%.2f", estado.presionActual)} / ${formatDouble("%.2f", estado.presionMaxima)} ${formatting.pressureUnit} [$barraLlena] $porcentajeFormateado%"
    }

    private fun rellenarSimbolo(simbolo: String, cantidad: Int): String {
        return if (cantidad <= 0) ""
        else {
            val sb = StringBuilder(cantidad)
            for (i in 1..cantidad) {
                sb.append(simbolo)
            }
            sb.toString()
        }
    }

    /**
     * Formatea la profundidad en metros.
     * @param estado EstadoHUD con la profundidad actual.
     * @return Cadena formateada con la profundidad en metros.
     */
    fun renderizarProfundidad(estado: EstadoHUD): String = "Profundidad: ${formatDouble("%.1f", estado.profundidad)} ${formatting.depthUnit}"

    /**
     * Formatea el peso de la bodega respecto al máximo, mostrando kg.
     * @param estado EstadoHUD con el peso actual y máximo de la bodega.
     * @return Cadena formateada con el peso y capacidad.
     */
    fun renderizarPesoBodega(estado: EstadoHUD): String {
        if (estado.pesoMaximoBodega <= formatting.weightZeroThreshold) {
            return "Peso bodega: ${formatDouble("%.1f", estado.pesoBodega)} / ${formatDouble("%.1f", estado.pesoMaximoBodega)} ${formatting.weightUnit} (${formatDouble("%.1f", estado.porcentajeBodega())}%)"
        }
        return "Peso bodega: ${formatDouble("%.1f", estado.pesoBodega)} / ${formatDouble("%.1f", estado.pesoMaximoBodega)} ${formatting.weightUnit}"
    }

    /**
     * Formatea el total de monedas.
     * @param estado EstadoHUD con el total de monedas.
     * @return Cadena formateada con el total de monedas.
     */
    fun renderizarMonedas(estado: EstadoHUD): String = "Monedas: ${estado.monedas}"

    /**
     * Devuelve el resumen estructurado de los 4 indicadores principales del HUD.
     * @param estado EstadoHUD con todos los datos del submarino.
     * @return Cadena con el HUD completo formateado.
     */
    fun renderizarHUDCompleto(estado: EstadoHUD): String {
        return renderizarBarraPresion(estado) + "\n" +
               renderizarProfundidad(estado) + "\n" +
               renderizarPesoBodega(estado) + "\n" +
               renderizarMonedas(estado)
    }
}