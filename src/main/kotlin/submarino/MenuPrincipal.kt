package submarino

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.effect.DropShadow
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Rectangle

/**
 * Pantalla de inicio del juego.
 *
 * Compone un fondo abisal procedural, el título, los botones JUGAR/REGLAS y un
 * panel modal de reglas. No utiliza imágenes externas.
 */
class MenuPrincipal(
    private val anchoEscena: Double,
    private val altoEscena: Double,
    private val alJugar: () -> Unit
) {

    private val raiz: StackPane = StackPane()
    private val fondo: FondoAbisal = FondoAbisal(anchoEscena, altoEscena, SEMILLA_FONDO)
    private val peces: PecesMarinos = PecesMarinos(anchoEscena, altoEscena, SEMILLA_PECES)
    private val submarino: SubmarinoVectorial = SubmarinoVectorial().apply {
        nodo.scaleX = 1.55
        nodo.scaleY = 1.55
    }
    private val overlayReglas: StackPane = crearOverlayReglas()

    init {
        val velo = Rectangle(anchoEscena, altoEscena).apply {
            fill = LinearGradient(
                0.0, 0.0, 0.0, 1.0,
                true,
                CycleMethod.NO_CYCLE,
                Stop(0.0, Color.web("#02121A", 0.10)),
                Stop(0.55, Color.web("#02121A", 0.22)),
                Stop(1.0, Color.web("#01070B", 0.72))
            )
            isMouseTransparent = true
        }

        val contenido = crearContenido()

        raiz.children.addAll(
            fondo.capaLejana,
            fondo.capaMedia,
            peces.nodo,
            velo,
            contenido,
            overlayReglas
        )
        StackPane.setAlignment(fondo.capaLejana, Pos.TOP_LEFT)
        StackPane.setAlignment(fondo.capaMedia, Pos.TOP_LEFT)
        StackPane.setAlignment(peces.nodo, Pos.TOP_LEFT)
        StackPane.setAlignment(velo, Pos.TOP_LEFT)
        StackPane.setAlignment(contenido, Pos.CENTER)
        StackPane.setAlignment(overlayReglas, Pos.CENTER)
        peces.iniciar()
    }

    fun obtenerRaiz(): Parent = raiz

    /**
     * Libera las animaciones del menú al cambiar de pantalla.
     */
    fun detenerAnimaciones() {
        submarino.detenerAnimaciones()
        peces.detener()
    }

    private fun crearContenido(): VBox {
        val titulo = Label("SUBMARINO PRIME").apply {
            style = ESTILO_TITULO
            textFill = LinearGradient(
                0.0, 0.0, 0.0, 1.0,
                true,
                CycleMethod.NO_CYCLE,
                Stop(0.0, Color.web("#FFF7CC")),
                Stop(0.45, Color.web("#FFD75A")),
                Stop(1.0, Color.web("#D99A25"))
            )
            effect = DropShadow(24.0, Color.web("#7FE3F0", 0.55))
        }
        val subtitulo = Label("EXPLORACIÓN ABISAL").apply {
            style = ESTILO_SUBTITULO
        }
        val descripcion = Label("Mapa de 5000 m × 3000 m · Minería · Comercio · Upgrades").apply {
            style = ESTILO_DESCRIPCION
        }

        val botonJugar = crearBoton("JUGAR", true).apply {
            setOnAction { alJugar() }
        }
        val botonReglas = crearBoton("REGLAS", false).apply {
            setOnAction { mostrarReglas(true) }
        }
        val botones = HBox(16.0).apply {
            alignment = Pos.CENTER
            children.addAll(botonJugar, botonReglas)
        }

        val indicaciones = Label("WASD / Flechas · ESPACIO: minar · E: acoplar").apply {
            style = ESTILO_INDICACIONES
        }

        val bloqueTexto = VBox(4.0).apply {
            alignment = Pos.CENTER
            children.addAll(titulo, subtitulo, descripcion)
        }

        return VBox(18.0).apply {
            alignment = Pos.CENTER
            translateY = -48.0
            children.addAll(
                bloqueTexto,
                botones,
                indicaciones,
                submarino.nodo
            )
            VBox.setMargin(submarino.nodo, Insets(28.0, 0.0, 0.0, 0.0))
        }
    }

    private fun crearOverlayReglas(): StackPane {
        val fondoModal = Rectangle().apply {
            fill = Color.rgb(0, 0, 0, 0.72)
            widthProperty().bind(raiz.widthProperty())
            heightProperty().bind(raiz.heightProperty())
        }

        val titulo = Label("REGLAS DE EXPLORACIÓN").apply {
            style = ESTILO_TITULO_MODAL
        }
        val reglas = VBox(10.0).apply {
            children.addAll(
                crearRegla("WASD / FLECHAS", "Mover el submarino en los ejes X e Y."),
                crearRegla("ESPACIO", "Minar minerales cuando estés cerca de un nodo."),
                crearRegla("TECLA E", "Acoplarte en la Estación Base (superficie) para abrir la Tienda."),
                crearRegla("OBJETIVO", "Recolecta Cobre, Hierro y Oro; vende en superficie y mejora tu nave.")
            )
        }
        val cerrar = crearBoton("VOLVER AL MENÚ", true).apply {
            setOnAction { mostrarReglas(false) }
        }

        val panel = VBox(16.0).apply {
            alignment = Pos.CENTER_LEFT
            style = ESTILO_PANEL_MODAL
            maxWidth = 620.0
            children.addAll(titulo, reglas, cerrar)
        }

        return StackPane().apply {
            children.addAll(fondoModal, panel)
            StackPane.setAlignment(panel, Pos.CENTER)
            isVisible = false
            isMouseTransparent = true
        }
    }

    private fun crearRegla(tecla: String, texto: String): HBox {
        val etiquetaTecla = Label(tecla).apply {
            style = ESTILO_TECLA
            minWidth = 130.0
        }
        val etiquetaTexto = Label(texto).apply {
            style = ESTILO_REGLA
            isWrapText = true
            maxWidth = 420.0
        }
        return HBox(12.0).apply {
            alignment = Pos.CENTER_LEFT
            children.addAll(etiquetaTecla, etiquetaTexto)
        }
    }

    private fun crearBoton(texto: String, principal: Boolean): Button = Button(texto).apply {
        style = if (principal) ESTILO_BOTON_PRINCIPAL else ESTILO_BOTON_SECUNDARIO
        setOnMouseEntered {
            style = if (principal) ESTILO_BOTON_PRINCIPAL_HOVER else ESTILO_BOTON_SECUNDARIO_HOVER
        }
        setOnMouseExited {
            style = if (principal) ESTILO_BOTON_PRINCIPAL else ESTILO_BOTON_SECUNDARIO
        }
    }

    private fun mostrarReglas(mostrar: Boolean) {
        overlayReglas.isVisible = mostrar
        overlayReglas.isMouseTransparent = !mostrar
    }

    companion object {
        private const val SEMILLA_FONDO = 11L
        private const val SEMILLA_PECES = 91L

        private const val ESTILO_TITULO =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 58px;" +
                "-fx-font-weight: bold;"

        private const val ESTILO_SUBTITULO =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #C8FAFF;" +
                "-fx-effect: dropshadow(gaussian, rgba(127, 227, 240, 0.55), 14, 0.25, 0, 0);"

        private const val ESTILO_DESCRIPCION =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #8FB6C2;"

        private const val ESTILO_INDICACIONES =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #5E8896;"

        private const val ESTILO_BOTON_PRINCIPAL =
            "-fx-background-color: #FFD75A;" +
                "-fx-text-fill: #031926;" +
                "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10;" +
                "-fx-border-radius: 10;" +
                "-fx-padding: 12 44 12 44;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(255, 215, 90, 0.35), 18, 0.3, 0, 4);"

        private const val ESTILO_BOTON_PRINCIPAL_HOVER =
            "-fx-background-color: #FFF0A8;" +
                "-fx-text-fill: #031926;" +
                "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10;" +
                "-fx-border-radius: 10;" +
                "-fx-padding: 12 44 12 44;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(255, 215, 90, 0.65), 24, 0.35, 0, 4);"

        private const val ESTILO_BOTON_SECUNDARIO =
            "-fx-background-color: rgba(4, 38, 52, 0.88);" +
                "-fx-text-fill: #C8FAFF;" +
                "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: rgba(127, 227, 240, 0.75);" +
                "-fx-border-radius: 10;" +
                "-fx-padding: 12 44 12 44;" +
                "-fx-cursor: hand;"

        private const val ESTILO_BOTON_SECUNDARIO_HOVER =
            "-fx-background-color: rgba(12, 74, 94, 0.95);" +
                "-fx-text-fill: #F2FFFF;" +
                "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #7FE3F0;" +
                "-fx-border-radius: 10;" +
                "-fx-padding: 12 44 12 44;" +
                "-fx-cursor: hand;"

        private const val ESTILO_PANEL_MODAL =
            "-fx-background-color: rgba(3, 25, 38, 0.97);" +
                "-fx-background-radius: 18;" +
                "-fx-border-color: rgba(127, 227, 240, 0.45);" +
                "-fx-border-radius: 18;" +
                "-fx-padding: 24;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.75), 36, 0.3, 0, 12);"

        private const val ESTILO_TITULO_MODAL =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #E6F7FA;"

        private const val ESTILO_TECLA =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #FFD75A;" +
                "-fx-background-color: rgba(255, 215, 90, 0.10);" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 5 8 5 8;"

        private const val ESTILO_REGLA =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #B7D8E1;"
    }
}
