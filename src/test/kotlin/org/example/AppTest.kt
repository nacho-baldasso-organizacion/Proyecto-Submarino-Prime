package org.example

import submarino.PruebaConjunta
import submarino.SimuladorSubmarino
import kotlin.test.Test

class SubmarinoIntegrationTest {

    @Test
    fun testEjecucionSimulacionCompleta() {
        SimuladorSubmarino().ejecutarSimulacionCompleta()
        PruebaConjunta().ejecutarSimulacionCompleta()
    }
}
