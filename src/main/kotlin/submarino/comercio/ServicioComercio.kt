package submarino

/**
 * ============================================================================
 * PROCESO TRANSACCIONAL Y COMERCIO EN SUPERFICIE (ISSUE #7)
 * PRINCIPIOS SOLID (SRP, OCP, LSP, DIP, ISP)
 * ============================================================================
 */

/**
 * Recibo inmutable que representa una transacción comercial concretada exitosamente.
 *
 * @property itemsVendidos Lista inmutable de ítems que fueron comercializados.
 * @property totalCreditos Valor monetario total obtenido por la venta.
 * @property cantidadItems Número total de unidades vendidas.
 * @property timestamp Estampa temporal en milisegundos en la que se efectuó la transacción.
 */
data class TransaccionComercial(
    val itemsVendidos: List<Vendible>,
    val totalCreditos: Int,
    val cantidadItems: Int,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Jerarquía sellada exhaustiva que modela los posibles desenlaces de un intento de transacción.
 * Facilita el manejo exhaustivo con `when` sin riesgo de estados no controlados (LSP).
 */
sealed class ResultadoComercio {
    /**
     * La transacción se completó con éxito, transfiriendo los fondos y vaciando el inventario.
     *
     * @property transaccion Recibo inmutable con el desglose de la venta.
     * @property saldoActualizado Saldo remanente final en el receptor de fondos.
     */
    data class Exito(
        val transaccion: TransaccionComercial,
        val saldoActualizado: Int
    ) : ResultadoComercio()

    /**
     * El intento de venta fue rechazado debido a que la nave no se encuentra amarrada/acoplada en la estación.
     */
    object NaveNoAcoplada : ResultadoComercio()

    /**
     * El intento de venta fue rechazado porque el compartimento de carga no contiene ningún ítem comercializable.
     */
    object BodegaVacia : ResultadoComercio()
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
 * Implementación silenciosa de [ObservadorComercio] para ejecución en tests y modo headless.
 */
class ObservadorComercioSilencioso : ObservadorComercio {
    override fun alRealizarVenta(transaccion: TransaccionComercial) {
        // No-op intencional
    }
}

/**
 * Servicio coordinador del proceso transaccional y comercial en la estación de acople.
 *
 * Cumple con:
 * - Single Responsibility Principle (SRP): se dedica exclusivamente a coordinar la transacción
 *   económica (validar acople, valuar ítems, acreditar fondos, vaciar carga y notificar observadores).
 * - Dependency Inversion Principle (DIP): depende únicamente de contratos abstractos
 *   ([CalculadoraPrecios], [Acoplable], [Almacenamiento], [ReceptorFondos], [ObservadorComercio]).
 * - Open/Closed Principle (OCP): admite cualquier estrategia de precios y cualquier nuevo ítem vendible.
 * - Liskov Substitution Principle (LSP): manejo de resultados tipados y cero casteos inseguros.
 *
 * @param calculadoraPrecios Abstracción para el cómputo del valor comercial de los ítems.
 */
class ServicioComercio(
    val calculadoraPrecios: CalculadoraPrecios = CalculadoraPreciosEstandar()
) : ProcesadorComercio {

    private val observadores: MutableList<ObservadorComercio> = mutableListOf()

    /**
     * Registra un observador para desacoplamiento con interfaz gráfica o sistemas de registro (Observer).
     */
    fun registrarObservador(observador: ObservadorComercio) {
        observadores.add(observador)
    }

    /**
     * Desregistra un observador comercial.
     */
    fun eliminarObservador(observador: ObservadorComercio) {
        observadores.remove(observador)
    }

    /**
     * Ejecuta el proceso de venta de todos los ítems contenidos en el almacenamiento indicado:
     * 1. Verifica si la nave se encuentra amarrada/acoplada ([Acoplable.estaAcoplado]).
     *    Si no está acoplada, rechaza la operación informando [ResultadoComercio.NaveNoAcoplada].
     * 2. Obtiene los elementos del [Almacenamiento]. Si está vacío, retorna [ResultadoComercio.BodegaVacia].
     * 3. Calcula el valor total de los recursos mediante la [CalculadoraPrecios].
     * 4. Acredita la suma monetaria en el [ReceptorFondos].
     * 5. Vacía el inventario del [Almacenamiento].
     * 6. Emite un evento a los [ObservadorComercio] registrados.
     * 7. Retorna [ResultadoComercio.Exito] con el recibo inmutable y el nuevo balance.
     *
     * @param acoplable Abstracción de estado de amarre de la nave vendedora.
     * @param almacenamiento Contenedor o bodega de donde se extraen los minerales.
     * @param receptorFondos Billetera o cuenta donde se acreditan las ganancias.
     * @return [ResultadoComercio] describiendo el resultado de la transacción.
     */
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

    /**
     * Sobrecarga de conveniencia semántica que procesa la venta de la bodega del [Submarino].
     * Extrae automáticamente la bodega y la billetera del submarino sin acoplar la lógica central.
     *
     * @param submarino Instancia del submarino que realiza la venta.
     * @return [ResultadoComercio] con el resultado de la operación.
     */
    fun venderBodegaSubmarino(submarino: Submarino): ResultadoComercio {
        val bodega = submarino.bodega ?: return ResultadoComercio.BodegaVacia
        return venderBodega(
            acoplable = submarino,
            almacenamiento = bodega,
            receptorFondos = submarino.billetera
        )
    }
}
