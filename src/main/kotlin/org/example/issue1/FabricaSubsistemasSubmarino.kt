package submarino

/**
 * Abstracción de construcción de los subsistemas de [Submarino] (DIP).
 *
 * [Submarino] deja de instanciar colaboradores concretos y depende de esta
 * fábrica. La implementación concreta vive en el composition root
 * [FabricaSubsistemasEstandar], separada de la lógica de dominio.
 */
interface FabricaSubsistemasSubmarino {
    fun crearBodega(capacidadMaxima: Double): Almacenamiento
    fun crearCalculadorPeso(): CalculadorPeso
    fun crearEstrategiaAscenso(): EstrategiaAscenso
    fun crearBilletera(saldoInicial: Int): Billetera
    fun crearEnergia(bateriaInicial: Double, consumoPorMetro: Double): SistemaEnergia
    fun crearAcople(): ControladorAcople
    fun crearPropulsion(velocidadAscensoBase: Double): SistemaPropulsion
    fun crearSistemaCasco(casco: Casco): SistemaCasco
    fun crearGestorAscenso(
        calculadorPeso: CalculadorPeso,
        estrategiaAscenso: EstrategiaAscenso
    ): GestorAscenso
    fun crearGestorBodega(): GestorBodega
    fun crearGestorVelocidad(
        velocidadActual: () -> Double,
        actualizarVelocidad: (Double) -> Unit
    ): GestorVelocidadNavegacion
    fun crearMovimiento(
        energia: SistemaEnergia,
        acople: ControladorAcople,
        notificador: Notificador,
        estaDestruido: () -> Boolean,
        posicionActual: () -> Pair<Double, Double>,
        actualizarPosicion: (Double, Double) -> Unit
    ): GestorMovimiento
    fun crearNavegador(
        movimiento: GestorMovimiento,
        gestorAscenso: GestorAscenso,
        posicionActual: () -> Pair<Double, Double>,
        velocidadAscensoActual: () -> Double,
        pesoBaseActual: () -> Double,
        bodegaActual: () -> Almacenamiento?
    ): NavegadorSubmarino
    fun crearGestorMejoras(
        propulsion: SistemaPropulsion,
        sistemaCasco: SistemaCasco,
        gestorBodega: GestorBodega,
        gestorVelocidad: GestorVelocidadNavegacion,
        bodegaActual: () -> Almacenamiento?
    ): GestorMejorasSubmarino
    fun crearFormateadorEstado(): FormateadorEstadoSubmarino
    fun crearEstadoSubmarino(
        posX: Double,
        posY: Double,
        pesoTotal: Double,
        velocidadAscenso: Double,
        saldo: Int,
        estaAcoplado: Boolean,
        bateria: Double,
        estaDestruido: Boolean,
        casco: Casco
    ): EstadoSubmarino
}

/**
 * Composition root por defecto. Es el único punto que conoce las clases
 * concretas de los subsistemas.
 */
object FabricaSubsistemasEstandar : FabricaSubsistemasSubmarino {
    override fun crearBodega(capacidadMaxima: Double): Almacenamiento = Bodega(capacidadMaxima)
    override fun crearCalculadorPeso(): CalculadorPeso = CalculadorPesoSubmarino()
    override fun crearEstrategiaAscenso(): EstrategiaAscenso = EstrategiaAscensoLineal()
    override fun crearBilletera(saldoInicial: Int): Billetera = Monedero(saldoInicial)
    override fun crearEnergia(bateriaInicial: Double, consumoPorMetro: Double): SistemaEnergia =
        SistemaEnergia(bateriaInicial, consumoPorMetro)
    override fun crearAcople(): ControladorAcople = ControladorAcople()
    override fun crearPropulsion(velocidadAscensoBase: Double): SistemaPropulsion =
        SistemaPropulsion(velocidadAscensoBase)
    override fun crearSistemaCasco(casco: Casco): SistemaCasco = SistemaCasco(casco)
    override fun crearGestorAscenso(
        calculadorPeso: CalculadorPeso,
        estrategiaAscenso: EstrategiaAscenso
    ): GestorAscenso = GestorAscenso(calculadorPeso, estrategiaAscenso)
    override fun crearGestorBodega(): GestorBodega = GestorBodega()
    override fun crearGestorVelocidad(
        velocidadActual: () -> Double,
        actualizarVelocidad: (Double) -> Unit
    ): GestorVelocidadNavegacion = GestorVelocidadNavegacion(velocidadActual, actualizarVelocidad)
    override fun crearMovimiento(
        energia: SistemaEnergia,
        acople: ControladorAcople,
        notificador: Notificador,
        estaDestruido: () -> Boolean,
        posicionActual: () -> Pair<Double, Double>,
        actualizarPosicion: (Double, Double) -> Unit
    ): GestorMovimiento = GestorMovimiento(
        energia = energia,
        acople = acople,
        notificador = notificador,
        estaDestruido = estaDestruido,
        posicionActual = posicionActual,
        actualizarPosicion = actualizarPosicion
    )
    override fun crearNavegador(
        movimiento: GestorMovimiento,
        gestorAscenso: GestorAscenso,
        posicionActual: () -> Pair<Double, Double>,
        velocidadAscensoActual: () -> Double,
        pesoBaseActual: () -> Double,
        bodegaActual: () -> Almacenamiento?
    ): NavegadorSubmarino = NavegadorSubmarino(
        movimiento = movimiento,
        gestorAscenso = gestorAscenso,
        posicionActual = posicionActual,
        velocidadAscensoActual = velocidadAscensoActual,
        pesoBaseActual = pesoBaseActual,
        bodegaActual = bodegaActual
    )
    override fun crearGestorMejoras(
        propulsion: SistemaPropulsion,
        sistemaCasco: SistemaCasco,
        gestorBodega: GestorBodega,
        gestorVelocidad: GestorVelocidadNavegacion,
        bodegaActual: () -> Almacenamiento?
    ): GestorMejorasSubmarino = GestorMejorasSubmarino(
        propulsion = propulsion,
        sistemaCasco = sistemaCasco,
        gestorBodega = gestorBodega,
        gestorVelocidad = gestorVelocidad,
        bodegaActual = bodegaActual
    )
    override fun crearFormateadorEstado(): FormateadorEstadoSubmarino = FormateadorEstadoSubmarino()
    override fun crearEstadoSubmarino(
        posX: Double,
        posY: Double,
        pesoTotal: Double,
        velocidadAscenso: Double,
        saldo: Int,
        estaAcoplado: Boolean,
        bateria: Double,
        estaDestruido: Boolean,
        casco: Casco
    ): EstadoSubmarino = EstadoSubmarino(
        posX = posX,
        posY = posY,
        pesoTotal = pesoTotal,
        velocidadAscenso = velocidadAscenso,
        saldo = saldo,
        estaAcoplado = estaAcoplado,
        bateria = bateria,
        estaDestruido = estaDestruido,
        casco = casco
    )
}

/**
 * Abstracción para construir instantáneas de [EstadoAscenso].
 */
interface FabricaEstadoAscenso {
    fun crear(
        pesoBase: Double,
        pesoCarga: Double,
        pesoTotal: Double,
        capacidadMaximaBodega: Double,
        factorVelocidad: Double,
        velocidadNominal: Double,
        velocidadAscensoEfectiva: Double
    ): EstadoAscenso
}

object FabricaEstadoAscensoEstandar : FabricaEstadoAscenso {
    override fun crear(
        pesoBase: Double,
        pesoCarga: Double,
        pesoTotal: Double,
        capacidadMaximaBodega: Double,
        factorVelocidad: Double,
        velocidadNominal: Double,
        velocidadAscensoEfectiva: Double
    ): EstadoAscenso = EstadoAscenso(
        pesoBase = pesoBase,
        pesoCarga = pesoCarga,
        pesoTotal = pesoTotal,
        capacidadMaximaBodega = capacidadMaximaBodega,
        factorVelocidad = factorVelocidad,
        velocidadNominal = velocidadNominal,
        velocidadAscensoEfectiva = velocidadAscensoEfectiva
    )
}
