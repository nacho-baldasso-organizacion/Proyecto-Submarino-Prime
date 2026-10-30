package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HUDViewTest {

    private val hud = HUDView()

    @Test
    fun testRenderizarHUDCompleto_resumenCompleto() {
        val estado = EstadoHUD(75.0, 100.0, 300.0, 200.0, 800.0, 150)
        val resultado = hud.renderizarHUDCompleto(estado)
        // Debe contener las 4 secciones separadas por saltos de línea
        val lineas = resultado.split("\n").toList()
        assertEquals(4, lineas.size)
        assertTrue(lineas[0].startsWith("Presión:"))
        assertTrue(lineas[1].startsWith("Profundidad:"))
        assertTrue(lineas[2].startsWith("Peso bodega:"))
        assertTrue(lineas[3].startsWith("Monedas:"))
    }

    @Test
    fun testRenderizarHUDCompleto_conCero() {
        val estado = EstadoHUD(0.0, 0.0, 0.0, 0.0, 0.0, 0)
        val resultado = hud.renderizarHUDCompleto(estado)
        val lineas = resultado.split("\n").toList()
        assertEquals(4, lineas.size)
        assertTrue(lineas.any { it.contains("0.0") })
    }
}
