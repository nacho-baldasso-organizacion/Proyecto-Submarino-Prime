package submarino

/**
 * Simula las maniobras de navegación y los intentos de recolección de minerales.
 */
class DemostradorNavegacionRecoleccion {

    fun demostrar(
        ancho: Int,
        alto: Int,
        minerales: MutableList<Recolectable>,
        bodega: Bodega,
        servicio: ServicioRecoleccionContrato
    ) {
        println("4. INICIALIZACIÓN DE LA BODEGA Y SERVICIO DE RECOLECCIÓN:")
        println("   Capacidad Máxima de Bodega: ${bodega.capacidadMaxima} kg")
        println("   Peso inicial: ${bodega.pesoActual} kg\n")

        println("5. SIMULACIÓN DE NAVEGACIÓN Y RECOLECCIÓN:")
        probarCentroMapa(ancho, alto, minerales, servicio)
        probarParedConMineral(minerales, servicio)
        probarReintentoMismaCoordenada(minerales, servicio)
        probarDesbordamientoBodega(minerales, bodega, servicio)
    }

    private fun probarCentroMapa(ancho: Int, alto: Int, minerales: List<Recolectable>, servicio: ServicioRecoleccionContrato) {
        val posCentro = Posicion(posX = ancho / 2, posY = alto / 2)
        println("   A) Submarino navega a posición central (${posCentro.posX}, ${posCentro.posY}):")
        val res = servicio.procesarRecoleccion(posCentro, minerales)
        println("      -> ${res.obtenerMensaje()}")
    }

    private fun probarParedConMineral(minerales: List<Recolectable>, servicio: ServicioRecoleccionContrato) {
        val mineral = minerales.first()
        val posMineral = Posicion(posX = mineral.posX, posY = mineral.posY)
        println("\n   B) Submarino navega a pared en (${posMineral.posX}, ${posMineral.posY}):")
        println("      Mineral objetivo: ${mineral.nombre} (Peso: ${mineral.peso} kg, Valor: $${mineral.valor})")
        val res = servicio.procesarRecoleccion(posMineral, minerales)
        println("      -> ${res.obtenerMensaje()}")
    }

    private fun probarReintentoMismaCoordenada(minerales: List<Recolectable>, servicio: ServicioRecoleccionContrato) {
        val mineral = minerales.first()
        val posMineral = Posicion(posX = mineral.posX, posY = mineral.posY)
        println("\n   C) Submarino intenta recolectar nuevamente en (${posMineral.posX}, ${posMineral.posY}):")
        val res = servicio.procesarRecoleccion(posMineral, minerales)
        println("      -> ${res.obtenerMensaje()}")
    }

    private fun probarDesbordamientoBodega(minerales: MutableList<Recolectable>, bodega: Bodega, servicio: ServicioRecoleccionContrato) {
        val artefactoPesado = Mineral(
            id = "MIN-ARTEF-TEST-99",
            tipo = TipoMineral.ARTEFACTO_HUNDIDO,
            posX = 0,
            posY = 0,
            recolectado = false
        )
        minerales.add(artefactoPesado)
        val posArtefacto = Posicion(posX = 0, posY = 0)

        println("\n   D) Submarino se traslada a caverna oculta en (0, 0) y detecta ${artefactoPesado.nombre} (Peso: ${artefactoPesado.peso} kg):")
        println("      Capacidad disponible en bodega antes del intento: ${bodega.capacidadDisponible} kg")
        val res = servicio.procesarRecoleccion(posArtefacto, minerales)
        println("      -> ${res.obtenerMensaje()}")
    }
}
