package submarino

/**
 * Data class que representa el módulo del casco del submarino para que la copia de esto no tenga
 que ser compleja.
 * presionMaximaPa Presión máxima que puede soportar el casco en Pascales
 * integridad Nivel actual de integridad del casco (0.0 - 1.0 o porcentaje)
 */
data class ModuloCasco(
    val presionMaximaPa: Double,
    var integridad: Double
)