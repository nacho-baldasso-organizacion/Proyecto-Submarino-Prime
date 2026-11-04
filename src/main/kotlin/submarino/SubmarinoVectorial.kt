package submarino

import javafx.scene.Group

class SubmarinoVectorial {

    private val helice: Group = HeliceSubmarinoVectorial.crear()
    private val animacionHelice: AnimacionHeliceSubmarino = AnimacionHeliceSubmarino()
    val nodo: Group = CuerpoSubmarinoVectorial.crear(helice)

    init {
        animacionHelice.iniciar(helice)
    }

    /**
     * Orienta el vehículo según la dirección horizontal de navegación.
     */
    fun actualizarDireccion(direccionHorizontal: Double) {
        nodo.scaleX = if (direccionHorizontal < 0.0) -ESCALA else ESCALA
        nodo.scaleY = ESCALA
    }

    /**
     * Detiene la animación de la hélice cuando la vista deja de estar activa.
     */
    fun detenerAnimaciones() {
        animacionHelice.detener()
    }

    companion object {
        private const val ESCALA = 1.0
    }
}
