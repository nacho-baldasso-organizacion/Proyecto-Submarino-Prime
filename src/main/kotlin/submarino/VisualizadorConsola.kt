package submarino

/**
 * Utilidad compartida de renderizado y formato de texto en consola para reducir la duplicación (DRY).
 * Provee métodos reutilizables para encabezados, secciones y cierres de simulaciones.
 */
object VisualizadorConsola {

    private const val SEPARADOR_PRINCIPAL = "================================================================================"
    private const val SEPARADOR_SECUNDARIO = "--------------------------------------------------------------------------------"

    /**
     * Imprime un encabezado con formato estándar para las pruebas y simulaciones.
     */
    fun imprimirEncabezado(titulo: String) {
        println(SEPARADOR_PRINCIPAL)
        println("       $titulo")
        println("$SEPARADOR_PRINCIPAL\n")
    }

    /**
     * Imprime una línea divisoria de sección con un subtítulo descriptivo.
     */
    fun imprimirSeccion(subtitulo: String) {
        println(SEPARADOR_SECUNDARIO)
        println(" $subtitulo")
        println(SEPARADOR_SECUNDARIO)
    }

    /**
     * Imprime un cierre formal de simulación exitosa.
     */
    fun imprimirCierre(mensaje: String = "SIMULACIÓN FINALIZADA CON ÉXITO") {
        println("\n$SEPARADOR_PRINCIPAL")
        println("                      $mensaje                         ")
        println(SEPARADOR_PRINCIPAL)
    }
}
