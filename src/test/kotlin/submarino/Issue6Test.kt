package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Issue6Test {

    // =========================================================================
    // 1. PRUEBAS DEL CÁLCULO DE PESO TOTAL (BODEGA VACÍA, MEDIA CARGA Y LLENA)
    // =========================================================================

    @Test
    fun `test calculo de peso total con bodega vacia es igual al peso base`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val submarino = Submarino(
            casco = ModuloCasco(500_000.0),
            pesoBase = 1000.0,
            bodega = bodega,
            velocidad = 10.0,
            notificador = NotificadorSilencioso
        )

        assertEquals(0.0, bodega.pesoActual, 0.001)
        assertEquals(1000.0, submarino.pesoBase, 0.001)
        assertEquals(1000.0, submarino.pesoTotal, 0.001)
        assertEquals(1000.0, submarino.calcularPesoTotal(), 0.001)
        assertEquals(1000.0, submarino.peso, 0.001) // Contrato Pesable
    }

    @Test
    fun `test calculo de peso total a media carga suma peso base y carga actual`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val submarino = Submarino(
            casco = ModuloCasco(500_000.0),
            pesoBase = 1000.0,
            bodega = bodega,
            velocidad = 10.0,
            notificador = NotificadorSilencioso
        )

        // Cargar minerales hasta 10.0 kg (50% de la capacidad de 20.0 kg)
        // Titanio Cristalino = 5.0 kg cada uno -> 2 minerales = 10.0 kg
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
    fun `test calculo de peso total con bodega llena alcanza peso base mas capacidad maxima`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val submarino = Submarino(
            casco = ModuloCasco(500_000.0),
            pesoBase = 1000.0,
            bodega = bodega,
            velocidad = 10.0,
            notificador = NotificadorSilencioso
        )

        // Cargar 4 minerales de Titanio Cristalino (5.0 kg * 4 = 20.0 kg) -> 100% capacidad
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

    // =========================================================================
    // 2. PRUEBAS DE VARIACIÓN DE VELOCIDAD DE ASCENSO SEGÚN CARGA
    // =========================================================================

    @Test
    fun `test variacion de velocidad de ascenso con bodega vacia, media carga y llena`() {
        val capacidad = 20.0
        val bodega = Bodega(capacidadMaxima = capacidad)
        val velocidadBase = 10.0
        val estrategia = EstrategiaAscensoLineal(factorReduccionMaximo = 0.5)

        val submarino = Submarino(
            casco = ModuloCasco(500_000.0),
            pesoBase = 1000.0,
            bodega = bodega,
            velocidad = velocidadBase,
            estrategiaAscenso = estrategia,
            notificador = NotificadorSilencioso
        )

        // 1. Bodega vacía (0% carga)
        // Factor = 1.0 -> Velocidad = 10.0 m/s
        val velVacia = submarino.velocidadAscenso
        assertEquals(1.0, submarino.factorAscenso, 0.001)
        assertEquals(10.0, velVacia, 0.001)
        assertEquals(10.0, submarino.calcularVelocidadAscenso(), 0.001)

        // 2. Media carga (10.0 kg / 20.0 kg = 50% carga)
        // Factor = 1.0 - (0.5 * 0.5) = 0.75 -> Velocidad = 7.5 m/s
        val min1 = Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 1) // 5.0 kg
        val min2 = Mineral("M2", TipoMineral.TITANIO_CRISTALINO, 0, 2) // 5.0 kg
        bodega.agregar(min1)
        bodega.agregar(min2)

        val velMedia = submarino.velocidadAscenso
        assertEquals(0.75, submarino.factorAscenso, 0.001)
        assertEquals(7.5, velMedia, 0.001)

        // 3. Bodega llena (20.0 kg / 20.0 kg = 100% carga)
        // Factor = 1.0 - (0.5 * 1.0) = 0.50 -> Velocidad = 5.0 m/s
        val min3 = Mineral("M3", TipoMineral.TITANIO_CRISTALINO, 0, 3) // 5.0 kg
        val min4 = Mineral("M4", TipoMineral.TITANIO_CRISTALINO, 0, 4) // 5.0 kg
        bodega.agregar(min3)
        bodega.agregar(min4)

        val velLlena = submarino.velocidadAscenso
        assertEquals(0.50, submarino.factorAscenso, 0.001)
        assertEquals(5.0, velLlena, 0.001)

        // Verificación estricta de orden: vacía > media carga > llena > 0
        assertTrue(velVacia > velMedia, "La velocidad con bodega vacía ($velVacia) debe ser mayor que a media carga ($velMedia)")
        assertTrue(velMedia > velLlena, "La velocidad a media carga ($velMedia) debe ser mayor que con bodega llena ($velLlena)")
        assertTrue(velLlena > 0.0, "La velocidad con bodega llena debe ser estrictamente positiva")
    }

    // =========================================================================
    // 3. PRUEBAS DE MANIOBRA FÍSICA DE ASCENSO Y RALENTIZACIÓN DEL DESPLAZAMIENTO
    // =========================================================================

    @Test
    fun `test maniobra de ascenso ralentiza el desplazamiento efectivo segun masa`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val sub = Submarino(
            posX = 0.0,
            posY = 100.0, // Profundidad de 100 metros
            velocidad = 10.0,
            casco = ModuloCasco(500_000.0),
            pesoBase = 1000.0,
            bodega = bodega,
            estrategiaAscenso = EstrategiaAscensoLineal(factorReduccionMaximo = 0.5),
            notificador = NotificadorSilencioso
        )

        // Con bodega vacía (factor 1.0): ascender 20 metros debe desplazar exactamente 20 metros hacia arriba
        assertTrue(sub.ascender(20.0))
        assertEquals(80.0, sub.posY, 0.001) // 100 - 20 = 80

        // Cargar a media capacidad (10 kg -> factor 0.75)
        bodega.agregar(Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 0)) // 5.0 kg
        bodega.agregar(Mineral("M2", TipoMineral.TITANIO_CRISTALINO, 0, 1)) // 5.0 kg

        // Ascender otros 20 metros nominales -> desplazamiento efectivo = 20 * 0.75 = 15 metros
        assertTrue(sub.ascender(20.0))
        assertEquals(65.0, sub.posY, 0.001) // 80 - 15 = 65

        // Llenar la bodega al 100% (20 kg -> factor 0.50)
        bodega.agregar(Mineral("M3", TipoMineral.TITANIO_CRISTALINO, 0, 2)) // 5.0 kg
        bodega.agregar(Mineral("M4", TipoMineral.TITANIO_CRISTALINO, 0, 3)) // 5.0 kg

        // Ascender otros 20 metros nominales -> desplazamiento efectivo = 20 * 0.50 = 10 metros
        assertTrue(sub.ascender(20.0))
        assertEquals(55.0, sub.posY, 0.001) // 65 - 10 = 55
    }

    @Test
    fun `test maniobra de ascenso semantica sin argumentos usa velocidad dinamica`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val sub = Submarino(
            posX = 0.0,
            posY = 50.0,
            velocidad = 10.0,
            casco = ModuloCasco(500_000.0),
            pesoBase = 1000.0,
            bodega = bodega,
            estrategiaAscenso = EstrategiaAscensoLineal(factorReduccionMaximo = 0.5),
            notificador = NotificadorSilencioso
        )

        // Bodega vacía -> velocidadAscenso = 10.0 m/s -> ascender() sube 10.0 m
        assertTrue(sub.ascender())
        assertEquals(40.0, sub.posY, 0.001)

        // Llenar bodega al 100% -> velocidadAscenso = 5.0 m/s -> ascender() sube 5.0 m
        for (i in 1..4) {
            bodega.agregar(Mineral("M$i", TipoMineral.TITANIO_CRISTALINO, 0, i))
        }
        assertTrue(sub.ascender())
        assertEquals(35.0, sub.posY, 0.001)
    }

    // =========================================================================
    // 4. PRUEBAS DE ARQUITECTURA SOLID (SRP, OCP, DIP, ISP)
    // =========================================================================

    @Test
    fun `test calculador de peso desacoplado con DIP y SRP`() {
        val mockAlmacenamiento = object : Almacenamiento {
            override val pesoActual: Double = 35.0
            override val capacidadMaxima: Double = 100.0
            override fun agregar(item: Recolectable): Boolean = true
            override fun obtenerElementos(): List<Recolectable> = emptyList()
        }

        val calculador = CalculadorPesoSubmarino()
        val pesoTotal = calculador.calcularPesoTotal(pesoBase = 500.0, almacenamiento = mockAlmacenamiento)
        assertEquals(535.0, pesoTotal, 0.001)

        // Probar inyección de un CalculadorPeso personalizado en Submarino (DIP)
        val calculadorCustom = object : CalculadorPeso {
            override fun calcularPesoTotal(pesoBase: Double, almacenamiento: Almacenamiento?): Double {
                return (pesoBase + (almacenamiento?.pesoActual ?: 0.0)) * 1.1 // Agrega 10% de tara
            }
        }

        val subCustom = Submarino(
            casco = ModuloCasco(500_000.0),
            pesoBase = 1000.0,
            bodega = mockAlmacenamiento,
            calculadorPeso = calculadorCustom,
            notificador = NotificadorSilencioso
        )

        // (1000 + 35) * 1.1 = 1138.5
        assertEquals(1138.5, subCustom.pesoTotal, 0.001)
    }

    @Test
    fun `test polimorfismo OCP con EstrategiaAscensoPorMasa`() {
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

        // Vacía: factor = 1000 / 1000 = 1.0 -> velocidad = 20.0
        assertEquals(1.0, sub.factorAscenso, 0.001)
        assertEquals(20.0, sub.velocidadAscenso, 0.001)

        // Agregar 1000 kg de carga (duplica la masa: 1000 + 1000 = 2000 kg)
        // Simulamos un recolectable pesado
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

        // Con doble de masa: factor = 1000 / 2000 = 0.50 -> velocidad = 10.0 m/s
        assertEquals(2000.0, sub.pesoTotal, 0.001)
        assertEquals(0.50, sub.factorAscenso, 0.001)
        assertEquals(10.0, sub.velocidadAscenso, 0.001)
    }

    @Test
    fun `test polimorfismo OCP con EstrategiaFisicaFlotabilidad de Arquimedes`() {
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

        // Vacía: factor = 1.0
        assertEquals(1.0, sub.factorAscenso, 0.001)
        assertEquals(10.0, sub.velocidadAscenso, 0.001)

        // Media carga (25 kg): decae linealmente entre 1.0 y 0.3 -> factor = 1.0 - (0.5 * 0.7) = 0.65
        val minMedia = Mineral("M1", TipoMineral.ARTEFACTO_HUNDIDO, 0, 0) // 8.0 kg
        val minMedia2 = Mineral("M2", TipoMineral.ARTEFACTO_HUNDIDO, 0, 1) // 8.0 kg
        val minMedia3 = Mineral("M3", TipoMineral.ARTEFACTO_HUNDIDO, 0, 2) // 8.0 kg
        val minMedia4 = Mineral("M4", TipoMineral.ARENA_DE_CUARZO, 0, 3) // 1.0 kg (Total = 25.0 kg)
        bodega.agregar(minMedia)
        bodega.agregar(minMedia2)
        bodega.agregar(minMedia3)
        bodega.agregar(minMedia4)

        assertEquals(25.0, bodega.pesoActual, 0.001)
        assertEquals(0.65, sub.factorAscenso, 0.001)
        assertEquals(6.5, sub.velocidadAscenso, 0.001)

        // Llenar bodega hasta 50.0 kg -> factor alcanza exactamente el factorEmpujeMinimo = 0.3
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
    fun `test segregacion de interfaces ISP con Pesable y Cargable`() {
        val mineral: Cargable = Mineral("M1", TipoMineral.NODULO_DE_MANGANESO, 0, 0)
        val submarino: Pesable = Submarino(
            casco = ModuloCasco(500_000.0),
            pesoBase = 800.0,
            bodega = null,
            notificador = NotificadorSilencioso
        )

        // Ambas entidades cumplen con el contrato elemental Pesable
        val objetosPesables: List<Pesable> = listOf(mineral, submarino)
        val pesoTotalCombinado = objetosPesables.sumOf { it.peso }

        assertEquals(4.0, mineral.peso, 0.001)
        assertEquals(800.0, submarino.peso, 0.001)
        assertEquals(804.0, pesoTotalCombinado, 0.001)
    }

    // =========================================================================
    // 5. CASOS DE BORDE, ESTADO INMUTABLE Y VALIDACIONES
    // =========================================================================

    @Test
    fun `test submarino sin bodega (null) mantiene peso base y velocidad nominal`() {
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
    fun `test validacion de invariantes de peso base y velocidad no negativas`() {
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
    fun `test integracion completa recoleccion de minerales y decremento reactivo de velocidad`() {
        val bodega = Bodega(capacidadMaxima = 10.0)
        val detector = DetectorColisionesConcreto()
        val servicio = ServicioRecoleccion(bodega, detector)

        val sub = Submarino(
            posX = 0.0,
            posY = 100.0,
            velocidad = 10.0,
            casco = ModuloCasco(500_000.0),
            pesoBase = 1000.0,
            bodega = bodega,
            estrategiaAscenso = EstrategiaAscensoLineal(factorReduccionMaximo = 0.5),
            notificador = NotificadorSilencioso
        )

        // 1. Inicial: vacía
        assertEquals(10.0, sub.velocidadAscenso, 0.001)

        // 2. Colisión y recolección de Nódulo de Manganeso (4.0 kg) en (0, 100)
        val mineral1 = Mineral("M1", TipoMineral.NODULO_DE_MANGANESO, posX = 0, posY = 100)
        val res1 = servicio.procesarRecoleccion(Posicion(0, 100), listOf(mineral1))
        assertTrue(res1 is ResultadoRecoleccion.Exito)

        // Inmediatamente la velocidad debe reaccionar al nuevo peso acumulado
        // 4.0 kg / 10.0 kg = 40% -> factor = 1.0 - (0.5 * 0.4) = 0.80 -> velocidad = 8.0 m/s
        assertEquals(1004.0, sub.pesoTotal, 0.001)
        assertEquals(8.0, sub.velocidadAscenso, 0.001)

        // 3. Colisión y recolección de Titanio Cristalino (5.0 kg) en (0, 90)
        sub.ascender(10.0) // Sube con lentitud moderada
        val mineral2 = Mineral("M2", TipoMineral.TITANIO_CRISTALINO, posX = 0, posY = sub.posY.toInt())
        val res2 = servicio.procesarRecoleccion(Posicion(0, sub.posY.toInt()), listOf(mineral2))
        assertTrue(res2 is ResultadoRecoleccion.Exito)

        // Peso actual en bodega: 4.0 + 5.0 = 9.0 kg (90% de capacidad)
        // factor = 1.0 - (0.5 * 0.9) = 0.55 -> velocidad = 5.5 m/s
        assertEquals(1009.0, sub.pesoTotal, 0.001)
        assertEquals(5.5, sub.velocidadAscenso, 0.001)
    }
}
