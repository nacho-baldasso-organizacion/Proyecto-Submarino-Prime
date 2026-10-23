package submarino

/**
 * Punto de entrada de prueba y demostración para la Issue #6:
 * "Cálculo de peso total y física de flotabilidad/ascenso del submarino".
 *
 * Encapsulado en [MainPruebaIssue6] cumpliendo con la regla de CERO funciones sueltas
 * y división en métodos con baja complejidad ciclomática.
 */
object MainPruebaIssue6 {

    @JvmStatic
    fun main(args: Array<String> = emptyArray()) {
        ejecutarDemostracion()
    }

    /**
     * Coordina el flujo completo de simulación física de ascenso.
     */
    fun ejecutarDemostracion() {
        imprimirEncabezado()

        val pesoBase = 1000.0
        val capacidadBodega = 20.0
        val velocidadNominal = 10.0
        val bodega = Bodega(capacidadMaxima = capacidadBodega)
        val casco = ModuloCasco(presionMaximaPa = 500_000.0)

        val submarino = Submarino(
            posX = 0.0,
            posY = 100.0,
            velocidad = velocidadNominal,
            casco = casco,
            pesoBase = pesoBase,
            bodega = bodega,
            estrategiaAscenso = EstrategiaAscensoLineal(factorReduccionMaximo = 0.5)
        )

        demostrarEspecificaciones(submarino, bodega)
        demostrarEstadoVacio(submarino)
        demostrarMediaCarga(submarino, bodega)
        demostrarCargaCompleta(submarino, bodega)
        demostrarComparativaEstrategias(bodega, casco)

        imprimirCierre()
    }

    private fun imprimirEncabezado() {
        println("================================================================================")
        println("       SUBMARINO PRIME - PRUEBA DE ISSUE #6: PESO TOTAL Y FÍSICA DE ASCENSO     ")
        println("================================================================================\n")
    }

    private fun demostrarEspecificaciones(submarino: Submarino, bodega: Bodega) {
        println("1. ESPECIFICACIONES INICIALES DE LA NAVE:")
        println("   • Peso base estructural constante : ${submarino.pesoBase} kg")
        println("   • Capacidad máxima de la bodega   : ${bodega.capacidadMaxima} kg")
        println("   • Velocidad nominal de motores    : ${submarino.velocidadAscensoBase} m/s")
        println("   • Profundidad inicial             : ${submarino.posY} m\n")
    }

    private fun demostrarEstadoVacio(submarino: Submarino) {
        println("--------------------------------------------------------------------------------")
        println(" A) ESTADO CON BODEGA VACÍA (0% CARGA)")
        println("--------------------------------------------------------------------------------")
        val estado = submarino.obtenerEstadoAscenso()
        println("   • Carga en bodega       : ${estado.pesoCarga} kg")
        println("   • Peso total de nave    : ${estado.pesoTotal} kg")
        println("   • Factor de ascenso     : ${"%.2f".format(estado.factorVelocidad * 100)}%")
        println("   • Velocidad efectiva    : ${estado.velocidadAscensoEfectiva} m/s (100% nominal)")

        val yAntes = submarino.posY
        submarino.ascender(deltaY = 10.0)
        println("   -> Maniobra: Ascender 10.0m -> Profundidad pasa de ${yAntes}m a ${submarino.posY}m (Δ = -10.0m)\n")
    }

    private fun demostrarMediaCarga(submarino: Submarino, bodega: Bodega) {
        println("--------------------------------------------------------------------------------")
        println(" B) ESTADO A MEDIA CARGA (50% CARGA - 10 kg)")
        println("--------------------------------------------------------------------------------")
        bodega.agregar(Mineral("M-MED-1", TipoMineral.TITANIO_CRISTALINO, 0, 0)) // 5.0 kg
        bodega.agregar(Mineral("M-MED-2", TipoMineral.TITANIO_CRISTALINO, 0, 1)) // 5.0 kg

        val estado = submarino.obtenerEstadoAscenso()
        println("   • Carga en bodega       : ${estado.pesoCarga} kg / ${estado.capacidadMaximaBodega} kg")
        println("   • Peso total de nave    : ${estado.pesoTotal} kg")
        println("   • Factor de ascenso     : ${"%.2f".format(estado.factorVelocidad * 100)}%")
        println("   • Velocidad efectiva    : ${estado.velocidadAscensoEfectiva} m/s (75% nominal)")

        val yAntes = submarino.posY
        submarino.ascender(deltaY = 10.0)
        println("   -> Maniobra: Ascenso con carga media -> Desplazamiento efectivo = ${"%.2f".format(yAntes - submarino.posY)}m (desacelerado por masa)\n")
    }

    private fun demostrarCargaCompleta(submarino: Submarino, bodega: Bodega) {
        println("--------------------------------------------------------------------------------")
        println(" C) ESTADO A CARGA COMPLETA (100% CARGA - 20 kg)")
        println("--------------------------------------------------------------------------------")
        bodega.agregar(Mineral("M-FULL-1", TipoMineral.TITANIO_CRISTALINO, 0, 2)) // 5.0 kg
        bodega.agregar(Mineral("M-FULL-2", TipoMineral.TITANIO_CRISTALINO, 0, 3)) // 5.0 kg

        val estado = submarino.obtenerEstadoAscenso()
        println("   • Carga en bodega       : ${estado.pesoCarga} kg / ${estado.capacidadMaximaBodega} kg [BODEGA LLENA]")
        println("   • Peso total de nave    : ${estado.pesoTotal} kg")
        println("   • Factor de ascenso     : ${"%.2f".format(estado.factorVelocidad * 100)}%")
        println("   • Velocidad efectiva    : ${estado.velocidadAscensoEfectiva} m/s (50% nominal)")

        val yAntes = submarino.posY
        submarino.ascender(deltaY = 10.0)
        println("   -> Maniobra: Ascenso a carga máxima -> Desplazamiento efectivo = ${"%.2f".format(yAntes - submarino.posY)}m (máxima ralentización)\n")
    }

    private fun demostrarComparativaEstrategias(bodega: Bodega, casco: ModuloCasco) {
        println("--------------------------------------------------------------------------------")
        println(" D) PRINCIPIO ABIERTO/CERRADO (OCP) - COMPARATIVA POLIMÓRFICA DE ESTRATEGIAS")
        println("--------------------------------------------------------------------------------")
        val subLineal = Submarino(pesoBase = 1000.0, bodega = bodega, velocidad = 10.0, casco = casco, estrategiaAscenso = EstrategiaAscensoLineal(0.5))
        val subMasa = Submarino(pesoBase = 1000.0, bodega = bodega, velocidad = 10.0, casco = casco, estrategiaAscenso = EstrategiaAscensoPorMasa())
        val subArquimedes = Submarino(pesoBase = 1000.0, bodega = bodega, velocidad = 10.0, casco = casco, estrategiaAscenso = EstrategiaFisicaFlotabilidad(factorEmpujeMinimo = 0.3))

        println("   1. Estrategia Lineal Estándar           -> Velocidad: ${"%.2f".format(subLineal.velocidadAscenso)} m/s")
        println("   2. Estrategia Inercial por Ratio Masa   -> Velocidad: ${"%.2f".format(subMasa.velocidadAscenso)} m/s")
        println("   3. Estrategia de Flotabilidad de Arquímedes -> Velocidad: ${"%.2f".format(subArquimedes.velocidadAscenso)} m/s\n")
    }

    private fun imprimirCierre() {
        println("================================================================================")
        println("                      PRUEBA FINALIZADA CON ÉXITO                               ")
        println("================================================================================")
    }
}
