package submarino

import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox

/**
 * HUD fijo de la partida.
 *
 * Muestra profundidad, posición horizontal, oxígeno, casco, bodega, monedas,
 * nodos restantes y estado de acople. Todos los nodos quedan encapsulados.
 */
class HUDJuego {

    private val contenedor: VBox = VBox(8.0)
    private val indicadorProfundidad: IndicadorMetrosHUD =
        FabricaHUD.crearIndicadorMetrosHUD("PROFUNDIDAD", ESTILO_TITULO, ESTILO_VALOR_BASE, "%.0f m")
    private val indicadorPosicionX: IndicadorMetrosHUD =
        FabricaHUD.crearIndicadorMetrosHUD("POSICIÓN X", ESTILO_TITULO, ESTILO_VALOR_BASE, "%.0f m")
    private val indicadorPresion: IndicadorPresionHUD = FabricaHUD.crearIndicadorPresionHUD(
        ESTILO_TITULO,
        ESTILO_VALOR_BASE,
        ANCHO_BARRA,
        ESTILO_BARRA_ESTABLE,
        ESTILO_BARRA_PRECAUCION,
        ESTILO_BARRA_CRITICO
    )
    private val indicadorOxigeno: IndicadorOxigenoJuegoHUD = FabricaHUD.crearIndicadorOxigenoJuegoHUD(
        ESTILO_TITULO,
        ESTILO_VALOR_BASE,
        ANCHO_BARRA,
        ESTILO_BARRA_ESTABLE,
        ESTILO_BARRA_PRECAUCION,
        ESTILO_BARRA_CRITICO
    )
    private val indicadorCasco: IndicadorCascoHUD = FabricaHUD.crearIndicadorCascoHUD(
        ESTILO_TITULO,
        ESTILO_VALOR_BASE,
        ANCHO_BARRA,
        ESTILO_BARRA_ESTABLE,
        ESTILO_BARRA_PRECAUCION,
        ESTILO_BARRA_CRITICO
    )
    private val indicadorMonedas: IndicadorMonedasHUD =
        FabricaHUD.crearIndicadorMonedasHUD(ESTILO_TITULO, ESTILO_VALOR_BASE)
    private val indicadorBodega: IndicadorCargoHUD =
        FabricaHUD.crearIndicadorCargoHUD(ESTILO_TITULO, ESTILO_VALOR_BASE)
    private val indicadorNodos: IndicadorNodosHUD =
        FabricaHUD.crearIndicadorNodosHUD(ESTILO_TITULO, ESTILO_VALOR_BASE)
    private val indicadorEstado: IndicadorAcopleHUD =
        FabricaHUD.crearIndicadorAcopleHUD(ESTILO_TITULO, ESTILO_VALOR_BASE)

    init {
        contenedor.alignment = Pos.TOP_CENTER
        contenedor.style = ESTILO_CONTENEDOR
        contenedor.maxWidth = Region.USE_PREF_SIZE
        contenedor.maxHeight = Region.USE_PREF_SIZE
        contenedor.isMouseTransparent = true

        val filaSuperior = HBox(ESPACIADO).apply {
            alignment = Pos.CENTER_LEFT
            children.addAll(
                indicadorProfundidad.nodo(),
                FabricaHUD.crearSeparadorHUD(ESTILO_SEPARADOR).nodo,
                indicadorPosicionX.nodo(),
                FabricaHUD.crearSeparadorHUD(ESTILO_SEPARADOR).nodo,
                indicadorPresion.nodo(),
                FabricaHUD.crearSeparadorHUD(ESTILO_SEPARADOR).nodo,
                indicadorOxigeno.nodo(),
                FabricaHUD.crearSeparadorHUD(ESTILO_SEPARADOR).nodo,
                indicadorCasco.nodo(),
                FabricaHUD.crearSeparadorHUD(ESTILO_SEPARADOR).nodo,
                indicadorMonedas.nodo()
            )
        }

        val filaInferior = HBox(ESPACIADO).apply {
            alignment = Pos.CENTER_LEFT
            children.addAll(
                indicadorBodega.nodo(),
                FabricaHUD.crearSeparadorHUD(ESTILO_SEPARADOR).nodo,
                indicadorNodos.nodo(),
                FabricaHUD.crearSeparadorHUD(ESTILO_SEPARADOR).nodo,
                indicadorEstado.nodo(),
                FabricaHUD.crearSeparadorHUD(ESTILO_SEPARADOR).nodo,
                FabricaHUD.crearTextoAyudaHUD("WASD / FLECHAS · ESPACIO: MINAR · E: ACOPLAR", ESTILO_AYUDA).nodo
            )
        }

        contenedor.children.addAll(filaSuperior, filaInferior)
    }

    fun obtenerNodo(): Parent = contenedor

    fun actualizar(estado: EstadoJuego) {
        indicadorProfundidad.actualizar(estado.profundidad)
        indicadorPosicionX.actualizar(estado.posicionX)
        indicadorPresion.actualizar(estado.presionActual, estado.presionMaxima)
        indicadorOxigeno.actualizar(estado.oxigeno, estado.capacidadOxigeno)
        indicadorCasco.actualizar(estado.integridadCasco)
        indicadorBodega.actualizar(estado.pesoBodega, estado.capacidadBodega, estado.cantidadItems)
        indicadorMonedas.actualizar(estado.monedas)
        indicadorNodos.actualizar(estado.nodosDisponibles)
        indicadorEstado.actualizar(estado.acoplado)
    }

    companion object {
        private const val ESPACIADO = 14.0
        private const val ANCHO_BARRA = 92.0

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
