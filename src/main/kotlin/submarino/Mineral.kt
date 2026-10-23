package submarino

/**
 * Representa una veta física o ejemplar de mineral colocado en una coordenada del mapa.
 * Implementa [Recolectable] cumpliendo con LSP (Liskov Substitution Principle).
 *
 * @property id Identificador único alfanumérico del mineral.
 * @property tipo Categoría taxonómica del mineral según [TipoMineral].
 * @property posX Coordenada horizontal (eje X) en la cuadrícula.
 * @property posY Coordenada vertical (eje Y) en la cuadrícula.
 * @property recolectado Estado actual de recolección (por defecto false).
 */
data class Mineral(
    override val id: String,
    val tipo: TipoMineral,
    override val posX: Int,
    override val posY: Int,
    override var recolectado: Boolean = false
) : Recolectable {

    override val nombre: String
        get() = tipo.nombreMineral

    override val peso: Double
        get() = tipo.peso

    override val valor: Int
        get() = tipo.valor

    val zona: String
        get() = tipo.zona

    /**
     * Marca el ejemplar como recolectado.
     */
    fun marcarRecolectado() {
        recolectado = true
    }

    /**
     * Evalúa si el mineral corresponde a una clase pesada (>= 3.0 kg).
     */
    fun esPesado(): Boolean = peso >= 3.0

    override fun toString(): String {
        return "Mineral(id='$id', nombre='$nombre', zona='$zona', peso=${peso}kg, valor=$$valor, pos=($posX, $posY), recolectado=$recolectado)"
    }
}
