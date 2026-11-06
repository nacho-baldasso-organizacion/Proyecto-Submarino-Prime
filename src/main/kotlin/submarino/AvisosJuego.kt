package submarino

import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.VBox

/**
 * Regla configurable de aviso: se aplica cuando el estado cumple la condición.
 */
internal class ReglaAviso(
    val condicion: (EstadoJuego) -> Boolean,
    val texto: String
)

/**
 * Panel inferior de avisos contextuales y mensajes de la partida.
 *
 * Responsabilidad única (SRP): decidir y pintar el aviso mostrado al jugador.
 */
class AvisosJuego {

    val notificacion: Label = Label("").apply {
        style = ESTILO_NOTIFICACION
        isMouseTransparent = true
        isVisible = false
        isManaged = false
    }

    val mensaje: Label = Label("").apply {
        style = ESTILO_MENSAJE
        isMouseTransparent = true
        isWrapText = true
        maxWidth = 760.0
        alignment = Pos.CENTER
    }

    val panel: VBox = VBox(6.0).apply {
        alignment = Pos.CENTER
        isMouseTransparent = true
        children.addAll(notificacion, mensaje)
    }

    private val reglas: List<ReglaAviso> = listOf(
        FabricaHUD.crearReglaAviso({ it.presionActual > it.presionMaxima }, "¡PRESIÓN CRÍTICA! Asciende o el casco colapsará"),
        FabricaHUD.crearReglaAviso({ it.oxigeno <= it.capacidadOxigeno * UMBRAL_OXIGENO_BAJO }, "¡OXÍGENO BAJO! Regresa a la estación para recargar"),
        FabricaHUD.crearReglaAviso({ it.acoplado }, "Acoplado · pulsa DESACOPLAR en la tienda para continuar"),
        FabricaHUD.crearReglaAviso({ it.enZonaAcople }, "Presiona E para acoplar")
    )

    fun actualizar(estado: EstadoJuego) {
        val presionCritica = estado.presionActual > estado.presionMaxima
        val oxigenoBajo = estado.oxigeno <= estado.capacidadOxigeno * UMBRAL_OXIGENO_BAJO
        val aviso = if (estado.destruido) {
            ""
        } else {
            reglas.firstOrNull { it.condicion(estado) }?.texto ?: ""
        }
        notificacion.text = aviso
        notificacion.style = if (presionCritica || oxigenoBajo) {
            ESTILO_NOTIFICACION_CRITICA
        } else {
            ESTILO_NOTIFICACION
        }
        notificacion.isVisible = aviso.isNotEmpty()
        notificacion.isManaged = aviso.isNotEmpty()

        if (mensaje.text != estado.mensaje) {
            mensaje.text = estado.mensaje
        }
    }

    companion object {
        private const val UMBRAL_OXIGENO_BAJO = 0.18

        private const val ESTILO_NOTIFICACION =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #031926;" +
                "-fx-background-color: #FFD75A;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 8 18 8 18;" +
                "-fx-effect: dropshadow(gaussian, rgba(255, 215, 90, 0.55), 18, 0.3, 0, 4);"

        private const val ESTILO_MENSAJE =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 11px;" +
                "-fx-text-fill: #B7D8E1;" +
                "-fx-background-color: rgba(2, 20, 28, 0.72);" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 6 14 6 14;"

        private const val ESTILO_NOTIFICACION_CRITICA =
            "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #FFF5F0;" +
                "-fx-background-color: #D6290B;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 8 18 8 18;" +
                "-fx-effect: dropshadow(gaussian, rgba(255, 74, 20, 0.85), 24, 0.4, 0, 4);"
    }
}
