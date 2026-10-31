package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Pruebas de superficie, zonas de acople, estación de acople y desacople automático.
 */
class Issue7AcopleTest {

    private fun crearSubmarinoPrueba(
        posX: Double = 0.0,
        posY: Double = 0.0,
        bodega: Almacenamiento? = null,
        billetera: Billetera = Monedero(0)
    ): Submarino = Submarino(
        posX = posX,
        posY = posY,
        casco = ModuloCasco(500_000.0),
        bodega = bodega,
        billetera = billetera,
        notificador = NotificadorSilencioso
    )

    @Test
    fun `test submarino detecta cuando ha llegado a la superficie`() {
        val subSumergido = crearSubmarinoPrueba(posX = 10.0, posY = 50.0)
        assertFalse(subSumergido.estaEnSuperficie)

        val subSuperficie = crearSubmarinoPrueba(posX = 10.0, posY = 0.0)
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
}
