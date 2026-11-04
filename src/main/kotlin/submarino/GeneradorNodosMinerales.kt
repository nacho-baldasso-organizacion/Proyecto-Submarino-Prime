package submarino

import kotlin.math.hypot
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * Genera de forma procedural los nodos minerales distribuidos sobre el terreno.
 *
 * Responsabilidad única (SRP): decidir dónde y de qué tipo aparece cada nodo.
 * No conoce la nave, la bodega ni la vista.
 */
class GeneradorNodosMinerales(
    private val terreno: TerrenoSubmarino,
    private val anchoMapa: Double,
    private val altoMapa: Double,
    private val semilla: Long
) {

    fun generar(): List<NodoMineral> {
        val random = Random(semilla + 991L)
        val generados = mutableListOf<NodoMineral>()
        var indice = 0
        var intentos = 0

        generados.add(crearNodo(TipoNodoMineral.COBRE, 2500.0, 300.0, "INICIO-COBRE"))
        generados.add(crearNodo(TipoNodoMineral.COBRE, 2200.0, 650.0, "INICIO-COBRE-2"))
        generados.add(crearNodo(TipoNodoMineral.HIERRO, 2650.0, 1450.0, "INICIO-HIERRO"))

        val formaciones = terreno.todas
        while (generados.size < CANTIDAD_NODOS && intentos < MAX_INTENTOS_GENERACION) {
            intentos++
            val formacion = formaciones[random.nextInt(formaciones.size)]
            val margen = (formacion.xMax - formacion.xMin) * 0.03
            val x = random.nextDouble(formacion.xMin + margen, formacion.xMax - margen)
            val y = formacion.superficieEn(x) ?: continue
            if (y < PROFUNDIDAD_MINIMA_NODO || y > altoMapa - MINIMO_MARGEN_FONDO) {
                continue
            }
            if (estaCercaDeLaEstacion(x, y) || estaDemasiadoCerca(generados, x, y)) {
                continue
            }
            val tipo = tipoSegunProfundidad(y)
            generados.add(crearNodo(tipo, x, y, "NODO-${tipo.name}-${indice++}"))
        }

        return generados
    }

    private fun tipoSegunProfundidad(profundidad: Double): TipoNodoMineral =
        TipoNodoMineral.entries.first { it.correspondeAProfundidad(profundidad) }

    private fun estaDemasiadoCerca(
        nodos: List<NodoMineral>,
        x: Double,
        y: Double
    ): Boolean = nodos.any { hypot(it.x - x, it.y - y) < DISTANCIA_MINIMA_ENTRE_NODOS }

    private fun crearNodo(
        tipo: TipoNodoMineral,
        x: Double,
        y: Double,
        id: String
    ): NodoMineral {
        val mineral = Mineral(
            id = id,
            tipo = tipo.tipoMineral,
            posX = x.roundToInt(),
            posY = y.roundToInt()
        )
        return NodoMineral(
            id = id,
            tipo = tipo,
            mineral = mineral
        )
    }

    private fun estaCercaDeLaEstacion(x: Double, y: Double): Boolean =
        x in (SesionJuego.ESTACION_X - SesionJuego.ESTACION_ANCHO / 2.0 - 150.0)..
            (SesionJuego.ESTACION_X + SesionJuego.ESTACION_ANCHO / 2.0 + 150.0) &&
            y < SesionJuego.PROFUNDIDAD_ZONA_ACOPLE + 120.0

    companion object {
        const val DISTANCIA_MINIMA_ENTRE_NODOS = 400.0
        private const val CANTIDAD_NODOS = 36
        private const val MAX_INTENTOS_GENERACION = 20_000
        private const val PROFUNDIDAD_MINIMA_NODO = 120.0
        private const val MINIMO_MARGEN_FONDO = 25.0
    }
}
