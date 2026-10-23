package submarino

/**
 * ============================================================================
 * GESTIÓN DE ZONA DE ACOPLE Y ESTACIÓN MARÍTIMA (ISSUE #7)
 * PRINCIPIOS SOLID (SRP, OCP, DIP, ISP)
 * ============================================================================
 */

/**
 * Delimita la zona de superficie natural del agua (profundidad Y <= profundidadMaxima).
 */
class ZonaSuperficie(
    val profundidadMaxima: Double = 0.0
) : ZonaAcople {
    override fun estaEnZona(x: Double, y: Double): Boolean = y <= profundidadMaxima

    fun descripcion(): String = "Zona Superficie (<= ${profundidadMaxima}m)"
}

/**
 * Delimita una plataforma o muelle flotante acotado horizontalmente en el eje X y en la superficie (Y).
 */
class ZonaAcoplePlataforma(
    val xMin: Double,
    val xMax: Double,
    val profundidadMaxima: Double = 0.0
) : ZonaAcople {

    init {
        require(xMin <= xMax) { "xMin ($xMin) no puede ser mayor que xMax ($xMax)" }
    }

    override fun estaEnZona(x: Double, y: Double): Boolean =
        y <= profundidadMaxima && x in xMin..xMax

    fun anchoPlataforma(): Double = xMax - xMin

    fun descripcion(): String = "Plataforma X: [$xMin, $xMax] en superficie (<= ${profundidadMaxima}m)"
}

/**
 * Gestión de la estación de acople en la superficie marina.
 *
 * Cumple con:
 * - Single Responsibility Principle (SRP)
 * - Dependency Inversion Principle (DIP)
 * - Interface Segregation Principle (ISP)
 * - Patrón Observer
 */
class EstacionAcople(
    val zonaAcople: ZonaAcople = ZonaSuperficie()
) : ControlAcople, ConsultaAcople {

    private val observadores: MutableList<ObservadorAcople> = mutableListOf()

    var naveAcoplada: Acoplable? = null
        private set

    override val hayNaveAcoplada: Boolean
        get() {
            val nave = naveAcoplada
            if (nave != null && !nave.estaAcoplado) {
                naveAcoplada = null
                return false
            }
            return nave != null
        }

    override fun puedeAcoplar(x: Double, y: Double): Boolean {
        return !hayNaveAcoplada && zonaAcople.estaEnZona(x, y)
    }

    override fun acoplar(nave: Acoplable, x: Double, y: Double): Boolean {
        if (!puedeAcoplar(x, y)) {
            return false
        }
        nave.acoplar()
        naveAcoplada = nave
        observadores.forEach { it.alAcoplar(nave) }
        return true
    }

    fun acoplar(nave: Acoplable, posicion: Posicionable): Boolean =
        acoplar(nave, posicion.posX.toDouble(), posicion.posY.toDouble())

    override fun desacoplar(): Boolean {
        val nave = naveAcoplada ?: return false
        nave.desacoplar()
        naveAcoplada = null
        observadores.forEach { it.alDesacoplar(nave) }
        return true
    }

    fun registrarObservador(observador: ObservadorAcople) {
        observadores.add(observador)
    }

    fun eliminarObservador(observador: ObservadorAcople) {
        observadores.remove(observador)
    }
}
