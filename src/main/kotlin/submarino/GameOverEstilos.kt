package submarino

/**
 * Estilos visuales del modal de fin de partida.
 *
 * Agrupa únicamente las hojas de estilo JavaFX para que la composición del
 * panel y la fábrica de botones no mezclen presentación con estructura.
 */
internal object GameOverEstilos {

    const val PANEL =
        "-fx-background-color: rgba(28, 8, 8, 0.97);" +
            "-fx-background-radius: 18;" +
            "-fx-border-color: rgba(255, 122, 26, 0.65);" +
            "-fx-border-radius: 18;" +
            "-fx-padding: 28;" +
            "-fx-effect: dropshadow(gaussian, rgba(255, 74, 20, 0.45), 42, 0.35, 0, 0);"

    const val TITULO =
        "-fx-font-family: 'Segoe UI';" +
            "-fx-font-size: 54px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #FF7A1A;" +
            "-fx-effect: dropshadow(gaussian, rgba(255, 90, 42, 0.85), 28, 0.4, 0, 0);"

    const val SUBTITULO =
        "-fx-font-family: 'Segoe UI';" +
            "-fx-font-size: 15px;" +
            "-fx-text-fill: #F7C9B5;"

    const val CAUSA =
        "-fx-font-family: 'Segoe UI';" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #FFD75A;" +
            "-fx-background-color: rgba(255, 122, 26, 0.12);" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 8 16 8 16;"

    const val BOTON_PRINCIPAL =
        "-fx-background-color: #FF7A1A;" +
            "-fx-text-fill: #1A0703;" +
            "-fx-font-family: 'Segoe UI';" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 11 32 11 32;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(255, 122, 26, 0.55), 18, 0.3, 0, 4);"

    const val BOTON_PRINCIPAL_HOVER =
        "-fx-background-color: #FFA24D;" +
            "-fx-text-fill: #1A0703;" +
            "-fx-font-family: 'Segoe UI';" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 11 32 11 32;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(255, 122, 26, 0.85), 24, 0.35, 0, 4);"

    const val BOTON_SECUNDARIO =
        "-fx-background-color: rgba(52, 20, 14, 0.9);" +
            "-fx-text-fill: #FFD7C2;" +
            "-fx-font-family: 'Segoe UI';" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: rgba(255, 160, 110, 0.75);" +
            "-fx-border-radius: 10;" +
            "-fx-padding: 11 32 11 32;" +
            "-fx-cursor: hand;"

    const val BOTON_SECUNDARIO_HOVER =
        "-fx-background-color: rgba(96, 38, 22, 0.95);" +
            "-fx-text-fill: #FFF3EA;" +
            "-fx-font-family: 'Segoe UI';" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #FFB07A;" +
            "-fx-border-radius: 10;" +
            "-fx-padding: 11 32 11 32;" +
            "-fx-cursor: hand;"

    private val estilosBoton: Map<Pair<Boolean, Boolean>, String> = mapOf(
        (true to true) to BOTON_PRINCIPAL_HOVER,
        (true to false) to BOTON_PRINCIPAL,
        (false to true) to BOTON_SECUNDARIO_HOVER,
        (false to false) to BOTON_SECUNDARIO
    )

    /**
     * Resuelve el estilo de un botón según su jerarquía y estado de hover.
     */
    fun estiloBoton(principal: Boolean, hover: Boolean): String =
        estilosBoton.getValue(principal to hover)
}
