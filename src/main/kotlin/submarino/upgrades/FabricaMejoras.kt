package submarino

/**
 * ============================================================================
 * FÁBRICA Y RAÍZ DE COMPOSICIÓN DE UPGRADES (ISSUE #8)
 * PRINCIPIOS SOLID (DIP, SRP, OCP, DRY)
 * ============================================================================
 */

/**
 * Abstracción para la creación del catálogo de mejoras (DIP).
 */
fun interface FabricaCatalogoUpgrades {
    fun crear(mejoras: Collection<Mejora>): CatalogoUpgrades
}

/**
 * Implementación estándar de [FabricaCatalogoUpgrades].
 */
class FabricaCatalogoUpgradesEstandar : FabricaCatalogoUpgrades {
    override fun crear(mejoras: Collection<Mejora>): CatalogoUpgrades =
        CatalogoUpgrades(mejoras)
}

/**
 * Abstracción para la creación del procesador de compras (DIP).
 */
fun interface FabricaProcesadorCompras {
    fun crear(catalogo: ConsultableCatalogo): ProcesadorCompras
}

/**
 * Implementación estándar de [FabricaProcesadorCompras].
 */
class FabricaProcesadorComprasEstandar : FabricaProcesadorCompras {
    override fun crear(catalogo: ConsultableCatalogo): ProcesadorCompras =
        ServicioUpgrades(catalogo)
}

/**
 * Abstracción para la creación de la fachada de tienda (DIP).
 */
fun interface FabricaTiendaMejoras {
    fun crear(catalogo: ConsultableCatalogo, procesador: ProcesadorCompras): TiendaMejoras
}

/**
 * Implementación estándar de [FabricaTiendaMejoras].
 */
class FabricaTiendaMejorasEstandar : FabricaTiendaMejoras {
    override fun crear(catalogo: ConsultableCatalogo, procesador: ProcesadorCompras): TiendaMejoras =
        TiendaMejoras(catalogo, procesador)
}

/**
 * Implementación de [FabricaUpgrades] que depende de abstracciones para ensamblar
 * el catálogo, el procesador de compras y la tienda (DIP).
 */
class FabricaMejorasEstandar(
    private val fabricaCatalogo: FabricaCatalogoUpgrades = FabricaCatalogoUpgradesEstandar(),
    private val fabricaProcesador: FabricaProcesadorCompras = FabricaProcesadorComprasEstandar(),
    private val fabricaTienda: FabricaTiendaMejoras = FabricaTiendaMejorasEstandar()
) : FabricaUpgrades {

    override fun crearMejoraBodega(
        id: String, nombre: String, descripcion: String, nivelInicial: Int,
        nivelMaximo: Int, incrementoCapacidadKg: Double, costoBase: Int, incrementoPorNivel: Int
    ): MejoraCapacidadBodega = MejoraCapacidadBodega(
        id, nombre, descripcion, nivelInicial, nivelMaximo,
        EfectoCapacidadBodega(incrementoCapacidadKg), EstrategiaCostoLineal(costoBase, incrementoPorNivel)
    )

    fun crearMejoraVelocidadAscenso(
        id: String = FabricaMejoras.ID_ASCENSO,
        nombre: String = "Turbina de Ascenso Vertical",
        descripcion: String = "Aumenta la velocidad nominal base de ascenso hacia la superficie (+2.0 m/s)",
        nivelInicial: Int = 1, nivelMaximo: Int = 5,
        incrementoVelocidadMs: Double = 2.0, costoBase: Int = 120, factorMultiplicador: Double = 1.5
    ): MejoraVelocidadAscenso = MejoraVelocidadAscenso(
        id, nombre, descripcion, nivelInicial, nivelMaximo,
        EfectoVelocidadAscenso(incrementoVelocidadMs), EstrategiaCostoExponencial(costoBase, factorMultiplicador)
    )

    fun crearMejoraResistenciaCasco(
        id: String = FabricaMejoras.ID_CASCO,
        nombre: String = "Blindaje de Presión Abisal",
        descripcion: String = "Refuerza la tolerancia del casco ante la sobrepresión (+150,000 Pa)",
        nivelInicial: Int = 1, nivelMaximo: Int = 5,
        incrementoPresionPa: Double = 150_000.0, costoBase: Int = 150, incrementoPorNivel: Int = 100
    ): MejoraResistenciaCasco = MejoraResistenciaCasco(
        id, nombre, descripcion, nivelInicial, nivelMaximo,
        EfectoResistenciaCasco(incrementoPresionPa), EstrategiaCostoLineal(costoBase, incrementoPorNivel)
    )

    fun crearMejoraVelocidadPropulsion(
        id: String = FabricaMejoras.ID_PROPULSION,
        nombre: String = "Motor Náutico de Alto Flujo",
        descripcion: String = "Incrementa la velocidad de propulsión horizontal en navegación (+2.5 m/s)",
        nivelInicial: Int = 1, nivelMaximo: Int = 5,
        incrementoVelocidadMs: Double = 2.5, costoBase: Int = 80, incrementoPorNivel: Int = 40
    ): MejoraVelocidadPropulsion = MejoraVelocidadPropulsion(
        id, nombre, descripcion, nivelInicial, nivelMaximo,
        EfectoVelocidadPropulsion(incrementoVelocidadMs), EstrategiaCostoLineal(costoBase, incrementoPorNivel)
    )

    override fun crearCatalogoEstandar(): CatalogoUpgrades =
        fabricaCatalogo.crear(listOf(
            crearMejoraBodega(), crearMejoraVelocidadAscenso(),
            crearMejoraResistenciaCasco(), crearMejoraVelocidadPropulsion()
        ))

    override fun crearTiendaEstandar(): TiendaMejoras {
        val catalogo: CatalogoUpgrades = crearCatalogoEstandar()
        val procesadorCompras: ProcesadorCompras = fabricaProcesador.crear(catalogo)
        return fabricaTienda.crear(catalogo, procesadorCompras)
    }
}

/**
 * Fachada estática de la fábrica y raíz de composición de upgrades.
 * Delega en [FabricaMejorasEstandar], que depende de abstracciones para sus colaboradores.
 */
object FabricaMejoras : FabricaUpgrades {

    const val ID_BODEGA = "UPG-BODEGA"
    const val ID_ASCENSO = "UPG-ASCENSO"
    const val ID_CASCO = "UPG-CASCO"
    const val ID_PROPULSION = "UPG-PROPULSION"

    private val implementacion: FabricaMejorasEstandar = FabricaMejorasEstandar()

    override fun crearMejoraBodega(
        id: String, nombre: String, descripcion: String, nivelInicial: Int,
        nivelMaximo: Int, incrementoCapacidadKg: Double, costoBase: Int, incrementoPorNivel: Int
    ): MejoraCapacidadBodega = implementacion.crearMejoraBodega(
        id, nombre, descripcion, nivelInicial, nivelMaximo,
        incrementoCapacidadKg, costoBase, incrementoPorNivel
    )

    fun crearMejoraVelocidadAscenso(
        id: String = ID_ASCENSO,
        nombre: String = "Turbina de Ascenso Vertical",
        descripcion: String = "Aumenta la velocidad nominal base de ascenso hacia la superficie (+2.0 m/s)",
        nivelInicial: Int = 1, nivelMaximo: Int = 5,
        incrementoVelocidadMs: Double = 2.0, costoBase: Int = 120, factorMultiplicador: Double = 1.5
    ): MejoraVelocidadAscenso = implementacion.crearMejoraVelocidadAscenso(
        id, nombre, descripcion, nivelInicial, nivelMaximo,
        incrementoVelocidadMs, costoBase, factorMultiplicador
    )

    fun crearMejoraResistenciaCasco(
        id: String = ID_CASCO,
        nombre: String = "Blindaje de Presión Abisal",
        descripcion: String = "Refuerza la tolerancia del casco ante la sobrepresión (+150,000 Pa)",
        nivelInicial: Int = 1, nivelMaximo: Int = 5,
        incrementoPresionPa: Double = 150_000.0, costoBase: Int = 150, incrementoPorNivel: Int = 100
    ): MejoraResistenciaCasco = implementacion.crearMejoraResistenciaCasco(
        id, nombre, descripcion, nivelInicial, nivelMaximo,
        incrementoPresionPa, costoBase, incrementoPorNivel
    )

    fun crearMejoraVelocidadPropulsion(
        id: String = ID_PROPULSION,
        nombre: String = "Motor Náutico de Alto Flujo",
        descripcion: String = "Incrementa la velocidad de propulsión horizontal en navegación (+2.5 m/s)",
        nivelInicial: Int = 1, nivelMaximo: Int = 5,
        incrementoVelocidadMs: Double = 2.5, costoBase: Int = 80, incrementoPorNivel: Int = 40
    ): MejoraVelocidadPropulsion = implementacion.crearMejoraVelocidadPropulsion(
        id, nombre, descripcion, nivelInicial, nivelMaximo,
        incrementoVelocidadMs, costoBase, incrementoPorNivel
    )

    override fun crearCatalogoEstandar(): CatalogoUpgrades = implementacion.crearCatalogoEstandar()

    override fun crearTiendaEstandar(): TiendaMejoras = implementacion.crearTiendaEstandar()
}
