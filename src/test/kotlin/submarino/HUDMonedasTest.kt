package submarino

import kotlin.test.Test
import kotlin.test.assertEquals

class HUDMonedasTest {

    private val hud = HUDView()

    @Test
    fun testRenderizarMonedas_valorCero() {
        val estado = EstadoHUD(0.0, 0.0, 0.0, 0.0, 0.0, 0)
        val resultado = hud.renderizarMonedas(estado)
        assertEquals("Monedas: 0", resultado)
    }

    @Test
    fun testRenderizarMonedas_conMonedas() {
        val estado = EstadoHUD(0.0, 0.0, 0.0, 0.0, 0.0, 42)
        val resultado = hud.renderizarMonedas(estado)
        assertEquals("Monedas: 42", resultado)
    }
}
