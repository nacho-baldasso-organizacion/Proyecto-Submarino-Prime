package org.example

import submarino.Billetera
import submarino.Bodega
import submarino.EstacionAcople
import submarino.EstrategiaAscenso
import submarino.EstrategiaAscensoLineal
import submarino.FabricaDatosPrueba
import submarino.Mineral
import submarino.Monedero
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

        val bodega = crearBodegaCon(
            Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 0),
            Mineral("M2", TipoMineral.NODULO_DE_MANGANESO, 0, 1)
        )
        val billetera = Monedero(saldoInicial = 100)
        val submarino = crearSubmarino(bodega = bodega, billetera = billetera, posY = 40.0)

        assertFalse(estacion.puedeAcoplar(submarino.posX, submarino.posY))
        assertFalse(submarino.estaEnZonaAcople(estacion.zonaAcople))

        submarino.mover(deltaX = 0.0, deltaY = -40.0)
        assertEquals(0.0, submarino.posY)
        assertTrue(submarino.estaEnSuperficie)
        assertTrue(submarino.estaEnZonaAcople(estacion.zonaAcople))
        assertTrue(estacion.puedeAcoplar(submarino.posX, submarino.posY))

        assertTrue(estacion.acoplar(submarino, submarino.posX, submarino.posY))
        assertTrue(submarino.estaAcoplado)
        assertTrue(estacion.hayNaveAcoplada)

        val resultado = servicioComercio.venderBodegaSubmarino(submarino)
        verificarVentaExitosa(resultado, totalCreditos = 310, saldoEsperado = 410, bodega = bodega)
        assertEquals(410, submarino.billetera.saldo)

        assertTrue(estacion.desacoplar())
        assertFalse(submarino.estaAcoplado)
        assertFalse(estacion.hayNaveAcoplada)
    }

    @Test
    fun testRechazoVentaEnInmersionSinAcopleIssue7() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val billetera = Monedero(saldoInicial = 50)
        val submarino = crearSubmarino(bodega = bodega, billetera = billetera, posY = 50.0)
        bodega.agregar(Mineral("M1", TipoMineral.ARENA_DE_CUARZO, 0, 50))

        val resultado = ServicioComercio().venderBodegaSubmarino(submarino)

        assertIs<ResultadoComercio.NaveNoAcoplada>(resultado)
        assertEquals(50, submarino.billetera.saldo)
        assertEquals(1, bodega.cantidadItems)
    }

    @Test
    fun testRestauracionVelocidadAscensoTrasVentaEnEstacionIssue7() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val submarino = crearSubmarino(
            bodega = bodega,
            estrategiaAscenso = EstrategiaAscensoLineal(factorReduccionMaximo = 0.5)
        )

        FabricaDatosPrueba.cargarMinerales(bodega, cantidad = 4, desde = 1)
        assertEquals(5.0, submarino.velocidadAscenso, 0.001)

        submarino.acoplar()
        val resultado = ServicioComercio().venderBodegaSubmarino(submarino)

        assertIs<ResultadoComercio.Exito>(resultado)
        assertEquals(0.0, bodega.pesoActual, 0.001)
        assertEquals(10.0, submarino.velocidadAscenso, 0.001)
        assertEquals(1.0, submarino.factorAscenso, 0.001)
    }

    private fun crearSubmarino(
        bodega: Bodega,
        billetera: Billetera = Monedero(0),
        posY: Double = 0.0,
        estrategiaAscenso: EstrategiaAscenso = EstrategiaAscensoLineal()
    ): Submarino = FabricaDatosPrueba.crearSubmarino(
        posY = posY,
        bodega = bodega,
        billetera = billetera,
        estrategiaAscenso = estrategiaAscenso
    )

    private fun crearBodegaCon(vararg minerales: Mineral): Bodega {
        val bodega = Bodega(capacidadMaxima = 30.0)
        minerales.forEach { bodega.agregar(it) }
        return bodega
    }

    private fun verificarVentaExitosa(
        resultado: ResultadoComercio,
        totalCreditos: Int,
        saldoEsperado: Int,
        bodega: Bodega
    ) {
        assertIs<ResultadoComercio.Exito>(resultado)
        assertEquals(totalCreditos, resultado.transaccion.totalCreditos)
        assertEquals(saldoEsperado, resultado.saldoActualizado)
        assertEquals(0.0, bodega.pesoActual, 0.001)
        assertEquals(0, bodega.cantidadItems)
    }
}
