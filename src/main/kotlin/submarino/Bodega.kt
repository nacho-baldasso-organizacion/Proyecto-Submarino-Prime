package submarino

/**
 * Sistema de almacenamiento y bodega de carga a bordo del submarino.
 * Controla la capacidad máxima de peso en kilogramos y gestiona la colección de ítems recolectados.
 *
 * Cumple con:
 * - Single Responsibility Principle (SRP): se ocupa exclusivamente de la gestión del inventario y del límite de carga.
 * - Open/Closed Principle (OCP): almacena cualquier objeto que cumpla con [Recolectable] sin requerir modificaciones.
 * - Liskov Substitution Principle (LSP): cualquier implementación de [Recolectable] es válida y almacenable.
 *
 * @property capacidadMaxima Peso máximo en kilogramos que la bodega puede albergar.
 */
class Bodega(
    override val capacidadMaxima: Double
) : Almacenamiento {

    init {
        require(capacidadMaxima >= 0.0) {
            "La capacidad máxima de la bodega no puede ser negativa: $capacidadMaxima"
        }
    }

    private val items: MutableList<Recolectable> = mutableListOf()

    /**
     * Peso acumulado actual en kilogramos de todos los elementos contenidos en la bodega.
     */
    override val pesoActual: Double
        get() = items.sumOf { it.peso }

    /**
     * Capacidad de peso remanente disponible en la bodega en kilogramos.
     */
    val capacidadDisponible: Double
        get() = (capacidadMaxima - pesoActual).coerceAtLeast(0.0)

    /**
     * Cantidad total de elementos almacenados en la bodega.
     */
    val cantidadItems: Int
        get() = items.size

    /**
     * Valor total acumulado en dólares ($) de todos los ítems guardados en la bodega.
     */
    val valorTotal: Int
        get() = items.sumOf { it.valor }

    /**
     * Indica si la bodega ha alcanzado o superado su capacidad de peso máximo.
     */
    fun estaLlena(): Boolean = pesoActual >= capacidadMaxima

    /**
     * Intenta almacenar un ítem en la bodega respetando la capacidad máxima soportada.
     * Si el peso actual sumado al peso del ítem no sobrepasa la [capacidadMaxima],
     * el ítem es agregado a la bodega, se marca su estado [Recolectable.recolectado] en true
     * y se retorna `true`.
     * Si sobrepasa la capacidad o ya fue recolectado, se rechaza y retorna `false`.
     *
     * @param item Objeto recolectable que se pretende almacenar.
     * @return `true` si el almacenamiento fue exitoso; `false` si excede la capacidad o ya fue recolectado.
     */
    override fun agregar(item: Recolectable): Boolean {
        if (item.recolectado) {
            return false
        }
        if (pesoActual + item.peso <= capacidadMaxima) {
            items.add(item)
            item.recolectado = true
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
    fun vaciar() {
        items.clear()
    }

    override fun toString(): String {
        return "Bodega(pesoActual=${String.format("%.2f", pesoActual)}/${String.format("%.2f", capacidadMaxima)} kg, " +
                "items=$cantidadItems, valorTotal=$$valorTotal)"
    }
}
