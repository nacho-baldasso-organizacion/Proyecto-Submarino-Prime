package submarino

/**
 * Jerarquía sellada que modela exhaustivamente los posibles resultados de un intento de recolección.
 * Provee polimorfismo de estado para evitar verificaciones externas mediante switch/is (OCP).
 * Incorpora métodos de dominio con comportamiento propio (elimina clases anémicas).
 */
sealed class ResultadoRecoleccion {

    /**
     * Indica si la operación de recolección fue exitosa y el ítem ingresó en la bodega.
     */
    abstract val esExito: Boolean

    /**
     * Retorna un mensaje descriptivo legible del resultado.
     */
    abstract fun obtenerMensaje(): String

    /**
     * Retorna el elemento involucrado en la interacción si existe.
     */
    open fun obtenerItem(): Recolectable? = null

    /**
     * Indica si el resultado requiere que el jugador vacíe o amplíe la bodega.
     */
    open fun requiereLiberarEspacio(): Boolean = false

    /**
     * El ítem colisionó y fue ingresado exitosamente en la bodega.
     */
    class Exito(
        item: Recolectable,
        pesoAcumulado: Double
    ) : ResultadoRecoleccion() {
        var item: Recolectable = item
            private set

        var pesoAcumulado: Double = pesoAcumulado
            private set

        override val esExito: Boolean = true

        override fun obtenerItem(): Recolectable = item

        override fun obtenerMensaje(): String =
            "Recolección completada: ${item.nombre} (ID: ${item.id}). Peso en bodega: ${pesoAcumulado}kg."

        fun porcentajeOcupado(capacidadMaxima: Double): Double =
            if (capacidadMaxima > 0.0) (pesoAcumulado / capacidadMaxima) * 100.0 else 0.0

        fun espacioRemanente(capacidadMaxima: Double): Double =
            maxOf(capacidadMaxima - pesoAcumulado, 0.0)

        override fun toString(): String =
            "Exito(item=$item, pesoAcumulado=$pesoAcumulado)"

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Exito) return false
            return item.id == other.item.id && pesoAcumulado == other.pesoAcumulado
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
    ) : ResultadoRecoleccion() {
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

        fun pesoFaltante(): Double = excesoPeso

        override fun toString(): String =
            "BodegaLlena(item=$item, excesoPeso=$excesoPeso, capacidadMaxima=$capacidadMaxima)"

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is BodegaLlena) return false
            return item.id == other.item.id && excesoPeso == other.excesoPeso && capacidadMaxima == other.capacidadMaxima
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
    data object SinColision : ResultadoRecoleccion() {
        override val esExito: Boolean = false
        override fun obtenerMensaje(): String =
            "Sin colisión en las coordenadas actuales."
    }

    /**
     * El ítem en la posición indicada ya fue recolectado con anterioridad.
     */
    class YaRecolectado(
        item: Recolectable
    ) : ResultadoRecoleccion() {
        var item: Recolectable = item
            private set

        override val esExito: Boolean = false

        override fun obtenerItem(): Recolectable = item

        override fun obtenerMensaje(): String =
            "El ítem ${item.nombre} ya fue recolectado previamente."

        override fun toString(): String =
            "YaRecolectado(item=$item)"

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is YaRecolectado) return false
            return item.id == other.item.id
        }

        override fun hashCode(): Int = item.hashCode()
    }
}
