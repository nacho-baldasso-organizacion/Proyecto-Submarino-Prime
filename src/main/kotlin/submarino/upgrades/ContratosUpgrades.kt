package submarino

/**
 * ============================================================================
 * CONTRATOS E INTERFACES DEL SISTEMA DE UPGRADES / MEJORAS (ISSUE #8)
 * PRINCIPIOS SOLID (ISP, DIP, OCP)
 * ============================================================================
 */

interface Identificable {
    val id: String
}

interface Descriptible {
    val nombre: String
    val descripcion: String
}

interface Comprable {
    val costo: Int
}

interface Mejorable {
    val nivel: Int
    fun subirNivel(): Boolean
}

interface LimiteNivel {
    val nivelMaximo: Int
    val esNivelMaximo: Boolean
}

interface AplicableSubmarino {
    fun aplicar(submarino: Submarino)
}

interface EfectoMejora : AplicableSubmarino {
    val magnitud: Double
}

interface EstrategiaCosto {
    fun calcularCosto(nivelActual: Int): Int
}

interface ConsultableCatalogo {
    fun buscarPorId(id: String): Mejora?
    fun obtenerTodas(): List<Mejora>
}

interface GestionableCatalogo {
    fun registrar(mejora: Mejora): Boolean
    fun eliminar(id: String): Boolean
}

interface ProcesadorCompras {
    fun comprarMejora(idMejora: String, pagable: Pagable, submarino: Submarino): ResultadoCompra
}

interface ObservadorTienda {
    fun alComprarMejora(mejora: Mejora, saldoRestante: Int)
    fun alRechazarCompra(idMejora: String, motivo: String)
}

interface GestorObservadoresTienda {
    fun registrarObservador(observador: ObservadorTienda)
    fun eliminarObservador(observador: ObservadorTienda)
}

interface TiendaMejorasContrato {
    fun comprar(idMejora: String, pagable: Pagable, submarino: Submarino): ResultadoCompra
    fun listarMejoras(): List<Mejora>
}

/**
 * Abstracción de fábrica para ensamblado de mejoras y tiendas (DIP).
 */
interface FabricaUpgrades {
    fun crearMejoraBodega(
        id: String = "UPG-BODEGA",
        nombre: String = "Expansión de Bodega",
        descripcion: String = "Incrementa la capacidad de carga en kilogramos (+15.0 kg por nivel)",
        nivelInicial: Int = 1,
        nivelMaximo: Int = 5,
        incrementoCapacidadKg: Double = 15.0,
        costoBase: Int = 100,
        incrementoPorNivel: Int = 50
    ): MejoraCapacidadBodega

    fun crearCatalogoEstandar(): CatalogoUpgrades
    fun crearTiendaEstandar(): TiendaMejoras
}
