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
}

/**
 * Estado inmutable que representa la visualización de la cabeza-up display (HUD)
 * del submarino, conteniendo todos los indicadores relevantes para la interfaz.
 */
data class EstadoHUD(
    val presionActual: Double,
    val presionMaxima: Double,
    val profundidad: Double,
    val pesoBodega: Double,
    val pesoMaximoBodega: Double,
    val monedas: Int
)

/**
 * View responsable de renderizar la información del HUD del submarino en formato de texto.
 * Provee métodos para formatear cada indicador individualmente y un resumen completo.
 * Cumple con principio SRP: cada método tiene una responsabilidad única de formateo.
 */
class HUDView {

    private val formatting = HUDFormatting

    private fun formatDouble(pattern: String, value: Double): String {
        return java.lang.String.format(formatting.usLocale, pattern, value)
    }

    /**
     * Genera una representación visual de la barra de presión con porcentaje.
     * @param estado EstadoHUD con la presión actual y máxima.
     * @return Cadena con la barra de presión y porcentaje calculado.
     */
    fun renderizarBarraPresion(estado: EstadoHUD): String {
        if (estado.presionMaxima <= formatting.pressureZeroThreshold) {
            return "Presión: ${formatDouble("%.2f", estado.presionActual)} / ${formatDouble("%.2f", estado.presionMaxima)} ${formatting.pressureUnit} (0.00%)"
        }
        val porcentaje = (estado.presionActual / estado.presionMaxima) * 100.0
        // Cada paso representa ${formatting.pressurePercentageStep}%: 50% -> 2 bloques, 100% -> 4 bloques
        val barrasLlenas = (porcentaje / formatting.pressurePercentageStep).toInt().coerceIn(0, formatting.maxPressureBars)
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
            return "Peso bodega: ${formatDouble("%.1f", estado.pesoBodega)} / ${formatDouble("%.1f", estado.pesoMaximoBodega)} ${formatting.weightUnit} (0.0%)"
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