package submarino

/**
 * Sub-fábrica delegada de creación de resultados de comercio y recolección.
 * Concentra métodos de instanciación de resultados comerciales y de recolección
 * con visibilidad pública, reduciendo el acoplamiento (CBO) de la fábrica raíz.
 */
object FabricaServiciosComercioYResultadosDelegate {

    fun crearResultadoRecoleccionExito(
        item: Recolectable,
        pesoAcumulado: Double
    ): ResultadoRecoleccion.Exito = ResultadoRecoleccion.Exito(item, pesoAcumulado)

    fun crearResultadoRecoleccionBodegaLlena(
        item: Recolectable,
        excesoPeso: Double,
        capacidadMaxima: Double
    ): ResultadoRecoleccion.BodegaLlena =
        ResultadoRecoleccion.BodegaLlena(item, excesoPeso, capacidadMaxima)

    fun crearResultadoRecoleccionYaRecolectado(
        item: Recolectable
    ): ResultadoRecoleccion.YaRecolectado = ResultadoRecoleccion.YaRecolectado(item)

    fun crearTransaccionComercial(
        itemsVendidos: List<Vendible>,
        totalCreditos: Int,
        cantidadItems: Int,
        timestamp: Long = System.currentTimeMillis()
    ): TransaccionComercial =
        TransaccionComercial(itemsVendidos, totalCreditos, cantidadItems, timestamp)

    fun crearResultadoComercioExito(
        transaccion: TransaccionComercial,
        saldoActualizado: Int
    ): ResultadoComercio.Exito = ResultadoComercio.Exito(transaccion, saldoActualizado)

    fun crearResultadoCompraExito(
        mejora: Mejora,
        nivelAnterior: Int,
        nuevoNivel: Int,
        costoPagado: Int,
        saldoRestante: Int
    ): ResultadoCompra.Exito = ResultadoCompra.Exito(mejora, nivelAnterior, nuevoNivel, costoPagado, saldoRestante)

    fun crearResultadoCompraFondosInsuficientes(
        mejoraId: String,
        costoRequerido: Int,
        saldoDisponible: Int
    ): ResultadoCompra.FondosInsuficientes =
        ResultadoCompra.FondosInsuficientes(mejoraId, costoRequerido, saldoDisponible)

    fun crearResultadoCompraNivelMaximoAlcanzado(
        mejoraId: String,
        nivelActual: Int,
        nivelMaximo: Int
    ): ResultadoCompra.NivelMaximoAlcanzado =
        ResultadoCompra.NivelMaximoAlcanzado(mejoraId, nivelActual, nivelMaximo)

    fun crearResultadoCompraMejoraNoEncontrada(
        mejoraId: String
    ): ResultadoCompra.MejoraNoEncontrada = ResultadoCompra.MejoraNoEncontrada(mejoraId)
}