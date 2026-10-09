package submarino

/**
 * Clase principal.
 * Hereda de EntidadFisica y añade batería, estado de destrucción y casco.
 */
class Submarino(
    posX: Double = 0.0,
    posY: Double = 0.0,
    velocidad: Double = 0.0,
    val casco: ModuloCasco
) : EntidadFisica(posX, posY, velocidad) {

    var bateria: Double = 100.0
    var estaDestruido: Boolean = false

    /**
     * Mueve el submarino en los ejes X e Y.
     * Impide que posY sea menor a 0.0.
     * Consume 0.5% de batería por cada unidad de distancia recorrida.
     *
     * esplazamiento en el eje X (adelante/atrás)
     * Desplazamiento en el eje Y (profundidad/superficie)
     */
    fun mover(deltaX: Double, deltaY: Double) {
        if (estaDestruido) {
            println("El submarino está destruido y no puede moverse.")
            return
        }

        if (bateria <= 0.0) {
            println("Batería agotada. El submarino no puede moverse.")
            return
        }

        /**Calcular la distancia recorrida (distancia euclidiana) la usamos a Math.hypot porque la funcion
        del sistema esta optimizada para no tener errores de precision en numeros
         */ 
        val distancia = Math.hypot(deltaX, deltaY)

        // Consumir 0.5% de batería por unidad de distancia
        val consumoBateria = distancia * 0.5
        bateria = maxOf(bateria - consumoBateria, 0.0)

        // Actualizar posiciones
        actualizarPosX(posX + deltaX)
        actualizarPosY(posY + deltaY)

        // Verificar si la batería se agotó durante el movimiento
        if (bateria <= 0.0) {
            bateria = 0.0
            println("¡Batería agotada tras el movimiento!")
        }
    }

    /**
     * Verifica si el submarino ha sido destruido por presión excesiva.
     * @param presionActualPa Presión actual en Pascales a la profundidad actual
     */
    fun verificarIntegridad(presionActualPa: Double): Boolean {
        if (presionActualPa > casco.presionMaximaPa) {
            val danio = (presionActualPa - casco.presionMaximaPa) / casco.presionMaximaPa
            casco.integridad = maxOf(casco.integridad - danio, 0.0)
            if (casco.integridad <= 0.0) {
                estaDestruido = true
                println("¡El submarino ha sido destruido por presión excesiva!")
            }
        }
        return estaDestruido
    }

    override fun toString(): String {
        return "Submarino(posX=$posX, posY=$posY, bateria=$bateria%, estaDestruido=$estaDestruido, casco=$casco)"
    }
}