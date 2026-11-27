package submarino

internal data class BodegaEstado(
    var capacidadMaxima: Double,
    val items: MutableList<Recolectable>
)

/**
 * Sistema de almacenamiento y bodega de carga a bordo del submarino.
 * Controla la capacidad máxima de peso en kilogramos y gestiona la colección de ítems recolectados.
 *
 * Cumple con:
 * - Single Responsibility Principle (SRP): se ocupa exclusivamente de la gestión del inventario y del límite de carga.
 * - Open/Closed Principle (OCP): almacena cualquier objeto que cumpla con [Recolectable] sin requerir modificaciones.
 * - Liskov Substitution Principle (LSP): cualquier implementación de [Recolectable] es válida y almacenable.
 * - Encapsulamiento estricto: campos y variables con visibilidad privada o 'private set'.
 */
class Bodega(
    capacidadMaxima: Double
) : Almacenamiento {

    private val estado: BodegaEstado = BodegaEstado(
        capacidadMaxima = capacidadMaxima,
        items = mutableListOf()
    )

    override var capacidadMaxima: Double = capacidadMaxima
        private set

    init {
        require(capacidadMaxima >= 0.0) {
            "La capacidad máxima de la bodega no puede ser negativa: $capacidadMaxima"
        }
    }

    override fun aumentarCapacidad(incremento: Double) {
        require(incremento > 0.0) {
            "El incremento de capacidad debe ser estrictamente positivo: $incremento"
        }
        estado.capacidadMaxima += incremento
        capacidadMaxima += incremento
    }

    override val pesoActual: Double
        get() = estado.items.sumOf { it.peso }

    override val capacidadDisponible: Double
        get() = (capacidadMaxima - pesoActual).coerceAtLeast(0.0)

    override val cantidadItems: Int
        get() = estado.items.size

    override val valorTotal: Int
        get() = estado.items.sumOf { it.valor }

    override fun estaLlena(): Boolean = pesoActual >= capacidadMaxima

    override fun agregar(item: Recolectable): Boolean {
        if (item.recolectado) {
            return false
        }
        if (pesoActual + item.peso <= capacidadMaxima) {
            estado.items.add(item)
            item.marcarRecolectado()
            return true
        }
        return false
    }

    override fun obtenerElementos(): List<Recolectable> = estado.items.toList()

    override fun vaciar() {
        estado.items.clear()
    }

    fun obtenerCapacidadMaxima(): Double = capacidadMaxima

    fun obtenerPesoActual(): Double = pesoActual

    fun obtenerCapacidadDisponible(): Double = capacidadDisponible

    fun obtenerCantidadItems(): Int = cantidadItems

    fun obtenerValorTotal(): Int = valorTotal

    override fun toString(): String {
        return "Bodega(pesoActual=${String.format("%.2f", pesoActual)}/${String.format("%.2f", capacidadMaxima)} kg, " +
                "items=${estado.items.size}, valorTotal=${estado.items.sumOf { it.valor }})"
    }
}