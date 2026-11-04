package submarino

import kotlin.math.min

data class PuntoCresta(
    val x: Double,
    val y: Double
)

class FormacionRocosa(
    val id: String,
    private val puntos: List<PuntoCresta>,
    val baseY: Double
) {

    init {
        require(puntos.size >= 2) { "Una formaci��n rocosa necesita al menos dos puntos de cresta" }
        require(baseY > puntos.maxOf { it.y }) { "La base debe quedar por debajo de la cresta" }
    }

    val crestas: List<PuntoCresta> = puntos.toList()
    val xMin: Double = puntos.first().x
    val xMax: Double = puntos.last().x
    val profundidadMinima: Double = puntos.minOf { it.y }
    val profundidadMaxima: Double = baseY

    fun superficieEn(x: Double): Double? {
        if (x < xMin || x > xMax) {
            return null
        }
        for (indice in 0 until puntos.size - 1) {
            val inicio = puntos[indice]
            val fin = puntos[indice + 1]
            if (x >= inicio.x && x <= fin.x) {
                val ancho = fin.x - inicio.x
                if (ancho <= 0.0) {
                    return min(inicio.y, fin.y)
                }
                val avance = (x - inicio.x) / ancho
                return inicio.y + (fin.y - inicio.y) * avance
            }
        }
        return puntos.last().y
    }
}

class TerrenoSubmarino internal constructor(
    val formaciones: List<FormacionRocosa>,
    val sueloMarino: FormacionRocosa
) {

    val todas: List<FormacionRocosa> = formaciones + sueloMarino

    companion object {

        /**
         * Construye el terreno delegando la generaci��n procedural en
         * [GeneradorTerrenoSubmarino] (SRP).
         */
        fun crear(semilla: Long, anchoMapa: Double, altoMapa: Double): TerrenoSubmarino =
            GeneradorTerrenoSubmarino().crear(semilla, anchoMapa, altoMapa)
    }
}
