package submarino

/**
 * ============================================================================
 * ENTIDAD CENTRAL DE MEJORA / UPGRADE (ISSUE #8)
 * PRINCIPIOS SOLID (SRP, OCP, LSP, ISP, DIP = 0 violaciones)
 *
 * Cumple con:
 * - Single Responsibility Principle (SRP): gestiona exclusivamente el estado de
 *   nivel, consulta de costo y delegación del efecto.
 * - Open/Closed Principle (OCP): admite cualquier [EfectoMejora] y cualquier
 *   [EstrategiaCosto] inyectadas vía interfaz sin modificar su implementación.
 * - Dependency Inversion Principle (DIP): depende exclusivamente de abstracciones
 *   ([EfectoMejora], [EstrategiaCosto]) y no de clases concretas.
 * - Interface Segregation Principle (ISP): implementa interfaces pequeñas de <= 2 métodos.
 * ============================================================================
 */

/**
 * Representa una mejora o upgrade comprable para el submarino.
 * Modela la progresión de niveles, costo escalable y efecto en estadísticas.
 *
 * @param id Identificador único alfanumérico de la mejora.
 * @param nombre Nombre descriptivo de la mejora.
 * @param descripcion Descripción legible de los beneficios que otorga.
 * @param nivelInicial Nivel inicial de la mejora (debe ser >= 1).
 * @param nivelMaximo Nivel tope máximo alcanzable (debe ser >= nivelInicial).
 * @param efecto Abstracción del efecto que aplica sobre el submarino (DIP).
 * @param estrategiaCosto Abstracción de cálculo y escalabilidad de precios (DIP).
 */
open class Mejora(
    override val id: String,
    override val nombre: String,
    override val descripcion: String,
    nivelInicial: Int = 1,
    override val nivelMaximo: Int = 5,
    val efecto: EfectoMejora,
    val estrategiaCosto: EstrategiaCosto
) : Identificable, Descriptible, Comprable, Mejorable, LimiteNivel, AplicableSubmarino {

    init {
        require(id.isNotBlank()) { "El identificador de la mejora no puede estar vacío." }
        require(nombre.isNotBlank()) { "El nombre de la mejora no puede estar vacío." }
        require(nivelInicial >= 1) { "El nivel inicial debe ser mayor o igual a 1: $nivelInicial" }
        require(nivelMaximo >= nivelInicial) {
            "El nivel máximo ($nivelMaximo) no puede ser inferior al nivel inicial ($nivelInicial)."
        }
    }

    final override var nivel: Int = nivelInicial
        private set

    /**
     * Indica si la mejora ha alcanzado el límite máximo de nivel permitido.
     */
    override val esNivelMaximo: Boolean
        get() = nivel >= nivelMaximo

    /**
     * Costo actual en monedas/créditos para ascender al siguiente nivel.
     * Si ya se encuentra en el nivel máximo, el costo es 0 ya que no admite compras adicionales.
     */
    override val costo: Int
        get() = if (esNivelMaximo) 0 else estrategiaCosto.calcularCosto(nivel)

    /**
     * Incrementa el nivel actual en una unidad siempre que no se haya alcanzado el nivel máximo.
     *
     * @return `true` si el ascenso de nivel fue exitoso; `false` si ya estaba en el nivel máximo.
     */
    override fun subirNivel(): Boolean {
        if (esNivelMaximo) {
            return false
        }
        nivel++
        return true
    }

    /**
     * Aplica el efecto de la mejora delegando en la abstracción [EfectoMejora].
     *
     * @param submarino Instancia del submarino que recibe el incremento en sus estadísticas.
     */
    override fun aplicar(submarino: Submarino) {
        efecto.aplicar(submarino)
    }

    override fun toString(): String {
        val estadoCosto = if (esNivelMaximo) "MAX" else "$$costo"
        return "Mejora(id='$id', nombre='$nombre', nivel=$nivel/$nivelMaximo, costo=$estadoCosto, efecto=${efecto})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Mejora) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}

/**
 * Especialización tipada para mejoras orientadas a ampliar la capacidad de la bodega.
 */
class MejoraCapacidadBodega(
    id: String,
    nombre: String,
    descripcion: String,
    nivelInicial: Int = 1,
    nivelMaximo: Int = 5,
    efecto: EfectoMejora,
    estrategiaCosto: EstrategiaCosto
) : Mejora(id, nombre, descripcion, nivelInicial, nivelMaximo, efecto, estrategiaCosto)

/**
 * Especialización tipada para mejoras orientadas a incrementar la velocidad de ascenso vertical.
 */
class MejoraVelocidadAscenso(
    id: String,
    nombre: String,
    descripcion: String,
    nivelInicial: Int = 1,
    nivelMaximo: Int = 5,
    efecto: EfectoMejora,
    estrategiaCosto: EstrategiaCosto
) : Mejora(id, nombre, descripcion, nivelInicial, nivelMaximo, efecto, estrategiaCosto)

/**
 * Especialización tipada para mejoras orientadas a reforzar la resistencia del casco contra la presión.
 */
class MejoraResistenciaCasco(
    id: String,
    nombre: String,
    descripcion: String,
    nivelInicial: Int = 1,
    nivelMaximo: Int = 5,
    efecto: EfectoMejora,
    estrategiaCosto: EstrategiaCosto
) : Mejora(id, nombre, descripcion, nivelInicial, nivelMaximo, efecto, estrategiaCosto)

/**
 * Especialización tipada para mejoras orientadas a incrementar la propulsión y navegación horizontal.
 */
class MejoraVelocidadPropulsion(
    id: String,
    nombre: String,
    descripcion: String,
    nivelInicial: Int = 1,
    nivelMaximo: Int = 5,
    efecto: EfectoMejora,
    estrategiaCosto: EstrategiaCosto
) : Mejora(id, nombre, descripcion, nivelInicial, nivelMaximo, efecto, estrategiaCosto)
