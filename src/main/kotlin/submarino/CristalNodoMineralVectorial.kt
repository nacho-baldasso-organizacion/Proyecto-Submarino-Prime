package submarino

import javafx.scene.Node
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Circle
import javafx.scene.shape.Polygon

internal object CristalNodoMineralVectorial {

    fun crear(paleta: PaletaNodoMineralVectorial): List<Node> {
        return listOf(
            crearCristalSecundario(paleta),
            crearCristalTerciario(paleta),
            crearCristal(paleta),
            crearFaceta(paleta),
            crearBrillo()
        )
    }

    private fun crearCristalSecundario(paleta: PaletaNodoMineralVectorial): Polygon {
        return Polygon(
            -20.0, 11.0,
            -17.0, -2.0,
            -11.0, -8.0,
            -9.0, 10.0
        ).apply {
            fill = paleta.oscuro.deriveColor(0.0, 0.9, 1.0, 0.92)
            stroke = paleta.oscuro
            strokeWidth = 1.0
        }
    }

    private fun crearCristalTerciario(paleta: PaletaNodoMineralVectorial): Polygon {
        return Polygon(
            15.0, 10.0,
            18.0, -1.0,
            23.0, -6.0,
            23.0, 12.0
        ).apply {
            fill = paleta.base.deriveColor(0.0, 0.9, 0.95, 0.92)
            stroke = paleta.oscuro
            strokeWidth = 1.0
        }
    }

    private fun crearCristal(paleta: PaletaNodoMineralVectorial): Polygon {
        return Polygon(
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
    }

    private fun crearFaceta(paleta: PaletaNodoMineralVectorial): Polygon {
        return Polygon(
            -8.0, -16.0,
            2.0, -27.0,
            0.0, -4.0
        ).apply {
            fill = paleta.claro.deriveColor(0.0, 1.0, 1.0, 0.75)
        }
    }

    private fun crearBrillo(): Circle {
        return Circle(4.0, -10.0, 3.0).apply {
            fill = Color.web("#FFFFFF", 0.85)
        }
    }
}