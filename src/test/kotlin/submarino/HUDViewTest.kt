package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HUDViewTest {

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