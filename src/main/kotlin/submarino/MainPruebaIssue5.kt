package submarino

/**
 * Punto de entrada de prueba y demostración para la Issue #5:
 * "Clase Mineral y Lógica de Recolección" del proyecto Submarino Prime.
 *
 * Encapsulado en [MainPruebaIssue5] respetando la regla de CERO funciones sueltas
 * y descomposición en métodos cortos de baja complejidad ciclomática.
 */
object MainPruebaIssue5 {

    @JvmStatic
    fun main(args: Array<String> = emptyArray()) {
        ejecutarDemostracion()
    }

    /**
     * Coordina el flujo completo de simulación de recolección y minerales.
     */
    fun ejecutarDemostracion() {
        VisualizadorConsola.imprimirEncabezado("SUBMARINO PRIME - PRUEBA DE ISSUE #5: MINERALES Y RECOLECCIÓN")

        val anchoMapa = 12
        val altoMapa = 10
        demostrarConfiguracionMapa(anchoMapa, altoMapa)
        demostrarCatalogoBiomas()

        val minerales = demostrarGeneracionProcedural(anchoMapa, altoMapa)
        val bodega = Bodega(capacidadMaxima = 7.0)
        val servicio: ServicioRecoleccionContrato = ServicioRecoleccion(bodega, DetectorColisionesConcreto())

        demostrarNavegacionYRecoleccion(anchoMapa, altoMapa, minerales, bodega, servicio)
        demostrarReporteFinal(bodega)
        VisualizadorConsola.imprimirCierre("PRUEBA ISSUE #5 COMPLETADA")
    }

    private fun demostrarConfiguracionMapa(ancho: Int, alto: Int) {
        println("1. CONFIGURACIÓN DEL MAPA:")
        println("   Dimensiones: ${ancho}x${alto} cuadrículas (X: 0..${ancho - 1}, Y: 0..${alto - 1})")
        println("   Paredes delimitadas en: x=0, x=${ancho - 1}, y=0, y=${alto - 1}\n")
    }

    private fun demostrarCatalogoBiomas() {
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

    private fun demostrarGeneracionProcedural(ancho: Int, alto: Int): MutableList<Recolectable> {
        println("3. GENERACIÓN PROCEDURAL EN PAREDES (Profundidad = 200m - Zona Mesopelágica):")
        val generador: GeneradorMinerales = GeneradorParedes()
        val minerales = generador.generarEnParedes(
            anchoMapa = ancho,
            altoMapa = alto,
            profundidadActual = 200.0,
            cantidad = 6
        ).toMutableList()

        println("   Minerales generados: ${minerales.size}")
        var todosEnParedes = true
        minerales.forEachIndexed { idx, item ->
            val esPared = (item.posX == 0 || item.posX == ancho - 1 || item.posY == 0 || item.posY == alto - 1)
            if (!esPared) todosEnParedes = false
            println("   [$idx] ID: ${item.id} | ${item.nombre} | Pos: (${item.posX}, ${item.posY}) | Peso: ${item.peso} kg | Valor: $${item.valor} | ¿En pared?: $esPared")
        }
        println("   >> Verificación de paredes: ${if (todosEnParedes) "EXITOSA (100% en límites)" else "FALLÓ"}\n")
        return minerales
    }

    private fun demostrarNavegacionYRecoleccion(
        ancho: Int,
        alto: Int,
        minerales: MutableList<Recolectable>,
        bodega: Bodega,
        servicio: ServicioRecoleccionContrato
    ) {
        println("4. INICIALIZACIÓN DE LA BODEGA Y SERVICIO DE RECOLECCIÓN:")
        println("   Capacidad Máxima de Bodega: ${bodega.capacidadMaxima} kg")
        println("   Peso inicial: ${bodega.pesoActual} kg\n")

        println("5. SIMULACIÓN DE NAVEGACIÓN Y RECOLECCIÓN:")
        probarCentroMapa(ancho, alto, minerales, servicio)
        probarParedConMineral(minerales, servicio)
        probarReintentoMismaCoordenada(minerales, servicio)
        probarDesbordamientoBodega(minerales, bodega, servicio)
    }

    private fun probarCentroMapa(ancho: Int, alto: Int, minerales: List<Recolectable>, servicio: ServicioRecoleccionContrato) {
        val posCentro = Posicion(posX = ancho / 2, posY = alto / 2)
        println("   A) Submarino navega a posición central (${posCentro.posX}, ${posCentro.posY}):")
        val res = servicio.procesarRecoleccion(posCentro, minerales)
        println("      -> ${res.obtenerMensaje()}")
    }

    private fun probarParedConMineral(minerales: List<Recolectable>, servicio: ServicioRecoleccionContrato) {
        val mineral = minerales.first()
        val posMineral = Posicion(posX = mineral.posX, posY = mineral.posY)
        println("\n   B) Submarino navega a pared en (${posMineral.posX}, ${posMineral.posY}):")
        println("      Mineral objetivo: ${mineral.nombre} (Peso: ${mineral.peso} kg, Valor: $${mineral.valor})")
        val res = servicio.procesarRecoleccion(posMineral, minerales)
        println("      -> ${res.obtenerMensaje()}")
    }

    private fun probarReintentoMismaCoordenada(minerales: List<Recolectable>, servicio: ServicioRecoleccionContrato) {
        val mineral = minerales.first()
        val posMineral = Posicion(posX = mineral.posX, posY = mineral.posY)
        println("\n   C) Submarino intenta recolectar nuevamente en (${posMineral.posX}, ${posMineral.posY}):")
        val res = servicio.procesarRecoleccion(posMineral, minerales)
        println("      -> ${res.obtenerMensaje()}")
    }

    private fun probarDesbordamientoBodega(minerales: MutableList<Recolectable>, bodega: Bodega, servicio: ServicioRecoleccionContrato) {
        val artefactoPesado = Mineral(
            id = "MIN-ARTEF-TEST-99",
            tipo = TipoMineral.ARTEFACTO_HUNDIDO,
            posX = 0,
            posY = 0,
            recolectado = false
        )
        minerales.add(artefactoPesado)
        val posArtefacto = Posicion(posX = 0, posY = 0)

        println("\n   D) Submarino se traslada a caverna oculta en (0, 0) y detecta ${artefactoPesado.nombre} (Peso: ${artefactoPesado.peso} kg):")
        println("      Capacidad disponible en bodega antes del intento: ${bodega.capacidadDisponible} kg")
        val res = servicio.procesarRecoleccion(posArtefacto, minerales)
        println("      -> ${res.obtenerMensaje()}")
    }

    private fun demostrarReporteFinal(bodega: Bodega) {
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
