package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class Issue6Test {

    @Test
    fun testIntegracionCompletaRecoleccionYDecrementoReactivo() {
        val bodega = Bodega(capacidadMaxima = 10.0)
        val servicio: ServicioRecoleccionContrato = ServicioRecoleccion(bodega, DetectorColisionesConcreto())
        val sub = SubmarinoPruebaFactory.crear(bodega = bodega, posY = 100.0)

        assertEquals(10.0, sub.velocidadAscenso, 0.001)

        val mineral1 = Mineral("M1", TipoMineral.NODULO_DE_MANGANESO, posX = 0, posY = 100)
        assertIs<ResultadoRecoleccion.Exito>(
            recolectar(servicio, mineral1, Posicion(0, 100))
        )
        verificarPesoYVelocidad(sub, pesoEsperado = 1004.0, velocidadEsperada = 8.0)

        sub.ascender(10.0)
        val mineral2 = Mineral("M2", TipoMineral.TITANIO_CRISTALINO, posX = 0, posY = sub.posY.toInt())
        assertIs<ResultadoRecoleccion.Exito>(
            recolectar(servicio, mineral2, Posicion(0, sub.posY.toInt()))
        )
        verificarPesoYVelocidad(sub, pesoEsperado = 1009.0, velocidadEsperada = 5.5)
    }

    private fun recolectar(
        servicio: ServicioRecoleccionContrato,
        mineral: Mineral,
        posicion: Posicion
    ): ResultadoRecoleccion = servicio.procesarRecoleccion(posicion, listOf(mineral))

    private fun verificarPesoYVelocidad(
        sub: Submarino,
        pesoEsperado: Double,
        velocidadEsperada: Double
    ) {
        assertEquals(pesoEsperado, sub.pesoTotal, 0.001)
        assertEquals(velocidadEsperada, sub.velocidadAscenso, 0.001)
    }
}
