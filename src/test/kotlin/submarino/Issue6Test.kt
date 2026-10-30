package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class Issue6Test {

    @Test
    fun testIntegracionCompletaRecoleccionYDecrementoReactivo() {
        val bodega = Bodega(capacidadMaxima = 10.0)
        val detector = DetectorColisionesConcreto()
        val servicio = ServicioRecoleccion(bodega, detector)

        val sub = SubmarinoPruebaFactory.crear(bodega = bodega, posY = 100.0)

        assertEquals(10.0, sub.velocidadAscenso, 0.001)

        val mineral1 = Mineral("M1", TipoMineral.NODULO_DE_MANGANESO, posX = 0, posY = 100)
        val res1 = servicio.procesarRecoleccion(Posicion(0, 100), listOf(mineral1))
        assertIs<ResultadoRecoleccion.Exito>(res1)

        assertEquals(1004.0, sub.pesoTotal, 0.001)
        assertEquals(8.0, sub.velocidadAscenso, 0.001)

        sub.ascender(10.0)
        val mineral2 = Mineral("M2", TipoMineral.TITANIO_CRISTALINO, posX = 0, posY = sub.posY.toInt())
        val res2 = servicio.procesarRecoleccion(Posicion(0, sub.posY.toInt()), listOf(mineral2))
        assertIs<ResultadoRecoleccion.Exito>(res2)

        assertEquals(1009.0, sub.pesoTotal, 0.001)
        assertEquals(5.5, sub.velocidadAscenso, 0.001)
    }
}
