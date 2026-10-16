package submarino

/**
 * ============================================================================
 * EFECTOS DE MEJORAS SOBRE ESTADÍSTICAS DEL SUBMARINO (ISSUE #8)
 * PRINCIPIOS SOLID (SRP = 0 violaciones, OCP = 0 violaciones, LSP = 0 violaciones)
 * ============================================================================
 */

/**
 * Efecto de mejora que incrementa la capacidad máxima de carga en la bodega del submarino.
 *
 * @param magnitud Kilogramos adicionales otorgados a la bodega por cada nivel adquirido.
 */
class EfectoCapacidadBodega(
    override val magnitud: Double
) : EfectoMejora {

    init {
        require(magnitud > 0.0) {
            "La magnitud del incremento de capacidad debe ser estrictamente positiva: $magnitud"
        }
    }

    override fun aplicar(submarino: Submarino) {
        submarino.aumentarCapacidadBodega(magnitud)
    }

    override fun toString(): String = "EfectoCapacidadBodega(+${magnitud}kg bodega)"
}

/**
 * Efecto de mejora que incrementa la velocidad nominal base de ascenso vertical del submarino.
 *
 * @param magnitud Metros por segundo (m/s) adicionales añadidos a la velocidad base vertical.
 */
class EfectoVelocidadAscenso(
    override val magnitud: Double
) : EfectoMejora {

    init {
        require(magnitud > 0.0) {
            "La magnitud del incremento de velocidad de ascenso debe ser estrictamente positiva: $magnitud"
        }
    }

    override fun aplicar(submarino: Submarino) {
        submarino.aumentarVelocidadAscenso(magnitud)
    }

    override fun toString(): String = "EfectoVelocidadAscenso(+${magnitud}m/s ascenso)"
}

/**
 * Efecto de mejora que refuerza el casco aumentando la presión máxima admisible antes de colapsar.
 *
 * @param magnitud Pascales (Pa) adicionales añadidos a la resistencia estructural del casco.
 */
class EfectoResistenciaCasco(
    override val magnitud: Double
) : EfectoMejora {

    init {
        require(magnitud > 0.0) {
            "La magnitud del incremento de resistencia debe ser estrictamente positiva: $magnitud"
        }
    }

    override fun aplicar(submarino: Submarino) {
        submarino.aumentarResistenciaCasco(magnitud)
    }

    override fun toString(): String = "EfectoResistenciaCasco(+${magnitud}Pa resistencia casco)"
}

/**
 * Efecto de mejora que incrementa la velocidad del motor para desplazamiento y navegación horizontal.
 *
 * @param magnitud Magnitud adicional de velocidad de propulsión horizontal.
 */
class EfectoVelocidadPropulsion(
    override val magnitud: Double
) : EfectoMejora {

    init {
        require(magnitud > 0.0) {
            "La magnitud del incremento de propulsión debe ser estrictamente positiva: $magnitud"
        }
    }

    override fun aplicar(submarino: Submarino) {
        submarino.aumentarVelocidadPropulsion(magnitud)
    }

    override fun toString(): String = "EfectoVelocidadPropulsion(+${magnitud}m/s propulsion)"
}
