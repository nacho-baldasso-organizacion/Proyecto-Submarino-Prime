package submarino

/**
 * ============================================================================
 * GESTIÓN DE ZONA DE ACOPLE Y ESTACIÓN MARÍTIMA (ISSUE #7)
 * PRINCIPIOS SOLID (SRP, OCP, DIP, ISP)
 * ============================================================================
 */

/**
 * Delimita la zona de superficie natural del agua (profundidad Y <= profundidadMaxima).
 * Encapsula sus campos con 'private set' y aporta métodos de dominio (no anémica).
 */
class ZonaSuperficie(
    profundidadMaxima: Double = 0.0
) : ZonaAcople {

    var profundidadMaxima: Double = profundidadMaxima
        private set

    /**
     * Indica si la coordenada vertical corresponde a la superficie del agua.
     */
    fun estaEnSuperficie(y: Double): Boolean = y <= profundidadMaxima

    /**
     * Indica si una entidad posicionable se encuentra dentro de esta zona.
     */
    fun contiene(posicion: Posicionable): Boolean = estaEnSuperficie(posicion.posY.toDouble())

    override fun estaEnZona(x: Double, y: Double): Boolean = estaEnSuperficie(y)

    fun descripcion(): String = "Zona Superficie (<= ${profundidadMaxima}m)"

    fun margenProfundidad(y: Double): Double = profundidadMaxima - y

    fun esCercanoASuperficie(y: Double, tolerancia: Double = 5.0): Boolean =
        y <= profundidadMaxima + tolerancia
}

/**
 * Delimita una plataforma o muelle flotante acotado horizontalmente en el eje X y en la superficie (Y).
 */
class ZonaAcoplePlataforma(
    xMin: Double,
    xMax: Double,
    profundidadMaxima: Double = 0.0
) : ZonaAcople {

    init {
        require(xMin <= xMax) { "xMin ($xMin) no puede ser mayor que xMax ($xMax)" }
    }

    var xMin: Double = xMin
        private set

    var xMax: Double = xMax
        private set

    var profundidadMaxima: Double = profundidadMaxima
        private set

    /**
     * Indica si la coordenada vertical corresponde a la superficie del agua.
     */
    fun estaEnSuperficie(y: Double): Boolean = y <= profundidadMaxima

    /**
     * Indica si la coordenada horizontal se encuentra dentro del ancho de la plataforma.
     */
    fun estaDentroDelAncho(x: Double): Boolean = x in xMin..xMax

    /**
     * Indica si una entidad posicionable se encuentra dentro de esta plataforma.
     */
    fun contiene(posicion: Posicionable): Boolean =
        estaEnSuperficie(posicion.posY.toDouble()) && estaDentroDelAncho(posicion.posX.toDouble())

    override fun estaEnZona(x: Double, y: Double): Boolean =
        estaEnSuperficie(y) && estaDentroDelAncho(x)

    fun anchoPlataforma(): Double = xMax - xMin

    fun centroPlataforma(): Double = (xMin + xMax) / 2.0

    fun distanciaAlCentro(x: Double): Double = kotlin.math.abs(x - centroPlataforma())

    fun descripcion(): String = "Plataforma X: [$xMin, $xMax] en superficie (<= ${profundidadMaxima}m)"
}

/**
 * Gestión de la estación de acople en la superficie marina.
 *
 * Cumple con:
 * - Single Responsibility Principle (SRP)
 * - Dependency Inversion Principle (DIP)
 * - Interface Segregation Principle (ISP)
 * - Encapsulación estricta (campos privados o 'private set')
 * - Patrón Observer
 */
class EstacionAcople(
    zonaAcople: ZonaAcople = ZonaSuperficie()
) : ControlAcople, ConsultaAcople {

    var zonaAcople: ZonaAcople = zonaAcople
        private set

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

    /**
     * Indica si la estación no tiene ninguna nave amarrada.
     */
    fun estaDisponible(): Boolean = !hayNaveAcoplada

    /**
     * Indica si la estación ya tiene una nave amarrada.
     */
    fun estaOcupada(): Boolean = hayNaveAcoplada

    /**
     * Determina si las coordenadas dadas pertenecen a la zona de acople configurada.
     */
    fun estaEnZonaDeAcople(x: Double, y: Double): Boolean = zonaAcople.estaEnZona(x, y)

    /**
     * Determina si la posición de una entidad pertenece a la zona de acople configurada.
     */
    fun estaEnZonaDeAcople(posicion: Posicionable): Boolean =
        estaEnZonaDeAcople(posicion.posX.toDouble(), posicion.posY.toDouble())

    override fun puedeAcoplar(x: Double, y: Double): Boolean =
        estaDisponible() && estaEnZonaDeAcople(x, y)

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

    fun estadoEstacion(): String = if (estaOcupada()) "Ocupada por nave amarrada" else "Disponible"

    fun cantidadObservadores(): Int = observadores.size
}
