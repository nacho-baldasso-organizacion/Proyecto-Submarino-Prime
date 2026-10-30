package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Pruebas de arquitectura SOLID (ISP, DIP, LSP, SRP) y fachada del catálogo de upgrades.
 */
class Issue8ArquitecturaTest {

    @Test
    fun `test todas las interfaces de upgrades cumplen con Interface Segregation (ISP) de maximo 2 metodos`() {
        val interfacesUpgrades = listOf(
            Identificable::class.java,
            Descriptible::class.java,
            Comprable::class.java,
            Mejorable::class.java,
            LimiteNivel::class.java,
            AplicableSubmarino::class.java,
            EfectoMejora::class.java,
            EstrategiaCosto::class.java,
            ConsultableCatalogo::class.java,
            GestionableCatalogo::class.java,
            ProcesadorCompras::class.java,
            ObservadorTienda::class.java,
            GestorObservadoresTienda::class.java,
            TiendaMejorasContrato::class.java
        )

        for (iface in interfacesUpgrades) {
            val metodosDeclarados = iface.declaredMethods.size
            assertTrue(
                metodosDeclarados <= 2,
                "La interfaz ${iface.simpleName} declara $metodosDeclarados métodos, violando la regla estricta de <= 2 métodos (ISP)."
            )
        }
    }

    @Test
    fun `test inyeccion de dependencias DIP en ServicioUpgrades mediante mocks abstractos`() {
        val mockCatalogo = object : ConsultableCatalogo {
            val mejoraMock = FabricaMejoras.crearMejoraBodega(costoBase = 50)
            override fun buscarPorId(id: String): Mejora? = if (id == mejoraMock.id) mejoraMock else null
            override fun obtenerTodas(): List<Mejora> = listOf(mejoraMock)
        }

        val mockPagable = object : Pagable {
            override var saldo: Int = 100
            override fun debitar(monto: Int): Boolean {
                if (monto <= saldo) {
                    saldo -= monto
                    return true
                }
                return false
            }
        }

        val sub = Submarino(casco = ModuloCasco(500_000.0), notificador = NotificadorSilencioso)
        val servicio = ServicioUpgrades(catalogo = mockCatalogo)

        val resultado = servicio.comprarMejora(mockCatalogo.mejoraMock.id, mockPagable, sub)
        assertTrue(resultado is ResultadoCompra.Exito)
        assertEquals(50, mockPagable.saldo)
    }

    @Test
    fun `test fachada TiendaMejoras delega y lista correctamente`() {
        val tienda = FabricaMejoras.crearTiendaEstandar()
        val sub = Submarino(casco = ModuloCasco(500_000.0), billetera = Monedero(500), notificador = NotificadorSilencioso)

        val lista = tienda.listarMejoras()
        assertEquals(4, lista.size)
        assertEquals(4, tienda.cantidadMejoras)
        assertNotNull(tienda.obtenerMejora(FabricaMejoras.ID_BODEGA))

        val resultado = tienda.comprarParaSubmarino(FabricaMejoras.ID_BODEGA, sub)
        assertTrue(resultado is ResultadoCompra.Exito)
        assertEquals(400, sub.billetera.saldo)
    }

    @Test
    fun `test catalogo upgrades gestiona altas y bajas`() {
        val catalogo = CatalogoUpgrades()
        assertEquals(0, catalogo.cantidad)

        val m1 = FabricaMejoras.crearMejoraBodega()
        val m2 = FabricaMejoras.crearMejoraVelocidadAscenso()

        assertTrue(catalogo.registrar(m1))
        assertFalse(catalogo.registrar(m1), "No debe registrar duplicados")
        assertTrue(catalogo.registrar(m2))
        assertEquals(2, catalogo.cantidad)
        assertTrue(catalogo.contiene(m1.id))

        assertTrue(catalogo.eliminar(m1.id))
        assertFalse(catalogo.contiene(m1.id))
        assertEquals(1, catalogo.cantidad)
        assertFalse(catalogo.eliminar(m1.id), "No puede eliminar lo que no existe")
    }
}
