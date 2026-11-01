package org.example

import submarino.Bodega
import submarino.EstrategiaAscensoLineal
import submarino.FabricaDatosPrueba
import submarino.Mineral
import submarino.Submarino
import submarino.TipoMineral
import kotlin.test.Test
import kotlin.test.assertEquals

class AscensoSubmarinoTest {

    @Test
    fun testCalculoPesoTotalYVariacionVelocidadAscensoIssue6() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val submarino = crearSubmarinoConBodega(bodega)

        assertEquals(1000.0, submarino.pesoTotal)
        assertEquals(10.0, submarino.velocidadAscenso)
        assertEquals(1.0, submarino.factorAscenso)

        agregarTitanios(bodega, cantidad = 2, desde = 1)
        assertEquals(1010.0, submarino.pesoTotal)
        assertEquals(7.5, submarino.velocidadAscenso)
        assertEquals(0.75, submarino.factorAscenso)

        agregarTitanios(bodega, cantidad = 2, desde = 3)
        assertEquals(1020.0, submarino.pesoTotal)
        assertEquals(5.0, submarino.velocidadAscenso)
        assertEquals(0.50, submarino.factorAscenso)
    }

    @Test
    fun testManiobraAscensoRalentizadaPorCargaIssue6() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val sub = FabricaDatosPrueba.crearSubmarino(
            posY = 50.0,
            bodega = bodega,
            pesoBase = 1000.0,
            estrategiaAscenso = EstrategiaAscensoLineal(factorReduccionMaximo = 0.5)
        )

        agregarTitanios(bodega, cantidad = 4, desde = 1)

        sub.ascender(10.0)
        assertEquals(45.0, sub.posY)
    }

    private fun crearSubmarinoConBodega(bodega: Bodega): Submarino =
        FabricaDatosPrueba.crearSubmarino(
            velocidad = 10.0,
            bodega = bodega,
            pesoBase = 1000.0,
            estrategiaAscenso = EstrategiaAscensoLineal(factorReduccionMaximo = 0.5)
        )

    private fun agregarTitanios(bodega: Bodega, cantidad: Int, desde: Int) {
        for (indice in desde until desde + cantidad) {
            bodega.agregar(Mineral("M$indice", TipoMineral.TITANIO_CRISTALINO, 0, indice))
        }
    }
}
