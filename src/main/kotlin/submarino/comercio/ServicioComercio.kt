package submarino

/**
 * ============================================================================
 * PROCESO TRANSACCIONAL Y COMERCIO EN SUPERFICIE (ISSUE #7)
 * PRINCIPIOS SOLID (SRP, OCP, LSP, DIP, ISP)
 * ============================================================================
 */

/**
 * Recibo inmutable que representa una transacción comercial concretada exitosamente.
 * Encapsulado con 'private set' y enriquecido con lógica de negocio (no anémico).
 */
class TransaccionComercial(
    itemsVendidos: List<Vendible>,
    totalCreditos: Int,
    cantidadItems: Int,
    timestamp: Long = System.currentTimeMillis()
) {
    var itemsVendidos: List<Vendible> = itemsVendidos
        private set

    var totalCreditos: Int = totalCreditos
        private set

    var cantidadItems: Int = cantidadItems
        private set

    var timestamp: Long = timestamp
        private set

    /**
     * Calcula el promedio de créditos obtenidos por cada unidad vendida.
     */
    fun valorPromedioPorItem(): Double =
        if (cantidadItems > 0) totalCreditos.toDouble() / cantidadItems else 0.0

    /**
     * Evalúa si la transacción supera un umbral de valor representativo.
     */
    fun esTransaccionValiosa(umbral: Int = 500): Boolean = totalCreditos >= umbral

    /**
     * Retorna una síntesis textual del comprobante de venta.
     */
    fun resumen(): String =
        "Transacción: $cantidadItems ítems por $$totalCreditos créditos (Promedio: $${"%.2f".format(valorPromedioPorItem())}/ítem)."

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TransaccionComercial) return false
        return totalCreditos == other.totalCreditos &&
                cantidadItems == other.cantidadItems &&
                itemsVendidos.size == other.itemsVendidos.size
    }

    override fun hashCode(): Int = 31 * totalCreditos + cantidadItems
}

/**
 * Jerarquía sellada exhaustiva que modela los posibles desenlaces de un intento de transacción.
 * Provee métodos polimórficos para evitar comprobaciones de tipo `is`/`when` en clientes (OCP).
 */
sealed class ResultadoComercio {

    abstract val esExitoso: Boolean
    abstract fun obtenerResumen(): String

    /**
     * La transacción se completó con éxito, transfiriendo los fondos y vaciando el inventario.
     */
    class Exito(
        transaccion: TransaccionComercial,
        saldoActualizado: Int
    ) : ResultadoComercio() {
        var transaccion: TransaccionComercial = transaccion
            private set

        var saldoActualizado: Int = saldoActualizado
            private set

        override val esExitoso: Boolean = true
        override fun obtenerResumen(): String =
            "Venta concretada: ${transaccion.cantidadItems} ítems comercializados por $${transaccion.totalCreditos}. Saldo actualizado: $$saldoActualizado."

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Exito) return false
            return transaccion == other.transaccion && saldoActualizado == other.saldoActualizado
        }

        override fun hashCode(): Int = 31 * transaccion.hashCode() + saldoActualizado
    }

    /**
     * El intento de venta fue rechazado debido a que la nave no se encuentra amarrada/acoplada en la estación.
     */
    data object NaveNoAcoplada : ResultadoComercio() {
        override val esExitoso: Boolean = false
        override fun obtenerResumen(): String =
            "Operación rechazada: la nave debe encontrarse amarrada en la estación de acople."
    }

    /**
     * El intento de venta fue rechazado porque el compartimento de carga no contiene ningún ítem comercializable.
     */
    data object BodegaVacia : ResultadoComercio() {
        override val esExitoso: Boolean = false
        override fun obtenerResumen(): String =
            "Operación rechazada: no hay recursos comercializables en el compartimento de carga."
    }
}

/**
 * Implementación de [ObservadorComercio] para impresión en consola estándar.
 */
class ObservadorComercioConsola : ObservadorComercio {
    override fun alRealizarVenta(transaccion: TransaccionComercial) {
        println("[COMERCIO] Venta efectuada: ${transaccion.cantidadItems} ítems por $${transaccion.totalCreditos} créditos.")
    }
}

/**
 * Implementación silenciada de [ObservadorComercio] para pruebas unitarias y modo headless.
 * Almacena el historial transaccional en memoria en lugar de descartarlo con un bloque vacío (LSP).
 */
class ObservadorComercioSilencioso : ObservadorComercio {
    private val historialTransacciones: MutableList<TransaccionComercial> = mutableListOf()

    override fun alRealizarVenta(transaccion: TransaccionComercial) {
        historialTransacciones.add(transaccion)
    }

    val transacciones: List<TransaccionComercial>
        get() = historialTransacciones.toList()

    fun limpiar() {
        historialTransacciones.clear()
    }
}

/**
 * Servicio coordinador del proceso transaccional y comercial en la estación de acople.
 *
 * Cumple con:
 * - Single Responsibility Principle (SRP)
 * - Dependency Inversion Principle (DIP): implementa [ServicioComercioContrato] y depende de abstracciones
 * - Open/Closed Principle (OCP)
 * - Liskov Substitution Principle (LSP)
 * - Encapsulación estricta
 */
class ServicioComercio(
    private val calculadoraPrecios: CalculadoraPrecios = CalculadoraPreciosEstandar()
) : ServicioComercioContrato {

    private val observadores: MutableList<ObservadorComercio> = mutableListOf()

    override fun registrarObservador(observador: ObservadorComercio) {
        observadores.add(observador)
    }

    override fun eliminarObservador(observador: ObservadorComercio) {
        observadores.remove(observador)
    }

    override fun venderBodega(
        acoplable: Acoplable,
        almacenamiento: Almacenamiento,
        receptorFondos: ReceptorFondos
    ): ResultadoComercio {
        if (!acoplable.estaAcoplado) {
            return ResultadoComercio.NaveNoAcoplada
        }

        val items = almacenamiento.obtenerElementos()
        if (items.isEmpty()) {
            return ResultadoComercio.BodegaVacia
        }

        val totalCreditos = calculadoraPrecios.calcularTotal(items)
        receptorFondos.acreditar(totalCreditos)
        almacenamiento.vaciar()

        val transaccion = TransaccionComercial(
            itemsVendidos = items,
            totalCreditos = totalCreditos,
            cantidadItems = items.size
        )

        observadores.forEach { it.alRealizarVenta(transaccion) }

        return ResultadoComercio.Exito(
            transaccion = transaccion,
            saldoActualizado = receptorFondos.saldo
        )
    }

    override fun venderBodegaSubmarino(submarino: Submarino): ResultadoComercio {
        if (!submarino.estaAcoplado) {
            return ResultadoComercio.NaveNoAcoplada
        }
        val bodega = submarino.bodega ?: return ResultadoComercio.BodegaVacia
        return venderBodega(
            acoplable = submarino,
            almacenamiento = bodega,
            receptorFondos = submarino.billetera
        )
    }
}
