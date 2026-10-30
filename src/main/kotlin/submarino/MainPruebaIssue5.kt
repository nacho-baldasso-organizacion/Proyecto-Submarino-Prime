package submarino

/**
 * Punto de entrada de prueba y demostración para la Issue #5:
 * "Clase Mineral y Lógica de Recolección" del proyecto Submarino Prime.
 *
 * Coordina demostradores pequeños y enfocados, respetando la regla de CERO funciones sueltas.
 */
object MainPruebaIssue5 {

    private const val ANCHO_MAPA = 12
    private const val ALTO_MAPA = 10

    @JvmStatic
    fun main(args: Array<String> = emptyArray()) {
        ejecutarDemostracion()
    }

    /**
     * Coordina el flujo completo de simulación de recolección y minerales.
     */
    fun ejecutarDemostracion() {
        VisualizadorConsola.imprimirEncabezado("SUBMARINO PRIME - PRUEBA DE ISSUE #5: MINERALES Y RECOLECCIÓN")

        DemostradorConfiguracionMapa().demostrar(ANCHO_MAPA, ALTO_MAPA)
        DemostradorCatalogoBiomas().demostrar()

        val minerales = DemostradorGeneracionProcedural().demostrar(ANCHO_MAPA, ALTO_MAPA)
        val bodega = Bodega(capacidadMaxima = 7.0)
        val servicio: ServicioRecoleccionContrato = ServicioRecoleccion(bodega, DetectorColisionesConcreto())

        DemostradorNavegacionRecoleccion().demostrar(ANCHO_MAPA, ALTO_MAPA, minerales, bodega, servicio)
        DemostradorReporteBodega().demostrar(bodega)
        VisualizadorConsola.imprimirCierre("PRUEBA ISSUE #5 COMPLETADA")
    }
}
