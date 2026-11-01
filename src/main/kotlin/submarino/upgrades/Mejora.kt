package submarino

/**
 * ============================================================================
 * ENTIDAD CENTRAL DE MEJORA / UPGRADE (ISSUE #8)
 * PRINCIPIOS SOLID (SRP, OCP, LSP, ISP, DIP)
 * ============================================================================
 */

/**
 * Componente especializado en el control de progresión de niveles y topes máximos (SRP).
 */
class ControladorNivelMejora(
    nivelInicial: Int = 1,
    nivelMaximo: Int = 5
) : Mejorable, LimiteNivel {

    init {
        require(nivelInicial >= 1) { "El nivel inicial debe ser mayor o igual a 1: $nivelInicial" }
        require(nivelMaximo >= nivelInicial) {
            "El nivel máximo ($nivelMaximo) no puede ser inferior al nivel inicial ($nivelInicial)."
        }
    }

    override var nivel: Int = nivelInicial
        private set

    override var nivelMaximo: Int = nivelMaximo
        private set

    override val esNivelMaximo: Boolean
        get() = nivel >= nivelMaximo

    override fun subirNivel(): Boolean {
        if (esNivelMaximo) {
            return false
        }
        nivel++
        return true
    }
}

/**
 * Representa una mejora o upgrade comprable para el submarino.
 * Descompuesta mediante delegación en [ControladorNivelMejora] para cumplir con SRP.
 * Encapsula la totalidad de sus campos con visibilidad 'private set' y métodos de comportamiento.
 */
open class Mejora(
    id: String,
    nombre: String,
    descripcion: String,
    nivelInicial: Int = 1,
    nivelMaximo: Int = 5,
    efecto: EfectoMejora,
    estrategiaCosto: EstrategiaCosto
) : Identificable, Descriptible, Comprable, Mejorable, LimiteNivel, AplicableSubmarino {

    init {
        require(id.isNotBlank()) { "El identificador de la mejora no puede estar vacío." }
        require(nombre.isNotBlank()) { "El nombre de la mejora no puede estar vacío." }
    }

    final override var id: String = id
        private set

    final override var nombre: String = nombre
        private set

    final override var descripcion: String = descripcion
        private set

    private val gestorNivel: ControladorNivelMejora = ControladorNivelMejora(nivelInicial, nivelMaximo)
    private var efectoMejora: EfectoMejora = efecto
    private var estrategiaCostoMejora: EstrategiaCosto = estrategiaCosto

    private val efecto: EfectoMejora
        get() = efectoMejora

    private val estrategiaCosto: EstrategiaCosto
        get() = estrategiaCostoMejora

    override val nivel: Int
        get() = gestorNivel.nivel

    override val nivelMaximo: Int
        get() = gestorNivel.nivelMaximo

    override val esNivelMaximo: Boolean
        get() = gestorNivel.esNivelMaximo

    override val costo: Int
        get() = if (esNivelMaximo) 0 else estrategiaCostoMejora.calcularCosto(nivel)

    override fun subirNivel(): Boolean = gestorNivel.subirNivel()

    override fun aplicar(submarino: SubmarinoMejorable) {
        efectoMejora.aplicar(submarino)
    }

    fun obtenerNivel(): Int = nivel

    fun obtenerNivelMaximo(): Int = nivelMaximo

    fun obtenerCosto(): Int = costo

    fun obtenerEfecto(): EfectoMejora = efectoMejora

    fun obtenerEstrategiaCosto(): EstrategiaCosto = estrategiaCostoMejora

    override fun toString(): String {
        val estadoCosto = if (esNivelMaximo) "MAX" else "$$costo"
        return "Mejora(id='$id', nombre='$nombre', nivel=$nivel/$nivelMaximo, costo=$estadoCosto, efecto=$efectoMejora)"
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
    id: String, nombre: String, descripcion: String, nivelInicial: Int = 1, nivelMaximo: Int = 5,
    efecto: EfectoMejora, estrategiaCosto: EstrategiaCosto
) : Mejora(id, nombre, descripcion, nivelInicial, nivelMaximo, efecto, estrategiaCosto)

/**
 * Especialización tipada para mejoras orientadas a incrementar la velocidad de ascenso vertical.
 */
class MejoraVelocidadAscenso(
    id: String, nombre: String, descripcion: String, nivelInicial: Int = 1, nivelMaximo: Int = 5,
    efecto: EfectoMejora, estrategiaCosto: EstrategiaCosto
) : Mejora(id, nombre, descripcion, nivelInicial, nivelMaximo, efecto, estrategiaCosto)

/**
 * Especialización tipada para mejoras orientadas a reforzar la resistencia del casco contra la presión.
 */
class MejoraResistenciaCasco(
    id: String, nombre: String, descripcion: String, nivelInicial: Int = 1, nivelMaximo: Int = 5,
    efecto: EfectoMejora, estrategiaCosto: EstrategiaCosto
) : Mejora(id, nombre, descripcion, nivelInicial, nivelMaximo, efecto, estrategiaCosto)

/**
 * Especialización tipada para mejoras orientadas a incrementar la propulsión y navegación horizontal.
 */
class MejoraVelocidadPropulsion(
    id: String, nombre: String, descripcion: String, nivelInicial: Int = 1, nivelMaximo: Int = 5,
    efecto: EfectoMejora, estrategiaCosto: EstrategiaCosto
) : Mejora(id, nombre, descripcion, nivelInicial, nivelMaximo, efecto, estrategiaCosto)
