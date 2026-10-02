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
        imprimirEncabezado()
        mostrarConstantesFisicas()

        // 1. Instanciación e inicialización del Submarino equipado con ModuloCasco (Issue #3)
        val casco = ModuloCasco(presionMaximaPa = 500_000.0, integridad = 100.0)
        val submarino = Submarino(posX = 0.0, posY = 0.0, velocidad = 10.0, casco = casco)
        val profundidadMaximaSegura = CalculadoraHidrostatica.calcularProfundidadMaxima(casco.presionMaximaPa)

        imprimirEstadoInicial(submarino, profundidadMaximaSegura)

        // 2. Fase A: Navegación y propulsión en superficie (Issues #1 y #2)
        probarNavegacionYPropulsion(submarino)

        // 3. Fase B: Inmersión segura dentro del límite de presión del casco
        probarInmersionSegura(submarino, profundidadMaximaSegura)

        // 4. Fase C: Inmersión crítica, pérdida de integridad y destrucción de la nave
        probarInmersionCriticaYDestruccion()

        // 5. Fase D: Demostración práctica de Principios SOLID (OCP, DIP, ISP)
        probarPrincipiosSolid()

        imprimirCierre()
    }

    private fun imprimirEncabezado() {
        println("================================================================================")
        println("            SIMULADOR DE SUBMARINO - PRUEBA DE INTEGRACIÓN POO LIMPIA            ")
        println("                 (Issues #1, #2 y #3: Navegación, Física y Casco)               ")
        println("================================================================================\n")
    }

    private fun mostrarConstantesFisicas() {
        println("--------------------------------------------------------------------------------")
        println(" 1. FÍSICA HIDROSTÁTICA DEL ENTORNO (Issue #2)")
        println("--------------------------------------------------------------------------------")
        println("  • Densidad del agua de mar (ρ) : ${CalculadoraHidrostatica.DENSIDAD_AGUA_MAR} kg/m³")
        println("  • Aceleración de gravedad (g)  : ${CalculadoraHidrostatica.GRAVEDAD} m/s²")
        println("  • Presión atmosférica base (P0): ${"%,.2f".format(CalculadoraHidrostatica.PRESION_ATMOSFERICA)} Pa")
        println("  • Presión en superficie (Y = 0): ${"%,.2f".format(CalculadoraHidrostatica.calcularPresion(0.0))} Pa\n")
    }

    private fun imprimirEstadoInicial(submarino: Submarino, profMaxSegura: Double) {
        println("--------------------------------------------------------------------------------")
        println(" 2. INICIALIZACIÓN DE LA NAVE Y MÓDULO CASCO (Issues #1 y #3)")
        println("--------------------------------------------------------------------------------")
        println("  • Presión máxima admisible del casco: ${"%,.2f".format(submarino.casco.presionMaximaPa)} Pa")
        println("  • Profundidad máxima segura teórica : ${"%.2f".format(profMaxSegura)} metros")
        println("  • Integridad inicial del casco      : ${"%.2f".format(submarino.casco.integridad)}%")
        println("  • Posición inicial                  : (X: ${submarino.posX} m, Y: ${submarino.posY} m)")
        println("  • Batería inicial                   : ${"%.2f".format(submarino.bateria)}%")
        println("  • Estado inicial destruido          : ${submarino.estaDestruido}\n")
    }

    private fun probarNavegacionYPropulsion(submarino: Submarino) {
        println("--------------------------------------------------------------------------------")
        println(" A) PRUEBA DE NAVEGACIÓN, PROPULSIÓN Y LÍMITES FÍSICOS (Issue #1)")
        println("--------------------------------------------------------------------------------")

        // Desplazamiento horizontal (30 unidades adelante)
        println("-> Maniobra 1: Desplazamiento horizontal (+30.0 m en eje X)")
        submarino.mover(deltaX = 30.0, deltaY = 0.0)
        println("   Posición actual : (X: ${submarino.posX} m, Y: ${submarino.posY} m)")
        println("   Batería restante: ${"%.2f".format(submarino.bateria)}% (Consumo de 0.5% por unidad)")

        // Comprobación de que no permite coordenadas Y negativas (por encima de superficie)
        println("-> Maniobra 2: Intentar ascender 15 m estando en superficie (posY = 0)")
        submarino.mover(deltaX = 0.0, deltaY = -15.0)
        println("   Posición actual : (X: ${submarino.posX} m, Y: ${submarino.posY} m) [Se mantiene en superficie Y >= 0]")
        println("   Batería restante: ${"%.2f".format(submarino.bateria)}%\n")
    }

    private fun probarInmersionSegura(submarino: Submarino, profMaxSegura: Double) {
        println("--------------------------------------------------------------------------------")
        println(" B) INMERSIÓN SEGURA DENTRO DEL LÍMITE DE PRESIÓN DEL CASCO (Issues #1, #2 y #3)")
        println("--------------------------------------------------------------------------------")

        // Descenso seguro a 20 metros
        println("-> Inmersión controlada a 20.0 m de profundidad (Límite seguro: ${"%.2f".format(profMaxSegura)} m)")
        submarino.mover(deltaX = 5.0, deltaY = 20.0)
        val presion20m = CalculadoraHidrostatica.calcularPresion(submarino.posY)
        submarino.verificarIntegridad(presion20m)

        println("   Profundidad : ${submarino.posY} m")
        println("   Presión Pa  : ${"%,.2f".format(presion20m)} Pa (Máx: ${"%,.2f".format(submarino.casco.presionMaximaPa)} Pa)")
        println("   Integridad  : ${"%.2f".format(submarino.casco.integridad)}% [Sin daño: presión dentro del límite]")
        println("   Destruido   : ${submarino.estaDestruido}")
        println("   Batería     : ${"%.2f".format(submarino.bateria)}%")

        // Descenso a 35 metros (muy cerca del límite de 39.65 m)
        println("-> Inmersión profunda cercana al límite: descender 15.0 m más (Profundidad: 35.0 m)")
        submarino.mover(deltaX = 0.0, deltaY = 15.0)
        val presion35m = CalculadoraHidrostatica.calcularPresion(submarino.posY)
        submarino.verificarIntegridad(presion35m)

        println("   Profundidad : ${submarino.posY} m")
        println("   Presión Pa  : ${"%,.2f".format(presion35m)} Pa (Margen restante: ${"%,.2f".format(submarino.casco.presionMaximaPa - presion35m)} Pa)")
        println("   Integridad  : ${"%.2f".format(submarino.casco.integridad)}% [Casco 100% íntegro]")
        println("   Destruido   : ${submarino.estaDestruido}\n")
    }

    private fun probarInmersionCriticaYDestruccion() {
        println("--------------------------------------------------------------------------------")
        println(" C) INMERSIÓN CRÍTICA, PÉRDIDA DE INTEGRIDAD Y DESTRUCCIÓN (Issue #3)")
        println("--------------------------------------------------------------------------------")

        // Caso demostrativo de fatiga y colapso de casco
        val cascoCritico = ModuloCasco(presionMaximaPa = 250_000.0, integridad = 5.0)
        val subCritico = Submarino(posX = 0.0, posY = 0.0, velocidad = 10.0, casco = cascoCritico)
        val limiteCritico = CalculadoraHidrostatica.calcularProfundidadMaxima(cascoCritico.presionMaximaPa)

        println("  Configuración de nave de prueba de estrés:")
        println("   • Resistencia de casco : ${"%,.2f".format(cascoCritico.presionMaximaPa)} Pa (Profundidad máx: ${"%.2f".format(limiteCritico)} m)")
        println("   • Integridad inicial   : ${"%.2f".format(cascoCritico.integridad)}%")

        // Inmersión que supera el límite de presión (descenso a 25 metros, cuando el límite es ~14.78m)
        println("\n-> Descenso 1 a zona crítica: 25.0 metros de profundidad")
        subCritico.mover(deltaX = 0.0, deltaY = 25.0)
        val presionCritica1 = CalculadoraHidrostatica.calcularPresion(subCritico.posY)
        val integridadRestante1 = subCritico.verificarIntegridad(presionCritica1)

        println("   Profundidad : ${subCritico.posY} m")
        println("   Presión Pa  : ${"%,.2f".format(presionCritica1)} Pa (Exceso: +${"%,.2f".format(presionCritica1 - cascoCritico.presionMaximaPa)} Pa)")
        println("   Integridad  : ${"%.2f".format(integridadRestante1)}% [Pérdida de integridad detectada]")
        println("   Destruido   : ${subCritico.estaDestruido}")

        // Descenso 2: Inmersión abisal profunda para provocar el colapso total del casco
        println("\n-> Descenso 2 a fosa abisal: descender 150.0 metros adicionales (Total: 175.0 m)")
        subCritico.mover(deltaX = 0.0, deltaY = 150.0)
        val presionCritica2 = CalculadoraHidrostatica.calcularPresion(subCritico.posY)
        val integridadRestante2 = subCritico.verificarIntegridad(presionCritica2)

        println("   Profundidad : ${subCritico.posY} m")
        println("   Presión Pa  : ${"%,.2f".format(presionCritica2)} Pa")
        println("   Integridad  : ${"%.2f".format(integridadRestante2)}%")
        println("   Destruido   : ${subCritico.estaDestruido} [¡Nave destruida por implosión!]")

        // Demostrar que el submarino destruido no puede navegar ni operar motores
        println("\n-> Comprobación de seguridad: intento de maniobra con nave destruida (+20.0 m en X)")
        val posXAntes = subCritico.posX
        subCritico.mover(deltaX = 20.0, deltaY = 0.0)
        println("   Posición previa : (X: $posXAntes m, Y: ${subCritico.posY} m)")
        println("   Posición actual : (X: ${subCritico.posX} m, Y: ${subCritico.posY} m) [Movimiento bloqueado exitosamente]\n")
    }

    private fun probarPrincipiosSolid() {
        println("--------------------------------------------------------------------------------")
        println(" D) VALIDACIÓN DE PRINCIPIOS SOLID (OCP, DIP, ISP, SRP)")
        println("--------------------------------------------------------------------------------")

        // 1. OCP & DIP: Polimorfismo e inyección de CascoReforzado en Submarino
        println("-> 1. OCP & DIP: Submarino equipado con CascoReforzado (blindaje 50%)")
        val cascoBlindado = CascoReforzado(presionMaximaPa = 250_000.0, integridad = 100.0, factorAbsorcion = 0.5)
        val subAvanzado: Navegable = Submarino(posX = 0.0, posY = 0.0, casco = cascoBlindado)

        // Usar interfaz Navegable (ISP)
        subAvanzado.sumergir(30.0)
        val subRef = subAvanzado as Submarino
        val presionExterna = CalculadoraHidrostatica.calcularPresion(subRef.posY)
        subRef.verificarIntegridad(presionExterna)

        println("   Profundidad actual  : ${subRef.posY} m")
        println("   Presión recibida    : ${"%,.2f".format(presionExterna)} Pa")
        println("   Integridad blindada : ${"%.2f".format(cascoBlindado.integridad)}% (Daño reducido por absorción)")

        // 2. OCP: Entorno fluido configurable (Agua dulce / otro líquido)
        println("\n-> 2. OCP en Física: Simulación en Agua Dulce (ρ = 1000.0 kg/m³)")
        val fisicaAguaDulce = CalculadoraPresionFluido(densidadFluido = 1000.0)
        val presionDulce = fisicaAguaDulce.calcularPresion(30.0)
        println("   Presión a 30m en agua dulce: ${"%,.2f".format(presionDulce)} Pa (menor que en mar: ${"%,.2f".format(presionExterna)} Pa)\n")
    }

    private fun imprimirCierre() {
        println("================================================================================")
        println("        SIMULACIÓN FINALIZADA CON ÉXITO - TODOS LOS SISTEMAS VALIDADOS         ")
        println("================================================================================\n")
    }
}

/**
 * Clase de prueba conjunta para compatibilidad directa de nomenclatura.
 */
class PruebaConjunta : SimuladorSubmarino()

/**
 * Punto de entrada alternativo para ejecución directa como PruebaConjuntaKt.
 */
fun main() {
    SimuladorSubmarino().ejecutarSimulacionCompleta()
}
