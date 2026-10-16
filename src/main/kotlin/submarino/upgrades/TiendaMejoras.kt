package submarino

/**
 * ============================================================================
 * FACHADA DE TIENDA DE MEJORAS (ISSUE #8)
 * PRINCIPIOS SOLID (SRP, OCP, LSP, ISP, DIP = 0 violaciones)
 *
 * Cumple con:
 * - Single Responsibility Principle (SRP): provee el punto de acceso unificado
 *   (fachada) para consulta del catálogo y solicitud de compras.
 * - Dependency Inversion Principle (DIP): depende exclusivamente de abstracciones
 *   ([ConsultableCatalogo], [ProcesadorCompras]).
 * - Interface Segregation Principle (ISP): implementa [TiendaMejorasContrato] (2 métodos).
 * ============================================================================
 */

/**
 * Fachada principal de la tienda de mejoras del submarino.
 * Desacopla la lógica de almacenamiento ([ConsultableCatalogo]) de la lógica transaccional ([ProcesadorCompras]).
 *
 * @param catalogo Abstracción de consulta del catálogo de mejoras.
 * @param procesadorCompras Abstracción de ejecución y procesamiento de compras.
 */
class TiendaMejoras(
    val catalogo: ConsultableCatalogo,
    val procesadorCompras: ProcesadorCompras
) : TiendaMejorasContrato {

    /**
     * Procesa la compra de una mejora dada su clave identificadora.
     *
     * @param idMejora Identificador de la mejora a adquirir.
     * @param pagable Billetera o cuenta de donde se debitan los fondos.
     * @param submarino Submarino que recibe el incremento en sus estadísticas.
     * @return [ResultadoCompra] con el resultado de la transacción.
     */
    override fun comprar(
        idMejora: String,
        pagable: Pagable,
        submarino: Submarino
    ): ResultadoCompra {
        return procesadorCompras.comprarMejora(idMejora, pagable, submarino)
    }

    /**
     * Retorna la lista completa de mejoras disponibles en la tienda.
     */
    override fun listarMejoras(): List<Mejora> = catalogo.obtenerTodas()

    /**
     * Sobrecarga de conveniencia semántica que utiliza la billetera incorporada del submarino.
     *
     * @param idMejora Identificador de la mejora a adquirir.
     * @param submarino Submarino comprador que aporta los fondos y recibe la mejora.
     * @return [ResultadoCompra] con el desenlace tipado de la operación.
     */
    fun comprarParaSubmarino(idMejora: String, submarino: Submarino): ResultadoCompra {
        return comprar(
            idMejora = idMejora,
            pagable = submarino.billetera,
            submarino = submarino
        )
    }

    /**
     * Consulta una mejora específica por su identificador.
     */
    fun obtenerMejora(id: String): Mejora? = catalogo.buscarPorId(id)

    /**
     * Cantidad total de mejoras registradas en la tienda.
     */
    val cantidadMejoras: Int
        get() = listarMejoras().size

    override fun toString(): String =
        "TiendaMejoras(mejorasDisponibles=$cantidadMejoras)"
}
