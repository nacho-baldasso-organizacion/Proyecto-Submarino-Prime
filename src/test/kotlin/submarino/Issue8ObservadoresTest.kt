package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Pruebas del patrón Observer en la tienda de mejoras.
 */
class Issue8ObservadoresTest {

    @Test
    fun `test observador de tienda es notificado en compras exitosas y rechazos`() {
        val sub = FabricaDatosPrueba.crearSubmarino(billetera = Monedero(150))
        val mejora = FabricaMejoras.crearMejoraBodega(costoBase = 100, incrementoPorNivel = 100)
        val servicio = crearServicio(mejora)

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

        val res1 = servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertTrue(res1 is ResultadoCompra.Exito)
        assertEquals(mejora, compraExitosaNotificada)
        assertEquals(50, saldoNotificado)
        assertNull(rechazoIdNotificado)

        val res2 = servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertTrue(res2 is ResultadoCompra.FondosInsuficientes)
        assertEquals(mejora.id, rechazoIdNotificado)
        assertNotNull(motivoRechazoNotificado)
        assertTrue(motivoRechazoNotificado?.contains("insuficientes") == true)
    }

    @Test
    fun `test desregistro de observador de tienda`() {
        val sub = FabricaDatosPrueba.crearSubmarino(billetera = Monedero(500))
        val mejora = FabricaMejoras.crearMejoraBodega(costoBase = 100)
        val servicio = crearServicio(mejora)

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

        servicio.eliminarObservador(obs)
        servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertEquals(1, llamadas, "El observador desregistrado no debe recibir nuevos eventos")
    }

    private fun crearServicio(vararg mejoras: Mejora): ServicioUpgrades =
        ServicioUpgrades(CatalogoUpgrades(mejoras.toList()))
}
