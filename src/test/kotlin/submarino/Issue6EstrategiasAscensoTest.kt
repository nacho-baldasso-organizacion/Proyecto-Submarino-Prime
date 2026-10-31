package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Issue6EstrategiasAscensoTest {

    @Test
    fun testPolimorfismoOCPConEstrategiaAscensoPorMasa() {
        val bodega = Bodega(capacidadMaxima = 1000.0)
        val estrategiaMasa = EstrategiaAscensoPorMasa()

        val sub = Submarino(
            casco = ModuloCasco(500_000.0),
            pesoBase = 1000.0,
            bodega = bodega,
            velocidad = 20.0,
            estrategiaAscenso = estrategiaMasa,
            notificador = NotificadorSilencioso
        )

        assertEquals(1.0, sub.factorAscenso, 0.001)
        assertEquals(20.0, sub.velocidadAscenso, 0.001)

        val mineralPesado = object : Recolectable {
            override val id = "PESADO-1"
            override val nombre = "Restos de Naufragio"
            override val peso = 1000.0
            override val valor = 5000
            override var recolectado = false
            override val posX = 0
            override val posY = 0
        }
        assertTrue(bodega.agregar(mineralPesado))

        assertEquals(2000.0, sub.pesoTotal, 0.001)
        assertEquals(0.50, sub.factorAscenso, 0.001)
        assertEquals(10.0, sub.velocidadAscenso, 0.001)
    }

    @Test
    fun testPolimorfismoOCPConEstrategiaFisicaFlotabilidadArquimedes() {
        val bodega = Bodega(capacidadMaxima = 50.0)
        val fisicaFlotabilidad = EstrategiaFisicaFlotabilidad(
            calculadorPresion = CalculadoraHidrostatica,
            factorEmpujeMinimo = 0.3
        )

        val sub = Submarino(
            casco = ModuloCasco(500_000.0),
            pesoBase = 1000.0,
            bodega = bodega,
            velocidad = 10.0,
            estrategiaAscenso = fisicaFlotabilidad,
            notificador = NotificadorSilencioso
        )

        assertEquals(1.0, sub.factorAscenso, 0.001)
        assertEquals(10.0, sub.velocidadAscenso, 0.001)

        val minMedia = Mineral("M1", TipoMineral.ARTEFACTO_HUNDIDO, 0, 0)
        val minMedia2 = Mineral("M2", TipoMineral.ARTEFACTO_HUNDIDO, 0, 1)
        val minMedia3 = Mineral("M3", TipoMineral.ARTEFACTO_HUNDIDO, 0, 2)
        val minMedia4 = Mineral("M4", TipoMineral.ARENA_DE_CUARZO, 0, 3)
        bodega.agregar(minMedia)
        bodega.agregar(minMedia2)
        bodega.agregar(minMedia3)
        bodega.agregar(minMedia4)

        assertEquals(25.0, bodega.pesoActual, 0.001)
        assertEquals(0.65, sub.factorAscenso, 0.001)
        assertEquals(6.5, sub.velocidadAscenso, 0.001)

        val itemRelleno = object : Recolectable {
            override val id = "RELLENO"
            override val nombre = "Lastre"
            override val peso = 25.0
            override val valor = 10
            override var recolectado = false
            override val posX = 0
            override val posY = 0
        }
        bodega.agregar(itemRelleno)

        assertEquals(50.0, bodega.pesoActual, 0.001)
        assertEquals(0.3, sub.factorAscenso, 0.001)
        assertEquals(3.0, sub.velocidadAscenso, 0.001)
    }
}
