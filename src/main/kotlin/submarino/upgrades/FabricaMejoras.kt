package submarino

/**
 * ============================================================================
 * FÁBRICA Y RAÍZ DE COMPOSICIÓN DE UPGRADES (ISSUE #8)
 * PRINCIPIOS SOLID (DIP, SRP, OCP = 0 violaciones)
 *
 * Encapsula la instanciación de dependencias concretas para el ensamble del
 * sistema de mejoras, garantizando que ninguna clase de dominio instancie a otra
 * directamente dentro de sus constructores o métodos de negocio (DIP estricto).
 * ============================================================================
 */
object FabricaMejoras {

    const val ID_BODEGA = "UPG-BODEGA"
    const val ID_ASCENSO = "UPG-ASCENSO"
    const val ID_CASCO = "UPG-CASCO"
    const val ID_PROPULSION = "UPG-PROPULSION"

    /**
     * Construye una mejora de capacidad de bodega con valores estándar o personalizados.
     */
    fun crearMejoraBodega(
        id: String = ID_BODEGA,
        nombre: String = "Expansión de Bodega",
        descripcion: String = "Incrementa la capacidad de carga en kilogramos (+15.0 kg por nivel)",
        nivelInicial: Int = 1,
        nivelMaximo: Int = 5,
        incrementoCapacidadKg: Double = 15.0,
        costoBase: Int = 100,
        incrementoPorNivel: Int = 50
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

    /**
     * Construye una mejora de velocidad nominal de ascenso con progresión geométrica de costo.
     */
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

    /**
     * Construye una mejora de resistencia de casco ante la presión hidrostática profunda.
     */
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

    /**
     * Construye una mejora de velocidad de propulsión y navegación horizontal de motor.
     */
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

    /**
     * Ensambla un catálogo estándar completo con todas las mejoras oficiales del juego.
     */
    fun crearCatalogoEstandar(): CatalogoUpgrades {
        val lista = listOf(
            crearMejoraBodega(),
            crearMejoraVelocidadAscenso(),
            crearMejoraResistenciaCasco(),
            crearMejoraVelocidadPropulsion()
        )
        return CatalogoUpgrades(lista)
    }

    /**
     * Ensambla una instancia completamente cableada de [TiendaMejoras] lista para operar.
     */
    fun crearTiendaEstandar(): TiendaMejoras {
        val catalogo: ConsultableCatalogo = crearCatalogoEstandar()
        val procesadorCompras: ProcesadorCompras = ServicioUpgrades(catalogo)
        return TiendaMejoras(
            catalogo = catalogo,
            procesadorCompras = procesadorCompras
        )
    }
}
