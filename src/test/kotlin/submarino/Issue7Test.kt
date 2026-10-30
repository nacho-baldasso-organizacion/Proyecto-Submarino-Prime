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
        val servicio = ServicioComercio(calculadoraPrecios = crearCalculadoraMock())

        val bodega = Bodega(capacidadMaxima = 10.0)
        FabricaDatosPrueba.cargarMinerales(bodega, cantidad = 2, tipo = TipoMineral.ARENA_DE_CUARZO, desde = 1)

        val receptorMock = crearReceptorFondosMock()
        val resultado = servicio.venderBodega(crearNaveAcopladaMock(), bodega, receptorMock)

        assertTrue(resultado is ResultadoComercio.Exito)
        assertEquals(1998, resultado.transaccion.totalCreditos)
        assertEquals(1998, receptorMock.saldo)
    }

    @Test
    fun `test limites declarados de contratos cumplen con Interface Segregation (ISP)`() {
        for (iface in interfacesComerciales()) {
            val metodosDeclarados = iface.declaredMethods.size
            assertTrue(
                metodosDeclarados <= 2,
                "La interfaz ${iface.simpleName} declara $metodosDeclarados métodos, violando la regla de <= 2 métodos"
            )
        }
    }

    @Test
    fun `test intento de venta sin acoplar en submarino sin bodega retorna NaveNoAcoplada`() {
        val subSinBodega = FabricaDatosPrueba.crearSubmarino(posY = 100.0, bodega = null)
        assertFalse(subSinBodega.estaAcoplado)

        val resultado = ServicioComercio().venderBodegaSubmarino(subSinBodega)
        assertEquals(ResultadoComercio.NaveNoAcoplada, resultado)
    }

    @Test
    fun `test desplazamiento de submarino amarrado libera la estacion de acople automaticamente`() {
        val estacion = EstacionAcople()
        val sub = FabricaDatosPrueba.crearSubmarino()

        assertTrue(estacion.acoplar(sub, 0.0, 0.0))
        assertTrue(sub.estaAcoplado)
        assertTrue(estacion.hayNaveAcoplada)

        sub.navegar(15.0)
        assertFalse(sub.estaAcoplado)

        assertFalse(estacion.hayNaveAcoplada)
        assertTrue(estacion.puedeAcoplar(0.0, 0.0))
    }

    @Test
    fun `test submarino destruido no puede acoplarse`() {
        val sub = FabricaDatosPrueba.crearSubmarino(
            casco = ModuloCasco(presionMaximaPa = 100_000.0, integridad = 0.1)
        )

        sub.verificarIntegridad(500_000.0)
        assertTrue(sub.estaDestruido)

        sub.acoplar()
        assertFalse(sub.estaAcoplado, "Un submarino destruido no puede acoplarse")
    }

    @Test
    fun `test toString de ResultadoComercio usa formato limpio con data object`() {
        assertEquals("NaveNoAcoplada", ResultadoComercio.NaveNoAcoplada.toString())
        assertEquals("BodegaVacia", ResultadoComercio.BodegaVacia.toString())
    }

    private fun crearCalculadoraMock(): CalculadoraPrecios = object : CalculadoraPrecios {
        override fun calcularPrecio(item: Vendible): Int = 999
        override fun calcularTotal(items: Collection<Vendible>): Int = items.size * 999
    }

    private fun crearNaveAcopladaMock(): Acoplable = object : Acoplable {
        override val estaAcoplado: Boolean = true
        override fun acoplar() {}
        override fun desacoplar() {}
    }

    private fun crearReceptorFondosMock(): ReceptorFondos = object : ReceptorFondos {
        override var saldo: Int = 0
        override fun acreditar(monto: Int) {
            saldo += monto
        }
    }

    private fun interfacesComerciales(): List<Class<*>> = listOf(
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
}
