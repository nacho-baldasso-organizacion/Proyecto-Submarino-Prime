package submarino

/**
 * Componente especializado en aplicar mejoras a los subsistemas del submarino (SRP).
 *
 * Agrupa la orquestación de incrementos de capacidad de bodega, velocidad de ascenso,
 * resistencia del casco y propulsión horizontal, manteniendo a [Submarino] enfocado
 * en su estado y comportamiento esencial.
 */
class GestorMejorasSubmarino(
    private val propulsion: SistemaPropulsion,
    private val sistemaCasco: SistemaCasco,
    private val gestorBodega: GestorBodega,
    private val gestorVelocidad: GestorVelocidadNavegacion,
    private val bodegaActual: () -> Almacenamiento?
) {

    internal data class Estado(
        val propulsion: SistemaPropulsion,
        val sistemaCasco: SistemaCasco,
        val gestorBodega: GestorBodega,
        val gestorVelocidad: GestorVelocidadNavegacion,
        val bodegaActual: () -> Almacenamiento?
    )

    private val estado = Estado(
        propulsion,
        sistemaCasco,
        gestorBodega,
        gestorVelocidad,
        bodegaActual
    )

    fun aumentarCapacidadBodega(incremento: Double) {
        estado.gestorBodega.aumentarCapacidad(estado.bodegaActual(), incremento)
    }

    fun aumentarVelocidadAscenso(incremento: Double) {
        estado.propulsion.aumentarVelocidadAscenso(incremento)
    }

    fun aumentarResistenciaCasco(incrementoPa: Double) {
        estado.sistemaCasco.reforzar(incrementoPa)
    }

    fun aumentarVelocidadPropulsion(incremento: Double) {
        estado.gestorVelocidad.aumentarVelocidad(incremento)
    }
}
