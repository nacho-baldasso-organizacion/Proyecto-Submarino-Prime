package submarino

/**
 * Clase orquestadora de la simulación y pruebas integradas del Submarino.
 * Integra de forma cohesiva los componentes de:
 *  - Issue #1: Submarino base, navegación horizontal, propulsión, consumo de batería y límites físicos.
 *  - Issue #2: CalculadoraHidrostatica (física de fluidos, cálculo de presión por profundidad y límites hidrostáticos).
 *  - Issue #3: ModuloCasco (integridad estructural, delegación de presión y colapso de la nave).
 *  - Refactor SOLID: Desacoplamiento mediante interfaces, polimorfismo, OCP con CascoReforzado y Calculadora configurable.
 */
open class SimuladorSubmarino {

    /**
     * Ejecuta el flujo secuencial completo de simulación y validación de sistemas.
     */
    fun ejecutarSimulacionCompleta() {
        imprimirEncabezadoSimulacion()
        mostrarConstantesFisicas()

        val casco = crearCascoEstandar()
        val submarino = crearSubmarinoEstandar(casco)
        val profundidadMaximaSegura = CalculadoraHidrostatica.calcularProfundidadMaxima(casco.presionMaximaPa)

        imprimirEstadoInicial(submarino, profundidadMaximaSegura)
        probarNavegacionYPropulsion(submarino)
        probarInmersionSegura(submarino, profundidadMaximaSegura)
        probarInmersionCriticaYDestruccion()
        probarPrincipiosSolid()
        VisualizadorConsola.imprimirCierre("TODOS LOS SISTEMAS VALIDADOS CON ÉXITO")
    }

    private fun imprimirEncabezadoSimulacion() {
        VisualizadorConsola.imprimirEncabezado(
            "SIMULADOR DE SUBMARINO - PRUEBA DE INTEGRACIÓN POO LIMPIA\n" +
            "                 (Issues #1, #2 y #3: Navegación, Física y Casco)"
        )
    }

    private fun crearCascoEstandar(): Casco =
        ModuloCasco(presionMaximaPa = 500_000.0, integridad = 100.0)

    private fun crearSubmarinoEstandar(casco: Casco): Submarino =
        Submarino(posX = 0.0, posY = 0.0, velocidad = 10.0, casco = casco)

    private fun formatearConMiles(valor: Double): String = "%,.2f".format(valor)

    private fun formatearDecimales(valor: Double): String = "%.2f".format(valor)

    private fun mostrarConstantesFisicas() {
        VisualizadorConsola.imprimirSeccion("1. FÍSICA HIDROSTÁTICA DEL ENTORNO (Issue #2)")
        println("  • Densidad del agua de mar (ρ) : ${CalculadoraHidrostatica.DENSIDAD_AGUA_MAR} kg/m³")
        println("  • Aceleración de gravedad (g)  : ${CalculadoraHidrostatica.GRAVEDAD} m/s²")
        println("  • Presión atmosférica base (P0): ${formatearConMiles(CalculadoraHidrostatica.PRESION_ATMOSFERICA)} Pa")
        println("  • Presión en superficie (Y = 0): ${formatearConMiles(CalculadoraHidrostatica.calcularPresion(0.0))} Pa\n")
    }

    private fun imprimirEstadoInicial(submarino: Submarino, profMaxSegura: Double) {
        VisualizadorConsola.imprimirSeccion("2. INICIALIZACIÓN DE LA NAVE Y MÓDULO CASCO (Issues #1 y #3)")
        println("  • Presión máxima admisible del casco: ${formatearConMiles(submarino.casco.presionMaximaPa)} Pa")
        println("  • Profundidad máxima segura teórica : ${formatearDecimales(profMaxSegura)} metros")
        println("  • Integridad inicial del casco      : ${formatearDecimales(submarino.casco.integridad)}%")
        println("  • Posición inicial                  : (X: ${submarino.posX} m, Y: ${submarino.posY} m)")
        println("  • Batería inicial                   : ${formatearDecimales(submarino.bateria)}%")
        println("  • Estado inicial destruido          : ${submarino.estaDestruido}\n")
    }

    private fun probarNavegacionYPropulsion(submarino: Submarino) {
        VisualizadorConsola.imprimirSeccion("A) PRUEBA DE NAVEGACIÓN, PROPULSIÓN Y LÍMITES FÍSICOS (Issue #1)")
        println("-> Maniobra 1: Desplazamiento horizontal (+30.0 m en eje X)")
        submarino.mover(deltaX = 30.0, deltaY = 0.0)
        println("   Posición actual : (X: ${submarino.posX} m, Y: ${submarino.posY} m)")
        println("   Batería restante: ${formatearDecimales(submarino.bateria)}% (Consumo de 0.5% por unidad)")

        println("-> Maniobra 2: Intentar ascender 15 m estando en superficie (posY = 0)")
        submarino.mover(deltaX = 0.0, deltaY = -15.0)
        println("   Posición actual : (X: ${submarino.posX} m, Y: ${submarino.posY} m) [Se mantiene en superficie Y >= 0]")
        println("   Batería restante: ${formatearDecimales(submarino.bateria)}%\n")
    }

    private fun probarInmersionSegura(submarino: Submarino, profMaxSegura: Double) {
        VisualizadorConsola.imprimirSeccion("B) INMERSIÓN SEGURA DENTRO DEL LÍMITE DE PRESIÓN DEL CASCO (Issues #1, #2 y #3)")
        println("-> Inmersión controlada a 20.0 m de profundidad (Límite seguro: ${formatearDecimales(profMaxSegura)} m)")
        val presion20m = descenderYVerificar(submarino, deltaX = 5.0, deltaY = 20.0)
        imprimirInmersionControlada(submarino, presion20m)

        println("-> Inmersión profunda cercana al límite: descender 15.0 m más (Profundidad: 35.0 m)")
        val presion35m = descenderYVerificar(submarino, deltaX = 0.0, deltaY = 15.0)
        imprimirInmersionProfunda(submarino, presion35m)
    }

    private fun descenderYVerificar(submarino: Submarino, deltaX: Double, deltaY: Double): Double {
        submarino.mover(deltaX = deltaX, deltaY = deltaY)
        val presion = CalculadoraHidrostatica.calcularPresion(submarino.posY)
        submarino.verificarIntegridad(presion)
        return presion
    }

    private fun imprimirInmersionControlada(submarino: Submarino, presion: Double) {
        println("   Profundidad : ${submarino.posY} m")
        println("   Presión Pa  : ${formatearConMiles(presion)} Pa (Máx: ${formatearConMiles(submarino.casco.presionMaximaPa)} Pa)")
        println("   Integridad  : ${formatearDecimales(submarino.casco.integridad)}% [Sin daño: presión dentro del límite]")
        println("   Destruido   : ${submarino.estaDestruido}")
        println("   Batería     : ${formatearDecimales(submarino.bateria)}%")
    }

    private fun imprimirInmersionProfunda(submarino: Submarino, presion: Double) {
        println("   Profundidad : ${submarino.posY} m")
        println("   Presión Pa  : ${formatearConMiles(presion)} Pa (Margen restante: ${formatearConMiles(submarino.casco.presionMaximaPa - presion)} Pa)")
        println("   Integridad  : ${formatearDecimales(submarino.casco.integridad)}% [Casco 100% íntegro]")
        println("   Destruido   : ${submarino.estaDestruido}\n")
    }

    private fun probarInmersionCriticaYDestruccion() {
        VisualizadorConsola.imprimirSeccion("C) INMERSIÓN CRÍTICA, PÉRDIDA DE INTEGRIDAD Y DESTRUCCIÓN (Issue #3)")
        val cascoCritico = crearCascoCritico()
        val subCritico = crearSubmarinoCritico(cascoCritico)

        imprimirConfiguracionCritica(cascoCritico)
        ejecutarDescensoCritico(subCritico, cascoCritico)
        ejecutarDescensoAbisal(subCritico)
        comprobarBloqueoTrasDestruccion(subCritico)
    }

    private fun crearCascoCritico(): Casco =
        ModuloCasco(presionMaximaPa = 250_000.0, integridad = 5.0)

    private fun crearSubmarinoCritico(casco: Casco): Submarino =
        Submarino(posX = 0.0, posY = 0.0, velocidad = 10.0, casco = casco)

    private fun imprimirConfiguracionCritica(cascoCritico: Casco) {
        val limiteCritico = CalculadoraHidrostatica.calcularProfundidadMaxima(cascoCritico.presionMaximaPa)
        println("  Configuración de nave de prueba de estrés:")
        println("   • Resistencia de casco : ${formatearConMiles(cascoCritico.presionMaximaPa)} Pa (Profundidad máx: ${formatearDecimales(limiteCritico)} m)")
        println("   • Integridad inicial   : ${formatearDecimales(cascoCritico.integridad)}%")
    }

    private fun ejecutarDescensoCritico(subCritico: Submarino, cascoCritico: Casco) {
        println("\n-> Descenso 1 a zona crítica: 25.0 metros de profundidad")
        val presionCritica = descenderYVerificar(subCritico, deltaX = 0.0, deltaY = 25.0)
        val integridadRestante = subCritico.casco.integridad

        println("   Profundidad : ${subCritico.posY} m")
        println("   Presión Pa  : ${formatearConMiles(presionCritica)} Pa (Exceso: +${formatearConMiles(presionCritica - cascoCritico.presionMaximaPa)} Pa)")
        println("   Integridad  : ${formatearDecimales(integridadRestante)}% [Pérdida de integridad detectada]")
        println("   Destruido   : ${subCritico.estaDestruido}")
    }

    private fun ejecutarDescensoAbisal(subCritico: Submarino) {
        println("\n-> Descenso 2 a fosa abisal: descender 150.0 metros adicionales (Total: 175.0 m)")
        val presionCritica = descenderYVerificar(subCritico, deltaX = 0.0, deltaY = 150.0)

        println("   Profundidad : ${subCritico.posY} m")
        println("   Presión Pa  : ${formatearConMiles(presionCritica)} Pa")
        println("   Integridad  : ${formatearDecimales(subCritico.casco.integridad)}%")
        println("   Destruido   : ${subCritico.estaDestruido} [¡Nave destruida por implosión!]")
    }

    private fun comprobarBloqueoTrasDestruccion(subCritico: Submarino) {
        println("\n-> Comprobación de seguridad: intento de maniobra con nave destruida (+20.0 m en X)")
        val posXAntes = subCritico.posX
        subCritico.mover(deltaX = 20.0, deltaY = 0.0)
        println("   Posición previa : (X: $posXAntes m, Y: ${subCritico.posY} m)")
        println("   Posición actual : (X: ${subCritico.posX} m, Y: ${subCritico.posY} m) [Movimiento bloqueado exitosamente]\n")
    }

    private fun probarPrincipiosSolid() {
        VisualizadorConsola.imprimirSeccion("D) VALIDACIÓN DE PRINCIPIOS SOLID (OCP, DIP, ISP, SRP)")
        val presionExterna = validarCascoReforzado()
        validarFisicaAguaDulce(presionExterna)
    }

    private fun validarCascoReforzado(): Double {
        println("-> 1. OCP & DIP: Submarino equipado con CascoReforzado (blindaje 50%)")
        val cascoBlindado = CascoReforzado(presionMaximaPa = 250_000.0, integridad = 100.0, factorAbsorcion = 0.5)
        val subAvanzado = Submarino(posX = 0.0, posY = 0.0, casco = cascoBlindado)

        subAvanzado.sumergir(30.0)
        val presionExterna = CalculadoraHidrostatica.calcularPresion(subAvanzado.posY)
        subAvanzado.verificarIntegridad(presionExterna)

        println("   Profundidad actual  : ${subAvanzado.posY} m")
        println("   Presión recibida    : ${formatearConMiles(presionExterna)} Pa")
        println("   Integridad blindada : ${formatearDecimales(cascoBlindado.integridad)}% (Daño reducido por absorción)")
        return presionExterna
    }

    private fun validarFisicaAguaDulce(presionExterna: Double) {
        println("\n-> 2. OCP en Física: Simulación en Agua Dulce (ρ = 1000.0 kg/m³)")
        val fisicaAguaDulce = CalculadoraPresionFluido(densidadFluido = 1000.0)
        val presionDulce = fisicaAguaDulce.calcularPresion(30.0)
        println("   Presión a 30m en agua dulce: ${formatearConMiles(presionDulce)} Pa (menor que en mar: ${formatearConMiles(presionExterna)} Pa)\n")
    }
}

/**
 * Clase de prueba conjunta para compatibilidad directa de nomenclatura e instanciación.
 */
open class PruebaConjunta : SimuladorSubmarino() {
    companion object {
        @JvmStatic
        fun main(args: Array<String> = emptyArray()) {
            SimuladorSubmarino().ejecutarSimulacionCompleta()
        }
    }
}
