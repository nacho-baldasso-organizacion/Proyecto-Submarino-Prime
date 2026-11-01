package submarino

import kotlin.test.Test
import kotlin.test.assertEquals

class HUDProfundidadTest {

    private val hud = HUDView()

    @Test
    fun testRenderizarProfundidad_valoresNormales() {
        val estado = EstadoHUD(0.0, 0.0, 125.5, 0.0, 0.0, 0)
        val resultado = hud.renderizarProfundidad(estado)
        assertEquals("Profundidad: 125.5 m", resultado)
    }

    @Test
    fun testRenderizarProfundidad_cero() {
        val estado = EstadoHUD(0.0, 0.0, 0.0, 0.0, 0.0, 0)
        val resultado = hud.renderizarProfundidad(estado)
        assertEquals("Profundidad: 0.0 m", resultado)
    }
}
