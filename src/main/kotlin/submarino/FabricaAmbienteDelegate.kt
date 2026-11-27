package submarino

object FabricaAmbienteDelegate {

    fun crearCalculadoraPresionFluido(
        densidadFluido: Double = 1025.0,
        gravedad: Double = 9.81,
        presionAtmosferica: Double = 101325.0
    ): CalculadoraPresionFluido = CalculadoraPresionFluido(
        densidadFluido = densidadFluido,
        gravedad = gravedad,
        presionAtmosferica = presionAtmosferica
    )

    fun crearReporteNavegacionSimulacion(): ReporteNavegacionSimulacion =
        FabricaReportesDelegate.crearReporteNavegacionSimulacion()

    fun crearReporteInmersionSimulacion(): ReporteInmersionSimulacion =
        FabricaReportesDelegate.crearReporteInmersionSimulacion()

    fun crearReporteSolidSimulacion(): ReporteSolidSimulacion =
        FabricaReportesDelegate.crearReporteSolidSimulacion()

    fun crearReporteSimulacionSubmarino(): ReporteSimulacionSubmarino =
        FabricaReportesDelegate.crearReporteSimulacionSubmarino()

    fun crearEscenarioNavegacionSubmarino(
        reporte: ReporteNavegacionSimulacion = FabricaReportesDelegate.crearReporteNavegacionSimulacion()
    ): EscenarioNavegacionSubmarino = EscenarioNavegacionSubmarino(reporte = reporte)

    fun crearEscenarioInmersionSubmarino(
        reporte: ReporteInmersionSimulacion = FabricaReportesDelegate.crearReporteInmersionSimulacion()
    ): EscenarioInmersionSubmarino = EscenarioInmersionSubmarino(reporte = reporte)

    fun crearEscenarioSolidSubmarino(
        reporte: ReporteSolidSimulacion = FabricaReportesDelegate.crearReporteSolidSimulacion()
    ): EscenarioSolidSubmarino = EscenarioSolidSubmarino(reporte = reporte)

    fun crearSimuladorSubmarino(
        reporte: ReporteSimulacionSubmarino = FabricaReportesDelegate.crearReporteSimulacionSubmarino(),
        fabrica: FabricaNavesSimulacion = FabricaRecursosDelegate.crearFabricaNavesSimulacion()
    ): SimuladorSubmarino = SimuladorSubmarino(reporte = reporte, fabrica = fabrica)

    fun crearPosicion(posX: Int, posY: Int): Posicion = Posicion(posX = posX, posY = posY)
}