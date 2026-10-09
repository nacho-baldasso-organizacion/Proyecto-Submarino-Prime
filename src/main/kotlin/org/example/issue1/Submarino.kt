package submarino

/**
 * Clase principal que representa al submarino.
 * Hereda de [EntidadFisica] y compone un [Casco] mediante interfaz (DIP, OCP).
 * Implementa [Navegable] cumpliendo con la Segregación de Interfaces (ISP).
 * Gestiona batería, desplazamiento y delega el estado estructural al casco.
 */
class Submarino(
    posX: Double = 0.0,
    posY: Double = 0.0,
    velocidad: Double = 0.0,
    val casco: Casco,
    val pesoBase: Double = PESO_BASE_DEFAULT,
    val bodega: Almacenamiento? = Bodega(capacidadMaxima = BODEGA_CAPACIDAD_DEFAULT),
    val velocidadAscensoBase: Double = if (velocidad > 0.0) velocidad else VELOCIDAD_ASCENSO_DEFAULT,
    val calculadorPeso: CalculadorPeso = CalculadorPesoSubmarino(),
    val estrategiaAscenso: EstrategiaAscenso = EstrategiaAscensoLineal(),
    private val notificador: Notificador = NotificadorConsola
) : EntidadFisica(posX, posY, velocidad), Navegable, Pesable {

    companion object {
        const val PESO_BASE_DEFAULT: Double = 1000.0
        const val BODEGA_CAPACIDAD_DEFAULT: Double = 50.0
        const val VELOCIDAD_ASCENSO_DEFAULT: Double = 5.0
    }

    init {
        require(pesoBase >= 0.0) { "El peso base no puede ser negativo: $pesoBase" }
        require(velocidadAscensoBase >= 0.0) { "La velocidad de ascenso base no puede ser negativa: $velocidadAscensoBase" }
    }

    var bateria: Double = 100.0
        private set

    /**
     * Delega el estado de destrucción al [Casco].
     */
    val estaDestruido: Boolean
        get() = casco.estaDestruido

    /**
     * Retorna el peso/masa total actual del submarino delegando en [CalculadorPeso] (SRP, DIP).
     * Suma el [pesoBase] constante más el peso total acumulado de todos los minerales guardados en la [bodega].
     */
    val pesoTotal: Double
        get() = calculadorPeso.calcularPesoTotal(pesoBase, bodega)

    /**
     * Implementación del contrato [Pesable] (ISP) que reporta la masa total del submarino.
     */
    override val peso: Double
        get() = pesoTotal

    /**
     * Retorna la velocidad dinámica de ascenso actual calculada por la [estrategiaAscenso] (OCP).
     * A mayor peso cargado en la bodega, menor es esta velocidad de ascenso.
     */
    val velocidadAscenso: Double
        get() = estrategiaAscenso.calcularVelocidadAscenso(
            velocidadBase = velocidadAscensoBase,
            pesoTotal = pesoTotal,
            pesoBase = pesoBase,
            capacidadCargaMaxima = bodega?.capacidadMaxima ?: 0.0
        )

    /**
     * Retorna el factor multiplicador de rendimiento vertical dictado por la masa total (entre 0.0 y 1.0).
     */
    val factorAscenso: Double
        get() = estrategiaAscenso.calcularFactorVelocidad(
            pesoTotal = pesoTotal,
            pesoBase = pesoBase,
            capacidadCargaMaxima = bodega?.capacidadMaxima ?: 0.0
        )

    /**
     * Consulta semántica del peso total de la nave.
     */
    fun calcularPesoTotal(): Double = pesoTotal

    /**
     * Consulta semántica de la velocidad dinámica de ascenso de la nave.
     */
    fun calcularVelocidadAscenso(): Double = velocidadAscenso

    /**
     * Obtiene una instantánea inmutable con todos los parámetros de peso y dinámica de ascenso.
     */
    fun obtenerEstadoAscenso(): EstadoAscenso {
        val pesoCarga = bodega?.pesoActual ?: 0.0
        val capMax = bodega?.capacidadMaxima ?: 0.0
        return EstadoAscenso(
            pesoBase = pesoBase,
            pesoCarga = pesoCarga,
            pesoTotal = pesoTotal,
            capacidadMaximaBodega = capMax,
            factorVelocidad = factorAscenso,
            velocidadNominal = velocidadAscensoBase,
            velocidadAscensoEfectiva = velocidadAscenso
        )
    }

    /**
     * Mueve el submarino en los ejes X e Y.
     * Impide que posY sea menor a 0.0.
     * Consume 0.5% de batería por cada unidad de distancia recorrida.
     *
     * @param deltaX Desplazamiento en el eje X (navegación horizontal / propulsión)
     * @param deltaY Desplazamiento en el eje Y (inmersión hacia abajo o ascenso hacia la superficie)
     * @return true si el desplazamiento se efectuó correctamente; false si la nave está destruida o sin batería.
     */
    override fun mover(deltaX: Double, deltaY: Double): Boolean {
        if (estaDestruido) {
            notificador.notificar("El submarino está destruido y no puede moverse.")
            return false
        }

        if (bateria <= 0.0) {
            notificador.notificar("Batería agotada. El submarino no puede moverse.")
            return false
        }

        // Distancia euclidiana usando Math.hypot para máxima precisión numérica
        val distancia = Math.hypot(deltaX, deltaY)

        // Consumir 0.5% de batería por unidad de distancia
        val consumoBateria = distancia * 0.5
        bateria = maxOf(bateria - consumoBateria, 0.0)

        // Actualizar posiciones físicas
        actualizarPosX(posX + deltaX)
        actualizarPosY(posY + deltaY)

        // Verificar si la batería se agotó durante el movimiento
        if (bateria <= 0.0) {
            bateria = 0.0
            notificador.notificar("¡Batería agotada tras el movimiento!")
        }

        return true
    }

    /**
     * Delega la verificación de integridad y recepción de presión al [Casco].
     *
     * @param presionActualPa Presión actual en Pascales a la profundidad actual.
     * @return El nivel de integridad resultante del casco.
     */
    fun verificarIntegridad(presionActualPa: Double): Double = casco.recibirPresion(presionActualPa)

    /**
     * Sobrecarga que permite evaluar la integridad delegando el cálculo de presión
     * a un [CalculadorPresion] (DIP).
     */
    fun verificarIntegridadConCalculador(calculador: CalculadorPresion): Double {
        val presion = calculador.calcularPresion(posY)
        return verificarIntegridad(presion)
    }

    /**
     * Métodos semánticos para navegación y maniobras de motor e inmersión (ISP).
     */
    override fun navegar(deltaX: Double): Boolean = mover(deltaX = deltaX, deltaY = 0.0)
    override fun sumergir(deltaY: Double): Boolean = mover(deltaX = 0.0, deltaY = deltaY)

    /**
     * Asciende hacia la superficie (reduce posY).
     * Aplica la ralentización dinámica de la física de ascenso según la masa total acumulada (Issue #6).
     *
     * @param deltaY Desplazamiento vertical nominal pretendido en metros.
     * @return true si el desplazamiento se efectuó correctamente; false si la nave está destruida o sin batería.
     */
    override fun ascender(deltaY: Double): Boolean {
        val desplazamientoEfectivo = estrategiaAscenso.calcularDesplazamientoAscenso(
            deltaYNominal = deltaY,
            pesoTotal = pesoTotal,
            pesoBase = pesoBase,
            capacidadCargaMaxima = bodega?.capacidadMaxima ?: 0.0
        )
        return mover(deltaX = 0.0, deltaY = -desplazamientoEfectivo)
    }

    /**
     * Maniobra semántica de ascenso continuo utilizando la velocidad dinámica efectiva de ascenso.
     * Desplaza al submarino verticalmente durante 1 unidad de tiempo hacia la superficie.
     */
    fun ascender(): Boolean = mover(deltaX = 0.0, deltaY = -velocidadAscenso)

    override fun toString(): String {
        return "Submarino(posX=$posX, posY=$posY, pesoTotal=${"%.2f".format(pesoTotal)}kg, velocidadAscenso=${"%.2f".format(velocidadAscenso)}m/s, bateria=${"%.2f".format(bateria)}%, estaDestruido=$estaDestruido, casco=$casco)"
    }
}