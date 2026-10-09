package submarino

/**
 * Punto de entrada de prueba y demostración para la Issue #6:
 * "Cálculo de peso total y física de flotabilidad/ascenso del submarino".
 *
 * Demuestra:
 * 1. Inicialización de la nave con peso estructural base constante y bodega de carga desacoplada.
 * 2. Cálculo reactivo de masa total según la acumulación de minerales en la bodega (SRP, DIP).
 * 3. Variación de la velocidad y fuerza de ascenso con bodega vacía, a media carga y llena.
 * 4. Maniobras de ascenso en el agua demostrando la ralentización física del desplazamiento vertical.
 * 5. Principio Abierto/Cerrado (OCP) intercambiando estrategias físicas de ascenso:
 *    - Estrategia Lineal Estándar
 *    - Estrategia Inercial por Ratio de Masa
 *    - Estrategia de Flotabilidad Hidrostática de Arquímedes
 */
fun main() {
    println("================================================================================")
    println("       SUBMARINO PRIME - PRUEBA DE ISSUE #6: PESO TOTAL Y FÍSICA DE ASCENSO     ")
    println("================================================================================\n")

    // 1. Configuración inicial de la nave
    val pesoBase = 1000.0 // kg (masa constante del submarino en vacío)
    val capacidadBodega = 20.0 // kg
    val velocidadNominal = 10.0 // m/s
    val bodega = Bodega(capacidadMaxima = capacidadBodega)
    val casco = ModuloCasco(presionMaximaPa = 500_000.0)

    val submarino = Submarino(
        posX = 0.0,
        posY = 100.0, // Sumergido a 100 metros de profundidad
        velocidad = velocidadNominal,
        casco = casco,
        pesoBase = pesoBase,
        bodega = bodega,
        estrategiaAscenso = EstrategiaAscensoLineal(factorReduccionMaximo = 0.5)
    )

    println("1. ESPECIFICACIONES INICIALES DE LA NAVE:")
    println("   • Peso base estructural constante : ${submarino.pesoBase} kg")
    println("   • Capacidad máxima de la bodega   : ${bodega.capacidadMaxima} kg")
    println("   • Velocidad nominal de motores    : ${submarino.velocidadAscensoBase} m/s")
    println("   • Profundidad inicial             : ${submarino.posY} m\n")

    // 2. Estado A: Bodega Vacía (0% carga)
    println("--------------------------------------------------------------------------------")
    println(" A) ESTADO CON BODEGA VACÍA (0% CARGA)")
    println("--------------------------------------------------------------------------------")
    var estado = submarino.obtenerEstadoAscenso()
    println("   • Carga en bodega       : ${estado.pesoCarga} kg")
    println("   • Peso total nave       : ${estado.pesoTotal} kg (Base: ${estado.pesoBase} kg + Carga: ${estado.pesoCarga} kg)")
    println("   • Factor de rendimiento : ${"%.2f".format(estado.factorVelocidad * 100)}%")
    println("   • Velocidad de ascenso  : ${"%.2f".format(estado.velocidadAscensoEfectiva)} m/s (Máximo desempeño)")

    println("\n   -> Maniobra de prueba: Ascender 20.0 metros nominales...")
    val posYAntesVacia = submarino.posY
    submarino.ascender(20.0)
    val distanciaAscendidaVacia = posYAntesVacia - submarino.posY
    println("      Profundidad previa : $posYAntesVacia m -> Profundidad actual: ${submarino.posY} m")
    println("      Distancia efectiva : ${"%.2f".format(distanciaAscendidaVacia)} m (100% de la orden nominal)\n")

    // 3. Estado B: Media Carga (50% carga = 10.0 kg)
    println("--------------------------------------------------------------------------------")
    println(" B) ESTADO A MEDIA CARGA (50% CARGA - 10.0 kg)")
    println("--------------------------------------------------------------------------------")
    val mineral1 = Mineral("MIN-01", TipoMineral.TITANIO_CRISTALINO, 0, 80) // 5.0 kg
    val mineral2 = Mineral("MIN-02", TipoMineral.TITANIO_CRISTALINO, 0, 80) // 5.0 kg
    bodega.agregar(mineral1)
    bodega.agregar(mineral2)

    estado = submarino.obtenerEstadoAscenso()
    println("   • Carga en bodega       : ${estado.pesoCarga} kg / ${estado.capacidadMaximaBodega} kg")
    println("   • Peso total nave       : ${estado.pesoTotal} kg")
    println("   • Factor de rendimiento : ${"%.2f".format(estado.factorVelocidad * 100)}%")
    println("   • Velocidad de ascenso  : ${"%.2f".format(estado.velocidadAscensoEfectiva)} m/s (Ralentización moderada)")

    println("\n   -> Maniobra de prueba: Ascender 20.0 metros nominales...")
    val posYAntesMedia = submarino.posY
    submarino.ascender(20.0)
    val distanciaAscendidaMedia = posYAntesMedia - submarino.posY
    println("      Profundidad previa : $posYAntesMedia m -> Profundidad actual: ${submarino.posY} m")
    println("      Distancia efectiva : ${"%.2f".format(distanciaAscendidaMedia)} m (Efecto de ralentización: 75% del valor nominal)\n")

    // 4. Estado C: Bodega Llena (100% carga = 20.0 kg)
    println("--------------------------------------------------------------------------------")
    println(" C) ESTADO CON BODEGA LLENA (100% CARGA - 20.0 kg)")
    println("--------------------------------------------------------------------------------")
    val mineral3 = Mineral("MIN-03", TipoMineral.TITANIO_CRISTALINO, 0, 60) // 5.0 kg
    val mineral4 = Mineral("MIN-04", TipoMineral.TITANIO_CRISTALINO, 0, 60) // 5.0 kg
    bodega.agregar(mineral3)
    bodega.agregar(mineral4)

    estado = submarino.obtenerEstadoAscenso()
    println("   • Carga en bodega       : ${estado.pesoCarga} kg / ${estado.capacidadMaximaBodega} kg [BODEGA SATURADA]")
    println("   • Peso total nave       : ${estado.pesoTotal} kg")
    println("   • Factor de rendimiento : ${"%.2f".format(estado.factorVelocidad * 100)}%")
    println("   • Velocidad de ascenso  : ${"%.2f".format(estado.velocidadAscensoEfectiva)} m/s (Ralentización máxima)")

    println("\n   -> Maniobra de prueba: Ascender 20.0 metros nominales...")
    val posYAntesLlena = submarino.posY
    submarino.ascender(20.0)
    val distanciaAscendidaLlena = posYAntesLlena - submarino.posY
    println("      Profundidad previa : $posYAntesLlena m -> Profundidad actual: ${submarino.posY} m")
    println("      Distancia efectiva : ${"%.2f".format(distanciaAscendidaLlena)} m (Efecto de ralentización máxima: 50% del valor nominal)\n")

    // 5. Tabla comparativa resumen
    println("--------------------------------------------------------------------------------")
    println(" TABLA COMPARATIVA DE DESEMPEÑO DINÁMICO")
    println("--------------------------------------------------------------------------------")
    println("  %-16s | %-12s | %-14s | %-16s | %-18s".format("Condición", "Carga (kg)", "Peso Total", "Velocidad Asc.", "Ascenso (en 20m)"))
    println("  --------------------------------------------------------------------------------")
    println("  %-16s | %10.1f kg | %12.1f kg | %14.2f m/s | %16.2f m".format("Bodega Vacía", 0.0, 1000.0, 10.0, distanciaAscendidaVacia))
    println("  %-16s | %10.1f kg | %12.1f kg | %14.2f m/s | %16.2f m".format("Media Carga", 10.0, 1010.0, 7.5, distanciaAscendidaMedia))
    println("  %-16s | %10.1f kg | %12.1f kg | %14.2f m/s | %16.2f m".format("Bodega Llena", 20.0, 1020.0, 5.0, distanciaAscendidaLlena))
    println()

    // 6. Demostración práctica de Principios SOLID (OCP, DIP, ISP)
    println("--------------------------------------------------------------------------------")
    println(" DEMOSTRACIÓN DE PRINCIPIOS SOLID (OCP, DIP, ISP)")
    println("--------------------------------------------------------------------------------")

    // OCP: Intercambio dinámico de estrategias sin modificar la clase Submarino
    println("-> 1. OCP: Comparación de Estrategias Físicas alternativas con bodega llena (20 kg):")
    val subLineal = Submarino(pesoBase = 1000.0, bodega = bodega, velocidad = 10.0, casco = casco, estrategiaAscenso = EstrategiaAscensoLineal(0.5))
    val subMasa = Submarino(pesoBase = 1000.0, bodega = bodega, velocidad = 10.0, casco = casco, estrategiaAscenso = EstrategiaAscensoPorMasa())
    val subArquimedes = Submarino(pesoBase = 1000.0, bodega = bodega, velocidad = 10.0, casco = casco, estrategiaAscenso = EstrategiaFisicaFlotabilidad(factorEmpujeMinimo = 0.3))

    println("   • Estrategia Lineal (50% máx red.)   : Vel = ${"%.2f".format(subLineal.velocidadAscenso)} m/s (Factor: ${"%.2f".format(subLineal.factorAscenso)})")
    println("   • Estrategia Inercial por Masa (m0/m): Vel = ${"%.2f".format(subMasa.velocidadAscenso)} m/s (Factor: ${"%.2f".format(subMasa.factorAscenso)})")
    println("   • Estrategia Flotabilidad Arquímedes : Vel = ${"%.2f".format(subArquimedes.velocidadAscenso)} m/s (Factor: ${"%.2f".format(subArquimedes.factorAscenso)})")

    // ISP & DIP: Manipulación polimórfica a través de Pesable
    println("\n-> 2. ISP & DIP: Agrupación polimórfica de entidades Pesable:")
    val itemsPesables: List<Pesable> = listOf(submarino, mineral1, mineral2, bodega)
    itemsPesables.forEachIndexed { i, it ->
        println("   [$i] Clase: %-25s | Peso reportado: %7.2f kg".format(it.javaClass.simpleName, it.peso))
    }

    println("\n================================================================================")
    println("                      SIMULACIÓN FINALIZADA CON ÉXITO                           ")
    println("================================================================================")
}
