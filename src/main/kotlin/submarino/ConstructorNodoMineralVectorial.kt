package submarino

import javafx.scene.Group

internal object ConstructorNodoMineralVectorial {

    fun crear(tipo: TipoNodoMineral): Group {
        val paleta = PaletaNodoMineralVectorial.para(tipo)

        return Group().apply {
            isPickOnBounds = false
            children.addAll(RocaNodoMineralVectorial.crear(paleta))
            children.addAll(CristalNodoMineralVectorial.crear(paleta))
        }
    }
}
