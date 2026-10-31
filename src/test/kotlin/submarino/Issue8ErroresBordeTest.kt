package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Pruebas de manejo de errores, invariantes y casos borde del dominio de upgrades.
 */
class Issue8ErroresBordeTest {

    @Test
    fun `test compra con identificador inexistente retorna MejoraNoEncontrada`() {
        val sub = FabricaDatosPrueba.crearSubmarino(billetera = Monedero(1000))
        val servicio = ServicioUpgrades(CatalogoUpgrades(emptyList()))

        val resultado = servicio.comprarMejora("ID-FANTASMA", sub.billetera, sub)

        assertTrue(resultado is ResultadoCompra.MejoraNoEncontrada)
        assertEquals("ID-FANTASMA", resultado.mejoraId)
        assertEquals(1000, sub.billetera.saldo)
    }

    @Test
    fun `test validacion de invariantes en modelos de mejora y costo`() {
        verificarInvariantesDeMejora()
        verificarInvariantesDeCosto()
        verificarInvariantesDeEfecto()
    }

    @Test
    fun `test submarino sin bodega ignora mejora de bodega de forma segura`() {
        val subSinBodega = FabricaDatosPrueba.crearSubmarino(
            bodega = null,
            billetera = Monedero(200)
        )

        val mejora = FabricaMejoras.crearMejoraBodega(costoBase = 50)
        val servicio = ServicioUpgrades(CatalogoUpgrades(listOf(mejora)))

        val resultado = servicio.comprarMejora(mejora.id, subSinBodega.billetera, subSinBodega)
        assertTrue(resultado is ResultadoCompra.Exito)
        assertNull(subSinBodega.bodega)
        assertEquals(150, subSinBodega.billetera.saldo)
    }

    private fun verificarInvariantesDeMejora() {
        assertFailsWith<IllegalArgumentException> { FabricaMejoras.crearMejoraBodega(id = "") }
        assertFailsWith<IllegalArgumentException> { FabricaMejoras.crearMejoraBodega(nombre = "  ") }
        assertFailsWith<IllegalArgumentException> { FabricaMejoras.crearMejoraBodega(nivelInicial = 0) }
        assertFailsWith<IllegalArgumentException> {
            FabricaMejoras.crearMejoraBodega(nivelInicial = 3, nivelMaximo = 2)
        }
    }

    private fun verificarInvariantesDeCosto() {
        assertFailsWith<IllegalArgumentException> {
            EstrategiaCostoLineal(costoBase = 0, incrementoPorNivel = 10)
        }
        assertFailsWith<IllegalArgumentException> {
            EstrategiaCostoLineal(costoBase = -50, incrementoPorNivel = 10)
        }
        assertFailsWith<IllegalArgumentException> {
            EstrategiaCostoLineal(costoBase = 100, incrementoPorNivel = -5)
        }
        assertFailsWith<IllegalArgumentException> {
            EstrategiaCostoExponencial(costoBase = 100, factorMultiplicador = 1.0)
        }
        assertFailsWith<IllegalArgumentException> {
            EstrategiaCostoFijo(costoFijo = 0)
        }
    }

    private fun verificarInvariantesDeEfecto() {
        assertFailsWith<IllegalArgumentException> { EfectoCapacidadBodega(magnitud = 0.0) }
        assertFailsWith<IllegalArgumentException> { EfectoVelocidadAscenso(magnitud = -1.0) }
        assertFailsWith<IllegalArgumentException> { EfectoResistenciaCasco(magnitud = -100.0) }
        assertFailsWith<IllegalArgumentException> { EfectoVelocidadPropulsion(magnitud = 0.0) }
    }
}
