package submarino

/**
 * Mixin para operaciones relacionadas con el ítem.
 * Provee implementación por defecto que retorna null.
 */
interface ConResultadoItem {
    fun obtenerItem(): Recolectable? = null
}

/**
 * Mixin para requisitos de espacio de bodega.
 * Por defecto indica que no se requiere liberar espacio.
 */
interface ConRequisitoEspacio {
    fun requiereLiberarEspacio(): Boolean = false
}

/**
 * Cálculos específicos a resultados de recolección exitosa.
 */
interface CalculosRecoleccion {
    fun porcentajeOcupado(capacidadMaxima: Double): Double
    fun espacioRemanente(capacidadMaxima: Double): Double
}

/**
 * Núcleo mínimo de la jerarquía sellada.
 * Los clientes dependiendo solo de esta interfaz no se ven obligados a
 * depender de preocupaciones de item o espacio que no usan.
 */
interface ResultadoRecoleccionBase {
    val esExito: Boolean
    fun obtenerMensaje(): String
}

/**
 * Jerarquía sellada que modela exhaustivamente los posibles resultados de un intento de recolección.
 * Provee polimorfismo de estado para evitar verificaciones externas de tipo (OCP).
 * Incorpora métodos de dominio con comportamiento propio (elimina clases anémicas).
 * Ahora segregado en interfaces más pequeñas y cohesivas (Principio ISP).
 */
public sealed interface ResultadoRecoleccion : ResultadoRecoleccionBase, ConResultadoItem, ConRequisitoEspacio {

    /**
     * Retorna el mensaje orientado al jugador. Por defecto coincide con [obtenerMensaje],
     * pero cada resultado puede adaptarlo sin que el cliente consulte su tipo (OCP).
     */
    fun obtenerMensajeJugador(): String = obtenerMensaje()

    /**
     * El ítem colisionó y fue ingresado exitosamente en la bodega.
     */
    class Exito(
        item: Recolectable,
        pesoAcumulado: Double
    ) : ResultadoRecoleccion, CalculosRecoleccion {
        var item: Recolectable = item
            private set

        var pesoAcumulado: Double = pesoAcumulado
            private set

        override val esExito: Boolean = true

        override fun obtenerItem(): Recolectable = item

        override fun obtenerMensaje(): String =
            "Recolección completada: ${item.nombre} (ID: ${item.id}). Peso en bodega: ${pesoAcumulado}kg."

        override fun obtenerMensajeJugador(): String
            = obtenerMensaje()

override fun porcentajeOcupado(capacidadMaxima: Double): Double
        = if (capacidadMaxima > 0.0) (pesoAcumulado / capacidadMaxima) * 100.0 else 0.0

    override fun espacioRemanente(capacidadMaxima: Double): Double
        = maxOf(capacidadMaxima - pesoAcumulado, 0.0)

        override fun toString(): String =
            "Exito(item=$item, pesoAcumulado=$pesoAcumulado)"

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            val otro = other as? Exito ?: return false
            return item.id == otro.item.id && pesoAcumulado == otro.pesoAcumulado
        }

        override fun hashCode(): Int = 31 * item.hashCode() + pesoAcumulado.hashCode()
    }

    /**
     * Se detectó colisión con un ítem, pero la bodega no tiene capacidad de peso suficiente.
     */
    class BodegaLlena(
        item: Recolectable,
        excesoPeso: Double,
        capacidadMaxima: Double
    ) : ResultadoRecoleccion {
        var item: Recolectable = item
            private set

        var excesoPeso: Double = excesoPeso
            private set

        var capacidadMaxima: Double = capacidadMaxima
            private set

        override val esExito: Boolean = false

        override fun obtenerItem(): Recolectable = item

        override fun requiereLiberarEspacio(): Boolean = true

        override fun obtenerMensaje(): String =
            "Bodega saturada: no se puede cargar ${item.nombre} (${item.peso}kg). Exceso: ${String.format("%.2f", excesoPeso)}kg sobre el límite de ${capacidadMaxima}kg."

        override fun obtenerMensajeJugador(): String
            = obtenerMensaje()

        fun pesoFaltante(): Double = excesoPeso

        override fun toString(): String =
            "BodegaLlena(item=$item, excesoPeso=$excesoPeso, capacidadMaxima=$capacidadMaxima)"

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            val otro = other as? BodegaLlena ?: return false
            return item.id == otro.item.id && excesoPeso == otro.excesoPeso && capacidadMaxima == otro.capacidadMaxima
        }

        override fun hashCode(): Int {
            var res = item.hashCode()
            res = 31 * res + excesoPeso.hashCode()
            res = 31 * res + capacidadMaxima.hashCode()
            return res
        }
    }

    /**
     * No existe ningún ítem colisionable en las coordenadas actuales.
     */
    data object SinColision : ResultadoRecoleccion {
        override val esExito: Boolean = false
        override fun obtenerMensaje(): String =
            "Sin colisión en las coordenadas actuales."

        override fun obtenerMensajeJugador(): String =
            "No hay minerales cerca. Acércate a un nodo y pulsa ESPACIO."
    }

    /**
     * El ítem en la posición indicada ya fue recolectado con anterioridad.
     */
    class YaRecolectado(
        item: Recolectable
    ) : ResultadoRecoleccion {
        var item: Recolectable = item
            private set

        override val esExito: Boolean = false

        override fun obtenerItem(): Recolectable = item

        override fun obtenerMensaje(): String =
            "El ítem ${item.nombre} ya fue recolectado previamente."

        override fun obtenerMensajeJugador(): String
            = obtenerMensaje()

        override fun toString(): String =
            "YaRecolectado(item=$item)"

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            val otro = other as? YaRecolectado ?: return false
            return item.id == otro.item.id
        }

        override fun hashCode(): Int = item.hashCode()
    }
}