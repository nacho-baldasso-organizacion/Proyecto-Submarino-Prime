package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * ============================================================================
 * PRUEBAS UNITARIAS Y DE INTEGRACIÓN - ISSUE #8
 * "Sistema de Upgrades / Mejoras para el submarino"
 *
 * Cumple estrictamente con la regla de CERO funciones sueltas:
 * Todo método y helper se encuentra formalmente encapsulado en [Issue8Test].
 * ============================================================================
 */
class Issue8Test {

    // =========================================================================
    // 1. PRUEBAS DE COMPRA EXITOSA, DÉBITO Y AUMENTO DE ESTADÍSTICAS
    // =========================================================================

    @Test
    fun `test compra exitosa de capacidad de bodega debita saldo e incrementa capacidad`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val monedero = Monedero(saldoInicial = 200)
        val sub = Submarino(
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = monedero,
            notificador = NotificadorSilencioso
        )

        val mejora = FabricaMejoras.crearMejoraBodega(
            incrementoCapacidadKg = 15.0,
            costoBase = 100,
            incrementoPorNivel = 50
        )
        val catalogo = CatalogoUpgrades(listOf(mejora))
        val servicio = ServicioUpgrades(catalogo)

        assertEquals(1, mejora.nivel)
        assertEquals(100, mejora.costo)
        assertEquals(20.0, bodega.capacidadMaxima)
        assertEquals(200, monedero.saldo)

        val resultado = servicio.comprarMejora(mejora.id, sub.billetera, sub)

        assertTrue(resultado is ResultadoCompra.Exito)
        assertEquals(1, resultado.nivelAnterior)
        assertEquals(2, resultado.nuevoNivel)
        assertEquals(100, resultado.costoPagado)
        assertEquals(100, resultado.saldoRestante)
        assertEquals(100, monedero.saldo)

        // Verificar incremento efectivo en las estadísticas de la bodega
        assertEquals(35.0, bodega.capacidadMaxima)
        assertEquals(2, mejora.nivel)
        assertEquals(150, mejora.costo) // 100 + 50 para nivel 2 -> 3
    }

    @Test
    fun `test bodega mejorada permite albergar mayor cantidad y peso de minerales`() {
        val bodega = Bodega(capacidadMaxima = 10.0) // solo entran 10 kg
        val monedero = Monedero(saldoInicial = 300)
        val sub = Submarino(
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = monedero,
            notificador = NotificadorSilencioso
        )

        // Cargar 2 minerales de titanio (5kg cada uno) -> llena la bodega al 100%
        val min1 = Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 0) // 5kg
        val min2 = Mineral("M2", TipoMineral.TITANIO_CRISTALINO, 0, 1) // 5kg
        val min3 = Mineral("M3", TipoMineral.TITANIO_CRISTALINO, 0, 2) // 5kg

        assertTrue(bodega.agregar(min1))
        assertTrue(bodega.agregar(min2))
        assertFalse(bodega.agregar(min3), "Debe rechazarse porque la capacidad máxima es 10.0 kg")

        // Comprar mejora de bodega (+15.0 kg -> nueva capacidad = 25.0 kg)
        val tienda = FabricaMejoras.crearTiendaEstandar()
        val resultado = tienda.comprarParaSubmarino(FabricaMejoras.ID_BODEGA, sub)
        assertTrue(resultado is ResultadoCompra.Exito)
        assertEquals(25.0, bodega.capacidadMaxima)

        // Ahora el tercer mineral sí debe ingresar exitosamente
        assertTrue(bodega.agregar(min3), "Tras la mejora, el tercer mineral debe entrar con éxito")
        assertEquals(15.0, bodega.pesoActual)
        assertEquals(3, bodega.cantidadItems)
    }

    @Test
    fun `test compra exitosa de velocidad de ascenso incrementa velocidad nominal y efectiva`() {
        val sub = Submarino(
            casco = ModuloCasco(500_000.0),
            velocidadAscensoBase = 5.0,
            billetera = Monedero(saldoInicial = 500),
            notificador = NotificadorSilencioso
        )

        val mejora = FabricaMejoras.crearMejoraVelocidadAscenso(
            incrementoVelocidadMs = 2.5,
            costoBase = 120
        )
        val catalogo = CatalogoUpgrades(listOf(mejora))
        val servicio = ServicioUpgrades(catalogo)

        val resultado = servicio.comprarMejora(mejora.id, sub.billetera, sub)

        assertTrue(resultado is ResultadoCompra.Exito)
        assertEquals(380, sub.billetera.saldo) // 500 - 120
        assertEquals(7.5, sub.velocidadAscensoBase, 0.001)
        assertEquals(7.5, sub.velocidadAscenso, 0.001)
    }

    @Test
    fun `test compra exitosa de resistencia de casco permite tolerar mayor sobrepresion`() {
        val casco = ModuloCasco(presionMaximaPa = 100_000.0, integridad = 100.0)
        val sub = Submarino(
            casco = casco,
            billetera = Monedero(saldoInicial = 300),
            notificador = NotificadorSilencioso
        )

        val mejora = FabricaMejoras.crearMejoraResistenciaCasco(
            incrementoPresionPa = 100_000.0,
            costoBase = 150
        )
        val catalogo = CatalogoUpgrades(listOf(mejora))
        val servicio = ServicioUpgrades(catalogo)

        // Sin mejora, una presión de 150,000 Pa generaría daño porque el límite es 100,000 Pa
        // Comprar mejora: límite pasa a 200,000 Pa
        val res = servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertTrue(res is ResultadoCompra.Exito)
        assertEquals(200_000.0, sub.casco.presionMaximaPa)

        // Someter a 150,000 Pa: como 150,000 <= 200,000, la integridad se mantiene intacta en 100.0%
        val integ = sub.verificarIntegridad(150_000.0)
        assertEquals(100.0, integ)
        assertFalse(sub.estaDestruido)
    }

    @Test
    fun `test compra exitosa de velocidad de propulsion horizontal incrementa velocidad de motor`() {
        val sub = Submarino(
            velocidad = 8.0,
            casco = ModuloCasco(500_000.0),
            billetera = Monedero(saldoInicial = 100),
            notificador = NotificadorSilencioso
        )

        val mejora = FabricaMejoras.crearMejoraVelocidadPropulsion(
            incrementoVelocidadMs = 3.0,
            costoBase = 80
        )
        val catalogo = CatalogoUpgrades(listOf(mejora))
        val servicio = ServicioUpgrades(catalogo)

        val res = servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertTrue(res is ResultadoCompra.Exito)
        assertEquals(20, sub.billetera.saldo)
        assertEquals(11.0, sub.velocidad, 0.001)
    }

    @Test
    fun `test compra con saldo exacto deja el monedero en cero`() {
        val sub = Submarino(
            casco = ModuloCasco(500_000.0),
            billetera = Monedero(saldoInicial = 100),
            notificador = NotificadorSilencioso
        )

        val mejora = FabricaMejoras.crearMejoraBodega(costoBase = 100)
        val servicio = ServicioUpgrades(CatalogoUpgrades(listOf(mejora)))

        val res = servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertTrue(res is ResultadoCompra.Exito)
        assertEquals(0, sub.billetera.saldo)
        assertEquals(0, res.saldoRestante)
    }

    // =========================================================================
    // 2. PRUEBAS DE RECHAZO POR FONDOS INSUFICIENTES
    // =========================================================================

    @Test
    fun `test rechazo de compra si saldo es menor al costo requerido`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val monedero = Monedero(saldoInicial = 75) // Requiere 100
        val sub = Submarino(
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = monedero,
            notificador = NotificadorSilencioso
        )

        val mejora = FabricaMejoras.crearMejoraBodega(costoBase = 100)
        val servicio = ServicioUpgrades(CatalogoUpgrades(listOf(mejora)))

        val resultado = servicio.comprarMejora(mejora.id, sub.billetera, sub)

        assertTrue(resultado is ResultadoCompra.FondosInsuficientes)
        assertEquals(mejora.id, resultado.mejoraId)
        assertEquals(100, resultado.costoRequerido)
        assertEquals(75, resultado.saldoDisponible)

        // Verificar que los fondos y las estadísticas permanecen inalterados
        assertEquals(75, monedero.saldo)
        assertEquals(1, mejora.nivel)
        assertEquals(20.0, bodega.capacidadMaxima)
    }

    @Test
    fun `test rechazo de compra si saldo es cero`() {
        val sub = Submarino(
            casco = ModuloCasco(500_000.0),
            billetera = Monedero(saldoInicial = 0),
            notificador = NotificadorSilencioso
        )

        val mejora = FabricaMejoras.crearMejoraVelocidadAscenso(costoBase = 120)
        val servicio = ServicioUpgrades(CatalogoUpgrades(listOf(mejora)))

        val resultado = servicio.comprarMejora(mejora.id, sub.billetera, sub)

        assertTrue(resultado is ResultadoCompra.FondosInsuficientes)
        assertEquals(0, sub.billetera.saldo)
        assertEquals(1, mejora.nivel)
    }

    // =========================================================================
    // 3. PRUEBAS DE PROGRESIÓN Y RECHAZO EN NIVEL MÁXIMO (CAP)
    // =========================================================================

    @Test
    fun `test progresion escalonada hasta nivel maximo`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val monedero = Monedero(saldoInicial = 5000)
        val sub = Submarino(
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = monedero,
            notificador = NotificadorSilencioso
        )

        // Nivel 1 a 3 (máximo 3)
        val mejora = FabricaMejoras.crearMejoraBodega(
            nivelInicial = 1,
            nivelMaximo = 3,
            incrementoCapacidadKg = 10.0,
            costoBase = 100,
            incrementoPorNivel = 50
        )
        val servicio = ServicioUpgrades(CatalogoUpgrades(listOf(mejora)))

        // 1. Compra a Nivel 2 (costo 100)
        assertFalse(mejora.esNivelMaximo)
        val res1 = servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertTrue(res1 is ResultadoCompra.Exito)
        assertEquals(2, mejora.nivel)
        assertEquals(30.0, bodega.capacidadMaxima)
        assertFalse(mejora.esNivelMaximo)

        // 2. Compra a Nivel 3 (costo 150) -> llega al tope
        val res2 = servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertTrue(res2 is ResultadoCompra.Exito)
        assertEquals(3, mejora.nivel)
        assertEquals(40.0, bodega.capacidadMaxima)
        assertTrue(mejora.esNivelMaximo)
        assertEquals(0, mejora.costo, "En nivel máximo el costo reportado debe ser 0")
    }

    @Test
    fun `test intento de compra en nivel maximo es rechazado formalmente`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val monedero = Monedero(saldoInicial = 1000)
        val sub = Submarino(
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = monedero,
            notificador = NotificadorSilencioso
        )

        val mejora = FabricaMejoras.crearMejoraBodega(
            nivelInicial = 2,
            nivelMaximo = 2, // Ya nace en nivel máximo
            incrementoCapacidadKg = 10.0,
            costoBase = 100
        )
        val servicio = ServicioUpgrades(CatalogoUpgrades(listOf(mejora)))

        assertTrue(mejora.esNivelMaximo)
        val saldoPrevio = monedero.saldo

        val resultado = servicio.comprarMejora(mejora.id, sub.billetera, sub)

        assertTrue(resultado is ResultadoCompra.NivelMaximoAlcanzado)
        assertEquals(mejora.id, resultado.mejoraId)
        assertEquals(2, resultado.nivelActual)
        assertEquals(2, resultado.nivelMaximo)

        // Monedas intactas y estadísticas intactas
        assertEquals(saldoPrevio, monedero.saldo)
        assertEquals(20.0, bodega.capacidadMaxima)
        assertFalse(mejora.subirNivel())
    }

    // =========================================================================
    // 4. PRUEBAS DE MANEJO DE ERRORES Y CASOS BORDE
    // =========================================================================

    @Test
    fun `test compra con identificador inexistente retorna MejoraNoEncontrada`() {
        val sub = Submarino(
            casco = ModuloCasco(500_000.0),
            billetera = Monedero(1000),
            notificador = NotificadorSilencioso
        )
        val catalogo = CatalogoUpgrades(emptyList())
        val servicio = ServicioUpgrades(catalogo)

        val resultado = servicio.comprarMejora("ID-FANTASMA", sub.billetera, sub)

        assertTrue(resultado is ResultadoCompra.MejoraNoEncontrada)
        assertEquals("ID-FANTASMA", resultado.mejoraId)
        assertEquals(1000, sub.billetera.saldo)
    }

    @Test
    fun `test validacion de invariantes en modelos de mejora y costo`() {
        // ID vacío
        assertFailsWith<IllegalArgumentException> {
            FabricaMejoras.crearMejoraBodega(id = "")
        }

        // Nombre vacío
        assertFailsWith<IllegalArgumentException> {
            FabricaMejoras.crearMejoraBodega(nombre = "  ")
        }

        // Nivel inicial < 1
        assertFailsWith<IllegalArgumentException> {
            FabricaMejoras.crearMejoraBodega(nivelInicial = 0)
        }

        // Nivel máximo < nivel inicial
        assertFailsWith<IllegalArgumentException> {
            FabricaMejoras.crearMejoraBodega(nivelInicial = 3, nivelMaximo = 2)
        }

        // Costo base negativo o cero
        assertFailsWith<IllegalArgumentException> {
            EstrategiaCostoLineal(costoBase = 0, incrementoPorNivel = 10)
        }
        assertFailsWith<IllegalArgumentException> {
            EstrategiaCostoLineal(costoBase = -50, incrementoPorNivel = 10)
        }

        // Incremento lineal negativo
        assertFailsWith<IllegalArgumentException> {
            EstrategiaCostoLineal(costoBase = 100, incrementoPorNivel = -5)
        }

        // Factor exponencial <= 1.0
        assertFailsWith<IllegalArgumentException> {
            EstrategiaCostoExponencial(costoBase = 100, factorMultiplicador = 1.0)
        }

        // Costo fijo <= 0
        assertFailsWith<IllegalArgumentException> {
            EstrategiaCostoFijo(costoFijo = 0)
        }

        // Magnitud de efecto <= 0
        assertFailsWith<IllegalArgumentException> {
            EfectoCapacidadBodega(magnitud = 0.0)
        }
        assertFailsWith<IllegalArgumentException> {
            EfectoVelocidadAscenso(magnitud = -1.0)
        }
        assertFailsWith<IllegalArgumentException> {
            EfectoResistenciaCasco(magnitud = -100.0)
        }
        assertFailsWith<IllegalArgumentException> {
            EfectoVelocidadPropulsion(magnitud = 0.0)
        }
    }

    @Test
    fun `test submarino sin bodega ignora mejora de bodega de forma segura`() {
        val subSinBodega = Submarino(
            casco = ModuloCasco(500_000.0),
            bodega = null,
            billetera = Monedero(200),
            notificador = NotificadorSilencioso
        )

        val mejora = FabricaMejoras.crearMejoraBodega(costoBase = 50)
        val servicio = ServicioUpgrades(CatalogoUpgrades(listOf(mejora)))

        val resultado = servicio.comprarMejora(mejora.id, subSinBodega.billetera, subSinBodega)
        assertTrue(resultado is ResultadoCompra.Exito)
        assertNull(subSinBodega.bodega)
        assertEquals(150, subSinBodega.billetera.saldo)
    }

    // =========================================================================
    // 5. PRUEBAS DE DESACOPLAMIENTO DE UI Y PATRÓN OBSERVER
    // =========================================================================

    @Test
    fun `test observador de tienda es notificado en compras exitosas y rechazos`() {
        val sub = Submarino(
            casco = ModuloCasco(500_000.0),
            billetera = Monedero(150),
            notificador = NotificadorSilencioso
        )
        val mejora = FabricaMejoras.crearMejoraBodega(costoBase = 100, incrementoPorNivel = 100)
        val servicio = ServicioUpgrades(CatalogoUpgrades(listOf(mejora)))

        var compraExitosaNotificada: Mejora? = null
        var saldoNotificado = -1
        var rechazoIdNotificado: String? = null
        var motivoRechazoNotificado: String? = null

        val observador = object : ObservadorTienda {
            override fun alComprarMejora(mejora: Mejora, saldoRestante: Int) {
                compraExitosaNotificada = mejora
                saldoNotificado = saldoRestante
            }

            override fun alRechazarCompra(idMejora: String, motivo: String) {
                rechazoIdNotificado = idMejora
                motivoRechazoNotificado = motivo
            }
        }

        servicio.registrarObservador(observador)

        // 1. Primera compra exitosa (150 saldo - 100 costo = 50 restante)
        val res1 = servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertTrue(res1 is ResultadoCompra.Exito)
        assertEquals(mejora, compraExitosaNotificada)
        assertEquals(50, saldoNotificado)
        assertNull(rechazoIdNotificado)

        // 2. Segunda compra rechazada por fondos (requiere 200, tiene 50)
        val res2 = servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertTrue(res2 is ResultadoCompra.FondosInsuficientes)
        assertEquals(mejora.id, rechazoIdNotificado)
        assertNotNull(motivoRechazoNotificado)
        assertTrue(motivoRechazoNotificado?.contains("insuficientes") == true)
    }

    @Test
    fun `test desregistro de observador de tienda`() {
        val sub = Submarino(casco = ModuloCasco(500_000.0), billetera = Monedero(500), notificador = NotificadorSilencioso)
        val mejora = FabricaMejoras.crearMejoraBodega(costoBase = 100)
        val servicio = ServicioUpgrades(CatalogoUpgrades(listOf(mejora)))

        var llamadas = 0
        val obs = object : ObservadorTienda {
            override fun alComprarMejora(mejora: Mejora, saldoRestante: Int) {
                llamadas++
            }
            override fun alRechazarCompra(idMejora: String, motivo: String) {
                llamadas++
            }
        }

        servicio.registrarObservador(obs)
        servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertEquals(1, llamadas)

        // Desregistrar observador
        servicio.eliminarObservador(obs)
        servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertEquals(1, llamadas, "El observador desregistrado no debe recibir nuevos eventos")
    }

    // =========================================================================
    // 6. PRUEBAS DE ESTRATEGIAS DE COSTO Y ESCALABILIDAD (OCP)
    // =========================================================================

    @Test
    fun `test estrategia costo lineal escala aditivamente por nivel`() {
        val estrategia = EstrategiaCostoLineal(costoBase = 100, incrementoPorNivel = 50)

        assertEquals(100, estrategia.calcularCosto(nivelActual = 1))
        assertEquals(150, estrategia.calcularCosto(nivelActual = 2))
        assertEquals(200, estrategia.calcularCosto(nivelActual = 3))
        assertEquals(250, estrategia.calcularCosto(nivelActual = 4))
    }

    @Test
    fun `test estrategia costo exponencial escala geometricamente`() {
        val estrategia = EstrategiaCostoExponencial(costoBase = 100, factorMultiplicador = 2.0)

        // Nivel 1: 100 * 2^0 = 100
        assertEquals(100, estrategia.calcularCosto(nivelActual = 1))
        // Nivel 2: 100 * 2^1 = 200
        assertEquals(200, estrategia.calcularCosto(nivelActual = 2))
        // Nivel 3: 100 * 2^2 = 400
        assertEquals(400, estrategia.calcularCosto(nivelActual = 3))
    }

    @Test
    fun `test estrategia costo fijo mantiene valor constante`() {
        val estrategia = EstrategiaCostoFijo(costoFijo = 300)

        assertEquals(300, estrategia.calcularCosto(nivelActual = 1))
        assertEquals(300, estrategia.calcularCosto(nivelActual = 2))
        assertEquals(300, estrategia.calcularCosto(nivelActual = 10))
    }

    @Test
    fun `test extension OCP admite nueva estrategia de costo sin modificar clases existentes`() {
        // Nueva estrategia con descuento por fidelidad (-10% por nivel)
        val estrategiaFidelidad = object : EstrategiaCosto {
            override fun calcularCosto(nivelActual: Int): Int = maxOf(1000 - (nivelActual * 100), 100)
        }

        assertEquals(900, estrategiaFidelidad.calcularCosto(1))
        assertEquals(800, estrategiaFidelidad.calcularCosto(2))

        val mejora = Mejora(
            id = "UPG-CUSTOM",
            nombre = "Blindaje Cuántico",
            descripcion = "Efecto experimental",
            efecto = EfectoCapacidadBodega(20.0),
            estrategiaCosto = estrategiaFidelidad
        )

        assertEquals(900, mejora.costo)
    }

    @Test
    fun `test extension OCP admite nuevo efecto de mejora sin modificar el servicio de compra`() {
        // Nuevo efecto que recarga batería al 100%
        var efectoAplicado = false
        val nuevoEfecto = object : EfectoMejora {
            override val magnitud: Double = 100.0
            override fun aplicar(submarino: Submarino) {
                efectoAplicado = true
            }
        }

        val mejoraNueva = Mejora(
            id = "UPG-BATERIA",
            nombre = "Pila de Fusión",
            descripcion = "Recarga instantánea",
            efecto = nuevoEfecto,
            estrategiaCosto = EstrategiaCostoFijo(50)
        )

        val sub = Submarino(casco = ModuloCasco(500_000.0), billetera = Monedero(100), notificador = NotificadorSilencioso)
        val servicio = ServicioUpgrades(CatalogoUpgrades(listOf(mejoraNueva)))

        val resultado = servicio.comprarMejora("UPG-BATERIA", sub.billetera, sub)
        assertTrue(resultado is ResultadoCompra.Exito)
        assertTrue(efectoAplicado)
        assertEquals(50, sub.billetera.saldo)
    }

    // =========================================================================
    // 7. PRUEBAS DE ARQUITECTURA SOLID (ISP, DIP, LSP, SRP)
    // =========================================================================

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

    // =========================================================================
    // 8. PRUEBAS DE INTEGRACIÓN ECONÓMICA COMPLETA (ISSUE #7 + ISSUE #8)
    // =========================================================================

    @Test
    fun `test ciclo integral mineria venta en superficie y compra de mejoras`() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val monedero = Monedero(saldoInicial = 0)
        val submarino = Submarino(
            posX = 0.0,
            posY = 0.0,
            velocidad = 10.0,
            casco = ModuloCasco(200_000.0),
            bodega = bodega,
            velocidadAscensoBase = 5.0,
            billetera = monedero,
            notificador = NotificadorSilencioso
        )

        val tienda = FabricaMejoras.crearTiendaEstandar()

        // 1. Sin fondos, no puede comprar nada
        val resFallo = tienda.comprarParaSubmarino(FabricaMejoras.ID_BODEGA, submarino)
        assertTrue(resFallo is ResultadoCompra.FondosInsuficientes)

        // 2. Expedición y recolección
        bodega.agregar(Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 50)) // $220
        bodega.agregar(Mineral("M2", TipoMineral.GEMA_DE_SALMUERA, 0, 80))    // $500
        bodega.agregar(Mineral("M3", TipoMineral.ARTEFACTO_HUNDIDO, 0, 100))  // $1200
        assertEquals(3, bodega.cantidadItems)

        // 3. Acople y venta comercial (Issue #7)
        submarino.acoplar()
        val servicioComercio = ServicioComercio()
        val resVenta = servicioComercio.venderBodegaSubmarino(submarino)
        assertTrue(resVenta is ResultadoComercio.Exito)
        assertEquals(1920, submarino.billetera.saldo)
        assertEquals(0, bodega.cantidadItems)

        // 4. Inversión en Upgrades (Issue #8)
        val resBodega = tienda.comprarParaSubmarino(FabricaMejoras.ID_BODEGA, submarino)
        val resAscenso = tienda.comprarParaSubmarino(FabricaMejoras.ID_ASCENSO, submarino)
        val resCasco = tienda.comprarParaSubmarino(FabricaMejoras.ID_CASCO, submarino)
        val resMotor = tienda.comprarParaSubmarino(FabricaMejoras.ID_PROPULSION, submarino)

        assertTrue(resBodega is ResultadoCompra.Exito)
        assertTrue(resAscenso is ResultadoCompra.Exito)
        assertTrue(resCasco is ResultadoCompra.Exito)
        assertTrue(resMotor is ResultadoCompra.Exito)

        // 5. Verificar estadísticas consolidadas del submarino
        assertEquals(35.0, bodega.capacidadMaxima)
        assertEquals(7.0, submarino.velocidadAscensoBase, 0.001)
        assertEquals(350_000.0, submarino.casco.presionMaximaPa)
        assertEquals(12.5, submarino.velocidad, 0.001)

        // Costos: 100 (bodega) + 120 (ascenso) + 150 (casco) + 80 (propulsion) = 450
        // 1920 - 450 = 1470
        assertEquals(1470, submarino.billetera.saldo)
    }

    @Test
    fun `test toString y representacion legible de mejoras y resultados`() {
        val mejora = FabricaMejoras.crearMejoraBodega()
        val strMejora = mejora.toString()
        assertTrue(strMejora.contains("UPG-BODEGA"))
        assertTrue(strMejora.contains("1/5"))

        val exito = ResultadoCompra.Exito(mejora, 1, 2, 100, 50)
        assertTrue(exito.toString().contains("nuevoNivel=2"))

        val fondos = ResultadoCompra.FondosInsuficientes("UPG-1", 100, 20)
        assertTrue(fondos.toString().contains("saldoDisponible=20"))

        val cap = ResultadoCompra.NivelMaximoAlcanzado("UPG-1", 5, 5)
        assertTrue(cap.toString().contains("nivelActual=5"))

        val noEncontrada = ResultadoCompra.MejoraNoEncontrada("UPG-X")
        assertTrue(noEncontrada.toString().contains("UPG-X"))
    }
}
