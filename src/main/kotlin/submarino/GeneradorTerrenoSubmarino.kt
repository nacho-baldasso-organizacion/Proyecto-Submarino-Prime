package submarino

import kotlin.math.PI
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

/**
 * Generador procedural del relieve submarino: formaciones rocosas y suelo marino.
 *
 * Responsabilidad ��nica (SRP): construir el conjunto de [FormacionRocosa] a partir
 * de una semilla, sin conocer c��mo se consultan o representan despuǸs.
 */
class GeneradorTerrenoSubmarino {

    fun crear(semilla: Long, anchoMapa: Double, altoMapa: Double): TerrenoSubmarino {
        val random = Random(semilla * 31L + 17L)
        val formaciones = mutableListOf<FormacionRocosa>()
        formaciones.addAll(formacionesAncla())
        repeat(CANTIDAD_FORMACIONES) { indice ->
            formaciones.add(crearFormacionProcedural(indice, random, anchoMapa, altoMapa))
        }
        return TerrenoSubmarino(formaciones, crearSueloMarino(random, anchoMapa, altoMapa))
    }

    private fun formacionesAncla(): List<FormacionRocosa> = listOf(
        FormacionRocosa(
            id = "MONTANA-COBRE",
            puntos = listOf(
                PuntoCresta(1460.0, 980.0),
                PuntoCresta(1720.0, 910.0),
                PuntoCresta(1960.0, 820.0),
                PuntoCresta(2100.0, 720.0),
                PuntoCresta(2200.0, 650.0),
                PuntoCresta(2320.0, 515.0),
                PuntoCresta(2410.0, 395.0),
                PuntoCresta(2500.0, 300.0),
                PuntoCresta(2615.0, 455.0),
                PuntoCresta(2760.0, 690.0),
                PuntoCresta(2940.0, 855.0),
                PuntoCresta(3220.0, 965.0)
            ),
            baseY = 1060.0
        ),
        FormacionRocosa(
            id = "ARRECIFE-HIERRO",
            puntos = listOf(
                PuntoCresta(2190.0, 1770.0),
                PuntoCresta(2400.0, 1680.0),
                PuntoCresta(2520.0, 1590.0),
                PuntoCresta(2650.0, 1450.0),
                PuntoCresta(2790.0, 1515.0),
                PuntoCresta(2960.0, 1660.0),
                PuntoCresta(3170.0, 1775.0)
            ),
            baseY = 1830.0
        )
    )

    private fun crearFormacionProcedural(
        indice: Int,
        random: Random,
        anchoMapa: Double,
        altoMapa: Double
    ): FormacionRocosa {
        val ancho = random.nextDouble(460.0, 1320.0)
        val margen = ancho / 2.0 + 60.0
        val centroX = random.nextDouble(margen, anchoMapa - margen)
        val baseY = when (indice % 3) {
            0 -> random.nextDouble(altoMapa * 0.18, altoMapa * 0.45)
            1 -> random.nextDouble(altoMapa * 0.45, altoMapa * 0.78)
            else -> random.nextDouble(altoMapa * 0.78, altoMapa * 0.99)
        }.coerceIn(420.0, altoMapa - 8.0)
        val altura = min(random.nextDouble(240.0, 640.0), baseY - 150.0)
        return FormacionRocosa(
            id = "FORMACION-$indice",
            puntos = crearCrestas(random, centroX, ancho, baseY, altura, altoMapa),
            baseY = baseY
        )
    }

    private fun crearCrestas(
        random: Random,
        centroX: Double,
        ancho: Double,
        baseY: Double,
        altura: Double,
        altoMapa: Double
    ): List<PuntoCresta> {
        val cantidad = random.nextInt(8, 13)
        val izquierda = centroX - ancho / 2.0
        val faseA = random.nextDouble(0.0, PI * 2.0)
        val faseB = random.nextDouble(0.0, PI * 2.0)
        val picos = random.nextInt(2, 5)
        val puntos = mutableListOf<PuntoCresta>()

        for (indice in 0 until cantidad) {
            val avance = indice.toDouble() / (cantidad - 1)
            val x = izquierda + ancho * avance
            val envolvente = sin(PI * avance).coerceIn(0.0, 1.0)
            val perfil = (
                0.62 * envolvente +
                    0.23 * sin(picos * PI * avance + faseA).coerceIn(0.0, 1.0) +
                    0.15 * sin((picos + 2) * PI * avance + faseB)
                ).coerceIn(0.0, 1.0)
            val desvio = random.nextDouble(-0.05, 0.05)
            var y = baseY - altura * (0.12 + 0.88 * (perfil + desvio).coerceIn(0.0, 1.0))
            if (indice == 0 || indice == cantidad - 1) {
                y = baseY - altura * random.nextDouble(0.02, 0.12)
            }
            puntos.add(PuntoCresta(x, y.coerceIn(130.0, altoMapa - 25.0)))
        }
        return puntos
    }

    private fun crearSueloMarino(
        random: Random,
        anchoMapa: Double,
        altoMapa: Double
    ): FormacionRocosa {
        val muestras = 34
        val nivel = altoMapa * 0.962
        val puntos = (0..muestras).map { indice ->
            val avance = indice.toDouble() / muestras
            val x = anchoMapa * avance
            val ondulacion = sin(avance * PI * 7.0) * altoMapa * 0.006
            val desvio = random.nextDouble(-altoMapa * 0.005, altoMapa * 0.005)
            val y = (nivel + ondulacion + desvio)
                .coerceIn(altoMapa * 0.925, altoMapa - 4.0)
            PuntoCresta(x, y)
        }
        return FormacionRocosa("SUELO-MARINO", puntos, altoMapa)
    }

    companion object {
        private const val CANTIDAD_FORMACIONES = 54
    }
}
