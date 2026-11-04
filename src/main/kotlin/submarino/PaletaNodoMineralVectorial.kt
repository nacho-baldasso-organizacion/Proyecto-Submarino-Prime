package submarino

import javafx.scene.paint.Color

internal data class PaletaNodoMineralVectorial(
    val base: Color,
    val claro: Color,
    val oscuro: Color,
    val brillo: Color
) {
    companion object {
        fun para(tipo: TipoNodoMineral): PaletaNodoMineralVectorial = when (tipo) {
            TipoNodoMineral.COBRE -> PaletaNodoMineralVectorial(
                base = Color.web("#B5652A"),
                claro = Color.web("#F0A35E"),
                oscuro = Color.web("#6E3512"),
                brillo = Color.web("#FF9A4D")
            )
            TipoNodoMineral.HIERRO -> PaletaNodoMineralVectorial(
                base = Color.web("#7E8A94"),
                claro = Color.web("#D5DEE5"),
                oscuro = Color.web("#414A52"),
                brillo = Color.web("#C7D6E0")
            )
            TipoNodoMineral.ORO -> PaletaNodoMineralVectorial(
                base = Color.web("#D9A61A"),
                claro = Color.web("#FFF2A8"),
                oscuro = Color.web("#8A6208"),
                brillo = Color.web("#FFD34D")
            )
        }
    }
}
