package submarino

/**
 * Fábrica de datos de prueba reutilizable para mantener los tests concisos.
 * Centraliza la construcción de naves, bodegas, minerales y servicios con
 * configuraciones silenciosas y valores por defecto equivalentes al dominio.
 */
object FabricaDatosPrueba {

    fun crearSubmarino(
        posX: Double = 0.0,
        posY: Double = 0.0,
        velocidad: Double = 10.0,
        casco: Casco = ModuloCasco(500_000.0),
        bodega: Almacenamiento? = Bodega(Submarino.BODEGA_CAPACIDAD_DEFAULT),
        billetera: Billetera = Monedero(0),
        pesoBase: Double = Submarino.PESO_BASE_DEFAULT,
        velocidadAscensoBase: Double =
            if (velocidad > 0.0) velocidad else Submarino.VELOCIDAD_ASCENSO_DEFAULT,
        estrategiaAscenso: EstrategiaAscenso = EstrategiaAscensoLineal(),
        calculadorPeso: CalculadorPeso = CalculadorPesoSubmarino()
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
        notificador = NotificadorSilencioso
    )

    fun crearBodega(capacidadMaxima: Double = Submarino.BODEGA_CAPACIDAD_DEFAULT): Bodega =
        Bodega(capacidadMaxima)

    fun cargarMinerales(
        bodega: Almacenamiento,
        cantidad: Int,
        tipo: TipoMineral = TipoMineral.TITANIO_CRISTALINO,
        desde: Int = 0
    ): List<Mineral> = (desde until desde + cantidad).map { indice ->
        Mineral("M$indice", tipo, 0, indice).also { bodega.agregar(it) }
    }

    fun crearServicioComercio(): ServicioComercio = ServicioComercio()

    fun crearServicioUpgrades(vararg mejoras: Mejora): ServicioUpgrades =
        ServicioUpgrades(CatalogoUpgrades(mejoras.toList()))
}
