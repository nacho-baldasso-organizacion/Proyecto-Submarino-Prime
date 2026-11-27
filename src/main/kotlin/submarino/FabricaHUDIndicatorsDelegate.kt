package submarino

/**
 * Sub-fábrica delegada de creación de indicadores y barras del HUD.
 * Contiene métodos de instanciación de componentes HUD con menos de 8 tipos del proyecto.
 */
object FabricaHUDIndicatorsDelegate {

    fun crearIndicadorMetrosHUD(
        titulo: String,
        estiloTitulo: String,
        estiloValor: String,
        patron: String
    ): IndicadorMetrosHUD = IndicadorMetrosHUD(titulo, estiloTitulo, estiloValor, patron)

    fun crearIndicadorPresionHUD(
        estiloTitulo: String,
        estiloValor: String,
        anchoBarra: Double,
        estiloBarraEstable: String,
        estiloBarraPrecaucion: String,
        estiloBarraCritico: String
    ): IndicadorPresionHUD = IndicadorPresionHUD(
        estiloTitulo,
        estiloValor,
        anchoBarra,
        estiloBarraEstable,
        estiloBarraPrecaucion,
        estiloBarraCritico
    )

    fun crearIndicadorOxigenoJuegoHUD(
        estiloTitulo: String,
        estiloValor: String,
        anchoBarra: Double,
        estiloBarraEstable: String,
        estiloBarraPrecaucion: String,
        estiloBarraCritico: String
    ): IndicadorOxigenoJuegoHUD = IndicadorOxigenoJuegoHUD(
        estiloTitulo,
        estiloValor,
        anchoBarra,
        estiloBarraEstable,
        estiloBarraPrecaucion,
        estiloBarraCritico
    )

    fun crearIndicadorCascoHUD(
        estiloTitulo: String,
        estiloValor: String,
        anchoBarra: Double,
        estiloBarraEstable: String,
        estiloBarraPrecaucion: String,
        estiloBarraCritico: String
    ): IndicadorCascoHUD = IndicadorCascoHUD(
        estiloTitulo,
        estiloValor,
        anchoBarra,
        estiloBarraEstable,
        estiloBarraPrecaucion,
        estiloBarraCritico
    )

    fun crearIndicadorCargoHUD(
        estiloTitulo: String,
        estiloValor: String
    ): IndicadorCargoHUD = IndicadorCargoHUD(estiloTitulo, estiloValor)

    fun crearIndicadorMonedasHUD(
        estiloTitulo: String,
        estiloValor: String
    ): IndicadorMonedasHUD = IndicadorMonedasHUD(estiloTitulo, estiloValor)

    fun crearIndicadorNodosHUD(
        estiloTitulo: String,
        estiloValor: String
    ): IndicadorNodosHUD = IndicadorNodosHUD(estiloTitulo, estiloValor)

    fun crearIndicadorAcopleHUD(
        estiloTitulo: String,
        estiloValor: String
    ): IndicadorAcopleHUD = IndicadorAcopleHUD(estiloTitulo, estiloValor)

    fun crearSeparadorHUD(estilo: String): SeparadorHUD = SeparadorHUD(estilo)

    fun crearTextoAyudaHUD(texto: String, estilo: String): TextoAyudaHUD =
        TextoAyudaHUD(texto, estilo)

    fun crearIndicadorEstadoOxigenoHUD(
        estiloTitulo: String,
        estiloValor: String
    ): IndicadorEstadoOxigenoHUD = IndicadorEstadoOxigenoHUD(estiloTitulo, estiloValor)

    fun crearIndicadorOxigenoNavegacionHUD(
        estiloTitulo: String,
        estiloValor: String,
        anchoBarra: Double,
        estiloBarraEstable: String,
        estiloBarraPrecaucion: String,
        estiloBarraCritico: String,
        estiloBarraAgotado: String
    ): IndicadorOxigenoNavegacionHUD = IndicadorOxigenoNavegacionHUD(
        estiloTitulo,
        estiloValor,
        anchoBarra,
        estiloBarraEstable,
        estiloBarraPrecaucion,
        estiloBarraCritico,
        estiloBarraAgotado
    )

    fun crearBarraUmbralHUD(
        ancho: Double,
        progresoInicial: Double,
        estiloEstable: String,
        estiloPrecaucion: String,
        estiloCritico: String
    ): BarraUmbralHUD = BarraUmbralHUD(
        ancho,
        progresoInicial,
        estiloEstable,
        estiloPrecaucion,
        estiloCritico
    )

    fun crearBarraEstadoOxigenoHUD(
        ancho: Double,
        estiloEstable: String,
        estiloPrecaucion: String,
        estiloCritico: String,
        estiloAgotado: String
    ): BarraEstadoOxigenoHUD = BarraEstadoOxigenoHUD(
        ancho,
        estiloEstable,
        estiloPrecaucion,
        estiloCritico,
        estiloAgotado
    )
}