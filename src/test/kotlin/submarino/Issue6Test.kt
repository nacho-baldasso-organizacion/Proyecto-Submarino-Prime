package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertIs

class Issue6Test {

    private fun crearSubmarinoPrueba(
        bodega: Almacenamiento? = null,
        pesoBase: Double = 1000.0,
        velocidad: Double = 10.0,
        estrategiaAscenso: EstrategiaAscenso = EstrategiaAscensoLineal(0.5),
        posX: Double = 0.0,
        posY: Double = 0.0
    ): Submarino = Submarino(
        posX = posX,
        posY = posY,
        velocidad = velocidad,
        casco = ModuloCasco(500_000.0),
        pesoBase = pesoBase,
        bodega = bodega,
        estrategiaAscenso = estrategiaAscenso,
        notificador = NotificadorSilencioso
    )

    @Test
    fun testCalculoDePesoTotalConBodegaVaciaEsIgualAlPesoBase() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val submarino = crearSubmarinoPrueba(bodega = bodega)

        assertEquals(0.0, bodega.pesoActual, 0.001)
        assertEquals(1000.0, submarino.pesoBase, 0.001)
        assertEquals(1000.0, submarino.pesoTotal, 0.001)
        assertEquals(1000.0, submarino.calcularPesoTotal(), 0.001)
        assertEquals(1000.0, submarino.peso, 0.001)
    }

    @Test
    fun testCalculoDePesoTotalAMediaCargaSumaPesoBaseYCargaActual() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val submarino = crearSubmarinoPrueba(bodega = bodega)

        val mineral1 = Mineral("MIN-1", TipoMineral.TITANIO_CRISTALINO, 0, 1)
        val mineral2 = Mineral("MIN-2", TipoMineral.TITANIO_CRISTALINO, 0, 2)
        assertTrue(bodega.agregar(mineral1))
        assertTrue(bodega.agregar(mineral2))

        assertEquals(10.0, bodega.pesoActual, 0.001)
        assertEquals(1010.0, submarino.pesoTotal, 0.001)
        assertEquals(1010.0, submarino.calcularPesoTotal(), 0.001)
        assertEquals(1010.0, submarino.peso, 0.001)
    }

    @Test
    fun testCalculoDePesoTotalConBodegaLlenaAlcanzaPesoBaseMasCapacidadMaxima() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val submarino = crearSubmarinoPrueba(bodega = bodega)
        )

        for (i in 1..4) {
            val min = Mineral("MIN-$i", TipoMineral.TITANIO_CRISTALINO, 0, i)
            assertTrue(bodega.agregar(min))
        }

        assertTrue(bodega.estaLlena())
        assertEquals(20.0, bodega.pesoActual, 0.001)
        assertEquals(1020.0, submarino.pesoTotal, 0.001)
        assertEquals(1020.0, submarino.calcularPesoTotal(), 0.001)
        assertEquals(1020.0, submarino.peso, 0.001)
    }

    @Test
    fun testVariacionDeVelocidadDeAscensoConBodegaVaciaMediaCargaYLlena() {
        val capacidad = 20.0
        val bodega = Bodega(capacidadMaxima = capacidad)
        val velocidadBase = 10.0
        val estrategia = EstrategiaAscensoLineal(factorReduccionMaximo = 0.5)
        val submarino = crearSubmarinoPrueba(bodega = bodega, velocidad = velocidadBase, estrategiaAscenso = estrategia)

        val velVacia = submarino.velocidadAscenso
        assertEquals(1.0, submarino.factorAscenso, 0.001)
        assertEquals(10.0, velVacia, 0.001)
        assertEquals(10.0, submarino.calcularVelocidadAscenso(), 0.001)

        val min1 = Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 1)
        val min2 = Mineral("M2", TipoMineral.TITANIO_CRISTALINO, 0, 2)
        bodega.agregar(min1)
        bodega.agregar(min2)

        val velMedia = submarino.velocidadAscenso
        assertEquals(0.75, submarino.factorAscenso, 0.001)
        assertEquals(7.5, velMedia, 0.001)

        val min3 = Mineral("M3", TipoMineral.TITANIO_CRISTALINO, 0, 3)
        val min4 = Mineral("M4", TipoMineral.TITANIO_CRISTALINO, 0, 4)
        bodega.agregar(min3)
        bodega.agregar(min4)

        val velLlena = submarino.velocidadAscenso
        assertEquals(0.50, submarino.factorAscenso, 0.001)
        assertEquals(5.0, velLlena, 0.001)

        assertTrue(velVacia > velMedia)
        assertTrue(velMedia > velLlena)
        assertTrue(velLlena > 0.0)
    }

    @Test
    fun testManiobraDeAscensoRalentizaElDesplazamientoEfectivoSegunMasa() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val sub = crearSubmarinoPrueba(bodega = bodega, posY = 100.0)

        assertTrue(sub.ascender(20.0))
        assertEquals(80.0, sub.posY, 0.001)

        bodega.agregar(Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 0))
        bodega.agregar(Mineral("M2", TipoMineral.TITANIO_CRISTALINO, 0, 1))

        assertTrue(sub.ascender(20.0))
        assertEquals(65.0, sub.posY, 0.001)

        bodega.agregar(Mineral("M3", TipoMineral.TITANIO_CRISTALINO, 0, 2))
        bodega.agregar(Mineral("M4", TipoMineral.TITANIO_CRISTALINO, 0, 3))

        assertTrue(sub.ascender(20.0))
        assertEquals(55.0, sub.posY, 0.001)
    }

    @Test
    fun testManiobraDeAscensoSemanticaSinArgumentosUsaVelocidadDinamica() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val sub = crearSubmarinoPrueba(bodega = bodega, posY = 50.0)

        assertTrue(sub.ascender())
        assertEquals(40.0, sub.posY, 0.001)

        for (i in 1..4) {
            bodega.agregar(Mineral("M$i", TipoMineral.TITANIO_CRISTALINO, 0, i))
        }
        assertTrue(sub.ascender())
        assertEquals(35.0, sub.posY, 0.001)
    }

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

    @Test
    fun testPolimorfismoOCPConEstrategiaAscensoPorMasa() {
        val bodega = Bodega(capacidadMaxima = 1000.0)
        val estrategiaMasa = EstrategiaAscensoPorMasa()

        val sub = Submarino(
            casco = ModuloCasco(500_000.0),
            pesoBase = 1000.0,
            bodega = bodega,
            velocidad = 20.0,
            estrategiaAscenso = estrategiaMasa,
            notificador = NotificadorSilencioso
        )

        assertEquals(1.0, sub.factorAscenso, 0.001)
        assertEquals(20.0, sub.velocidadAscenso, 0.001)

        val mineralPesado = object : Recolectable {
            override val id = "PESADO-1"
            override val nombre = "Restos de Naufragio"
            override val peso = 1000.0
            override val valor = 5000
            override var recolectado = false
            override val posX = 0
            override val posY = 0
        }
        assertTrue(bodega.agregar(mineralPesado))

        assertEquals(2000.0, sub.pesoTotal, 0.001)
        assertEquals(0.50, sub.factorAscenso, 0.001)
        assertEquals(10.0, sub.velocidadAscenso, 0.001)
    }

    @Test
    fun testPolimorfismoOCPConEstrategiaFisicaFlotabilidadArquimedes() {
        val bodega = Bodega(capacidadMaxima = 50.0)
        val fisicaFlotabilidad = EstrategiaFisicaFlotabilidad(
            calculadorPresion = CalculadoraHidrostatica,
            factorEmpujeMinimo = 0.3
        )

        val sub = Submarino(
            casco = ModuloCasco(500_000.0),
            pesoBase = 1000.0,
            bodega = bodega,
            velocidad = 10.0,
            estrategiaAscenso = fisicaFlotabilidad,
            notificador = NotificadorSilencioso
        )

        assertEquals(1.0, sub.factorAscenso, 0.001)
        assertEquals(10.0, sub.velocidadAscenso, 0.001)

        val minMedia = Mineral("M1", TipoMineral.ARTEFACTO_HUNDIDO, 0, 0)
        val minMedia2 = Mineral("M2", TipoMineral.ARTEFACTO_HUNDIDO, 0, 1)
        val minMedia3 = Mineral("M3", TipoMineral.ARTEFACTO_HUNDIDO, 0, 2)
        val minMedia4 = Mineral("M4", TipoMineral.ARENA_DE_CUARZO, 0, 3)
        bodega.agregar(minMedia)
        bodega.agregar(minMedia2)
        bodega.agregar(minMedia3)
        bodega.agregar(minMedia4)

        assertEquals(25.0, bodega.pesoActual, 0.001)
        assertEquals(0.65, sub.factorAscenso, 0.001)
        assertEquals(6.5, sub.velocidadAscenso, 0.001)

        val itemRelleno = object : Recolectable {
            override val id = "RELLENO"
            override val nombre = "Lastre"
            override val peso = 25.0
            override val valor = 10
            override var recolectado = false
            override val posX = 0
            override val posY = 0
        }
        bodega.agregar(itemRelleno)

        assertEquals(50.0, bodega.pesoActual, 0.001)
        assertEquals(0.3, sub.factorAscenso, 0.001)
        assertEquals(3.0, sub.velocidadAscenso, 0.001)
    }

    @Test
    fun testSegregacionDeInterfacesISPConPesableYCargable() {
        val mineral: Cargable = Mineral("M1", TipoMineral.NODULO_DE_MANGANESO, 0, 0)
        val submarino: Pesable = Submarino(
            casco = ModuloCasco(500_000.0),
            pesoBase = 800.0,
            bodega = null,
            notificador = NotificadorSilencioso
        )

        val objetosPesables: List<Pesable> = listOf(mineral, submarino)
        val pesoTotalCombinado = objetosPesables.sumOf { it.peso }

        assertEquals(4.0, mineral.peso, 0.001)
        assertEquals(800.0, submarino.peso, 0.001)
        assertEquals(804.0, pesoTotalCombinado, 0.001)
    }

    @Test
    fun testSubmarinoSinBodegaMantienePesoBaseYVelocidadNominal() {
        val subSinBodega = Submarino(
            casco = ModuloCasco(500_000.0),
            pesoBase = 1200.0,
            bodega = null,
            velocidad = 8.0,
            notificador = NotificadorSilencioso
        )

        assertEquals(1200.0, subSinBodega.pesoTotal, 0.001)
        assertEquals(8.0, subSinBodega.velocidadAscenso, 0.001)
        assertEquals(1.0, subSinBodega.factorAscenso, 0.001)

        val estado = subSinBodega.obtenerEstadoAscenso()
        assertEquals(1200.0, estado.pesoBase, 0.001)
        assertEquals(0.0, estado.pesoCarga, 0.001)
        assertEquals(1200.0, estado.pesoTotal, 0.001)
        assertEquals(0.0, estado.capacidadMaximaBodega, 0.001)
        assertEquals(1.0, estado.factorVelocidad, 0.001)
        assertEquals(8.0, estado.velocidadNominal, 0.001)
        assertEquals(8.0, estado.velocidadAscensoEfectiva, 0.001)
    }

    @Test
    fun testValidacionDeInvariantesDePesoBaseYVelocidad() {
        assertFailsWith<IllegalArgumentException> {
            Submarino(
                casco = ModuloCasco(500_000.0),
                pesoBase = -10.0,
                notificador = NotificadorSilencioso
            )
        }

        assertFailsWith<IllegalArgumentException> {
            Submarino(
                casco = ModuloCasco(500_000.0),
                velocidadAscensoBase = -5.0,
                notificador = NotificadorSilencioso
            )
        }

        assertFailsWith<IllegalArgumentException> {
            EstrategiaAscensoLineal(factorReduccionMaximo = 1.5)
        }

        assertFailsWith<IllegalArgumentException> {
            EstrategiaAscensoLineal(factorReduccionMaximo = -0.1)
        }
    }

    @Test
    fun testIntegracionCompletaRecoleccionYDecrementoReactivo() {
        val bodega = Bodega(capacidadMaxima = 10.0)
        val detector = DetectorColisionesConcreto()
        val servicio = ServicioRecoleccion(bodega, detector)

        val sub = crearSubmarinoPrueba(bodega = bodega, posY = 100.0)

        assertEquals(10.0, sub.velocidadAscenso, 0.001)

        val mineral1 = Mineral("M1", TipoMineral.NODULO_DE_MANGANESO, posX = 0, posY = 100)
        val res1 = servicio.procesarRecoleccion(Posicion(0, 100), listOf(mineral1))
        assertIs<ResultadoRecoleccion.Exito>(res1)

        assertEquals(1004.0, sub.pesoTotal, 0.001)
        assertEquals(8.0, sub.velocidadAscenso, 0.001)

        sub.ascender(10.0)
        val mineral2 = Mineral("M2", TipoMineral.TITANIO_CRISTALINO, posX = 0, posY = sub.posY.toInt())
        val res2 = servicio.procesarRecoleccion(Posicion(0, sub.posY.toInt()), listOf(mineral2))
        assertIs<ResultadoRecoleccion.Exito>(res2)

        assertEquals(1009.0, sub.pesoTotal, 0.001)
        assertEquals(5.5, sub.velocidadAscenso, 0.001)
    }
}
