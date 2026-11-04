package submarino

import javafx.scene.Group
import javafx.scene.shape.Circle

internal object ConstructorEstacionVectorial {

    fun crear(baliza: Circle): Group = Group().apply {
        isPickOnBounds = false
        children.addAll(PlataformaEstacionVectorial.crear())
        children.addAll(TorreEstacionVectorial.crear())
        children.addAll(PlataformaEstacionVectorial.crearLuces())
        children.add(baliza)
    }
}
