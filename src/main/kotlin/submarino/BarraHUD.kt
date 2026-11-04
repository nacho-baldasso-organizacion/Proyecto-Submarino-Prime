package submarino

import javafx.scene.control.ProgressBar

/**
 * Barra de progreso de ancho fijo compartida por los widgets del HUD.
 */
class BarraHUD(
    ancho: Double,
    estiloInicial: String,
    progresoInicial: Double = 1.0
) {

    val nodo: ProgressBar = ProgressBar(progresoInicial).apply {
        prefWidth = ancho
        minWidth = ancho
        maxWidth = ancho
        style = estiloInicial
    }

    fun actualizar(progreso: Double, estilo: String) {
        nodo.progress = progreso.coerceIn(0.0, 1.0)
        nodo.style = estilo
    }
}

/**
 * Barra que cambia de color según umbrales porcentuales.
 */
class BarraUmbralHUD(
    ancho: Double,
    progresoInicial: Double,
    private val estiloEstable: String,
    private val estiloPrecaucion: String,
    private val estiloCritico: String
) {

    private val barra: BarraHUD = BarraHUD(ancho, estiloEstable, progresoInicial)

    val nodo: ProgressBar
        get() = barra.nodo

    fun actualizar(porcentaje: Double) {
        barra.actualizar(porcentaje / 100.0, estiloPara(porcentaje))
    }

    private fun estiloPara(valor: Double): String = when {
        valor <= UMBRAL_CRITICO -> estiloCritico
        valor <= UMBRAL_PRECAUCION -> estiloPrecaucion
        else -> estiloEstable
    }

    companion object {
        private const val UMBRAL_CRITICO = 20.0
        private const val UMBRAL_PRECAUCION = 50.0
    }
}

/**
 * Barra de oxígeno cuyo color depende del estado de navegación.
 */
class BarraEstadoOxigenoHUD(
    ancho: Double,
    private val estiloEstable: String,
    private val estiloPrecaucion: String,
    private val estiloCritico: String,
    private val estiloAgotado: String
) {

    private val barra: BarraHUD = BarraHUD(ancho, estiloEstable, 1.0)

    val nodo: ProgressBar
        get() = barra.nodo

    fun actualizar(progreso: Double, estado: EstadoOxigeno) {
        barra.actualizar(progreso, estiloPara(estado))
    }

    private fun estiloPara(estado: EstadoOxigeno): String = when (estado) {
        EstadoOxigeno.ESTABLE -> estiloEstable
        EstadoOxigeno.PRECAUCION -> estiloPrecaucion
        EstadoOxigeno.CRITICO -> estiloCritico
        EstadoOxigeno.AGOTADO -> estiloAgotado
    }
}
