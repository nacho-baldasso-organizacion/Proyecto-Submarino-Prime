package submarino

/**
 * Presenta las dimensiones y límites del mapa usado en la demostración de la Issue #5.
 */
class DemostradorConfiguracionMapa {

    fun demostrar(ancho: Int, alto: Int) {
        println("1. CONFIGURACIÓN DEL MAPA:")
        println("   Dimensiones: ${ancho}x${alto} cuadrículas (X: 0..${ancho - 1}, Y: 0..${alto - 1})")
        println("   Paredes delimitadas en: x=0, x=${ancho - 1}, y=0, y=${alto - 1}\n")
    }
}
