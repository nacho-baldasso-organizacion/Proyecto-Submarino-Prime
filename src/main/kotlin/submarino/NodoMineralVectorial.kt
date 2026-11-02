package submarino

import javafx.scene.Group
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Circle
import javafx.scene.shape.Ellipse
import javafx.scene.shape.Polygon

/**
 * Representación vectorial de un nodo mineral.
 *
 * El color depende del tipo de recurso: cobrizo, gris metálico o dorado.
 */
class NodoMineralVectorial(
    private val modelo: NodoMineral
) {

    val nodo: Group = crearNodo()

    init {
        actualizar()
    }

    /**
     * Oculta el nodo cuando ya fue recolectado.
     */
    fun actualizar() {
        nodo.isVisible = !modelo.recolectado
    }

    private fun crearNodo(): Group {
        val paleta = paletaDe(modelo.tipo)

        val halo = Circle(0.0, 0.0, 26.0).apply {
            fill = paleta.brillo.deriveColor(0.0, 1.0, 1.0, 0.18)
            effect = DropShadow(24.0, paleta.brillo.deriveColor(0.0, 1.0, 1.0, 0.55))
        }

        val sombra = Ellipse(0.0, 21.0, 31.0, 7.0).apply {
            fill = Color.web("#000000", 0.32)
        }

        val roca = Polygon(
            -24.0, 14.0,
            -14.0, 4.0,
            0.0, 8.0,
            16.0, 2.0,
            25.0, 14.0,
            12.0, 22.0,
            -12.0, 23.0
        ).apply {
            fill = LinearGradient(
                0.0, 0.0, 0.0, 1.0,
                true,
                CycleMethod.NO_CYCLE,
                Stop(0.0, Color.web("#2A474F")),
                Stop(0.55, Color.web("#162B32")),
                Stop(1.0, Color.web("#0A1A20"))
            )
            stroke = Color.web("#355A66")
            strokeWidth = 1.5
        }

        val rocaIzquierda = Polygon(
            -32.0, 19.0,
            -28.0, 8.0,
            -19.0, 12.0,
            -20.0, 21.0,
            -27.0, 25.0
        ).apply {
            fill = Color.web("#12262D")
            stroke = Color.web("#2B4C56")
            strokeWidth = 1.0
        }

        val rocaDerecha = Polygon(
            31.0, 18.0,
            24.0, 6.0,
            17.0, 11.0,
            19.0, 22.0,
            27.0, 24.0
        ).apply {
            fill = Color.web("#142A31")
            stroke = Color.web("#2B4C56")
            strokeWidth = 1.0
        }

        val veta = Polygon(
            -13.0, 15.0,
            -8.0, 7.0,
            -1.0, 12.0,
            5.0, 8.0,
            11.0, 15.0,
            3.0, 19.0,
            -5.0, 18.0
        ).apply {
            fill = paleta.base.deriveColor(0.0, 1.0, 1.0, 0.42)
        }

        val sedimento = Ellipse(0.0, 20.0, 18.0, 3.5).apply {
            fill = Color.web("#3D3724", 0.45)
        }

        val cristalSecundario = Polygon(
            -20.0, 11.0,
            -17.0, -2.0,
            -11.0, -8.0,
            -9.0, 10.0
        ).apply {
            fill = paleta.oscuro.deriveColor(0.0, 0.9, 1.0, 0.92)
            stroke = paleta.oscuro
            strokeWidth = 1.0
        }

        val cristalTerciario = Polygon(
            15.0, 10.0,
            18.0, -1.0,
            23.0, -6.0,
            23.0, 12.0
        ).apply {
            fill = paleta.base.deriveColor(0.0, 0.9, 0.95, 0.92)
            stroke = paleta.oscuro
            strokeWidth = 1.0
        }

        val cristal = Polygon(
            -12.0, 10.0,
            -8.0, -16.0,
            2.0, -27.0,
            13.0, -8.0,
            9.0, 11.0
        ).apply {
            fill = LinearGradient(
                0.0, 0.0, 0.0, 1.0,
                true,
                CycleMethod.NO_CYCLE,
                Stop(0.0, paleta.claro),
                Stop(0.45, paleta.base),
                Stop(1.0, paleta.oscuro)
            )
            stroke = paleta.oscuro
            strokeWidth = 2.0
            effect = DropShadow(12.0, paleta.brillo.deriveColor(0.0, 1.0, 1.0, 0.7))
        }

        val faceta = Polygon(
            -8.0, -16.0,
            2.0, -27.0,
            0.0, -4.0
        ).apply {
            fill = paleta.claro.deriveColor(0.0, 1.0, 1.0, 0.75)
        }

        val brillo = Circle(4.0, -10.0, 3.0).apply {
            fill = Color.web("#FFFFFF", 0.85)
        }

        return Group().apply {
            isPickOnBounds = false
            children.addAll(
                halo,
                sombra,
                rocaIzquierda,
                rocaDerecha,
                roca,
                veta,
                sedimento,
                cristalSecundario,
                cristalTerciario,
                cristal,
                faceta,
                brillo
            )
        }
    }

    private fun paletaDe(tipo: TipoNodoMineral): PaletaNodo = when (tipo) {
        TipoNodoMineral.COBRE -> PaletaNodo(
            base = Color.web("#B5652A"),
            claro = Color.web("#F0A35E"),
            oscuro = Color.web("#6E3512"),
            brillo = Color.web("#FF9A4D")
        )
        TipoNodoMineral.HIERRO -> PaletaNodo(
            base = Color.web("#7E8A94"),
            claro = Color.web("#D5DEE5"),
            oscuro = Color.web("#414A52"),
            brillo = Color.web("#C7D6E0")
        )
        TipoNodoMineral.ORO -> PaletaNodo(
            base = Color.web("#D9A61A"),
            claro = Color.web("#FFF2A8"),
            oscuro = Color.web("#8A6208"),
            brillo = Color.web("#FFD34D")
        )
    }

    private data class PaletaNodo(
        val base: Color,
        val claro: Color,
        val oscuro: Color,
        val brillo: Color
    )
}
