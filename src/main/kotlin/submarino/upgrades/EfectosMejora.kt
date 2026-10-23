package submarino

/**
 * ============================================================================
 * EFECTOS DE MEJORAS SOBRE ESTADÍSTICAS DEL SUBMARINO (ISSUE #8)
 * PRINCIPIOS SOLID (SRP, OCP, LSP, DRY)
 * ============================================================================
 */

/**
 * Clase base común para todos los efectos cuantitativos de mejora.
 * Elimina la duplicación de validación y almacenamiento de magnitud (DRY).
 */
abstract class EfectoBase(
    override val magnitud: Double
) : EfectoMejora {

    init {
        require(magnitud > 0.0) {
            "La magnitud del incremento debe ser estrictamente positiva: $magnitud"
        }
    }
}

/**
 * Efecto de mejora que incrementa la capacidad máxima de carga en la bodega del submarino.
 */
class EfectoCapacidadBodega(
    magnitud: Double
) : EfectoBase(magnitud) {

    override fun aplicar(submarino: Submarino) {
        submarino.aumentarCapacidadBodega(magnitud)
    }

    override fun toString(): String = "EfectoCapacidadBodega(+${magnitud}kg bodega)"
}

/**
 * Efecto de mejora que incrementa la velocidad nominal base de ascenso vertical del submarino.
 */
class EfectoVelocidadAscenso(
    magnitud: Double
) : EfectoBase(magnitud) {

    override fun aplicar(submarino: Submarino) {
        submarino.aumentarVelocidadAscenso(magnitud)
    }

    override fun toString(): String = "EfectoVelocidadAscenso(+${magnitud}m/s ascenso)"
}

/**
 * Efecto de mejora que refuerza el casco aumentando la presión máxima admisible antes de colapsar.
 */
class EfectoResistenciaCasco(
    magnitud: Double
) : EfectoBase(magnitud) {

    override fun aplicar(submarino: Submarino) {
        submarino.aumentarResistenciaCasco(magnitud)
    }

    override fun toString(): String = "EfectoResistenciaCasco(+${magnitud}Pa resistencia casco)"
}

/**
 * Efecto de mejora que incrementa la velocidad del motor para desplazamiento y navegación horizontal.
 */
class EfectoVelocidadPropulsion(
    magnitud: Double
) : EfectoBase(magnitud) {

    override fun aplicar(submarino: Submarino) {
        submarino.aumentarVelocidadPropulsion(magnitud)
    }

    override fun toString(): String = "EfectoVelocidadPropulsion(+${magnitud}m/s propulsion)"
}
