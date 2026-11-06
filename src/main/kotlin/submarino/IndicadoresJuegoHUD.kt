package submarino

import javafx.scene.layout.VBox
import java.util.Locale

/**
 * Indicador de presión con barra y color de valor según umbrales.
 */
class IndicadorPresionHUD(
    estiloTitulo: String,
    estiloValor: String,
    anchoBarra: Double,
    estiloBarraEstable: String,
    estiloBarraPrecaucion: String,
    estiloBarraCritico: String
) {

    private val barra: BarraUmbralHUD = FabricaHUD.crearBarraUmbralHUD(
        anchoBarra,
        0.0,
        estiloBarraEstable,
        estiloBarraPrecaucion,
        estiloBarraCritico
    )
    private val tarjeta: TarjetaHUD = FabricaHUD.crearTarjetaHUD("PRESIÓN", estiloTitulo, estiloValor, barra.nodo)

    private val colores: List<Pair<Double, String>> = listOf(
        100.0 to "-fx-text-fill: #FF5A36;",
        UMBRAL_PRECAUCION to "-fx-text-fill: #FBBF24;"
    )

    fun nodo(): VBox = tarjeta.nodo()

    fun actualizar(presionActual: Double, presionMaxima: Double) {
        val maxima = presionMaxima.coerceAtLeast(1.0)
        val porcentaje = presionActual / maxima * 100.0
        tarjeta.actualizar(
            String.format(Locale.US, "%.1f MPa", presionActual / MEGAPASCAL),
            color(porcentaje)
        )
        barra.actualizar(porcentaje)
    }

    private fun color(porcentaje: Double): String =
        colores.firstOrNull { porcentaje >= it.first }?.second ?: "-fx-text-fill: #E6F7FA;"

    companion object {
        private const val MEGAPASCAL = 1_000_000.0
        private const val UMBRAL_PRECAUCION = 80.0
    }
}

/**
 * Indicador de oxígeno porcentual con barra de umbrales.
 */
class IndicadorOxigenoJuegoHUD(
    estiloTitulo: String,
    estiloValor: String,
    anchoBarra: Double,
    estiloBarraEstable: String,
    estiloBarraPrecaucion: String,
    estiloBarraCritico: String
) {

    private val barra: BarraUmbralHUD = FabricaHUD.crearBarraUmbralHUD(
        anchoBarra,
        1.0,
        estiloBarraEstable,
        estiloBarraPrecaucion,
        estiloBarraCritico
    )
    private val tarjeta: TarjetaHUD = FabricaHUD.crearTarjetaHUD("OXÍGENO", estiloTitulo, estiloValor, barra.nodo)

    fun nodo(): VBox = tarjeta.nodo()

    fun actualizar(oxigeno: Double, capacidadOxigeno: Double) {
        val capacidad = capacidadOxigeno.coerceAtLeast(1.0)
        val porcentaje = (oxigeno / capacidad * 100.0).coerceIn(0.0, 100.0)
        tarjeta.actualizar(String.format(Locale.US, "%.0f %%", porcentaje))
        barra.actualizar(porcentaje)
    }
}

/**
 * Indicador de integridad del casco con barra de umbrales.
 */
class IndicadorCascoHUD(
    estiloTitulo: String,
    estiloValor: String,
    anchoBarra: Double,
    estiloBarraEstable: String,
    estiloBarraPrecaucion: String,
    estiloBarraCritico: String
) {

    private val barra: BarraUmbralHUD = FabricaHUD.crearBarraUmbralHUD(
        anchoBarra,
        1.0,
        estiloBarraEstable,
        estiloBarraPrecaucion,
        estiloBarraCritico
    )
    private val tarjeta: TarjetaHUD = FabricaHUD.crearTarjetaHUD("CASCO", estiloTitulo, estiloValor, barra.nodo)

    fun nodo(): VBox = tarjeta.nodo()

    fun actualizar(integridad: Double) {
        tarjeta.actualizar(String.format(Locale.US, "%.0f %%", integridad))
        barra.actualizar(integridad)
    }
}

/**
 * Indicador de peso y cantidad de items de la bodega.
 */
class IndicadorCargoHUD(
    estiloTitulo: String,
    estiloValor: String
) {

    private val tarjeta: TarjetaHUD = FabricaHUD.crearTarjetaHUD("BODEGA", estiloTitulo, estiloValor)

    fun nodo(): VBox = tarjeta.nodo()

    fun actualizar(peso: Double, capacidad: Double, cantidadItems: Int) {
        tarjeta.actualizar(
            String.format(Locale.US, "%.1f / %.1f kg · %d", peso, capacidad, cantidadItems)
        )
    }
}

/**
 * Indicador del total de monedas.
 */
class IndicadorMonedasHUD(
    estiloTitulo: String,
    estiloValor: String
) {

    private val tarjeta: TarjetaHUD = FabricaHUD.crearTarjetaHUD("MONEDAS", estiloTitulo, estiloValor)

    fun nodo(): VBox = tarjeta.nodo()

    fun actualizar(monedas: Int) {
        tarjeta.actualizar("$ ${monedas}")
    }
}

/**
 * Indicador de nodos minerales restantes.
 */
class IndicadorNodosHUD(
    estiloTitulo: String,
    estiloValor: String
) {

    private val tarjeta: TarjetaHUD = FabricaHUD.crearTarjetaHUD("NODOS", estiloTitulo, estiloValor)

    fun nodo(): VBox = tarjeta.nodo()

    fun actualizar(nodosDisponibles: Int) {
        tarjeta.actualizar(nodosDisponibles.toString())
    }
}

/**
 * Indicador de estado de acople con color según la situación.
 */
class IndicadorAcopleHUD(
    estiloTitulo: String,
    estiloValor: String
) {

    private val tarjeta: TarjetaHUD = FabricaHUD.crearTarjetaHUD("ESTADO", estiloTitulo, estiloValor)

    fun nodo(): VBox = tarjeta.nodo()

    fun actualizar(acoplado: Boolean) {
        tarjeta.actualizar(
            if (acoplado) "ACOPLADO" else "EXPLORANDO",
            if (acoplado) "-fx-text-fill: #6EE7B7;" else "-fx-text-fill: #7FE3F0;"
        )
    }
}
