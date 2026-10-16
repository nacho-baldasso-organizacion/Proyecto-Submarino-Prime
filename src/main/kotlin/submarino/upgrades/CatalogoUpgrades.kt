package submarino

/**
 * ============================================================================
 * GESTIÓN Y CATÁLOGO DE UPGRADES (ISSUE #8)
 * PRINCIPIOS SOLID (SRP, OCP, LSP, ISP, DIP = 0 violaciones)
 *
 * Cumple con:
 * - Single Responsibility Principle (SRP): se encarga exclusivamente de administrar
 *   el almacenamiento, consulta y mutación de la colección de mejoras disponibles.
 * - Interface Segregation Principle (ISP): implementa [ConsultableCatalogo] y [GestionableCatalogo].
 * ============================================================================
 */

/**
 * Catálogo centralizado que almacena y organiza las mejoras disponibles para compra.
 *
 * @param mejorasIniciales Colección inicial opcional de mejoras para poblar el catálogo.
 */
class CatalogoUpgrades(
    mejorasIniciales: Collection<Mejora> = emptyList()
) : ConsultableCatalogo, GestionableCatalogo {

    private val mejoras: MutableMap<String, Mejora> = LinkedHashMap()

    init {
        mejorasIniciales.forEach { registrar(it) }
    }

    /**
     * Cantidad total de mejoras registradas en el catálogo.
     */
    val cantidad: Int
        get() = mejoras.size

    /**
     * Busca una mejora por su identificador único.
     *
     * @param id Clave identificadora de la mejora buscada.
     * @return La [Mejora] correspondiente si existe, o `null` si no está registrada.
     */
    override fun buscarPorId(id: String): Mejora? = mejoras[id]

    /**
     * Retorna una lista inmutable con todas las mejoras actualmente registradas.
     */
    override fun obtenerTodas(): List<Mejora> = mejoras.values.toList()

    /**
     * Registra una nueva mejora en el catálogo.
     * Si ya existe una mejora con el mismo identificador, no la sobreescribe y retorna `false`.
     *
     * @param mejora Instancia de [Mejora] a incorporar.
     * @return `true` si fue registrada con éxito; `false` si ya existía el id.
     */
    override fun registrar(mejora: Mejora): Boolean {
        if (mejoras.containsKey(mejora.id)) {
            return false
        }
        mejoras[mejora.id] = mejora
        return true
    }

    /**
     * Elimina una mejora del catálogo según su identificador.
     *
     * @param id Identificador de la mejora a remover.
     * @return `true` si la mejora fue eliminada; `false` si no existía.
     */
    override fun eliminar(id: String): Boolean {
        return mejoras.remove(id) != null
    }

    /**
     * Verifica si una mejora con el ID especificado existe en el catálogo.
     */
    fun contiene(id: String): Boolean = mejoras.containsKey(id)

    override fun toString(): String =
        "CatalogoUpgrades(totalMejoras=$cantidad, items=${mejoras.keys})"
}
