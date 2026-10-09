package submarino

/**
 * Punto de entrada de prueba y demostración para la Issue #5:
 * "Clase Mineral y Lógica de Recolección" del proyecto Submarino Prime.
 *
 * Demuestra:
 * 1. Generación procedural de minerales en las paredes del mapa respetando zonas y profundidades.
 * 2. Comprobación geométrica de que todos los minerales residen en las paredes perimetrales.
 * 3. Detección de colisiones mediante coordenadas discretas (X, Y).
 * 4. Recolección automática e ingreso a la bodega.
 * 5. Control estricto de peso máximo en bodega (rechazo por exceso de peso).
 * 6. Balance económico de valor acumulado en el mercado.
 */
fun main() {
    println("================================================================================")
    println("         SUBMARINO PRIME - PRUEBA DE ISSUE #5: MINERALES Y RECOLECCIÓN          ")
    println("================================================================================\n")

    // 1. Configuración del mapa submarino
    val anchoMapa = 12
    val altoMapa = 10
    println("1. CONFIGURACIÓN DEL MAPA:")
    println("   Dimensiones: ${anchoMapa}x${altoMapa} cuadrículas (X: 0..${anchoMapa - 1}, Y: 0..${altoMapa - 1})")
    println("   Paredes delimitadas en: x=0, x=${anchoMapa - 1}, y=0, y=${altoMapa - 1}\n")

    // 2. Demostración de catálogo de minerales según profundidad y zona
    println("2. CATÁLOGO DE MINERALES Y BIOMAS:")
    val profundidadesDePrueba = listOf(
        25.0 to false,   // Superficie (Arena de Cuarzo)
        100.0 to false,  // Zona Fótica (Cobre Marino)
        250.0 to false,  // Zona Mesopelágica (Nódulo de Manganeso)
        600.0 to false,  // Fosa Abisal (Titanio Cristalino)
        1200.0 to false, // Pozos de Alta Densidad (Gema de Salmuera)
        300.0 to true    // Cavernas Ocultas (Artefacto Hundido)
    )

    profundidadesDePrueba.forEach { (profundidad, esCaverna) ->
        val tipo = TipoMineral.obtenerPorProfundidad(profundidad, esCaverna)
        val etiquetaEntorno = if (esCaverna) "Caverna Oculta" else "${profundidad}m"
        println("   - Profundidad: %-16s -> Mineral: %-22s | Zona: %-28s | Peso: %3.1f kg | Valor: $%4d".format(
            etiquetaEntorno, tipo.nombreMineral, tipo.zona, tipo.peso, tipo.valor
        ))
    }
    println()

    // 3. Generación procedural de minerales en las paredes del mapa
    println("3. GENERACIÓN PROCEDURAL EN PAREDES (Profundidad = 200m - Zona Mesopelágica):")
    val generador: GeneradorMinerales = GeneradorParedes()
    val cantidadAGenerar = 6
    val profundidadActual = 200.0
    val mineralesEnMapa = generador.generarEnParedes(
        anchoMapa = anchoMapa,
        altoMapa = altoMapa,
        profundidadActual = profundidadActual,
        cantidad = cantidadAGenerar
    ).toMutableList()

    println("   Minerales generados: ${mineralesEnMapa.size}")
    var todosEnParedes = true
    mineralesEnMapa.forEachIndexed { idx, item ->
        val esPared = (item.posX == 0 || item.posX == anchoMapa - 1 || item.posY == 0 || item.posY == altoMapa - 1)
        if (!esPared) todosEnParedes = false
        println("   [$idx] ID: ${item.id} | ${item.nombre} | Pos: (${item.posX}, ${item.posY}) | Peso: ${item.peso} kg | Valor: $${item.valor} | ¿En pared?: $esPared")
    }
    println("   >> Verificación de paredes: ${if (todosEnParedes) "EXITOSA (100% en límites)" else "FALLÓ"}\n")

    // 4. Inicialización de Bodega y Servicio de Recolección con inyección de dependencias (DIP)
    val capacidadMaximaBodega = 7.0 // Límite de 7.0 kg para probar fácilmente la saturación
    val bodega: Almacenamiento = Bodega(capacidadMaxima = capacidadMaximaBodega)
    val detectorColisiones: DetectorColisiones = DetectorColisionesConcreto()
    val servicioRecoleccion = ServicioRecoleccion(
        almacenamiento = bodega,
        detectorColisiones = detectorColisiones
    )

    println("4. INICIALIZACIÓN DE LA BODEGA Y SERVICIO DE RECOLECCIÓN:")
    println("   Capacidad Máxima de Bodega: ${bodega.capacidadMaxima} kg")
    println("   Peso inicial: ${bodega.pesoActual} kg\n")

    // 5. Simulación de movimiento del submarino y pruebas de colisión
    println("5. SIMULACIÓN DE NAVEGACIÓN Y RECOLECCIÓN:")

    // Caso A: El submarino está en el centro del mapa (sin mineral)
    val posicionCentro = Posicion(posX = anchoMapa / 2, posY = altoMapa / 2)
    println("   A) Submarino navega a posición central (${posicionCentro.posX}, ${posicionCentro.posY}):")
    when (val res = servicioRecoleccion.procesarRecoleccion(posicionCentro, mineralesEnMapa)) {
        is ResultadoRecoleccion.SinColision -> println("      -> [OK] Sin colisión detectada en aguas abiertas.")
        else -> println("      -> Resultado inesperado: $res")
    }

    // Caso B: El submarino llega a la posición del primer mineral en la pared
    val primerMineral = mineralesEnMapa.first()
    val posicionConMineral1 = Posicion(posX = primerMineral.posX, posY = primerMineral.posY)
    println("\n   B) Submarino navega a pared en (${posicionConMineral1.posX}, ${posicionConMineral1.posY}):")
    println("      Mineral objetivo: ${primerMineral.nombre} (Peso: ${primerMineral.peso} kg, Valor: $${primerMineral.valor})")

    when (val res = servicioRecoleccion.procesarRecoleccion(posicionConMineral1, mineralesEnMapa)) {
        is ResultadoRecoleccion.Exito -> {
            println("      -> [ÉXITO] Recolección completada.")
            println("         Mineral recolectado: ${res.item.nombre} (ID: ${res.item.id})")
            println("         Peso actual en bodega: ${res.pesoAcumulado} kg / ${bodega.capacidadMaxima} kg")
            println("         Estado recolectado del mineral: ${res.item.recolectado}")
        }
        is ResultadoRecoleccion.BodegaLlena -> println("      -> [ERROR] Bodega llena: excedente ${res.excesoPeso} kg.")
        ResultadoRecoleccion.SinColision -> println("      -> [ERROR] No hubo colisión.")
        is ResultadoRecoleccion.YaRecolectado -> println("      -> [AVISO] Ya fue recolectado.")
    }

    // Caso C: Reintentar recolección en la misma coordenada (debe ignorarse porque ya fue recolectado)
    println("\n   C) Submarino intenta recolectar nuevamente en (${posicionConMineral1.posX}, ${posicionConMineral1.posY}):")
    when (val res = servicioRecoleccion.procesarRecoleccion(posicionConMineral1, mineralesEnMapa)) {
        ResultadoRecoleccion.SinColision -> println("      -> [OK] El mineral ya fue retirado del mapa, no hay colisión activa.")
        is ResultadoRecoleccion.YaRecolectado -> println("      -> [OK] El ítem ya se encuentra marcado como recolectado.")
        else -> println("      -> Resultado: $res")
    }

    // Caso D: Agregar un artefacto pesado para probar el desbordamiento de la bodega
    val artefactoPesado = Mineral(
        id = "MIN-ARTEF-TEST-99",
        tipo = TipoMineral.ARTEFACTO_HUNDIDO,
        posX = 0,
        posY = 0,
        recolectado = false
    )
    mineralesEnMapa.add(artefactoPesado)
    val posicionArtefacto = Posicion(posX = 0, posY = 0)

    println("\n   D) Submarino se traslada a caverna oculta en (0, 0) y detecta ${artefactoPesado.nombre} (Peso: ${artefactoPesado.peso} kg):")
    println("      Capacidad disponible en bodega antes del intento: ${(bodega as Bodega).capacidadDisponible} kg")

    when (val res = servicioRecoleccion.procesarRecoleccion(posicionArtefacto, mineralesEnMapa)) {
        is ResultadoRecoleccion.BodegaLlena -> {
            println("      -> [BODEGA LLENA - RECHAZO CONTROLADO]")
            println("         No se puede cargar: ${res.item.nombre} de ${res.item.peso} kg.")
            println("         Exceso de capacidad: ${String.format("%.2f", res.excesoPeso)} kg por encima del límite de ${res.capacidadMaxima} kg.")
            println("         El mineral permanece en el mapa (recolectado = ${res.item.recolectado}).")
        }
        is ResultadoRecoleccion.Exito -> println("      -> [ERROR INESPERADO] Se cargó cuando debía exceder el peso.")
        else -> println("      -> Resultado: $res")
    }

    // 6. Resumen de la Bodega y Mercado
    println("\n6. REPORTE FINAL DE LA BODEGA:")
    println("   ${bodega}")
    println("   Elementos en bodega:")
    bodega.obtenerElementos().forEachIndexed { index, item ->
        println("     [$index] ${item.nombre} - Peso: ${item.peso} kg - Valor: $${item.valor}")
    }
    println("   Valor total para el mercado: $${(bodega as Bodega).valorTotal}")
    println("   Peso total ocupado: ${(bodega as Bodega).pesoActual} kg / ${bodega.capacidadMaxima} kg")

    println("\n================================================================================")
    println("                      SIMULACIÓN FINALIZADA CON ÉXITO                           ")
    println("================================================================================")
}
