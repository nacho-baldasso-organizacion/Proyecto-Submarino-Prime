package submarino

import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox

/**
 * Construye y reconstruye las filas de mejoras disponibles en la tienda.
 *
 * Cada fila refleja el estado del catálogo y dispara la compra a través de
 * [SesionJuego]; notifica el mensaje resultante y la necesidad de refrescar.
 */
class ConstructorFilasMejoraTienda(
    private val sesion: SesionJuego,
    private val contenedor: VBox,
    private val alMensaje: (String) -> Unit,
    private val alRefrescar: () -> Unit
) {

    fun reconstruir() {
        contenedor.children.clear()
        sesion.catalogo().forEach { mejora ->
            contenedor.children.add(crearFilaMejora(mejora))
        }
    }

    private fun crearFilaMejora(mejora: Mejora): HBox {
        val nombre = Label("${mejora.nombre}  ·  Nv. ${mejora.nivel}/${mejora.nivelMaximo()}").apply {
            style = EstilosTienda.ESTILO_MEJORA_NOMBRE
        }
        val descripcion = Label(mejora.descripcion()).apply {
            style = EstilosTienda.ESTILO_MEJORA_DESCRIPCION
            isWrapText = true
            maxWidth = 520.0
        }
        val info = VBox(2.0).apply {
            children.addAll(nombre, descripcion)
        }
        val espacio = Region().apply {
            HBox.setHgrow(this, Priority.ALWAYS)
        }
        val costo = Label(if (mejora.esNivelMaximo) "MÁXIMO" else "$ ${mejora.costo}").apply {
            style = EstilosTienda.ESTILO_COSTO
        }
        val boton = ControlesTienda.crearBoton(
            if (mejora.esNivelMaximo) "MÁX" else "COMPRAR",
            if (mejora.esNivelMaximo) "#475569" else "#34D399"
        ).apply {
            isDisable = mejora.esNivelMaximo
            setOnAction {
                val resultado = sesion.comprarMejora(mejora.id)
                alMensaje(resultado.obtenerMensaje())
                alRefrescar()
            }
        }

        return HBox(12.0).apply {
            alignment = Pos.CENTER_LEFT
            style = EstilosTienda.ESTILO_FILA_MEJORA
            children.addAll(info, espacio, costo, boton)
        }
    }
}
