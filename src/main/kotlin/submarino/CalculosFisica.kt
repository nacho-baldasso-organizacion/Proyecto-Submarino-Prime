package submarino

/**
 * Clase utilitaria para cálculos matemáticos y físicos compartidos (DRY).
 * Evita la duplicación de algoritmos de reducción proporcional y ratios de carga.
 */
object CalculosFisica {

    /**
     * Calcula la relación entre la carga actual y la capacidad máxima disponible.
     *
     * @return Ratio normalizado entre 0.0 y 1.0.
     */
    fun calcularRatioCarga(pesoTotal: Double, pesoBase: Double, capacidadCargaMaxima: Double): Double {
        if (capacidadCargaMaxima <= 0.0) return 0.0
        val pesoCarga = (pesoTotal - pesoBase).coerceAtLeast(0.0)
        return (pesoCarga / capacidadCargaMaxima).coerceIn(0.0, 1.0)
    }

    /**
     * Aplica un factor de escala cuantitativo sobre un valor nominal base.
     */
    fun aplicarFactorEscala(valorBase: Double, factor: Double): Double =
        maxOf(valorBase * factor.coerceAtLeast(0.0), 0.0)
}
