package org.example

import submarino.CalculadoraHidrostatica
import submarino.CalculadoraPresionFluido
import submarino.Casco
import submarino.CascoReforzado
import submarino.EntidadFisica
import submarino.ModuloCasco
import submarino.Movible
import submarino.Navegable
import submarino.Notificador
import submarino.NotificadorSilencioso
import submarino.PruebaConjunta
import submarino.SimuladorSubmarino
import submarino.Submarino
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SubmarinoIntegrationTest {

    @Test
    fun testComposicionYDelegacionCasco() {
        val casco = ModuloCasco(presionMaximaPa = 300_000.0, integridad = 100.0)
        val submarino = Submarino(posX = 0.0, posY = 0.0, velocidad = 5.0, casco = casco)

        // Verificar composición
        assertEquals(casco, submarino.casco)
        assertFalse(submarino.estaDestruido)

        // Delegación de presión sin superar límite
        val integridad = submarino.verificarIntegridad(200_000.0)
        assertEquals(100.0, integridad)
        assertFalse(submarino.estaDestruido)

        // Delegación de presión superando límite
        val nuevaIntegridad = submarino.verificarIntegridad(600_000.0)
        assertTrue(nuevaIntegridad < 100.0)
        assertEquals(submarino.casco.integridad, nuevaIntegridad)
    }

    @Test
    fun testMovimientoYConsumoBateria() {
        val casco = ModuloCasco(presionMaximaPa = 500_000.0, integridad = 100.0)
        val submarino = Submarino(posX = 0.0, posY = 0.0, velocidad = 10.0, casco = casco)

        // Mover 10 metros adelante
        val movido = submarino.mover(deltaX = 10.0, deltaY = 0.0)
        assertTrue(movido)
        assertEquals(10.0, submarino.posX)
        assertEquals(0.0, submarino.posY)
        assertEquals(95.0, submarino.bateria) // 10 * 0.5% = 5% consumido

        // Intentar ascender estando en superficie (posY debe ser >= 0.0)
        submarino.mover(deltaX = 0.0, deltaY = -20.0)
        assertEquals(0.0, submarino.posY)
    }

    @Test
    fun testInmersionCriticaYBloqueoDeMovimiento() {
        val casco = ModuloCasco(presionMaximaPa = 100_000.0, integridad = 0.5)
        val submarino = Submarino(posX = 0.0, posY = 0.0, velocidad = 10.0, casco = casco)

        // Someter a presión extrema
        submarino.verificarIntegridad(500_000.0)
        assertTrue(submarino.estaDestruido)
        assertTrue(casco.estaDestruido)
        assertEquals(0.0, casco.integridad)

        // Intentar mover nave destruida
        val posPreviaX = submarino.posX
        val resultadoMovimiento = submarino.mover(deltaX = 15.0, deltaY = 0.0)
        assertFalse(resultadoMovimiento)
        assertEquals(posPreviaX, submarino.posX)
    }

    @Test
    fun testCalculadoraHidrostatica() {
        val presionSuperficie = CalculadoraHidrostatica.calcularPresion(0.0)
        assertEquals(CalculadoraHidrostatica.PRESION_ATMOSFERICA, presionSuperficie)

        val profMax = CalculadoraHidrostatica.calcularProfundidadMaxima(CalculadoraHidrostatica.PRESION_ATMOSFERICA)
        assertEquals(0.0, profMax)
    }

    @Test
    fun testEjecucionSimulacionCompleta() {
        // Verifica que la simulación orquestadora se ejecute sin excepciones
        SimuladorSubmarino().ejecutarSimulacionCompleta()
        PruebaConjunta().ejecutarSimulacionCompleta()
    }

    // =========================================================================
    // PRUEBAS ESPECÍFICAS DE PRINCIPIOS SOLID
    // =========================================================================

    @Test
    fun testPrincipioAbiertoCerradoYPolimorfismoCasco() {
        // OCP + DIP: Submarino puede recibir CascoReforzado mediante la interfaz Casco
        val cascoEstandar = ModuloCasco(presionMaximaPa = 200_000.0, integridad = 100.0, notificador = NotificadorSilencioso)
        val cascoReforzado = CascoReforzado(presionMaximaPa = 200_000.0, integridad = 100.0, factorAbsorcion = 0.5, notificador = NotificadorSilencioso)

        val subEstandar = Submarino(casco = cascoEstandar, notificador = NotificadorSilencioso)
        val subReforzado = Submarino(casco = cascoReforzado, notificador = NotificadorSilencioso)

        // Someter ambos a la misma sobrepresión
        val presion = 400_000.0 // exceso = 200_000 -> daño base = 1.0
        val integEstandar = subEstandar.verificarIntegridad(presion)
        val integReforzada = subReforzado.verificarIntegridad(presion)

        // El casco reforzado absorbe el 50% del daño: recibe solo 0.5 de daño vs 1.0
        assertEquals(99.0, integEstandar)
        assertEquals(99.5, integReforzada)
    }

    @Test
    fun testPrincipioAbiertoCerradoFisicaFluidos() {
        // OCP: Configuración de fluidos con diferente densidad sin modificar CalculadoraHidrostatica
        val aguaDulce = CalculadoraPresionFluido(densidadFluido = 1000.0)
        val aguaMar = CalculadoraHidrostatica

        val presionDulce = aguaDulce.calcularPresion(10.0)
        val presionMar = aguaMar.calcularPresion(10.0)

        // La densidad del mar (1025) genera mayor presión que la dulce (1000) a 10m
        assertTrue(presionMar > presionDulce)
    }

    @Test
    fun testSegregacionDeInterfacesNavegable() {
        // ISP: El cliente interactúa exclusivamente con el contrato Navegable
        val submarino: Navegable = Submarino(
            casco = ModuloCasco(500_000.0),
            notificador = NotificadorSilencioso
        )

        assertTrue(submarino.navegar(10.0))
        assertTrue(submarino.sumergir(20.0))
        assertTrue(submarino.ascender(5.0))
    }

    @Test
    fun testPrincipioSustitucionLiskov() {
        // LSP: Un Submarino sustituye transparentemente a EntidadFisica y a Movible
        val entidad: EntidadFisica = Submarino(
            posX = 5.0,
            posY = 10.0,
            casco = ModuloCasco(500_000.0),
            notificador = NotificadorSilencioso
        )

        val movible: Movible = entidad
        val exito = movible.mover(15.0, 5.0)

        assertTrue(exito)
        assertEquals(20.0, entidad.posX)
        assertEquals(15.0, entidad.posY)
    }

    @Test
    fun testDesacoplamientoNotificadorSRP() {
        // SRP / DIP: Las alertas no van forzosamente a la consola, se pueden interceptar
        val mensajesCapturados = mutableListOf<String>()
        val mockNotificador = object : Notificador {
            override fun notificar(mensaje: String) {
                mensajesCapturados.add(mensaje)
            }
        }

        val casco = ModuloCasco(presionMaximaPa = 100_000.0, integridad = 0.5, notificador = mockNotificador)
        val sub = Submarino(casco = casco, notificador = mockNotificador)

        sub.verificarIntegridad(500_000.0)
        sub.mover(10.0, 0.0)

        // Verificar que se notificó la destrucción y el bloqueo de movimiento sin tocar System.out
        assertTrue(mensajesCapturados.isNotEmpty())
        assertTrue(mensajesCapturados.any { it.contains("colapsado") })
        assertTrue(mensajesCapturados.any { it.contains("destruido") })
    }
}
