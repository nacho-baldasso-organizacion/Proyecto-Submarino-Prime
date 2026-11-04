package submarino

/**
 * Subsistema especializado en el nivel, consumo y capacidad m��xima de ox��geno (SRP).
 *
 * A��sla la econom��a de ox��geno de la navegaci��n y del estado de la partida: los
 * clientes solo le indican el tiempo transcurrido y la intensidad del movimiento.
 */
class SistemaOxigeno(
    oxigenoInicial: Double,
    maximoInicial: Double = oxigenoInicial,
    private val consumoBasePorSegundo: Double,
    private val consumoMovimientoPorSegundo: Double,
    private val maximoAbsoluto: Double = Double.MAX_VALUE,
    private val minimo: Double = 0.0
) {

    init {
        require(maximoInicial >= minimo) { "El ox��geno m��ximo no puede ser negativo: $maximoInicial" }
        require(maximoAbsoluto >= maximoInicial) {
            "El m��ximo absoluto ($maximoAbsoluto) no puede ser menor que el inicial ($maximoInicial)"
        }
        require(consumoBasePorSegundo >= 0.0) { "El consumo base no puede ser negativo" }
        require(consumoMovimientoPorSegundo >= 0.0) { "El consumo por movimiento no puede ser negativo" }
    }

    var oxigeno: Double = oxigenoInicial.coerceIn(minimo, maximoInicial)
        private set

    var maximo: Double = maximoInicial
        private set

    val estaAgotado: Boolean
        get() = oxigeno <= minimo

    /**
     * Consume ox��geno segǧn el tiempo transcurrido y la intensidad del movimiento.
     */
    fun consumir(deltaSegundos: Double, intensidadMovimiento: Double): Double {
        if (!deltaSegundos.isFinite() || deltaSegundos <= 0.0) {
            return oxigeno
        }
        val intensidad = intensidadMovimiento.coerceAtLeast(0.0)
        val consumo = (
            consumoBasePorSegundo +
                consumoMovimientoPorSegundo * intensidad
            ) * deltaSegundos
        oxigeno = (oxigeno - consumo).coerceIn(minimo, maximo)
        return oxigeno
    }

    /**
     * Fija el nivel actual de ox��geno respetando los l��mites configurados.
     */
    fun establecer(valor: Double) {
        oxigeno = valor.coerceIn(minimo, maximo)
    }

    /**
     * Ampl��a la capacidad m��xima de ox��geno sin superar el tope absoluto.
     */
    fun aumentarMaximo(incremento: Double) {
        if (incremento <= 0.0) {
            return
        }
        maximo = (maximo + incremento).coerceAtMost(maximoAbsoluto)
        oxigeno = oxigeno.coerceAtMost(maximo)
    }

    /**
     * Restaura el ox��geno al m��ximo disponible.
     */
    fun recargar() {
        oxigeno = maximo
    }
}
