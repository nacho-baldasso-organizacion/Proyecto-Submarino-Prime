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

    internal data class Estado(
        val movimiento: GestorMovimiento,
        val gestorAscenso: GestorAscenso,
        val posicionActual: () -> Pair<Double, Double>,
        val velocidadAscensoActual: () -> Double,
        val pesoBaseActual: () -> Double,
        val bodegaActual: () -> Almacenamiento?
    )

    private val estado = Estado(
        movimiento,
        gestorAscenso,
        posicionActual,
        velocidadAscensoActual,
        pesoBaseActual,
        bodegaActual
    )

    fun mover(deltaX: Double, deltaY: Double): Boolean =
        estado.movimiento.mover(deltaX, deltaY)

    fun navegar(deltaX: Double): Boolean = mover(deltaX = deltaX, deltaY = 0.0)

    fun sumergir(deltaY: Double): Boolean = mover(deltaX = 0.0, deltaY = deltaY)

    fun ascender(deltaY: Double): Boolean {
        val desplazamientoEfectivo = estado.gestorAscenso.desplazamientoAscenso(
            deltaY,
            estado.pesoBaseActual(),
            estado.bodegaActual()
        )
        return mover(deltaX = 0.0, deltaY = -desplazamientoEfectivo)
    }

    fun ascender(): Boolean = mover(deltaX = 0.0, deltaY = -estado.velocidadAscensoActual())

    fun estaEnSuperficie(): Boolean = estado.posicionActual().second <= 0.0

    fun estaEnZona(zona: ZonaAcople): Boolean {
        val (x, y) = estado.posicionActual()
        return zona.estaEnZona(x, y)
    }
}
