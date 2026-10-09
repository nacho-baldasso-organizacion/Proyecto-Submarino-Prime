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
 * Contrato que define un elemento del juego que puede ser recolectado y almacenado.
 * Extiende [Posicionable] para garantizar que todo recolectable posee una ubicación espacial.
 * Aplica OCP (Open/Closed Principle) y LSP (Liskov Substitution Principle),
 * permitiendo que cualquier nuevo objeto recolectable (minerales, artefactos, restos)
 * sea procesado uniformemente por el sistema sin alterar el código existente.
 */
interface Recolectable : Posicionable {
    val id: String
    val nombre: String
    val peso: Double
    val valor: Int
    var recolectado: Boolean
}

/**
 * Contrato para sistemas de inventario y almacenamiento de ítems recolectables.
 * Aplica DIP (Dependency Inversion Principle) y OCP al operar únicamente contra la abstracción [Recolectable].
 */
interface Almacenamiento {
    val pesoActual: Double
    val capacidadMaxima: Double

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
}

/**
 * Contrato para generadores de minerales en el mapa submarino.
 * Aplica ISP y DIP permitiendo diferentes algoritmos de generación procedural.
 */
interface GeneradorMinerales {
    /**
     * Genera una lista de minerales ubicados exclusivamente en los bordes o paredes del mapa.
     *
     * @param anchoMapa Ancho total de la cuadrícula del mapa (eje X).
     * @param altoMapa Alto total de la cuadrícula del mapa (eje Y).
     * @param profundidadActual Profundidad actual del submarino o sector (en metros).
     * @param cantidad Cantidad máxima de minerales a colocar en las paredes.
     * @return Lista de elementos [Recolectable] ubicados en las paredes.
     */
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
    /**
     * Verifica si una posición dada colisiona con algún recolectable disponible en la lista.
     *
     * @param posicion Posición del submarino u objeto explorador.
     * @param elementos Lista de elementos potencialmente colisionables en el mapa.
     * @return El [Recolectable] con el que se produjo la colisión, o `null` si no hay colisión.
     */
    fun verificarColision(
        posicion: Posicionable,
        elementos: List<Recolectable>
    ): Recolectable?
}

/**
 * Implementación inmutable de [Posicionable] para representar coordenadas en el plano 2D.
 */
data class Posicion(
    override val posX: Int,
    override val posY: Int
) : Posicionable
