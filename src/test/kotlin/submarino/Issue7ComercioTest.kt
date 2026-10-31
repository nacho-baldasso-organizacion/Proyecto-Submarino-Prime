package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Pruebas de venta de bodega, economía y vaciado tras la transacción.
 */
class Issue7ComercioTest {

    @Test
    fun `test venta de bodega exitosa incrementa fondos y vacia el almacenamiento`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val billetera = Monedero(saldoInicial = 50)
        val sub = Submarino(
            posX = 0.0,
            posY = 0.0,
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = billetera,
            notificador = NotificadorSilencioso
        )

        // Cargar minerales: Titanio ($220, 5kg) y Cobre ($35, 2.5kg) -> Total: $255, 7.5kg
        val min1 = Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 0)
        val min2 = Mineral("M2", TipoMineral.COBRE_MARINO, 0, 0)
        assertTrue(bodega.agregar(min1))
        assertTrue(bodega.agregar(min2))

        assertEquals(7.5, bodega.pesoActual, 0.001)
        assertEquals(2, bodega.cantidadItems)
        assertEquals(255, bodega.valorTotal)
        assertEquals(50, billetera.saldo)

        // Acoplar nave
        sub.acoplar()
        assertTrue(sub.estaAcoplado)

        val servicio = ServicioComercio()
        val resultado = servicio.venderBodegaSubmarino(sub)

        assertTrue(resultado is ResultadoComercio.Exito)
        assertEquals(255, resultado.transaccion.totalCreditos)
        assertEquals(2, resultado.transaccion.cantidadItems)
        assertEquals(305, resultado.saldoActualizado) // 50 inicial + 255 venta

        // Verificar que los fondos se incrementaron en la billetera
        assertEquals(305, sub.billetera.saldo)

        // Verificar que la bodega quedó completamente vacía
        assertEquals(0.0, bodega.pesoActual, 0.001)
        assertEquals(0, bodega.cantidadItems)
        assertEquals(0, bodega.valorTotal)
        assertTrue(bodega.obtenerElementos().isEmpty())

        // Verificar que el peso del submarino se restableció al peso base
        assertEquals(sub.pesoBase, sub.pesoTotal, 0.001)
        assertEquals(1.0, sub.factorAscenso, 0.001)
    }

    @Test
    fun `test intento de venta con nave no acoplada es rechazado`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val billetera = Monedero(saldoInicial = 100)
        val sub = Submarino(
            posX = 0.0,
            posY = 50.0, // Sumergido, sin acoplar
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = billetera,
            notificador = NotificadorSilencioso
        )

        bodega.agregar(Mineral("M1", TipoMineral.ARENA_DE_CUARZO, 0, 50))
        assertFalse(sub.estaAcoplado)

        val servicio = ServicioComercio()
        val resultado = servicio.venderBodegaSubmarino(sub)

        assertTrue(resultado is ResultadoComercio.NaveNoAcoplada)
        // Fondos intactos
        assertEquals(100, billetera.saldo)
        // Bodega intacta
        assertEquals(1, bodega.cantidadItems)
        assertEquals(1.0, bodega.pesoActual, 0.001)
    }

    @Test
    fun `test intento de venta con bodega vacia informa BodegaVacia`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val billetera = Monedero(saldoInicial = 200)
        val sub = Submarino(
            posX = 0.0,
            posY = 0.0,
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = billetera,
            notificador = NotificadorSilencioso
        )

        sub.acoplar()
        assertTrue(sub.estaAcoplado)
        assertEquals(0, bodega.cantidadItems)

        val servicio = ServicioComercio()
        val resultado = servicio.venderBodegaSubmarino(sub)

        assertTrue(resultado is ResultadoComercio.BodegaVacia)
        assertEquals(200, billetera.saldo)
    }

    @Test
    fun `test multiples ventas consecutivas acumulan creditos correctamente`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val sub = Submarino(
            posX = 0.0,
            posY = 0.0,
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = Monedero(0),
            notificador = NotificadorSilencioso
        )
        sub.acoplar()

        val servicio = ServicioComercio()

        // Primera venta: 1 Arena de cuarzo ($10)
        bodega.agregar(Mineral("M1", TipoMineral.ARENA_DE_CUARZO, 0, 0))
        val res1 = servicio.venderBodegaSubmarino(sub)
        assertTrue(res1 is ResultadoComercio.Exito)
        assertEquals(10, sub.billetera.saldo)

        // Segunda venta: 1 Nódulo de manganeso ($90)
        bodega.agregar(Mineral("M2", TipoMineral.NODULO_DE_MANGANESO, 0, 0))
        val res2 = servicio.venderBodegaSubmarino(sub)
        assertTrue(res2 is ResultadoComercio.Exito)
        assertEquals(100, sub.billetera.saldo) // 10 + 90
    }
}
