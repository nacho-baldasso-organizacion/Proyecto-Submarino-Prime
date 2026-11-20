package submarino

/**
 * Calcula y aplica el desgaste del casco por presión hidrostática y asfixia.
 *
 * Responsabilidad única (SRP): traducir profundidad y tiempo en daño estructural.
 * No decide cuándo termina la partida; solo informa el daño al [Casco].
 */
class GestorDanioSubmarino(
    private val casco: Casco
) {

    /**
     * Calcula la presión actual basándose en la profundidad.
     * El gradiente de presión aumenta linealmente con la profundidad,
     * haciendo que el casco explote antes (a menor profundidad) que con
     * una relación lineal pura.
     */
    fun presionActual(posicionY: Double): Double {
        // Presión cuadrática: aumenta más rápido que lineal a medida que profundizas
        val gradienteBase = 1025.0 * 9.81
        return gradienteBase * posicionY * (1.0 + 0.01 * posicionY)
    }

    fun aplicarDanioProfundidad(posicionY: Double, deltaSegundos: Double) {
        val presionMaxima = casco.presionMaximaPa
        val presion = presionActual(posicionY)
        if (presion <= presionMaxima) {
            return
        }
        val excesoRelativo = ((presion - presionMaxima) / presionMaxima).coerceIn(0.0, 1.0)
        val danio = excesoRelativo * DANIO_POR_EXCESO * deltaSegundos
        if (danio > 0.0) {
            casco.recibirPresion(presionMaxima * (1.0 + danio))
        }
    }

    fun aplicarDanioAsfixia(deltaSegundos: Double) {
        val danio = DANIO_POR_ASFIXIA * deltaSegundos
        casco.recibirPresion(casco.presionMaximaPa * (1.0 + danio))
    }

    companion object {
        private const val DANIO_POR_EXCESO = 6.0
        private const val DANIO_POR_ASFIXIA = 0.5
    }
}
