package submarino

/**
 * ============================================================================
 * GESTIÓN DE ZONA DE ACOPLE Y ESTACIÓN MARÍTIMA (ISSUE #7)
 * PRINCIPIOS SOLID (SRP, OCP, DIP, ISP)
 * ============================================================================
 */

/**
 * Delimita la zona de superficie natural del agua (profundidad Y <= profundidadMaxima).
 * Cumple con SRP y OCP al desacoplar la topología espacial de la estación.
 *
 * @param profundidadMaxima Profundidad máxima en metros para considerar que una nave está en superficie (default 0.0).
 */
class ZonaSuperficie(
    val profundidadMaxima: Double = 0.0
) : ZonaAcople {
    override fun estaEnZona(x: Double, y: Double): Boolean = y <= profundidadMaxima
}

/**
 * Delimita una plataforma o muelle flotante acotado horizontalmente en el eje X y en la superficie (Y).
 * Demuestra OCP al agregar geometrías de amarre sin modificar clases existentes.
 *
 * @param xMin Coordenada X mínima del muelle.
 * @param xMax Coordenada X máxima del muelle.
 * @param profundidadMaxima Profundidad vertical máxima admisible para el amarre.
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
}

/**
 * Gestión de la estación de acople en la superficie marina.
 *
 * Cumple con:
 * - Single Responsibility Principle (SRP): se encarga exclusivamente de administrar
 *   el estado de amarre de naves y verificar compatibilidad espacial de acople.
 * - Dependency Inversion Principle (DIP): inyecta [ZonaAcople] como abstracción en el constructor.
 * - Interface Segregation Principle (ISP): implementa [ControlAcople] y [ConsultaAcople].
 * - Patrón Observer: desacopla avisos de amarre para futuras interfaces gráficas.
 *
 * @param zonaAcople Abstracción del área espacial admisible para el amarre.
 */
class EstacionAcople(
    val zonaAcople: ZonaAcople = ZonaSuperficie()
) : ControlAcople, ConsultaAcople {

    private val observadores: MutableList<ObservadorAcople> = mutableListOf()

    var naveAcoplada: Acoplable? = null
        private set

    override val hayNaveAcoplada: Boolean
        get() = naveAcoplada != null

    /**
     * Verifica si una posición en coordenadas (x, y) es apta para acople y la estación está disponible.
     *
     * @param x Coordenada horizontal.
     * @param y Coordenada vertical (profundidad).
     * @return `true` si la estación está libre y las coordenadas están en la zona de acople.
     */
    override fun puedeAcoplar(x: Double, y: Double): Boolean {
        return !hayNaveAcoplada && zonaAcople.estaEnZona(x, y)
    }

    /**
     * Intenta acoplar una nave en la estación verificando su posición en (x, y).
     *
     * @param nave Abstracción [Acoplable] de la nave.
     * @param x Coordenada horizontal actual de la nave.
     * @param y Coordenada vertical actual de la nave.
     * @return `true` si el acople fue concretado exitosamente; `false` si no está en zona o la estación está ocupada.
     */
    override fun acoplar(nave: Acoplable, x: Double, y: Double): Boolean {
        if (!puedeAcoplar(x, y)) {
            return false
        }
        nave.acoplar()
        naveAcoplada = nave
        observadores.forEach { it.alAcoplar(nave) }
        return true
    }

    /**
     * Sobrecarga polimórfica para acoplar utilizando una entidad con contrato [Posicionable].
     */
    fun acoplar(nave: Acoplable, posicion: Posicionable): Boolean =
        acoplar(nave, posicion.posX.toDouble(), posicion.posY.toDouble())

    /**
     * Desacopla la nave actualmente amarrada en la estación.
     *
     * @return `true` si había una nave amarrada y fue liberada; `false` si la estación ya estaba vacía.
     */
    override fun desacoplar(): Boolean {
        val nave = naveAcoplada ?: return false
        nave.desacoplar()
        naveAcoplada = null
        observadores.forEach { it.alDesacoplar(nave) }
        return true
    }

    /**
     * Registra un observador para notificaciones de amarre/desamarre (Patrón Observer / UI desacoplada).
     */
    fun registrarObservador(observador: ObservadorAcople) {
        observadores.add(observador)
    }

    /**
     * Remueve un observador previamente registrado.
     */
    fun eliminarObservador(observador: ObservadorAcople) {
        observadores.remove(observador)
    }
}
