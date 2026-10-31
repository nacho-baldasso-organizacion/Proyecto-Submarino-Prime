package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HUDPesoBodegaTest {

    private val hud = HUDView()

    @Test
    fun testRenderizarPesoBodega_valoresNormales() {
        val estado = EstadoHUD(0.0, 0.0, 0.0, 250.0, 500.0, 0)
        val resultado = hud.renderizarPesoBodega(estado)
        assertTrue(resultado.contains("250.0 / 500.0 kg"))
    }

    @Test
    fun testRenderizarPesoBodega_sinCapacidadMaxima() {
        val estado = EstadoHUD(0.0, 0.0, 0.0, 0.0, 0.0, 0)
        val resultado = hud.renderizarPesoBodega(estado)
        assertEquals("Peso bodega: 0.0 / 0.0 kg (0.0%)", resultado)
    }
}
