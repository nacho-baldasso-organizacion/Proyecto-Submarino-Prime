package submarino

/**
 * Instantánea inmutable del estado de la partida que consume la capa JavaFX.
 *
 * Además de transportar datos, expone consultas de dominio para que la vista no
 * tenga que recalcular porcentajes ni reglas de peligro.
 */
data class EstadoJuego(
    val posicionX: Double,
    val profundidad: Double,
    val oxigeno: Double,
    val capacidadOxigeno: Double,
    val integridadCasco: Double,
    val monedas: Int,
    val pesoBodega: Double,
    val capacidadBodega: Double,
    val cantidadItems: Int,
    val nodosDisponibles: Int,
    val enZonaAcople: Boolean,
    val acoplado: Boolean,
    val velocidad: Double,
    val direccionHorizontal: Double,
    val mensaje: String,
    val destruido: Boolean = false,
    val causaDestruccion: String = "",
    val presionActual: Double = 0.0,
    val presionMaxima: Double = 1.0
) {
    fun porcentajeOxigeno(): Double = porcentaje(oxigeno, capacidadOxigeno)

    fun porcentajeCasco(): Double = porcentaje(integridadCasco, INTEGRIDAD_MAXIMA)

    fun porcentajeBodega(): Double = porcentaje(pesoBodega, capacidadBodega)

    fun presionSegura(): Boolean = presionActual <= presionMaxima

    fun enPeligro(): Boolean = destruido || !presionSegura() || oxigeno <= OXIGENO_MINIMO

    private fun porcentaje(valor: Double, maximo: Double): Double =
        if (maximo <= 0.0) 0.0 else (valor / maximo * 100.0).coerceIn(0.0, 100.0)

    companion object {
        const val INTEGRIDAD_MAXIMA = 100.0
        const val OXIGENO_MINIMO = 0.0
    }
}
