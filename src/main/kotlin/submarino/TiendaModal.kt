package submarino

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.Separator
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import java.util.Locale

/**
 * Modal de la tienda de upgrades de la Estación Base.
 *
 * Se muestra como una capa semitransparente sobre la partida. Toda la lógica
 * económica y de mejoras se delega en [SesionJuego]; esta clase solo compone
 * controles y refleja el estado resultante.
 */
class TiendaModal(
    private val sesion: SesionJuego
) {

    private val raiz: StackPane = StackPane()
    private val etiquetaMonedas: Label = crearValor()
    private val etiquetaOxigeno: Label = crearValor()
    private val etiquetaCasco: Label = crearValor()
    private val etiquetaBodega: Label = crearValor()
    private val etiquetaNodos: Label = crearValor()
    private val etiquetaMensaje: Label = Label(
        "Vende minerales para financiar mejoras. Elige una mejora y pulsa COMPRAR."
    ).apply {
        style = ESTILO_MENSAJE
        isWrapText = true
        maxWidth = 760.0
    }
    private val contenedorMejoras: VBox = VBox(8.0).apply {
        style = "-fx-background-color: transparent;"
    }

    init {
        val fondo = Rectangle().apply {
            fill = Color.rgb(0, 0, 0, 0.64)
            widthProperty().bind(raiz.widthProperty())
            heightProperty().bind(raiz.heightProperty())
        }

        val panel = VBox(12.0).apply {
            style = ESTILO_PANEL
            maxWidth = 860.0
            maxHeight = 620.0
            children.addAll(
                crearEncabezado(),
                crearFilaEstadisticas(),
                crearFilaAcciones(),
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

    fun obtenerNodo(): Parent = raiz

    fun abrir() {
        raiz.isVisible = true
        raiz.isMouseTransparent = false
        refrescar()
    }

    fun cerrar() {
        raiz.isVisible = false
        raiz.isMouseTransparent = true
    }

    fun estaAbierto(): Boolean = raiz.isVisible

    fun refrescar() {
        val estado = sesion.estado()
        etiquetaMonedas.text = "$ ${estado.monedas}"
        etiquetaOxigeno.text = String.format(Locale.US, "%.0f / %.0f", estado.oxigeno, estado.capacidadOxigeno)
        etiquetaCasco.text = String.format(Locale.US, "%.0f %%", estado.integridadCasco)
        etiquetaBodega.text = String.format(
            Locale.US,
            "%.1f / %.1f kg",
            estado.pesoBodega,
            estado.capacidadBodega
        )
        etiquetaNodos.text = estado.nodosDisponibles.toString()
        reconstruirMejoras()
    }

    private fun crearEncabezado(): HBox {
        val titulo = Label("ESTACIÓN BASE · TIENDA DE MEJORAS").apply {
            style = ESTILO_TITULO
        }
        val subtitulo = Label("Muelle de acople superficial").apply {
            style = ESTILO_SUBTITULO
        }
        val textos = VBox(2.0).apply {
            children.addAll(titulo, subtitulo)
        }
        val monedas = VBox(2.0).apply {
            alignment = Pos.CENTER_RIGHT
            children.addAll(
                Label("MONEDAS").apply { style = ESTILO_TITULO_PEQUENO },
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

    private fun crearFilaEstadisticas(): HBox = HBox(10.0).apply {
        alignment = Pos.CENTER_LEFT
        children.addAll(
            crearTarjeta("OXÍGENO", etiquetaOxigeno),
            crearTarjeta("CASCO", etiquetaCasco),
            crearTarjeta("BODEGA", etiquetaBodega),
            crearTarjeta("NODOS", etiquetaNodos)
        )
    }

    private fun crearFilaAcciones(): HBox {
        val vender = crearBoton("VENDER MINERALES", "#34D399").apply {
            setOnAction {
                val resultado = sesion.venderMinerales()
                etiquetaMensaje.text = resultado.obtenerResumen()
                refrescar()
            }
        }
        val oxigeno = crearBoton(
            "RECARGAR OXÍGENO · $${SesionJuego.COSTO_RECARGA_OXIGENO}",
            "#38BDF8"
        ).apply {
            setOnAction {
                etiquetaMensaje.text = sesion.recargarOxigeno()
                refrescar()
            }
        }
        val casco = crearBoton(
            "REPARAR CASCO · $${SesionJuego.COSTO_REPARACION_CASCO}",
            "#FBBF24"
        ).apply {
            setOnAction {
                etiquetaMensaje.text = sesion.repararCasco()
                refrescar()
            }
        }
        val desacoplar = crearBoton("DESACOPLAR", "#FB7185").apply {
            setOnAction {
                sesion.desacoplar()
                cerrar()
            }
        }

        return HBox(10.0).apply {
            alignment = Pos.CENTER_LEFT
            children.addAll(vender, oxigeno, casco, desacoplar)
        }
    }

    private fun crearSeccionMejoras(): VBox {
        val titulo = Label("MEJORAS DISPONIBLES").apply {
            style = ESTILO_TITULO_PEQUENO
        }
        val scroll = ScrollPane(contenedorMejoras).apply {
            setFitToWidth(true)
            prefHeight = 230.0
            style = ESTILO_SCROLL
        }
        VBox.setVgrow(scroll, Priority.ALWAYS)

        return VBox(8.0).apply {
            children.addAll(titulo, scroll)
        }
    }

    private fun reconstruirMejoras() {
        contenedorMejoras.children.clear()
        sesion.catalogo().forEach { mejora ->
            contenedorMejoras.children.add(crearFilaMejora(mejora))
        }
    }

    private fun crearFilaMejora(mejora: Mejora): HBox {
        val nombre = Label("${mejora.nombre}  ·  Nv. ${mejora.nivel}/${mejora.nivelMaximo}").apply {
            style = ESTILO_MEJORA_NOMBRE
        }
        val descripcion = Label(mejora.descripcion).apply {
            style = ESTILO_MEJORA_DESCRIPCION
            isWrapText = true
            maxWidth = 520.0
        }
        val info = VBox(2.0).apply {
            children.addAll(nombre, descripcion)
        }
        val espacio = Region().apply {
            HBox.setHgrow(this, Priority.ALWAYS)
        }
        val costo = Label(if (mejora.esNivelMaximo) "MÁXIMO" else "$ ${mejora.costo}").apply {
            style = ESTILO_COSTO
        }
        val boton = crearBoton(
            if (mejora.esNivelMaximo) "MÁX" else "COMPRAR",
            if (mejora.esNivelMaximo) "#475569" else "#34D399"
        ).apply {
            isDisable = mejora.esNivelMaximo
            setOnAction {
                val resultado = sesion.comprarMejora(mejora.id)
                etiquetaMensaje.text = resultado.obtenerMensaje()
                refrescar()
            }
        }

        return HBox(12.0).apply {
            alignment = Pos.CENTER_LEFT
            style = ESTILO_FILA_MEJORA
            children.addAll(info, espacio, costo, boton)
        }
    }

    private fun crearTarjeta(titulo: String, valor: Label): VBox = VBox(2.0).apply {
        style = ESTILO_TARJETA
        children.addAll(
            Label(titulo).apply { style = ESTILO_TITULO_PEQUENO },
            valor
        )
    }

    private fun crearValor(): Label = Label("--").apply {
        style = ESTILO_VALOR
    }

    private fun crearBoton(texto: String, color: String): Button = Button(texto).apply {
        style = """
            -fx-background-color: $color;
            -fx-text-fill: #031926;
            -fx-font-family: 'Segoe UI';
            -fx-font-size: 11px;
            -fx-font-weight: bold;
            -fx-background-radius: 8;
            -fx-padding: 8 14 8 14;
            -fx-cursor: hand;
        """.trimIndent()
    }

    companion object {
        private const val ESTILO_PANEL =
            "-fx-background-color: rgba(3, 25, 38, 0.97);" +
                "-fx-background-radius: 18;" +
                "-fx-border-color: rgba(127, 227, 240, 0.45);" +
                "-fx-border-radius: 18;" +
                "-fx-padding: 20;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.75), 36, 0.3, 0, 12);"

        private const val ESTILO_TITULO =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #E6F7FA;"

        private const val ESTILO_SUBTITULO =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 11px;" +
                "-fx-text-fill: #7FA8B5;"

        private const val ESTILO_TITULO_PEQUENO =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 9px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #7FA8B5;"

        private const val ESTILO_VALOR =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #E6F7FA;"

        private const val ESTILO_MENSAJE =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 11px;" +
                "-fx-text-fill: #9FE8F2;" +
                "-fx-background-color: rgba(127, 227, 240, 0.08);" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 8 12 8 12;"

        private const val ESTILO_TARJETA =
            "-fx-background-color: rgba(127, 227, 240, 0.07);" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 8 12 8 12;" +
                "-fx-min-width: 130;"

        private const val ESTILO_FILA_MEJORA =
            "-fx-background-color: rgba(127, 227, 240, 0.06);" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 8 10 8 10;"

        private const val ESTILO_MEJORA_NOMBRE =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #E6F7FA;"

        private const val ESTILO_MEJORA_DESCRIPCION =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 10px;" +
                "-fx-text-fill: #8FB6C2;"

        private const val ESTILO_COSTO =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #FFD75A;"

        private const val ESTILO_SCROLL =
            "-fx-background: transparent;" +
                "-fx-background-color: transparent;" +
                "-fx-border-color: transparent;"
    }
}
