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
        val submarino = Submarino(
            posX = 0.0,
            posY = 0.0,
            velocidad = 10.0,
            casco = ModuloCasco(200_000.0),
            bodega = bodega,
            velocidadAscensoBase = 5.0,
            billetera = monedero,
            notificador = NotificadorSilencioso
        )

        val tienda = FabricaMejoras.crearTiendaEstandar()

        // 1. Sin fondos, no puede comprar nada
        val resFallo = tienda.comprarParaSubmarino(FabricaMejoras.ID_BODEGA, submarino)
        assertTrue(resFallo is ResultadoCompra.FondosInsuficientes)

        // 2. Expedición y recolección
        bodega.agregar(Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 50)) // $220
        bodega.agregar(Mineral("M2", TipoMineral.GEMA_DE_SALMUERA, 0, 80))    // $500
        bodega.agregar(Mineral("M3", TipoMineral.ARTEFACTO_HUNDIDO, 0, 100))  // $1200
        assertEquals(3, bodega.cantidadItems)

        // 3. Acople y venta comercial (Issue #7)
        submarino.acoplar()
        val servicioComercio = ServicioComercio()
        val resVenta = servicioComercio.venderBodegaSubmarino(submarino)
        assertTrue(resVenta is ResultadoComercio.Exito)
        assertEquals(1920, submarino.billetera.saldo)
        assertEquals(0, bodega.cantidadItems)

        // 4. Inversión en Upgrades (Issue #8)
        val resBodega = tienda.comprarParaSubmarino(FabricaMejoras.ID_BODEGA, submarino)
        val resAscenso = tienda.comprarParaSubmarino(FabricaMejoras.ID_ASCENSO, submarino)
        val resCasco = tienda.comprarParaSubmarino(FabricaMejoras.ID_CASCO, submarino)
        val resMotor = tienda.comprarParaSubmarino(FabricaMejoras.ID_PROPULSION, submarino)

        assertTrue(resBodega is ResultadoCompra.Exito)
        assertTrue(resAscenso is ResultadoCompra.Exito)
        assertTrue(resCasco is ResultadoCompra.Exito)
        assertTrue(resMotor is ResultadoCompra.Exito)

        // 5. Verificar estadísticas consolidadas del submarino
        assertEquals(35.0, bodega.capacidadMaxima)
        assertEquals(7.0, submarino.velocidadAscensoBase, 0.001)
        assertEquals(350_000.0, submarino.casco.presionMaximaPa)
        assertEquals(12.5, submarino.velocidad, 0.001)

        // Costos: 100 (bodega) + 120 (ascenso) + 150 (casco) + 80 (propulsion) = 450
        // 1920 - 450 = 1470
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
}
