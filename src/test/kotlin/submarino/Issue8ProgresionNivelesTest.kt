package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Pruebas de progresión de niveles y rechazo al alcanzar el nivel máximo.
 */
class Issue8ProgresionNivelesTest {

    @Test
    fun `test progresion escalonada hasta nivel maximo`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val sub = crearSubmarino(bodega = bodega, saldoInicial = 5000)

        val mejora = FabricaMejoras.crearMejoraBodega(
            nivelInicial = 1,
            nivelMaximo = 3,
            incrementoCapacidadKg = 10.0,
            costoBase = 100,
            incrementoPorNivel = 50
        )
        val servicio = crearServicio(mejora)

        assertFalse(mejora.esNivelMaximo)
        val res1 = servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertTrue(res1 is ResultadoCompra.Exito)
        assertEquals(2, mejora.nivel)
        assertEquals(30.0, bodega.capacidadMaxima)
        assertFalse(mejora.esNivelMaximo)

        val res2 = servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertTrue(res2 is ResultadoCompra.Exito)
        assertEquals(3, mejora.nivel)
        assertEquals(40.0, bodega.capacidadMaxima)
        assertTrue(mejora.esNivelMaximo)
        assertEquals(0, mejora.costo, "En nivel máximo el costo reportado debe ser 0")
    }

    @Test
    fun `test intento de compra en nivel maximo es rechazado formalmente`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val monedero = Monedero(saldoInicial = 1000)
        val sub = crearSubmarino(bodega = bodega, monedero = monedero)

        val mejora = FabricaMejoras.crearMejoraBodega(
            nivelInicial = 2,
            nivelMaximo = 2,
            incrementoCapacidadKg = 10.0,
            costoBase = 100
        )
        val servicio = crearServicio(mejora)

        assertTrue(mejora.esNivelMaximo)
        val saldoPrevio = monedero.saldo

        val resultado = servicio.comprarMejora(mejora.id, sub.billetera, sub)

        assertTrue(resultado is ResultadoCompra.NivelMaximoAlcanzado)
        assertEquals(mejora.id, resultado.mejoraId)
        assertEquals(2, resultado.nivelActual)
        assertEquals(2, resultado.nivelMaximo)

        assertEquals(saldoPrevio, monedero.saldo)
        assertEquals(20.0, bodega.capacidadMaxima)
        assertFalse(mejora.subirNivel())
    }

    private fun crearSubmarino(bodega: Bodega, saldoInicial: Int = 0): Submarino =
        FabricaDatosPrueba.crearSubmarino(bodega = bodega, billetera = Monedero(saldoInicial))

    private fun crearSubmarino(bodega: Bodega, monedero: Billetera): Submarino =
        FabricaDatosPrueba.crearSubmarino(bodega = bodega, billetera = monedero)

    private fun crearServicio(vararg mejoras: Mejora): ServicioUpgrades =
        ServicioUpgrades(CatalogoUpgrades(mejoras.toList()))
}
