package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Pruebas de billetera y gestión de fondos (SRP, ISP).
 */
class Issue7BilleteraTest {

    @Test
    fun `test monedero acredita y debita fondos correctamente`() {
        val monedero = Monedero(saldoInicial = 100)
        assertEquals(100, monedero.saldo)

        monedero.acreditar(50)
        assertEquals(150, monedero.saldo)

        // Débito exitoso
        assertTrue(monedero.debitar(70))
        assertEquals(80, monedero.saldo)

        // Débito de valor exacto
        assertTrue(monedero.debitar(80))
        assertEquals(0, monedero.saldo)

        // Débito insuficiente
        assertFalse(monedero.debitar(1))
        assertEquals(0, monedero.saldo)
    }

    @Test
    fun `test monedero valida invariantes de no negatividad`() {
        assertFailsWith<IllegalArgumentException> {
            Monedero(saldoInicial = -10)
        }

        val monedero = Monedero(saldoInicial = 0)
        assertFailsWith<IllegalArgumentException> {
            monedero.acreditar(-5)
        }

        assertFailsWith<IllegalArgumentException> {
            monedero.debitar(-20)
        }
    }

    @Test
    fun `test segregacion de interfaces ISP con billetera y monedero`() {
        val monedero: Billetera = Monedero(saldoInicial = 500)

        // Polimorfismo a través de ReceptorFondos
        val receptor: ReceptorFondos = monedero
        receptor.acreditar(200)
        assertEquals(700, receptor.saldo)

        // Polimorfismo a través de Pagable
        val pagable: Pagable = monedero
        assertTrue(pagable.debitar(300))
        assertEquals(400, pagable.saldo)

        // Polimorfismo a través de ConsultableSaldo
        val consultable: ConsultableSaldo = monedero
        assertEquals(400, consultable.saldo)
    }
}
