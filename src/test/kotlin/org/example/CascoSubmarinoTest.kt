package org.example

import submarino.Casco
import submarino.CascoReforzado
import submarino.EntidadFisica
import submarino.ModuloCasco
import submarino.Movible
import submarino.Navegable
import submarino.Notificador
import submarino.NotificadorSilencioso
import submarino.Submarino
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CascoSubmarinoTest {

    @Test
    fun testComposicionYDelegacionCasco() {
        val casco = ModuloCasco(presionMaximaPa = 300_000.0, integridad = 100.0)
        val submarino = crearSubmarino(casco = casco, velocidad = 5.0)

        assertEquals(casco, submarino.casco)
        assertFalse(submarino.estaDestruido)

        val integridad = submarino.verificarIntegridad(200_000.0)
        assertEquals(100.0, integridad)
        assertFalse(submarino.estaDestruido)

        val nuevaIntegridad = submarino.verificarIntegridad(600_000.0)
        assertTrue(nuevaIntegridad < 100.0)
        assertEquals(submarino.casco.integridad, nuevaIntegridad)
    }

    @Test
    fun testMovimientoYConsumoBateria() {
        val submarino = crearSubmarino(casco = ModuloCasco(500_000.0, 100.0))

        val movido = submarino.mover(deltaX = 10.0, deltaY = 0.0)
        assertTrue(movido)
        assertEquals(10.0, submarino.posX)
        assertEquals(0.0, submarino.posY)
        assertEquals(95.0, submarino.bateria)

        submarino.mover(deltaX = 0.0, deltaY = -20.0)
        assertEquals(0.0, submarino.posY)
    }

    @Test
    fun testInmersionCriticaYBloqueoDeMovimiento() {
        val casco = ModuloCasco(presionMaximaPa = 100_000.0, integridad = 0.5)
        val submarino = crearSubmarino(casco = casco)

        submarino.verificarIntegridad(500_000.0)
        assertTrue(submarino.estaDestruido)
        assertTrue(casco.estaDestruido)
        assertEquals(0.0, casco.integridad)

        val posPreviaX = submarino.posX
        val resultadoMovimiento = submarino.mover(deltaX = 15.0, deltaY = 0.0)
        assertFalse(resultadoMovimiento)
        assertEquals(posPreviaX, submarino.posX)
    }

    @Test
    fun testPrincipioAbiertoCerradoYPolimorfismoCasco() {
        val cascoEstandar = ModuloCasco(200_000.0, 100.0, NotificadorSilencioso)
        val cascoReforzado = CascoReforzado(200_000.0, 100.0, 0.5, NotificadorSilencioso)

        val subEstandar = crearSubmarino(casco = cascoEstandar)
        val subReforzado = crearSubmarino(casco = cascoReforzado)

        val presion = 400_000.0
        assertEquals(99.0, subEstandar.verificarIntegridad(presion))
        assertEquals(99.5, subReforzado.verificarIntegridad(presion))
    }

    @Test
    fun testPrincipioSustitucionLiskov() {
        val entidad: EntidadFisica = crearSubmarino(posX = 5.0, posY = 10.0)

        val movible: Movible = entidad
        val exito = movible.mover(15.0, 5.0)

        assertTrue(exito)
        assertEquals(20.0, entidad.posX)
        assertEquals(15.0, entidad.posY)
    }

    @Test
    fun testDesacoplamientoNotificadorSRP() {
        val mensajesCapturados = mutableListOf<String>()
        val mockNotificador = object : Notificador {
            override fun notificar(mensaje: String) {
                mensajesCapturados.add(mensaje)
            }
        }

        val casco = ModuloCasco(presionMaximaPa = 100_000.0, integridad = 0.5, notificador = mockNotificador)
        val sub = crearSubmarino(casco = casco, notificador = mockNotificador)

        sub.verificarIntegridad(500_000.0)
        sub.mover(10.0, 0.0)

        assertTrue(mensajesCapturados.isNotEmpty())
        assertTrue(mensajesCapturados.any { it.contains("colapsado") })
        assertTrue(mensajesCapturados.any { it.contains("destruido") })
    }

    @Test
    fun testSegregacionDeInterfacesNavegable() {
        val submarino: Navegable = crearSubmarino()

        assertTrue(submarino.navegar(10.0))
        assertTrue(submarino.sumergir(20.0))
        assertTrue(submarino.ascender(5.0))
    }

    private fun crearSubmarino(
        casco: Casco = ModuloCasco(500_000.0),
        posX: Double = 0.0,
        posY: Double = 0.0,
        velocidad: Double = 10.0,
        notificador: Notificador = NotificadorSilencioso
    ): Submarino = Submarino(
        posX = posX,
        posY = posY,
        velocidad = velocidad,
        casco = casco,
        notificador = notificador
    )
}
