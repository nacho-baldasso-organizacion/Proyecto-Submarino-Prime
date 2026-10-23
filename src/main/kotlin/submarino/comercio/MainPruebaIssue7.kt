package submarino

/**
 * ============================================================================
 * DEMOSTRACIÓN INTEGRAL DE LA ISSUE #7:
 * "Zona de acople en la superficie para venta de minerales y gestión de monedas"
 *
 * Encapsulado en [MainPruebaIssue7] respetando la regla de CERO funciones sueltas
 * y descomposición en métodos breves de baja complejidad ciclomática.
 * ============================================================================
 */
object MainPruebaIssue7 {

    @JvmStatic
    fun main(args: Array<String> = emptyArray()) {
        ejecutarDemostracion()
    }

    /**
     * Coordina el flujo completo de simulación comercial y acople.
     */
    fun ejecutarDemostracion() {
        imprimirEncabezado()

        val zonaSuperficie = ZonaSuperficie(profundidadMaxima = 0.0)
        val estacion = EstacionAcople(zonaAcople = zonaSuperficie)
        val servicioComercio = ServicioComercio(CalculadoraPreciosEstandar())
        servicioComercio.registrarObservador(ObservadorComercioConsola())

        val submarino = configurarSubmarino()
        demostrarCargaEnProfundidad(submarino)
        demostrarFalloPorNoAcople(submarino, servicioComercio)
        demostrarAcopleYVenta(submarino, estacion, servicioComercio)
        demostrarDesacople(submarino, estacion)

        imprimirCierre()
    }

    private fun imprimirEncabezado() {
        println("================================================================================")
        println("     SUBMARINO PRIME - PRUEBA DE ISSUE #7: ZONA DE ACOPLE, VENTA Y MONEDAS     ")
        println("================================================================================\n")
    }

    private fun configurarSubmarino(): Submarino {
        println("1. INICIALIZACIÓN DE LA ESTACIÓN Y NAVE:")
        val sub = Submarino(
            posX = 50.0,
            posY = 100.0,
            velocidad = 10.0,
            casco = ModuloCasco(500_000.0),
            bodega = Bodega(capacidadMaxima = 50.0),
            billetera = Monedero(saldoInicial = 0)
        )
        println("   • Posición inicial submarino: (X: ${sub.posX}, Y: ${sub.posY}m) [Sumergido]")
        println("   • Saldo en billetera: $${sub.billetera.saldo}")
        println("   • ¿Está en superficie?: ${sub.estaEnSuperficie}")
        println("   • ¿Está acoplado?: ${sub.estaAcoplado}\n")
        return sub
    }

    private fun demostrarCargaEnProfundidad(sub: Submarino) {
        println("--------------------------------------------------------------------------------")
        println(" 2. RECOLECCIÓN Y CARGA DE MINERALES EN BODEGA")
        println("--------------------------------------------------------------------------------")
        val mineral1 = Mineral("MIN-01", TipoMineral.TITANIO_CRISTALINO, 50, 100)
        val mineral2 = Mineral("MIN-02", TipoMineral.NODULO_DE_MANGANESO, 50, 100)
        val mineral3 = Mineral("MIN-03", TipoMineral.COBRE_MARINO, 50, 100)

        sub.bodega?.agregar(mineral1)
        sub.bodega?.agregar(mineral2)
        sub.bodega?.agregar(mineral3)

        println("   • Minerales recolectados:")
        sub.bodega?.obtenerElementos()?.forEachIndexed { idx, it ->
            println("     [$idx] ${it.nombre} -> Peso: ${it.peso} kg | Valor: $${it.valor}")
        }
        println("   • Carga total bodega : ${sub.bodega?.pesoActual} kg")
        println("   • Valor potencial    : $${(sub.bodega as? Bodega)?.valorTotal}\n")
    }

    private fun demostrarFalloPorNoAcople(sub: Submarino, servicioComercio: ServicioComercio) {
        println("--------------------------------------------------------------------------------")
        println(" 3. INTENTO DE VENTA FALLIDO (NAVE NO ACOPLADA / SUMERGIDA)")
        println("--------------------------------------------------------------------------------")
        val resultadoFallo = servicioComercio.venderBodegaSubmarino(sub)
        println("   -> Resultado: ${resultadoFallo.obtenerResumen()}")
        println("   • Saldo en billetera : $${sub.billetera.saldo}")
        println("   • Carga en bodega    : ${sub.bodega?.pesoActual} kg (sin cambios)\n")
    }

    private fun demostrarAcopleYVenta(sub: Submarino, estacion: EstacionAcople, servicioComercio: ServicioComercio) {
        println("--------------------------------------------------------------------------------")
        println(" 4. ASCENSO A SUPERFICIE Y AMARRE EN ESTACIÓN")
        println("--------------------------------------------------------------------------------")
        sub.ascender(deltaY = 100.0)
        println("   • Nueva cota vertical: Y = ${sub.posY}m (Superficie alcanzada)")
        println("   • ¿Apto para acoplar en estación?: ${estacion.puedeAcoplar(sub.posX, sub.posY)}")

        val acopleExitoso = estacion.acoplar(sub, sub.posX, sub.posY)
        println("   • Maniobra de acople: ${if (acopleExitoso) "EXITOSA" else "FALLIDA"}")
        println("   • Estado acople nave: ${sub.estaAcoplado}")
        println("   • Estación ocupada  : ${estacion.hayNaveAcoplada}\n")

        println("--------------------------------------------------------------------------------")
        println(" 5. TRANSACCIÓN COMERCIAL EN SUPERFICIE")
        println("--------------------------------------------------------------------------------")
        val resultadoVenta = servicioComercio.venderBodegaSubmarino(sub)
        println("   -> ${resultadoVenta.obtenerResumen()}")
        println("   • Saldo final en billetera : $${sub.billetera.saldo}")
        println("   • Carga final en bodega    : ${sub.bodega?.pesoActual} kg (bodega vaciada)\n")
    }

    private fun demostrarDesacople(sub: Submarino, estacion: EstacionAcople) {
        println("--------------------------------------------------------------------------------")
        println(" 6. DESACOPLE Y REGRESO A NAVEGACIÓN")
        println("--------------------------------------------------------------------------------")
        estacion.desacoplar()
        println("   • Estado acople tras desamarre: ${sub.estaAcoplado}")
        println("   • Estación libre              : ${!estacion.hayNaveAcoplada}\n")
    }

    private fun imprimirCierre() {
        println("================================================================================")
        println("           SIMULACIÓN COMERCIAL FINALIZADA CON ÉXITO                            ")
        println("================================================================================")
    }
}
