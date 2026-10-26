package submarino

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

    override var capacidadMaxima: Double = capacidadMaxima
        private set

    init {
        require(capacidadMaxima >= 0.0) {
            "La capacidad máxima de la bodega no puede ser negativa: $capacidadMaxima"
        }
    }

    private val items: MutableList<Recolectable> = mutableListOf()

    /**
     * Amplía la capacidad máxima de peso en kilogramos de la bodega.
     *
     * @param incremento Cantidad de kilogramos a adicionar a la capacidad máxima actual.
     */
    override fun aumentarCapacidad(incremento: Double) {
        require(incremento > 0.0) {
            "El incremento de capacidad debe ser estrictamente positivo: $incremento"
        }
        capacidadMaxima += incremento
    }

    /**
     * Peso acumulado actual en kilogramos de todos los elementos contenidos en la bodega.
     */
    override val pesoActual: Double
        get() = items.sumOf { it.peso }

    /**
     * Capacidad de peso remanente disponible en la bodega en kilogramos.
     */
    override val capacidadDisponible: Double
        get() = (capacidadMaxima - pesoActual).coerceAtLeast(0.0)

    /**
     * Cantidad total de elementos almacenados en la bodega.
     */
    override val cantidadItems: Int
        get() = items.size

    /**
     * Valor total acumulado en dólares ($) de todos los ítems guardados en la bodega.
     */
    override val valorTotal: Int
        get() = items.sumOf { it.valor }

    /**
     * Indica si la bodega ha alcanzado o superado su capacidad de peso máximo.
     */
    override fun estaLlena(): Boolean = pesoActual >= capacidadMaxima

    /**
     * Intenta almacenar un ítem en la bodega respetando la capacidad máxima soportada.
     */
    override fun agregar(item: Recolectable): Boolean {
        if (item.recolectado) {
            return false
        }
        if (pesoActual + item.peso <= capacidadMaxima) {
            items.add(item)
            item.marcarRecolectado()
            return true
        }
        return false
    }

    /**
     * Retorna una lista inmutable con todos los elementos actualmente almacenados en la bodega.
     */
    override fun obtenerElementos(): List<Recolectable> = items.toList()

    /**
     * Limpia el inventario de la bodega.
     */
    override fun vaciar() {
        items.clear()
    }

    fun obtenerCapacidadMaxima(): Double = capacidadMaxima

    fun obtenerPesoActual(): Double = pesoActual

    fun obtenerCapacidadDisponible(): Double = capacidadDisponible

    fun obtenerCantidadItems(): Int = cantidadItems

    fun obtenerValorTotal(): Int = valorTotal

    override fun toString(): String {
        return "Bodega(pesoActual=${String.format("%.2f", pesoActual)}/${String.format("%.2f", capacidadMaxima)} kg, " +
                "items=$cantidadItems, valorTotal=$$valorTotal)"
    }
}
