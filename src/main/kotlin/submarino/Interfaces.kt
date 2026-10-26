package submarino

/**
 * Contrato que define cualquier entidad que posee coordenadas enteras en una cuadrícula 2D.
 * Aplica ISP (Interface Segregation Principle) manteniendo el contrato mínimo para posicionamiento.
 */
interface Posicionable {
    val posX: Int
    val posY: Int
}

/**
 * Segregación de Interfaces (ISP):
 * Contrato para cualquier entidad u objeto en el dominio que posee masa o peso medible en kilogramos.
 */
interface Pesable {
    val peso: Double
}

/**
 * Segregación de Interfaces (ISP):
 * Contrato para objetos capaces de ser transportados o cargados en un compartimento de almacenamiento.
 * Hereda de [Pesable], garantizando que todo objeto cargable posee una masa cuantificable.
 */
interface Cargable : Pesable

/**
 * Contrato que define un elemento del juego que puede ser recolectado y almacenado.
 * Extiende [Posicionable] y [Cargable] para garantizar que todo recolectable posee ubicación y masa.
 * Aplica OCP (Open/Closed Principle) y LSP (Liskov Substitution Principle).
 */
interface Recolectable : Posicionable, Cargable, Vendible {
    val id: String
    val nombre: String
    override val peso: Double
    override val valor: Int
    var recolectado: Boolean

    fun marcarRecolectado() {
        recolectado = true
    }
}

/**
 * Contrato para sistemas de inventario y almacenamiento de ítems recolectables.
 * Implementa [Pesable] reportando su peso actual acumulado.
 * Aplica DIP (Dependency Inversion Principle) y OCP al operar únicamente contra la abstracción [Recolectable].
 */
interface Almacenamiento : Pesable {
    val pesoActual: Double
    override val peso: Double
        get() = pesoActual
    val capacidadMaxima: Double

    val capacidadDisponible: Double
        get() = (capacidadMaxima - pesoActual).coerceAtLeast(0.0)

    val cantidadItems: Int
        get() = obtenerElementos().size

    val valorTotal: Int
        get() = obtenerElementos().sumOf { it.valor }

    /**
     * Intenta almacenar un ítem en el contenedor respetando la capacidad máxima de peso.
     * @param item Objeto recolectable a ingresar en la bodega.
     * @return `true` si el ítem fue agregado con éxito, `false` si excede la capacidad disponible.
     */
    fun agregar(item: Recolectable): Boolean

    /**
     * Retorna una lista inmutable de los elementos almacenados actualmente.
     */
    fun obtenerElementos(): List<Recolectable>

    /**
     * Limpia o vacía los elementos almacenados en el contenedor.
     */
    fun vaciar() {}

    /**
     * Amplía la capacidad máxima de almacenamiento en kilogramos.
     */
    fun aumentarCapacidad(incremento: Double) {}

    /**
     * Determina si el compartimento se encuentra a plena capacidad.
     */
    fun estaLlena(): Boolean = pesoActual >= capacidadMaxima
}

/**
 * Contrato para generadores de minerales en el mapa submarino.
 * Aplica ISP y DIP permitiendo diferentes algoritmos de generación procedural.
 */
interface GeneradorMinerales {
    fun generarEnParedes(
        anchoMapa: Int,
        altoMapa: Int,
        profundidadActual: Double,
        cantidad: Int
    ): List<Recolectable>
}

/**
 * Contrato para el subsistema de detección de colisiones espaciales.
 * Aplica SRP (Single Responsibility Principle) e ISP aislando la lógica de contacto físico entre entidades.
 */
interface DetectorColisiones {
    fun verificarColision(
        posicion: Posicionable,
        elementos: List<Recolectable>
    ): Recolectable?
}
