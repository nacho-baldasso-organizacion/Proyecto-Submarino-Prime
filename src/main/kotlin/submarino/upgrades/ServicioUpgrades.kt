package submarino

/**
 * ============================================================================
 * PROCESADOR DE COMPRAS Y TRANSACCIONES DE UPGRADES (ISSUE #8)
 * PRINCIPIOS SOLID (SRP, OCP, LSP, ISP, DIP)
 * ============================================================================
 */

/**
 * Servicio coordinador del proceso de compra y aplicación de mejoras.
 * Cumple con DIP implementando [ServicioUpgradesContrato].
 */
class ServicioUpgrades(
    private val catalogo: ConsultableCatalogo
) : ServicioUpgradesContrato {

    private val observadores: MutableList<ObservadorTienda> = mutableListOf()

    override fun registrarObservador(observador: ObservadorTienda) {
        if (!observadores.contains(observador)) {
            observadores.add(observador)
        }
    }

    override fun eliminarObservador(observador: ObservadorTienda) {
        observadores.remove(observador)
    }

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
            return rechazarFondos(idMejora, costoRequerido, pagable.saldo, motivo)
        }

        val debitado = pagable.debitar(costoRequerido)
        if (!debitado) {
            val motivo = "No fue posible debitar $$costoRequerido del saldo disponible"
            return rechazarFondos(idMejora, costoRequerido, pagable.saldo, motivo)
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

    private fun rechazarFondos(idMejora: String, costo: Int, saldo: Int, motivo: String): ResultadoCompra.FondosInsuficientes {
        notificarRechazo(idMejora, motivo)
        return ResultadoCompra.FondosInsuficientes(idMejora, costo, saldo)
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
 * Implementación silenciada de [ObservadorTienda] para pruebas unitarias.
 * Registra en memoria las adquisiciones y rechazos para verificación sin violar LSP con métodos vacíos.
 */
class ObservadorTiendaSilencioso : ObservadorTienda {
    private val compras: MutableList<Mejora> = mutableListOf()
    private val rechazos: MutableList<Pair<String, String>> = mutableListOf()

    override fun alComprarMejora(mejora: Mejora, saldoRestante: Int) {
        compras.add(mejora)
    }

    override fun alRechazarCompra(idMejora: String, motivo: String) {
        rechazos.add(idMejora to motivo)
    }

    private val mejorasCompradas: List<Mejora>
        get() = compras.toList()

    private val rechazosRegistrados: List<Pair<String, String>>
        get() = rechazos.toList()

    fun limpiar() {
        compras.clear()
        rechazos.clear()
    }
}
