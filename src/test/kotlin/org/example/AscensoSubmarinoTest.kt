package org.example

import submarino.Bodega
import submarino.EstrategiaAscensoLineal
import submarino.Mineral
import submarino.ModuloCasco
import submarino.NotificadorSilencioso
import submarino.Submarino
import submarino.TipoMineral
import kotlin.test.Test
import kotlin.test.assertEquals

class AscensoSubmarinoTest {

    @Test
    fun testCalculoPesoTotalYVariacionVelocidadAscensoIssue6() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val submarino = Submarino(
            casco = ModuloCasco(500_000.0),
            pesoBase = 1000.0,
            bodega = bodega,
            velocidad = 10.0,
            estrategiaAscenso = EstrategiaAscensoLineal(factorReduccionMaximo = 0.5),
            notificador = NotificadorSilencioso
        )

        assertEquals(1000.0, submarino.pesoTotal)
        assertEquals(10.0, submarino.velocidadAscenso)
        assertEquals(1.0, submarino.factorAscenso)

        bodega.agregar(Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 0))
        bodega.agregar(Mineral("M2", TipoMineral.TITANIO_CRISTALINO, 0, 1))
        assertEquals(1010.0, submarino.pesoTotal)
        assertEquals(7.5, submarino.velocidadAscenso)
        assertEquals(0.75, submarino.factorAscenso)

        bodega.agregar(Mineral("M3", TipoMineral.TITANIO_CRISTALINO, 0, 2))
        bodega.agregar(Mineral("M4", TipoMineral.TITANIO_CRISTALINO, 0, 3))
        assertEquals(1020.0, submarino.pesoTotal)
        assertEquals(5.0, submarino.velocidadAscenso)
        assertEquals(0.50, submarino.factorAscenso)
    }

    @Test
    fun testManiobraAscensoRalentizadaPorCargaIssue6() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val sub = Submarino(
            posX = 0.0,
            posY = 50.0,
            velocidad = 10.0,
            casco = ModuloCasco(500_000.0),
            pesoBase = 1000.0,
            bodega = bodega,
            estrategiaAscenso = EstrategiaAscensoLineal(factorReduccionMaximo = 0.5),
            notificador = NotificadorSilencioso
        )

        for (i in 1..4) {
            bodega.agregar(Mineral("M$i", TipoMineral.TITANIO_CRISTALINO, 0, i))
        }

        sub.ascender(10.0)
        assertEquals(45.0, sub.posY)
    }
}
