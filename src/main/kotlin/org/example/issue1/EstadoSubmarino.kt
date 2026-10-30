package submarino

/**
 * Instantánea inmutable del estado observable del submarino.
 * Separa la recolección de datos de su representación textual (SRP).
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
)

/**
 * Formateador especializado en la representación legible del estado del submarino (SRP).
 */
class FormateadorEstadoSubmarino {

    fun formatear(estado: EstadoSubmarino): String {
        return "Submarino(posX=${estado.posX}, posY=${estado.posY}, " +
                "pesoTotal=${"%.2f".format(estado.pesoTotal)}kg, " +
                "velocidadAscenso=${"%.2f".format(estado.velocidadAscenso)}m/s, saldo=$${estado.saldo}, " +
                "estaAcoplado=${estado.estaAcoplado}, bateria=${"%.2f".format(estado.bateria)}%, " +
                "estaDestruido=${estado.estaDestruido}, casco=${estado.casco})"
    }
}
