package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.assertIs

class Issue5Test {

    @Test
    fun testCatalogoDeMineralesCumpleConTablaDeEspecificacion() {
        assertEquals("Arena de Cuarzo", TipoMineral.ARENA_DE_CUARZO.nombreMineral)
        assertEquals("Superficie (0-50m)", TipoMineral.ARENA_DE_CUARZO.zona)
        assertEquals(1.0, TipoMineral.ARENA_DE_CUARZO.peso)
        assertEquals(10, TipoMineral.ARENA_DE_CUARZO.valor)

        assertEquals("Cobre Marino", TipoMineral.COBRE_MARINO.nombreMineral)
        assertEquals("Zona Fótica (50-150m)", TipoMineral.COBRE_MARINO.zona)
        assertEquals(2.5, TipoMineral.COBRE_MARINO.peso)
        assertEquals(35, TipoMineral.COBRE_MARINO.valor)

        assertEquals("Nódulo de Manganeso", TipoMineral.NODULO_DE_MANGANESO.nombreMineral)
        assertEquals("Zona Mesopelágica (150-400m)", TipoMineral.NODULO_DE_MANGANESO.zona)
        assertEquals(4.0, TipoMineral.NODULO_DE_MANGANESO.peso)
        assertEquals(90, TipoMineral.NODULO_DE_MANGANESO.valor)

        assertEquals("Titanio Cristalino", TipoMineral.TITANIO_CRISTALINO.nombreMineral)
        assertEquals("Fosa Abisal (400-800m)", TipoMineral.TITANIO_CRISTALINO.zona)
        assertEquals(5.0, TipoMineral.TITANIO_CRISTALINO.peso)
        assertEquals(220, TipoMineral.TITANIO_CRISTALINO.valor)

        assertEquals("Gema de Salmuera", TipoMineral.GEMA_DE_SALMUERA.nombreMineral)
        assertEquals("Pozos de Alta Densidad (>800m)", TipoMineral.GEMA_DE_SALMUERA.zona)
        assertEquals(1.5, TipoMineral.GEMA_DE_SALMUERA.peso)
        assertEquals(500, TipoMineral.GEMA_DE_SALMUERA.valor)

        assertEquals("Artefacto Hundido", TipoMineral.ARTEFACTO_HUNDIDO.nombreMineral)
        assertEquals("Cavernas Ocultas", TipoMineral.ARTEFACTO_HUNDIDO.zona)
        assertEquals(8.0, TipoMineral.ARTEFACTO_HUNDIDO.peso)
        assertEquals(1200, TipoMineral.ARTEFACTO_HUNDIDO.valor)
    }

    @Test
    fun testObtenerMineralPorProfundidadBatimetrica() {
        assertEquals(TipoMineral.ARENA_DE_CUARZO, TipoMineral.obtenerPorProfundidad(0.0))
        assertEquals(TipoMineral.ARENA_DE_CUARZO, TipoMineral.obtenerPorProfundidad(49.9))
        assertEquals(TipoMineral.COBRE_MARINO, TipoMineral.obtenerPorProfundidad(50.0))
        assertEquals(TipoMineral.COBRE_MARINO, TipoMineral.obtenerPorProfundidad(149.9))
        assertEquals(TipoMineral.NODULO_DE_MANGANESO, TipoMineral.obtenerPorProfundidad(150.0))
        assertEquals(TipoMineral.NODULO_DE_MANGANESO, TipoMineral.obtenerPorProfundidad(399.9))
        assertEquals(TipoMineral.TITANIO_CRISTALINO, TipoMineral.obtenerPorProfundidad(400.0))
        assertEquals(TipoMineral.TITANIO_CRISTALINO, TipoMineral.obtenerPorProfundidad(800.0))
        assertEquals(TipoMineral.GEMA_DE_SALMUERA, TipoMineral.obtenerPorProfundidad(800.1))
        assertEquals(TipoMineral.GEMA_DE_SALMUERA, TipoMineral.obtenerPorProfundidad(1500.0))
        assertEquals(TipoMineral.ARTEFACTO_HUNDIDO, TipoMineral.obtenerPorProfundidad(200.0, esCavernaOculta = true))
    }

    @Test
    fun testGeneradorParedesColocaMineralesUnicamenteEnLosLimites() {
        val generador = GeneradorParedes()
        val ancho = 15
        val alto = 12
        val cantidad = 20

        val minerales = generador.generarEnParedes(ancho, alto, profundidadActual = 100.0, cantidad = cantidad)

        assertEquals(cantidad, minerales.size)
        minerales.forEach { mineral ->
            val estaEnPared = (mineral.posX == 0 || mineral.posX == ancho - 1 ||
                    mineral.posY == 0 || mineral.posY == alto - 1)
            assertTrue(estaEnPared, "El mineral en (${mineral.posX}, ${mineral.posY}) no está en la pared")
            assertEquals("Cobre Marino", mineral.nombre)
            assertFalse(mineral.recolectado)
        }
    }

    @Test
    fun testBodegaControlaLimiteDePesoMaximoYAgregaItems() {
        val bodega = Bodega(capacidadMaxima = 6.0)
        assertEquals(0.0, bodega.pesoActual)
        assertEquals(6.0, bodega.capacidadDisponible)
        assertFalse(bodega.estaLlena())

        val mineral1 = Mineral("M1", TipoMineral.ARENA_DE_CUARZO, 0, 0)
        val mineral2 = Mineral("M2", TipoMineral.NODULO_DE_MANGANESO, 0, 1)
        val mineralPesado = Mineral("M3", TipoMineral.COBRE_MARINO, 0, 2)

        assertTrue(bodega.agregar(mineral1))
        assertTrue(mineral1.recolectado)
        assertEquals(1.0, bodega.pesoActual)
        assertEquals(5.0, bodega.capacidadDisponible)

        assertTrue(bodega.agregar(mineral2))
        assertTrue(mineral2.recolectado)
        assertEquals(5.0, bodega.pesoActual)
        assertEquals(1.0, bodega.capacidadDisponible)

        assertFalse(bodega.agregar(mineralPesado))
        assertFalse(mineralPesado.recolectado)
        assertEquals(5.0, bodega.pesoActual)
        assertEquals(2, bodega.cantidadItems)
        assertEquals(100, bodega.valorTotal)
    }

    @Test
    fun testDetectorDeColisionesDetectaCoincidenciaExacta() {
        val detector = DetectorColisionesConcreto()
        val mineral = Mineral("M1", TipoMineral.ARENA_DE_CUARZO, posX = 5, posY = 0)
        val lista = listOf(mineral)

        val colision = detector.verificarColision(Posicion(5, 0), lista)
        assertNotNull(colision)
        assertEquals("M1", colision.id)

        val sinColision = detector.verificarColision(Posicion(4, 0), lista)
        assertNull(sinColision)

        mineral.recolectado = true
        val colisionPostRecoleccion = detector.verificarColision(Posicion(5, 0), lista)
        assertNull(colisionPostRecoleccion)
    }

    @Test
    fun testServicioRecoleccionAdministraFlujoCompleto() {
        val bodega = Bodega(capacidadMaxima = 5.0)
        val detector = DetectorColisionesConcreto()
        val servicio = ServicioRecoleccion(bodega, detector)

        val mineral1 = Mineral("M1", TipoMineral.ARENA_DE_CUARZO, 0, 3)
        val mineralGrande = Mineral("M2", TipoMineral.TITANIO_CRISTALINO, 0, 4)
        val mapa = listOf(mineral1, mineralGrande)

        val resSinColision = servicio.procesarRecoleccion(Posicion(1, 1), mapa)
        assertIs<ResultadoRecoleccion.SinColision>(resSinColision)

        val resExito = servicio.procesarRecoleccion(Posicion(0, 3), mapa)
        assertIs<ResultadoRecoleccion.Exito>(resExito)
        assertEquals(1.0, resExito.pesoAcumulado)
        assertTrue(mineral1.recolectado)

        val resBodegaLlena = servicio.procesarRecoleccion(Posicion(0, 4), mapa)
        assertIs<ResultadoRecoleccion.BodegaLlena>(resBodegaLlena)
        assertEquals(1.0, resBodegaLlena.excesoPeso)
        assertFalse(mineralGrande.recolectado)
    }
}
