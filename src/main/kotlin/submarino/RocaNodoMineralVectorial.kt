package submarino

import javafx.scene.Node
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Circle
import javafx.scene.shape.Ellipse
import javafx.scene.shape.Polygon

internal object RocaNodoMineralVectorial {

    fun crear(paleta: PaletaNodoMineralVectorial): List<Node> {
        return listOf(
            crearHalo(paleta),
            crearSombra(paleta),
            crearRoca(paleta),
            crearRocaIzquierda(paleta),
            crearRocaDerecha(paleta),
            crearVeta(paleta),
            crearSedimento(paleta)
        )
    }

    private fun crearHalo(paleta: PaletaNodoMineralVectorial): Circle {
        return Circle(0.0, 0.0, 26.0).apply {
            fill = paleta.brillo.deriveColor(0.0, 1.0, 1.0, 0.18)
            effect = DropShadow(24.0, paleta.brillo.deriveColor(0.0, 1.0, 1.0, 0.55))
        }
    }

    private fun crearSombra(paleta: PaletaNodoMineralVectorial): Ellipse {
        return Ellipse(0.0, 21.0, 31.0, 7.0).apply {
            fill = Color.web("#000000", 0.32)
        }
    }

    private fun crearRoca(paleta: PaletaNodoMineralVectorial): Polygon {
        return Polygon(
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
    }

    private fun crearRocaIzquierda(paleta: PaletaNodoMineralVectorial): Polygon {
        return Polygon(
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
    }

    private fun crearRocaDerecha(paleta: PaletaNodoMineralVectorial): Polygon {
        return Polygon(
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
    }

    private fun crearVeta(paleta: PaletaNodoMineralVectorial): Polygon {
        return Polygon(
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
    }

    private fun crearSedimento(paleta: PaletaNodoMineralVectorial): Ellipse {
        return Ellipse(0.0, 20.0, 18.0, 3.5).apply {
            fill = Color.web("#3D3724", 0.45)
        }
    }
}