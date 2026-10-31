package submarino

/**
 * Genera y describe los minerales procedurales colocados en las paredes del mapa.
 */
class DemostradorGeneracionProcedural {

    fun demostrar(ancho: Int, alto: Int): MutableList<Recolectable> {
        println("3. GENERACIÓN PROCEDURAL EN PAREDES (Profundidad = 200m - Zona Mesopelágica):")
        val generador: GeneradorMinerales = GeneradorParedes()
        val minerales = generador.generarEnParedes(
            anchoMapa = ancho,
            altoMapa = alto,
            profundidadActual = 200.0,
            cantidad = 6
        ).toMutableList()

        println("   Minerales generados: ${minerales.size}")
        var todosEnParedes = true
        minerales.forEachIndexed { idx, item ->
            val esPared = (item.posX == 0 || item.posX == ancho - 1 || item.posY == 0 || item.posY == alto - 1)
            if (!esPared) todosEnParedes = false
            println("   [$idx] ID: ${item.id} | ${item.nombre} | Pos: (${item.posX}, ${item.posY}) | Peso: ${item.peso} kg | Valor: $${item.valor} | ¿En pared?: $esPared")
        }
        println("   >> Verificación de paredes: ${if (todosEnParedes) "EXITOSA (100% en límites)" else "FALLÓ"}\n")
        return minerales
    }
}
