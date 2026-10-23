package submarino

/**
 * ============================================================================
 * CONTRATOS E INTERFACES - PRINCIPIOS SOLID (ISP, DIP, OCP)
 * ============================================================================
 */

/**
 * Segregación de Interfaces (ISP):
 * Contrato elemental para cualquier entidad del entorno capaz de desplazarse en el espacio 2D.
 */
interface Movible {
    /**
     * Intenta mover la entidad aplicando desplazamientos relativos en X e Y.
     * @return true si el movimiento pudo ejecutarse, false si fue bloqueado por alguna restricción de estado.
     */
    fun mover(deltaX: Double, deltaY: Double): Boolean
}

/**
 * Segregación de Interfaces (ISP):
 * Especialización de movimiento náutico con métodos semánticos para navegación en superficie e inmersión.
 */
interface Navegable : Movible {
    fun navegar(deltaX: Double): Boolean
    fun sumergir(deltaY: Double): Boolean
    fun ascender(deltaY: Double): Boolean
}

/**
 * Inversión de Dependencias (DIP) y Abierto/Cerrado (OCP):
 * Abstracción que representa la resistencia física y estructural de un casco.
 */
interface Casco {
    val presionMaximaPa: Double
    val integridad: Double
    val estaDestruido: Boolean

    /**
     * Evalúa y aplica el daño estructural provocado por la presión externa recibida.
     * @param presionActualPa Presión hidrostática externa a la profundidad actual.
     * @return Nivel de integridad resultante tras el impacto de presión.
     */
    fun recibirPresion(presionActualPa: Double): Double

    /**
     * Refuerza la presión máxima admisible que el casco puede tolerar.
     *
     * @param incrementoPresionPa Cantidad de Pascales a adicionar al límite de resistencia.
     */
    fun reforzar(incrementoPresionPa: Double)
}

/**
 * Responsabilidad Única (SRP) e Inversión de Dependencias (DIP):
 * Abstracción para el desacoplamiento de mensajes y alertas del sistema.
 */
interface Notificador {
    fun notificar(mensaje: String)
}

/**
 * Implementación estándar de Notificador para salida por consola de comandos.
 */
object NotificadorConsola : Notificador {
    override fun notificar(mensaje: String) {
        println(mensaje)
    }
}

/**
 * Implementación silenciada de Notificador para pruebas unitarias y ejecución en segundo plano.
 * Registra los avisos en memoria para verificación sin producir salida en la consola de comandos.
 */
object NotificadorSilencioso : Notificador {
    private val historialMensajes: MutableList<String> = mutableListOf()

    override fun notificar(mensaje: String) {
        historialMensajes.add(mensaje)
    }

    val mensajes: List<String>
        get() = historialMensajes.toList()

    fun limpiar() {
        historialMensajes.clear()
    }
}
