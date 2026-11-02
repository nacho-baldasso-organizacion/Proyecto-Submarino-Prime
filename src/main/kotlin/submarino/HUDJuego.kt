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
 * HUD fijo de la partida.
 *
 * Muestra profundidad, posición horizontal, oxígeno, casco, bodega, monedas,
 * nodos restantes y estado de acople. Todos los nodos quedan encapsulados.
 */
class HUDJuego {

    private val contenedor: VBox = VBox(8.0)
    private val valorProfundidad: Label = crearValor()
    private val valorPosicionX: Label = crearValor()
    private val valorPresion: Label = crearValor()
    private val valorOxigeno: Label = crearValor()
    private val valorCasco: Label = crearValor()
    private val valorBodega: Label = crearValor()
    private val valorMonedas: Label = crearValor()
    private val valorNodos: Label = crearValor()
    private val valorEstado: Label = crearValor()
    private val barraPresion: ProgressBar = ProgressBar(0.0)
    private val barraOxigeno: ProgressBar = ProgressBar(1.0)
    private val barraCasco: ProgressBar = ProgressBar(1.0)

    init {
        configurarBarra(barraPresion)
        configurarBarra(barraOxigeno)
        configurarBarra(barraCasco)

        contenedor.alignment = Pos.TOP_CENTER
        contenedor.style = ESTILO_CONTENEDOR
        contenedor.maxWidth = Region.USE_PREF_SIZE
        contenedor.maxHeight = Region.USE_PREF_SIZE
        contenedor.isMouseTransparent = true

        val filaSuperior = HBox(14.0).apply {
            alignment = Pos.CENTER_LEFT
            children.addAll(
                crearTarjeta("PROFUNDIDAD", valorProfundidad),
                crearSeparador(),
                crearTarjeta("POSICIÓN X", valorPosicionX),
                crearSeparador(),
                crearTarjetaBarra("PRESIÓN", valorPresion, barraPresion),
                crearSeparador(),
                crearTarjetaBarra("OXÍGENO", valorOxigeno, barraOxigeno),
                crearSeparador(),
                crearTarjetaBarra("CASCO", valorCasco, barraCasco),
                crearSeparador(),
                crearTarjeta("MONEDAS", valorMonedas)
            )
        }

        val filaInferior = HBox(14.0).apply {
            alignment = Pos.CENTER_LEFT
            children.addAll(
                crearTarjeta("BODEGA", valorBodega),
                crearSeparador(),
                crearTarjeta("NODOS", valorNodos),
                crearSeparador(),
                crearTarjeta("ESTADO", valorEstado),
                crearSeparador(),
                crearAyuda()
            )
        }

        contenedor.children.addAll(filaSuperior, filaInferior)
    }

    fun obtenerNodo(): Parent = contenedor

    fun actualizar(estado: EstadoJuego) {
        val capacidadOxigeno = estado.capacidadOxigeno.coerceAtLeast(1.0)
        val porcentajeOxigeno = (estado.oxigeno / capacidadOxigeno * 100.0).coerceIn(0.0, 100.0)
        val presionMaxima = estado.presionMaxima.coerceAtLeast(1.0)
        val porcentajePresion = estado.presionActual / presionMaxima * 100.0
        valorProfundidad.text = String.format(Locale.US, "%.0f m", estado.profundidad)
        valorPosicionX.text = String.format(Locale.US, "%.0f m", estado.posicionX)
        valorPresion.text = String.format(Locale.US, "%.1f MPa", estado.presionActual / MEGAPASCAL)
        valorOxigeno.text = String.format(Locale.US, "%.0f %%", porcentajeOxigeno)
        valorCasco.text = String.format(Locale.US, "%.0f %%", estado.integridadCasco)
        valorBodega.text = String.format(
            Locale.US,
            "%.1f / %.1f kg · %d",
            estado.pesoBodega,
            estado.capacidadBodega,
            estado.cantidadItems
        )
        valorMonedas.text = "$ ${estado.monedas}"
        valorNodos.text = estado.nodosDisponibles.toString()
        valorEstado.text = if (estado.acoplado) "ACOPLADO" else "EXPLORANDO"
        valorEstado.style = ESTILO_VALOR_BASE + if (estado.acoplado) {
            "-fx-text-fill: #6EE7B7;"
        } else {
            "-fx-text-fill: #7FE3F0;"
        }

        barraOxigeno.progress = (porcentajeOxigeno / 100.0).coerceIn(0.0, 1.0)
        barraOxigeno.style = estiloBarra(porcentajeOxigeno)
        barraPresion.progress = (porcentajePresion / 100.0).coerceIn(0.0, 1.0)
        barraPresion.style = estiloBarra(porcentajePresion)
        valorPresion.style = ESTILO_VALOR_BASE + if (porcentajePresion >= 100.0) {
            "-fx-text-fill: #FF5A36;"
        } else if (porcentajePresion >= UMBRAL_PRESION_PRECAUCION) {
            "-fx-text-fill: #FBBF24;"
        } else {
            "-fx-text-fill: #E6F7FA;"
        }
        barraCasco.progress = (estado.integridadCasco / CASCO_MAXIMO).coerceIn(0.0, 1.0)
        barraCasco.style = estiloBarra(estado.integridadCasco)
    }

    private fun configurarBarra(barra: ProgressBar) {
        barra.prefWidth = ANCHO_BARRA
        barra.minWidth = ANCHO_BARRA
        barra.maxWidth = ANCHO_BARRA
        barra.style = ESTILO_BARRA_ESTABLE
    }

    private fun crearTarjeta(titulo: String, valor: Label): VBox {
        val tarjeta = VBox(2.0)
        tarjeta.alignment = Pos.CENTER_LEFT
        tarjeta.children.addAll(crearTitulo(titulo), valor)
        return tarjeta
    }

    private fun crearTarjetaBarra(titulo: String, valor: Label, barra: ProgressBar): VBox {
        val fila = HBox(8.0)
        fila.alignment = Pos.CENTER_LEFT
        fila.children.addAll(valor, barra)

        val tarjeta = VBox(2.0)
        tarjeta.alignment = Pos.CENTER_LEFT
        tarjeta.children.addAll(crearTitulo(titulo), fila)
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

    private fun crearAyuda(): Label = Label("WASD / FLECHAS · ESPACIO: MINAR · E: ACOPLAR").apply {
        style = ESTILO_AYUDA
    }

    private fun estiloBarra(valor: Double): String = when {
        valor <= 20.0 -> ESTILO_BARRA_CRITICO
        valor <= 50.0 -> ESTILO_BARRA_PRECAUCION
        else -> ESTILO_BARRA_ESTABLE
    }

    companion object {
        private const val ESPACIADO = 14.0
        private const val ANCHO_BARRA = 92.0
        private const val CASCO_MAXIMO = 100.0
        private const val UMBRAL_PRESION_PRECAUCION = 80.0
        private const val MEGAPASCAL = 1_000_000.0

        private const val ESTILO_CONTENEDOR =
            "-fx-background-color: rgba(2, 20, 28, 0.86);" +
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
                "-fx-font-size: 15px;" +
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
    }
}
