package org.example

import submarino.Bodega
import submarino.CalculadoraHidrostatica
import submarino.CalculadoraPresionFluido
import submarino.Casco
import submarino.CascoReforzado
import submarino.EntidadFisica
import submarino.EstrategiaAscensoLineal
import submarino.EstacionAcople
import submarino.Mineral
import submarino.ModuloCasco
import submarino.Monedero
import submarino.Movible
import submarino.Navegable
import submarino.Notificador
import submarino.NotificadorSilencioso
import submarino.Pesable
import submarino.PruebaConjunta
import submarino.ResultadoComercio
import submarino.ServicioComercio
import submarino.SimuladorSubmarino
import submarino.Submarino
import submarino.TipoMineral
import submarino.ZonaSuperficie
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertIs

class SubmarinoIntegrationTest {

    @Test
    fun testComposicionYDelegacionCasco() {
        val casco = ModuloCasco(presionMaximaPa = 300_000.0, integridad = 100.0)
        val submarino = Submarino(posX = 0.0, posY = 0.0, velocidad = 5.0, casco = casco)

        assertEquals(casco, submarino.casco)
        assertFalse(submarino.estaDestruido)

        val integridad = submarino.verificarIntegridad(200_000.0)
        assertEquals(100.0, integridad)
        assertFalse(submarino.estaDestruido)

        val nuevaIntegridad = submarino.verificarIntegridad(600_000.0)
        assertTrue(nuevaIntegridad < 100.0)
        assertEquals(submarino.casco.integridad, nuevaIntegridad)
    }

    @Test
    fun testMovimientoYConsumoBateria() {
        val casco = ModuloCasco(presionMaximaPa = 500_000.0, integridad = 100.0)
        val submarino = Submarino(posX = 0.0, posY = 0.0, velocidad = 10.0, casco = casco)

        val movido = submarino.mover(deltaX = 10.0, deltaY = 0.0)
        assertTrue(movido)
        assertEquals(10.0, submarino.posX)
        assertEquals(0.0, submarino.posY)
        assertEquals(95.0, submarino.bateria)

        submarino.mover(deltaX = 0.0, deltaY = -20.0)
        assertEquals(0.0, submarino.posY)
    }

    @Test
    fun testInmersionCriticaYBloqueoDeMovimiento() {
        val casco = ModuloCasco(presionMaximaPa = 100_000.0, integridad = 0.5)
        val submarino = Submarino(posX = 0.0, posY = 0.0, velocidad = 10.0, casco = casco)

        submarino.verificarIntegridad(500_000.0)
        assertTrue(submarino.estaDestruido)
        assertTrue(casco.estaDestruido)
        assertEquals(0.0, casco.integridad)

        val posPreviaX = submarino.posX
        val resultadoMovimiento = submarino.mover(deltaX = 15.0, deltaY = 0.0)
        assertFalse(resultadoMovimiento)
        assertEquals(posPreviaX, submarino.posX)
    }

    @Test
    fun testCalculadoraHidrostatica() {
        val presionSuperficie = CalculadoraHidrostatica.calcularPresion(0.0)
        assertEquals(CalculadoraHidrostatica.PRESION_ATMOSFERICA, presionSuperficie)

        val profMax = CalculadoraHidrostatica.calcularProfundidadMaxima(CalculadoraHidrostatica.PRESION_ATMOSFERICA)
        assertEquals(0.0, profMax)
    }

    @Test
    fun testEjecucionSimulacionCompleta() {
        SimuladorSubmarino().ejecutarSimulacionCompleta()
        PruebaConjunta().ejecutarSimulacionCompleta()
    }

    @Test
    fun testPrincipioAbiertoCerradoYPolimorfismoCasco() {
        val cascoEstandar = ModuloCasco(presionMaximaPa = 200_000.0, integridad = 100.0, notificador = NotificadorSilencioso)
        val cascoReforzado = CascoReforzado(presionMaximaPa = 200_000.0, integridad = 100.0, factorAbsorcion = 0.5, notificador = NotificadorSilencioso)

        val subEstandar = Submarino(casco = cascoEstandar, notificador = NotificadorSilencioso)
        val subReforzado = Submarino(casco = cascoReforzado, notificador = NotificadorSilencioso)

        val presion = 400_000.0
        val integEstandar = subEstandar.verificarIntegridad(presion)
        val integReforzada = subReforzado.verificarIntegridad(presion)

        assertEquals(99.0, integEstandar)
        assertEquals(99.5, integReforzada)
    }

    @Test
    fun testPrincipioAbiertoCerradoFisicaFluidos() {
        val aguaDulce = CalculadoraPresionFluido(densidadFluido = 1000.0)
        val aguaMar = CalculadoraHidrostatica

        val presionDulce = aguaDulce.calcularPresion(10.0)
        val presionMar = aguaMar.calcularPresion(10.0)

        assertTrue(presionMar > presionDulce)
    }

    @Test
    fun testSegregacionDeInterfacesNavegable() {
        val submarino: Navegable = Submarino(
            casco = ModuloCasco(500_000.0),
            notificador = NotificadorSilencioso
        )

        assertTrue(submarino.navegar(10.0))
        assertTrue(submarino.sumergir(20.0))
        assertTrue(submarino.ascender(5.0))
    }

    @Test
    fun testPrincipioSustitucionLiskov() {
        val entidad: EntidadFisica = Submarino(
            posX = 5.0,
            posY = 10.0,
            casco = ModuloCasco(500_000.0),
            notificador = NotificadorSilencioso
        )

        val movible: Movible = entidad
        val exito = movible.mover(15.0, 5.0)

        assertTrue(exito)
        assertEquals(20.0, entidad.posX)
        assertEquals(15.0, entidad.posY)
    }

    @Test
    fun testDesacoplamientoNotificadorSRP() {
        val mensajesCapturados = mutableListOf<String>()
        val mockNotificador = object : Notificador {
            override fun notificar(mensaje: String) {
                mensajesCapturados.add(mensaje)
            }
        }

        val casco = ModuloCasco(presionMaximaPa = 100_000.0, integridad = 0.5, notificador = mockNotificador)
        val sub = Submarino(casco = casco, notificador = mockNotificador)

        sub.verificarIntegridad(500_000.0)
        sub.mover(10.0, 0.0)

        assertTrue(mensajesCapturados.isNotEmpty())
        assertTrue(mensajesCapturados.any { it.contains("colapsado") })
        assertTrue(mensajesCapturados.any { it.contains("destruido") })
    }

    @Test
    fun testCalculoPesoTotalYVariacionVelocidadAscensoIssue6() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val submarino = Submarino(
            casco = ModuloCasco(500_000.0),
            pesoBase = 1000.0,
            bodega = bodega,
            velocidad = 10.0,
            estrategiaAscenso = EstrategiaAscensoLineal(factorReduccionMaximo = 0.5),
            notificador = NotificadorSilencioso
        )

        assertEquals(1000.0, submarino.pesoTotal)
        assertEquals(10.0, submarino.velocidadAscenso)
        assertEquals(1.0, submarino.factorAscenso)

        bodega.agregar(Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 0))
        bodega.agregar(Mineral("M2", TipoMineral.TITANIO_CRISTALINO, 0, 1))
        assertEquals(1010.0, submarino.pesoTotal)
        assertEquals(7.5, submarino.velocidadAscenso)
        assertEquals(0.75, submarino.factorAscenso)

        bodega.agregar(Mineral("M3", TipoMineral.TITANIO_CRISTALINO, 0, 2))
        bodega.agregar(Mineral("M4", TipoMineral.TITANIO_CRISTALINO, 0, 3))
        assertEquals(1020.0, submarino.pesoTotal)
        assertEquals(5.0, submarino.velocidadAscenso)
        assertEquals(0.50, submarino.factorAscenso)
    }

    @Test
    fun testManiobraAscensoRalentizadaPorCargaIssue6() {
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

        for (i in 1..4) {
            bodega.agregar(Mineral("M$i", TipoMineral.TITANIO_CRISTALINO, 0, i))
        }

        sub.ascender(10.0)
        assertEquals(45.0, sub.posY)
    }

    @Test
    fun testFlujoCompletoDeAcopleYComercioEnSuperficieIssue7() {
        val zonaSuperficie = ZonaSuperficie(profundidadMaxima = 0.0)
        val estacion = EstacionAcople(zonaAcople = zonaSuperficie)
        val servicioComercio = ServicioComercio()

        val bodega = Bodega(capacidadMaxima = 30.0)
        bodega.agregar(Mineral("M1", TipoMineral.TITANIO_CRISTALINO, 0, 0))
        bodega.agregar(Mineral("M2", TipoMineral.NODULO_DE_MANGANESO, 0, 1))

        val billetera = Monedero(saldoInicial = 100)
        val submarino = Submarino(
            posX = 20.0,
            posY = 40.0,
            velocidad = 10.0,
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = billetera,
            notificador = NotificadorSilencioso
        )

        assertFalse(estacion.puedeAcoplar(submarino.posX, submarino.posY))
        assertFalse(submarino.estaEnZonaAcople(estacion.zonaAcople))

        submarino.ascender(deltaY = 40.0)
        assertEquals(0.0, submarino.posY)
        assertTrue(submarino.estaEnSuperficie)
        assertTrue(submarino.estaEnZonaAcople(estacion.zonaAcople))
        assertTrue(estacion.puedeAcoplar(submarino.posX, submarino.posY))

        val acoplado = estacion.acoplar(submarino, submarino.posX, submarino.posY)
        assertTrue(acoplado)
        assertTrue(submarino.estaAcoplado)
        assertTrue(estacion.hayNaveAcoplada)

        val resultado = servicioComercio.venderBodegaSubmarino(submarino)
        assertIs<ResultadoComercio.Exito>(resultado)
        assertEquals(310, resultado.transaccion.totalCreditos)
        assertEquals(410, submarino.billetera.saldo)
        assertEquals(0.0, bodega.pesoActual, 0.001)
        assertEquals(0, bodega.cantidadItems)

        assertTrue(estacion.desacoplar())
        assertFalse(submarino.estaAcoplado)
        assertFalse(estacion.hayNaveAcoplada)
    }

    @Test
    fun testRechazoVentaEnInmersionSinAcopleIssue7() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val billetera = Monedero(saldoInicial = 50)
        val submarino = Submarino(
            posX = 0.0,
            posY = 50.0,
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            billetera = billetera,
            notificador = NotificadorSilencioso
        )
        bodega.agregar(Mineral("M1", TipoMineral.ARENA_DE_CUARZO, 0, 50))

        val servicioComercio = ServicioComercio()
        val resultado = servicioComercio.venderBodegaSubmarino(submarino)

        assertIs<ResultadoComercio.NaveNoAcoplada>(resultado)
        assertEquals(50, submarino.billetera.saldo)
        assertEquals(1, bodega.cantidadItems)
    }

    @Test
    fun testRestauracionVelocidadAscensoTrasVentaEnEstacionIssue7() {
        val bodega = Bodega(capacidadMaxima = 20.0)
        val submarino = Submarino(
            posX = 0.0,
            posY = 0.0,
            velocidad = 10.0,
            casco = ModuloCasco(500_000.0),
            bodega = bodega,
            estrategiaAscenso = EstrategiaAscensoLineal(factorReduccionMaximo = 0.5),
            notificador = NotificadorSilencioso
        )

        for (i in 1..4) {
            bodega.agregar(Mineral("M$i", TipoMineral.TITANIO_CRISTALINO, 0, i))
        }
        assertEquals(5.0, submarino.velocidadAscenso, 0.001)

        submarino.acoplar()
        val servicioComercio = ServicioComercio()
        val resultado = servicioComercio.venderBodegaSubmarino(submarino)

        assertIs<ResultadoComercio.Exito>(resultado)
        assertEquals(0.0, bodega.pesoActual, 0.001)
        assertEquals(10.0, submarino.velocidadAscenso, 0.001)
        assertEquals(1.0, submarino.factorAscenso, 0.001)
    }
}
