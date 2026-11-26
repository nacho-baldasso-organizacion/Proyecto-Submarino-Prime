package submarino

/**
 * Sub-fábrica delegada de creación de efectos, estrategias y fábricas de mejoras.
 * Concentra métodos de instanciación de efectos de mejora, costos y fábricas
 * con visibilidad pública, reduciendo el acoplamiento (CBO) de la fábrica raíz.
 */
object FabricaServiciosMejoraYEstrategiasDelegate {

    fun crearEstrategiaPreciosBase(): EstrategiaPreciosBase = EstrategiaPreciosBase()

    fun crearCalculadoraPreciosEstandar(
        estrategia: EstrategiaPrecios = crearEstrategiaPreciosBase()
    ): CalculadoraPreciosEstandar = CalculadoraPreciosEstandar(estrategia)

    fun crearZonaSuperficie(profundidadMaxima: Double = 0.0): ZonaSuperficie =
        ZonaSuperficie(profundidadMaxima)

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