package submarino

object FabricaReportesDelegate {

    fun crearReporteSimulacionSubmarino(): ReporteSimulacionSubmarino =
        ReporteSimulacionSubmarino()

    fun crearReporteNavegacionSimulacion(): ReporteNavegacionSimulacion =
        ReporteNavegacionSimulacion()

    fun crearReporteInmersionSimulacion(): ReporteInmersionSimulacion =
        ReporteInmersionSimulacion()

    fun crearReporteSolidSimulacion(): ReporteSolidSimulacion =
        ReporteSolidSimulacion()
}