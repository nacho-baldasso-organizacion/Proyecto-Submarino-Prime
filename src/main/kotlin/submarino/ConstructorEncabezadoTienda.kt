package submarino

import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox

/**
 * Construye el encabezado y la fila de estadísticas del modal de la tienda.
 *
 * Recibe las etiquetas de valores ya creadas para que la vista principal solo
 * se encargue de actualizarlas cuando cambia el estado de la sesión.
 */
class ConstructorEncabezadoTienda(
    private val etiquetaMonedas: Label,
    private val etiquetaOxigeno: Label,
    private val etiquetaCasco: Label,
    private val etiquetaBodega: Label,
    private val etiquetaNodos: Label
) {

    fun crearEncabezado(): HBox {
        val titulo = Label("ESTACIÓN BASE · TIENDA DE MEJORAS").apply {
            style = EstilosTienda.ESTILO_TITULO
        }
        val subtitulo = Label("Muelle de acople superficial").apply {
            style = EstilosTienda.ESTILO_SUBTITULO
        }
        val textos = VBox(2.0).apply {
            children.addAll(titulo, subtitulo)
        }
        val monedas = VBox(2.0).apply {
            alignment = Pos.CENTER_RIGHT
            children.addAll(
                Label("MONEDAS").apply { style = EstilosTienda.ESTILO_TITULO_PEQUENO },
                etiquetaMonedas
            )
        }
        val separador = Region().apply {
            HBox.setHgrow(this, Priority.ALWAYS)
        }

        return HBox(12.0).apply {
            alignment = Pos.CENTER_LEFT
            children.addAll(textos, separador, monedas)
        }
    }

    fun crearFilaEstadisticas(): HBox = HBox(10.0).apply {
        alignment = Pos.CENTER_LEFT
        children.addAll(
            ControlesTienda.crearTarjeta("OXÍGENO", etiquetaOxigeno),
            ControlesTienda.crearTarjeta("CASCO", etiquetaCasco),
            ControlesTienda.crearTarjeta("BODEGA", etiquetaBodega),
            ControlesTienda.crearTarjeta("NODOS", etiquetaNodos)
        )
    }
}
