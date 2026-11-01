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

    fun aumentarCapacidadBodega(incremento: Double) {
        gestorBodega.aumentarCapacidad(bodegaActual(), incremento)
    }

    fun aumentarVelocidadAscenso(incremento: Double) {
        propulsion.aumentarVelocidadAscenso(incremento)
    }

    fun aumentarResistenciaCasco(incrementoPa: Double) {
        sistemaCasco.reforzar(incrementoPa)
    }

    fun aumentarVelocidadPropulsion(incremento: Double) {
        gestorVelocidad.aumentarVelocidad(incremento)
    }
}
