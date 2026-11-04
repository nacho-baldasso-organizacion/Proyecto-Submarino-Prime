package submarino

/**
 * Fabrica de naves y cascos de prueba utilizados por los escenarios de simulacion (SRP).
 */
class FabricaNavesSimulacion {

    fun cascoEstandar(): Casco = ModuloCasco(presionMaximaPa = 500_000.0, integridad = 100.0)

    fun submarinoEstandar(casco: Casco): Submarino =
        Submarino(posX = 0.0, posY = 0.0, velocidad = 10.0, casco = casco)

    fun cascoCritico(): Casco = ModuloCasco(presionMaximaPa = 250_000.0, integridad = 5.0)

    fun submarinoCritico(casco: Casco): Submarino =
        Submarino(posX = 0.0, posY = 0.0, velocidad = 10.0, casco = casco)
}

/**
 * Escenario de navegacion horizontal, propulsion y limites fisicos (SRP).
 */
class EscenarioNavegacionSubmarino(
    private val reporte: ReporteNavegacionSimulacion = ReporteNavegacionSimulacion()
) {

    fun ejecutar(submarino: Submarino) {
        reporte.seccion()
        submarino.mover(deltaX = 30.0, deltaY = 0.0)
        reporte.maniobraHorizontal(submarino)
        submarino.mover(deltaX = 0.0, deltaY = -15.0)
        reporte.intentoAscensoEnSuperficie(submarino)
    }
}

/**
 * Escenarios de inmersion segura y critica con verificacion de integridad (SRP).
 */
class EscenarioInmersionSubmarino(
    private val reporte: ReporteInmersionSimulacion = ReporteInmersionSimulacion()
) {

    fun ejecutarInmersionSegura(submarino: Submarino, profMaxSegura: Double) {
        reporte.seccionSegura(profMaxSegura)
        val presion20m = descenderYVerificar(submarino, deltaX = 5.0, deltaY = 20.0)
        reporte.inmersionControlada(submarino, presion20m)
        val presion35m = descenderYVerificar(submarino, deltaX = 0.0, deltaY = 15.0)
        reporte.inmersionProfunda(submarino, presion35m)
    }

    fun ejecutarInmersionCritica(fabrica: FabricaNavesSimulacion) {
        reporte.seccionCritica()
        val cascoCritico = fabrica.cascoCritico()
        val subCritico = fabrica.submarinoCritico(cascoCritico)

        reporte.configuracionCritica(cascoCritico)
        val presionCritica = descenderYVerificar(subCritico, deltaX = 0.0, deltaY = 25.0)
        reporte.descensoCritico(subCritico, presionCritica, cascoCritico)

        val presionAbisal = descenderYVerificar(subCritico, deltaX = 0.0, deltaY = 150.0)
        reporte.descensoAbisal(subCritico, presionAbisal)

        val posXAntes = subCritico.posX
        subCritico.mover(deltaX = 20.0, deltaY = 0.0)
        reporte.bloqueoTrasDestruccion(subCritico, posXAntes)
    }

    private fun descenderYVerificar(submarino: Submarino, deltaX: Double, deltaY: Double): Double {
        submarino.mover(deltaX = deltaX, deltaY = deltaY)
        val presion = CalculadoraHidrostatica.calcularPresion(submarino.posY)
        submarino.verificarIntegridad(presion)
        return presion
    }
}

/**
 * Escenario de validacion de los principios SOLID (OCP, DIP, ISP, SRP) (SRP).
 */
class EscenarioSolidSubmarino(
    private val reporte: ReporteSolidSimulacion = ReporteSolidSimulacion()
) {

    fun ejecutar() {
        reporte.seccion()
        val presionExterna = validarCascoReforzado()
        validarFisicaAguaDulce(presionExterna)
    }

    private fun validarCascoReforzado(): Double {
        val cascoBlindado = CascoReforzado(
            presionMaximaPa = 250_000.0,
            integridad = 100.0,
            factorAbsorcion = 0.5
        )
        val subAvanzado = Submarino(posX = 0.0, posY = 0.0, casco = cascoBlindado)

        subAvanzado.sumergir(30.0)
        val presionExterna = CalculadoraHidrostatica.calcularPresion(subAvanzado.posY)
        subAvanzado.verificarIntegridad(presionExterna)

        reporte.cascoReforzado(subAvanzado, presionExterna, cascoBlindado)
        return presionExterna
    }

    private fun validarFisicaAguaDulce(presionExterna: Double) {
        val fisicaAguaDulce = CalculadoraPresionFluido(densidadFluido = 1000.0)
        val presionDulce = fisicaAguaDulce.calcularPresion(30.0)
        reporte.fisicaAguaDulce(presionDulce, presionExterna)
    }
}
