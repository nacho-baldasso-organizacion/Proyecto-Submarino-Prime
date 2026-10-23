package submarino

/**
 * Jerarquía sellada exhaustiva que modela los resultados posibles de una compra de mejora.
 * Garantiza cumplimiento con Liskov Substitution Principle (LSP) y provee métodos de dominio polimórficos.
 */
sealed class ResultadoCompra {

    abstract val esExitosa: Boolean
    abstract fun obtenerMensaje(): String

    /**
     * Transacción de compra concretada exitosamente.
     */
    data class Exito(
        val mejora: Mejora,
        val nivelAnterior: Int,
        val nuevoNivel: Int,
        val costoPagado: Int,
        val saldoRestante: Int
    ) : ResultadoCompra() {
        override val esExitosa: Boolean = true
        override fun obtenerMensaje(): String =
            "Mejora '${mejora.nombre}' adquirida exitosamente: nivel $nivelAnterior -> $nuevoNivel por $$costoPagado. Saldo restante: $$saldoRestante."
    }

    /**
     * Compra rechazada debido a que los fondos en la billetera no cubren el costo requerido.
     */
    data class FondosInsuficientes(
        val mejoraId: String,
        val costoRequerido: Int,
        val saldoDisponible: Int
    ) : ResultadoCompra() {
        override val esExitosa: Boolean = false
        override fun obtenerMensaje(): String =
            "Fondos insuficientes para adquirir la mejora '$mejoraId'. Requiere $$costoRequerido, disponible $$saldoDisponible."
    }

    /**
     * Compra rechazada debido a que la mejora ya alcanzó su nivel máximo permitido.
     */
    data class NivelMaximoAlcanzado(
        val mejoraId: String,
        val nivelActual: Int,
        val nivelMaximo: Int
    ) : ResultadoCompra() {
        override val esExitosa: Boolean = false
        override fun obtenerMensaje(): String =
            "La mejora '$mejoraId' ya alcanzó su nivel máximo ($nivelActual/$nivelMaximo)."
    }

    /**
     * Compra rechazada debido a que el identificador no existe en el catálogo.
     */
    data class MejoraNoEncontrada(
        val mejoraId: String
    ) : ResultadoCompra() {
        override val esExitosa: Boolean = false
        override fun obtenerMensaje(): String =
            "La mejora con identificador '$mejoraId' no existe en el catálogo."
    }
}

