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
        verificarMineral(TipoMineral.ARENA_DE_CUARZO, "Arena de Cuarzo", "Superficie (0-50m)", 1.0, 10)
        verificarMineral(TipoMineral.COBRE_MARINO, "Cobre Marino", "Zona Fótica (50-150m)", 2.5, 35)
        verificarMineral(TipoMineral.NODULO_DE_MANGANESO, "Nódulo de Manganeso", "Zona Mesopelágica (150-400m)", 4.0, 90)
        verificarMineral(TipoMineral.TITANIO_CRISTALINO, "Titanio Cristalino", "Fosa Abisal (400-800m)", 5.0, 220)
        verificarMineral(TipoMineral.GEMA_DE_SALMUERA, "Gema de Salmuera", "Pozos de Alta Densidad (>800m)", 1.5, 500)
        verificarMineral(TipoMineral.ARTEFACTO_HUNDIDO, "Artefacto Hundido", "Cavernas Ocultas", 8.0, 1200)
    }

    @Test
    fun testObtenerMineralPorProfundidadBatimetrica() {
        verificarProfundidad(0.0, TipoMineral.ARENA_DE_CUARZO)
        verificarProfundidad(49.9, TipoMineral.ARENA_DE_CUARZO)
        verificarProfundidad(50.0, TipoMineral.COBRE_MARINO)
        verificarProfundidad(149.9, TipoMineral.COBRE_MARINO)
        verificarProfundidad(150.0, TipoMineral.NODULO_DE_MANGANESO)
        verificarProfundidad(399.9, TipoMineral.NODULO_DE_MANGANESO)
        verificarProfundidad(400.0, TipoMineral.TITANIO_CRISTALINO)
        verificarProfundidad(800.0, TipoMineral.TITANIO_CRISTALINO)
        verificarProfundidad(800.1, TipoMineral.GEMA_DE_SALMUERA)
        verificarProfundidad(1500.0, TipoMineral.GEMA_DE_SALMUERA)
        verificarProfundidad(200.0, TipoMineral.ARTEFACTO_HUNDIDO, esCavernaOculta = true)
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
            verificarMineralEnPared(mineral, ancho, alto)
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
        val servicio = ServicioRecoleccion(bodega, DetectorColisionesConcreto())

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

    private fun verificarMineral(
        tipo: TipoMineral,
        nombre: String,
        zona: String,
        peso: Double,
        valor: Int
    ) {
        assertEquals(nombre, tipo.nombreMineral)
        assertEquals(zona, tipo.zona)
        assertEquals(peso, tipo.peso)
        assertEquals(valor, tipo.valor)
    }

    private fun verificarProfundidad(
        profundidad: Double,
        esperado: TipoMineral,
        esCavernaOculta: Boolean = false
    ) {
        assertEquals(esperado, TipoMineral.obtenerPorProfundidad(profundidad, esCavernaOculta))
    }

    private fun verificarMineralEnPared(mineral: Recolectable, ancho: Int, alto: Int) {
        val estaEnPared = mineral.posX == 0 || mineral.posX == ancho - 1 ||
                mineral.posY == 0 || mineral.posY == alto - 1
        assertTrue(
            estaEnPared,
            "El mineral en (${mineral.posX}, ${mineral.posY}) no está en la pared"
        )
    }
}
