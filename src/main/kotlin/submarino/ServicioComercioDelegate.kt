package submarino

/**
 * Sub-fábrica delegada de creación para el servicio de comercio.
 * Concentra las llamadas a [FabricaServicios] para reducir el acoplamiento (CBO)
 * de la clase [ServicioComercio], manteniendo la firma pública inalterable.
 */
object ServicioComercioDelegate {

    fun crearTransaccionComercial(
        itemsVendidos: List<Vendible>,
        totalCreditos: Int,
        cantidadItems: Int
    ): TransaccionComercial =
        FabricaServicios.crearTransaccionComercial(
            itemsVendidos = itemsVendidos,
            totalCreditos = totalCreditos,
            cantidadItems = cantidadItems
        )

    fun crearResultadoComercioExito(
        transaccion: TransaccionComercial,
        saldoActualizado: Int
    ): ResultadoComercio.Exito =
        FabricaServicios.crearResultadoComercioExito(
            transaccion = transaccion,
            saldoActualizado = saldoActualizado
        )
}