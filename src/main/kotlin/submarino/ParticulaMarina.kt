package submarino

/**
 * Modelo de una partícula suspendida con su deriva y oscilación.
 */
internal data class ParticulaMarina(
    var x: Double,
    var y: Double,
    val radio: Double,
    val velocidadX: Double,
    val velocidadY: Double,
    val alfa: Double,
    val fase: Double
)
