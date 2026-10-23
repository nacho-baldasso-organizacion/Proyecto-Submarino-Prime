package submarino

import java.util.UUID
import kotlin.random.Random

/**
 * Generador de minerales ubicado exclusivamente en las paredes o bordes del mapa submarino.
 * Cumple con SRP (Single Responsibility Principle) y DIP (Dependency Inversion Principle).
 */
class GeneradorParedes(
    private val random: Random = Random.Default
) : GeneradorMinerales {

    override fun generarEnParedes(
        anchoMapa: Int,
        altoMapa: Int,
        profundidadActual: Double,
        cantidad: Int
    ): List<Recolectable> {
        require(anchoMapa > 0) { "El ancho del mapa debe ser mayor a 0: $anchoMapa" }
        require(altoMapa > 0) { "El alto del mapa debe ser mayor a 0: $altoMapa" }

        if (cantidad <= 0) {
            return emptyList()
        }

        val coordenadasParedes = calcularParedes(anchoMapa, altoMapa)
        val tipoMineral = TipoMineral.obtenerPorProfundidad(profundidadActual)
        val seleccionadas = coordenadasParedes.shuffled(random).take(cantidad)

        return seleccionadas.mapIndexed { index, (x, y) ->
            crearMineralEnPared(tipoMineral, x, y, index)
        }
    }

    /**
     * Calcula geométricamente todas las celdas que pertenecen a los bordes exteriores del mapa.
     */
    fun calcularParedes(anchoMapa: Int, altoMapa: Int): Set<Pair<Int, Int>> {
        val coordenadas = mutableSetOf<Pair<Int, Int>>()
        for (x in 0 until anchoMapa) {
            coordenadas.add(Pair(x, 0))
            coordenadas.add(Pair(x, altoMapa - 1))
        }
        for (y in 0 until altoMapa) {
            coordenadas.add(Pair(0, y))
            coordenadas.add(Pair(anchoMapa - 1, y))
        }
        return coordenadas
    }

    /**
     * Fabrica un ejemplar de mineral ubicado en el perímetro con un identificador único.
     */
    fun crearMineralEnPared(tipo: TipoMineral, x: Int, y: Int, index: Int): Recolectable {
        val idUnico = "MIN-${tipo.name.take(4)}-${UUID.randomUUID().toString().take(6).uppercase()}-$index"
        return Mineral(
            id = idUnico,
            tipo = tipo,
            posX = x,
            posY = y,
            recolectado = false
        )
    }
}
