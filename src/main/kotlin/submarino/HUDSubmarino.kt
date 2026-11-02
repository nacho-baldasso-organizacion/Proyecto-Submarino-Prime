package submarino

import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import java.util.Locale

/**
 * HUD fijo de la interfaz del submarino.
 *
 * Muestra profundidad, coordenada horizontal, oxígeno y estado operativo.
 * Todos los nodos y etiquetas quedan encapsulados como campos privados.
 */
class HUDSubmarino {

    private val contenedor: HBox = HBox(ESPACIADO)
    private val valorProfundidad: Label = crearValor()
    private val valorCoordenadaX: Label = crearValor()
    private val valorOxigeno: Label = crearValor()
    private val valorEstado: Label = crearValor()
    private val barraOxigeno: ProgressBar = ProgressBar(1.0)

    init {
        contenedor.alignment = Pos.CENTER_LEFT
        contenedor.style = ESTILO_CONTENEDOR
        contenedor.maxWidth = Region.USE_PREF_SIZE
        contenedor.maxHeight = Region.USE_PREF_SIZE
        contenedor.isMouseTransparent = true
        barraOxigeno.prefWidth = ANCHO_BARRA
        barraOxigeno.minWidth = ANCHO_BARRA
        barraOxigeno.maxWidth = ANCHO_BARRA
        barraOxigeno.style = ESTILO_BARRA_ESTABLE

        contenedor.children.addAll(
            crearTarjeta("PROFUNDIDAD", valorProfundidad),
            crearSeparador(),
            crearTarjeta("COORDENADA X", valorCoordenadaX),
            crearSeparador(),
            crearTarjetaOxigeno(),
            crearSeparador(),
            crearTarjeta("ESTADO", valorEstado),
            crearAyuda()
        )
    }

    /**
     * Nodo raíz del HUD para incorporar a la escena.
     */
    fun obtenerNodo(): Parent = contenedor

    /**
     * Refresca los indicadores con una instantánea del modelo.
     */
    fun actualizar(estado: EstadoNavegacion) {
        valorProfundidad.text = String.format(Locale.US, "%.1f m", estado.profundidad)
        valorCoordenadaX.text = String.format(Locale.US, "%.1f m", estado.coordenadaX)
        valorOxigeno.text = String.format(Locale.US, "%.1f %%", estado.oxigeno)
        barraOxigeno.progress = (estado.oxigeno / ModeloNavegacion.OXIGENO_MAXIMO)
            .coerceIn(0.0, 1.0)
        valorEstado.text = estado.estadoOxigeno.etiqueta
        valorEstado.style = ESTILO_VALOR_BASE + colorEstado(estado.estadoOxigeno)
        barraOxigeno.style = estiloBarra(estado.estadoOxigeno)
    }

    private fun crearTarjeta(titulo: String, valor: Label): VBox {
        val tarjeta = VBox(2.0)
        tarjeta.alignment = Pos.CENTER_LEFT
        tarjeta.children.addAll(crearTitulo(titulo), valor)
        return tarjeta
    }

    private fun crearTarjetaOxigeno(): VBox {
        val fila = HBox(8.0)
        fila.alignment = Pos.CENTER_LEFT
        fila.children.addAll(valorOxigeno, barraOxigeno)

        val tarjeta = VBox(2.0)
        tarjeta.alignment = Pos.CENTER_LEFT
        tarjeta.children.addAll(crearTitulo("OXÍGENO"), fila)
        return tarjeta
    }

    private fun crearTitulo(texto: String): Label = Label(texto).apply {
        style = ESTILO_TITULO
    }

    private fun crearValor(): Label = Label("--").apply {
        style = ESTILO_VALOR_BASE
    }

    private fun crearSeparador(): Region = Region().apply {
        prefWidth = 1.0
        minWidth = 1.0
        maxWidth = 1.0
        prefHeight = 34.0
        style = ESTILO_SEPARADOR
    }

    private fun crearAyuda(): Label = Label("WASD / FLECHAS").apply {
        style = ESTILO_AYUDA
    }

    private fun colorEstado(estado: EstadoOxigeno): String = when (estado) {
        EstadoOxigeno.ESTABLE -> "-fx-text-fill: #6EE7B7;"
        EstadoOxigeno.PRECAUCION -> "-fx-text-fill: #FBBF24;"
        EstadoOxigeno.CRITICO -> "-fx-text-fill: #FB7185;"
        EstadoOxigeno.AGOTADO -> "-fx-text-fill: #EF4444;"
    }

    private fun estiloBarra(estado: EstadoOxigeno): String = when (estado) {
        EstadoOxigeno.ESTABLE -> ESTILO_BARRA_ESTABLE
        EstadoOxigeno.PRECAUCION -> ESTILO_BARRA_PRECAUCION
        EstadoOxigeno.CRITICO -> ESTILO_BARRA_CRITICO
        EstadoOxigeno.AGOTADO -> ESTILO_BARRA_AGOTADO
    }

    companion object {
        private const val ESPACIADO = 14.0
        private const val ANCHO_BARRA = 110.0

        private const val ESTILO_CONTENEDOR =
            "-fx-background-color: rgba(2, 20, 28, 0.84);" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: rgba(127, 227, 240, 0.32);" +
                "-fx-border-radius: 14;" +
                "-fx-padding: 12 18 12 18;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.45), 18, 0.25, 0, 6);"

        private const val ESTILO_TITULO =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 9px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #7FA8B5;"

        private const val ESTILO_VALOR_BASE =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #E6F7FA;"

        private const val ESTILO_SEPARADOR =
            "-fx-background-color: rgba(127, 227, 240, 0.24);" +
                "-fx-background-radius: 1;"

        private const val ESTILO_AYUDA =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 10px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #5E8896;"

        private const val ESTILO_BARRA_ESTABLE = "-fx-accent: #34D399;"
        private const val ESTILO_BARRA_PRECAUCION = "-fx-accent: #FBBF24;"
        private const val ESTILO_BARRA_CRITICO = "-fx-accent: #FB7185;"
        private const val ESTILO_BARRA_AGOTADO = "-fx-accent: #EF4444;"
    }
}
