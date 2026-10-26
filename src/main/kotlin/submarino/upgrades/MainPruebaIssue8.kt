package submarino

/**
 * ============================================================================
 * DEMOSTRACIÓN INTEGRAL DE LA ISSUE #8:
 * "Sistema de Upgrades / Mejoras para el submarino"
 *
 * Encapsulado en [MainPruebaIssue8] respetando la regla de CERO funciones sueltas
 * y división en métodos modulares de baja complejidad ciclomática.
 * ============================================================================
 */
object MainPruebaIssue8 {

    @JvmStatic
    fun main(args: Array<String> = emptyArray()) {
        ejecutarDemostracion()
    }

    /**
     * Coordina el flujo completo de simulación de upgrades, economía y verificación de estadísticas.
     */
    fun ejecutarDemostracion() {
        VisualizadorConsola.imprimirEncabezado("SUBMARINO PRIME - PRUEBA DE ISSUE #8: SISTEMA DE UPGRADES / MEJORAS")

        val catalogo = FabricaMejoras.crearCatalogoEstandar()
        val servicioUpgrades: ServicioUpgradesContrato = ServicioUpgrades(catalogo)
        servicioUpgrades.registrarObservador(ObservadorTiendaConsola())

        val tienda = TiendaMejoras(catalogo, servicioUpgrades)
        val servicioComercio: ServicioComercioContrato = ServicioComercio(CalculadoraPreciosEstandar())

        val submarino = configurarSubmarino()
        demostrarCatalogo(catalogo)
        demostrarRechazoFondos(tienda, submarino)
        demostrarVentaBodega(servicioComercio, submarino)
        demostrarCompraExitosa(tienda, submarino)
        demostrarProgresionYCap(tienda, submarino)
        VisualizadorConsola.imprimirCierre("DEMOSTRACIÓN DE UPGRADES FINALIZADA CON ÉXITO")
    }

    private fun configurarSubmarino(): Submarino {
        println("1. INICIALIZACIÓN DE LA NAVE Y ESTADO BASE:")
        val sub = Submarino(
            posX = 0.0,
            posY = 0.0,
            velocidad = 5.0,
            casco = ModuloCasco(presionMaximaPa = 250_000.0),
            pesoBase = 1000.0,
            bodega = Bodega(capacidadMaxima = 10.0),
            velocidadAscensoBase = 4.0,
            billetera = Monedero(saldoInicial = 0)
        )
        println("   • Saldo inicial     : $${sub.billetera.saldo}")
        println("   • Capacidad bodega  : ${sub.bodega?.capacidadMaxima} kg")
        println("   • Velocidad ascenso : ${sub.velocidadAscensoBase} m/s")
        println("   • Resistencia casco : ${sub.casco.presionMaximaPa} Pa")
        println("   • Propulsión motor  : ${sub.velocidad} m/s\n")
        return sub
    }

    private fun demostrarCatalogo(catalogo: CatalogoUpgrades) {
        VisualizadorConsola.imprimirSeccion("2. CATÁLOGO DISPONIBLE EN LA TIENDA DE MEJORAS")
        catalogo.obtenerTodas().forEachIndexed { idx, it ->
            println("   [$idx] ID: ${it.id} | ${it.nombre} | Nivel: ${it.nivel}/${it.nivelMaximo} | Costo: $$${it.costo}")
            println("        -> Efecto: ${it.descripcion}")
        }
        println()
    }

    private fun demostrarRechazoFondos(tienda: TiendaMejoras, submarino: Submarino) {
        VisualizadorConsola.imprimirSeccion("3. INTENTO DE COMPRA SIN FONDOS (RECHAZO CONTROLADO)")
        val resultadoSinFondos = tienda.comprarParaSubmarino(FabricaMejoras.ID_BODEGA, submarino)
        println("   -> ${resultadoSinFondos.obtenerMensaje()}")
        println("   • Saldo en billetera: $${submarino.billetera.saldo} (sin cambios)\n")
    }

    private fun demostrarVentaBodega(servicioComercio: ServicioComercioContrato, submarino: Submarino) {
        VisualizadorConsola.imprimirSeccion("4. CARGA DE MINERALES, ACOPLE Y VENTA PARA OBTENER FONDOS")
        submarino.bodega?.agregar(Mineral("M-1", TipoMineral.TITANIO_CRISTALINO, 0, 0))
        submarino.bodega?.agregar(Mineral("M-2", TipoMineral.TITANIO_CRISTALINO, 0, 0))
        submarino.acoplar()

        val resultadoVenta = servicioComercio.venderBodegaSubmarino(submarino)
        println("   -> ${resultadoVenta.obtenerResumen()}")
        println("   • Saldo disponible tras venta: $${submarino.billetera.saldo}\n")
    }

    private fun demostrarCompraExitosa(tienda: TiendaMejoras, submarino: Submarino) {
        VisualizadorConsola.imprimirSeccion("5. ADQUISICIÓN DE MEJORAS Y AUMENTO DINÁMICO DE ESTADÍSTICAS")
        val resBodega = tienda.comprarParaSubmarino(FabricaMejoras.ID_BODEGA, submarino)
        println("   A) Compra Bodega: ${resBodega.obtenerMensaje()}")
        println("      -> Nueva capacidad de bodega: ${submarino.bodega?.capacidadMaxima} kg (antes 10.0 kg)")

        val resAscenso = tienda.comprarParaSubmarino(FabricaMejoras.ID_ASCENSO, submarino)
        println("\n   B) Compra Ascenso: ${resAscenso.obtenerMensaje()}")
        println("      -> Nueva velocidad ascenso: ${submarino.velocidadAscensoBase} m/s (antes 4.0 m/s)")

        val resCasco = tienda.comprarParaSubmarino(FabricaMejoras.ID_CASCO, submarino)
        println("\n   C) Compra Blindaje: ${resCasco.obtenerMensaje()}")
        println("      -> Nueva resistencia casco: ${submarino.casco.presionMaximaPa} Pa (antes 250,000 Pa)")

        val resMotor = tienda.comprarParaSubmarino(FabricaMejoras.ID_PROPULSION, submarino)
        println("\n   D) Compra Propulsión: ${resMotor.obtenerMensaje()}")
        println("      -> Nueva velocidad motor: ${submarino.velocidad} m/s (antes 5.0 m/s)")
        println("   • Saldo restante: $${submarino.billetera.saldo}\n")
    }

    private fun demostrarProgresionYCap(tienda: TiendaMejoras, submarino: Submarino) {
        VisualizadorConsola.imprimirSeccion("6. PROGRESIÓN HASTA NIVEL MÁXIMO (CAP)")
        submarino.billetera.acreditar(5000)

        for (i in 2..5) {
            val resProg = tienda.comprarParaSubmarino(FabricaMejoras.ID_BODEGA, submarino)
            println("   -> Ascenso a nivel $i: ${resProg.obtenerMensaje()}")
        }

        println("\n-> Intento de compra sobre mejora en nivel máximo:")
        val resCap = tienda.comprarParaSubmarino(FabricaMejoras.ID_BODEGA, submarino)
        println("   -> ${resCap.obtenerMensaje()}")
        println("   • Capacidad final de bodega: ${submarino.bodega?.capacidadMaxima} kg\n")
    }
}
