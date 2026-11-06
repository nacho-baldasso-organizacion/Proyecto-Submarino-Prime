package submarino

/**
 * Raíz de composición de los servicios y resultados del dominio submarino.
 * Concentra la creación de implementaciones concretas (DIP).
 */
object FabricaServicios {

    fun crearResultadoRecoleccionExito(
        item: Recolectable,
        pesoAcumulado: Double
    ): ResultadoRecoleccion.Exito = ResultadoRecoleccion.Exito(item, pesoAcumulado)

    fun crearResultadoRecoleccionBodegaLlena(
        item: Recolectable,
        excesoPeso: Double,
        capacidadMaxima: Double
    ): ResultadoRecoleccion.BodegaLlena =
        ResultadoRecoleccion.BodegaLlena(item, excesoPeso, capacidadMaxima)

    fun crearResultadoRecoleccionYaRecolectado(
        item: Recolectable
    ): ResultadoRecoleccion.YaRecolectado = ResultadoRecoleccion.YaRecolectado(item)

    fun crearTransaccionComercial(
        itemsVendidos: List<Vendible>,
        totalCreditos: Int,
        cantidadItems: Int,
        timestamp: Long = System.currentTimeMillis()
    ): TransaccionComercial =
        TransaccionComercial(itemsVendidos, totalCreditos, cantidadItems, timestamp)

    fun crearResultadoComercioExito(
        transaccion: TransaccionComercial,
        saldoActualizado: Int
    ): ResultadoComercio.Exito = ResultadoComercio.Exito(transaccion, saldoActualizado)

    fun crearEstrategiaPreciosBase(): EstrategiaPreciosBase = EstrategiaPreciosBase()

    fun crearCalculadoraPreciosEstandar(
        estrategia: EstrategiaPrecios = crearEstrategiaPreciosBase()
    ): CalculadoraPreciosEstandar = CalculadoraPreciosEstandar(estrategia)

    fun crearZonaSuperficie(profundidadMaxima: Double = 0.0): ZonaSuperficie =
        ZonaSuperficie(profundidadMaxima)

    fun crearResultadoCompraExito(
        mejora: Mejora,
        nivelAnterior: Int,
        nuevoNivel: Int,
        costoPagado: Int,
        saldoRestante: Int
    ): ResultadoCompra.Exito =
        ResultadoCompra.Exito(mejora, nivelAnterior, nuevoNivel, costoPagado, saldoRestante)

    fun crearResultadoCompraFondosInsuficientes(
        mejoraId: String,
        costoRequerido: Int,
        saldoDisponible: Int
    ): ResultadoCompra.FondosInsuficientes =
        ResultadoCompra.FondosInsuficientes(mejoraId, costoRequerido, saldoDisponible)

    fun crearResultadoCompraNivelMaximoAlcanzado(
        mejoraId: String,
        nivelActual: Int,
        nivelMaximo: Int
    ): ResultadoCompra.NivelMaximoAlcanzado =
        ResultadoCompra.NivelMaximoAlcanzado(mejoraId, nivelActual, nivelMaximo)

    fun crearResultadoCompraMejoraNoEncontrada(
        mejoraId: String
    ): ResultadoCompra.MejoraNoEncontrada = ResultadoCompra.MejoraNoEncontrada(mejoraId)

    fun crearControladorNivelMejora(
        nivelInicial: Int = 1,
        nivelMaximo: Int = 5
    ): ControladorNivelMejora = ControladorNivelMejora(nivelInicial, nivelMaximo)

    fun crearMejora(
        id: String,
        nombre: String,
        descripcion: String,
        nivelInicial: Int = 1,
        nivelMaximo: Int = 5,
        efecto: EfectoMejora,
        estrategiaCosto: EstrategiaCosto
    ): Mejora = Mejora(id, nombre, descripcion, nivelInicial, nivelMaximo, efecto, estrategiaCosto)

    fun crearCatalogoUpgrades(
        mejorasIniciales: Collection<Mejora> = emptyList()
    ): CatalogoUpgrades = CatalogoUpgrades(mejorasIniciales)

    fun crearEfectoCapacidadBodega(magnitud: Double): EfectoCapacidadBodega =
        EfectoCapacidadBodega(magnitud)

    fun crearEfectoResistenciaCasco(magnitud: Double): EfectoResistenciaCasco =
        EfectoResistenciaCasco(magnitud)

    fun crearEfectoVelocidadAscenso(magnitud: Double): EfectoVelocidadAscenso =
        EfectoVelocidadAscenso(magnitud)

    fun crearEfectoVelocidadPropulsion(magnitud: Double): EfectoVelocidadPropulsion =
        EfectoVelocidadPropulsion(magnitud)

    fun crearEstrategiaCostoExponencial(
        costoBase: Int,
        factorMultiplicador: Double = 1.5
    ): EstrategiaCostoExponencial = EstrategiaCostoExponencial(costoBase, factorMultiplicador)

    fun crearEstrategiaCostoLineal(
        costoBase: Int,
        incrementoPorNivel: Int
    ): EstrategiaCostoLineal = EstrategiaCostoLineal(costoBase, incrementoPorNivel)

    fun crearFabricaCatalogoUpgradesEstandar(): FabricaCatalogoUpgradesEstandar =
        FabricaCatalogoUpgradesEstandar()

    fun crearFabricaProcesadorComprasEstandar(): FabricaProcesadorComprasEstandar =
        FabricaProcesadorComprasEstandar()

    fun crearFabricaTiendaMejorasEstandar(): FabricaTiendaMejorasEstandar =
        FabricaTiendaMejorasEstandar()

    fun crearFabricaMejorasEstandar(
        fabricaCatalogo: FabricaCatalogoUpgrades = crearFabricaCatalogoUpgradesEstandar(),
        fabricaProcesador: FabricaProcesadorCompras = crearFabricaProcesadorComprasEstandar(),
        fabricaTienda: FabricaTiendaMejoras = crearFabricaTiendaMejorasEstandar()
    ): FabricaMejorasEstandar =
        FabricaMejorasEstandar(fabricaCatalogo, fabricaProcesador, fabricaTienda)

    fun crearMejoraCapacidadBodega(
        id: String,
        nombre: String,
        descripcion: String,
        nivelInicial: Int = 1,
        nivelMaximo: Int = 5,
        efecto: EfectoMejora,
        estrategiaCosto: EstrategiaCosto
    ): MejoraCapacidadBodega =
        MejoraCapacidadBodega(id, nombre, descripcion, nivelInicial, nivelMaximo, efecto, estrategiaCosto)

    fun crearMejoraResistenciaCasco(
        id: String,
        nombre: String,
        descripcion: String,
        nivelInicial: Int = 1,
        nivelMaximo: Int = 5,
        efecto: EfectoMejora,
        estrategiaCosto: EstrategiaCosto
    ): MejoraResistenciaCasco =
        MejoraResistenciaCasco(id, nombre, descripcion, nivelInicial, nivelMaximo, efecto, estrategiaCosto)

    fun crearMejoraVelocidadAscenso(
        id: String,
        nombre: String,
        descripcion: String,
        nivelInicial: Int = 1,
        nivelMaximo: Int = 5,
        efecto: EfectoMejora,
        estrategiaCosto: EstrategiaCosto
    ): MejoraVelocidadAscenso =
        MejoraVelocidadAscenso(id, nombre, descripcion, nivelInicial, nivelMaximo, efecto, estrategiaCosto)

    fun crearMejoraVelocidadPropulsion(
        id: String,
        nombre: String,
        descripcion: String,
        nivelInicial: Int = 1,
        nivelMaximo: Int = 5,
        efecto: EfectoMejora,
        estrategiaCosto: EstrategiaCosto
    ): MejoraVelocidadPropulsion =
        MejoraVelocidadPropulsion(id, nombre, descripcion, nivelInicial, nivelMaximo, efecto, estrategiaCosto)

    fun crearServicioUpgrades(catalogo: ConsultableCatalogo): ServicioUpgrades =
        ServicioUpgrades(catalogo)

    fun crearTiendaMejoras(
        catalogo: ConsultableCatalogo,
        procesadorCompras: ProcesadorCompras
    ): TiendaMejoras = TiendaMejoras(catalogo, procesadorCompras)
}
