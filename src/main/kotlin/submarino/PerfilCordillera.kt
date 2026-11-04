package submarino

import kotlin.math.min

/**
 * Perfil interpolado de una cordillera dibujada sobre un canvas.
 *
 * Responsabilidad única (SRP): responder la altura de la silueta para una
 * coordenada X dada, sin conocer colores ni efectos visuales.
 */
class PerfilCordillera(
    private val xs: DoubleArray,
    private val ys: DoubleArray
) {

    fun alturaEn(x: Double): Double {
        if (x <= xs.first()) {
            return ys.first()
        }
        if (x >= xs.last()) {
            return ys.last()
        }
        var bajo = 0
        var alto = xs.size - 1
        while (alto - bajo > 1) {
            val medio = (bajo + alto) / 2
            if (xs[medio] <= x) {
                bajo = medio
            } else {
                alto = medio
            }
        }
        val ancho = xs[alto] - xs[bajo]
        if (ancho <= 0.0) {
            return min(ys[bajo], ys[alto])
        }
        val avance = (x - xs[bajo]) / ancho
        return ys[bajo] + (ys[alto] - ys[bajo]) * avance
    }
}
