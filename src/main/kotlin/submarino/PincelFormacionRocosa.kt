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

    fun dibujar(formacion: FormacionRocosa, esSuelo: Boolean) {
        val crestas = formacion.crestas
        if (crestas.size < 2) {
            return
        }

        val xs = DoubleArray(crestas.size + 2)
        val ys = DoubleArray(crestas.size + 2)
        xs[0] = crestas.first().x * escala
        ys[0] = formacion.baseY * escala
        crestas.forEachIndexed { indice, punto ->
            xs[indice + 1] = punto.x * escala
            ys[indice + 1] = punto.y * escala
        }
        xs[xs.lastIndex] = crestas.last().x * escala
        ys[ys.lastIndex] = formacion.baseY * escala

        val colorBase = paleta.colorBase(formacion.profundidadMinima, esSuelo)
        val colorClaro = colorBase.interpolate(Color.web("#9FE8DA"), if (esSuelo) 0.10 else 0.24)
        val colorOscuro = colorBase.interpolate(Color.web("#010A0E"), if (esSuelo) 0.55 else 0.42)
        val yCresta = crestas.minOf { it.y } * escala
        val yBase = formacion.baseY * escala

        gc.fill = LinearGradient(
            0.0, yCresta, 0.0, yBase,
            false,
            CycleMethod.NO_CYCLE,
            Stop(0.0, colorClaro),
            Stop(0.38, colorBase),
            Stop(1.0, colorOscuro)
        )
        gc.fillPolygon(xs, ys, xs.size)

        gc.save()
        gc.beginPath()
        gc.moveTo(xs[0], ys[0])
        for (indice in 1 until xs.size) {
            gc.lineTo(xs[indice], ys[indice])
        }
        gc.closePath()
        gc.clip()

        val detalles = DetallesRoca(gc, random, escala)
        detalles.dibujarEstratos(formacion, yCresta, yBase)
        detalles.dibujarRocasInteriores(formacion)
        detalles.dibujarGrietas(formacion)
        detalles.dibujarSedimento(formacion)
        if (esSuelo) {
            detalles.dibujarDetallesSuelo(formacion)
        }
        gc.restore()

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
