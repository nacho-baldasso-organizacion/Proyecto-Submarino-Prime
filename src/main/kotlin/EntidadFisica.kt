package submarino

/**
 * La clase abstracta basicamente lo que hace es definir las propiedades de las posiciones en X, Y y
 su velocidad.
 */
abstract class EntidadFisica(
    var posX: Double = 0.0,
    var posY: Double = 0.0,
    var velocidad: Double = 0.0
) {
    /**
     * Actualiza la posición Y asegurando que no sea menor a 0.
     * Basicamente el maxOf lo usamos para que siempre muestre el mayor numero y no pueda ser -0 
     y no tener que usar un sistema anidado para no tener errores mas adelante.
     */
    protected fun actualizarPosY(nuevaPosY: Double) {
        posY = maxOf(nuevaPosY, 0.0)
    }

    /**
     * Actualiza la posición X.
     * 
     */
    protected fun actualizarPosX(nuevaPosX: Double) {
        posX = nuevaPosX
    }
}