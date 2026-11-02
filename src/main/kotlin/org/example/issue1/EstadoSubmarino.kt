package submarino

/**
 * Value Object inmutable que representa el estado observable del submarino.
 * Encapsula los datos del estado y provee métodos de dominio para su manipulación y representación.
 * SRP: esta clase tiene una única responsabilidad: representar el estado del submarino
 * con sus datos y comportamiento asociado.
 */
data class EstadoSubmarino(
    val posX: Double,
    val posY: Double,
    val pesoTotal: Double,
    val velocidadAscenso: Double,
    val saldo: Int,
    val estaAcoplado: Boolean,
    val bateria: Double,
    val estaDestruido: Boolean,
    val casco: Casco
) {

    /**
     * Formatea este estado como cadena de texto.
     * Separa la responsabilidad de formateo de Submarino y otros componentes.
     */
    fun formatear(): String {
        return "Submarino(posX=${posX}, posY=${posY}, " +
                "pesoTotal=${"%.2f".format(pesoTotal)}kg, " +
                "velocidadAscenso=${"%.2f".format(velocidadAscenso)}m/s, saldo=${saldo}, " +
                "estaAcoplado=${estaAcoplado}, bateria=${"%.2f".format(bateria)}%, " +
                "estaDestruido=${estaDestruido}, casco=$casco)"
    }

    /**
     * Determina si el submarino se encuentra en un estado crítico.
     * Crítico significa: casco destruido O batería agotada.
     */
    fun estaEnEstadoCritico(): Boolean = estaDestruido || bateria <= 0.0

    /**
     * Determina si el submarino puede moverse (tiene batería y no está destruido).
     */
    fun puedeMoverse(): Boolean = !estaDestruido && bateria > 0.0

    /**
     * Calcula la relación de peso total respecto al peso base.
     */
    fun ratioPesoCarga(): Double {
        if (pesoTotal <= 0.0) return 0.0
        return (pesoTotal / (pesoTotal + 100.0)).coerceIn(0.0, 1.0)
    }

    /**
     * Determina si la bodega está completamente llena.
     * Nota: Esta evaluación requiere conocimiento del contexto de la bodega,
     * pero este método proporciona una estimación basada en el peso total.
     */
    fun estaSaturada(): Boolean = pesoTotal > 500.0
}

/**
 * Formateador especializado en la representación legible del estado del submarino (SRP).
 * Esta clase mantiene la responsabilidad de formateo especializado y puede
 * personalizarse fácilmente para diferentes estilos de presentación.
 * SRP: esta clase tiene una única responsabilidad: formatear el estado del submarino.
 * Puede ser reemplazado o subclaseado para diferentes formatos de presentación.
 */
class FormateadorEstadoSubmarino {

    /**
     * Formatea el estado utilizando el formato estándar del submarino.
     */
    fun formatear(estado: EstadoSubmarino): String {
        return estado.formatear()
    }

    /**
     * Formatea el estado para mostrar solo indicadores críticos.
     */
    fun formatearCritico(estado: EstadoSubmarino): String {
        val componente = if (estado.estaEnEstadoCritico()) " [CRITICO]" else ""
        return "${estado.formatear()}${componente}"
    }
}
