package submarino

/**
 * Imprime el catálogo de minerales asociado a cada bioma/profundidad de la Issue #5.
 */
class DemostradorCatalogoBiomas {

    fun demostrar() {
        println("2. CATÁLOGO DE MINERALES Y BIOMAS:")
        val profundidadesDePrueba = listOf(
            25.0 to false,
            100.0 to false,
            250.0 to false,
            600.0 to false,
            1200.0 to false,
            300.0 to true
        )

        profundidadesDePrueba.forEach { (profundidad, esCaverna) ->
            val tipo = TipoMineral.obtenerPorProfundidad(profundidad, esCaverna)
            val etiquetaEntorno = if (esCaverna) "Caverna Oculta" else "${profundidad}m"
            println("   - Profundidad: %-16s -> Mineral: %-22s | Zona: %-28s | Peso: %3.1f kg | Valor: $%4d".format(
                etiquetaEntorno, tipo.nombreMineral, tipo.zona, tipo.peso, tipo.valor
            ))
        }
        println()
    }
}
