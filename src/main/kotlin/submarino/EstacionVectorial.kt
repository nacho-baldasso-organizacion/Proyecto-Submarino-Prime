package submarino

import javafx.scene.Group
import javafx.scene.shape.Circle

/**
 * Estación base flotante construida con formas vectoriales.
 *
 * Incluye plataforma, torre de control, luces de balizamiento y cartel.
 */
class EstacionVectorial {

    private val baliza: Circle = BalizaEstacionVectorial.crear()
    private val animacionBaliza: AnimacionBalizaEstacion = FabricaVisual.crearAnimacionBalizaEstacion()

    val nodo: Group = ConstructorEstacionVectorial.crear(baliza)

    init {
        animacionBaliza.iniciar(baliza)
    }

    /**
     * Detiene el parpadeo de la baliza cuando la vista deja de estar activa.
     */
    fun detenerAnimaciones() {
        animacionBaliza.detener()
    }
}
