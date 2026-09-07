package submarino

/**
 * Prueba de integración: Issue 1 + Issue 2
 * ------------------------------------------------------------------
 * Issue 1: EntidadFisica, ModuloCasco y Submarino (movimiento + integridad).
 * Issue 2: CalculadoraHidrostatica (presión según profundidad y profundidad máxima).
 *
 * Esta prueba une ambas issues: el submarino desciende y, en cada profundidad,
 * se calcula la presión hidrostática real (Issue 2), que se pasa a
 * verificarIntegridad() (Issue 1) para ver el estado del casco.
 */
fun main() {
    println("=== PRUEBA CONJUNTA: Issue 1 + Issue 2 ===\n")

    // ---------- Issue 2: constantes físicas ----------
    println("--- Issue 2: constantes de la física hidrostática ---")
    println("  DENSIDAD_AGUA_MAR      = ${CalculadoraHidrostatica.DENSIDAD_AGUA_MAR} kg/m³")
    println("  GRAVEDAD               = ${CalculadoraHidrostatica.GRAVEDAD} m/s²")
    println("  PRESION_ATMOSFERICA    = ${CalculadoraHidrostatica.PRESION_ATMOSFERICA} Pa")
    println("  Presión en superficie  = ${CalculadoraHidrostatica.calcularPresion(0.0)} Pa\n")

    // ---------- Issue 1 + Issue 2: descenso y presión ----------
    val casco = ModuloCasco(presionMaximaPa = 500_000.0, integridad = 100.0)
    val submarino = Submarino(posX = 0.0, posY = 0.0, velocidad = 10.0, casco = casco)

    val profundidadMaxima = CalculadoraHidrostatica.calcularProfundidadMaxima(casco.presionMaximaPa)
    println("--- Integración: descenso con presión hidrostática real ---")
    println("  Casco soporta ${casco.presionMaximaPa} Pa -> profundidad máxima ≈ ${"%.2f".format(profundidadMaxima)} m\n")

    for ((i, deltaY) in doubleArrayOf(20.0, 20.0, 60.0).withIndex()) {
        submarino.mover(deltaX = 0.0, deltaY = deltaY)
        val presion = CalculadoraHidrostatica.calcularPresion(submarino.posY)
        submarino.verificarIntegridad(presion)
        println("  Mov ${i + 1}: profundidad = ${submarino.posY} m | presión = ${"%.0f".format(presion)} Pa | " +
                "integridad = ${"%.2f".format(casco.integridad)}% | destruido = ${submarino.estaDestruido}")
    }

    // ---------- Issue 1 + Issue 2: destrucción ----------
    println("\n--- Integración: superando la profundidad máxima hasta colapsar ---")
    val cascoDebil = ModuloCasco(presionMaximaPa = 150_000.0, integridad = 2.0)
    val subDebil = Submarino(posX = 0.0, posY = 0.0, velocidad = 10.0, casco = cascoDebil)
    println("  Casco débil: soporta ${cascoDebil.presionMaximaPa} Pa -> profundidad máxima ≈ " +
            "${"%.2f".format(CalculadoraHidrostatica.calcularProfundidadMaxima(cascoDebil.presionMaximaPa))} m")

    for ((i, deltaY) in doubleArrayOf(20.0, 10.0).withIndex()) {
        subDebil.mover(deltaX = 0.0, deltaY = deltaY)
        val presion = CalculadoraHidrostatica.calcularPresion(subDebil.posY)
        subDebil.verificarIntegridad(presion)
        println("  Mov ${i + 1}: profundidad = ${subDebil.posY} m | presión = ${"%.0f".format(presion)} Pa | " +
                "integridad = ${"%.2f".format(cascoDebil.integridad)}% | destruido = ${subDebil.estaDestruido}")
    }

    println("\n=== Fin de la prueba conjunta ===")
}
