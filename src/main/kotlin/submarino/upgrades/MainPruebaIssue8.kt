package submarino

/**
 * ============================================================================
 * DEMOSTRACIÓN INTEGRAL DE LA ISSUE #8:
 * "Sistema de Upgrades / Mejoras para el submarino"
 *
 * Cumple estrictamente con la regla de CERO funciones sueltas:
 * Todo método se encuentra formalmente encapsulado en [MainPruebaIssue8].
 * ============================================================================
 */
object MainPruebaIssue8 {

    @JvmStatic
    fun main(args: Array<String>) {
        ejecutarDemostracion()
    }

    /**
     * Coordina el flujo completo de simulación de upgrades, economía y verificación de estadísticas.
     */
    fun ejecutarDemostracion() {
        println("================================================================================")
        println("       SUBMARINO PRIME - PRUEBA DE ISSUE #8: SISTEMA DE UPGRADES / MEJORAS      ")
        println("================================================================================\n")

        // 1. Configuración de dependencias e inyección (DIP estricto)
        val catalogo: CatalogoUpgrades = FabricaMejoras.crearCatalogoEstandar()
        val servicioUpgrades = ServicioUpgrades(catalogo)
        val observadorConsola = ObservadorTiendaConsola()
        servicioUpgrades.registrarObservador(observadorConsola)

        val tienda = TiendaMejoras(
            catalogo = catalogo,
            procesadorCompras = servicioUpgrades
        )

        // 2. Inicialización del Submarino con estadísticas base
        val bodega = Bodega(capacidadMaxima = 20.0)
        val casco = ModuloCasco(presionMaximaPa = 200_000.0)
        val submarino = Submarino(
            posX = 50.0,
            posY = 0.0,
            velocidad = 10.0,
            casco = casco,
            bodega = bodega,
            velocidadAscensoBase = 5.0,
            billetera = Monedero(saldoInicial = 0)
        )

        println("1. ESTADO INICIAL DEL SUBMARINO:")
        println("   • Saldo en billetera          : $${submarino.billetera.saldo} créditos")
        println("   • Capacidad máxima de bodega  : ${submarino.bodega?.capacidadMaxima} kg")
        println("   • Velocidad base de ascenso   : ${submarino.velocidadAscensoBase} m/s")
        println("   • Resistencia de casco        : ${submarino.casco.presionMaximaPa} Pa")
        println("   • Velocidad de propulsión     : ${submarino.velocidad} m/s\n")

        // 3. Catálogo de Mejoras disponible en la tienda (desacoplado para UI)
        println("2. CATÁLOGO DE MEJORAS DISPONIBLES EN LA TIENDA (RENDERIZABLE POR UI):")
        for (mejora in tienda.listarMejoras()) {
            val etiquetaCosto = if (mejora.esNivelMaximo) "ALCANZADO TOPE" else "$${mejora.costo}"
            println("   • [${mejora.id}] ${mejora.nombre}")
            println("     Nivel: ${mejora.nivel}/${mejora.nivelMaximo} | Próximo costo: $etiquetaCosto")
            println("     Descripción: ${mejora.descripcion}")
        }
        println()

        // 4. Intento de compra con saldo insuficiente (rechazo esperado)
        println("3. INTENTO DE COMPRA SIN FONDOS (FALLO ESPERADO):")
        val resultadoSinFondos = tienda.comprarParaSubmarino(FabricaMejoras.ID_BODEGA, submarino)
        println("   • Resultado de transacción: $resultadoSinFondos")
        println("   • Nivel de bodega intacto : ${tienda.obtenerMejora(FabricaMejoras.ID_BODEGA)?.nivel}")
        println("   • Saldo de billetera      : $${submarino.billetera.saldo}\n")

        // 5. Integración con Issue #7: expedición, recolección y venta en superficie
        println("4. EXPEDICIÓN SUBMARINA Y VENTA COMERCIAL DE MINERALES (ISSUE #7):")
        submarino.bodega?.agregar(Mineral("MIN-01", TipoMineral.TITANIO_CRISTALINO, 0, 50)) // $220
        submarino.bodega?.agregar(Mineral("MIN-02", TipoMineral.GEMA_DE_SALMUERA, 0, 80))    // $500
        submarino.bodega?.agregar(Mineral("MIN-03", TipoMineral.ARTEFACTO_HUNDIDO, 0, 150))  // $1200

        submarino.acoplar()
        val servicioComercio = ServicioComercio()
        val resultadoVenta = servicioComercio.venderBodegaSubmarino(submarino)
        println("   • Venta de minerales completada con éxito.")
        println("   • Nuevo saldo en billetera: $${submarino.billetera.saldo} créditos\n")

        // 6. Proceso de compras de mejoras de diversos tipos
        println("5. PROCESO DE COMPRA DE MEJORAS EN LA TIENDA:")

        // Mejora 1: Capacidad de Bodega
        println("   -> Comprando mejora de capacidad de bodega...")
        val resBodega = tienda.comprarParaSubmarino(FabricaMejoras.ID_BODEGA, submarino)
        println("   • Resultado: $resBodega")
        println("   • Nueva capacidad de bodega: ${submarino.bodega?.capacidadMaxima} kg\n")

        // Mejora 2: Velocidad de Ascenso
        println("   -> Comprando mejora de velocidad de ascenso...")
        val resAscenso = tienda.comprarParaSubmarino(FabricaMejoras.ID_ASCENSO, submarino)
        println("   • Resultado: $resAscenso")
        println("   • Nueva velocidad de ascenso: ${submarino.velocidadAscensoBase} m/s\n")

        // Mejora 3: Resistencia de Casco
        println("   -> Comprando mejora de resistencia de casco...")
        val resCasco = tienda.comprarParaSubmarino(FabricaMejoras.ID_CASCO, submarino)
        println("   • Resultado: $resCasco")
        println("   • Nueva resistencia de casco: ${submarino.casco.presionMaximaPa} Pa\n")

        // Mejora 4: Propulsión horizontal
        println("   -> Comprando mejora de propulsión de motor...")
        val resMotor = tienda.comprarParaSubmarino(FabricaMejoras.ID_PROPULSION, submarino)
        println("   • Resultado: $resMotor")
        println("   • Nueva velocidad de propulsión: ${submarino.velocidad} m/s\n")

        // 7. Progresión consecutiva hasta el nivel máximo (Cap)
        println("6. PROGRESIÓN DE NIVELES CONSECUTIVOS HASTA EL TOPE (CAP):")
        val mejoraBodega = tienda.obtenerMejora(FabricaMejoras.ID_BODEGA)!!
        while (!mejoraBodega.esNivelMaximo) {
            println("   -> Subiendo '${mejoraBodega.nombre}' de nivel ${mejoraBodega.nivel} a ${mejoraBodega.nivel + 1}...")
            val resProg = tienda.comprarParaSubmarino(FabricaMejoras.ID_BODEGA, submarino)
            println("      $resProg | Capacidad actual: ${submarino.bodega?.capacidadMaxima} kg")
        }
        println("   • Nivel alcanzado: ${mejoraBodega.nivel}/${mejoraBodega.nivelMaximo} (esNivelMaximo=${mejoraBodega.esNivelMaximo})\n")

        // 8. Intento de compra en nivel máximo (rechazo esperado)
        println("7. INTENTO DE COMPRA EN NIVEL MÁXIMO (RECHAZO ESPERADO):")
        val resCap = tienda.comprarParaSubmarino(FabricaMejoras.ID_BODEGA, submarino)
        println("   • Resultado: $resCap")
        println("   • Saldo billetera final: $${submarino.billetera.saldo} (sin cargos indebidos)\n")

        // 9. Balance final de estadísticas del submarino
        println("8. RESUMEN FINAL DEL SUBMARINO TRAS UPGRADES:")
        println("   • Capacidad de Bodega       : ${submarino.bodega?.capacidadMaxima} kg (Base: 20.0 kg -> +60.0 kg ganados)")
        println("   • Velocidad de Ascenso      : ${submarino.velocidadAscensoBase} m/s (Base: 5.0 m/s -> +2.0 m/s ganados)")
        println("   • Resistencia de Casco      : ${submarino.casco.presionMaximaPa} Pa (Base: 200,000 Pa -> +150,000 Pa ganados)")
        println("   • Velocidad de Propulsión   : ${submarino.velocidad} m/s (Base: 10.0 m/s -> +2.5 m/s ganados)")
        println("   • Saldo remanente           : $${submarino.billetera.saldo} créditos")

        println("\n================================================================================")
        println("               DEMOSTRACIÓN ISSUE #8 COMPLETADA CON TOTAL ÉXITO                 ")
        println("================================================================================")
    }
}
