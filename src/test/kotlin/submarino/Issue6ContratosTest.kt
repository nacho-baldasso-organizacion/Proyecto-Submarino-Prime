package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class Issue6ContratosTest {

    @Test
    fun testSegregacionDeInterfacesISPConPesableYCargable() {
        val mineral: Cargable = Mineral("M1", TipoMineral.NODULO_DE_MANGANESO, 0, 0)
        val submarino: Pesable = Submarino(
            casco = ModuloCasco(500_000.0),
            pesoBase = 800.0,
            bodega = null,
            notificador = NotificadorSilencioso
        )

        val objetosPesables: List<Pesable> = listOf(mineral, submarino)
        val pesoTotalCombinado = objetosPesables.sumOf { it.peso }

        assertEquals(4.0, mineral.peso, 0.001)
        assertEquals(800.0, submarino.peso, 0.001)
        assertEquals(804.0, pesoTotalCombinado, 0.001)
    }

    @Test
    fun testValidacionDeInvariantesDePesoBaseYVelocidad() {
        assertFailsWith<IllegalArgumentException> {
            Submarino(
                casco = ModuloCasco(500_000.0),
                pesoBase = -10.0,
                notificador = NotificadorSilencioso
            )
        }

        assertFailsWith<IllegalArgumentException> {
            Submarino(
                casco = ModuloCasco(500_000.0),
                velocidadAscensoBase = -5.0,
                notificador = NotificadorSilencioso
            )
        }

        assertFailsWith<IllegalArgumentException> {
            EstrategiaAscensoLineal(factorReduccionMaximo = 1.5)
        }

        assertFailsWith<IllegalArgumentException> {
            EstrategiaAscensoLineal(factorReduccionMaximo = -0.1)
        }
    }
}
