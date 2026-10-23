package submarino

/**
 * ============================================================================
 * FÁBRICA Y RAÍZ DE COMPOSICIÓN DE UPGRADES (ISSUE #8)
 * PRINCIPIOS SOLID (DIP, SRP, OCP)
 * ============================================================================
 */
object FabricaMejoras : FabricaUpgrades {

    const val ID_BODEGA = "UPG-BODEGA"
    const val ID_ASCENSO = "UPG-ASCENSO"
    const val ID_CASCO = "UPG-CASCO"
    const val ID_PROPULSION = "UPG-PROPULSION"

    override fun crearMejoraBodega(
        id: String,
        nombre: String,
        descripcion: String,
        nivelInicial: Int,
        nivelMaximo: Int,
        incrementoCapacidadKg: Double,
        costoBase: Int,
        incrementoPorNivel: Int
    ): MejoraCapacidadBodega {
        val efecto: EfectoMejora = EfectoCapacidadBodega(incrementoCapacidadKg)
        val estrategiaCosto: EstrategiaCosto = EstrategiaCostoLineal(costoBase, incrementoPorNivel)
        return MejoraCapacidadBodega(
            id = id,
            nombre = nombre,
            descripcion = descripcion,
            nivelInicial = nivelInicial,
            nivelMaximo = nivelMaximo,
            efecto = efecto,
            estrategiaCosto = estrategiaCosto
        )
    }

    fun crearMejoraVelocidadAscenso(
        id: String = ID_ASCENSO,
        nombre: String = "Turbina de Ascenso Vertical",
        descripcion: String = "Aumenta la velocidad nominal base de ascenso hacia la superficie (+2.0 m/s)",
        nivelInicial: Int = 1,
        nivelMaximo: Int = 5,
        incrementoVelocidadMs: Double = 2.0,
        costoBase: Int = 120,
        factorMultiplicador: Double = 1.5
    ): MejoraVelocidadAscenso {
        val efecto: EfectoMejora = EfectoVelocidadAscenso(incrementoVelocidadMs)
        val estrategiaCosto: EstrategiaCosto = EstrategiaCostoExponencial(costoBase, factorMultiplicador)
        return MejoraVelocidadAscenso(
            id = id,
            nombre = nombre,
            descripcion = descripcion,
            nivelInicial = nivelInicial,
            nivelMaximo = nivelMaximo,
            efecto = efecto,
            estrategiaCosto = estrategiaCosto
        )
    }

    fun crearMejoraResistenciaCasco(
        id: String = ID_CASCO,
        nombre: String = "Blindaje de Presión Abisal",
        descripcion: String = "Refuerza la tolerancia del casco ante la sobrepresión (+150,000 Pa)",
        nivelInicial: Int = 1,
        nivelMaximo: Int = 5,
        incrementoPresionPa: Double = 150_000.0,
        costoBase: Int = 150,
        incrementoPorNivel: Int = 100
    ): MejoraResistenciaCasco {
        val efecto: EfectoMejora = EfectoResistenciaCasco(incrementoPresionPa)
        val estrategiaCosto: EstrategiaCosto = EstrategiaCostoLineal(costoBase, incrementoPorNivel)
        return MejoraResistenciaCasco(
            id = id,
            nombre = nombre,
            descripcion = descripcion,
            nivelInicial = nivelInicial,
            nivelMaximo = nivelMaximo,
            efecto = efecto,
            estrategiaCosto = estrategiaCosto
        )
    }

    fun crearMejoraVelocidadPropulsion(
        id: String = ID_PROPULSION,
        nombre: String = "Motor Náutico de Alto Flujo",
        descripcion: String = "Incrementa la velocidad de propulsión horizontal en navegación (+2.5 m/s)",
        nivelInicial: Int = 1,
        nivelMaximo: Int = 5,
        incrementoVelocidadMs: Double = 2.5,
        costoBase: Int = 80,
        incrementoPorNivel: Int = 40
    ): MejoraVelocidadPropulsion {
        val efecto: EfectoMejora = EfectoVelocidadPropulsion(incrementoVelocidadMs)
        val estrategiaCosto: EstrategiaCosto = EstrategiaCostoLineal(costoBase, incrementoPorNivel)
        return MejoraVelocidadPropulsion(
            id = id,
            nombre = nombre,
            descripcion = descripcion,
            nivelInicial = nivelInicial,
            nivelMaximo = nivelMaximo,
            efecto = efecto,
            estrategiaCosto = estrategiaCosto
        )
    }

    override fun crearCatalogoEstandar(): CatalogoUpgrades {
        val lista = listOf(
            crearMejoraBodega(),
            crearMejoraVelocidadAscenso(),
            crearMejoraResistenciaCasco(),
            crearMejoraVelocidadPropulsion()
        )
        return CatalogoUpgrades(lista)
    }

    override fun crearTiendaEstandar(): TiendaMejoras {
        val catalogo: ConsultableCatalogo = crearCatalogoEstandar()
        val procesadorCompras: ProcesadorCompras = ServicioUpgrades(catalogo)
        return TiendaMejoras(
            catalogo = catalogo,
            procesadorCompras = procesadorCompras
        )
    }
}
