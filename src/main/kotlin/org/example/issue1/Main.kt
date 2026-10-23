package submarino

/**
 * Punto de entrada principal de la aplicación, encapsulado dentro de [Main]
 * cumpliendo con la exigencia universitaria de cero funciones a nivel de módulo.
 */
object Main {

    @JvmStatic
    fun main(args: Array<String> = emptyArray()) {
        SimuladorSubmarino().ejecutarSimulacionCompleta()
    }
}
