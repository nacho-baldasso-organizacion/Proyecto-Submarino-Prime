package submarino

/**
 * Clase base abstracta que encapsula el comportamiento común y el estado de cualquier [Casco].
 * Cumple con SRP, OCP (Template Method) y DRY eliminando la duplicación estructural entre cascos.
 */
abstract class CascoBase(
    presionMaximaPa: Double,
    integridad: Double = 100.0,
    private val notificador: Notificador = NotificadorConsola
) : Casco {

    final override var presionMaximaPa: Double = presionMaximaPa
        private set

    final override var integridad: Double = integridad
        private set

    override val estaDestruido: Boolean
        get() = integridad <= 0.0

    override fun reforzar(incrementoPresionPa: Double) {
        require(incrementoPresionPa > 0.0) {
            "El incremento de presión debe ser estrictamente positivo: $incrementoPresionPa"
        }
        presionMaximaPa += incrementoPresionPa
    }

    override fun recibirPresion(presionActualPa: Double): Double {
        if (presionActualPa > presionMaximaPa) {
            val exceso = presionActualPa - presionMaximaPa
            val danio = calcularDanio(exceso)
            integridad = maxOf(integridad - danio, 0.0)
            if (estaDestruido) {
                notificarColapso()
            }
        }
        return integridad
    }

    /**
     * Algoritmo de cálculo de daño estructural según el exceso de presión experimentado.
     */
    protected abstract fun calcularDanio(excesoPresion: Double): Double

    /**
     * Emite la alerta de colapso a través del notificador desacoplado.
     */
    protected open fun notificarColapso() {
        notificador.notificar("¡ALERTA CRÍTICA: El casco ha colapsado por exceso de presión hidrostática!")
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
 * Representa el módulo de casco estándar del submarino (Issue #3).
 */
class ModuloCasco(
    presionMaximaPa: Double,
    integridad: Double = 100.0,
    notificador: Notificador = NotificadorConsola
) : CascoBase(presionMaximaPa, integridad, notificador) {

    override fun calcularDanio(excesoPresion: Double): Double {
        return excesoPresion / presionMaximaPa
    }

    override fun toString(): String {
        return "ModuloCasco(presionMaximaPa=$presionMaximaPa, integridad=${"%.2f".format(integridad)}%)"
    }
}

/**
 * Especialización de Casco con blindaje cerámico/aleación de titanio que reduce el daño por sobrepresión (OCP).
 *
 * @param factorAbsorcion Porcentaje de absorción del daño recibido (0.0 sin protección adicional, 0.5 absorbe 50%).
 */
class CascoReforzado(
    presionMaximaPa: Double,
    integridad: Double = 100.0,
    private val factorAbsorcion: Double = 0.5,
    notificador: Notificador = NotificadorConsola
) : CascoBase(presionMaximaPa, integridad, notificador) {

    val factorBlindaje: Double
        get() = factorAbsorcion

    override fun calcularDanio(excesoPresion: Double): Double {
        val danioBase = excesoPresion / presionMaximaPa
        val mitigacion = (1.0 - factorAbsorcion.coerceIn(0.0, 1.0))
        return danioBase * mitigacion
    }

    override fun notificarColapso() {
        super.notificarColapso()
    }

    override fun toString(): String {
        return "CascoReforzado(presionMaximaPa=$presionMaximaPa, integridad=${"%.2f".format(integridad)}%, blindaje=${(factorAbsorcion * 100).toInt()}%)"
    }
}
