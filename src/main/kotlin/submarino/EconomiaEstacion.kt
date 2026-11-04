package submarino

/**
 * Operaciones económicas disponibles mientras la nave está en la estación:
 * venta de bodega, recarga de oxígeno, reparación del casco y compra de mejoras.
 *
 * Responsabilidad única (SRP): encapsular las transacciones de la estación y
 * devolver el mensaje resultante, sin conocer la vista.
 */
class EconomiaEstacion(
    private val billetera: Monedero,
    private val casco: ModuloCasco,
    private val submarino: Submarino,
    private val servicioComercio: ServicioComercioContrato,
    private val tienda: TiendaMejoras
) {

    fun venderMinerales(): ResultadoComercio = servicioComercio.venderBodegaSubmarino(submarino)

    fun comprarMejora(idMejora: String): ResultadoCompra =
        tienda.comprarParaSubmarino(idMejora, submarino)

    /**
     * Recarga el oxígeno al máximo descontando monedas y notifica el nuevo valor
     * a través de [aplicarOxigeno].
     */
    fun recargarOxigeno(
        oxigenoActual: Double,
        oxigenoMaximo: Double,
        destruido: Boolean,
        aplicarOxigeno: (Double) -> Unit
    ): String {
        if (destruido) {
            return "El submarino está destruido. La estación no puede recargarlo."
        }
        if (oxigenoActual >= oxigenoMaximo - UMBRAL_COMPLETO) {
            return "El oxígeno ya está al máximo."
        }
        if (!billetera.debitar(COSTO_RECARGA_OXIGENO)) {
            return "Monedas insuficientes. Recarga cuesta $$COSTO_RECARGA_OXIGENO y tienes $${billetera.saldo}."
        }
        aplicarOxigeno(oxigenoMaximo)
        return "Oxígeno recargado al máximo (-$$COSTO_RECARGA_OXIGENO)."
    }

    /**
     * Repara el casco al máximo descontando monedas y devuelve el mensaje resultante.
     */
    fun repararCasco(destruido: Boolean): String {
        if (destruido) {
            return "El casco está destruido. La estación no puede repararlo."
        }
        if (casco.integridad >= CascoBase.INTEGRIDAD_MAXIMA - UMBRAL_COMPLETO) {
            return "El casco ya está en perfecto estado."
        }
        if (!billetera.debitar(COSTO_REPARACION_CASCO)) {
            return "Monedas insuficientes. Reparación cuesta $$COSTO_REPARACION_CASCO y tienes $${billetera.saldo}."
        }
        casco.reparar(CascoBase.INTEGRIDAD_MAXIMA)
        return "Casco reparado al 100% (-$$COSTO_REPARACION_CASCO)."
    }

    companion object {
        const val COSTO_RECARGA_OXIGENO = 60
        const val COSTO_REPARACION_CASCO = 100
        private const val UMBRAL_COMPLETO = 0.01
    }
}
