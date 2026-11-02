package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class SesionJuegoTest {

    @Test
    fun `el catalogo de la estacion contiene las cuatro mejoras requeridas`() {
        val sesion = SesionJuego()
        val ids = sesion.catalogo().map { it.id }

        assertEquals(4, ids.size)
        assertTrue(FabricaMejoras.ID_BODEGA in ids)
        assertTrue(FabricaMejoras.ID_CASCO in ids)
        assertTrue(FabricaMejoras.ID_PROPULSION in ids)
        assertTrue(SesionJuego.ID_MEJORA_OXIGENO in ids)
        assertFalse(FabricaMejoras.ID_ASCENSO in ids)
    }

    @Test
    fun `la bodega inicia con 10 kg y cuenta con una mejora para ampliarla`() {
        val sesion = SesionJuego()

        assertEquals(10.0, sesion.estado().capacidadBodega, 0.001)

        val mejoraBodega = sesion.catalogo().first { it.id == FabricaMejoras.ID_BODEGA }
        assertTrue(mejoraBodega.obtenerEfecto().magnitud > 0.0)
    }

    @Test
    fun `el acople solo se permite en la superficie dentro de la plataforma`() {
        val sesion = SesionJuego()

        assertFalse(sesion.estado().enZonaAcople)
        assertFalse(sesion.acoplar())

        sesion.actualizar(1.0, 0.0, -1.0)

        val estado = sesion.estado()
        assertTrue(estado.enZonaAcople)
        assertTrue(sesion.acoplar())
        assertTrue(sesion.estado().acoplado)
        assertTrue(sesion.desacoplar())
        assertFalse(sesion.estado().acoplado)
    }

    @Test
    fun `ciclo completo minar acoplar vender y comprar oxigeno`() {
        val sesion = SesionJuego()

        sesion.actualizar(5.2, 0.0, 1.0)
        sesion.actualizar(0.7, 1.0, 0.0)
        assertTrue(sesion.minar().esExito)
        assertEquals(1, sesion.estado().cantidadItems)

        sesion.actualizar(6.2, 0.0, -1.0)
        assertTrue(sesion.acoplar())
        assertTrue(sesion.venderMinerales().esExitoso)
        assertEquals(90, sesion.estado().monedas)

        val compra = sesion.comprarMejora(SesionJuego.ID_MEJORA_OXIGENO)
        val fondos = assertIs<ResultadoCompra.FondosInsuficientes>(compra)
        assertEquals(150, fondos.costoRequerido)
        assertEquals(90, fondos.saldoDisponible)
        assertEquals(100.0, sesion.estado().capacidadOxigeno)
    }

    @Test
    fun `el submarino se destruye y bloquea los controles cuando se agota el oxigeno`() {
        val sesion = SesionJuego()

        var frames = 0
        while (!sesion.estaDestruida() && frames < 600) {
            sesion.actualizar(1.0, 1.0, 0.0)
            frames++
        }

        val estado = sesion.estado()
        assertTrue(estado.destruido, "La sesión debe marcar destrucción por asfixia")
        assertEquals("Oxígeno agotado", estado.causaDestruccion)
        assertEquals(0.0, estado.oxigeno, 0.001)

        assertFalse(sesion.acoplar(), "Una nave destruida no puede acoplarse")
        assertFalse(sesion.minar().esExito, "Una nave destruida no puede minar")
        val posicionAntes = estado.posicionX to estado.profundidad
        sesion.actualizar(1.0, 1.0, 1.0)
        assertEquals(posicionAntes, sesion.estado().posicionX to sesion.estado().profundidad)
    }

    @Test
    fun `la presion abisal destruye el casco cuando se desciende al fondo`() {
        val sesion = SesionJuego()

        var frames = 0
        while (!sesion.estaDestruida() && frames < 600) {
            sesion.actualizar(1.0, 0.0, 1.0)
            frames++
        }

        assertTrue(sesion.estaDestruida(), "El descenso al fondo debe destruir la nave")
        val estado = sesion.estado()
        assertTrue(estado.destruido)
        assertTrue(
            estado.causaDestruccion.startsWith("Casco colapsado"),
            "La causa debe ser el colapso del casco, fue: ${estado.causaDestruccion}"
        )
        assertEquals(0.0, estado.integridadCasco, 0.001)
    }

    @Test
    fun `los nodos se generan espaciados y en cantidad reducida`() {
        val sesion = SesionJuego()
        val nodos = sesion.nodos()

        assertTrue(nodos.size <= 40, "La densidad de nodos debe reducirse (actual: ${nodos.size})")
        assertTrue(nodos.size >= 30, "Debe seguir habiendo nodos suficientes para progresar")

        for (i in nodos.indices) {
            for (j in i + 1 until nodos.size) {
                val distancia = kotlin.math.hypot(
                    nodos[i].x - nodos[j].x,
                    nodos[i].y - nodos[j].y
                )
                assertTrue(
                    distancia >= SesionJuego.DISTANCIA_MINIMA_ENTRE_NODOS,
                    "Los nodos ${nodos[i].id} y ${nodos[j].id} están a ${distancia.toInt()} m"
                )
            }
        }
    }
}
