package submarino

/**
 * Jerarquía sellada que modela exhaustivamente los posibles resultados de un intento de recolección.
 * Provee polimorfismo de estado para evitar verificaciones externas mediante switch/is (OCP).
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
     * El ítem colisionó y fue ingresado exitosamente en la bodega.
     */
    data class Exito(
        val item: Recolectable,
        val pesoAcumulado: Double
    ) : ResultadoRecoleccion() {
        override val esExito: Boolean = true
        override fun obtenerMensaje(): String =
            "Recolección completada: ${item.nombre} (ID: ${item.id}). Peso en bodega: ${pesoAcumulado}kg."
    }

    /**
     * Se detectó colisión con un ítem, pero la bodega no tiene capacidad de peso suficiente.
     */
    data class BodegaLlena(
        val item: Recolectable,
        val excesoPeso: Double,
        val capacidadMaxima: Double
    ) : ResultadoRecoleccion() {
        override val esExito: Boolean = false
        override fun obtenerMensaje(): String =
            "Bodega saturada: no se puede cargar ${item.nombre} (${item.peso}kg). Exceso: ${String.format("%.2f", excesoPeso)}kg sobre el límite de ${capacidadMaxima}kg."
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
    data class YaRecolectado(
        val item: Recolectable
    ) : ResultadoRecoleccion() {
        override val esExito: Boolean = false
        override fun obtenerMensaje(): String =
            "El ítem ${item.nombre} ya fue recolectado previamente."
    }
}

