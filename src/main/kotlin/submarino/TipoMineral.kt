package submarino

/**
 * Catálogo oficial de tipos de minerales y recursos marinos según la especificación de la Issue #5.
 *
 * Incluye los valores económicos, pesos y zonas batimétricas oficiales:
 * - Arena de Cuarzo: Superficie (0-50m), Peso: 1.0 kg, Valor: $10
 * - Cobre Marino: Zona Fótica (50-150m), Peso: 2.5 kg, Valor: $35
 * - Nódulo de Manganeso: Zona Mesopelágica (150-400m), Peso: 4.0 kg, Valor: $90
 * - Titanio Cristalino: Fosa Abisal (400-800m), Peso: 5.0 kg, Valor: $220
 * - Gema de Salmuera: Pozos de Alta Densidad (>800m), Peso: 1.5 kg, Valor: $500
 * - Artefacto Hundido: Cavernas Ocultas, Peso: 8.0 kg, Valor: $1200
 */
enum class TipoMineral(
    val nombreMineral: String,
    val zona: String,
    val peso: Double,
    val valor: Int,
    val profundidadMin: Double,
    val profundidadMax: Double
) {
    ARENA_DE_CUARZO(
        nombreMineral = "Arena de Cuarzo",
        zona = "Superficie (0-50m)",
        peso = 1.0,
        valor = 10,
        profundidadMin = 0.0,
        profundidadMax = 50.0
    ),
    COBRE_MARINO(
        nombreMineral = "Cobre Marino",
        zona = "Zona Fótica (50-150m)",
        peso = 2.5,
        valor = 35,
        profundidadMin = 50.0,
        profundidadMax = 150.0
    ),
    NODULO_DE_MANGANESO(
        nombreMineral = "Nódulo de Manganeso",
        zona = "Zona Mesopelágica (150-400m)",
        peso = 4.0,
        valor = 90,
        profundidadMin = 150.0,
        profundidadMax = 400.0
    ),
    TITANIO_CRISTALINO(
        nombreMineral = "Titanio Cristalino",
        zona = "Fosa Abisal (400-800m)",
        peso = 5.0,
        valor = 220,
        profundidadMin = 400.0,
        profundidadMax = 800.0
    ),
    GEMA_DE_SALMUERA(
        nombreMineral = "Gema de Salmuera",
        zona = "Pozos de Alta Densidad (>800m)",
        peso = 1.5,
        valor = 500,
        profundidadMin = 800.0,
        profundidadMax = Double.MAX_VALUE
    ),
    ARTEFACTO_HUNDIDO(
        nombreMineral = "Artefacto Hundido",
        zona = "Cavernas Ocultas",
        peso = 8.0,
        valor = 1200,
        profundidadMin = 0.0,
        profundidadMax = Double.MAX_VALUE
    );

    companion object {
        fun obtenerPorProfundidad(profundidad: Double, esCavernaOculta: Boolean = false): TipoMineral {
            if (esCavernaOculta) return ARTEFACTO_HUNDIDO
            return when {
                profundidad < 50.0 -> ARENA_DE_CUARZO
                profundidad < 150.0 -> COBRE_MARINO
                profundidad < 400.0 -> NODULO_DE_MANGANESO
                profundidad <= 800.0 -> TITANIO_CRISTALINO
                else -> GEMA_DE_SALMUERA
            }
        }
    }
}

