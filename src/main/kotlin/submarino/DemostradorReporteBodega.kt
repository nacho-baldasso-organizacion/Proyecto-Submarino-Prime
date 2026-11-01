package submarino

/**
 * Presenta el reporte final del contenido y valor de la bodega.
 */
class DemostradorReporteBodega {

    fun demostrar(bodega: Bodega) {
        println("\n6. REPORTE FINAL DE LA BODEGA:")
        println("   $bodega")
        println("   Elementos en bodega:")
        bodega.obtenerElementos().forEachIndexed { index, item ->
            println("     [$index] ${item.nombre} - Peso: ${item.peso} kg - Valor: $${item.valor}")
        }
        println("   Valor total para el mercado: $${bodega.valorTotal}")
        println("   Peso total ocupado: ${bodega.pesoActual} kg / ${bodega.capacidadMaxima} kg")
    }
}
