package submarino

import javafx.scene.paint.Color

/**
 * Resuelve el color base de una formación rocosa según su profundidad.
 *
 * Responsabilidad única (SRP): centralizar la paleta del terreno submarino.
 */
class PaletaTerrenoRocoso {

    fun colorBase(profundidad: Double, esSuelo: Boolean): Color {
        if (esSuelo) {
            return COLOR_SUELO
        }
        return FRANJAS_PROFUNDIDAD
            .firstOrNull { profundidad < it.first }
            ?.second
            ?: COLOR_ABISAL
    }

    companion object {
        private val COLOR_SUELO: Color = Color.web("#082A31")
        private val COLOR_ABISAL: Color = Color.web("#05242C")

        private val FRANJAS_PROFUNDIDAD: List<Pair<Double, Color>> = listOf(
            850.0 to Color.web("#1C6064"),
            1500.0 to Color.web("#14505A"),
            2200.0 to Color.web("#0C3C46"),
            2700.0 to Color.web("#083039")
        )
    }
}
