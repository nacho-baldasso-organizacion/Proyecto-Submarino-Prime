package submarino

import kotlin.test.Test
import kotlin.test.assertEquals

class Issue6CalculadorPesoTest {

    @Test
    fun testCalculadorDePesoDesacopladoConDIPySRP() {
        val mockAlmacenamiento = object : Almacenamiento {
            override val pesoActual: Double = 35.0
            override val capacidadMaxima: Double = 100.0
            override fun agregar(item: Recolectable): Boolean = true
            override fun obtenerElementos(): List<Recolectable> = emptyList()
        }

        val calculador = CalculadorPesoSubmarino()
        val pesoTotal = calculador.calcularPesoTotal(pesoBase = 500.0, almacenamiento = mockAlmacenamiento)
        assertEquals(535.0, pesoTotal, 0.001)

        val calculadorCustom = object : CalculadorPeso {
            override fun calcularPesoTotal(pesoBase: Double, almacenamiento: Almacenamiento?): Double {
                return (pesoBase + (almacenamiento?.pesoActual ?: 0.0)) * 1.1
            }
        }

        val subCustom = Submarino(
            casco = ModuloCasco(500_000.0),
            pesoBase = 1000.0,
            bodega = mockAlmacenamiento,
            calculadorPeso = calculadorCustom,
            notificador = NotificadorSilencioso
        )

        assertEquals(1138.5, subCustom.pesoTotal, 0.001)
    }
}
