package submarino

/**
 * Formato numerico compartido por los reportes de simulacion (SRP).
 */
object FormatoSimulacion {

    fun miles(valor: Double): String = "%,.2f".format(valor)

    fun decimales(valor: Double): String = "%.2f".format(valor)
}

/**
 * Presentacion de las secciones generales de la simulacion integrada (SRP).
 */
class ReporteSimulacionSubmarino {

    fun encabezado() {
        VisualizadorConsola.imprimirEncabezado(
            "SIMULADOR DE SUBMARINO - PRUEBA DE INTEGRACION POO LIMPIA\n" +
                "                 (Issues #1, #2 y #3: Navegacion, Fisica y Casco)"
        )
    }

    fun constantesFisicas() {
        VisualizadorConsola.imprimirSeccion("1. FISICA HIDROSTATICA DEL ENTORNO (Issue #2)")
        println("  - Densidad del agua de mar : ${CalculadoraHidrostatica.DENSIDAD_AGUA_MAR} kg/m3")
        println("  - Aceleracion de gravedad  : ${CalculadoraHidrostatica.GRAVEDAD} m/s2")
        println("  - Presion atmosferica base : ${FormatoSimulacion.miles(CalculadoraHidrostatica.PRESION_ATMOSFERICA)} Pa")
        println("  - Presion en superficie    : ${FormatoSimulacion.miles(CalculadoraHidrostatica.calcularPresion(0.0))} Pa\n")
    }

    fun estadoInicial(submarino: Submarino, profMaxSegura: Double) {
        VisualizadorConsola.imprimirSeccion("2. INICIALIZACION DE LA NAVE Y MODULO CASCO (Issues #1 y #3)")
        println("  - Presion maxima admisible del casco: ${FormatoSimulacion.miles(submarino.casco.presionMaximaPa)} Pa")
        println("  - Profundidad maxima segura teorica : ${FormatoSimulacion.decimales(profMaxSegura)} metros")
        println("  - Integridad inicial del casco      : ${FormatoSimulacion.decimales(submarino.casco.integridad)}%")
        println("  - Posicion inicial                  : (X: ${submarino.posX} m, Y: ${submarino.posY} m)")
        println("  - Bateria inicial                   : ${FormatoSimulacion.decimales(submarino.bateria)}%")
        println("  - Estado inicial destruido          : ${submarino.estaDestruido}\n")
    }

    fun cierre() {
        VisualizadorConsola.imprimirCierre("TODOS LOS SISTEMAS VALIDADOS CON EXITO")
    }
}

/**
 * Presentacion del escenario de navegacion (SRP).
 */
class ReporteNavegacionSimulacion {

    fun seccion() {
        VisualizadorConsola.imprimirSeccion("A) PRUEBA DE NAVEGACION, PROPULSION Y LIMITES FISICOS (Issue #1)")
    }

    fun maniobraHorizontal(submarino: Submarino) {
        println("-> Maniobra 1: Desplazamiento horizontal (+30.0 m en eje X)")
        println("   Posicion actual : (X: ${submarino.posX} m, Y: ${submarino.posY} m)")
        println("   Bateria restante: ${FormatoSimulacion.decimales(submarino.bateria)}% (Consumo de 0.5% por unidad)")
    }

    fun intentoAscensoEnSuperficie(submarino: Submarino) {
        println("-> Maniobra 2: Intentar ascender 15 m estando en superficie (posY = 0)")
        println("   Posicion actual : (X: ${submarino.posX} m, Y: ${submarino.posY} m) [Se mantiene en superficie Y >= 0]")
        println("   Bateria restante: ${FormatoSimulacion.decimales(submarino.bateria)}%\n")
    }
}

/**
 * Presentacion de los escenarios de inmersion segura y critica (SRP).
 */
class ReporteInmersionSimulacion {

    fun seccionSegura(profMaxSegura: Double) {
        VisualizadorConsola.imprimirSeccion("B) INMERSION SEGURA DENTRO DEL LIMITE DE PRESION DEL CASCO (Issues #1, #2 y #3)")
        println("-> Inmersion controlada a 20.0 m de profundidad (Limite seguro: ${FormatoSimulacion.decimales(profMaxSegura)} m)")
    }

    fun inmersionControlada(submarino: Submarino, presion: Double) {
        println("   Profundidad : ${submarino.posY} m")
        println("   Presion Pa  : ${FormatoSimulacion.miles(presion)} Pa (Max: ${FormatoSimulacion.miles(submarino.casco.presionMaximaPa)} Pa)")
        println("   Integridad  : ${FormatoSimulacion.decimales(submarino.casco.integridad)}% [Sin dano: presion dentro del limite]")
        println("   Destruido   : ${submarino.estaDestruido}")
        println("   Bateria     : ${FormatoSimulacion.decimales(submarino.bateria)}%")
        println("\n-> Inmersion profunda cercana al limite: descender 15.0 m mas (Profundidad: 35.0 m)")
    }

    fun inmersionProfunda(submarino: Submarino, presion: Double) {
        println("   Profundidad : ${submarino.posY} m")
        println("   Presion Pa  : ${FormatoSimulacion.miles(presion)} Pa (Margen restante: ${FormatoSimulacion.miles(submarino.casco.presionMaximaPa - presion)} Pa)")
        println("   Integridad  : ${FormatoSimulacion.decimales(submarino.casco.integridad)}% [Casco 100% integro]")
        println("   Destruido   : ${submarino.estaDestruido}\n")
    }

    fun seccionCritica() {
        VisualizadorConsola.imprimirSeccion("C) INMERSION CRITICA, PERDIDA DE INTEGRIDAD Y DESTRUCCION (Issue #3)")
    }

    fun configuracionCritica(cascoCritico: Casco) {
        val limiteCritico = CalculadoraHidrostatica.calcularProfundidadMaxima(cascoCritico.presionMaximaPa)
        println("  Configuracion de nave de prueba de estres:")
        println("   - Resistencia de casco : ${FormatoSimulacion.miles(cascoCritico.presionMaximaPa)} Pa (Profundidad max: ${FormatoSimulacion.decimales(limiteCritico)} m)")
        println("   - Integridad inicial   : ${FormatoSimulacion.decimales(cascoCritico.integridad)}%")
    }

    fun descensoCritico(subCritico: Submarino, presionCritica: Double, cascoCritico: Casco) {
        println("\n-> Descenso 1 a zona critica: 25.0 metros de profundidad")
        println("   Profundidad : ${subCritico.posY} m")
        println("   Presion Pa  : ${FormatoSimulacion.miles(presionCritica)} Pa (Exceso: +${FormatoSimulacion.miles(presionCritica - cascoCritico.presionMaximaPa)} Pa)")
        println("   Integridad  : ${FormatoSimulacion.decimales(subCritico.casco.integridad)}% [Perdida de integridad detectada]")
        println("   Destruido   : ${subCritico.estaDestruido}")
    }

    fun descensoAbisal(subCritico: Submarino, presionCritica: Double) {
        println("\n-> Descenso 2 a fosa abisal: descender 150.0 metros adicionales (Total: 175.0 m)")
        println("   Profundidad : ${subCritico.posY} m")
        println("   Presion Pa  : ${FormatoSimulacion.miles(presionCritica)} Pa")
        println("   Integridad  : ${FormatoSimulacion.decimales(subCritico.casco.integridad)}%")
        println("   Destruido   : ${subCritico.estaDestruido} [Nave destruida por implosion]")
    }

    fun bloqueoTrasDestruccion(subCritico: Submarino, posXAntes: Double) {
        println("\n-> Comprobacion de seguridad: intento de maniobra con nave destruida (+20.0 m en X)")
        println("   Posicion previa : (X: $posXAntes m, Y: ${subCritico.posY} m)")
        println("   Posicion actual : (X: ${subCritico.posX} m, Y: ${subCritico.posY} m) [Movimiento bloqueado exitosamente]\n")
    }
}

/**
 * Presentacion del escenario de validacion SOLID (SRP).
 */
class ReporteSolidSimulacion {

    fun seccion() {
        VisualizadorConsola.imprimirSeccion("D) VALIDACION DE PRINCIPIOS SOLID (OCP, DIP, ISP, SRP)")
    }

    fun cascoReforzado(subAvanzado: Submarino, presionExterna: Double, cascoBlindado: Casco) {
        println("-> 1. OCP & DIP: Submarino equipado con CascoReforzado (blindaje 50%)")
        println("   Profundidad actual  : ${subAvanzado.posY} m")
        println("   Presion recibida    : ${FormatoSimulacion.miles(presionExterna)} Pa")
        println("   Integridad blindada : ${FormatoSimulacion.decimales(cascoBlindado.integridad)}% (Dano reducido por absorcion)")
    }

    fun fisicaAguaDulce(presionDulce: Double, presionExterna: Double) {
        println("\n-> 2. OCP en Fisica: Simulacion en Agua Dulce (densidad = 1000.0 kg/m3)")
        println("   Presion a 30m en agua dulce: ${FormatoSimulacion.miles(presionDulce)} Pa (menor que en mar: ${FormatoSimulacion.miles(presionExterna)} Pa)\n")
    }
}
