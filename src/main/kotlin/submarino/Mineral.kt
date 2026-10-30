package submarino

/**
 * Componente especializado responsable de las características físicas y comerciales del recurso (SRP).
 */
class ClasificacionRecurso(
    val tipo: TipoMineral
) {
    val nombre: String
        get() = tipo.nombreMineral

    val peso: Double
        get() = tipo.peso

    val valor: Int
        get() = tipo.valor

    val zona: String
        get() = tipo.zona

    /**
     * Valor comercial obtenido por cada kilogramo de recurso.
     */
    fun valorPorKilogramo(): Double = tipo.ratioValorPeso()

    /**
     * Determina si el recurso supera el umbral de valor económico indicado.
     */
    fun esValioso(umbral: Int = 100): Boolean = tipo.esValioso(umbral)

    /**
     * Indica si la profundidad dada corresponde a la franja natural de este recurso.
     */
    fun estaEnRangoProfundidad(profundidad: Double): Boolean = tipo.estaEnRango(profundidad)

    fun esPesado(): Boolean = peso >= UMBRAL_PESO_PESADO

    fun esRentable(umbralRatio: Double = 20.0): Boolean = valorPorKilogramo() >= umbralRatio

    companion object {
        const val UMBRAL_PESO_PESADO: Double = 3.0
    }
}

/**
 * Componente especializado en el ciclo de vida y estado de recolección de un ítem (SRP).
 */
class EstadoRecoleccionMineral(
    inicialmenteRecolectado: Boolean = false
) {
    var estaRecolectado: Boolean = inicialmenteRecolectado
        private set

    /**
     * Indica si el recurso aún puede ser recolectado.
     */
    fun estaDisponible(): Boolean = !estaRecolectado

    fun marcarRecolectado(): Boolean {
        if (!estaDisponible()) return false
        estaRecolectado = true
        return true
    }
}

/**
 * Representa una veta física o ejemplar de mineral colocado en una coordenada del mapa.
 * Compone [ClasificacionRecurso] y [EstadoRecoleccionMineral] cumpliendo con SRP.
 * Implementa [Recolectable] con encapsulación estricta de estado (campos con `private set`).
 */
class Mineral(
    id: String,
    tipo: TipoMineral,
    posX: Int,
    posY: Int,
    recolectado: Boolean = false
) : Recolectable {

    override val id: String = id

    override val posX: Int = posX

    override val posY: Int = posY

    private val clasificacion: ClasificacionRecurso = ClasificacionRecurso(tipo)
    private val estadoRecoleccion: EstadoRecoleccionMineral = EstadoRecoleccionMineral(recolectado)

    val tipo: TipoMineral
        get() = clasificacion.tipo

    override var recolectado: Boolean
        get() = estadoRecoleccion.estaRecolectado
        set(value) {
            if (value) {
                estadoRecoleccion.marcarRecolectado()
            }
        }

    override val nombre: String
        get() = clasificacion.nombre

    override val peso: Double
        get() = clasificacion.peso

    override val valor: Int
        get() = clasificacion.valor

    val zona: String
        get() = clasificacion.zona

    /**
     * Marca el ejemplar como recolectado modificando su estado interno.
     */
    override fun marcarRecolectado() {
        estadoRecoleccion.marcarRecolectado()
    }

    /**
     * Evalúa si el mineral corresponde a una clase pesada (>= 3.0 kg).
     */
    fun esPesado(): Boolean = clasificacion.esPesado()

    /**
     * Indica si el mineral aún no ha sido recolectado.
     */
    fun estaDisponibleParaRecoleccion(): Boolean = estadoRecoleccion.estaDisponible()

    /**
     * Indica si el mineral ocupa exactamente las coordenadas indicadas.
     */
    fun estaEnPosicion(posX: Int, posY: Int): Boolean = this.posX == posX && this.posY == posY

    /**
     * Indica si el mineral ocupa la misma posición que otra entidad posicionable.
     */
    fun estaEnPosicion(posicion: Posicionable): Boolean =
        estaEnPosicion(posicion.posX, posicion.posY)

    /**
     * Valor comercial obtenido por cada kilogramo de este mineral.
     */
    fun ratioValorPeso(): Double = clasificacion.valorPorKilogramo()

    /**
     * Determina si el mineral es rentable según el umbral de valor por kilogramo indicado.
     */
    fun esRentable(umbralRatio: Double = 20.0): Boolean = clasificacion.esRentable(umbralRatio)

    /**
     * Determina si el mineral supera el umbral de valor económico indicado.
     */
    fun esValioso(umbral: Int = 100): Boolean = clasificacion.esValioso(umbral)

    /**
     * Indica si la profundidad dada corresponde a la franja natural de este mineral.
     */
    fun estaEnRangoProfundidad(profundidad: Double): Boolean =
        clasificacion.estaEnRangoProfundidad(profundidad)

    fun obtenerPosicion(): Posicion = Posicion(posX, posY)

    fun obtenerTipo(): TipoMineral = tipo

    override fun toString(): String {
        return "Mineral(id='$id', nombre='$nombre', zona='$zona', peso=${peso}kg, valor=$$valor, pos=($posX, $posY), recolectado=$recolectado)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Mineral) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}
