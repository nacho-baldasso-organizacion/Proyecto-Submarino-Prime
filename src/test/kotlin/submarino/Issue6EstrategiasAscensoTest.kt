package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.Triple

class Issue6EstrategiasAscensoTest {

    @Test
    fun testPolimorfismoOCPConEstrategiaFisicaFlotabilidadArquimedes() {
        val (bodega, estrategia, sub) = crearSubmarinoConEstrategia()
        ejecutarPrimerasPruebas(bodega, sub)
        agregarMineralesYLastre(bodega, sub)
        verificarResultadoFinal(bodega, sub)
    }

    private fun crearSubmarinoConEstrategia(): Triple<Bodega, EstrategiaFisicaFlotabilidad, Submarino> {
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
        return Triple(bodega, fisicaFlotabilidad, sub)
    }

    private fun ejecutarPrimerasPruebas(bodega: Bodega, sub: Submarino) {
        assertEquals(1.0, sub.factorAscenso, 0.001)
        assertEquals(10.0, sub.velocidadAscenso, 0.001)
    }

    private fun agregarMineralesYLastre(bodega: Bodega, sub: Submarino) {
        val minMedia = Mineral("M1", TipoMineral.ARTEFACTO_HUNDIDO, 0, 0)
        val minMedia2 = Mineral("M2", TipoMineral.ARTEFACTO_HUNDIDO, 0, 1)
        val minMedia3 = Mineral("M3", TipoMineral.ARTEFACTO_HUNDIDO, 0, 2)
        val minMedia4 = Mineral("M4", TipoMineral.ARENA_DE_CUARZO, 0, 3)
        bodega.agregar(minMedia)
        bodega.agregar(minMedia2)
        bodega.agregar(minMedia3)
        bodega.agregar(minMedia4)

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
    }

    private fun verificarResultadoFinal(bodega: Bodega, sub: Submarino) {
        assertEquals(50.0, bodega.pesoActual, 0.001)
        assertEquals(0.3, sub.factorAscenso, 0.001)
        assertEquals(3.0, sub.velocidadAscenso, 0.001)
    }
}