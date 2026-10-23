package submarino

/**
 * ============================================================================
 * PROCESO TRANSACCIONAL Y COMERCIO EN SUPERFICIE (ISSUE #7)
 * PRINCIPIOS SOLID (SRP, OCP, LSP, DIP, ISP)
 * ============================================================================
 */

/**
 * Recibo inmutable que representa una transacción comercial concretada exitosamente.
 */
data class TransaccionComercial(
    val itemsVendidos: List<Vendible>,
    val totalCreditos: Int,
    val cantidadItems: Int,
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * Calcula el promedio de créditos obtenidos por cada unidad vendida.
     */
    fun valorPromedioPorItem(): Double =
        if (cantidadItems > 0) totalCreditos.toDouble() / cantidadItems else 0.0

    /**
     * Retorna una síntesis textual del comprobante de venta.
     */
    fun resumen(): String =
        "Transacción: $cantidadItems ítems por $$totalCreditos créditos (Promedio: $${"%.2f".format(valorPromedioPorItem())}/ítem)."
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
    data class Exito(
        val transaccion: TransaccionComercial,
        val saldoActualizado: Int
    ) : ResultadoComercio() {
        override val esExitoso: Boolean = true
        override fun obtenerResumen(): String =
            "Venta concretada: ${transaccion.cantidadItems} ítems comercializados por $${transaccion.totalCreditos}. Saldo actualizado: $$saldoActualizado."
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
 * - Dependency Inversion Principle (DIP)
 * - Open/Closed Principle (OCP)
 * - Liskov Substitution Principle (LSP)
 */
class ServicioComercio(
    private val calculadoraPrecios: CalculadoraPrecios = CalculadoraPreciosEstandar()
) : ProcesadorComercio {

    private val observadores: MutableList<ObservadorComercio> = mutableListOf()

    fun registrarObservador(observador: ObservadorComercio) {
        observadores.add(observador)
    }

    fun eliminarObservador(observador: ObservadorComercio) {
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

    fun venderBodegaSubmarino(submarino: Submarino): ResultadoComercio {
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
