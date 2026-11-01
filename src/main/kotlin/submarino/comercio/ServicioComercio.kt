package submarino

/**
 * ============================================================================
 * PROCESO TRANSACCIONAL Y COMERCIO EN SUPERFICIE (ISSUE #7)
 * PRINCIPIOS SOLID (SRP, OCP, LSP, DIP, ISP)
 * ============================================================================
 */

/**
 * Recibo inmutable que representa una transacción comercial concretada exitosamente.
 * Al ser una data class, la igualdad y el hash se derivan del estado del recibo.
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
     * Evalúa si la transacción supera un umbral de valor representativo.
     */
    fun esTransaccionValiosa(umbral: Int = 500): Boolean = totalCreditos >= umbral

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

    private val transacciones: List<TransaccionComercial>
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
    private val calculadoraPrecios: CalculadoraPrecios
) : ServicioComercioContrato {

    /**
     * Constructor de conveniencia que configura la calculadora estándar de precios.
     * La lógica de negocio depende únicamente de la abstracción [CalculadoraPrecios].
     */
    constructor() : this(CalculadoraPreciosEstandar())

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
        validarAcople(acoplable)?.let { return it }

        val items = almacenamiento.obtenerElementos()
        validarBodega(items)?.let { return it }

        return concretarVenta(almacenamiento, receptorFondos, items)
    }

    override fun venderBodegaSubmarino(submarino: NaveComercial): ResultadoComercio =
        venderNaveComercial(submarino)

    /**
     * Punto de entrada polimórfico para cualquier nave que exponga su bodega y billetera.
     */
    fun venderNaveComercial(nave: NaveComercial): ResultadoComercio {
        validarAcople(nave)?.let { return it }

        val bodega = nave.bodega ?: return ResultadoComercio.BodegaVacia
        return venderBodega(
            acoplable = nave,
            almacenamiento = bodega,
            receptorFondos = nave.billetera
        )
    }

    private fun validarAcople(acoplable: Acoplable): ResultadoComercio? =
        if (acoplable.estaAcoplado) null else ResultadoComercio.NaveNoAcoplada

    private fun validarBodega(items: List<Vendible>): ResultadoComercio? =
        if (items.isEmpty()) ResultadoComercio.BodegaVacia else null

    private fun concretarVenta(
        almacenamiento: Almacenamiento,
        receptorFondos: ReceptorFondos,
        items: List<Vendible>
    ): ResultadoComercio.Exito {
        val totalCreditos = calculadoraPrecios.calcularTotal(items)
        receptorFondos.acreditar(totalCreditos)
        almacenamiento.vaciar()

        val transaccion = TransaccionComercial(
            itemsVendidos = items,
            totalCreditos = totalCreditos,
            cantidadItems = items.size
        )
        notificarObservadores(transaccion)

        return ResultadoComercio.Exito(
            transaccion = transaccion,
            saldoActualizado = receptorFondos.saldo
        )
    }

    private fun notificarObservadores(transaccion: TransaccionComercial) {
        observadores.forEach { it.alRealizarVenta(transaccion) }
    }
}
