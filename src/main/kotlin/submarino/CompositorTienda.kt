package submarino

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.Separator
import javafx.scene.layout.Priority
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

/**
 * Compone el fondo y el panel del modal de la tienda a partir de los
 * constructores de encabezado, acciones y filas de mejoras.
 */
class CompositorTienda(
    private val raiz: StackPane,
    private val constructorEncabezado: ConstructorEncabezadoTienda,
    private val constructorAcciones: ConstructorAccionesTienda,
    private val etiquetaMensaje: Label,
    private val contenedorMejoras: VBox
) {

    fun componer() {
        val fondo = Rectangle().apply {
            fill = Color.rgb(0, 0, 0, 0.64)
            widthProperty().bind(raiz.widthProperty())
            heightProperty().bind(raiz.heightProperty())
        }

        val panel = VBox(12.0).apply {
            style = EstilosTienda.ESTILO_PANEL
            maxWidth = 860.0
            maxHeight = 620.0
            children.addAll(
                constructorEncabezado.crearEncabezado(),
                constructorEncabezado.crearFilaEstadisticas(),
                constructorAcciones.crearFilaAcciones(),
                etiquetaMensaje,
                Separator().apply { style = "-fx-background-color: rgba(127, 227, 240, 0.22);" },
                crearSeccionMejoras()
            )
        }

        raiz.children.addAll(fondo, panel)
        StackPane.setAlignment(panel, Pos.CENTER)
        StackPane.setMargin(panel, Insets(24.0))
        raiz.isVisible = false
        raiz.isMouseTransparent = true
    }

    private fun crearSeccionMejoras(): VBox {
        val titulo = Label("MEJORAS DISPONIBLES").apply {
            style = EstilosTienda.ESTILO_TITULO_PEQUENO
        }
        val scroll = ScrollPane(contenedorMejoras).apply {
            setFitToWidth(true)
            prefHeight = 230.0
            style = EstilosTienda.ESTILO_SCROLL
        }
        VBox.setVgrow(scroll, Priority.ALWAYS)

        return VBox(8.0).apply {
            children.addAll(titulo, scroll)
        }
    }
}
