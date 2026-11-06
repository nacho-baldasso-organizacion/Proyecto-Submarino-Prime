package submarino

/**
 * Clase orquestadora de la simulacion integrada del Submarino.
 *
 * Responsabilidad unica (SRP): coordinar el orden de los escenarios y delegar
 * la creacion de naves y la presentacion en consola en colaboradores
 * especializados ([FabricaNavesSimulacion] y los reportes de simulacion).
 *
 * Integra de forma cohesiva los componentes de:
 *  - Issue #1: Submarino base, navegacion horizontal, propulsion, consumo de bateria y limites fisicos.
 *  - Issue #2: CalculadoraHidrostatica (fisica de fluidos, calculo de presion por profundidad y limites hidrostaticos).
 *  - Issue #3: ModuloCasco (integridad estructural, delegacion de presion y colapso de la nave).
 *  - Refactor SOLID: Desacoplamiento mediante interfaces, polimorfismo, OCP con CascoReforzado y Calculadora configurable.
 */
open class SimuladorSubmarino(
    private val reporte: ReporteSimulacionSubmarino =
        FabricaDominio.crearReporteSimulacionSubmarino(),
    private val fabrica: FabricaNavesSimulacion =
        FabricaDominio.crearFabricaNavesSimulacion()
) {

    private val escenarioNavegacion = FabricaDominio.crearEscenarioNavegacionSubmarino()
    private val escenarioInmersion = FabricaDominio.crearEscenarioInmersionSubmarino()
    private val escenarioSolid = FabricaDominio.crearEscenarioSolidSubmarino()

    /**
     * Ejecuta el flujo secuencial completo de simulacion y validacion de sistemas.
     */
    fun ejecutarSimulacionCompleta() {
        reporte.encabezado()
        reporte.constantesFisicas()

        val casco = fabrica.cascoEstandar()
        val submarino = fabrica.submarinoEstandar(casco)
        val profundidadMaximaSegura =
            CalculadoraHidrostatica.calcularProfundidadMaxima(casco.presionMaximaPa)

        reporte.estadoInicial(submarino, profundidadMaximaSegura)
        escenarioNavegacion.ejecutar(submarino)
        escenarioInmersion.ejecutarInmersionSegura(submarino, profundidadMaximaSegura)
        escenarioInmersion.ejecutarInmersionCritica(fabrica)
        escenarioSolid.ejecutar()
        reporte.cierre()
    }
}

/**
 * Clase de prueba conjunta para compatibilidad directa de nomenclatura e instanciacion.
 */
open class PruebaConjunta : SimuladorSubmarino() {
    companion object {
        @JvmStatic
        fun main(args: Array<String> = emptyArray()) {
            FabricaDominio.crearSimuladorSubmarino().ejecutarSimulacionCompleta()
        }
    }
}
