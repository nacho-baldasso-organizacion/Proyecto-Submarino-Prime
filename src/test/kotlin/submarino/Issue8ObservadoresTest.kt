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
        val (sub, mejora, servicio) = crearContextoCompra()
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
        procesarPrimeraCompra(servicio, mejora, sub, observador)
        assertEquals(50, saldoNotificado)
        procesarSegundaCompra(servicio, mejora, sub, observador)
    }

    private data class ContextoCompra(
        val sub: Submarino,
        val mejora: Mejora,
        val servicio: ServicioUpgrades
    )

    private fun crearContextoCompra(): ContextoCompra {
        val sub = FabricaDatosPrueba.crearSubmarino(billetera = Monedero(150))
        val mejora = FabricaMejoras.crearMejoraBodega(costoBase = 100, incrementoPorNivel = 100)
        val servicio = crearServicio(mejora)
        return ContextoCompra(sub = sub, mejora = mejora, servicio = servicio)
    }

    private fun crearObservador(
        onCompra: (Mejora, Int) -> Unit,
        onRechazo: (String, String) -> Unit
    ): ObservadorTienda {
        return object : ObservadorTienda {
            override fun alComprarMejora(mejora: Mejora, saldoRestante: Int) {
                onCompra(mejora, saldoRestante)
            }

            override fun alRechazarCompra(idMejora: String, motivo: String) {
                onRechazo(idMejora, motivo)
            }
        }
    }

    private fun procesarPrimeraCompra(
        servicio: ServicioUpgrades,
        mejora: Mejora,
        sub: Submarino,
        observador: ObservadorTienda
    ) {
        val res1 = servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertTrue(res1 is ResultadoCompra.Exito)

        val res2 = servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertTrue(res2 is ResultadoCompra.FondosInsuficientes)
    }

    private fun procesarSegundaCompra(
        servicio: ServicioUpgrades,
        mejora: Mejora,
        sub: Submarino,
        observador: ObservadorTienda
    ) {
        val res2 = servicio.comprarMejora(mejora.id, sub.billetera, sub)
        assertTrue(res2 is ResultadoCompra.FondosInsuficientes)
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