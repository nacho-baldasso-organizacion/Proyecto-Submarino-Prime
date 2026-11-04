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
            return Color.web("#082A31")
        }
        return when {
            profundidad < 850.0 -> Color.web("#1C6064")
            profundidad < 1500.0 -> Color.web("#14505A")
            profundidad < 2200.0 -> Color.web("#0C3C46")
            profundidad < 2700.0 -> Color.web("#083039")
            else -> Color.web("#05242C")
        }
    }
}
