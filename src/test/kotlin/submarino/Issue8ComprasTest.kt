package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Pruebas de compras exitosas, débito y aumento de estadísticas del submarino.
 */
class Issue8ComprasTest {

    @Test
    fun `test compra exitosa de capacidad de bodega debita saldo e incrementa capacidad`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val monedero = Monedero(saldoInicial = 200)
        val sub = Submarino(
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = monedero,
            notificador = NotificadorSilencioso
        )

        val mejora = FabricaMejoras.crearMejoraBodega(
            incrementoCapacidadKg = 15.0,
            costoBase = 100,
            incrementoPorNivel = 50
        )
        val catalogo = CatalogoUpgrades(listOf(mejora))
        val servicio = ServicioUpgrades(catalogo)

        assertEquals(1, mejora.nivel)
        assertEquals(100, mejora.costo)
        assertEquals(20.0, bodega.capacidadMaxima)
        assertEquals(200, monedero.saldo)

        val resultado = servicio.comprarMejora(mejora.id, sub.billetera, sub)

        assertTrue(resultado is ResultadoCompra.Exito)
        assertEquals(1, resultado.nivelAnterior)
        assertEquals(2, resultado.nuevoNivel)
        assertEquals(100, resultado.costoPagado)
        assertEquals(100, resultado.saldoRestante)
        assertEquals(100, monedero.saldo)

        // Verificar incremento efectivo en las estadísticas de la bodega
        assertEquals(35.0, bodega.capacidadMaxima)
        assertEquals(2, mejora.nivel)
        assertEquals(150, mejora.costo) // 100 + 50 para nivel 2 -> 3
    }

    @Test
    fun `test bodega mejorada permite albergar mayor cantidad y peso de minerales`() {
        val bodega = Bodega(capacidadMaxima = 10.0) // solo entran 10 kg
        val monedero = Monedero(saldoInicial = 300)
        val sub = Submarino(
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = monedero,
            notificador = NotificadorSilencioso
        )

        // Cargar 2 minerales de titanio (5kg cada uno) -> llena la bodega al 100%
        val min1 = Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 0) // 5kg
        val min2 = Mineral("M2", TipoMineral.TITANIO_CRISTALINO, 0, 1) // 5kg
        val min3 = Mineral("M3", TipoMineral.TITANIO_CRISTALINO, 0, 2) // 5kg

        assertTrue(bodega.agregar(min1))
        assertTrue(bodega.agregar(min2))
        assertFalse(bodega.agregar(min3), "Debe rechazarse porque la capacidad máxima es 10.0 kg")

        // Comprar mejora de bodega (+15.0 kg -> nueva capacidad = 25.0 kg)
        val tienda = FabricaMejoras.crearTiendaEstandar()
        val resultado = tienda.comprarParaSubmarino(FabricaMejoras.ID_BODEGA, sub)
        assertTrue(resultado is ResultadoCompra.Exito)
        assertEquals(25.0, bodega.capacidadMaxima)

        // Ahora el tercer mineral sí debe ingresar exitosamente
        assertTrue(bodega.agregar(min3), "Tras la mejora, el tercer mineral debe entrar con éxito")
        assertEquals(15.0, bodega.pesoActual)
        assertEquals(3, bodega.cantidadItems)
    }

    @Test
    fun `test compra exitosa de velocidad de ascenso incrementa velocidad nominal y efectiva`() {
        val sub = Submarino(
            casco = ModuloCasco(500_000.0),
            velocidadAscensoBase = 5.0,
            billetera = Monedero(saldoInicial = 500),
            notificador = NotificadorSilencioso
        )

        val mejora = FabricaMejoras.crearMejoraVelocidadAscenso(
            incrementoVelocidadMs = 2.5,
            costoBase = 120
        )
        val catalogo = CatalogoUpgrades(listOf(mejora))
        val servicio = ServicioUpgrades(catalogo)

        val resultado = servicio.comprarMejora(mejora.id, sub.billetera, sub)

        assertTrue(resultado is ResultadoCompra.Exito)
        assertEquals(380, sub.billetera.saldo) // 500 - 120
        assertEquals(7.5, sub.velocidadAscensoBase, 0.001)
        assertEquals(7.5, sub.velocidadAscenso, 0.001)
    }

    @Test
    fun `test compra exitosa de resistencia de casco permite tolerar mayor sobrepresion`() {
        val casco = ModuloCasco(presionMaximaPa = 100_000.0, integridad = 100.0)
        val sub = Submarino(
            casco = casco,
            billetera = Monedero(saldoInicial = 300),
            notificador = NotificadorSilencioso
        )

        val mejora = FabricaMejoras.crearMejoraResistenciaCasco(
            incrementoPresionPa = 100_000.0,
            costoBase = 150
        )
        val catalogo = CatalogoUpgrades(listOf(mejora))
        val servicio = ServicioUpgrades(catalogo)

        // Sin mejora, una presión de 150,000 Pa generaría daño porque el límite es 100,000 Pa
        // Comprar mejora: límite pasa a 200,000 Pa
        val res = servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertTrue(res is ResultadoCompra.Exito)
        assertEquals(200_000.0, sub.casco.presionMaximaPa)

        // Someter a 150,000 Pa: como 150,000 <= 200,000, la integridad se mantiene intacta en 100.0%
        val integ = sub.verificarIntegridad(150_000.0)
        assertEquals(100.0, integ)
        assertFalse(sub.estaDestruido)
    }

    @Test
    fun `test compra exitosa de velocidad de propulsion horizontal incrementa velocidad de motor`() {
        val sub = Submarino(
            velocidad = 8.0,
            casco = ModuloCasco(500_000.0),
            billetera = Monedero(saldoInicial = 100),
            notificador = NotificadorSilencioso
        )

        val mejora = FabricaMejoras.crearMejoraVelocidadPropulsion(
            incrementoVelocidadMs = 3.0,
            costoBase = 80
        )
        val catalogo = CatalogoUpgrades(listOf(mejora))
        val servicio = ServicioUpgrades(catalogo)

        val res = servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertTrue(res is ResultadoCompra.Exito)
        assertEquals(20, sub.billetera.saldo)
        assertEquals(11.0, sub.velocidad, 0.001)
    }

    @Test
    fun `test compra con saldo exacto deja el monedero en cero`() {
        val sub = Submarino(
            casco = ModuloCasco(500_000.0),
            billetera = Monedero(saldoInicial = 100),
            notificador = NotificadorSilencioso
        )

        val mejora = FabricaMejoras.crearMejoraBodega(costoBase = 100)
        val servicio = ServicioUpgrades(CatalogoUpgrades(listOf(mejora)))

        val res = servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertTrue(res is ResultadoCompra.Exito)
        assertEquals(0, sub.billetera.saldo)
        assertEquals(0, res.saldoRestante)
    }
}
