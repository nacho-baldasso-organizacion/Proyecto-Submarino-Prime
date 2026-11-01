package submarino

/**
 * Fábrica compartida de submarinos para las pruebas de la Issue #6.
 */
object SubmarinoPruebaFactory {

    fun crear(
        bodega: Almacenamiento? = null,
        pesoBase: Double = 1000.0,
        velocidad: Double = 10.0,
        estrategiaAscenso: EstrategiaAscenso = EstrategiaAscensoLineal(0.5),
        posX: Double = 0.0,
        posY: Double = 0.0
    ): Submarino = Submarino(
        posX = posX,
        posY = posY,
        velocidad = velocidad,
        casco = ModuloCasco(500_000.0),
        pesoBase = pesoBase,
        bodega = bodega,
        estrategiaAscenso = estrategiaAscenso,
        notificador = NotificadorSilencioso
    )
}
