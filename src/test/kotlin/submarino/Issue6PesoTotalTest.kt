package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Issue6PesoTotalTest {

    @Test
    fun testCalculoDePesoTotalConBodegaVaciaEsIgualAlPesoBase() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val submarino = SubmarinoPruebaFactory.crear(bodega = bodega)

        assertEquals(0.0, bodega.pesoActual, 0.001)
        assertEquals(1000.0, submarino.pesoBase, 0.001)
        assertEquals(1000.0, submarino.pesoTotal, 0.001)
        assertEquals(1000.0, submarino.calcularPesoTotal(), 0.001)
        assertEquals(1000.0, submarino.peso, 0.001)
    }

    @Test
    fun testCalculoDePesoTotalAMediaCargaSumaPesoBaseYCargaActual() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val submarino = SubmarinoPruebaFactory.crear(bodega = bodega)

        val mineral1 = Mineral("MIN-1", TipoMineral.TITANIO_CRISTALINO, 0, 1)
        val mineral2 = Mineral("MIN-2", TipoMineral.TITANIO_CRISTALINO, 0, 2)
        assertTrue(bodega.agregar(mineral1))
        assertTrue(bodega.agregar(mineral2))

        assertEquals(10.0, bodega.pesoActual, 0.001)
        assertEquals(1010.0, submarino.pesoTotal, 0.001)
        assertEquals(1010.0, submarino.calcularPesoTotal(), 0.001)
        assertEquals(1010.0, submarino.peso, 0.001)
    }

    @Test
    fun testCalculoDePesoTotalConBodegaLlenaAlcanzaPesoBaseMasCapacidadMaxima() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val submarino = SubmarinoPruebaFactory.crear(bodega = bodega)

        for (i in 1..4) {
            val min = Mineral("MIN-$i", TipoMineral.TITANIO_CRISTALINO, 0, i)
            assertTrue(bodega.agregar(min))
        }

        assertTrue(bodega.estaLlena())
        assertEquals(20.0, bodega.pesoActual, 0.001)
        assertEquals(1020.0, submarino.pesoTotal, 0.001)
        assertEquals(1020.0, submarino.calcularPesoTotal(), 0.001)
        assertEquals(1020.0, submarino.peso, 0.001)
    }
}
