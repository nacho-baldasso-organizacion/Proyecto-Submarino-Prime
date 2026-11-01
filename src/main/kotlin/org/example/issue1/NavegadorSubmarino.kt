package submarino

/**
 * Componente especializado en la ejecución de maniobras de navegación (SRP).
 *
 * Traduce comandos de dominio (navegar, sumergir, ascender) en desplazamientos
 * concretos y delega la validación física del movimiento en [GestorMovimiento].
 */
class NavegadorSubmarino(
    private val movimiento: GestorMovimiento,
    private val gestorAscenso: GestorAscenso,
    private val posicionActual: () -> Pair<Double, Double>,
    private val velocidadAscensoActual: () -> Double,
    private val pesoBaseActual: () -> Double,
    private val bodegaActual: () -> Almacenamiento?
) {

    fun mover(deltaX: Double, deltaY: Double): Boolean =
        movimiento.mover(deltaX, deltaY)

    fun navegar(deltaX: Double): Boolean = mover(deltaX = deltaX, deltaY = 0.0)

    fun sumergir(deltaY: Double): Boolean = mover(deltaX = 0.0, deltaY = deltaY)

    fun ascender(deltaY: Double): Boolean {
        val desplazamientoEfectivo = gestorAscenso.desplazamientoAscenso(
            deltaY,
            pesoBaseActual(),
            bodegaActual()
        )
        return mover(deltaX = 0.0, deltaY = -desplazamientoEfectivo)
    }

    fun ascender(): Boolean = mover(deltaX = 0.0, deltaY = -velocidadAscensoActual())

    fun estaEnSuperficie(): Boolean = posicionActual().second <= 0.0

    fun estaEnZona(zona: ZonaAcople): Boolean {
        val (x, y) = posicionActual()
        return zona.estaEnZona(x, y)
    }
}
