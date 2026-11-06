package submarino

import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.layout.HBox
import javafx.scene.layout.Region

/**
 * HUD fijo de la interfaz del submarino.
 *
 * Muestra profundidad, coordenada horizontal, oxígeno y estado operativo.
 * Todos los nodos y etiquetas quedan encapsulados como campos privados.
 */
class HUDSubmarino {

    private val contenedor: HBox = HBox(ESPACIADO)
    private val indicadorProfundidad: IndicadorMetrosHUD =
        FabricaHUD.crearIndicadorMetrosHUD("PROFUNDIDAD", ESTILO_TITULO, ESTILO_VALOR_BASE, "%.1f m")
    private val indicadorCoordenadaX: IndicadorMetrosHUD =
        FabricaHUD.crearIndicadorMetrosHUD("COORDENADA X", ESTILO_TITULO, ESTILO_VALOR_BASE, "%.1f m")
    private val indicadorOxigeno: IndicadorOxigenoNavegacionHUD = FabricaHUD.crearIndicadorOxigenoNavegacionHUD(
        ESTILO_TITULO,
        ESTILO_VALOR_BASE,
        ANCHO_BARRA,
        ESTILO_BARRA_ESTABLE,
        ESTILO_BARRA_PRECAUCION,
        ESTILO_BARRA_CRITICO,
        ESTILO_BARRA_AGOTADO
    )
    private val indicadorEstado: IndicadorEstadoOxigenoHUD =
        FabricaHUD.crearIndicadorEstadoOxigenoHUD(ESTILO_TITULO, ESTILO_VALOR_BASE)

    init {
        contenedor.alignment = Pos.CENTER_LEFT
        contenedor.style = ESTILO_CONTENEDOR
        contenedor.maxWidth = Region.USE_PREF_SIZE
        contenedor.maxHeight = Region.USE_PREF_SIZE
        contenedor.isMouseTransparent = true

        contenedor.children.addAll(
            indicadorProfundidad.nodo(),
            FabricaHUD.crearSeparadorHUD(ESTILO_SEPARADOR).nodo,
            indicadorCoordenadaX.nodo(),
            FabricaHUD.crearSeparadorHUD(ESTILO_SEPARADOR).nodo,
            indicadorOxigeno.nodo(),
            FabricaHUD.crearSeparadorHUD(ESTILO_SEPARADOR).nodo,
            indicadorEstado.nodo(),
            FabricaHUD.crearTextoAyudaHUD("WASD / FLECHAS", ESTILO_AYUDA).nodo
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
        indicadorProfundidad.actualizar(estado.profundidad)
        indicadorCoordenadaX.actualizar(estado.coordenadaX)
        indicadorOxigeno.actualizar(estado.oxigeno, estado.estadoOxigeno)
        indicadorEstado.actualizar(estado.estadoOxigeno)
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
