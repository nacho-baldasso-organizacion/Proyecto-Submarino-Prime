package submarino

import java.util.UUID
import kotlin.random.Random

/**
 * Generador de minerales ubicado exclusivamente en las paredes o bordes del mapa submarino.
 *
 * Cumple con:
 * - Single Responsibility Principle (SRP): su única responsabilidad es distribuir aleatoriamente
 *   minerales en los perímetros del mapa según la profundidad batimétrica.
 * - Dependency Inversion Principle (DIP): implementa la interfaz [GeneradorMinerales].
 *
 * Límites perimetrales del mapa:
 * - Pared izquierda: x = 0
 * - Pared derecha: x = anchoMapa - 1
 * - Pared superior: y = 0
 * - Pared inferior: y = altoMapa - 1
 *
 * @param random Generador de números pseudoaleatorios inyectable para facilitar pruebas unitarias.
 */
class GeneradorParedes(
    private val random: Random = Random.Default
) : GeneradorMinerales {

    /**
     * Coloca minerales exclusivamente en las paredes del mapa, ajustando el tipo de mineral
     * según la profundidad batimétrica especificada.
     *
     * @param anchoMapa Ancho de la cuadrícula del mapa (eje X). Debe ser mayor a 0.
     * @param altoMapa Alto de la cuadrícula del mapa (eje Y). Debe ser mayor a 0.
     * @param profundidadActual Profundidad del sector o submarino en metros.
     * @param cantidad Cantidad máxima de minerales a colocar en las paredes.
     * @return Lista de minerales ([Recolectable]) generados en las coordenadas perimetrales.
     */
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

        // Conjunto de todas las coordenadas de los cuatro bordes (paredes) sin repeticiones
        val coordenadasParedes = mutableSetOf<Pair<Int, Int>>()

        // Pared superior (y = 0) e inferior (y = altoMapa - 1)
        for (x in 0 until anchoMapa) {
            coordenadasParedes.add(Pair(x, 0))
            coordenadasParedes.add(Pair(x, altoMapa - 1))
        }

        // Pared izquierda (x = 0) y derecha (x = anchoMapa - 1)
        for (y in 0 until altoMapa) {
            coordenadasParedes.add(Pair(0, y))
            coordenadasParedes.add(Pair(anchoMapa - 1, y))
        }

        // Determinar el tipo de mineral correspondiente según la profundidad
        val tipoMineral = TipoMineral.obtenerPorProfundidad(profundidadActual)

        // Tomar aleatoriamente hasta la cantidad solicitada (o el total de celdas de pared disponibles)
        val coordenadasSeleccionadas = coordenadasParedes.shuffled(random).take(cantidad)

        // Instanciar cada mineral asegurando unicidad en su identificador
        return coordenadasSeleccionadas.mapIndexed { index, (x, y) ->
            val idUnico = "MIN-${tipoMineral.name.take(4)}-${UUID.randomUUID().toString().take(6).uppercase()}-$index"
            Mineral(
                id = idUnico,
                tipo = tipoMineral,
                posX = x,
                posY = y,
                recolectado = false
            )
        }
    }
}
