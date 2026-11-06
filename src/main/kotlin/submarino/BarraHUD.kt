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

    private val barra: BarraHUD = FabricaHUD.crearBarraHUD(ancho, estiloEstable, progresoInicial)

    private val umbrales: List<Pair<Double, String>> = listOf(
        UMBRAL_CRITICO to estiloCritico,
        UMBRAL_PRECAUCION to estiloPrecaucion
    )

    val nodo: ProgressBar
        get() = barra.nodo

    fun actualizar(porcentaje: Double) {
        barra.actualizar(porcentaje / 100.0, estiloPara(porcentaje))
    }

    private fun estiloPara(valor: Double): String =
        umbrales.firstOrNull { valor <= it.first }?.second ?: estiloEstable

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

    private val barra: BarraHUD = FabricaHUD.crearBarraHUD(ancho, estiloEstable, 1.0)

    private val estilos: Map<EstadoOxigeno, String> = mapOf(
        EstadoOxigeno.ESTABLE to estiloEstable,
        EstadoOxigeno.PRECAUCION to estiloPrecaucion,
        EstadoOxigeno.CRITICO to estiloCritico,
        EstadoOxigeno.AGOTADO to estiloAgotado
    )

    val nodo: ProgressBar
        get() = barra.nodo

    fun actualizar(progreso: Double, estado: EstadoOxigeno) {
        barra.actualizar(progreso, estilos.getValue(estado))
    }
}
