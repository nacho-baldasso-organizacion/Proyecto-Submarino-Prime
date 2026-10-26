package submarino

/**
 * Componente especializado responsable de las características físicas y comerciales del recurso (SRP).
 */
class ClasificacionRecurso(
    tipo: TipoMineral
) {
    var tipo: TipoMineral = tipo
        private set

    val nombre: String
        get() = tipo.nombreMineral

    val peso: Double
        get() = tipo.peso

    val valor: Int
        get() = tipo.valor

    val zona: String
        get() = tipo.zona

    fun esPesado(): Boolean = peso >= 3.0

    fun esRentable(umbralRatio: Double = 20.0): Boolean = tipo.ratioValorPeso() >= umbralRatio
}

/**
 * Componente especializado en el ciclo de vida y estado de recolección de un ítem (SRP).
 */
class EstadoRecoleccionMineral(
    inicialmenteRecolectado: Boolean = false
) {
    var estaRecolectado: Boolean = inicialmenteRecolectado
        private set

    fun marcarRecolectado(): Boolean {
        if (estaRecolectado) return false
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

    override var id: String = id
        private set

    override var posX: Int = posX
        private set

    override var posY: Int = posY
        private set

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
