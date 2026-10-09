package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class Issue5Test {

    @Test
    fun `test catalogo de minerales cumple con tabla de especificacion de Issue 5`() {
        // Arena de Cuarzo: Superficie (0-50m), Peso: 1.0 kg, Valor: $10
        assertEquals("Arena de Cuarzo", TipoMineral.ARENA_DE_CUARZO.nombreMineral)
        assertEquals("Superficie (0-50m)", TipoMineral.ARENA_DE_CUARZO.zona)
        assertEquals(1.0, TipoMineral.ARENA_DE_CUARZO.peso)
        assertEquals(10, TipoMineral.ARENA_DE_CUARZO.valor)

        // Cobre Marino: Zona Fótica (50-150m), Peso: 2.5 kg, Valor: $35
        assertEquals("Cobre Marino", TipoMineral.COBRE_MARINO.nombreMineral)
        assertEquals("Zona Fótica (50-150m)", TipoMineral.COBRE_MARINO.zona)
        assertEquals(2.5, TipoMineral.COBRE_MARINO.peso)
        assertEquals(35, TipoMineral.COBRE_MARINO.valor)

        // Nódulo de Manganeso: Zona Mesopelágica (150-400m), Peso: 4.0 kg, Valor: $90
        assertEquals("Nódulo de Manganeso", TipoMineral.NODULO_DE_MANGANESO.nombreMineral)
        assertEquals("Zona Mesopelágica (150-400m)", TipoMineral.NODULO_DE_MANGANESO.zona)
        assertEquals(4.0, TipoMineral.NODULO_DE_MANGANESO.peso)
        assertEquals(90, TipoMineral.NODULO_DE_MANGANESO.valor)

        // Titanio Cristalino: Fosa Abisal (400-800m), Peso: 5.0 kg, Valor: $220
        assertEquals("Titanio Cristalino", TipoMineral.TITANIO_CRISTALINO.nombreMineral)
        assertEquals("Fosa Abisal (400-800m)", TipoMineral.TITANIO_CRISTALINO.zona)
        assertEquals(5.0, TipoMineral.TITANIO_CRISTALINO.peso)
        assertEquals(220, TipoMineral.TITANIO_CRISTALINO.valor)

        // Gema de Salmuera: Pozos de Alta Densidad (>800m), Peso: 1.5 kg, Valor: $500
        assertEquals("Gema de Salmuera", TipoMineral.GEMA_DE_SALMUERA.nombreMineral)
        assertEquals("Pozos de Alta Densidad (>800m)", TipoMineral.GEMA_DE_SALMUERA.zona)
        assertEquals(1.5, TipoMineral.GEMA_DE_SALMUERA.peso)
        assertEquals(500, TipoMineral.GEMA_DE_SALMUERA.valor)

        // Artefacto Hundido: Cavernas Ocultas, Peso: 8.0 kg, Valor: $1200
        assertEquals("Artefacto Hundido", TipoMineral.ARTEFACTO_HUNDIDO.nombreMineral)
        assertEquals("Cavernas Ocultas", TipoMineral.ARTEFACTO_HUNDIDO.zona)
        assertEquals(8.0, TipoMineral.ARTEFACTO_HUNDIDO.peso)
        assertEquals(1200, TipoMineral.ARTEFACTO_HUNDIDO.valor)
    }

    @Test
    fun `test obtener mineral por profundidad batimetrica`() {
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
    fun `test generador paredes coloca minerales unicamente en los limites`() {
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
    fun `test bodega controla limite de peso maximo y agrega items respetando capacidad`() {
        val bodega = Bodega(capacidadMaxima = 6.0)
        assertEquals(0.0, bodega.pesoActual)
        assertEquals(6.0, bodega.capacidadDisponible)
        assertFalse(bodega.estaLlena())

        val mineral1 = Mineral("M1", TipoMineral.ARENA_DE_CUARZO, 0, 0) // Peso 1.0 kg
        val mineral2 = Mineral("M2", TipoMineral.NODULO_DE_MANGANESO, 0, 1) // Peso 4.0 kg
        val mineralPesado = Mineral("M3", TipoMineral.COBRE_MARINO, 0, 2) // Peso 2.5 kg

        // Agregar mineral 1 (1.0 kg <= 6.0 kg)
        assertTrue(bodega.agregar(mineral1))
        assertTrue(mineral1.recolectado)
        assertEquals(1.0, bodega.pesoActual)
        assertEquals(5.0, bodega.capacidadDisponible)

        // Agregar mineral 2 (1.0 + 4.0 = 5.0 kg <= 6.0 kg)
        assertTrue(bodega.agregar(mineral2))
        assertTrue(mineral2.recolectado)
        assertEquals(5.0, bodega.pesoActual)
        assertEquals(1.0, bodega.capacidadDisponible)

        // Intentar agregar mineral pesado (5.0 + 2.5 = 7.5 kg > 6.0 kg) -> debe rechazarse
        assertFalse(bodega.agregar(mineralPesado))
        assertFalse(mineralPesado.recolectado)
        assertEquals(5.0, bodega.pesoActual)
        assertEquals(2, bodega.cantidadItems)
        assertEquals(100, bodega.valorTotal) // 10 + 90
    }

    @Test
    fun `test detector de colisiones detecta coincidencia exacta de coordenadas`() {
        val detector = DetectorColisionesConcreto()
        val mineral = Mineral("M1", TipoMineral.ARENA_DE_CUARZO, posX = 5, posY = 0)
        val lista = listOf(mineral)

        // Coincidencia exacta
        val colision = detector.verificarColision(Posicion(5, 0), lista)
        assertNotNull(colision)
        assertEquals("M1", colision.id)

        // Sin coincidencia
        val sinColision = detector.verificarColision(Posicion(4, 0), lista)
        assertNull(sinColision)

        // Si ya fue recolectado, no colisiona como disponible
        mineral.recolectado = true
        val colisionPostRecoleccion = detector.verificarColision(Posicion(5, 0), lista)
        assertNull(colisionPostRecoleccion)
    }

    @Test
    fun `test servicio recoleccion administra flujo completo y DIP`() {
        val bodega = Bodega(capacidadMaxima = 5.0)
        val detector = DetectorColisionesConcreto()
        val servicio = ServicioRecoleccion(bodega, detector)

        val mineral1 = Mineral("M1", TipoMineral.ARENA_DE_CUARZO, 0, 3) // 1.0 kg, $10
        val mineralGrande = Mineral("M2", TipoMineral.TITANIO_CRISTALINO, 0, 4) // 5.0 kg, $220
        val mapa = listOf(mineral1, mineralGrande)

        // 1. Sin colisión
        val resSinColision = servicio.procesarRecoleccion(Posicion(1, 1), mapa)
        assertTrue(resSinColision is ResultadoRecoleccion.SinColision)

        // 2. Éxito de recolección
        val resExito = servicio.procesarRecoleccion(Posicion(0, 3), mapa)
        assertTrue(resExito is ResultadoRecoleccion.Exito)
        assertEquals(1.0, resExito.pesoAcumulado)
        assertTrue(mineral1.recolectado)

        // 3. Intento en mineral que excede peso (1.0 + 5.0 = 6.0 > 5.0)
        val resBodegaLlena = servicio.procesarRecoleccion(Posicion(0, 4), mapa)
        assertTrue(resBodegaLlena is ResultadoRecoleccion.BodegaLlena)
        assertEquals(1.0, resBodegaLlena.excesoPeso)
        assertFalse(mineralGrande.recolectado)
    }
}
