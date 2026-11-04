package submarino

/**
 * Estilos JavaFX compartidos por el modal de la tienda de mejoras.
 *
 * Concentra los valores visuales para que los constructores de controles y
 * paneles no dupliquen cadenas de estilo.
 */
object EstilosTienda {

    const val ESTILO_PANEL =
        "-fx-background-color: rgba(3, 25, 38, 0.97);" +
            "-fx-background-radius: 18;" +
            "-fx-border-color: rgba(127, 227, 240, 0.45);" +
            "-fx-border-radius: 18;" +
            "-fx-padding: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.75), 36, 0.3, 0, 12);"

    const val ESTILO_TITULO =
        "-fx-font-family: 'Segoe UI';" +
            "-fx-font-size: 22px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #E6F7FA;"

    const val ESTILO_SUBTITULO =
        "-fx-font-family: 'Segoe UI';" +
            "-fx-font-size: 11px;" +
            "-fx-text-fill: #7FA8B5;"

    const val ESTILO_TITULO_PEQUENO =
        "-fx-font-family: 'Segoe UI';" +
            "-fx-font-size: 9px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #7FA8B5;"

    const val ESTILO_VALOR =
        "-fx-font-family: 'Segoe UI';" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #E6F7FA;"

    const val ESTILO_MENSAJE =
        "-fx-font-family: 'Segoe UI';" +
            "-fx-font-size: 11px;" +
            "-fx-text-fill: #9FE8F2;" +
            "-fx-background-color: rgba(127, 227, 240, 0.08);" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 8 12 8 12;"

    const val ESTILO_TARJETA =
        "-fx-background-color: rgba(127, 227, 240, 0.07);" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 8 12 8 12;" +
            "-fx-min-width: 130;"

    const val ESTILO_FILA_MEJORA =
        "-fx-background-color: rgba(127, 227, 240, 0.06);" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 8 10 8 10;"

    const val ESTILO_MEJORA_NOMBRE =
        "-fx-font-family: 'Segoe UI';" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #E6F7FA;"

    const val ESTILO_MEJORA_DESCRIPCION =
        "-fx-font-family: 'Segoe UI';" +
            "-fx-font-size: 10px;" +
            "-fx-text-fill: #8FB6C2;"

    const val ESTILO_COSTO =
        "-fx-font-family: 'Segoe UI';" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #FFD75A;"

    const val ESTILO_SCROLL =
        "-fx-background: transparent;" +
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;"

    /**
     * Estilo del mensaje informativo del modal.
     */
    fun estiloMensaje(): String = ESTILO_MENSAJE
}
