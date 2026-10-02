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
 * Permite que Submarino dependa de esta interfaz y no de una implementación concreta,
 * facilitando la incorporación de nuevos tipos de cascos (reforzados, experimentales, escudos)
 * sin modificar la lógica del submarino.
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
}

/**
 * Responsabilidad Única (SRP) e Inversión de Dependencias (DIP):
 * Abstracción para el desacoplamiento de mensajes y alertas del sistema.
 * Evita que las entidades de dominio llamen directamente a `println`, permitiendo
 * redirigir avisos a consolas, interfaces gráficas, logs o silenciarlos en tests unitarios.
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
 * Implementación silenciosa de Notificador para pruebas unitarias o ejecución headless.
 */
object NotificadorSilencioso : Notificador {
    override fun notificar(mensaje: String) {
        // No-op intencional
    }
}
