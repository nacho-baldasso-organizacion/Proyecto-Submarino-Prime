package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * Pruebas de compras exitosas, débito y aumento de estadísticas del submarino.
 */
class Issue8ComprasTest {

    @Test
    fun `test compra exitosa de capacidad de bodega debita saldo e incrementa capacidad`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val monedero = Monedero(saldoInicial = 200)
        val sub = crearSubmarino(bodega = bodega, billetera = monedero)

        val mejora = FabricaMejoras.crearMejoraBodega(
            incrementoCapacidadKg = 15.0,
            costoBase = 100,
            incrementoPorNivel = 50
        )
        val servicio = FabricaDatosPrueba.crearServicioUpgrades(mejora)

        assertEquals(1, mejora.nivel)
        assertEquals(100, mejora.costo)
        assertEquals(20.0, bodega.capacidadMaxima)
        assertEquals(200, monedero.saldo)

        val resultado = servicio.comprarMejora(mejora.id, sub.billetera, sub)

        verificarCompraExitosa(resultado, nivelAnterior = 1, nuevoNivel = 2, costo = 100, saldo = 100)
        assertEquals(100, monedero.saldo)
        assertEquals(35.0, bodega.capacidadMaxima)
        assertEquals(2, mejora.nivel)
        assertEquals(150, mejora.costo)
    }

    @Test
    fun `test bodega mejorada permite albergar mayor cantidad y peso de minerales`() {
        val bodega = Bodega(capacidadMaxima = 10.0)
        val sub = crearSubmarino(bodega = bodega, billetera = Monedero(300))

        val min1 = Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 0)
        val min2 = Mineral("M2", TipoMineral.TITANIO_CRISTALINO, 0, 1)
        val min3 = Mineral("M3", TipoMineral.TITANIO_CRISTALINO, 0, 2)

        assertTrue(bodega.agregar(min1))
        assertTrue(bodega.agregar(min2))
        assertFalse(bodega.agregar(min3), "Debe rechazarse porque la capacidad máxima es 10.0 kg")

        val tienda = FabricaMejoras.crearTiendaEstandar()
        val resultado = tienda.comprarParaSubmarino(FabricaMejoras.ID_BODEGA, sub)
        assertIs<ResultadoCompra.Exito>(resultado)
        assertEquals(25.0, bodega.capacidadMaxima)

        assertTrue(bodega.agregar(min3), "Tras la mejora, el tercer mineral debe entrar con éxito")
        assertEquals(15.0, bodega.pesoActual)
        assertEquals(3, bodega.cantidadItems)
    }

    @Test
    fun `test compra exitosa de velocidad de ascenso incrementa velocidad nominal y efectiva`() {
        val sub = crearSubmarino(
            billetera = Monedero(500),
            velocidadAscensoBase = 5.0
        )

        val mejora = FabricaMejoras.crearMejoraVelocidadAscenso(
            incrementoVelocidadMs = 2.5,
            costoBase = 120
        )
        val servicio = FabricaDatosPrueba.crearServicioUpgrades(mejora)

        val resultado = servicio.comprarMejora(mejora.id, sub.billetera, sub)

        assertIs<ResultadoCompra.Exito>(resultado)
        assertEquals(380, sub.billetera.saldo)
        assertEquals(7.5, sub.velocidadAscensoBase, 0.001)
        assertEquals(7.5, sub.velocidadAscenso, 0.001)
    }

    @Test
    fun `test compra exitosa de resistencia de casco permite tolerar mayor sobrepresion`() {
        val casco = ModuloCasco(presionMaximaPa = 100_000.0, integridad = 100.0)
        val sub = crearSubmarino(casco = casco, billetera = Monedero(300))

        val mejora = FabricaMejoras.crearMejoraResistenciaCasco(
            incrementoPresionPa = 100_000.0,
            costoBase = 150
        )
        val servicio = FabricaDatosPrueba.crearServicioUpgrades(mejora)

        val res = servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertIs<ResultadoCompra.Exito>(res)
        assertEquals(200_000.0, sub.casco.presionMaximaPa)

        val integ = sub.verificarIntegridad(150_000.0)
        assertEquals(100.0, integ)
        assertFalse(sub.estaDestruido)
    }

    @Test
    fun `test compra exitosa de velocidad de propulsion horizontal incrementa velocidad de motor`() {
        val sub = crearSubmarino(
            velocidad = 8.0,
            billetera = Monedero(100)
        )

        val mejora = FabricaMejoras.crearMejoraVelocidadPropulsion(
            incrementoVelocidadMs = 3.0,
            costoBase = 80
        )
        val servicio = FabricaDatosPrueba.crearServicioUpgrades(mejora)

        val res = servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertIs<ResultadoCompra.Exito>(res)
        assertEquals(20, sub.billetera.saldo)
        assertEquals(11.0, sub.velocidad, 0.001)
    }

    @Test
    fun `test compra con saldo exacto deja el monedero en cero`() {
        val sub = crearSubmarino(billetera = Monedero(100))

        val mejora = FabricaMejoras.crearMejoraBodega(costoBase = 100)
        val servicio = FabricaDatosPrueba.crearServicioUpgrades(mejora)

        val res = servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertIs<ResultadoCompra.Exito>(res)
        assertEquals(0, sub.billetera.saldo)
        assertEquals(0, res.saldoRestante)
    }

    private fun crearSubmarino(
        bodega: Almacenamiento? = Bodega(Submarino.BODEGA_CAPACIDAD_DEFAULT),
        billetera: Billetera = Monedero(0),
        velocidad: Double = 10.0,
        casco: Casco = ModuloCasco(500_000.0),
        velocidadAscensoBase: Double =
            if (velocidad > 0.0) velocidad else Submarino.VELOCIDAD_ASCENSO_DEFAULT
    ): Submarino = FabricaDatosPrueba.crearSubmarino(
        velocidad = velocidad,
        casco = casco,
        bodega = bodega,
        billetera = billetera,
        velocidadAscensoBase = velocidadAscensoBase
    )

    private fun verificarCompraExitosa(
        resultado: ResultadoCompra,
        nivelAnterior: Int,
        nuevoNivel: Int,
        costo: Int,
        saldo: Int
    ) {
        val exito = assertIs<ResultadoCompra.Exito>(resultado)
        assertEquals(nivelAnterior, exito.nivelAnterior)
        assertEquals(nuevoNivel, exito.nuevoNivel)
        assertEquals(costo, exito.costoPagado)
        assertEquals(saldo, exito.saldoRestante)
    }
}
