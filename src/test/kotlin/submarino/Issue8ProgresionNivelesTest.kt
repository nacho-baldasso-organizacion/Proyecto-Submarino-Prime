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
        val monedero = Monedero(saldoInicial = 5000)
        val sub = Submarino(
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = monedero,
            notificador = NotificadorSilencioso
        )

        // Nivel 1 a 3 (máximo 3)
        val mejora = FabricaMejoras.crearMejoraBodega(
            nivelInicial = 1,
            nivelMaximo = 3,
            incrementoCapacidadKg = 10.0,
            costoBase = 100,
            incrementoPorNivel = 50
        )
        val servicio = ServicioUpgrades(CatalogoUpgrades(listOf(mejora)))

        // 1. Compra a Nivel 2 (costo 100)
        assertFalse(mejora.esNivelMaximo)
        val res1 = servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertTrue(res1 is ResultadoCompra.Exito)
        assertEquals(2, mejora.nivel)
        assertEquals(30.0, bodega.capacidadMaxima)
        assertFalse(mejora.esNivelMaximo)

        // 2. Compra a Nivel 3 (costo 150) -> llega al tope
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
        val sub = Submarino(
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = monedero,
            notificador = NotificadorSilencioso
        )

        val mejora = FabricaMejoras.crearMejoraBodega(
            nivelInicial = 2,
            nivelMaximo = 2, // Ya nace en nivel máximo
            incrementoCapacidadKg = 10.0,
            costoBase = 100
        )
        val servicio = ServicioUpgrades(CatalogoUpgrades(listOf(mejora)))

        assertTrue(mejora.esNivelMaximo)
        val saldoPrevio = monedero.saldo

        val resultado = servicio.comprarMejora(mejora.id, sub.billetera, sub)

        assertTrue(resultado is ResultadoCompra.NivelMaximoAlcanzado)
        assertEquals(mejora.id, resultado.mejoraId)
        assertEquals(2, resultado.nivelActual)
        assertEquals(2, resultado.nivelMaximo)

        // Monedas intactas y estadísticas intactas
        assertEquals(saldoPrevio, monedero.saldo)
        assertEquals(20.0, bodega.capacidadMaxima)
        assertFalse(mejora.subirNivel())
    }
}
