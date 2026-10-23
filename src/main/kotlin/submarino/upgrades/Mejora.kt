package submarino

/**
 * ============================================================================
 * ENTIDAD CENTRAL DE MEJORA / UPGRADE (ISSUE #8)
 * PRINCIPIOS SOLID (SRP, OCP, LSP, ISP, DIP)
 * ============================================================================
 */

/**
 * Representa una mejora o upgrade comprable para el submarino.
 * Modela la progresión de niveles, costo escalable y efecto en estadísticas.
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

    override val esNivelMaximo: Boolean
        get() = nivel >= nivelMaximo

    override val costo: Int
        get() = if (esNivelMaximo) 0 else estrategiaCosto.calcularCosto(nivel)

    override fun subirNivel(): Boolean {
        if (esNivelMaximo) {
            return false
        }
        nivel++
        return true
    }

    override fun aplicar(submarino: Submarino) {
        efecto.aplicar(submarino)
    }

    override fun toString(): String {
        val estadoCosto = if (esNivelMaximo) "MAX" else "$$costo"
        return "Mejora(id='$id', nombre='$nombre', nivel=$nivel/$nivelMaximo, costo=$estadoCosto, efecto=$efecto)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherMejora = other as? Mejora ?: return false
        return id == otherMejora.id
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
