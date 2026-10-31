package org.example

import submarino.Bodega
import submarino.EstacionAcople
import submarino.EstrategiaAscensoLineal
import submarino.Mineral
import submarino.ModuloCasco
import submarino.Monedero
import submarino.NotificadorSilencioso
import submarino.ResultadoComercio
import submarino.ServicioComercio
import submarino.Submarino
import submarino.TipoMineral
import submarino.ZonaSuperficie
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ComercioSubmarinoTest {

    @Test
    fun testFlujoCompletoDeAcopleYComercioEnSuperficieIssue7() {
        val zonaSuperficie = ZonaSuperficie(profundidadMaxima = 0.0)
        val estacion = EstacionAcople(zonaAcople = zonaSuperficie)
        val servicioComercio = ServicioComercio()

        val bodega = Bodega(capacidadMaxima = 30.0)
        bodega.agregar(Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 0))
        bodega.agregar(Mineral("M2", TipoMineral.NODULO_DE_MANGANESO, 0, 1))

        val billetera = Monedero(saldoInicial = 100)
        val submarino = Submarino(
            posX = 20.0,
            posY = 40.0,
            velocidad = 10.0,
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = billetera,
            notificador = NotificadorSilencioso
        )

        assertFalse(estacion.puedeAcoplar(submarino.posX, submarino.posY))
        assertFalse(submarino.estaEnZonaAcople(estacion.zonaAcople))

        submarino.mover(deltaX = 0.0, deltaY = -40.0)
        assertEquals(0.0, submarino.posY)
        assertTrue(submarino.estaEnSuperficie)
        assertTrue(submarino.estaEnZonaAcople(estacion.zonaAcople))
        assertTrue(estacion.puedeAcoplar(submarino.posX, submarino.posY))

        val acoplado = estacion.acoplar(submarino, submarino.posX, submarino.posY)
        assertTrue(acoplado)
        assertTrue(submarino.estaAcoplado)
        assertTrue(estacion.hayNaveAcoplada)

        val resultado = servicioComercio.venderBodegaSubmarino(submarino)
        assertIs<ResultadoComercio.Exito>(resultado)
        assertEquals(310, resultado.transaccion.totalCreditos)
        assertEquals(410, submarino.billetera.saldo)
        assertEquals(0.0, bodega.pesoActual, 0.001)
        assertEquals(0, bodega.cantidadItems)

        assertTrue(estacion.desacoplar())
        assertFalse(submarino.estaAcoplado)
        assertFalse(estacion.hayNaveAcoplada)
    }

    @Test
    fun testRechazoVentaEnInmersionSinAcopleIssue7() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val billetera = Monedero(saldoInicial = 50)
        val submarino = Submarino(
            posX = 0.0,
            posY = 50.0,
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = billetera,
            notificador = NotificadorSilencioso
        )
        bodega.agregar(Mineral("M1", TipoMineral.ARENA_DE_CUARZO, 0, 50))

        val servicioComercio = ServicioComercio()
        val resultado = servicioComercio.venderBodegaSubmarino(submarino)

        assertIs<ResultadoComercio.NaveNoAcoplada>(resultado)
        assertEquals(50, submarino.billetera.saldo)
        assertEquals(1, bodega.cantidadItems)
    }

    @Test
    fun testRestauracionVelocidadAscensoTrasVentaEnEstacionIssue7() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val submarino = Submarino(
            posX = 0.0,
            posY = 0.0,
            velocidad = 10.0,
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            estrategiaAscenso = EstrategiaAscensoLineal(factorReduccionMaximo = 0.5),
            notificador = NotificadorSilencioso
        )

        for (i in 1..4) {
            bodega.agregar(Mineral("M$i", TipoMineral.TITANIO_CRISTALINO, 0, i))
        }
        assertEquals(5.0, submarino.velocidadAscenso, 0.001)

        submarino.acoplar()
        val servicioComercio = ServicioComercio()
        val resultado = servicioComercio.venderBodegaSubmarino(submarino)

        assertIs<ResultadoComercio.Exito>(resultado)
        assertEquals(0.0, bodega.pesoActual, 0.001)
        assertEquals(10.0, submarino.velocidadAscenso, 0.001)
        assertEquals(1.0, submarino.factorAscenso, 0.001)
    }
}
