package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * Pruebas de venta de bodega, economía y vaciado tras la transacción.
 */
class Issue7ComercioTest {

    @Test
    fun `test venta de bodega exitosa incrementa fondos y vacia el almacenamiento`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val billetera = Monedero(saldoInicial = 50)
        val sub = crearSubmarino(bodega = bodega, billetera = billetera)

        cargar(
            bodega,
            Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 0),
            Mineral("M2", TipoMineral.COBRE_MARINO, 0, 0)
        )
        verificarBodega(bodega, peso = 7.5, items = 2, valor = 255)
        assertEquals(50, billetera.saldo)

        sub.acoplar()
        assertTrue(sub.estaAcoplado)

        val resultado = ServicioComercio().venderBodegaSubmarino(sub)

        verificarVentaExitosa(resultado, total = 255, cantidad = 2, saldo = 305, bodega = bodega)
        assertEquals(305, sub.billetera.saldo)
        assertEquals(sub.pesoBase, sub.pesoTotal, 0.001)
        assertEquals(1.0, sub.factorAscenso, 0.001)
    }

    @Test
    fun `test intento de venta con nave no acoplada es rechazado`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val billetera = Monedero(saldoInicial = 100)
        val sub = crearSubmarino(bodega = bodega, billetera = billetera, posY = 50.0)

        bodega.agregar(Mineral("M1", TipoMineral.ARENA_DE_CUARZO, 0, 50))
        assertFalse(sub.estaAcoplado)

        val resultado = ServicioComercio().venderBodegaSubmarino(sub)

        assertIs<ResultadoComercio.NaveNoAcoplada>(resultado)
        assertEquals(100, billetera.saldo)
        assertEquals(1, bodega.cantidadItems)
        assertEquals(1.0, bodega.pesoActual, 0.001)
    }

    @Test
    fun `test intento de venta con bodega vacia informa BodegaVacia`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val billetera = Monedero(saldoInicial = 200)
        val sub = crearSubmarino(bodega = bodega, billetera = billetera)

        sub.acoplar()
        assertTrue(sub.estaAcoplado)
        assertEquals(0, bodega.cantidadItems)

        val resultado = ServicioComercio().venderBodegaSubmarino(sub)

        assertIs<ResultadoComercio.BodegaVacia>(resultado)
        assertEquals(200, billetera.saldo)
    }

    @Test
    fun `test multiples ventas consecutivas acumulan creditos correctamente`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val sub = crearSubmarino(bodega = bodega, billetera = Monedero(0))
        sub.acoplar()

        val servicio = ServicioComercio()

        bodega.agregar(Mineral("M1", TipoMineral.ARENA_DE_CUARZO, 0, 0))
        val res1 = servicio.venderBodegaSubmarino(sub)
        assertIs<ResultadoComercio.Exito>(res1)
        assertEquals(10, sub.billetera.saldo)

        bodega.agregar(Mineral("M2", TipoMineral.NODULO_DE_MANGANESO, 0, 0))
        val res2 = servicio.venderBodegaSubmarino(sub)
        assertIs<ResultadoComercio.Exito>(res2)
        assertEquals(100, sub.billetera.saldo)
    }

    private fun crearSubmarino(
        bodega: Bodega,
        billetera: Billetera = Monedero(0),
        posY: Double = 0.0
    ): Submarino = FabricaDatosPrueba.crearSubmarino(
        posY = posY,
        bodega = bodega,
        billetera = billetera
    )

    private fun cargar(bodega: Bodega, vararg minerales: Mineral) {
        minerales.forEach { assertTrue(bodega.agregar(it)) }
    }

    private fun verificarBodega(bodega: Bodega, peso: Double, items: Int, valor: Int) {
        assertEquals(peso, bodega.pesoActual, 0.001)
        assertEquals(items, bodega.cantidadItems)
        assertEquals(valor, bodega.valorTotal)
    }

    private fun verificarVentaExitosa(
        resultado: ResultadoComercio,
        total: Int,
        cantidad: Int,
        saldo: Int,
        bodega: Bodega
    ) {
        val exito = assertIs<ResultadoComercio.Exito>(resultado)
        assertEquals(total, exito.transaccion.totalCreditos)
        assertEquals(cantidad, exito.transaccion.cantidadItems)
        assertEquals(saldo, exito.saldoActualizado)
        assertEquals(0.0, bodega.pesoActual, 0.001)
        assertEquals(0, bodega.cantidadItems)
        assertEquals(0, bodega.valorTotal)
        assertTrue(bodega.obtenerElementos().isEmpty())
    }
}
