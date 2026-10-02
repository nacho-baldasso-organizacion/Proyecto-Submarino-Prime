package submarino

/**
 * Punto de entrada principal del juego de submarinos.
 * Crea un submarino, simula movimiento e imprime el estado.
 */
fun main() {
    println("=== Juego de Submarinos 2D ===\n")

    // Crear un módulo de casco: presión máxima 500,000 Pa (aprox. 50m de profundidad), integridad al 100%
    val casco = ModuloCasco(presionMaximaPa = 500_000.0, integridad = 100.0)

    // Crear submarino en posición inicial (0, 0) con velocidad 10.0
    val submarino = Submarino(
        posX = 0.0,
        posY = 0.0,
        velocidad = 10.0,
        casco = casco
    )

    val presionInicial = CalculadoraHidrostatica.calcularPresion(submarino.posY)

    println("Estado inicial:")
    println("  Posición: (X: ${submarino.posX}, Y: ${submarino.posY})")
    println("  Batería: ${submarino.bateria}%")
    println("  Casco: ${submarino.casco}")
    println("  Destruido: ${submarino.estaDestruido}")
    println("  Presión hidrostática: ${String.format("%.2f", presionInicial)} Pa\n")

    // Simular movimiento: 30 unidades adelante (X) y 20 unidades hacia abajo (Y)
    println("--- Movimiento 1: Avanzar 30 unidades y descender 20 unidades ---")
    submarino.mover(deltaX = 30.0, deltaY = 20.0)

    println("\nEstado después del movimiento 1:")
    println("  Posición: (X: ${submarino.posX}, Y: ${submarino.posY})")
    println("  Batería: ${String.format("%.2f", submarino.bateria)}%")
    println("  Casco: ${submarino.casco}")
    println("  Destruido: ${submarino.estaDestruido}")
    println("  Presión hidrostática: ${String.format("%.2f", CalculadoraHidrostatica.calcularPresion(submarino.posY))} Pa\n")

    // Simular segundo movimiento: 15 unidades adelante y 10 unidades más hacia abajo
    println("--- Movimiento 2: Avanzar 15 unidades y descender 10 unidades ---")
    submarino.mover(deltaX = 15.0, deltaY = 10.0)

    println("\nEstado después del movimiento 2:")
    println("  Posición: (X: ${submarino.posX}, Y: ${submarino.posY})")
    println("  Batería: ${String.format("%.2f", submarino.bateria)}%")
    println("  Casco: ${submarino.casco}")
    println("  Destruido: ${submarino.estaDestruido}")
    println("  Presión hidrostática: ${String.format("%.2f", CalculadoraHidrostatica.calcularPresion(submarino.posY))} Pa\n")

    // Intentar subir a la superficie (Y negativo - debería quedarse en 0)
    println("--- Movimiento 3: Intentar subir 50 unidades (debería quedarse en superficie Y=0) ---")
    submarino.mover(deltaX = 10.0, deltaY = -50.0)

    println("\nEstado después del movimiento 3:")
    println("  Posición: (X: ${submarino.posX}, Y: ${submarino.posY})")
    println("  Batería: ${String.format("%.2f", submarino.bateria)}%")
    println("  Casco: ${submarino.casco}")
    println("  Destruido: ${submarino.estaDestruido}")
    println("  Presión hidrostática: ${String.format("%.2f", CalculadoraHidrostatica.calcularPresion(submarino.posY))} Pa\n")

    // Descender 100m para probar la integridad a gran profundidad
    println("--- Movimiento 4: Descender 100 metros ---")
    submarino.mover(deltaX = 0.0, deltaY = 100.0)
    println("  Posición: (X: ${submarino.posX}, Y: ${submarino.posY})")
    println("  Presión hidrostática: ${String.format("%.2f", CalculadoraHidrostatica.calcularPresion(submarino.posY))} Pa\n")

    // Verificar integridad usando la presión hidrostática real calculada por la profundidad actual
    println("--- Verificación de integridad a la profundidad actual (${submarino.posY}m) ---")
    submarino.verificarIntegridad(CalculadoraHidrostatica.calcularPresion(submarino.posY))

    println("\nEstado final:")
    println("  Posición: (X: ${submarino.posX}, Y: ${submarino.posY})")
    println("  Batería: ${String.format("%.2f", submarino.bateria)}%")
    println("  Casco: ${submarino.casco}")
    println("  Destruido: ${submarino.estaDestruido}")
    println("  Presión hidrostática: ${String.format("%.2f", CalculadoraHidrostatica.calcularPresion(submarino.posY))} Pa")

    println("\n=== Fin de la simulación ===")
}