package submarino

/**
 * ============================================================================
 * DEMOSTRACIÓN INTEGRAL DE LA ISSUE #7:
 * "Zona de acople en la superficie para venta de minerales y gestión de monedas"
 *
 * Cumple estrictamente con la regla de CERO funciones sueltas:
 * Todo método se encuentra formalmente encapsulado en [MainPruebaIssue7].
 * ============================================================================
 */
object MainPruebaIssue7 {

    @JvmStatic
    fun main(args: Array<String>) {
        ejecutarDemostracion()
    }

    /**
     * Coordina el flujo completo de simulación comercial y acople.
     */
    fun ejecutarDemostracion() {
        println("================================================================================")
        println("     SUBMARINO PRIME - PRUEBA DE ISSUE #7: ZONA DE ACOPLE, VENTA Y MONEDAS     ")
        println("================================================================================\n")

        // 1. Configuración de la Estación de Acople y Servicios (DIP / Inyección)
        val zonaSuperficie = ZonaSuperficie(profundidadMaxima = 0.0)
        val estacion = EstacionAcople(zonaAcople = zonaSuperficie)
        val calculadora = CalculadoraPreciosEstandar(estrategia = EstrategiaPreciosBase())
        val servicioComercio = ServicioComercio(calculadoraPrecios = calculadora)

        // Registro de observadores desacoplados (Patrón Observer para futura UI)
        estacion.registrarObservador(object : ObservadorAcople {
            override fun alAcoplar(nave: Acoplable) {
                println("   [EVENTO UI] ¡Nave amarrada exitosamente en la estación de acople!")
            }

            override fun alDesacoplar(nave: Acoplable) {
                println("   [EVENTO UI] ¡Nave desamarrada de la estación. Lista para inmersión!")
            }
        })

        servicioComercio.registrarObservador(ObservadorComercioConsola())

        // 2. Inicialización del Submarino con Bodega y Billetera
        val bodega = Bodega(capacidadMaxima = 25.0)
        val submarino = Submarino(
            posX = 50.0,
            posY = 120.0, // Sumergido a 120 metros
            velocidad = 10.0,
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = Monedero(saldoInicial = 0)
        )

        println("1. ESTADO INICIAL DEL SUBMARINO:")
        println("   • Posición           : (X: ${submarino.posX} m, Y: ${submarino.posY} m)")
        println("   • Saldo en billetera : $${submarino.billetera.saldo} créditos")
        println("   • Carga en bodega    : ${bodega.pesoActual} kg / ${bodega.capacidadMaxima} kg")
        println("   • Acoplado           : ${submarino.estaAcoplado}")
        println("   • En superficie      : ${submarino.estaEnSuperficie}\n")

        // 3. Cargar minerales en la bodega durante expedición profunda
        println("2. EXPEDICIÓN SUBMARINA - RECOLECCIÓN DE MINERALES:")
        val mineral1 = Mineral("MIN-01", TipoMineral.TITANIO_CRISTALINO, 0, 100) // 5.0 kg, $220
        val mineral2 = Mineral("MIN-02", TipoMineral.NODULO_DE_MANGANESO, 0, 100) // 4.0 kg, $90
        val mineral3 = Mineral("MIN-03", TipoMineral.GEMA_DE_SALMUERA, 0, 100) // 1.5 kg, $500
        val mineral4 = Mineral("MIN-04", TipoMineral.COBRE_MARINO, 0, 100) // 2.5 kg, $35
        bodega.agregar(mineral1)
        bodega.agregar(mineral2)
        bodega.agregar(mineral3)
        bodega.agregar(mineral4)

        println("   • Minerales recolectados : ${bodega.cantidadItems} unidades")
        println("   • Peso total de carga    : ${bodega.pesoActual} kg")
        println("   • Valoración total ($)   : $${bodega.valorTotal}")
        println("   • Velocidad de ascenso   : ${"%.2f".format(submarino.velocidadAscenso)} m/s (reducida por peso)\n")

        // 4. Intento de venta antes de acoplarse (debe fallar por regla de negocio)
        println("3. INTENTO DE VENTA PREVIA AL ACOPLE (FALLO ESPERADO):")
        val resultadoFallo = servicioComercio.venderBodegaSubmarino(submarino)
        println("   • Intento de venta sin acoplar : $resultadoFallo")
        println("   • Saldo en billetera           : $${submarino.billetera.saldo} (sin cambios)")
        println("   • Carga en bodega              : ${bodega.pesoActual} kg (minerales intactos)\n")

        // 5. Ascenso a la superficie y amarre en la estación
        println("4. ASCENSO HACIA LA SUPERFICIE Y DETECCIÓN DE ZONA DE ACOPLE:")
        while (submarino.posY > 0.0) {
            submarino.ascender(40.0)
        }
        println("   • Posición tras ascenso : (X: ${submarino.posX} m, Y: ${submarino.posY} m)")
        println("   • En superficie         : ${submarino.estaEnSuperficie}")
        println("   • En zona de estación   : ${submarino.estaEnZonaAcople(zonaSuperficie)}")

        println("\n   -> Procediendo al amarre en la estación...")
        val acopleExitoso = estacion.acoplar(submarino, submarino.posX, submarino.posY)
        println("   • Acople concretado     : $acopleExitoso")
        println("   • Estado nave acoplada  : ${submarino.estaAcoplado}")
        println("   • Estación ocupada      : ${estacion.hayNaveAcoplada}\n")

        // 6. Transacción comercial en la superficie
        println("5. PROCESO DE VENTA Y TRANSFERENCIA DE CRÉDITOS:")
        val resultadoVenta = servicioComercio.venderBodegaSubmarino(submarino)
        when (resultadoVenta) {
            is ResultadoComercio.Exito -> {
                println("   • Estado de transacción : ÉXITO")
                println("   • Unidades vendidas     : ${resultadoVenta.transaccion.cantidadItems}")
                println("   • Créditos acreditados  : +$${resultadoVenta.transaccion.totalCreditos}")
                println("   • Saldo final billetera : $${resultadoVenta.saldoActualizado}")
            }
            ResultadoComercio.NaveNoAcoplada -> println("   • Error: Nave no acoplada")
            ResultadoComercio.BodegaVacia -> println("   • Error: Bodega vacía")
        }

        println("   • Estado actual bodega  : ${bodega.pesoActual} kg (vaciada tras venta)")
        println("   • Items en bodega       : ${bodega.cantidadItems}")
        println("   • Velocidad restaurada  : ${"%.2f".format(submarino.velocidadAscenso)} m/s (100% desempeño nominal)\n")

        // 7. Desacople y partida para una nueva inmersión
        println("6. DESACOPLE Y PARTIDA PARA NUEVA MISIÓN:")
        estacion.desacoplar()
        println("   • Estado nave acoplada  : ${submarino.estaAcoplado}")
        println("   • Estación libre        : ${!estacion.hayNaveAcoplada}")

        println("\n================================================================================")
        println("                     DEMOSTRACIÓN FINALIZADA CON ÉXITO                          ")
        println("================================================================================")
    }
}
