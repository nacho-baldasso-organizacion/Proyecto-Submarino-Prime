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
    private val notificador: Notificador = NotificadorConsola
) : EntidadFisica(posX, posY, velocidad), Navegable {

    var bateria: Double = 100.0
        private set

    /**
     * Delega el estado de destrucción al [Casco].
     */
    val estaDestruido: Boolean
        get() = casco.estaDestruido

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
    override fun ascender(deltaY: Double): Boolean = mover(deltaX = 0.0, deltaY = -deltaY)

    override fun toString(): String {
        return "Submarino(posX=$posX, posY=$posY, bateria=${"%.2f".format(bateria)}%, estaDestruido=$estaDestruido, casco=$casco)"
    }
}