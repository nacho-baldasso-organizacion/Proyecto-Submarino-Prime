package submarino

/**
 * ============================================================================
 * GESTIÓN DE FONDOS Y BILLETERA DIGITAL (ISSUE #7)
 * PRINCIPIOS SOLID:
 * - Single Responsibility Principle (SRP = 0 violaciones): se encarga exclusivamente
 *   de administrar el saldo monetario, créditos y transacciones de débito.
 * - Liskov Substitution Principle (LSP = 0 violaciones): contratos limpios sin
 *   casteos ni excepciones no controladas.
 * - Interface Segregation Principle (ISP = 0 violaciones): implementa [Billetera],
 *   [ReceptorFondos], [Pagable] y [ConsultableSaldo].
 * ============================================================================
 */

/**
 * Implementación de monedero/billetera para el submarino o jugador.
 * Administra el saldo de créditos con validación de invariantes de no negatividad.
 *
 * @param saldoInicial Saldo monetario con el que inicia la billetera (debe ser >= 0).
 */
class Monedero(
    saldoInicial: Int = 0
) : Billetera {

    init {
        require(saldoInicial >= 0) {
            "El saldo inicial no puede ser negativo: $saldoInicial"
        }
    }

    override var saldo: Int = saldoInicial
        private set

    /**
     * Incrementa los fondos disponibles sumando el monto indicado.
     *
     * @param monto Cantidad de créditos a ingresar (debe ser >= 0).
     */
    override fun acreditar(monto: Int) {
        require(monto >= 0) {
            "El monto a acreditar no puede ser negativo: $monto"
        }
        saldo += monto
    }

    /**
     * Intenta debitar una cantidad de fondos del saldo actual.
     *
     * @param monto Cantidad de créditos a descontar (debe ser >= 0).
     * @return `true` si el saldo fue suficiente y la operación se concretó; `false` si no alcanza.
     */
    override fun debitar(monto: Int): Boolean {
        require(monto >= 0) {
            "El monto a debitar no puede ser negativo: $monto"
        }
        if (monto <= saldo) {
            saldo -= monto
            return true
        }
        return false
    }

    override fun toString(): String = "Monedero(saldo=$$saldo)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ConsultableSaldo) return false
        return saldo == other.saldo
    }

    override fun hashCode(): Int = saldo.hashCode()
}
