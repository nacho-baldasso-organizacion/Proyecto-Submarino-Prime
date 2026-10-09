package submarino

/**
 * Jerarquía sellada que modela exhaustivamente los posibles resultados de un intento de recolección.
 * Facilita el manejo seguro de casos de uso (pattern matching exhaustivo con `when`).
 */
sealed class ResultadoRecoleccion {
    /**
     * El ítem colisionó y fue ingresado exitosamente en la bodega.
     * @property item Objeto recolectado.
     * @property pesoAcumulado Peso total de la bodega tras la recolección.
     */
    data class Exito(
        val item: Recolectable,
        val pesoAcumulado: Double
    ) : ResultadoRecoleccion()

    /**
     * Se detectó colisión con un ítem, pero la bodega no tiene capacidad de peso suficiente.
     * @property item Objeto que no pudo ser almacenado.
     * @property excesoPeso Peso excedente que impidió la recolección.
     * @property capacidadMaxima Capacidad de carga máxima de la bodega.
     */
    data class BodegaLlena(
        val item: Recolectable,
        val excesoPeso: Double,
        val capacidadMaxima: Double
    ) : ResultadoRecoleccion()

    /**
     * No existe ningún ítem colisionable en las coordenadas actuales.
     */
    object SinColision : ResultadoRecoleccion()

    /**
     * El ítem en la posición indicada ya fue recolectado con anterioridad.
     */
    data class YaRecolectado(
        val item: Recolectable
    ) : ResultadoRecoleccion()
}

/**
 * Servicio coordinador de la lógica de recolección de minerales.
 *
 * Cumple estrictamente con:
 * - Dependency Inversion Principle (DIP): depende exclusivamente de las abstracciones
 *   [Almacenamiento] y [DetectorColisiones], inyectadas a través del constructor.
 * - Single Responsibility Principle (SRP): coordina el flujo entre detección espacial y
 *   almacenamiento de inventario sin acoplarse a detalles de infraestructura.
 *
 * @property almacenamiento Abstracción del contenedor donde se guardan los recursos recolectados.
 * @property detectorColisiones Abstracción del detector de proximidad o colisión física.
 */
class ServicioRecoleccion(
    val almacenamiento: Almacenamiento,
    val detectorColisiones: DetectorColisiones
) {

    /**
     * Administra el flujo de verificación de colisión y recolección automática al colisionar
     * con un mineral en el mapa:
     * 1. Consulta al [DetectorColisiones] si hay contacto con algún [Recolectable] disponible.
     * 2. Si no hay colisión, informa [ResultadoRecoleccion.SinColision].
     * 3. Si el ítem ya está recolectado, informa [ResultadoRecoleccion.YaRecolectado].
     * 4. Intenta guardar el mineral en el [Almacenamiento].
     * 5. Informa [ResultadoRecoleccion.Exito] si se guardó, o [ResultadoRecoleccion.BodegaLlena]
     *    si se sobrepasa la capacidad máxima de peso.
     *
     * @param posicion Posición actual del submarino o sonda de recolección.
     * @param elementosEnMapa Colección de elementos recolectables existentes en el entorno.
     * @return [ResultadoRecoleccion] describiendo el resultado exacto de la operación.
     */
    fun procesarRecoleccion(
        posicion: Posicionable,
        elementosEnMapa: List<Recolectable>
    ): ResultadoRecoleccion {
        // 1. Detección de colisión
        val itemColisionado = detectorColisiones.verificarColision(posicion, elementosEnMapa)
            ?: return ResultadoRecoleccion.SinColision

        // 2. Comprobación de estado previo
        if (itemColisionado.recolectado) {
            return ResultadoRecoleccion.YaRecolectado(itemColisionado)
        }

        // 3. Intento de almacenamiento en bodega
        val agregadoExitoso = almacenamiento.agregar(itemColisionado)

        return if (agregadoExitoso) {
            ResultadoRecoleccion.Exito(
                item = itemColisionado,
                pesoAcumulado = almacenamiento.pesoActual
            )
        } else {
            val exceso = (almacenamiento.pesoActual + itemColisionado.peso) - almacenamiento.capacidadMaxima
            ResultadoRecoleccion.BodegaLlena(
                item = itemColisionado,
                excesoPeso = exceso,
                capacidadMaxima = almacenamiento.capacidadMaxima
            )
        }
    }
}
