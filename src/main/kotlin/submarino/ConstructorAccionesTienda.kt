package submarino

import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.HBox

/**
 * Construye la fila de acciones de la tienda: vender, recargar, reparar y
 * desacoplar. Delega toda la lógica económica en [SesionJuego].
 */
class ConstructorAccionesTienda(
    private val sesion: SesionJuego,
    private val etiquetaMensaje: Label,
    private val alDesacoplar: () -> Unit,
    private val alRefrescar: () -> Unit
) {

    fun crearFilaAcciones(): HBox {
        val vender = ControlesTienda.crearBoton("VENDER MINERALES", "#34D399").apply {
            setOnAction {
                val resultado = sesion.venderMinerales()
                etiquetaMensaje.text = resultado.obtenerResumen()
                alRefrescar()
            }
        }
        val oxigeno = ControlesTienda.crearBoton(
            "RECARGAR OXÍGENO · $${SesionJuego.COSTO_RECARGA_OXIGENO}",
            "#38BDF8"
        ).apply {
            setOnAction {
                etiquetaMensaje.text = sesion.recargarOxigeno()
                alRefrescar()
            }
        }
        val casco = ControlesTienda.crearBoton(
            "REPARAR CASCO · $${SesionJuego.COSTO_REPARACION_CASCO}",
            "#FBBF24"
        ).apply {
            setOnAction {
                etiquetaMensaje.text = sesion.repararCasco()
                alRefrescar()
            }
        }
        val desacoplar = ControlesTienda.crearBoton("DESACOPLAR", "#FB7185").apply {
            setOnAction {
                sesion.desacoplar()
                alDesacoplar()
            }
        }

        return HBox(10.0).apply {
            alignment = Pos.CENTER_LEFT
            children.addAll(vender, oxigeno, casco, desacoplar)
        }
    }
}
