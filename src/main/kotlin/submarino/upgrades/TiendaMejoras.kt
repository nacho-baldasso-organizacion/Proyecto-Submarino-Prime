package submarino

/**
 * ============================================================================
 * FACHADA DE TIENDA DE MEJORAS (ISSUE #8)
 * PRINCIPIOS SOLID (SRP, OCP, LSP, ISP, DIP)
 * ============================================================================
 */

/**
 * Fachada principal de la tienda de mejoras del submarino.
 * Desacopla la lógica de almacenamiento ([ConsultableCatalogo]) de la lógica transaccional ([ProcesadorCompras]).
 */
class TiendaMejoras(
    private val catalogo: ConsultableCatalogo,
    private val procesadorCompras: ProcesadorCompras
) : TiendaMejorasContrato {

    override fun comprar(
        idMejora: String,
        pagable: Pagable,
        submarino: SubmarinoMejorable
    ): ResultadoCompra {
        return procesadorCompras.comprarMejora(idMejora, pagable, submarino)
    }

    override fun listarMejoras(): List<Mejora> = catalogo.obtenerTodas()

    fun comprarParaSubmarino(idMejora: String, submarino: SubmarinoMejorable): ResultadoCompra {
        return comprar(
            idMejora = idMejora,
            pagable = submarino.billetera,
            submarino = submarino
        )
    }

    fun obtenerMejora(id: String): Mejora? = catalogo.buscarPorId(id)

    val cantidadMejoras: Int
        get() = listarMejoras().size

    override fun toString(): String =
        "TiendaMejoras(mejorasDisponibles=$cantidadMejoras)"
}
