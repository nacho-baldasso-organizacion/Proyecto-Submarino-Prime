package submarino

/**
 * Parámetros de configuración de una partida: dimensiones del mapa, estado
 * inicial de la nave y consumos base.
 *
 * Responsabilidad única (SRP): concentrar los valores de configuración que no
 * pertenecen a ningún colaborador concreto.
 */
class ConfiguracionSesion {

    companion object {
        const val ANCHO_MAPA = 5000.0
        const val ALTO_MAPA = 3000.0
        const val ESTACION_X = 2500.0
        const val ESTACION_ANCHO = 400.0
        const val PROFUNDIDAD_ZONA_ACOPLE = 120.0
        const val RADIO_INTERACCION = 65.0

        const val SEMILLA_DEFAULT = 2026L
        const val INTEGRIDAD_MINIMA = 0.0
        const val PROFUNDIDAD_INICIAL = 220.0
        const val VELOCIDAD_INICIAL = 240.0
        const val VELOCIDAD_MINIMA = 80.0
        const val VELOCIDAD_ASCENSO_INICIAL = 180.0
        const val PESO_BASE = 1000.0
        const val CAPACIDAD_INICIAL_BODEGA = 10.0
        const val SALDO_INICIAL = 0
        const val PRESION_MAXIMA_INICIAL = 20_000_000.0
        const val CONSUMO_OXIGENO_BASE = 1.0
        const val CONSUMO_OXIGENO_MOVIMIENTO = 0.8
        const val OXIGENO_MAXIMO_INICIAL = 100.0
        const val OXIGENO_MAXIMO_ABSOLUTO = 200.0
        const val OXIGENO_MINIMO = 0.0

        /**
         * Descripción legible de las dimensiones del mapa configurado.
         */
        fun descripcionMapa(): String = "Mapa ${ANCHO_MAPA.toInt()} x ${ALTO_MAPA.toInt()}"
    }
}
