package submarino

/**
 * ============================================================================
 * CONTRATOS E INTERFACES DEL SISTEMA DE UPGRADES / MEJORAS (ISSUE #8)
 * PRINCIPIOS SOLID (ISP = 0 violaciones, DIP = 0 violaciones, OCP = 0 violaciones)
 *
 * Cada interfaz posee un propósito estrictamente delimitado con 1 o 2 métodos
 * declarados como máximo, cumpliendo con la Segregación de Interfaces (ISP).
 * ============================================================================
 */

/**
 * Segregación de Interfaces (ISP):
 * Contrato para cualquier entidad o ítem del sistema que posea un identificador único.
 */
interface Identificable {
    val id: String
}

/**
 * Segregación de Interfaces (ISP):
 * Contrato descriptivo con nombre y descripción textual legible.
 */
interface Descriptible {
    val nombre: String
    val descripcion: String
}

/**
 * Segregación de Interfaces (ISP):
 * Contrato para bienes u operaciones que poseen un precio o costo en monedas/créditos.
 */
interface Comprable {
    val costo: Int
}

/**
 * Segregación de Interfaces (ISP):
 * Contrato para entidades susceptibles de incrementar su nivel de efectividad.
 */
interface Mejorable {
    val nivel: Int
    fun subirNivel(): Boolean
}

/**
 * Segregación de Interfaces (ISP):
 * Contrato de consulta sobre el tope o límite máximo de nivel de una mejora.
 */
interface LimiteNivel {
    val nivelMaximo: Int
    val esNivelMaximo: Boolean
}

/**
 * Principio Abierto/Cerrado (OCP) y Segregación de Interfaces (ISP):
 * Abstracción que representa la capacidad de aplicar un efecto o modificación sobre el submarino.
 */
interface AplicableSubmarino {
    fun aplicar(submarino: Submarino)
}

/**
 * Segregación de Interfaces (ISP) y Principio Abierto/Cerrado (OCP):
 * Abstracción de un efecto concreto de mejora con su respectiva magnitud cuantificable.
 */
interface EfectoMejora : AplicableSubmarino {
    val magnitud: Double
}

/**
 * Segregación de Interfaces (ISP) y Principio Abierto/Cerrado (OCP):
 * Contrato para algoritmos y modelos de cálculo de escalabilidad de costos por nivel.
 */
interface EstrategiaCosto {
    fun calcularCosto(nivelActual: Int): Int
}

/**
 * Segregación de Interfaces (ISP):
 * Contrato de consulta y recuperación de mejoras registradas en el catálogo.
 */
interface ConsultableCatalogo {
    fun buscarPorId(id: String): Mejora?
    fun obtenerTodas(): List<Mejora>
}

/**
 * Segregación de Interfaces (ISP):
 * Contrato para administración y registro de mejoras en el catálogo.
 */
interface GestionableCatalogo {
    fun registrar(mejora: Mejora): Boolean
    fun eliminar(id: String): Boolean
}

/**
 * Segregación de Interfaces (ISP):
 * Contrato para la orquestación y procesamiento de transacciones de compra de mejoras.
 */
interface ProcesadorCompras {
    fun comprarMejora(idMejora: String, pagable: Pagable, submarino: Submarino): ResultadoCompra
}

/**
 * Segregación de Interfaces (ISP) y Patrón Observer:
 * Notificador de eventos emitidos por la tienda de mejoras para desacoplamiento total con la UI.
 */
interface ObservadorTienda {
    fun alComprarMejora(mejora: Mejora, saldoRestante: Int)
    fun alRechazarCompra(idMejora: String, motivo: String)
}

/**
 * Segregación de Interfaces (ISP):
 * Contrato para suscripción y desuscripción de observadores de la tienda.
 */
interface GestorObservadoresTienda {
    fun registrarObservador(observador: ObservadorTienda)
    fun eliminarObservador(observador: ObservadorTienda)
}

/**
 * Segregación de Interfaces (ISP):
 * Contrato principal de fachada pública expuesta por la tienda de mejoras.
 */
interface TiendaMejorasContrato {
    fun comprar(idMejora: String, pagable: Pagable, submarino: Submarino): ResultadoCompra
    fun listarMejoras(): List<Mejora>
}

/**
 * Jerarquía sellada exhaustiva que modela los resultados posibles de una compra de mejora.
 * Garantiza cumplimiento con Liskov Substitution Principle (LSP) evitando excepciones no controladas.
 */
sealed class ResultadoCompra {
    /**
     * Transacción de compra concretada exitosamente.
     *
     * @property mejora La instancia de la mejora adquirida.
     * @property nivelAnterior Nivel previo antes de la compra.
     * @property nuevoNivel Nivel alcanzado tras la compra.
     * @property costoPagado Créditos debitados del pagable.
     * @property saldoRestante Saldo remanente final en la billetera/monedero.
     */
    data class Exito(
        val mejora: Mejora,
        val nivelAnterior: Int,
        val nuevoNivel: Int,
        val costoPagado: Int,
        val saldoRestante: Int
    ) : ResultadoCompra()

    /**
     * Compra rechazada debido a que los fondos en la billetera no cubren el costo requerido.
     *
     * @property mejoraId Identificador de la mejora pretendida.
     * @property costoRequerido Costo necesario para efectuar la compra.
     * @property saldoDisponible Créditos actuales en el monedero/billetera.
     */
    data class FondosInsuficientes(
        val mejoraId: String,
        val costoRequerido: Int,
        val saldoDisponible: Int
    ) : ResultadoCompra()

    /**
     * Compra rechazada debido a que la mejora ya alcanzó su nivel máximo permitido (cap).
     *
     * @property mejoraId Identificador de la mejora.
     * @property nivelActual Nivel actual en el que se encuentra la mejora.
     * @property nivelMaximo Nivel tope configurado.
     */
    data class NivelMaximoAlcanzado(
        val mejoraId: String,
        val nivelActual: Int,
        val nivelMaximo: Int
    ) : ResultadoCompra()

    /**
     * Compra rechazada debido a que el identificador no existe en el catálogo.
     *
     * @property mejoraId Identificador inexistente buscado.
     */
    data class MejoraNoEncontrada(
        val mejoraId: String
    ) : ResultadoCompra()
}
