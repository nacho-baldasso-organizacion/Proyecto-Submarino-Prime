package submarino

object FabricaSubmarinoDelegate {

    fun crearSubmarino(
        posX: Double = 0.0,
        posY: Double = 0.0,
        velocidad: Double = 0.0,
        casco: Casco,
        pesoBase: Double = Submarino.PESO_BASE_DEFAULT,
        bodega: Almacenamiento? = null,
        velocidadAscensoBase: Double =
            if (velocidad > 0.0) velocidad else Submarino.VELOCIDAD_ASCENSO_DEFAULT,
        calculadorPeso: CalculadorPeso? = null,
        estrategiaAscenso: EstrategiaAscenso? = null,
        billetera: Billetera? = null,
        notificador: Notificador = NotificadorConsola,
        factoria: FabricaSubsistemasSubmarino = FabricaSubsistemasEstandar
    ): Submarino = Submarino(
        posX = posX,
        posY = posY,
        velocidad = velocidad,
        casco = casco,
        pesoBase = pesoBase,
        bodega = bodega,
        velocidadAscensoBase = velocidadAscensoBase,
        calculadorPeso = calculadorPeso,
        estrategiaAscenso = estrategiaAscenso,
        billetera = billetera,
        notificador = notificador,
        factoria = factoria
    )

    fun crearCascoReforzado(
        presionMaximaPa: Double,
        integridad: Double = 100.0,
        factorAbsorcion: Double = 0.5,
        notificador: Notificador = NotificadorConsola
    ): CascoReforzado = CascoReforzado(
        presionMaximaPa = presionMaximaPa,
        integridad = integridad,
        factorAbsorcion = factorAbsorcion,
        notificador = notificador
    )

    fun crearModuloCasco(
        presionMaximaPa: Double,
        integridad: Double = 100.0,
        notificador: Notificador = NotificadorConsola
    ): ModuloCasco = ModuloCasco(
        presionMaximaPa = presionMaximaPa,
        integridad = integridad,
        notificador = notificador
    )

    fun crearBodega(capacidadMaxima: Double): Bodega = Bodega(capacidadMaxima = capacidadMaxima)

    fun crearGestorDanioSubmarino(casco: Casco): GestorDanioSubmarino =
        GestorDanioSubmarino(casco = casco)

    fun crearZonaSuperficie(profundidadMaxima: Double = 0.0): ZonaSuperficie =
        ZonaSuperficie(profundidadMaxima = profundidadMaxima)

    fun crearEstacionAcople(
        zonaAcople: ZonaAcople = crearZonaSuperficie()
    ): EstacionAcople = EstacionAcople(zonaAcople = zonaAcople)
}