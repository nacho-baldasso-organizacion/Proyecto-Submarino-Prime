package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Pruebas de estrategias de costo y extensibilidad (OCP).
 */
class Issue8EstrategiasCostoTest {

    @Test
    fun `test estrategia costo lineal escala aditivamente por nivel`() {
        val estrategia = EstrategiaCostoLineal(costoBase = 100, incrementoPorNivel = 50)

        assertEquals(100, estrategia.calcularCosto(nivelActual = 1))
        assertEquals(150, estrategia.calcularCosto(nivelActual = 2))
        assertEquals(200, estrategia.calcularCosto(nivelActual = 3))
        assertEquals(250, estrategia.calcularCosto(nivelActual = 4))
    }

    @Test
    fun `test estrategia costo exponencial escala geometricamente`() {
        val estrategia = EstrategiaCostoExponencial(costoBase = 100, factorMultiplicador = 2.0)

        // Nivel 1: 100 * 2^0 = 100
        assertEquals(100, estrategia.calcularCosto(nivelActual = 1))
        // Nivel 2: 100 * 2^1 = 200
        assertEquals(200, estrategia.calcularCosto(nivelActual = 2))
        // Nivel 3: 100 * 2^2 = 400
        assertEquals(400, estrategia.calcularCosto(nivelActual = 3))
    }

    @Test
    fun `test estrategia costo fijo mantiene valor constante`() {
        val estrategia = EstrategiaCostoFijo(costoFijo = 300)

        assertEquals(300, estrategia.calcularCosto(nivelActual = 1))
        assertEquals(300, estrategia.calcularCosto(nivelActual = 2))
        assertEquals(300, estrategia.calcularCosto(nivelActual = 10))
    }

    @Test
    fun `test extension OCP admite nueva estrategia de costo sin modificar clases existentes`() {
        // Nueva estrategia con descuento por fidelidad (-10% por nivel)
        val estrategiaFidelidad = object : EstrategiaCosto {
            override fun calcularCosto(nivelActual: Int): Int = maxOf(1000 - (nivelActual * 100), 100)
        }

        assertEquals(900, estrategiaFidelidad.calcularCosto(1))
        assertEquals(800, estrategiaFidelidad.calcularCosto(2))

        val mejora = Mejora(
            id = "UPG-CUSTOM",
            nombre = "Blindaje Cuántico",
            descripcion = "Efecto experimental",
            efecto = EfectoCapacidadBodega(20.0),
            estrategiaCosto = estrategiaFidelidad
        )

        assertEquals(900, mejora.costo)
    }

    @Test
    fun `test extension OCP admite nuevo efecto de mejora sin modificar el servicio de compra`() {
        // Nuevo efecto que recarga batería al 100%
        var efectoAplicado = false
        val nuevoEfecto = object : EfectoMejora {
            override val magnitud: Double = 100.0
            override fun aplicar(submarino: SubmarinoMejorable) {
                efectoAplicado = true
            }
        }

        val mejoraNueva = Mejora(
            id = "UPG-BATERIA",
            nombre = "Pila de Fusión",
            descripcion = "Recarga instantánea",
            efecto = nuevoEfecto,
            estrategiaCosto = EstrategiaCostoFijo(50)
        )

        val sub = Submarino(casco = ModuloCasco(500_000.0), billetera = Monedero(100), notificador = NotificadorSilencioso)
        val servicio = ServicioUpgrades(CatalogoUpgrades(listOf(mejoraNueva)))

        val resultado = servicio.comprarMejora("UPG-BATERIA", sub.billetera, sub)
        assertTrue(resultado is ResultadoCompra.Exito)
        assertTrue(efectoAplicado)
        assertEquals(50, sub.billetera.saldo)
    }
}
