package submarino

import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

/**
 * Tarjeta del HUD con título, valor y, opcionalmente, una barra.
 */
class TarjetaHUD(
    titulo: String,
    private val estiloTitulo: String,
    private val estiloValorBase: String,
    barra: ProgressBar? = null
) {

    internal data class Estado(
        val titulo: String,
        val estiloTitulo: String,
        val estiloValorBase: String,
        val barra: ProgressBar?
    )

    private val state = Estado(
        titulo = titulo,
        estiloTitulo = estiloTitulo,
        estiloValorBase = estiloValorBase,
        barra = barra
    )

    val valor: Label = Label("--").apply {
        style = state.estiloValorBase
    }

    private val tarjeta: VBox = VBox(2.0).apply {
        alignment = Pos.CENTER_LEFT
        children.addAll(crearTitulo(titulo), crearContenido(barra))
    }

    fun nodo(): VBox = tarjeta

    fun actualizar(texto: String) {
        valor.text = texto
    }

    fun actualizar(texto: String, estiloTexto: String) {
        valor.text = texto
        valor.style = state.estiloValorBase + estiloTexto
    }

    private fun crearContenido(barra: ProgressBar?): Node {
        if (barra == null) {
            return valor
        }
        val fila = HBox(8.0).apply {
            alignment = Pos.CENTER_LEFT
            children.addAll(valor, barra)
        }
        return fila
    }

    private fun crearTitulo(texto: String): Label = Label(texto).apply {
        style = state.estiloTitulo
    }
}