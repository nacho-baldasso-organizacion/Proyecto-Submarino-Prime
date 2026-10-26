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
 */
interface ServicioComercioContrato : ProcesadorComercio {
    fun venderBodegaSubmarino(submarino: Submarino): ResultadoComercio
    fun registrarObservador(observador: ObservadorComercio)
    fun eliminarObservador(observador: ObservadorComercio)
}
