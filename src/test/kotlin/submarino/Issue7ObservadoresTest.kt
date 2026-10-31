package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Pruebas del patrón Observer aplicado al acople y al comercio.
 */
class Issue7ObservadoresTest {

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
}
