package submarino

import kotlin.math.abs

/**
 * Clasificación del nivel de oxígeno disponible a bordo.
 */
enum class EstadoOxigeno(val etiqueta: String) {
    ESTABLE("ESTABLE"),
    PRECAUCION("PRECAUCIÓN"),
    CRITICO("CRÍTICO"),
    AGOTADO("AGOTADO");

    /**
     * Indica si el nivel exige atención inmediata (crítico o agotado).
     */
    fun esCritico(): Boolean = this == CRITICO || this == AGOTADO

    /**
     * Indica si el nivel es estable y no requiere recarga.
     */
    fun esEstable(): Boolean = this == ESTABLE

    /**
     * Indica si conviene regresar a la estación para recargar oxígeno.
     */
    fun requiereRecarga(): Boolean = !esEstable()
}

/**
 * Instantánea inmutable del estado de navegación que consume la vista.
 */
data class EstadoNavegacion(
    val posicionX: Double,
    val posicionY: Double,
    val coordenadaX: Double,
    val profundidad: Double,
    val oxigeno: Double,
    val estadoOxigeno: EstadoOxigeno
) {

    /**
     * Proporción de oxígeno disponible en el rango [0.0, 1.0].
     */
    fun oxigenoNormalizado(maximo: Double = ModeloNavegacion.OXIGENO_MAXIMO): Double {
        if (maximo <= 0.0) {
            return 0.0
        }
        return (oxigeno / maximo).coerceIn(0.0, 1.0)
    }

    /**
     * Indica si el oxígeno se agotó por completo.
     */
    fun estaSinOxigeno(): Boolean = estadoOxigeno == EstadoOxigeno.AGOTADO

    /**
     * Indica si el nivel de oxígeno requiere atención inmediata.
     */
    fun estaEnRangoCritico(): Boolean = estadoOxigeno.esCritico()

    /**
     * Distancia horizontal entre la nave y otra coordenada del mapa.
     */
    fun distanciaHorizontalA(otraCoordenada: Double): Double = abs(coordenadaX - otraCoordenada)

    /**
     * Profundidad expresada en kilómetros.
     */
    fun profundidadEnKilometros(): Double = profundidad / 1000.0
}

/**
 * Modelo de navegación del submarino, independiente de JavaFX.
 *
 * Encapsula la posición, el consumo de oxígeno y los límites del área de juego.
 * La vista solo consume instantáneas inmutables mediante [estado].
 */
class ModeloNavegacion(
    private val anchoEscena: Double,
    private val altoEscena: Double,
    private val margenHorizontal: Double = MARGEN_HORIZONTAL_DEFAULT,
    private val margenVertical: Double = MARGEN_VERTICAL_DEFAULT,
    private val velocidadPixelesPorSegundo: Double = VELOCIDAD_DEFAULT,
    private val metrosPorPixel: Double = METROS_POR_PIXEL_DEFAULT,
    private val consumoOxigenoPorSegundo: Double = CONSUMO_BASE_DEFAULT,
    private val consumoOxigenoPorMovimiento: Double = CONSUMO_MOVIMIENTO_DEFAULT
) {

    private var posicionX: Double = anchoEscena / 2.0
    private var posicionY: Double = altoEscena / 2.0
    private val sistemaOxigeno: SistemaOxigeno = FabricaDominio.crearSistemaOxigeno(
        oxigenoInicial = OXIGENO_INICIAL,
        maximoInicial = OXIGENO_MAXIMO,
        consumoBasePorSegundo = consumoOxigenoPorSegundo,
        consumoMovimientoPorSegundo = consumoOxigenoPorMovimiento,
        maximoAbsoluto = OXIGENO_MAXIMO,
        minimo = OXIGENO_MINIMO
    )

    init {
        require(anchoEscena > 0.0) { "El ancho de la escena debe ser positivo: $anchoEscena" }
        require(altoEscena > 0.0) { "El alto de la escena debe ser positivo: $altoEscena" }
        require(margenHorizontal >= 0.0) { "El margen horizontal no puede ser negativo" }
        require(margenVertical >= 0.0) { "El margen vertical no puede ser negativo" }
    }

    /**
     * Actualiza la posición y el oxígeno para el intervalo indicado.
     *
     * @param deltaSegundos Tiempo transcurrido desde el último cuadro.
     * @param direccionX Componente horizontal normalizada en [-1.0, 1.0].
     * @param direccionY Componente vertical normalizada en [-1.0, 1.0].
     */
    fun actualizar(deltaSegundos: Double, direccionX: Double, direccionY: Double) {
        if (!deltaSegundos.isFinite() || deltaSegundos <= 0.0) {
            return
        }

        val dx = direccionX.coerceIn(-1.0, 1.0)
        val dy = direccionY.coerceIn(-1.0, 1.0)
        val desplazamiento = velocidadPixelesPorSegundo * deltaSegundos

        posicionX = (posicionX + dx * desplazamiento)
            .coerceIn(margenHorizontal, anchoEscena - margenHorizontal)
        posicionY = (posicionY + dy * desplazamiento)
            .coerceIn(margenVertical, altoEscena - margenVertical)

        val intensidadMovimiento = abs(dx) + abs(dy)
        sistemaOxigeno.consumir(deltaSegundos, intensidadMovimiento)
    }

    /**
     * Genera una instantánea inmutable del estado actual.
     */
    fun estado(): EstadoNavegacion = FabricaDominio.crearEstadoNavegacion(
        posicionX = posicionX,
        posicionY = posicionY,
        coordenadaX = posicionX * metrosPorPixel,
        profundidad = posicionY * metrosPorPixel,
        oxigeno = sistemaOxigeno.oxigeno,
        estadoOxigeno = clasificarOxigeno(sistemaOxigeno.oxigeno)
    )

    private fun clasificarOxigeno(nivel: Double): EstadoOxigeno = if (nivel <= OXIGENO_MINIMO) {
        EstadoOxigeno.AGOTADO
    } else if (nivel < UMBRAL_CRITICO) {
        EstadoOxigeno.CRITICO
    } else if (nivel < UMBRAL_PRECAUCION) {
        EstadoOxigeno.PRECAUCION
    } else {
        EstadoOxigeno.ESTABLE
    }

    companion object {
        const val OXIGENO_INICIAL = 100.0
        const val OXIGENO_MINIMO = 0.0
        const val OXIGENO_MAXIMO = 100.0

        private const val MARGEN_HORIZONTAL_DEFAULT = 70.0
        private const val MARGEN_VERTICAL_DEFAULT = 60.0
        private const val VELOCIDAD_DEFAULT = 260.0
        private const val METROS_POR_PIXEL_DEFAULT = 0.1
        private const val CONSUMO_BASE_DEFAULT = 0.8
        private const val CONSUMO_MOVIMIENTO_DEFAULT = 0.4
        private const val UMBRAL_CRITICO = 25.0
        private const val UMBRAL_PRECAUCION = 55.0
    }
}
