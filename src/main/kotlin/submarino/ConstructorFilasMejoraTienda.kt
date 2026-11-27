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
        return HBox(12.0).apply {
            alignment = Pos.CENTER_LEFT
            style = EstilosTienda.ESTILO_FILA_MEJORA
            children.addAll(
                crearInfoVBox(mejora),
                crearEspacio(),
                crearCostoLabel(mejora),
                crearBoton(mejora)
            )
        }
    }

    private fun crearInfoVBox(mejora: Mejora): VBox {
        val nombre = crearNombreLabel(mejora)
        val descripcion = crearDescripcionLabel(mejora)
        val info = VBox(2.0).apply {
            children.addAll(nombre, descripcion)
        }
        return info
    }

    private fun crearNombreLabel(mejora: Mejora): Label {
        val label = Label("${mejora.nombre}  ·  Nv. ${mejora.nivel}/${mejora.nivelMaximo()}").apply {
            style = EstilosTienda.ESTILO_MEJORA_NOMBRE
        }
        return label
    }

    private fun crearDescripcionLabel(mejora: Mejora): Label {
        val label = Label(mejora.descripcion()).apply {
            style = EstilosTienda.ESTILO_MEJORA_DESCRIPCION
            isWrapText = true
            maxWidth = 520.0
        }
        return label
    }

    private fun crearEspacio(): Region {
        val region = Region().apply {
            HBox.setHgrow(this, Priority.ALWAYS)
        }
        return region
    }

    private fun crearCostoLabel(mejora: Mejora): Label {
        val label = Label(if (mejora.esNivelMaximo) "MÁXIMO" else "$ ${mejora.costo}").apply {
            style = EstilosTienda.ESTILO_COSTO
        }
        return label
    }

    private fun crearBoton(mejora: Mejora): javafx.scene.control.Button {
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
        return boton
    }
}