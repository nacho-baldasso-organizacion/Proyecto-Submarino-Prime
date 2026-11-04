package submarino

/**
 * ============================================================================
 * CONTRATOS E INTERFACES DEL SISTEMA COMERCIAL Y ZONA DE ACOPLE (ISSUE #7)
 * PRINCIPIOS SOLID (ISP, DIP, OCP)
 *
 * Cada interfaz posee un propósito estrictamente delimitado con 1 o 2 métodos
 * declarados como máximo, cumpliendo con la Segregación de Interfaces (ISP).
 * ============================================================================
 */

/**
 * Segregación de Interfaces (ISP):
 * Contrato para cualquier recurso o bien de juego que posea valor monetarioizable.
 */
interface Vendible {
    val valor: Int

    /**
     * Indica si este bien posee un valor monetario estrictamente mayor que [otro].
     */
    fun esMasValiosoQue(otro: Vendible): Boolean = valor > otro.valor
}

/**
 * Segregación de Interfaces (ISP):
 * Contrato para entidades receptoras de transferencias monetarias o créditos.
 */
interface ReceptorFondos {
    val saldo: Int
    fun acreditar(monto: Int)
}

/**
 * Segregación de Interfaces (ISP):
 * Contrato para entidades desde las cuales se pueden debitar fondos o realizar pagos.
 */
interface Pagable {
    val saldo: Int
    fun debitar(monto: Int): Boolean
}

/**
 * Segregación de Interfaces (ISP):
 * Contrato elemental para consultar el saldo acumulado en una cuenta o monedero.
 */
interface ConsultableSaldo {
    val saldo: Int

    /**
     * Indica si el saldo disponible cubre el [monto] solicitado.
     */
    fun saldoSuficiente(monto: Int): Boolean = saldo >= monto
}

/**
 * Contrato compuesto que une recepción y débito de fondos para una billetera integral.
 */
interface Billetera : ReceptorFondos, Pagable, ConsultableSaldo

/**
 * Segregación de Interfaces (ISP):
 * Contrato de consulta del estado de acople de una entidad móvil o navegable.
 */
interface EstadoAcople {
    val estaAcoplado: Boolean

    /**
     * Indica si la entidad se encuentra actualmente desacoplada.
     */
    fun estaDesacoplado(): Boolean = !estaAcoplado
}

/**
 * Segregación de Interfaces (ISP):
 * Contrato para entidades susceptibles de acoplarse y desacoplarse en una estación o puerto.
 */
interface Acoplable : EstadoAcople {
    fun acoplar()
    fun desacoplar()
}

/**
 * Contrato de dominio que agrupa todo lo necesario para comerciar desde una nave:
 * estado de acople, compartimento de carga y receptor de fondos.
 *
 * Permite que [ServicioComercio] opere polimórficamente sin conocer la clase concreta
 * de la nave ni realizar comprobaciones de tipo o casteos manuales.
 */
interface NaveComercial : Acoplable {
    val bodega: Almacenamiento?
    val billetera: Billetera

    /**
     * Indica si la nave dispone de una bodega asignada.
     */
    fun tieneBodega(): Boolean = bodega != null

    /**
     * Determina si la nave puede vender: debe estar acoplada y tener carga disponible.
     */
    fun puedeVender(): Boolean = estaAcoplado && (bodega?.cantidadItems ?: 0) > 0

    /**
     * Determina si la nave puede afrontar un costo con los fondos de su billetera.
     */
    fun puedeComprar(costo: Int): Boolean = billetera.saldoSuficiente(costo)
}

/**
 * Segregación de Interfaces (ISP) y Principio Abierto/Cerrado (OCP):
 * Abstracción para delimitar áreas geográficas o batimétricas donde el acople es admitido.
 */
interface ZonaAcople {
    fun estaEnZona(x: Double, y: Double): Boolean
}

/**
 * Segregación de Interfaces (ISP) y Patrón Observer:
 * Notificador de eventos de acople y desacople de naves para desacoplar lógica y UI.
 */
interface ObservadorAcople {
    fun alAcoplar(nave: Acoplable)
    fun alDesacoplar(nave: Acoplable)
}

/**
 * Segregación de Interfaces (ISP) y Patrón Observer:
 * Notificador desacoplado de transacciones comerciales para futura interfaz de usuario.
 */
interface ObservadorComercio {
    fun alRealizarVenta(transaccion: TransaccionComercial)
}

/**
 * Segregación de Interfaces (ISP) y Principio Abierto/Cerrado (OCP):
 * Contrato de valoración individual de bienes vendibles.
 */
interface EstrategiaPrecios {
    fun calcularPrecio(item: Vendible): Int
}

/**
 * Segregación de Interfaces (ISP):
 * Contrato para cálculo unitario y tasación acumulada de lotes de recursos vendibles.
 */
interface CalculadoraPrecios {
    fun calcularPrecio(item: Vendible): Int
    fun calcularTotal(items: Collection<Vendible>): Int
}

/**
 * Segregación de Interfaces (ISP):
 * Contrato operativo para control de amarre y desamarre en la estación de acople.
 */
interface ControlAcople {
    fun acoplar(nave: Acoplable, x: Double, y: Double): Boolean
    fun desacoplar(): Boolean
}

/**
 * Segregación de Interfaces (ISP):
 * Contrato de consulta de disponibilidad y compatibilidad de acople en la estación.
 */
interface ConsultaAcople {
    val hayNaveAcoplada: Boolean
    fun puedeAcoplar(x: Double, y: Double): Boolean
}

/**
 * Segregación de Interfaces (ISP):
 * Contrato para el procesamiento de ventas de lotes de inventario/bodega.
 */
interface ProcesadorComercio {
    fun venderBodega(
        acoplable: Acoplable,
        almacenamiento: Almacenamiento,
        receptorFondos: ReceptorFondos
    ): ResultadoComercio
}

/**
 * Inversión de Dependencias (DIP):
 * Abstracción integral para el servicio coordinador de comercio en superficie.
 * Opera polimórficamente sobre la abstracción [NaveComercial], sin acoplarse a la clase concreta del submarino.
 */
interface ServicioComercioContrato : ProcesadorComercio {
    fun venderBodegaSubmarino(submarino: NaveComercial): ResultadoComercio
    fun registrarObservador(observador: ObservadorComercio)
    fun eliminarObservador(observador: ObservadorComercio)
}
