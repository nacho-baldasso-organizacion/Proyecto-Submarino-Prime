package submarino

/**
 * ============================================================================
 * FÁBRICA Y RAÍZ DE COMPOSICIÓN DE UPGRADES (ISSUE #8)
 * PRINCIPIOS SOLID (DIP, SRP, OCP, DRY)
 * ============================================================================
 */
object FabricaMejoras : FabricaUpgrades {

    const val ID_BODEGA = "UPG-BODEGA"
    const val ID_ASCENSO = "UPG-ASCENSO"
    const val ID_CASCO = "UPG-CASCO"
    const val ID_PROPULSION = "UPG-PROPULSION"

    override fun crearMejoraBodega(
        id: String, nombre: String, descripcion: String, nivelInicial: Int,
        nivelMaximo: Int, incrementoCapacidadKg: Double, costoBase: Int, incrementoPorNivel: Int
    ): MejoraCapacidadBodega = MejoraCapacidadBodega(
        id, nombre, descripcion, nivelInicial, nivelMaximo,
        EfectoCapacidadBodega(incrementoCapacidadKg), EstrategiaCostoLineal(costoBase, incrementoPorNivel)
    )

    fun crearMejoraVelocidadAscenso(
        id: String = ID_ASCENSO,
        nombre: String = "Turbina de Ascenso Vertical",
        descripcion: String = "Aumenta la velocidad nominal base de ascenso hacia la superficie (+2.0 m/s)",
        nivelInicial: Int = 1, nivelMaximo: Int = 5,
        incrementoVelocidadMs: Double = 2.0, costoBase: Int = 120, factorMultiplicador: Double = 1.5
    ): MejoraVelocidadAscenso = MejoraVelocidadAscenso(
        id, nombre, descripcion, nivelInicial, nivelMaximo,
        EfectoVelocidadAscenso(incrementoVelocidadMs), EstrategiaCostoExponencial(costoBase, factorMultiplicador)
    )

    fun crearMejoraResistenciaCasco(
        id: String = ID_CASCO,
        nombre: String = "Blindaje de Presión Abisal",
        descripcion: String = "Refuerza la tolerancia del casco ante la sobrepresión (+150,000 Pa)",
        nivelInicial: Int = 1, nivelMaximo: Int = 5,
        incrementoPresionPa: Double = 150_000.0, costoBase: Int = 150, incrementoPorNivel: Int = 100
    ): MejoraResistenciaCasco = MejoraResistenciaCasco(
        id, nombre, descripcion, nivelInicial, nivelMaximo,
        EfectoResistenciaCasco(incrementoPresionPa), EstrategiaCostoLineal(costoBase, incrementoPorNivel)
    )

    fun crearMejoraVelocidadPropulsion(
        id: String = ID_PROPULSION,
        nombre: String = "Motor Náutico de Alto Flujo",
        descripcion: String = "Incrementa la velocidad de propulsión horizontal en navegación (+2.5 m/s)",
        nivelInicial: Int = 1, nivelMaximo: Int = 5,
        incrementoVelocidadMs: Double = 2.5, costoBase: Int = 80, incrementoPorNivel: Int = 40
    ): MejoraVelocidadPropulsion = MejoraVelocidadPropulsion(
        id, nombre, descripcion, nivelInicial, nivelMaximo,
        EfectoVelocidadPropulsion(incrementoVelocidadMs), EstrategiaCostoLineal(costoBase, incrementoPorNivel)
    )

    override fun crearCatalogoEstandar(): CatalogoUpgrades =
        CatalogoUpgrades(listOf(
            crearMejoraBodega(), crearMejoraVelocidadAscenso(),
            crearMejoraResistenciaCasco(), crearMejoraVelocidadPropulsion()
        ))

    override fun crearTiendaEstandar(): TiendaMejoras {
        val catalogo: ConsultableCatalogo = crearCatalogoEstandar()
        val procesadorCompras: ProcesadorCompras = ServicioUpgrades(catalogo)
        return TiendaMejoras(catalogo, procesadorCompras)
    }
}
