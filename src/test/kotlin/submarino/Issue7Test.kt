package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class Issue7Test {

    // =========================================================================
    // 1. PRUEBAS DE DETECCIÓN DE SUPERFICIE Y ZONA DE ACOPLE
    // =========================================================================

    @Test
    fun `test submarino detecta cuando ha llegado a la superficie`() {
        val subSumergido = Submarino(
            posX = 10.0,
            posY = 50.0,
            casco = ModuloCasco(500_000.0),
            notificador = NotificadorSilencioso
        )
        assertFalse(subSumergido.estaEnSuperficie)

        val subSuperficie = Submarino(
            posX = 10.0,
            posY = 0.0,
            casco = ModuloCasco(500_000.0),
            notificador = NotificadorSilencioso
        )
        assertTrue(subSuperficie.estaEnSuperficie)
    }

    @Test
    fun `test zona superficie valida limites verticales adecuadamente`() {
        val zona = ZonaSuperficie(profundidadMaxima = 0.0)

        assertTrue(zona.estaEnZona(x = 0.0, y = 0.0))
        assertTrue(zona.estaEnZona(x = 100.0, y = 0.0))
        assertFalse(zona.estaEnZona(x = 0.0, y = 0.1))
        assertFalse(zona.estaEnZona(x = 50.0, y = 10.0))
    }

    @Test
    fun `test zona plataforma valida rangos horizontales y verticales`() {
        val plataforma = ZonaAcoplePlataforma(xMin = 20.0, xMax = 80.0, profundidadMaxima = 0.0)

        // Dentro de la plataforma en superficie
        assertTrue(plataforma.estaEnZona(x = 20.0, y = 0.0))
        assertTrue(plataforma.estaEnZona(x = 50.0, y = 0.0))
        assertTrue(plataforma.estaEnZona(x = 80.0, y = 0.0))

        // Fuera por coordenadas X
        assertFalse(plataforma.estaEnZona(x = 19.9, y = 0.0))
        assertFalse(plataforma.estaEnZona(x = 80.1, y = 0.0))

        // Fuera por profundidad Y
        assertFalse(plataforma.estaEnZona(x = 50.0, y = 1.0))
    }

    @Test
    fun `test estacion acopla exitosamente a nave en posicion valida`() {
        val estacion = EstacionAcople(zonaAcople = ZonaSuperficie())
        val sub = Submarino(
            posX = 0.0,
            posY = 0.0,
            casco = ModuloCasco(500_000.0),
            notificador = NotificadorSilencioso
        )

        assertFalse(sub.estaAcoplado)
        assertFalse(estacion.hayNaveAcoplada)
        assertTrue(estacion.puedeAcoplar(sub.posX, sub.posY))

        val exitoAcople = estacion.acoplar(sub, sub.posX, sub.posY)
        assertTrue(exitoAcople)
        assertTrue(sub.estaAcoplado)
        assertTrue(estacion.hayNaveAcoplada)
        assertEquals(sub, estacion.naveAcoplada)
    }

    @Test
    fun `test estacion rechaza acople si la nave esta sumergida fuera de zona`() {
        val estacion = EstacionAcople(zonaAcople = ZonaSuperficie(profundidadMaxima = 0.0))
        val sub = Submarino(
            posX = 0.0,
            posY = 25.0, // Sumergido
            casco = ModuloCasco(500_000.0),
            notificador = NotificadorSilencioso
        )

        assertFalse(estacion.puedeAcoplar(sub.posX, sub.posY))
        val exitoAcople = estacion.acoplar(sub, sub.posX, sub.posY)
        assertFalse(exitoAcople)
        assertFalse(sub.estaAcoplado)
        assertFalse(estacion.hayNaveAcoplada)
    }

    @Test
    fun `test estacion rechaza acople si ya se encuentra ocupada por otra nave`() {
        val estacion = EstacionAcople()
        val sub1 = Submarino(posX = 0.0, posY = 0.0, casco = ModuloCasco(500_000.0), notificador = NotificadorSilencioso)
        val sub2 = Submarino(posX = 0.0, posY = 0.0, casco = ModuloCasco(500_000.0), notificador = NotificadorSilencioso)

        assertTrue(estacion.acoplar(sub1, 0.0, 0.0))
        assertTrue(sub1.estaAcoplado)

        // Intento de segunda nave
        assertFalse(estacion.puedeAcoplar(0.0, 0.0))
        val exitoSub2 = estacion.acoplar(sub2, 0.0, 0.0)
        assertFalse(exitoSub2)
        assertFalse(sub2.estaAcoplado)
        assertEquals(sub1, estacion.naveAcoplada)
    }

    @Test
    fun `test desacople voluntario libera la estacion y actualiza la nave`() {
        val estacion = EstacionAcople()
        val sub = Submarino(posX = 0.0, posY = 0.0, casco = ModuloCasco(500_000.0), notificador = NotificadorSilencioso)

        estacion.acoplar(sub, 0.0, 0.0)
        assertTrue(sub.estaAcoplado)

        val exitoDesacople = estacion.desacoplar()
        assertTrue(exitoDesacople)
        assertFalse(sub.estaAcoplado)
        assertFalse(estacion.hayNaveAcoplada)
        assertNull(estacion.naveAcoplada)

        // Desacoplar cuando está vacía retorna false
        assertFalse(estacion.desacoplar())
    }

    @Test
    fun `test navegacion o movimiento de submarino lo desacopla automaticamente`() {
        val sub = Submarino(posX = 0.0, posY = 0.0, casco = ModuloCasco(500_000.0), notificador = NotificadorSilencioso)
        sub.acoplar()
        assertTrue(sub.estaAcoplado)

        // Navegar horizontalmente
        sub.navegar(10.0)
        assertFalse(sub.estaAcoplado, "Al navegar hacia adelante debe desacoplarse automáticamente")

        // Sumergirse
        sub.acoplar()
        assertTrue(sub.estaAcoplado)
        sub.sumergir(5.0)
        assertFalse(sub.estaAcoplado, "Al sumergirse debe desacoplarse automáticamente")
    }

    @Test
    fun `test observador de acople recibe notificaciones de eventos (Observer)`() {
        val estacion = EstacionAcople()
        val sub = Submarino(posX = 0.0, posY = 0.0, casco = ModuloCasco(500_000.0), notificador = NotificadorSilencioso)

        var eventosAcople = 0
        var eventosDesacople = 0

        val observador = object : ObservadorAcople {
            override fun alAcoplar(nave: Acoplable) {
                eventosAcople++
            }

            override fun alDesacoplar(nave: Acoplable) {
                eventosDesacople++
            }
        }

        estacion.registrarObservador(observador)
        estacion.acoplar(sub, 0.0, 0.0)
        assertEquals(1, eventosAcople)
        assertEquals(0, eventosDesacople)

        estacion.desacoplar()
        assertEquals(1, eventosAcople)
        assertEquals(1, eventosDesacople)

        // Al desregistrar no debe recibir más eventos
        estacion.eliminarObservador(observador)
        estacion.acoplar(sub, 0.0, 0.0)
        assertEquals(1, eventosAcople)
    }

    // =========================================================================
    // 2. PRUEBAS DE ESTRATEGIAS Y CÁLCULO DE PRECIOS (OCP)
    // =========================================================================

    @Test
    fun `test estrategia precios base valua correctamente segun catalogo oficial`() {
        val estrategia = EstrategiaPreciosBase()

        val arena = Mineral("M1", TipoMineral.ARENA_DE_CUARZO, 0, 0)
        val cobre = Mineral("M2", TipoMineral.COBRE_MARINO, 0, 0)
        val nodulo = Mineral("M3", TipoMineral.NODULO_DE_MANGANESO, 0, 0)
        val titanio = Mineral("M4", TipoMineral.TITANIO_CRISTALINO, 0, 0)
        val gema = Mineral("M5", TipoMineral.GEMA_DE_SALMUERA, 0, 0)
        val artefacto = Mineral("M6", TipoMineral.ARTEFACTO_HUNDIDO, 0, 0)

        assertEquals(10, estrategia.calcularPrecio(arena))
        assertEquals(35, estrategia.calcularPrecio(cobre))
        assertEquals(90, estrategia.calcularPrecio(nodulo))
        assertEquals(220, estrategia.calcularPrecio(titanio))
        assertEquals(500, estrategia.calcularPrecio(gema))
        assertEquals(1200, estrategia.calcularPrecio(artefacto))
    }

    @Test
    fun `test estrategia precios multiplicador bonifica las ventas (OCP)`() {
        val estrategiaBonus = EstrategiaPreciosMultiplicador(factor = 1.5) // +50% de bonus
        val mineral = Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 0) // $220 base

        // 220 * 1.5 = 330
        assertEquals(330, estrategiaBonus.calcularPrecio(mineral))
    }

    @Test
    fun `test extension OCP admite nuevo vendible sin modificar clases existentes`() {
        // Creamos un nuevo recurso vendible que no es un mineral
        val nuevoTesoro = object : Vendible {
            override val valor: Int = 2500
        }

        val calculadora = CalculadoraPreciosEstandar()
        assertEquals(2500, calculadora.calcularPrecio(nuevoTesoro))
        assertEquals(2500, calculadora.calcularTotal(listOf(nuevoTesoro)))
    }

    @Test
    fun `test calculadora precios estandar totaliza correctamente colecciones`() {
        val calculadora = CalculadoraPreciosEstandar()
        val items = listOf(
            Mineral("M1", TipoMineral.ARENA_DE_CUARZO, 0, 0),      // $10
            Mineral("M2", TipoMineral.COBRE_MARINO, 0, 0),         // $35
            Mineral("M3", TipoMineral.NODULO_DE_MANGANESO, 0, 0)   // $90
        )

        assertEquals(135, calculadora.calcularTotal(items))
        assertEquals(0, calculadora.calcularTotal(emptyList()))
    }

    // =========================================================================
    // 3. PRUEBAS DE VENTA DE BODEGA, ECONOMÍA Y VACIADO TRAS TRANSACCIÓN
    // =========================================================================

    @Test
    fun `test venta de bodega exitosa incrementa fondos y vacia el almacenamiento`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val billetera = Monedero(saldoInicial = 50)
        val sub = Submarino(
            posX = 0.0,
            posY = 0.0,
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = billetera,
            notificador = NotificadorSilencioso
        )

        // Cargar minerales: Titanio ($220, 5kg) y Cobre ($35, 2.5kg) -> Total: $255, 7.5kg
        val min1 = Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 0)
        val min2 = Mineral("M2", TipoMineral.COBRE_MARINO, 0, 0)
        assertTrue(bodega.agregar(min1))
        assertTrue(bodega.agregar(min2))

        assertEquals(7.5, bodega.pesoActual, 0.001)
        assertEquals(2, bodega.cantidadItems)
        assertEquals(255, bodega.valorTotal)
        assertEquals(50, billetera.saldo)

        // Acoplar nave
        sub.acoplar()
        assertTrue(sub.estaAcoplado)

        val servicio = ServicioComercio()
        val resultado = servicio.venderBodegaSubmarino(sub)

        assertTrue(resultado is ResultadoComercio.Exito)
        assertEquals(255, resultado.transaccion.totalCreditos)
        assertEquals(2, resultado.transaccion.cantidadItems)
        assertEquals(305, resultado.saldoActualizado) // 50 inicial + 255 venta

        // Verificar que los fondos se incrementaron en la billetera
        assertEquals(305, sub.billetera.saldo)

        // Verificar que la bodega quedó completamente vacía
        assertEquals(0.0, bodega.pesoActual, 0.001)
        assertEquals(0, bodega.cantidadItems)
        assertEquals(0, bodega.valorTotal)
        assertTrue(bodega.obtenerElementos().isEmpty())

        // Verificar que el peso del submarino se restableció al peso base
        assertEquals(sub.pesoBase, sub.pesoTotal, 0.001)
        assertEquals(1.0, sub.factorAscenso, 0.001)
    }

    @Test
    fun `test intento de venta con nave no acoplada es rechazado`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val billetera = Monedero(saldoInicial = 100)
        val sub = Submarino(
            posX = 0.0,
            posY = 50.0, // Sumergido, sin acoplar
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = billetera,
            notificador = NotificadorSilencioso
        )

        bodega.agregar(Mineral("M1", TipoMineral.ARENA_DE_CUARZO, 0, 50))
        assertFalse(sub.estaAcoplado)

        val servicio = ServicioComercio()
        val resultado = servicio.venderBodegaSubmarino(sub)

        assertTrue(resultado is ResultadoComercio.NaveNoAcoplada)
        // Fondos intactos
        assertEquals(100, billetera.saldo)
        // Bodega intacta
        assertEquals(1, bodega.cantidadItems)
        assertEquals(1.0, bodega.pesoActual, 0.001)
    }

    @Test
    fun `test intento de venta con bodega vacia informa BodegaVacia`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val billetera = Monedero(saldoInicial = 200)
        val sub = Submarino(
            posX = 0.0,
            posY = 0.0,
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = billetera,
            notificador = NotificadorSilencioso
        )

        sub.acoplar()
        assertTrue(sub.estaAcoplado)
        assertEquals(0, bodega.cantidadItems)

        val servicio = ServicioComercio()
        val resultado = servicio.venderBodegaSubmarino(sub)

        assertTrue(resultado is ResultadoComercio.BodegaVacia)
        assertEquals(200, billetera.saldo)
    }

    @Test
    fun `test multiples ventas consecutivas acumulan creditos correctamente`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val sub = Submarino(
            posX = 0.0,
            posY = 0.0,
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = Monedero(0),
            notificador = NotificadorSilencioso
        )
        sub.acoplar()

        val servicio = ServicioComercio()

        // Primera venta: 1 Arena de cuarzo ($10)
        bodega.agregar(Mineral("M1", TipoMineral.ARENA_DE_CUARZO, 0, 0))
        val res1 = servicio.venderBodegaSubmarino(sub)
        assertTrue(res1 is ResultadoComercio.Exito)
        assertEquals(10, sub.billetera.saldo)

        // Segunda venta: 1 Nódulo de manganeso ($90)
        bodega.agregar(Mineral("M2", TipoMineral.NODULO_DE_MANGANESO, 0, 0))
        val res2 = servicio.venderBodegaSubmarino(sub)
        assertTrue(res2 is ResultadoComercio.Exito)
        assertEquals(100, sub.billetera.saldo) // 10 + 90
    }

    // =========================================================================
    // 4. PRUEBAS DE BILLETERA Y GESTIÓN DE FONDOS (SRP, ISP)
    // =========================================================================

    @Test
    fun `test monedero acredita y debita fondos correctamente`() {
        val monedero = Monedero(saldoInicial = 100)
        assertEquals(100, monedero.saldo)

        monedero.acreditar(50)
        assertEquals(150, monedero.saldo)

        // Débito exitoso
        assertTrue(monedero.debitar(70))
        assertEquals(80, monedero.saldo)

        // Débito de valor exacto
        assertTrue(monedero.debitar(80))
        assertEquals(0, monedero.saldo)

        // Débito insuficiente
        assertFalse(monedero.debitar(1))
        assertEquals(0, monedero.saldo)
    }

    @Test
    fun `test monedero valida invariantes de no negatividad`() {
        assertFailsWith<IllegalArgumentException> {
            Monedero(saldoInicial = -10)
        }

        val monedero = Monedero(saldoInicial = 0)
        assertFailsWith<IllegalArgumentException> {
            monedero.acreditar(-5)
        }

        assertFailsWith<IllegalArgumentException> {
            monedero.debitar(-20)
        }
    }

    @Test
    fun `test segregacion de interfaces ISP con billetera y monedero`() {
        val monedero: Billetera = Monedero(saldoInicial = 500)

        // Polimorfismo a través de ReceptorFondos
        val receptor: ReceptorFondos = monedero
        receptor.acreditar(200)
        assertEquals(700, receptor.saldo)

        // Polimorfismo a través de Pagable
        val pagable: Pagable = monedero
        assertTrue(pagable.debitar(300))
        assertEquals(400, pagable.saldo)

        // Polimorfismo a través de ConsultableSaldo
        val consultable: ConsultableSaldo = monedero
        assertEquals(400, consultable.saldo)
    }

    // =========================================================================
    // 5. PRUEBAS DE DESACOPLAMIENTO DE UI Y PATRÓN OBSERVER
    // =========================================================================

    @Test
    fun `test observador de comercio es notificado con recibo transaccional completo`() {
        val bodega = Bodega(capacidadMaxima = 30.0)
        val sub = Submarino(
            posX = 0.0,
            posY = 0.0,
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = Monedero(0),
            notificador = NotificadorSilencioso
        )
        sub.acoplar()

        val min1 = Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 0) // $220
        val min2 = Mineral("M2", TipoMineral.GEMA_DE_SALMUERA, 0, 0)     // $500
        bodega.agregar(min1)
        bodega.agregar(min2)

        var reciboRecibido: TransaccionComercial? = null

        val observador = object : ObservadorComercio {
            override fun alRealizarVenta(transaccion: TransaccionComercial) {
                reciboRecibido = transaccion
            }
        }

        val servicio = ServicioComercio()
        servicio.registrarObservador(observador)

        val res = servicio.venderBodegaSubmarino(sub)
        assertTrue(res is ResultadoComercio.Exito)

        assertNotNull(reciboRecibido)
        assertEquals(720, reciboRecibido?.totalCreditos)
        assertEquals(2, reciboRecibido?.cantidadItems)
        assertEquals(2, reciboRecibido?.itemsVendidos?.size)
        assertTrue(reciboRecibido?.itemsVendidos?.contains(min1) == true)
        assertTrue(reciboRecibido?.itemsVendidos?.contains(min2) == true)
    }

    @Test
    fun `test desregistro de observador comercial`() {
        val bodega = Bodega(capacidadMaxima = 10.0)
        val sub = Submarino(posX = 0.0, posY = 0.0, casco = ModuloCasco(500_000.0), bodega = bodega, notificador = NotificadorSilencioso)
        sub.acoplar()
        bodega.agregar(Mineral("M1", TipoMineral.ARENA_DE_CUARZO, 0, 0))

        var llamadas = 0
        val obs = object : ObservadorComercio {
            override fun alRealizarVenta(transaccion: TransaccionComercial) {
                llamadas++
            }
        }

        val servicio = ServicioComercio()
        servicio.registrarObservador(obs)
        servicio.venderBodegaSubmarino(sub)
        assertEquals(1, llamadas)

        // Cargar otro y desregistrar
        bodega.agregar(Mineral("M2", TipoMineral.ARENA_DE_CUARZO, 0, 0))
        servicio.eliminarObservador(obs)
        servicio.venderBodegaSubmarino(sub)
        assertEquals(1, llamadas) // No se incrementó
    }

    // =========================================================================
    // 6. PRUEBAS DE ARQUITECTURA SOLID (DIP, LSP, ISP, OCP, SRP)
    // =========================================================================

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
}
