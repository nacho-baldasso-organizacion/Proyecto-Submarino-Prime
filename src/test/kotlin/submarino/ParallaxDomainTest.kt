package submarino

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests unitarios para la lógica de dominio de parallax vertical y cambio de color según profundidad.
 */
class CapaParallaxTest {

    @Test
    fun testFactor1_0_trasladaProfundidadCompleta() {
        val capa = CapaParallax(1.0)
        val offset = capa.calcularOffsetY(100.0)
        assertEquals(100.0, offset)
    }

    @Test
    fun testFactor0_5_trasladaMitadProfundidad() {
        val capa = CapaParallax(0.5)
        val offset = capa.calcularOffsetY(100.0)
        assertEquals(50.0, offset)
    }

    @Test
    fun testFactor0_2_trasladaQuintaProfundidad() {
        val capa = CapaParallax(0.2)
        val offset = capa.calcularOffsetY(200.0)
        assertEquals(40.0, offset)
    }

    @Test
    fun testFactor0_0_sinDesplazamiento() {
        val capa = CapaParallax(0.0)
        val offset = capa.calcularOffsetY(500.0)
        assertEquals(0.0, offset)
    }

    @Test
    fun testProfundidadCeroOffsetCero() {
        val capa = CapaParallax(1.5)
        val offset = capa.calcularOffsetY(0.0)
        assertEquals(0.0, offset)
    }

    @Test
    fun testProfundidadNegativaDevuelveCero() {
        val capa = CapaParallax(0.5)
        val offset = capa.calcularOffsetY(-10.0)
        assertEquals(0.0, offset)
    }

    @Test
    fun testProfundidadNoFinitaDevuelveCero() {
        val capa = CapaParallax(0.5)

        assertEquals(0.0, capa.calcularOffsetY(Double.NaN))
        assertEquals(0.0, capa.calcularOffsetY(Double.POSITIVE_INFINITY))
        assertEquals(0.0, capa.calcularOffsetY(Double.NEGATIVE_INFINITY))
    }
}

class CalculadorColorProfundidadTest {

    @Test
    fun testSuperficieRetornaColorCalido() {
        val color = CalculadorColorProfundidad.calcularColor(50.0)
        assertEquals(255, color.r)
        assertEquals(200, color.g)
        assertEquals(150, color.b)
    }

    @Test
    fun testZonaFoticaRetornaColorVerdeAzul() {
        val color = CalculadorColorProfundidad.calcularColor(500.0)
        assertEquals(100, color.r)
        assertEquals(150, color.g)
        assertEquals(255, color.b)
    }

    @Test
    fun testZonaAbisalRetornaColorOscuro() {
        val color = CalculadorColorProfundidad.calcularColor(1200.0)
        assertEquals(0, color.r)
        assertEquals(0, color.g)
        assertEquals(80, color.b)
    }

    @Test
    fun testLimiteSuperficie200m() {
        val color = CalculadorColorProfundidad.calcularColor(200.0)
        assertEquals(255, color.r)
    }

    @Test
    fun testLimite1000m() {
        val color = CalculadorColorProfundidad.calcularColor(1000.0)
        assertEquals(0, color.r)
    }

    @Test
    fun testProfundidadMuyGrandeZonaAbisal() {
        val color = CalculadorColorProfundidad.calcularColor(10000.0)
        assertEquals(0, color.r)
        assertEquals(0, color.g)
        assertEquals(80, color.b)
    }

    @Test
    fun testProfundidadNegativaZonaAbisalPorDefecto() {
        val color = CalculadorColorProfundidad.calcularColor(-100.0)
        assertEquals(0, color.r)
    }

    @Test
    fun testZonaSuperficie() {
        assertEquals(
            ColorProfundidad.SUPERFICIE,
            CalculadorColorProfundidad.calcularZona(0.0)
        )
        assertEquals(
            ColorProfundidad.SUPERFICIE,
            CalculadorColorProfundidad.calcularZona(150.0)
        )
    }

    @Test
    fun testZonaFotica() {
        assertEquals(
            ColorProfundidad.FOTICA,
            CalculadorColorProfundidad.calcularZona(200.1)
        )
        assertEquals(
            ColorProfundidad.FOTICA,
            CalculadorColorProfundidad.calcularZona(999.9)
        )
    }

    @Test
    fun testZonaAbisal() {
        assertEquals(
            ColorProfundidad.ABISAL,
            CalculadorColorProfundidad.calcularZona(1000.0)
        )
        assertEquals(
            ColorProfundidad.ABISAL,
            CalculadorColorProfundidad.calcularZona(5000.0)
        )
    }

    @Test
    fun testLimiteSuperficiePerteneceASuperficie() {
        assertEquals(
            ColorProfundidad.SUPERFICIE,
            CalculadorColorProfundidad.calcularZona(CalculadorColorProfundidad.LIMITE_SUPERFICIE)
        )
    }

    @Test
    fun testLimiteFoticaPerteneceAAbisal() {
        assertEquals(
            ColorProfundidad.ABISAL,
            CalculadorColorProfundidad.calcularZona(CalculadorColorProfundidad.LIMITE_ZONA_FOTICA)
        )
    }

    @Test
    fun testProfundidadNoFinitaZonaAbisal() {
        assertEquals(
            ColorProfundidad.ABISAL,
            CalculadorColorProfundidad.calcularZona(Double.NaN)
        )
        assertEquals(
            ColorProfundidad.ABISAL,
            CalculadorColorProfundidad.calcularZona(Double.POSITIVE_INFINITY)
        )
    }
}

class FondoParallaxManagerTest {

    @Test
    fun testCalculaOffsetsYColorParaProfundidadMedia() {
        val profundidad = 400.0 // Dentro de zonaFotica
        val estado = FondoParallaxManager.calcularEstado(profundidad)

        assertEquals(listOf(80.0, 200.0, 400.0), estado.offsetsY)
        assertEquals(100, estado.colorActivo.r)
        assertEquals(150, estado.colorActivo.g)
        assertEquals(255, estado.colorActivo.b)
    }

    @Test
    fun testCalculaOffsetsYColorParaProfundidadSuperficial() {
        val profundidad = 100.0 // Dentro de superficie
        val estado = FondoParallaxManager.calcularEstado(profundidad)

        assertEquals(listOf(20.0, 50.0, 100.0), estado.offsetsY)
        assertEquals(255, estado.colorActivo.r)
        assertEquals(200, estado.colorActivo.g)
        assertEquals(150, estado.colorActivo.b)
    }

    @Test
    fun testCalculaOffsetsYColorParaProfundidadAbisal() {
        val profundidad = 1500.0 // Zona abisal
        val estado = FondoParallaxManager.calcularEstado(profundidad)

        assertEquals(listOf(300.0, 750.0, 1500.0), estado.offsetsY)
        assertEquals(0, estado.colorActivo.r)
        assertEquals(0, estado.colorActivo.g)
        assertEquals(80, estado.colorActivo.b)
    }

    @Test
    fun testMetodoConFactoresPersonalizados() {
        val profundidad = 250.0
        val factores = listOf(0.1, 0.3, 0.7, 1.2)
        val estado = FondoParallaxManager.calcularEstadoConFactores(factores, profundidad)

        val esperados = listOf(25.0, 75.0, 175.0, 300.0)
        assertEquals(esperados, estado.offsetsY)
        assertEquals(100, estado.colorActivo.r)
        assertEquals(150, estado.colorActivo.g)
        assertEquals(255, estado.colorActivo.b)
    }

    @Test
    fun testEstadoInmutableSoloVal() {
        val estado = FondoParallaxManager.calcularEstado(100.0)

        assertEquals(3, estado.offsetsY.size)
        assertTrue(estado.offsetsY.contains(20.0))
        assertTrue(estado.offsetsY.contains(50.0))
        assertTrue(estado.offsetsY.contains(100.0))
    }
}