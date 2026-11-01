package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HUDBarraPresionTest {

    private val hud = HUDView()

    @Test
    fun testRenderizarBarraPresion_conValoresNormales() {
        val estado = EstadoHUD(50.0, 100.0, 0.0, 0.0, 0.0, 0)
        val resultado = hud.renderizarBarraPresion(estado)
        assertEquals("Presión: 50.00 / 100.00 mmHg [▇▇] 50.0%", resultado)
    }

    @Test
    fun testRenderizarBarraPresion_conPresionMaximaCero() {
        val estado = EstadoHUD(0.0, 0.0, 0.0, 0.0, 0.0, 0)
        val resultado = hud.renderizarBarraPresion(estado)
        assertEquals("Presión: 0.00 / 0.00 mmHg (0.00%)", resultado)
    }

    @Test
    fun testRenderizarBarraPresion_porEncimaDelMaximo() {
        val estado = EstadoHUD(150.0, 100.0, 0.0, 0.0, 0.0, 0)
        val resultado = hud.renderizarBarraPresion(estado)
        // Porcentaje puede exceder 100% si la presión actual supera la máxima
        assertTrue(resultado.contains("150.00"))
    }
}
