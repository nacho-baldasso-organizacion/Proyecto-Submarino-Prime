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
 *
 * @property nombreMineral Nombre descriptivo del mineral.
 * @property zona Zona batimétrica o bioma donde se origina.
 * @property peso Peso en kilogramos (kg).
 * @property valor Valor comercial en dólares ($).
 * @property profundidadMin Profundidad mínima del rango en metros.
 * @property profundidadMax Profundidad máxima del rango en metros.
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
        /**
         * Determina el tipo de mineral correspondiente según la profundidad batimétrica indicada.
         * Si se señala que el entorno son cavernas ocultas, se obtiene el mineral legendario [ARTEFACTO_HUNDIDO].
         *
         * @param profundidad Profundidad en metros.
         * @param esCavernaOculta Indica si la zona explorada es una caverna oculta especial.
         * @return [TipoMineral] perteneciente a dicho bioma.
         */
        fun obtenerPorProfundidad(profundidad: Double, esCavernaOculta: Boolean = false): TipoMineral {
            if (esCavernaOculta) {
                return ARTEFACTO_HUNDIDO
            }
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

/**
 * Representa una veta física o ejemplar de mineral colocado en una coordenada del mapa.
 * Implementa [Recolectable] cumpliendo con LSP (Liskov Substitution Principle).
 *
 * @property id Identificador único alfanumérico del mineral.
 * @property tipo Categoría taxonómica del mineral según [TipoMineral].
 * @property posX Coordenada horizontal (eje X) en la cuadrícula.
 * @property posY Coordenada vertical (eje Y) en la cuadrícula.
 * @property recolectado Estado actual de recolección (por defecto false).
 */
data class Mineral(
    override val id: String,
    val tipo: TipoMineral,
    override val posX: Int,
    override val posY: Int,
    override var recolectado: Boolean = false
) : Recolectable {
    override val nombre: String
        get() = tipo.nombreMineral

    override val peso: Double
        get() = tipo.peso

    override val valor: Int
        get() = tipo.valor

    override fun toString(): String {
        return "Mineral(id='$id', nombre='$nombre', zona='${tipo.zona}', peso=${peso}kg, valor=$$valor, pos=($posX, $posY), recolectado=$recolectado)"
    }
}
