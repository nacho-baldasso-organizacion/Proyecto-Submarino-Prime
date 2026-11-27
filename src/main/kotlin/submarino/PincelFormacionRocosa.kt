package submarino

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import kotlin.random.Random

/**
 * Dibuja una única formación rocosa combinando relleno, detalles y exterior.
 *
 * Responsabilidad única (SRP): orquestar el trazo de una formación sobre el canvas.
 */
class PincelFormacionRocosa(
    private val gc: GraphicsContext,
    private val random: Random,
    private val escala: Double,
    private val paleta: PaletaTerrenoRocoso = PaletaTerrenoRocoso()
) {

    private val xs = mutableListOf<Double>()
    private val ys = mutableListOf<Double>()

    fun dibujar(formacion: FormacionRocosa, esSuelo: Boolean) {
        procesarCrestas(formacion)
        val (colorBase, colorClaro, colorOscuro) = configurarColores(formacion, esSuelo)
        val yCresta = formacion.crestas.minOf { it.y } * escala
        val yBase = formacion.baseY * escala
        dibujarBase(colorBase, yCresta, yBase, esSuelo)
        aplicarClipYDetalles(formacion, esSuelo, colorBase, colorClaro, colorOscuro, yCresta, yBase)
        dibujarExterior(formacion, esSuelo, colorClaro, colorOscuro, yCresta, yBase)
    }

    private fun procesarCrestas(formacion: FormacionRocosa) {
        val crestas = formacion.crestas
        if (crestas.size < 2) {
            return
        }

        xs.clear()
        ys.clear()
        xs.add(crestas.first().x * escala)
        ys.add(formacion.baseY * escala)
        crestas.forEachIndexed { indice, punto ->
            xs.add(punto.x * escala)
            ys.add(punto.y * escala)
        }
        xs.add(crestas.last().x * escala)
        ys.add(formacion.baseY * escala)
    }

    private fun configurarColores(formacion: FormacionRocosa, esSuelo: Boolean): Triple<Color, Color, Color> {
        val colorBase = paleta.colorBase(formacion.profundidadMinima, esSuelo)
        val colorClaro = colorBase.interpolate(Color.web("#9FE8DA"), if (esSuelo) 0.10 else 0.24)
        val colorOscuro = colorBase.interpolate(Color.web("#010A0E"), if (esSuelo) 0.55 else 0.42)
        return Triple(colorBase, colorClaro, colorOscuro)
    }

    private fun dibujarBase(colorBase: Color, yCresta: Double, yBase: Double, esSuelo: Boolean) {
        val colorClaro = colorBase.interpolate(Color.web("#9FE8DA"), if (esSuelo) 0.10 else 0.24)
        val colorOscuro = colorBase.interpolate(Color.web("#010A0E"), if (esSuelo) 0.55 else 0.42)
        gc.fill = LinearGradient(
            0.0, yCresta, 0.0, yBase,
            false,
            CycleMethod.NO_CYCLE,
            Stop(0.0, colorClaro),
            Stop(0.38, colorBase),
            Stop(1.0, colorOscuro)
        )
        gc.fillPolygon(xs.toDoubleArray(), ys.toDoubleArray(), xs.size)

        gc.save()
        gc.beginPath()
        gc.moveTo(xs[0], ys[0])
        for (indice in 1 until xs.size) {
            gc.lineTo(xs[indice], ys[indice])
        }
        gc.closePath()
        gc.clip()
    }

    private fun aplicarClipYDetalles(formacion: FormacionRocosa, esSuelo: Boolean, colorBase: Color, colorClaro: Color, colorOscuro: Color, yCresta: Double, yBase: Double) {
        val detalles = DetallesRoca(gc, random, escala)
        detalles.dibujarEstratos(formacion, yCresta, yBase)
        detalles.dibujarRocasInteriores(formacion)
        detalles.dibujarGrietas(formacion)
        detalles.dibujarSedimento(formacion)
        if (esSuelo) {
            detalles.dibujarDetallesSuelo(formacion)
        }
        gc.restore()
    }

    private fun dibujarExterior(formacion: FormacionRocosa, esSuelo: Boolean, colorClaro: Color, colorOscuro: Color, yCresta: Double, yBase: Double) {
        val exterior = PincelRocaExterior(gc, random, escala)
        if (!esSuelo) {
            exterior.dibujarCrestaSinuosa(formacion, colorClaro)
            exterior.dibujarSalientes(formacion, colorClaro, colorOscuro)
        } else {
            exterior.dibujarSalientes(formacion, colorClaro, colorOscuro)
            exterior.dibujarArrecifeDeFondo(formacion)
        }
        if (formacion.profundidadMinima < PROFUNDIDAD_ARRECIFE) {
            exterior.dibujarArrecifeDeCresta(formacion)
        }
    }

    companion object {
        private const val PROFUNDIDAD_ARRECIFE = 1650.0
    }
}