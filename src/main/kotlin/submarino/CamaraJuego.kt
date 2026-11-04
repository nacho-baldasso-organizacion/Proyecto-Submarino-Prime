package submarino

/**
 * Traduce coordenadas del mundo a la ventana visible de la cámara.
 *
 * Responsabilidad única (SRP): cálculo de los límites de la cámara en X e Y.
 */
class CamaraJuego(
    private val anchoMundo: Double,
    private val altoMundo: Double,
    private val anchoEscena: Double,
    private val altoEscena: Double,
    private val escala: Double
) {

    fun x(posicionX: Double): Double {
        val maximo = (anchoMundo - anchoEscena).coerceAtLeast(0.0)
        return (posicionX * escala - anchoEscena / 2.0).coerceIn(0.0, maximo)
    }

    fun y(profundidad: Double): Double {
        val maximo = (altoMundo - altoEscena).coerceAtLeast(0.0)
        return (profundidad * escala - altoEscena / 2.0)
            .coerceIn(CAMARA_Y_MINIMA, maximo)
    }

    companion object {
        private const val CAMARA_Y_MINIMA = -180.0
    }
}
