package submarino

import javafx.scene.layout.VBox
import java.util.Locale

/**
 * Indicador de una distancia en metros con formato configurable.
 */
class IndicadorMetrosHUD(
    titulo: String,
    estiloTitulo: String,
    estiloValor: String,
    private val patron: String
) {

    private val tarjeta: TarjetaHUD = TarjetaHUD(titulo, estiloTitulo, estiloValor)

    fun nodo(): VBox = tarjeta.nodo()

    fun actualizar(metros: Double) {
        tarjeta.actualizar(String.format(Locale.US, patron, metros))
    }
}
