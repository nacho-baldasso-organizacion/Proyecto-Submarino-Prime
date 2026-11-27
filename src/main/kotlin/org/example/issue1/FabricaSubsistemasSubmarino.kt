package submarino

/**
 * Colaborador que encapsula la creación de objetos de propulsión y casco.
 */
private class CreadorPropulsionYCasco {
    fun crearPropulsion(velocidadAscensoBase: Double): SistemaPropulsion = SistemaPropulsion(velocidadAscensoBase)
    fun crearSistemaCasco(casco: Casco): SistemaCasco = SistemaCasco(casco)
}

/**
 * Colaborador que encapsula la creación de objetos de navegación y mejoras.
 */
private class CreadorNavegacionYMejoras {
    fun crearGestorVelocidad(
        velocidadActual: () -> Double,
        actualizarVelocidad: (Double) -> Unit
    ): GestorVelocidadNavegacion = GestorVelocidadNavegacion(velocidadActual, actualizarVelocidad)

    fun crearNavegador(
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

    fun crearGestorMejoras(
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
}

/**
 * Colaborador que encapsula la creación de objetos de estado y configuración del submarino.
 */
private class CreadorEstadoSubmarino {
    fun crearBodega(capacidadMaxima: Double): Almacenamiento = Bodega(capacidadMaxima)
    fun crearCalculadorPeso(): CalculadorPeso = CalculadorPesoSubmarino()
    fun crearEstrategiaAscenso(): EstrategiaAscenso = EstrategiaAscensoLineal()
    fun crearBilletera(saldoInicial: Int): Billetera = Monedero(saldoInicial)
    fun crearEnergia(bateriaInicial: Double, consumoPorMetro: Double): SistemaEnergia = SistemaEnergia(bateriaInicial, consumoPorMetro)
    fun crearAcople(): ControladorAcople = ControladorAcople()
    fun crearGestorAscenso(calculadorPeso: CalculadorPeso, estrategiaAscenso: EstrategiaAscenso): GestorAscenso = GestorAscenso(calculadorPeso, estrategiaAscenso)
    fun crearGestorBodega(): GestorBodega = GestorBodega()
    fun crearMovimiento(
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
    fun crearFormateadorEstado(): FormateadorEstadoSubmarino = FormateadorEstadoSubmarino()
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
 * Composition root por defecto. Delegará en los colaboradores para mantener CBO bajo.
 */
object FabricaSubsistemasEstandar : FabricaSubsistemasSubmarino {
    private val creadorPropulsionYCasco = CreadorPropulsionYCasco()
    private val creadorNavegacionYMejoras = CreadorNavegacionYMejoras()
    private val creadorEstadoSubmarino = CreadorEstadoSubmarino()

    override fun crearBodega(capacidadMaxima: Double): Almacenamiento = creadorEstadoSubmarino.crearBodega(capacidadMaxima)
    override fun crearCalculadorPeso(): CalculadorPeso = creadorEstadoSubmarino.crearCalculadorPeso()
    override fun crearEstrategiaAscenso(): EstrategiaAscenso = creadorEstadoSubmarino.crearEstrategiaAscenso()
    override fun crearBilletera(saldoInicial: Int): Billetera = creadorEstadoSubmarino.crearBilletera(saldoInicial)
    override fun crearEnergia(bateriaInicial: Double, consumoPorMetro: Double): SistemaEnergia = creadorEstadoSubmarino.crearEnergia(bateriaInicial, consumoPorMetro)
    override fun crearAcople(): ControladorAcople = creadorEstadoSubmarino.crearAcople()
    override fun crearPropulsion(velocidadAscensoBase: Double): SistemaPropulsion = creadorPropulsionYCasco.crearPropulsion(velocidadAscensoBase)
    override fun crearSistemaCasco(casco: Casco): SistemaCasco = creadorPropulsionYCasco.crearSistemaCasco(casco)
    override fun crearGestorAscenso(
        calculadorPeso: CalculadorPeso,
        estrategiaAscenso: EstrategiaAscenso
    ): GestorAscenso = creadorEstadoSubmarino.crearGestorAscenso(calculadorPeso, estrategiaAscenso)
    override fun crearGestorBodega(): GestorBodega = creadorEstadoSubmarino.crearGestorBodega()
    override fun crearGestorVelocidad(
        velocidadActual: () -> Double,
        actualizarVelocidad: (Double) -> Unit
    ): GestorVelocidadNavegacion = creadorNavegacionYMejoras.crearGestorVelocidad(velocidadActual, actualizarVelocidad)
    override fun crearMovimiento(
        energia: SistemaEnergia,
        acople: ControladorAcople,
        notificador: Notificador,
        estaDestruido: () -> Boolean,
        posicionActual: () -> Pair<Double, Double>,
        actualizarPosicion: (Double, Double) -> Unit
    ): GestorMovimiento = creadorEstadoSubmarino.crearMovimiento(energia, acople, notificador, estaDestruido, posicionActual, actualizarPosicion)
    override fun crearNavegador(
        movimiento: GestorMovimiento,
        gestorAscenso: GestorAscenso,
        posicionActual: () -> Pair<Double, Double>,
        velocidadAscensoActual: () -> Double,
        pesoBaseActual: () -> Double,
        bodegaActual: () -> Almacenamiento?
    ): NavegadorSubmarino = creadorNavegacionYMejoras.crearNavegador(movimiento, gestorAscenso, posicionActual, velocidadAscensoActual, pesoBaseActual, bodegaActual)
    override fun crearGestorMejoras(
        propulsion: SistemaPropulsion,
        sistemaCasco: SistemaCasco,
        gestorBodega: GestorBodega,
        gestorVelocidad: GestorVelocidadNavegacion,
        bodegaActual: () -> Almacenamiento?
    ): GestorMejorasSubmarino = creadorNavegacionYMejoras.crearGestorMejoras(propulsion, sistemaCasco, gestorBodega, gestorVelocidad, bodegaActual)
    override fun crearFormateadorEstado(): FormateadorEstadoSubmarino = creadorEstadoSubmarino.crearFormateadorEstado()
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
    ): EstadoSubmarino = creadorEstadoSubmarino.crearEstadoSubmarino(posX, posY, pesoTotal, velocidadAscenso, saldo, estaAcoplado, bateria, estaDestruido, casco)
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