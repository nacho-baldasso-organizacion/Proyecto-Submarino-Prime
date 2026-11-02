package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ModeloNavegacionTest {

    private fun crearModelo(
        consumoOxigenoPorSegundo: Double = 0.8
    ): ModeloNavegacion = ModeloNavegacion(
        anchoEscena = 1000.0,
        altoEscena = 500.0,
        consumoOxigenoPorSegundo = consumoOxigenoPorSegundo
    )

    @Test
    fun testEstadoInicialCentrado() {
        val estado = crearModelo().estado()

        assertEquals(500.0, estado.posicionX)
        assertEquals(250.0, estado.posicionY)
        assertEquals(25.0, estado.profundidad)
        assertEquals(50.0, estado.coordenadaX)
        assertEquals(100.0, estado.oxigeno)
        assertEquals(EstadoOxigeno.ESTABLE, estado.estadoOxigeno)
    }

    @Test
    fun testMoverDerechaYAbajo() {
        val modelo = crearModelo()

        modelo.actualizar(1.0, 1.0, 1.0)

        assertEquals(760.0, modelo.estado().posicionX)
        assertEquals(440.0, modelo.estado().posicionY)
    }

    @Test
    fun testMovimientoQuedaAcotadoALosMargenes() {
        val modelo = crearModelo()

        modelo.actualizar(10.0, 1.0, 1.0)
        assertEquals(930.0, modelo.estado().posicionX)
        assertEquals(440.0, modelo.estado().posicionY)

        modelo.actualizar(10.0, -1.0, -1.0)
        assertEquals(70.0, modelo.estado().posicionX)
        assertEquals(60.0, modelo.estado().posicionY)
    }

    @Test
    fun testOxigenoDisminuyeYCambiaDeEstado() {
        val modelo = crearModelo(consumoOxigenoPorSegundo = 50.0)

        modelo.actualizar(1.0, 0.0, 0.0)
        assertEquals(50.0, modelo.estado().oxigeno)
        assertEquals(EstadoOxigeno.PRECAUCION, modelo.estado().estadoOxigeno)

        modelo.actualizar(1.0, 0.0, 0.0)
        assertEquals(0.0, modelo.estado().oxigeno)
        assertEquals(EstadoOxigeno.AGOTADO, modelo.estado().estadoOxigeno)
    }

    @Test
    fun testDeltaInvalidoNoModificaElEstado() {
        val modelo = crearModelo()
        val inicial = modelo.estado()

        modelo.actualizar(0.0, 1.0, 1.0)
        modelo.actualizar(Double.NaN, 1.0, 1.0)
        modelo.actualizar(-1.0, 1.0, 1.0)

        assertEquals(inicial, modelo.estado())
    }

    @Test
    fun testConsumoExtraAlMoverse() {
        val modelo = crearModelo()

        modelo.actualizar(1.0, 1.0, 0.0)

        assertTrue(modelo.estado().oxigeno < 99.2)
    }
}
