package org.example

import submarino.CalculadoraHidrostatica
import submarino.CalculadoraPresionFluido
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FisicaSubmarinoTest {

    @Test
    fun testCalculadoraHidrostatica() {
        val presionSuperficie = CalculadoraHidrostatica.calcularPresion(0.0)
        assertEquals(CalculadoraHidrostatica.PRESION_ATMOSFERICA, presionSuperficie)

        val profMax = CalculadoraHidrostatica.calcularProfundidadMaxima(CalculadoraHidrostatica.PRESION_ATMOSFERICA)
        assertEquals(0.0, profMax)
    }

    @Test
    fun testPrincipioAbiertoCerradoFisicaFluidos() {
        val aguaDulce = CalculadoraPresionFluido(densidadFluido = 1000.0)
        val aguaMar = CalculadoraHidrostatica

        val presionDulce = aguaDulce.calcularPresion(10.0)
        val presionMar = aguaMar.calcularPresion(10.0)

        assertTrue(presionMar > presionDulce)
    }
}
