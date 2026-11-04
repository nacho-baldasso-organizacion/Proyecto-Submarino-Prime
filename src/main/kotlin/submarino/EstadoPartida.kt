package submarino

import kotlin.math.abs

/**
 * Estado mutable de una partida en curso: posición, oxígeno, dirección,
 * mensajes y condición de destrucción.
 *
 * Responsabilidad única (SRP): avanzar la simulación de la nave y exponer su
 * estado de forma controlada; la orquestación queda en [SesionJuego].
 */
class EstadoPartida(
    private val anchoMapa: Double,
    private val altoMapa: Double,
    private val gestorDanio: GestorDanioSubmarino,
    private val casco: ModuloCasco
) {

    var posicionX: Double = ConfiguracionSesion.ESTACION_X
        private set
    var posicionY: Double = ConfiguracionSesion.PROFUNDIDAD_INICIAL
        private set
    private val sistemaOxigeno: SistemaOxigeno = SistemaOxigeno(
        oxigenoInicial = ConfiguracionSesion.OXIGENO_MAXIMO_INICIAL,
        maximoInicial = ConfiguracionSesion.OXIGENO_MAXIMO_INICIAL,
        consumoBasePorSegundo = ConfiguracionSesion.CONSUMO_OXIGENO_BASE,
        consumoMovimientoPorSegundo = ConfiguracionSesion.CONSUMO_OXIGENO_MOVIMIENTO,
        maximoAbsoluto = ConfiguracionSesion.OXIGENO_MAXIMO_ABSOLUTO,
        minimo = ConfiguracionSesion.OXIGENO_MINIMO
    )
    val oxigenoMaximo: Double
        get() = sistemaOxigeno.maximo
    val oxigeno: Double
        get() = sistemaOxigeno.oxigeno
    var direccionHorizontal: Double = 1.0
        private set
    var destruido: Boolean = false
        private set
    var causaDestruccion: String = ""
        private set
    var mensaje: String =
        "Explora el abismo: WASD / Flechas para moverte y ESPACIO para minar."
        private set

    /**
     * Avanza la nave y consume oxígeno; aplica daño por asfixia o presión y
     * marca la partida como destruida cuando corresponde.
     */
    fun actualizar(
        deltaSegundos: Double,
        direccionX: Double,
        direccionY: Double,
        estaAcoplado: Boolean,
        velocidadNave: Double
    ) {
        if (destruido) {
            return
        }
        if (!deltaSegundos.isFinite() || deltaSegundos <= 0.0) {
            return
        }
        if (estaAcoplado) {
            return
        }

        val dx = direccionX.coerceIn(-1.0, 1.0)
        val dy = direccionY.coerceIn(-1.0, 1.0)
        if (dx != 0.0) {
            direccionHorizontal = if (dx < 0.0) -1.0 else 1.0
        }

        val velocidad = velocidadNave.coerceAtLeast(ConfiguracionSesion.VELOCIDAD_MINIMA)
        posicionX = (posicionX + dx * velocidad * deltaSegundos)
            .coerceIn(0.0, anchoMapa)
        posicionY = (posicionY + dy * velocidad * deltaSegundos)
            .coerceIn(0.0, altoMapa)

        val intensidadMovimiento = abs(dx) + abs(dy)
        sistemaOxigeno.consumir(deltaSegundos, intensidadMovimiento)

        if (sistemaOxigeno.estaAgotado) {
            gestorDanio.aplicarDanioAsfixia(deltaSegundos)
            registrarDestruccion("Oxígeno agotado")
            return
        }

        gestorDanio.aplicarDanioProfundidad(posicionY, deltaSegundos)
        if (casco.integridad <= ConfiguracionSesion.INTEGRIDAD_MINIMA) {
            registrarDestruccion("Casco colapsado por sobrepresión")
        }
    }

    fun actualizarMensaje(texto: String) {
        mensaje = texto
    }

    fun establecerOxigeno(valor: Double) {
        sistemaOxigeno.establecer(valor)
    }

    fun aumentarOxigenoMaximo(incremento: Double) {
        sistemaOxigeno.aumentarMaximo(incremento)
    }

    private fun registrarDestruccion(causa: String) {
        if (destruido) {
            return
        }
        destruido = true
        causaDestruccion = causa
        mensaje = "GAME OVER · $causa. El submarino ha sido destruido."
    }
}
