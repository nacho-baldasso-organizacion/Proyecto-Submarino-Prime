package submarino

/**
 * Representa el módulo de casco estándar del submarino (Issue #3).
 * Implementa [Casco] cumpliendo con el Principio de Responsabilidad Única (SRP),
 * Inversión de Dependencias (DIP) y encapsulamiento de estado estricto.
 *
 * @param presionMaximaPa Presión máxima que puede soportar el casco en Pascales antes de sufrir daños.
 * @param integridad Nivel actual de integridad del casco (porcentaje de 0.0 a 100.0).
 * @param notificador Manejador de alertas desacoplado de la consola (DIP / SRP).
 */
class ModuloCasco(
    override val presionMaximaPa: Double,
    integridad: Double = 100.0,
    private val notificador: Notificador = NotificadorConsola
) : Casco {

    override var integridad: Double = integridad
        private set

    /**
     * Indica si el casco ha colapsado o ha sido destruido en su totalidad (integridad <= 0.0).
     */
    override val estaDestruido: Boolean
        get() = integridad <= 0.0

    /**
     * Recibe la presión externa en Pascales.
     * Si supera la presión máxima admisible, reduce la integridad proporcionalmente al exceso de presión.
     *
     * @param presionActualPa Presión actual en Pascales a la que se somete el casco.
     * @return El valor resultante de la integridad tras recibir la presión.
     */
    override fun recibirPresion(presionActualPa: Double): Double {
        if (presionActualPa > presionMaximaPa) {
            val danio = (presionActualPa - presionMaximaPa) / presionMaximaPa
            integridad = maxOf(integridad - danio, 0.0)
            if (estaDestruido) {
                notificador.notificar("¡ALERTA CRÍTICA: El casco ha colapsado por exceso de presión hidrostática!")
            }
        }
        return integridad
    }

    override fun toString(): String {
        return "ModuloCasco(presionMaximaPa=$presionMaximaPa, integridad=${"%.2f".format(integridad)}%)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Casco) return false
        return presionMaximaPa == other.presionMaximaPa && integridad == other.integridad
    }

    override fun hashCode(): Int {
        var result = presionMaximaPa.hashCode()
        result = 31 * result + integridad.hashCode()
        return result
    }
}

/**
 * Ejemplo directo del Principio Abierto/Cerrado (OCP):
 * Especialización de Casco con blindaje cerámico/aleación de titanio que reduce el daño por sobrepresión.
 * Se incorpora una nueva funcionalidad sin alterar ni una sola línea de [Submarino] ni de [ModuloCasco].
 *
 * @param factorAbsorcion Porcentaje de absorción del daño recibido (0.0 sin protección adicional, 0.5 absorbe 50%).
 */
class CascoReforzado(
    override val presionMaximaPa: Double,
    integridad: Double = 100.0,
    val factorAbsorcion: Double = 0.5,
    private val notificador: Notificador = NotificadorConsola
) : Casco {

    override var integridad: Double = integridad
        private set

    override val estaDestruido: Boolean
        get() = integridad <= 0.0

    override fun recibirPresion(presionActualPa: Double): Double {
        if (presionActualPa > presionMaximaPa) {
            val exceso = presionActualPa - presionMaximaPa
            val danioBase = exceso / presionMaximaPa
            val danioMitigado = danioBase * (1.0 - factorAbsorcion.coerceIn(0.0, 1.0))
            integridad = maxOf(integridad - danioMitigado, 0.0)
            if (estaDestruido) {
                notificador.notificar("¡ALERTA CRÍTICA: El casco reforzado ha colapsado por sobrepresión extrema!")
            }
        }
        return integridad
    }

    override fun toString(): String {
        return "CascoReforzado(presionMaximaPa=$presionMaximaPa, integridad=${"%.2f".format(integridad)}%, blindaje=${(factorAbsorcion * 100).toInt()}%)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Casco) return false
        return presionMaximaPa == other.presionMaximaPa && integridad == other.integridad
    }

    override fun hashCode(): Int {
        var result = presionMaximaPa.hashCode()
        result = 31 * result + integridad.hashCode()
        return result
    }
}