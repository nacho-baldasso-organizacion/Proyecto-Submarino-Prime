package submarino

/**
 * Construye el catálogo de mejoras disponible durante una partida.
 *
 * Responsabilidad única (SRP): componer las mejoras estándar y la mejora
 * específica de oxígeno que solo existe en la sesión de juego.
 */
class CatalogoMejorasSesion {

    fun crear(aumentarOxigenoMaximo: (Double) -> Unit): CatalogoUpgrades {
        val catalogo = FabricaMejoras.crearCatalogoEstandar()
        catalogo.eliminar(FabricaMejoras.ID_ASCENSO)
        catalogo.registrar(crearMejoraOxigeno(aumentarOxigenoMaximo))
        return catalogo
    }

    private fun crearMejoraOxigeno(aumentarOxigenoMaximo: (Double) -> Unit): Mejora =
        FabricaDominio.crearMejora(
            id = ID_MEJORA_OXIGENO,
            nombre = "Tanque de Oxígeno Abisal",
            descripcion = "Amplía la reserva de oxígeno en +${INCREMENTO_OXIGENO_POR_MEJORA.toInt()} puntos por nivel.",
            nivelInicial = 1,
            nivelMaximo = 5,
            efecto = FabricaDominio.crearEfectoOxigeno(
                INCREMENTO_OXIGENO_POR_MEJORA,
                aumentarOxigenoMaximo
            ),
            estrategiaCosto =
                FabricaDominio.crearEstrategiaCostoLineal(
                    COSTO_BASE_OXIGENO,
                    INCREMENTO_COSTO_OXIGENO
                )
        )

    companion object {
        const val ID_MEJORA_OXIGENO = "UPG-OXIGENO"
        const val INCREMENTO_OXIGENO_POR_MEJORA = 20.0
        const val COSTO_BASE_OXIGENO = 150
        const val INCREMENTO_COSTO_OXIGENO = 85
    }
}
