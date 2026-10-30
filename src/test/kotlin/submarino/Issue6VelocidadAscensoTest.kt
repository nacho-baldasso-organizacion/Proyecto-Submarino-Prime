package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Issue6VelocidadAscensoTest {

    @Test
    fun testVariacionDeVelocidadDeAscensoConBodegaVaciaMediaCargaYLlena() {
        val capacidad = 20.0
        val bodega = Bodega(capacidadMaxima = capacidad)
        val velocidadBase = 10.0
        val estrategia = EstrategiaAscensoLineal(factorReduccionMaximo = 0.5)
        val submarino = SubmarinoPruebaFactory.crear(bodega = bodega, velocidad = velocidadBase, estrategiaAscenso = estrategia)

        val velVacia = submarino.velocidadAscenso
        assertEquals(1.0, submarino.factorAscenso, 0.001)
        assertEquals(10.0, velVacia, 0.001)
        assertEquals(10.0, submarino.calcularVelocidadAscenso(), 0.001)

        val min1 = Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 1)
        val min2 = Mineral("M2", TipoMineral.TITANIO_CRISTALINO, 0, 2)
        bodega.agregar(min1)
        bodega.agregar(min2)

        val velMedia = submarino.velocidadAscenso
        assertEquals(0.75, submarino.factorAscenso, 0.001)
        assertEquals(7.5, velMedia, 0.001)

        val min3 = Mineral("M3", TipoMineral.TITANIO_CRISTALINO, 0, 3)
        val min4 = Mineral("M4", TipoMineral.TITANIO_CRISTALINO, 0, 4)
        bodega.agregar(min3)
        bodega.agregar(min4)

        val velLlena = submarino.velocidadAscenso
        assertEquals(0.50, submarino.factorAscenso, 0.001)
        assertEquals(5.0, velLlena, 0.001)

        assertTrue(velVacia > velMedia)
        assertTrue(velMedia > velLlena)
        assertTrue(velLlena > 0.0)
    }

    @Test
    fun testManiobraDeAscensoRalentizaElDesplazamientoEfectivoSegunMasa() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val sub = SubmarinoPruebaFactory.crear(bodega = bodega, posY = 100.0)

        assertTrue(sub.ascender(20.0))
        assertEquals(80.0, sub.posY, 0.001)

        bodega.agregar(Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 0))
        bodega.agregar(Mineral("M2", TipoMineral.TITANIO_CRISTALINO, 0, 1))

        assertTrue(sub.ascender(20.0))
        assertEquals(65.0, sub.posY, 0.001)

        bodega.agregar(Mineral("M3", TipoMineral.TITANIO_CRISTALINO, 0, 2))
        bodega.agregar(Mineral("M4", TipoMineral.TITANIO_CRISTALINO, 0, 3))

        assertTrue(sub.ascender(20.0))
        assertEquals(55.0, sub.posY, 0.001)
    }

    @Test
    fun testManiobraDeAscensoSemanticaSinArgumentosUsaVelocidadDinamica() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val sub = SubmarinoPruebaFactory.crear(bodega = bodega, posY = 50.0)

        assertTrue(sub.ascender())
        assertEquals(40.0, sub.posY, 0.001)

        for (i in 1..4) {
            bodega.agregar(Mineral("M$i", TipoMineral.TITANIO_CRISTALINO, 0, i))
        }
        assertTrue(sub.ascender())
        assertEquals(35.0, sub.posY, 0.001)
    }

    @Test
    fun testSubmarinoSinBodegaMantienePesoBaseYVelocidadNominal() {
        val subSinBodega = Submarino(
            casco = ModuloCasco(500_000.0),
            pesoBase = 1200.0,
            bodega = null,
            velocidad = 8.0,
            notificador = NotificadorSilencioso
        )

        assertEquals(1200.0, subSinBodega.pesoTotal, 0.001)
        assertEquals(8.0, subSinBodega.velocidadAscenso, 0.001)
        assertEquals(1.0, subSinBodega.factorAscenso, 0.001)

        val estado = subSinBodega.obtenerEstadoAscenso()
        assertEquals(1200.0, estado.pesoBase, 0.001)
        assertEquals(0.0, estado.pesoCarga, 0.001)
        assertEquals(1200.0, estado.pesoTotal, 0.001)
        assertEquals(0.0, estado.capacidadMaximaBodega, 0.001)
        assertEquals(1.0, estado.factorVelocidad, 0.001)
        assertEquals(8.0, estado.velocidadNominal, 0.001)
        assertEquals(8.0, estado.velocidadAscensoEfectiva, 0.001)
    }
}
