package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Pruebas de arquitectura SOLID e integración del dominio de comercio.
 */
class Issue7Test {

    @Test
    fun `test inyeccion de dependencias DIP en ServicioComercio`() {
        // Mock de CalculadoraPrecios inyectada
        val mockCalculadora = object : CalculadoraPrecios {
            override fun calcularPrecio(item: Vendible): Int = 999
            override fun calcularTotal(items: Collection<Vendible>): Int = items.size * 999
        }

        val servicio = ServicioComercio(calculadoraPrecios = mockCalculadora)

        val bodega = Bodega(capacidadMaxima = 10.0)
        bodega.agregar(Mineral("M1", TipoMineral.ARENA_DE_CUARZO, 0, 0))
        bodega.agregar(Mineral("M2", TipoMineral.ARENA_DE_CUARZO, 0, 0))

        val naveMock = object : Acoplable {
            override val estaAcoplado: Boolean = true
            override fun acoplar() {}
            override fun desacoplar() {}
        }

        val receptorMock = object : ReceptorFondos {
            override var saldo: Int = 0
            override fun acreditar(monto: Int) {
                saldo += monto
            }
        }

        val resultado = servicio.venderBodega(naveMock, bodega, receptorMock)
        assertTrue(resultado is ResultadoComercio.Exito)
        assertEquals(1998, resultado.transaccion.totalCreditos) // 2 * 999
        assertEquals(1998, receptorMock.saldo)
    }

    @Test
    fun `test limites declarados de contratos cumplen con Interface Segregation (ISP)`() {
        // Validamos reflexivamente que las interfaces del sistema comercial no superen 2 métodos declarados
        val interfaces = listOf(
            Vendible::class.java,
            ReceptorFondos::class.java,
            Pagable::class.java,
            ConsultableSaldo::class.java,
            EstadoAcople::class.java,
            Acoplable::class.java,
            ZonaAcople::class.java,
            ObservadorAcople::class.java,
            ObservadorComercio::class.java,
            EstrategiaPrecios::class.java,
            CalculadoraPrecios::class.java,
            ControlAcople::class.java,
            ConsultaAcople::class.java,
            ProcesadorComercio::class.java
        )

        for (iface in interfaces) {
            val metodosDeclarados = iface.declaredMethods.size
            assertTrue(
                metodosDeclarados <= 2,
                "La interfaz ${iface.simpleName} declara $metodosDeclarados métodos, violando la regla de <= 2 métodos"
            )
        }
    }

    @Test
    fun `test intento de venta sin acoplar en submarino sin bodega retorna NaveNoAcoplada`() {
        val subSinBodega = Submarino(
            posX = 0.0,
            posY = 100.0,
            casco = ModuloCasco(500_000.0),
            bodega = null,
            notificador = NotificadorSilencioso
        )
        assertFalse(subSinBodega.estaAcoplado)

        val servicio = ServicioComercio()
        val resultado = servicio.venderBodegaSubmarino(subSinBodega)
        assertEquals(ResultadoComercio.NaveNoAcoplada, resultado)
    }

    @Test
    fun `test desplazamiento de submarino amarrado libera la estacion de acople automaticamente`() {
        val estacion = EstacionAcople()
        val sub = Submarino(posX = 0.0, posY = 0.0, casco = ModuloCasco(500_000.0), notificador = NotificadorSilencioso)

        assertTrue(estacion.acoplar(sub, 0.0, 0.0))
        assertTrue(sub.estaAcoplado)
        assertTrue(estacion.hayNaveAcoplada)

        // El submarino navega hacia adelante (se desacopla internamente)
        sub.navegar(15.0)
        assertFalse(sub.estaAcoplado)

        // La estación reconoce reactivamente que la nave se alejó y queda disponible
        assertFalse(estacion.hayNaveAcoplada)
        assertTrue(estacion.puedeAcoplar(0.0, 0.0))
    }

    @Test
    fun `test submarino destruido no puede acoplarse`() {
        val sub = Submarino(
            posX = 0.0,
            posY = 0.0,
            casco = ModuloCasco(presionMaximaPa = 100_000.0, integridad = 0.1),
            notificador = NotificadorSilencioso
        )

        // Provocar destrucción por sobrepresión
        sub.verificarIntegridad(500_000.0)
        assertTrue(sub.estaDestruido)

        // Intentar acople
        sub.acoplar()
        assertFalse(sub.estaAcoplado, "Un submarino destruido no puede acoplarse")
    }

    @Test
    fun `test toString de ResultadoComercio usa formato limpio con data object`() {
        assertEquals("NaveNoAcoplada", ResultadoComercio.NaveNoAcoplada.toString())
        assertEquals("BodegaVacia", ResultadoComercio.BodegaVacia.toString())
    }
}
