package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Pruebas de integración económica completa (Issue #7 + Issue #8) y representación textual.
 */
class Issue8Test {

    @Test
    fun `test ciclo integral mineria venta en superficie y compra de mejoras`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val monedero = Monedero(saldoInicial = 0)
        val submarino = crearSubmarinoIntegracion(bodega, monedero)
        val tienda = FabricaMejoras.crearTiendaEstandar()

        val resFallo = tienda.comprarParaSubmarino(FabricaMejoras.ID_BODEGA, submarino)
        assertTrue(resFallo is ResultadoCompra.FondosInsuficientes)

        cargarMineralesDeExpedicion(bodega)
        assertEquals(3, bodega.cantidadItems)

        submarino.acoplar()
        val resVenta = ServicioComercio().venderBodegaSubmarino(submarino)
        assertTrue(resVenta is ResultadoComercio.Exito)
        assertEquals(1920, submarino.billetera.saldo)
        assertEquals(0, bodega.cantidadItems)

        val compras = comprarMejorasEstandar(tienda, submarino)
        verificarComprasExitosas(compras)

        assertEquals(35.0, bodega.capacidadMaxima)
        assertEquals(7.0, submarino.velocidadAscensoBase, 0.001)
        assertEquals(350_000.0, submarino.casco.presionMaximaPa)
        assertEquals(12.5, submarino.velocidad, 0.001)
        assertEquals(1470, submarino.billetera.saldo)
    }

    @Test
    fun `test toString y representacion legible de mejoras y resultados`() {
        val mejora = FabricaMejoras.crearMejoraBodega()
        val strMejora = mejora.toString()
        assertTrue(strMejora.contains("UPG-BODEGA"))
        assertTrue(strMejora.contains("1/5"))

        val exito = ResultadoCompra.Exito(mejora, 1, 2, 100, 50)
        assertTrue(exito.toString().contains("nuevoNivel=2"))

        val fondos = ResultadoCompra.FondosInsuficientes("UPG-1", 100, 20)
        assertTrue(fondos.toString().contains("saldoDisponible=20"))

        val cap = ResultadoCompra.NivelMaximoAlcanzado("UPG-1", 5, 5)
        assertTrue(cap.toString().contains("nivelActual=5"))

        val noEncontrada = ResultadoCompra.MejoraNoEncontrada("UPG-X")
        assertTrue(noEncontrada.toString().contains("UPG-X"))
    }

    private fun crearSubmarinoIntegracion(
        bodega: Bodega,
        billetera: Billetera
    ): Submarino = FabricaDatosPrueba.crearSubmarino(
        velocidad = 10.0,
        casco = ModuloCasco(200_000.0),
        bodega = bodega,
        billetera = billetera,
        velocidadAscensoBase = 5.0
    )

    private fun cargarMineralesDeExpedicion(bodega: Bodega) {
        bodega.agregar(Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 50))
        bodega.agregar(Mineral("M2", TipoMineral.GEMA_DE_SALMUERA, 0, 80))
        bodega.agregar(Mineral("M3", TipoMineral.ARTEFACTO_HUNDIDO, 0, 100))
    }

    private fun comprarMejorasEstandar(
        tienda: TiendaMejoras,
        submarino: Submarino
    ): List<ResultadoCompra> = listOf(
        tienda.comprarParaSubmarino(FabricaMejoras.ID_BODEGA, submarino),
        tienda.comprarParaSubmarino(FabricaMejoras.ID_ASCENSO, submarino),
        tienda.comprarParaSubmarino(FabricaMejoras.ID_CASCO, submarino),
        tienda.comprarParaSubmarino(FabricaMejoras.ID_PROPULSION, submarino)
    )

    private fun verificarComprasExitosas(compras: List<ResultadoCompra>) {
        compras.forEach { assertTrue(it is ResultadoCompra.Exito) }
    }
}
