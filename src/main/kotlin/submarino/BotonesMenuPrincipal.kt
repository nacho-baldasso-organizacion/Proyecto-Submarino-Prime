package submarino

import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.layout.HBox

/**
 * Fabrica los botones del menú principal y compone la fila JUGAR/REGLAS.
 */
class BotonesMenuPrincipal {

    fun crearPanel(alJugar: () -> Unit, alMostrarReglas: () -> Unit): HBox {
        val botonJugar = crearBoton("JUGAR", true).apply {
            setOnAction { alJugar() }
        }
        val botonReglas = crearBoton("REGLAS", false).apply {
            setOnAction { alMostrarReglas() }
        }

        return HBox(16.0).apply {
            alignment = Pos.CENTER
            children.addAll(botonJugar, botonReglas)
        }
    }

    fun crearBoton(texto: String, principal: Boolean): Button = Button(texto).apply {
        style = if (principal) ESTILO_BOTON_PRINCIPAL else ESTILO_BOTON_SECUNDARIO
        setOnMouseEntered {
            style = if (principal) ESTILO_BOTON_PRINCIPAL_HOVER else ESTILO_BOTON_SECUNDARIO_HOVER
        }
        setOnMouseExited {
            style = if (principal) ESTILO_BOTON_PRINCIPAL else ESTILO_BOTON_SECUNDARIO
        }
    }

    companion object {
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
    }
}
