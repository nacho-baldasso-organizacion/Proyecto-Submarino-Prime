package submarino

import javafx.scene.layout.VBox
import java.util.Locale

/**
 * Indicador de oxígeno porcentual con barra según el estado de navegación.
 */
class IndicadorOxigenoNavegacionHUD(
    estiloTitulo: String,
    estiloValor: String,
    anchoBarra: Double,
    estiloBarraEstable: String,
    estiloBarraPrecaucion: String,
    estiloBarraCritico: String,
    estiloBarraAgotado: String
) {

    private val barra: BarraEstadoOxigenoHUD = BarraEstadoOxigenoHUD(
        anchoBarra,
        estiloBarraEstable,
        estiloBarraPrecaucion,
        estiloBarraCritico,
        estiloBarraAgotado
    )
    private val tarjeta: TarjetaHUD = TarjetaHUD("OXÍGENO", estiloTitulo, estiloValor, barra.nodo)

    fun nodo(): VBox = tarjeta.nodo()

    fun actualizar(oxigeno: Double, estado: EstadoOxigeno) {
        val progreso = (oxigeno / ModeloNavegacion.OXIGENO_MAXIMO).coerceIn(0.0, 1.0)
        tarjeta.actualizar(String.format(Locale.US, "%.1f %%", oxigeno))
        barra.actualizar(progreso, estado)
    }
}

/**
 * Indicador textual del estado de oxígeno con color asociado.
 */
class IndicadorEstadoOxigenoHUD(
    estiloTitulo: String,
    estiloValor: String
) {

    private val tarjeta: TarjetaHUD = TarjetaHUD("ESTADO", estiloTitulo, estiloValor)

    private val colores: Map<EstadoOxigeno, String> = mapOf(
        EstadoOxigeno.ESTABLE to "-fx-text-fill: #6EE7B7;",
        EstadoOxigeno.PRECAUCION to "-fx-text-fill: #FBBF24;",
        EstadoOxigeno.CRITICO to "-fx-text-fill: #FB7185;",
        EstadoOxigeno.AGOTADO to "-fx-text-fill: #EF4444;"
    )

    fun nodo(): VBox = tarjeta.nodo()

    fun actualizar(estado: EstadoOxigeno) {
        tarjeta.actualizar(estado.etiqueta, colores.getValue(estado))
    }
}
