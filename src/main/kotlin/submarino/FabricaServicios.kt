package submarino

/**
 * Raíz de composición de los servicios y resultados del dominio submarino.
 * Concentra la creación de implementaciones concretas (DIP).
 */
object FabricaServicios {

    fun crearResultadoRecoleccionExito(
        item: Recolectable,
        pesoAcumulado: Double
    ): ResultadoRecoleccion.Exito = FabricaServiciosComercioYResultadosDelegate.crearResultadoRecoleccionExito(
        item = item,
        pesoAcumulado = pesoAcumulado
    )

    fun crearResultadoRecoleccionBodegaLlena(
        item: Recolectable,
        excesoPeso: Double,
        capacidadMaxima: Double
    ): ResultadoRecoleccion.BodegaLlena =
        FabricaServiciosComercioYResultadosDelegate.crearResultadoRecoleccionBodegaLlena(
            item = item,
            excesoPeso = excesoPeso,
            capacidadMaxima = capacidadMaxima
        )

    fun crearResultadoRecoleccionYaRecolectado(
        item: Recolectable
    ): ResultadoRecoleccion.YaRecolectado =
        FabricaServiciosComercioYResultadosDelegate.crearResultadoRecoleccionYaRecolectado(item = item)

    fun crearTransaccionComercial(
        itemsVendidos: List<Vendible>,
        totalCreditos: Int,
        cantidadItems: Int,
        timestamp: Long = System.currentTimeMillis()
    ): TransaccionComercial =
        FabricaServiciosComercioYResultadosDelegate.crearTransaccionComercial(
            itemsVendidos = itemsVendidos,
            totalCreditos = totalCreditos,
            cantidadItems = cantidadItems,
            timestamp = timestamp
        )

    fun crearResultadoComercioExito(
        transaccion: TransaccionComercial,
        saldoActualizado: Int
    ): ResultadoComercio.Exito =
        FabricaServiciosComercioYResultadosDelegate.crearResultadoComercioExito(
            transaccion = transaccion,
            saldoActualizado = saldoActualizado
        )

    fun crearEstrategiaPreciosBase(): EstrategiaPreciosBase =
        FabricaServiciosMejoraYEstrategiasDelegate.crearEstrategiaPreciosBase()

    fun crearCalculadoraPreciosEstandar(
        estrategia: EstrategiaPrecios = crearEstrategiaPreciosBase()
    ): CalculadoraPreciosEstandar =
        FabricaServiciosMejoraYEstrategiasDelegate.crearCalculadoraPreciosEstandar(estrategia = estrategia)

    fun crearZonaSuperficie(profundidadMaxima: Double = 0.0): ZonaSuperficie =
        FabricaServiciosMejoraYEstrategiasDelegate.crearZonaSuperficie(profundidadMaxima = profundidadMaxima)

    fun crearResultadoCompraExito(
        mejora: Mejora,
        nivelAnterior: Int,
        nuevoNivel: Int,
        costoPagado: Int,
        saldoRestante: Int
    ): ResultadoCompra.Exito =
        FabricaServiciosComercioYResultadosDelegate.crearResultadoCompraExito(
            mejora = mejora,
            nivelAnterior = nivelAnterior,
            nuevoNivel = nuevoNivel,
            costoPagado = costoPagado,
            saldoRestante = saldoRestante
        )

    fun crearResultadoCompraFondosInsuficientes(
        mejoraId: String,
        costoRequerido: Int,
        saldoDisponible: Int
    ): ResultadoCompra.FondosInsuficientes =
        FabricaServiciosComercioYResultadosDelegate.crearResultadoCompraFondosInsuficientes(
            mejoraId = mejoraId,
            costoRequerido = costoRequerido,
            saldoDisponible = saldoDisponible
        )

    fun crearResultadoCompraNivelMaximoAlcanzado(
        mejoraId: String,
        nivelActual: Int,
        nivelMaximo: Int
    ): ResultadoCompra.NivelMaximoAlcanzado =
        FabricaServiciosComercioYResultadosDelegate.crearResultadoCompraNivelMaximoAlcanzado(
            mejoraId = mejoraId,
            nivelActual = nivelActual,
            nivelMaximo = nivelMaximo
        )

    fun crearResultadoCompraMejoraNoEncontrada(
        mejoraId: String
    ): ResultadoCompra.MejoraNoEncontrada =
        FabricaServiciosComercioYResultadosDelegate.crearResultadoCompraMejoraNoEncontrada(mejoraId = mejoraId)

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
    ): Mejora = FabricaServiciosMejoraYEstrategiasDelegate.crearMejora(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        nivelInicial = nivelInicial,
        nivelMaximo = nivelMaximo,
        efecto = efecto,
        estrategiaCosto = estrategiaCosto
    )

    fun crearCatalogoUpgrades(
        mejorasIniciales: Collection<Mejora> = emptyList()
    ): CatalogoUpgrades =
        FabricaServiciosMejoraYEstrategiasDelegate.crearCatalogoUpgrades(mejorasIniciales = mejorasIniciales)

    fun crearEfectoCapacidadBodega(magnitud: Double): EfectoCapacidadBodega =
        FabricaServiciosMejoraYEstrategiasDelegate.crearEfectoCapacidadBodega(magnitud = magnitud)

    fun crearEfectoResistenciaCasco(magnitud: Double): EfectoResistenciaCasco =
        FabricaServiciosMejoraYEstrategiasDelegate.crearEfectoResistenciaCasco(magnitud = magnitud)

    fun crearEfectoVelocidadAscenso(magnitud: Double): EfectoVelocidadAscenso =
        FabricaServiciosMejoraYEstrategiasDelegate.crearEfectoVelocidadAscenso(magnitud = magnitud)

    fun crearEfectoVelocidadPropulsion(magnitud: Double): EfectoVelocidadPropulsion =
        FabricaServiciosMejoraYEstrategiasDelegate.crearEfectoVelocidadPropulsion(magnitud = magnitud)

    fun crearEstrategiaCostoExponencial(
        costoBase: Int,
        factorMultiplicador: Double = 1.5
    ): EstrategiaCostoExponencial =
        FabricaServiciosMejoraYEstrategiasDelegate.crearEstrategiaCostoExponencial(
            costoBase = costoBase,
            factorMultiplicador = factorMultiplicador
        )

    fun crearEstrategiaCostoLineal(
        costoBase: Int,
        incrementoPorNivel: Int
    ): EstrategiaCostoLineal =
        FabricaServiciosMejoraYEstrategiasDelegate.crearEstrategiaCostoLineal(
            costoBase = costoBase,
            incrementoPorNivel = incrementoPorNivel
        )

    fun crearFabricaCatalogoUpgradesEstandar(): FabricaCatalogoUpgradesEstandar =
        FabricaServiciosMejoraYEstrategiasDelegate.crearFabricaCatalogoUpgradesEstandar()

    fun crearFabricaProcesadorComprasEstandar(): FabricaProcesadorComprasEstandar =
        FabricaServiciosMejoraYEstrategiasDelegate.crearFabricaProcesadorComprasEstandar()

    fun crearFabricaTiendaMejorasEstandar(): FabricaTiendaMejorasEstandar =
        FabricaServiciosMejoraYEstrategiasDelegate.crearFabricaTiendaMejorasEstandar()

    fun crearFabricaMejorasEstandar(
        fabricaCatalogo: FabricaCatalogoUpgrades = crearFabricaCatalogoUpgradesEstandar(),
        fabricaProcesador: FabricaProcesadorCompras = crearFabricaProcesadorComprasEstandar(),
        fabricaTienda: FabricaTiendaMejoras = crearFabricaTiendaMejorasEstandar()
    ): FabricaMejorasEstandar =
        FabricaServiciosMejoraYEstrategiasDelegate.crearFabricaMejorasEstandar(
            fabricaCatalogo = fabricaCatalogo,
            fabricaProcesador = fabricaProcesador,
            fabricaTienda = fabricaTienda
        )

    fun crearMejoraCapacidadBodega(
        id: String,
        nombre: String,
        descripcion: String,
        nivelInicial: Int = 1,
        nivelMaximo: Int = 5,
        efecto: EfectoMejora,
        estrategiaCosto: EstrategiaCosto
    ): MejoraCapacidadBodega =
        FabricaServiciosMejoraYEstrategiasDelegate.crearMejoraCapacidadBodega(
            id = id,
            nombre = nombre,
            descripcion = descripcion,
            nivelInicial = nivelInicial,
            nivelMaximo = nivelMaximo,
            efecto = efecto,
            estrategiaCosto = estrategiaCosto
        )

    fun crearMejoraResistenciaCasco(
        id: String,
        nombre: String,
        descripcion: String,
        nivelInicial: Int = 1,
        nivelMaximo: Int = 5,
        efecto: EfectoMejora,
        estrategiaCosto: EstrategiaCosto
    ): MejoraResistenciaCasco =
        FabricaServiciosMejoraYEstrategiasDelegate.crearMejoraResistenciaCasco(
            id = id,
            nombre = nombre,
            descripcion = descripcion,
            nivelInicial = nivelInicial,
            nivelMaximo = nivelMaximo,
            efecto = efecto,
            estrategiaCosto = estrategiaCosto
        )

    fun crearMejoraVelocidadAscenso(
        id: String,
        nombre: String,
        descripcion: String,
        nivelInicial: Int = 1,
        nivelMaximo: Int = 5,
        efecto: EfectoMejora,
        estrategiaCosto: EstrategiaCosto
    ): MejoraVelocidadAscenso =
        FabricaServiciosMejoraYEstrategiasDelegate.crearMejoraVelocidadAscenso(
            id = id,
            nombre = nombre,
            descripcion = descripcion,
            nivelInicial = nivelInicial,
            nivelMaximo = nivelMaximo,
            efecto = efecto,
            estrategiaCosto = estrategiaCosto
        )

    fun crearMejoraVelocidadPropulsion(
        id: String,
        nombre: String,
        descripcion: String,
        nivelInicial: Int = 1,
        nivelMaximo: Int = 5,
        efecto: EfectoMejora,
        estrategiaCosto: EstrategiaCosto
    ): MejoraVelocidadPropulsion =
        FabricaServiciosMejoraYEstrategiasDelegate.crearMejoraVelocidadPropulsion(
            id = id,
            nombre = nombre,
            descripcion = descripcion,
            nivelInicial = nivelInicial,
            nivelMaximo = nivelMaximo,
            efecto = efecto,
            estrategiaCosto = estrategiaCosto
        )

    fun crearServicioUpgrades(catalogo: ConsultableCatalogo): ServicioUpgrades =
        FabricaServiciosMejoraYEstrategiasDelegate.crearServicioUpgrades(catalogo = catalogo)

    fun crearTiendaMejoras(
        catalogo: ConsultableCatalogo,
        procesadorCompras: ProcesadorCompras
    ): TiendaMejoras = FabricaServiciosMejoraYEstrategiasDelegate.crearTiendaMejoras(
        catalogo = catalogo,
        procesadorCompras = procesadorCompras
    )
}
