package submarino

/**
 * Jerarquía sellada exhaustiva que modela los resultados posibles de una compra de mejora.
 * Garantiza cumplimiento con Liskov Substitution Principle (LSP) y provee métodos de dominio polimórficos.
 * Elimina clases anémicas dotando de comportamiento de cálculo a los resultados.
 */
sealed interface ResultadoCompra {

    val esExitosa: Boolean
    fun obtenerMensaje(): String

    fun obtenerIdMejora(): String = ""
    fun saldoFinal(): Int = -1

    /**
     * Transacción de compra concretada exitosamente.
     */
    class Exito(
        mejora: Mejora,
        nivelAnterior: Int,
        nuevoNivel: Int,
        costoPagado: Int,
        saldoRestante: Int
    ) : ResultadoCompra {
        var mejora: Mejora = mejora
            private set

        var nivelAnterior: Int = nivelAnterior
            private set

        var nuevoNivel: Int = nuevoNivel
            private set

        var costoPagado: Int = costoPagado
            private set

        var saldoRestante: Int = saldoRestante
            private set

        override val esExitosa: Boolean = true

        override fun obtenerIdMejora(): String = mejora.id

        override fun saldoFinal(): Int = saldoRestante

        override fun obtenerMensaje(): String =
            "Mejora '${mejora.nombre}' adquirida exitosamente: nivel $nivelAnterior -> $nuevoNivel por $$costoPagado. Saldo restante: $$saldoRestante."

        fun incrementoNivel(): Int = nuevoNivel - nivelAnterior

        fun esNivelMaximo(): Boolean = nuevoNivel >= mejora.nivelMaximo()

        override fun toString(): String =
            "Exito(mejora=$mejora, nivelAnterior=$nivelAnterior, nuevoNivel=$nuevoNivel, costoPagado=$costoPagado, saldoRestante=$saldoRestante)"

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            val otro = other as? Exito ?: return false
            return mejora.id == otro.mejora.id && nuevoNivel == otro.nuevoNivel && costoPagado == otro.costoPagado
        }

        override fun hashCode(): Int = 31 * mejora.id.hashCode() + nuevoNivel
    }

    /**
     * Compra rechazada debido a que los fondos en la billetera no cubren el costo requerido.
     */
    class FondosInsuficientes(
        mejoraId: String,
        costoRequerido: Int,
        saldoDisponible: Int
    ) : ResultadoCompra {
        var mejoraId: String = mejoraId
            private set

        var costoRequerido: Int = costoRequerido
            private set

        var saldoDisponible: Int = saldoDisponible
            private set

        override val esExitosa: Boolean = false

        override fun obtenerIdMejora(): String = mejoraId

        override fun saldoFinal(): Int = saldoDisponible

        override fun obtenerMensaje(): String =
            "Fondos insuficientes para adquirir la mejora '$mejoraId'. Requiere $$costoRequerido, disponible $$saldoDisponible."

        fun deficit(): Int = maxOf(costoRequerido - saldoDisponible, 0)

        override fun toString(): String =
            "FondosInsuficientes(mejoraId=$mejoraId, costoRequerido=$costoRequerido, saldoDisponible=$saldoDisponible)"

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            val otro = other as? FondosInsuficientes ?: return false
            return mejoraId == otro.mejoraId && costoRequerido == otro.costoRequerido
        }

        override fun hashCode(): Int = 31 * mejoraId.hashCode() + costoRequerido
    }

    /**
     * Compra rechazada debido a que la mejora ya alcanzó su nivel máximo permitido.
     */
    class NivelMaximoAlcanzado(
        mejoraId: String,
        nivelActual: Int,
        nivelMaximo: Int
    ) : ResultadoCompra {
        var mejoraId: String = mejoraId
            private set

        var nivelActual: Int = nivelActual
            private set

        var nivelMaximo: Int = nivelMaximo
            private set

        override val esExitosa: Boolean = false

        override fun obtenerIdMejora(): String = mejoraId

        override fun obtenerMensaje(): String =
            "La mejora '$mejoraId' ya alcanzó su nivel máximo ($nivelActual/$nivelMaximo)."

        fun margenNivel(): Int = maxOf(nivelMaximo - nivelActual, 0)

        override fun toString(): String =
            "NivelMaximoAlcanzado(mejoraId=$mejoraId, nivelActual=$nivelActual, nivelMaximo=$nivelMaximo)"

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            val otro = other as? NivelMaximoAlcanzado ?: return false
            return mejoraId == otro.mejoraId && nivelActual == otro.nivelActual
        }

        override fun hashCode(): Int = 31 * mejoraId.hashCode() + nivelActual
    }

    /**
     * Compra rechazada debido a que el identificador no existe en el catálogo.
     */
    class MejoraNoEncontrada(
        mejoraId: String
    ) : ResultadoCompra {
        var mejoraId: String = mejoraId
            private set

        override val esExitosa: Boolean = false

        override fun obtenerIdMejora(): String = mejoraId

        override fun obtenerMensaje(): String =
            "La mejora con identificador '$mejoraId' no existe en el catálogo."

        override fun toString(): String =
            "MejoraNoEncontrada(mejoraId=$mejoraId)"

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            val otro = other as? MejoraNoEncontrada ?: return false
            return mejoraId == otro.mejoraId
        }

        override fun hashCode(): Int = mejoraId.hashCode()
    }
}
