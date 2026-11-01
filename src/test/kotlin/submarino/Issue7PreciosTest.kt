package submarino

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Pruebas de estrategias de precios y cálculo de valores de venta (OCP).
 */
class Issue7PreciosTest {

    @Test
    fun `test estrategia precios base valua correctamente segun catalogo oficial`() {
        val estrategia = EstrategiaPreciosBase()

        val arena = Mineral("M1", TipoMineral.ARENA_DE_CUARZO, 0, 0)
        val cobre = Mineral("M2", TipoMineral.COBRE_MARINO, 0, 0)
        val nodulo = Mineral("M3", TipoMineral.NODULO_DE_MANGANESO, 0, 0)
        val titanio = Mineral("M4", TipoMineral.TITANIO_CRISTALINO, 0, 0)
        val gema = Mineral("M5", TipoMineral.GEMA_DE_SALMUERA, 0, 0)
        val artefacto = Mineral("M6", TipoMineral.ARTEFACTO_HUNDIDO, 0, 0)

        assertEquals(10, estrategia.calcularPrecio(arena))
        assertEquals(35, estrategia.calcularPrecio(cobre))
        assertEquals(90, estrategia.calcularPrecio(nodulo))
        assertEquals(220, estrategia.calcularPrecio(titanio))
        assertEquals(500, estrategia.calcularPrecio(gema))
        assertEquals(1200, estrategia.calcularPrecio(artefacto))
    }

    @Test
    fun `test estrategia precios multiplicador bonifica las ventas (OCP)`() {
        val estrategiaBonus = EstrategiaPreciosMultiplicador(factor = 1.5) // +50% de bonus
        val mineral = Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 0) // $220 base

        // 220 * 1.5 = 330
        assertEquals(330, estrategiaBonus.calcularPrecio(mineral))
    }

    @Test
    fun `test extension OCP admite nuevo vendible sin modificar clases existentes`() {
        // Creamos un nuevo recurso vendible que no es un mineral
        val nuevoTesoro = object : Vendible {
            override val valor: Int = 2500
        }

        val calculadora = CalculadoraPreciosEstandar()
        assertEquals(2500, calculadora.calcularPrecio(nuevoTesoro))
        assertEquals(2500, calculadora.calcularTotal(listOf(nuevoTesoro)))
    }

    @Test
    fun `test calculadora precios estandar totaliza correctamente colecciones`() {
        val calculadora = CalculadoraPreciosEstandar()
        val items = listOf(
            Mineral("M1", TipoMineral.ARENA_DE_CUARZO, 0, 0),      // $10
            Mineral("M2", TipoMineral.COBRE_MARINO, 0, 0),         // $35
            Mineral("M3", TipoMineral.NODULO_DE_MANGANESO, 0, 0)   // $90
        )

        assertEquals(135, calculadora.calcularTotal(items))
        assertEquals(0, calculadora.calcularTotal(emptyList()))
    }
}
