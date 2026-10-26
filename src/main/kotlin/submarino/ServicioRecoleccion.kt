package submarino

/**
 * Inversión de Dependencias (DIP):
 * Abstracción del servicio coordinador de recolección de recursos en el mapa.
 */
interface ServicioRecoleccionContrato {
    /**
     * Procesa la interacción espacial y evalúa si corresponde recolectar algún elemento.
     */
    fun procesarRecoleccion(
        posicion: Posicionable,
        elementosEnMapa: List<Recolectable>
    ): ResultadoRecoleccion
}

/**
 * Servicio coordinador de la lógica de recolección de minerales.
 *
 * Cumple estrictamente con:
 * - Dependency Inversion Principle (DIP): implementa [ServicioRecoleccionContrato]
 *   y depende exclusivamente de las abstracciones [Almacenamiento] y [DetectorColisiones],
 *   inyectadas a través del constructor.
 * - Single Responsibility Principle (SRP): coordina el flujo entre detección espacial y
 *   almacenamiento de inventario sin acoplarse a detalles de infraestructura.
 * - Encapsulación: campos privados protegidos contra mutación externa.
 */
class ServicioRecoleccion(
    private val almacenamiento: Almacenamiento,
    private val detectorColisiones: DetectorColisiones
) : ServicioRecoleccionContrato {

    /**
     * Administra el flujo de verificación de colisión y recolección automática al colisionar
     * con un mineral en el mapa.
     *
     * @param posicion Posición actual del submarino o sonda de recolección.
     * @param elementosEnMapa Colección de elementos recolectables existentes en el entorno.
     * @return [ResultadoRecoleccion] describiendo el resultado exacto de la operación.
     */
    override fun procesarRecoleccion(
        posicion: Posicionable,
        elementosEnMapa: List<Recolectable>
    ): ResultadoRecoleccion {
        val itemColisionado = detectorColisiones.verificarColision(posicion, elementosEnMapa)
            ?: return ResultadoRecoleccion.SinColision

        if (itemColisionado.recolectado) {
            return ResultadoRecoleccion.YaRecolectado(itemColisionado)
        }

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
