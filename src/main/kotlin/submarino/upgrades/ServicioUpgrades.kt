package submarino

/**
 * ============================================================================
 * PROCESADOR DE COMPRAS Y TRANSACCIONES DE UPGRADES (ISSUE #8)
 * PRINCIPIOS SOLID (SRP, OCP, LSP, ISP, DIP = 0 violaciones)
 *
 * Cumple con:
 * - Single Responsibility Principle (SRP): se dedica exclusivamente a validar,
 *   debitar fondos, avanzar el nivel, aplicar el efecto al submarino y notificar.
 * - Dependency Inversion Principle (DIP): depende exclusivamente de contratos
 *   abstractos ([ConsultableCatalogo], [Pagable], [ObservadorTienda]).
 * - Open/Closed Principle (OCP): admite cualquier nueva mejora y cualquier nueva
 *   fuente pagable sin modificar su lógica transaccional.
 * - Liskov Substitution Principle (LSP): manejo estricto de resultados sellados sin
 *   casteos `is`/`as` dentro de la lógica interna ni excepciones descontroladas.
 * ============================================================================
 */

/**
 * Servicio coordinador del proceso de compra y aplicación de mejoras.
 *
 * @param catalogo Abstracción para la consulta de mejoras disponibles en la tienda (DIP).
 */
class ServicioUpgrades(
    val catalogo: ConsultableCatalogo
) : ProcesadorCompras, GestorObservadoresTienda {

    private val observadores: MutableList<ObservadorTienda> = mutableListOf()

    override fun registrarObservador(observador: ObservadorTienda) {
        if (!observadores.contains(observador)) {
            observadores.add(observador)
        }
    }

    override fun eliminarObservador(observador: ObservadorTienda) {
        observadores.remove(observador)
    }

    /**
     * Ejecuta el proceso transaccional de compra de una mejora:
     * 1. Consulta la mejora por su identificador en el [ConsultableCatalogo].
     *    Si no existe, retorna [ResultadoCompra.MejoraNoEncontrada].
     * 2. Verifica si la mejora se encuentra en su nivel máximo ([LimiteNivel.esNivelMaximo]).
     *    Si ya está al tope, retorna [ResultadoCompra.NivelMaximoAlcanzado].
     * 3. Evalúa si el [Pagable] posee saldo suficiente para cubrir el costo actual.
     *    Si no alcanza, retorna [ResultadoCompra.FondosInsuficientes].
     * 4. Debita el costo monetario del [Pagable].
     * 5. Incrementa el nivel de la mejora ([Mejorable.subirNivel]).
     * 6. Aplica el efecto sobre el [Submarino] ([AplicableSubmarino.aplicar]).
     * 7. Emite las notificaciones pertinentes a todos los [ObservadorTienda] registrados.
     * 8. Retorna [ResultadoCompra.Exito] con el desglose inmutable de la operación.
     *
     * @param idMejora Clave única de la mejora que se desea adquirir.
     * @param pagable Entidad desde la cual se debitan los créditos (billetera/monedero).
     * @param submarino Entidad que recibe el incremento en sus estadísticas.
     * @return [ResultadoCompra] describiendo el desenlace tipado de la operación.
     */
    override fun comprarMejora(
        idMejora: String,
        pagable: Pagable,
        submarino: Submarino
    ): ResultadoCompra {
        val mejora = catalogo.buscarPorId(idMejora)
        if (mejora == null) {
            val motivo = "Mejora no encontrada en el catálogo: '$idMejora'"
            notificarRechazo(idMejora, motivo)
            return ResultadoCompra.MejoraNoEncontrada(idMejora)
        }

        if (mejora.esNivelMaximo) {
            val motivo = "La mejora '${mejora.nombre}' ya alcanzó el nivel máximo (${mejora.nivelMaximo})"
            notificarRechazo(idMejora, motivo)
            return ResultadoCompra.NivelMaximoAlcanzado(
                mejoraId = idMejora,
                nivelActual = mejora.nivel,
                nivelMaximo = mejora.nivelMaximo
            )
        }

        val costoRequerido = mejora.costo
        if (pagable.saldo < costoRequerido) {
            val motivo = "Fondos insuficientes para '${mejora.nombre}'. Requiere $$costoRequerido, disponible $$${pagable.saldo}"
            notificarRechazo(idMejora, motivo)
            return ResultadoCompra.FondosInsuficientes(
                mejoraId = idMejora,
                costoRequerido = costoRequerido,
                saldoDisponible = pagable.saldo
            )
        }

        val debitado = pagable.debitar(costoRequerido)
        if (!debitado) {
            val motivo = "No fue posible debitar $$costoRequerido del saldo disponible"
            notificarRechazo(idMejora, motivo)
            return ResultadoCompra.FondosInsuficientes(
                mejoraId = idMejora,
                costoRequerido = costoRequerido,
                saldoDisponible = pagable.saldo
            )
        }

        val nivelAnterior = mejora.nivel
        mejora.subirNivel()
        mejora.aplicar(submarino)

        notificarExito(mejora, pagable.saldo)

        return ResultadoCompra.Exito(
            mejora = mejora,
            nivelAnterior = nivelAnterior,
            nuevoNivel = mejora.nivel,
            costoPagado = costoRequerido,
            saldoRestante = pagable.saldo
        )
    }

    private fun notificarExito(mejora: Mejora, saldoRestante: Int) {
        observadores.forEach { it.alComprarMejora(mejora, saldoRestante) }
    }

    private fun notificarRechazo(idMejora: String, motivo: String) {
        observadores.forEach { it.alRechazarCompra(idMejora, motivo) }
    }
}

/**
 * Implementación de [ObservadorTienda] para renderizado en consola estándar.
 */
class ObservadorTiendaConsola : ObservadorTienda {
    override fun alComprarMejora(mejora: Mejora, saldoRestante: Int) {
        println("[TIENDA UPGRADES] ¡Mejora adquirida! '${mejora.nombre}' ahora en nivel ${mejora.nivel}/${mejora.nivelMaximo}. Saldo restante: $$saldoRestante.")
    }

    override fun alRechazarCompra(idMejora: String, motivo: String) {
        println("[TIENDA UPGRADES] Compra rechazada para mejora '$idMejora'. Motivo: $motivo.")
    }
}

/**
 * Implementación silenciosa de [ObservadorTienda] para tests unitarios o ejecución headless.
 */
class ObservadorTiendaSilencioso : ObservadorTienda {
    override fun alComprarMejora(mejora: Mejora, saldoRestante: Int) {
        // No-op intencional
    }

    override fun alRechazarCompra(idMejora: String, motivo: String) {
        // No-op intencional
    }
}
