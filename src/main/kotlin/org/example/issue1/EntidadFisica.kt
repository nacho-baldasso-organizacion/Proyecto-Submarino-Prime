package submarino

/**
 * La clase abstracta define las propiedades físicas de posición en X, Y y velocidad,
 * garantizando la invariante de que Y jamás sea negativa (superficie del agua).
 *
 * Implementa [Movible] para cumplir con el Principio de Sustitución de Liskov (LSP)
 * y encapsula la mutación de estado con visibilidad `private set`.
 */
abstract class EntidadFisica(
    posX: Double = 0.0,
    posY: Double = 0.0,
    velocidad: Double = 0.0
) : Movible {

    var posX: Double = posX
        private set

    var posY: Double = maxOf(posY, 0.0)
        private set

    var velocidad: Double = velocidad
        private set

    /**
     * Actualiza la posición Y asegurando que no sea menor a 0.
     * El uso de maxOf garantiza que siempre se mantenga en o por debajo de la superficie (Y >= 0).
     */
    protected fun actualizarPosY(nuevaPosY: Double) {
        posY = maxOf(nuevaPosY, 0.0)
    }

    /**
     * Actualiza la posición X.
     */
    protected fun actualizarPosX(nuevaPosX: Double) {
        posX = nuevaPosX
    }

    /**
     * Actualiza la velocidad nominal del móvil.
     */
    protected fun actualizarVelocidad(nuevaVelocidad: Double) {
        velocidad = maxOf(nuevaVelocidad, 0.0)
    }
}
