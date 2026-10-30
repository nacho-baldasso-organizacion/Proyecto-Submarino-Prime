package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Pruebas de rechazo de compras por fondos insuficientes.
 */
class Issue8FondosInsuficientesTest {

    @Test
    fun `test rechazo de compra si saldo es menor al costo requerido`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val monedero = Monedero(saldoInicial = 75)
        val sub = FabricaDatosPrueba.crearSubmarino(bodega = bodega, billetera = monedero)

        val mejora = FabricaMejoras.crearMejoraBodega(costoBase = 100)
        val servicio = crearServicio(mejora)

        val resultado = servicio.comprarMejora(mejora.id, sub.billetera, sub)

        assertTrue(resultado is ResultadoCompra.FondosInsuficientes)
        assertEquals(mejora.id, resultado.mejoraId)
        assertEquals(100, resultado.costoRequerido)
        assertEquals(75, resultado.saldoDisponible)
        assertEquals(75, monedero.saldo)
        assertEquals(1, mejora.nivel)
        assertEquals(20.0, bodega.capacidadMaxima)
    }

    @Test
    fun `test rechazo de compra si saldo es cero`() {
        val sub = FabricaDatosPrueba.crearSubmarino(billetera = Monedero(0))

        val mejora = FabricaMejoras.crearMejoraVelocidadAscenso(costoBase = 120)
        val servicio = crearServicio(mejora)

        val resultado = servicio.comprarMejora(mejora.id, sub.billetera, sub)

        assertTrue(resultado is ResultadoCompra.FondosInsuficientes)
        assertEquals(0, sub.billetera.saldo)
        assertEquals(1, mejora.nivel)
    }

    private fun crearServicio(vararg mejoras: Mejora): ServicioUpgrades =
        ServicioUpgrades(CatalogoUpgrades(mejoras.toList()))
}
