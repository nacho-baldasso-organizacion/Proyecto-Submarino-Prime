<!-- evaluacion:inicio -->
# Evaluación — nacho-baldasso-organizacion/Proyecto-Submarino-Prime

> Documento **generado automáticamente** por la cátedra de Programación II (RepoSense + análisis de calidad POO/SOLID). **No editar a mano**: se regenera en cada evaluación.
> Rama evaluada: `main`. Análisis **heurístico** (regex + métricas): es una criba para orientar la revisión, **no una nota**.
> Generado: 2026-11-25.

## 1. Veredicto

| Aspecto | Resultado |
|---|---|
| Índice de calidad POO/SOLID | **85/100** |
| Commit evaluado | `4be405da9f69` (2026-11-20) |
| Código (LOC) | 14,144 |
| Archivos de código | 192 |
| Clases (bien usadas) | 332 (218 OK) |
| Código dentro de clases (%POO) | 99% |
| Funciones fuera de clases | 0 |
| Duplicación lógica | 3.0% |
| Señales S/O/L/I/D | 19/0/0/2/14 |

> Este informe **describe** lo que el análisis midió con los criterios ya establecidos de la materia. No es una nota.

### Semáforo de aprobación

Criterios de `CRITERIOS-CALIDAD.md`. **Aprobar** se juega con el perfil `estandar`; **destacar**, con el `exigente` (meta de excelencia).

| Semáforo | Resultado |
|---|---|
| Aprobar (`estandar`) | **no cumple** — 3/4 |
| Destacar (`exigente`) | **no cumple** — 4/5 |

**Criterios para aprobar**

| Criterio | Requisito | Tu resultado | Cumple |
|---|---|---|---|
| Código dentro de clases (%POO) | ≥ 85% | 99% | sí |
| Duplicación lógica | ≤ 10% | 3.0% | sí |
| Funciones fuera de clases | ≤ 1 | 0 | sí |
| Señales POO/SOLID críticas | ninguna | Baja cohesión (LCOM), Acoplamiento alto (CBO), Interfaz con demasiados métodos (ISP) | no |

**Criterios para destacar**

| Criterio | Requisito | Tu resultado | Cumple |
|---|---|---|---|
| Código dentro de clases (%POO) | ≥ 95% | 99% | sí |
| Duplicación lógica | ≤ 5% | 3.0% | sí |
| Funciones fuera de clases | ≤ 1 | 0 | sí |
| Señales POO/SOLID críticas | ninguna | Baja cohesión (LCOM), Complejidad alta, Acoplamiento alto (CBO), Interfaz con demasiados métodos (ISP) | no |
| Índice de calidad POO/SOLID | ≥ 60 | 85/100 | sí |

### Cómo se calcula el %POO (y por qué no sube solo)

`%POO = líneas de lógica dentro de clases ÷ líneas de lógica totales`.

Se cuentan las líneas de **lógica y campos**; no cuentan `package`/`import`/`using`/anotaciones ni comentarios, Javadoc o docstrings. Es una **proporción**: si agregás código nuevo dentro de una clase, el numerador y el denominador crecen juntos y el porcentaje casi no cambia.

Sube cuando el código que hoy está **fuera** de clases pasa a ser método de una clase (o desaparece): funciones sueltas, `main`, constantes de módulo, scripts y lógica de archivos sueltos. También importa que ese trabajo esté **integrado en `main`**: la evaluación siempre mide `main`, así que lo que sigue en otra rama no se refleja todavía.

## 2. Autoría (RepoSense)

| Autor | Commits | Código (LOC) | Tests (LOC) | Docs (LOC) | Días activos | % del código |
|---|---:|---:|---:|---:|---:|---:|
| ibaldasso-elpepe | 30 | 13,525 | 2,974 | 1 | 17 | 100% |

> _El LOC de autoría lo calcula RepoSense sobre el historial y puede diferir del LOC de código del análisis de calidad (que mira solo el estado de `main`)._

## 3. Fallas detectadas

Reúne lo que el análisis marcó y lo que todavía falta para cada semáforo. Cuando la señal se puede asociar a archivos, se indican para orientar la revisión. No es una nota.

| Falla | Tu resultado | Requisito | Archivos |
|---|---|---|---|
| Señales POO/SOLID críticas | Baja cohesión (LCOM), Acoplamiento alto (CBO), Interfaz con demasiados métodos (ISP) | Aprobar: ninguna<br>Destacar: ninguna | `src/main/kotlin/org/example/issue1/FabricaSubsistemasSubmarino.kt`, `src/main/kotlin/org/example/issue1/GestorMejorasSubmarino.kt`, `src/main/kotlin/org/example/issue1/NavegadorSubmarino.kt`, `src/main/kotlin/org/example/issue1/Submarino.kt`, `src/main/kotlin/submarino/Bodega.kt`, `src/main/kotlin/submarino/ControladorTeclado.kt` (+26 más) |
| Métodos/funciones largos o con muchos parámetros | **34** método(s)/función(es) largos o con muchos parámetros | Sin señales | `src/main/kotlin/submarino/AvatarSubmarino.kt`, `src/main/kotlin/submarino/CapaFondoParallax.kt`, `src/main/kotlin/submarino/ConstructorAccionesTienda.kt`, `src/main/kotlin/submarino/ConstructorExplosionSubmarino.kt`, `src/main/kotlin/submarino/ConstructorFilasMejoraTienda.kt`, `src/main/kotlin/submarino/CristalNodoMineralVectorial.kt` (+23 más) |

## 4. Aspectos evaluados

Cada aspecto indica **qué mide**, **por qué importa** y **tu resultado**. Cuando el análisis reportó una señal, se agregan preguntas para **investigar la causa** (no hay recetas: entender el porqué es parte de la evaluación).

### 1. Índice de calidad POO/SOLID (indice)

- **Qué mide:** Un resumen numérico (0-100) que combina la riqueza de clases bien usadas con la penalización por señales POO/SOLID y duplicación, y luego aplica topes duros.
- **Por qué importa:** Ordena y prioriza la revisión: un índice bajo indica varias señales que conviene mirar juntas.
- **Tu resultado:** índice **85/100**

### 2. Duplicación lógica (DRY) (duplicacion) — sin señales

- **Qué mide:** Porcentaje de bloques de LÓGICA repetidos. Quedan fuera imports/includes, anotaciones, declaraciones de campos y comentarios/Javadoc/docstrings: esos bloques son boilerplate, no lógica.
- **Por qué importa:** Cada copia de lógica se mantiene por separado: un arreglo hay que repetirlo y los defectos se multiplican.
- **Tu resultado:** **3.0%** de duplicación lógica

### 3. Adopción de POO (% de código en clases) (poo) — sin señales

- **Qué mide:** Proporción de las **líneas de lógica** que viven dentro de una clase. Quedan fuera `package`/`import`/`using`/anotaciones y comentarios/Javadoc/docstrings: no son código que pueda estar dentro de una clase y, si contaran, el porcentaje quedaría topado por el boilerplate de cada archivo.
- **Por qué importa:** El diseño orientado a objetos es el eje de la materia: el comportamiento debería vivir como método de una clase.
- **Tu resultado:** **99.0%** del código dentro de clases
- **Cómo leerlo:** Es una **proporción**, no un acumulado: agregar código dentro de una clase no la sube por sí solo (numerador y denominador crecen juntos). Sube cuando el código que está **fuera** de clases (funciones sueltas, `main`, constantes de módulo, scripts) pasa a ser método de una clase o deja de existir. La medición se hace sobre `main`: el trabajo que todavía vive en otra rama no se refleja hasta que se integra.

### 4. Funciones fuera de clases (sueltas) — sin señales

- **Qué mide:** Funciones de nivel de módulo que no pertenecen a ninguna clase (en esta materia el comportamiento debe ser método de una clase).
- **Por qué importa:** Son la forma más directa de código procedural en un proyecto que debería ser orientado a objetos.
- **Tu resultado:** **0** función(es) fuera de clases

### 5. Clase demasiado grande (SRP) (god) — sin señales

- **Qué mide:** Clases con demasiadas líneas o métodos de LÓGICA. No cuentan los getters/setters triviales ni la cantidad de campos por sí sola.
- **Por qué importa:** Una clase que hace de todo cambia por muchos motivos y cuesta entenderla y probarla.
- **Tu resultado:** **0** clase(s) demasiado grande(s)

### 6. Baja cohesión (LCOM) (cohesion) — con señales

- **Qué mide:** Métodos y campos de una clase que casi no se relacionan entre sí.
- **Por qué importa:** Una unidad que cambia por motivos distintos y comparte poca información suele esconder más de una responsabilidad.
- **Tu resultado:** **18** clase(s) con baja cohesión
- **Para investigar la causa:**
    - ¿Qué métodos de la clase no comparten estado con el resto?
    - ¿Cambian por motivos distintos según la tarea?
    - ¿Se pensó su responsabilidad antes de escribirla, o se fue agregando?

### 7. Complejidad alta (complejidad) — con señales

- **Qué mide:** Complejidad ciclomática: cantidad de caminos posibles dentro de un método o función (condicionales, bucles, casos).
- **Por qué importa:** Muchos caminos = difícil de seguir y de probar; suelen esconderse casos sin cubrir.
- **Tu resultado:** **1** método(s)/función(es) con complejidad alta
- **Para investigar la causa:**
    - ¿Se agregaron condiciones a medida que aparecían casos nuevos?
    - ¿Se probaron todos los caminos o solo el caso principal?
    - ¿Hay reglas de negocio mezcladas con detalles técnicos?

### 8. Métodos/funciones largos o con muchos parámetros (tamano) — con señales

- **Qué mide:** Tamaño de los métodos/funciones y cantidad de parámetros.
- **Por qué importa:** Una función que hace varias cosas cuesta leerla, nombrarla y reutilizarla.
- **Tu resultado:** **34** método(s)/función(es) largos o con muchos parámetros
- **Para investigar la causa:**
    - ¿La función creció por acumulación de pasos?
    - ¿Agrupa operaciones que podrían describirse por separado?
    - ¿Los parámetros representan una misma idea (un objeto) o cosas sueltas?

### 9. Acoplamiento alto (CBO) (acoplamiento) — con señales

- **Qué mide:** Cuánto depende una clase de otros tipos del proyecto. No cuentan los tipos de la plataforma (biblioteca estándar, DOM, motor Godot) ni los métodos/propiedades propias.
- **Por qué importa:** Piezas muy enlazadas hacen que un cambio se propague y que sea difícil reemplazar partes.
- **Tu resultado:** **14** clase(s) muy acoplada(s)
- **Para investigar la causa:**
    - ¿La clase conoce detalles internos de muchas otras?
    - ¿Se puede cambiar una parte sin tocar las demás?
    - ¿Los colaboradores llegan como parámetros o se crean por dentro?

### 10. Herencia profunda (DIT) (herencia) — sin señales

- **Qué mide:** Profundidad de la cadena de herencia de una clase.
- **Por qué importa:** Jerarquías muy profundas vuelven difícil saber de dónde viene cada comportamiento.
- **Tu resultado:** **0** clase(s) con herencia profunda

### 11. Switch sobre tipos (OCP) (ocp) — sin señales

- **Qué mide:** Cadenas de dispatch por tipo (`else if ... is` / `else if ... instanceof`, `switch` sobre tipo o `typeof`). Un `is Tipo variable` aislado (pattern matching / cast seguro) NO cuenta como switch.
- **Por qué importa:** Cada tipo nuevo obliga a tocar varios lugares, en vez de extender con una implementación nueva.
- **Tu resultado:** **0** clase(s) con switch sobre tipos

### 12. Interfaz con demasiados métodos (ISP) (isp) — con señales

- **Qué mide:** Interfaces o clases abstractas con muchos métodos declarados.
- **Por qué importa:** Obliga a implementar operaciones que no se usan y acopla a los clientes a métodos que no necesitan.
- **Tu resultado:** **2** interfaz(es) con demasiados métodos
- **Para investigar la causa:**
    - ¿Qué subconjunto de métodos usa realmente cada implementación?
    - ¿Se agruparon métodos de necesidades distintas en la misma interfaz?
    - ¿Hay implementaciones que dejan métodos sin uso?

### 13. Override vacío o 'no implementado' (LSP) (lsp) — sin señales

- **Qué mide:** Métodos sobrescritos que quedan vacíos o lanzan 'no implementado'.
- **Por qué importa:** Suele indicar que la jerarquía no representa bien el problema y rompe la sustitución entre clases.
- **Tu resultado:** **0** override(s) vacío(s) o 'no implementado'

### 14. Clase sin métodos (solo datos) (data) — sin señales

- **Qué mide:** Clases que solo acumulan campos y no tienen comportamiento.
- **Por qué importa:** Separar datos de comportamiento reparte por el proyecto la lógica que debería estar cerca de esos datos.
- **Tu resultado:** **0** clase(s) sin métodos (solo datos)

### 15. Campos mutables públicos (encapsulación) (encapsulacion) — sin señales

- **Qué mide:** Clases que exponen estado **modificable** desde afuera. No cuentan constantes (`static final`, `const`), campos `final`/`readonly`, propiedades `val` de Kotlin ni propiedades con `private set`.
- **Por qué importa:** Expone el estado interno y hace que cualquier cambio dependa de quién lo modifica desde afuera.
- **Tu resultado:** **0** clase(s) con campos mutables públicos

### 16. Clase solo con métodos estáticos (utilitaria) (static) — sin señales

- **Qué mide:** Clases donde todos los métodos son estáticos.
- **Por qué importa:** Es comportamiento procedural disfrazado de clase: no se instancia ni participa del modelo de objetos.
- **Tu resultado:** **0** clase(s) solo con métodos estáticos

### Resumen de señales por tipo

| Señal | Cantidad | Principio | Archivos |
|---|---:|---|---:|
| método largo | 27 | S | 25 |
| baja cohesión | 18 | S | 17 |
| acoplamiento alto | 14 | D | 14 |
| muchos parámetros | 7 | S | 7 |
| bloque duplicado | 5 | DRY | 2 |
| interfaz grande | 2 | I | 2 |
| duplicación | 1 | DRY | 0 |
| complejidad alta | 1 | S | 1 |

### Resumen por archivo

> Archivos ordenados por cantidad de señales. Se excluyen **1** señal(es) de nivel repositorio sin archivo propio.

| Archivo | Señales | Aspectos | Líneas |
|---|---:|---|---|
| `build.gradle.kts` | 4 | DRY | 25, 40, 41, 42 |
| `src/main/kotlin/submarino/GeneradorTerrenoSubmarino.kt` | 3 | tamaño | 29, 82 |
| `src/main/kotlin/org/example/issue1/Submarino.kt` | 2 | acoplamiento, cohesión | 24 |
| `src/main/kotlin/submarino/ControladorTeclado.kt` | 2 | cohesión | 13, 107 |
| `src/main/kotlin/submarino/DetallesRoca.kt` | 2 | tamaño | 50, 102 |
| `src/main/kotlin/submarino/EstadoPartida.kt` | 2 | tamaño | 49 |
| `src/main/kotlin/submarino/PanelReglasMenuPrincipal.kt` | 2 | cohesión, tamaño | 14, 28 |
| `src/main/kotlin/submarino/PincelCordillera.kt` | 2 | tamaño | 26 |
| `src/main/kotlin/submarino/VistaJuego.kt` | 2 | acoplamiento, cohesión | 19 |
| `src/main/kotlin/submarino/upgrades/ServicioUpgrades.kt` | 2 | acoplamiento, tamaño | 14, 30 |
| `src/main/kotlin/org/example/issue1/FabricaSubsistemasSubmarino.kt` | 1 | acoplamiento | 69 |
| `src/main/kotlin/org/example/issue1/GestorMejorasSubmarino.kt` | 1 | cohesión | 10 |
| `src/main/kotlin/org/example/issue1/NavegadorSubmarino.kt` | 1 | cohesión | 9 |
| `src/main/kotlin/submarino/AvatarSubmarino.kt` | 1 | tamaño | 21 |
| `src/main/kotlin/submarino/Bodega.kt` | 1 | cohesión | 13 |
| `src/main/kotlin/submarino/CapaFondoParallax.kt` | 1 | tamaño | 22 |
| `src/main/kotlin/submarino/ConstructorAccionesTienda.kt` | 1 | tamaño | 18 |
| `src/main/kotlin/submarino/ConstructorExplosionSubmarino.kt` | 1 | tamaño | 17 |
| `src/main/kotlin/submarino/ConstructorFilasMejoraTienda.kt` | 1 | tamaño | 30 |
| `src/main/kotlin/submarino/CristalNodoMineralVectorial.kt` | 1 | tamaño | 14 |
| `src/main/kotlin/submarino/CuerpoSubmarinoVectorial.kt` | 1 | tamaño | 19 |
| `src/main/kotlin/submarino/DemostradorNavegacionRecoleccion.kt` | 1 | tamaño | 8 |
| `src/main/kotlin/submarino/EstadoJuego.kt` | 1 | cohesión | 9 |
| `src/main/kotlin/submarino/FabricaDominio.kt` | 1 | acoplamiento | 10 |
| `src/main/kotlin/submarino/FabricaHUD.kt` | 1 | acoplamiento | 14 |
| `src/main/kotlin/submarino/FabricaPecesMarinos.kt` | 1 | tamaño | 16 |
| `src/main/kotlin/submarino/FabricaServicios.kt` | 1 | acoplamiento | 7 |
| `src/main/kotlin/submarino/FabricaVisual.kt` | 1 | acoplamiento | 15 |
| `src/main/kotlin/submarino/FisicaAscenso.kt` | 1 | cohesión | 15 |
| `src/main/kotlin/submarino/FondoAbisal.kt` | 1 | tamaño | 35 |
| `src/main/kotlin/submarino/GameOverEstilos.kt` | 1 | DRY | 41 |
| `src/main/kotlin/submarino/HUDJuego.kt` | 1 | acoplamiento | 15 |
| `src/main/kotlin/submarino/HUDView.kt` | 1 | cohesión | 38 |
| `src/main/kotlin/submarino/HeliceSubmarinoVectorial.kt` | 1 | tamaño | 12 |
| `src/main/kotlin/submarino/Mineral.kt` | 1 | cohesión | 71 |
| `src/main/kotlin/submarino/ModeloNavegacion.kt` | 1 | cohesión | 33 |
| `src/main/kotlin/submarino/ParticulasMarinas.kt` | 1 | cohesión | 14 |
| `src/main/kotlin/submarino/PecesMarinos.kt` | 1 | cohesión | 14 |
| `src/main/kotlin/submarino/PincelArrecifes.kt` | 1 | tamaño | 24 |
| `src/main/kotlin/submarino/PincelFormacionRocosa.kt` | 1 | tamaño | 22 |
| `src/main/kotlin/submarino/PintorPecesMarinos.kt` | 1 | tamaño | 21 |
| `src/main/kotlin/submarino/PlataformaEstacionVectorial.kt` | 1 | tamaño | 14 |
| `src/main/kotlin/submarino/ResultadoRecoleccion.kt` | 1 | ISP | 8 |
| `src/main/kotlin/submarino/RocaNodoMineralVectorial.kt` | 1 | tamaño | 15 |
| `src/main/kotlin/submarino/SesionJuego.kt` | 1 | acoplamiento | 11 |
| `src/main/kotlin/submarino/SubmarinoApp.kt` | 1 | tamaño | 54 |
| `src/main/kotlin/submarino/TarjetaHUD.kt` | 1 | cohesión | 13 |
| `src/main/kotlin/submarino/TorreEstacionVectorial.kt` | 1 | tamaño | 17 |
| `src/main/kotlin/submarino/VistaSubmarino.kt` | 1 | acoplamiento | 23 |
| `src/main/kotlin/submarino/comercio/EstacionAcople.kt` | 1 | cohesión | 101 |
| `src/main/kotlin/submarino/comercio/ServicioComercio.kt` | 1 | acoplamiento | 117 |
| `src/main/kotlin/submarino/upgrades/FabricaMejoras.kt` | 1 | acoplamiento | 59 |
| `src/main/kotlin/submarino/upgrades/Mejora.kt` | 1 | cohesión | 49 |
| `src/main/kotlin/submarino/upgrades/ResultadoCompra.kt` | 1 | ISP | 8 |
| `src/test/kotlin/submarino/FabricaDatosPrueba.kt` | 1 | acoplamiento | 8 |
| `src/test/kotlin/submarino/Issue5Test.kt` | 1 | tamaño | 124 |
| `src/test/kotlin/submarino/Issue6EstrategiasAscensoTest.kt` | 1 | tamaño | 43 |
| `src/test/kotlin/submarino/Issue7ComercioTest.kt` | 1 | tamaño | 111 |
| `src/test/kotlin/submarino/Issue7ObservadoresTest.kt` | 1 | complejidad | 46 |
| `src/test/kotlin/submarino/Issue8ComprasTest.kt` | 1 | tamaño | 152 |
| `src/test/kotlin/submarino/Issue8ObservadoresTest.kt` | 1 | tamaño | 15 |

### Archivos donde se originan las señales

> Se listan **75** hallazgo(s) localizados en **61** archivo(s). La columna `Línea` es la línea del archivo donde el análisis detectó la señal; `—` indica una señal de nivel repositorio (sin archivo propio).

| Aspecto | Archivo | Línea | Hallazgo |
|---|---|---:|---|
| Duplicación lógica (DRY) | — | — | duplicación lógica: 3.0% · boilerplate: 2.7% |
| Duplicación lógica (DRY) | `build.gradle.kts` | 25 | bloque de 8 líneas repetido 4 veces en build.gradle.kts:25, build.gradle.kts:43, build.gradle.kts:50 |
| Duplicación lógica (DRY) | `build.gradle.kts` | 40 | bloque de 8 líneas repetido 3 veces en build.gradle.kts:40, build.gradle.kts:47, build.gradle.kts:54 |
| Duplicación lógica (DRY) | `build.gradle.kts` | 41 | bloque de 8 líneas repetido 3 veces en build.gradle.kts:41, build.gradle.kts:48, build.gradle.kts:55 |
| Duplicación lógica (DRY) | `build.gradle.kts` | 42 | bloque de 8 líneas repetido 3 veces en build.gradle.kts:42, build.gradle.kts:49, build.gradle.kts:56 |
| Duplicación lógica (DRY) | `src/main/kotlin/submarino/GameOverEstilos.kt` | 41 | bloque de 8 líneas repetido 14 veces en src/main/kotlin/submarino/GameOverEstilos.kt:41, src/main/kotlin/submarino/GameOverEstilos.kt:52, src/main/kotlin/submarino/GameOverEstilos.kt:63 |
| Baja cohesión (LCOM) | `src/main/kotlin/org/example/issue1/GestorMejorasSubmarino.kt` | 10 | clase 'GestorMejorasSubmarino' con baja cohesión (0%) |
| Baja cohesión (LCOM) | `src/main/kotlin/org/example/issue1/NavegadorSubmarino.kt` | 9 | clase 'NavegadorSubmarino' con baja cohesión (10%) |
| Baja cohesión (LCOM) | `src/main/kotlin/org/example/issue1/Submarino.kt` | 24 | clase 'Submarino' con baja cohesión (12%) |
| Baja cohesión (LCOM) | `src/main/kotlin/submarino/Bodega.kt` | 13 | clase 'Bodega' con baja cohesión (36%) |
| Baja cohesión (LCOM) | `src/main/kotlin/submarino/ControladorTeclado.kt` | 13 | clase 'ControladorMovimientoTeclado' con baja cohesión (20%) |
| Baja cohesión (LCOM) | `src/main/kotlin/submarino/ControladorTeclado.kt` | 107 | clase 'ControladorAccionesTeclado' con baja cohesión (30%) |
| Baja cohesión (LCOM) | `src/main/kotlin/submarino/EstadoJuego.kt` | 9 | clase 'EstadoJuego' con baja cohesión (10%) |
| Baja cohesión (LCOM) | `src/main/kotlin/submarino/FisicaAscenso.kt` | 15 | clase 'EstadoAscenso' con baja cohesión (49%) |
| Baja cohesión (LCOM) | `src/main/kotlin/submarino/HUDView.kt` | 38 | clase 'EstadoHUD' con baja cohesión (40%) |
| Baja cohesión (LCOM) | `src/main/kotlin/submarino/Mineral.kt` | 71 | clase 'Mineral' con baja cohesión (22%) |
| Baja cohesión (LCOM) | `src/main/kotlin/submarino/ModeloNavegacion.kt` | 33 | clase 'EstadoNavegacion' con baja cohesión (10%) |
| Baja cohesión (LCOM) | `src/main/kotlin/submarino/PanelReglasMenuPrincipal.kt` | 14 | clase 'PanelReglasMenuPrincipal' con baja cohesión (17%) |
| Baja cohesión (LCOM) | `src/main/kotlin/submarino/ParticulasMarinas.kt` | 14 | clase 'ParticulasMarinas' con baja cohesión (33%) |
| Baja cohesión (LCOM) | `src/main/kotlin/submarino/PecesMarinos.kt` | 14 | clase 'PecesMarinos' con baja cohesión (33%) |
| Baja cohesión (LCOM) | `src/main/kotlin/submarino/TarjetaHUD.kt` | 13 | clase 'TarjetaHUD' con baja cohesión (30%) |
| Baja cohesión (LCOM) | `src/main/kotlin/submarino/VistaJuego.kt` | 19 | clase 'VistaJuego' con baja cohesión (30%) |
| Baja cohesión (LCOM) | `src/main/kotlin/submarino/comercio/EstacionAcople.kt` | 101 | clase 'EstacionAcople' con baja cohesión (36%) |
| Baja cohesión (LCOM) | `src/main/kotlin/submarino/upgrades/Mejora.kt` | 49 | clase 'Mejora' con baja cohesión (17%) |
| Complejidad alta | `src/test/kotlin/submarino/Issue7ObservadoresTest.kt` | 46 | método 'Issue7ObservadoresTest.`test observador de comercio es notificado con recibo transaccional completo`' con complejidad 10 |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/AvatarSubmarino.kt` | 21 | método 'AvatarSubmarino.crear' de 91 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/CapaFondoParallax.kt` | 22 | método 'CapaFondoParallax.crear' de 44 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/ConstructorAccionesTienda.kt` | 18 | método 'ConstructorAccionesTienda.crearFilaAcciones' de 37 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/ConstructorExplosionSubmarino.kt` | 17 | método 'ConstructorExplosionSubmarino.crear' de 35 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/ConstructorFilasMejoraTienda.kt` | 30 | método 'ConstructorFilasMejoraTienda.crearFilaMejora' de 35 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/CristalNodoMineralVectorial.kt` | 14 | método 'CristalNodoMineralVectorial.crear' de 52 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/CuerpoSubmarinoVectorial.kt` | 19 | método 'CuerpoSubmarinoVectorial.crear' de 118 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/DemostradorNavegacionRecoleccion.kt` | 8 | método 'DemostradorNavegacionRecoleccion.demostrar' con 5 parámetros |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/DetallesRoca.kt` | 50 | método 'DetallesRoca.dibujarRocasInteriores' de 30 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/DetallesRoca.kt` | 102 | método 'DetallesRoca.dibujarSedimento' de 36 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/EstadoPartida.kt` | 49 | método 'EstadoPartida.actualizar' con 5 parámetros |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/EstadoPartida.kt` | 49 | método 'EstadoPartida.actualizar' de 38 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/FabricaPecesMarinos.kt` | 16 | método 'FabricaPecesMarinos.crear' de 30 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/FondoAbisal.kt` | 35 | método 'FondoAbisal.dibujarCapaLejana' de 31 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/GeneradorTerrenoSubmarino.kt` | 29 | método 'GeneradorTerrenoSubmarino.formacionesAncla' de 32 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/GeneradorTerrenoSubmarino.kt` | 82 | método 'GeneradorTerrenoSubmarino.crearCrestas' con 6 parámetros |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/GeneradorTerrenoSubmarino.kt` | 82 | método 'GeneradorTerrenoSubmarino.crearCrestas' de 32 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/HeliceSubmarinoVectorial.kt` | 12 | método 'HeliceSubmarinoVectorial.crear' de 31 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/PanelReglasMenuPrincipal.kt` | 28 | método 'PanelReglasMenuPrincipal.crearOverlayReglas' de 33 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/PincelArrecifes.kt` | 24 | método 'PincelArrecifes.dibujarEstructurasRocosas' de 55 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/PincelCordillera.kt` | 26 | método 'PincelCordillera.dibujar' con 5 parámetros |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/PincelCordillera.kt` | 26 | método 'PincelCordillera.dibujar' de 79 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/PincelFormacionRocosa.kt` | 22 | método 'PincelFormacionRocosa.dibujar' de 58 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/PintorPecesMarinos.kt` | 21 | método 'PintorPecesMarinos.dibujarPez' de 43 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/PlataformaEstacionVectorial.kt` | 14 | método 'PlataformaEstacionVectorial.crear' de 40 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/RocaNodoMineralVectorial.kt` | 15 | método 'RocaNodoMineralVectorial.crear' de 66 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/SubmarinoApp.kt` | 54 | método 'SubmarinoApp.iniciarJuego' de 31 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/TorreEstacionVectorial.kt` | 17 | método 'TorreEstacionVectorial.crear' de 64 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/main/kotlin/submarino/upgrades/ServicioUpgrades.kt` | 30 | método 'ServicioUpgrades.comprarMejora' de 42 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/test/kotlin/submarino/Issue5Test.kt` | 124 | método 'Issue5Test.verificarMineral' con 5 parámetros |
| Métodos/funciones largos o con muchos parámetros | `src/test/kotlin/submarino/Issue6EstrategiasAscensoTest.kt` | 43 | método 'Issue6EstrategiasAscensoTest.testPolimorfismoOCPConEstrategiaFisicaFlotabilidadArquimedes' de 41 líneas |
| Métodos/funciones largos o con muchos parámetros | `src/test/kotlin/submarino/Issue7ComercioTest.kt` | 111 | método 'Issue7ComercioTest.verificarVentaExitosa' con 5 parámetros |
| Métodos/funciones largos o con muchos parámetros | `src/test/kotlin/submarino/Issue8ComprasTest.kt` | 152 | método 'Issue8ComprasTest.verificarCompraExitosa' con 5 parámetros |
| Métodos/funciones largos o con muchos parámetros | `src/test/kotlin/submarino/Issue8ObservadoresTest.kt` | 15 | método 'Issue8ObservadoresTest.`test observador de tienda es notificado en compras exitosas y rechazos`' de 30 líneas |
| Acoplamiento alto (CBO) | `src/main/kotlin/org/example/issue1/FabricaSubsistemasSubmarino.kt` | 69 | clase 'FabricaSubsistemasEstandar' muy acoplada (CBO=18) |
| Acoplamiento alto (CBO) | `src/main/kotlin/org/example/issue1/Submarino.kt` | 24 | clase 'Submarino' muy acoplada (CBO=25) |
| Acoplamiento alto (CBO) | `src/main/kotlin/submarino/FabricaDominio.kt` | 10 | clase 'FabricaDominio' muy acoplada (CBO=67) |
| Acoplamiento alto (CBO) | `src/main/kotlin/submarino/FabricaHUD.kt` | 14 | clase 'FabricaHUD' muy acoplada (CBO=33) |
| Acoplamiento alto (CBO) | `src/main/kotlin/submarino/FabricaServicios.kt` | 7 | clase 'FabricaServicios' muy acoplada (CBO=37) |
| Acoplamiento alto (CBO) | `src/main/kotlin/submarino/FabricaVisual.kt` | 15 | clase 'FabricaVisual' muy acoplada (CBO=52) |
| Acoplamiento alto (CBO) | `src/main/kotlin/submarino/HUDJuego.kt` | 15 | clase 'HUDJuego' muy acoplada (CBO=11) |
| Acoplamiento alto (CBO) | `src/main/kotlin/submarino/SesionJuego.kt` | 11 | clase 'SesionJuego' muy acoplada (CBO=23) |
| Acoplamiento alto (CBO) | `src/main/kotlin/submarino/VistaJuego.kt` | 19 | clase 'VistaJuego' muy acoplada (CBO=21) |
| Acoplamiento alto (CBO) | `src/main/kotlin/submarino/VistaSubmarino.kt` | 23 | clase 'VistaSubmarino' muy acoplada (CBO=8) |
| Acoplamiento alto (CBO) | `src/main/kotlin/submarino/comercio/ServicioComercio.kt` | 117 | clase 'ServicioComercio' muy acoplada (CBO=9) |
| Acoplamiento alto (CBO) | `src/main/kotlin/submarino/upgrades/FabricaMejoras.kt` | 59 | clase 'FabricaMejorasEstandar' muy acoplada (CBO=8) |
| Acoplamiento alto (CBO) | `src/main/kotlin/submarino/upgrades/ServicioUpgrades.kt` | 14 | clase 'ServicioUpgrades' muy acoplada (CBO=8) |
| Acoplamiento alto (CBO) | `src/test/kotlin/submarino/FabricaDatosPrueba.kt` | 8 | clase 'FabricaDatosPrueba' muy acoplada (CBO=12) |
| Interfaz con demasiados métodos (ISP) | `src/main/kotlin/submarino/ResultadoRecoleccion.kt` | 8 | interfaz 'ResultadoRecoleccion' con 24 métodos |
| Interfaz con demasiados métodos (ISP) | `src/main/kotlin/submarino/upgrades/ResultadoCompra.kt` | 8 | interfaz 'ResultadoCompra' con 28 métodos |

### Clases señaladas — métricas

> Solo clases con al menos una señal. `LOC lóg.` y `Mét. lóg.` descuentan getters/setters; `LCOM` es la cantidad de pares de métodos sin campos en común.

| Clase | Archivo | Línea | LOC lóg. | Mét. lóg. | Campos | CBO | LCOM | Cohesión | WMC | RFC | Señales |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| `GeneradorTerrenoSubmarino` | `src/main/kotlin/submarino/GeneradorTerrenoSubmarino.kt` | 14 | 112 | 5 | 2 | 3 | 1 | 0% | 7 | 24 | método largo×2, muchos parámetros×1 |
| `DetallesRoca` | `src/main/kotlin/submarino/DetallesRoca.kt` | 16 | 141 | 6 | 3 | 2 | 2 | 87% | 20 | 27 | método largo×2 |
| `PincelCordillera` | `src/main/kotlin/submarino/PincelCordillera.kt` | 20 | 84 | 2 | 3 | 1 | 0 | 100% | 8 | 38 | método largo×1, muchos parámetros×1 |
| `ServicioUpgrades` | `src/main/kotlin/submarino/upgrades/ServicioUpgrades.kt` | 14 | 60 | 6 | 2 | 8 | 4 | 60% | 11 | 28 | acoplamiento alto×1, método largo×1 |
| `EstadoPartida` | `src/main/kotlin/submarino/EstadoPartida.kt` | 12 | 55 | 5 | 13 | 3 | 5 | 50% | 13 | 22 | método largo×1, muchos parámetros×1 |
| `PanelReglasMenuPrincipal` | `src/main/kotlin/submarino/PanelReglasMenuPrincipal.kt` | 14 | 53 | 4 | 7 | 3 | 5 | 17% | 4 | 21 | método largo×1, baja cohesión×1 |
| `Submarino` | `src/main/kotlin/org/example/issue1/Submarino.kt` | 24 | 48 | 23 | 29 | 25 | 223 | 12% | 24 | 76 | acoplamiento alto×1, baja cohesión×1 |
| `VistaJuego` | `src/main/kotlin/submarino/VistaJuego.kt` | 19 | 34 | 5 | 40 | 21 | 7 | 30% | 7 | 46 | acoplamiento alto×1, baja cohesión×1 |
| `CuerpoSubmarinoVectorial` | `src/main/kotlin/submarino/CuerpoSubmarinoVectorial.kt` | 15 | 118 | 1 | 1 | 1 | 0 | 100% | 1 | 13 | método largo×1 |
| `Issue5Test` | `src/test/kotlin/submarino/Issue5Test.kt` | 11 | 113 | 9 | 0 | 3 | 0 | 100% | 9 | 36 | muchos parámetros×1 |
| `Issue8ComprasTest` | `src/test/kotlin/submarino/Issue8ComprasTest.kt` | 12 | 106 | 7 | 0 | 5 | 0 | 100% | 7 | 26 | muchos parámetros×1 |
| `SesionJuego` | `src/main/kotlin/submarino/SesionJuego.kt` | 11 | 103 | 14 | 31 | 23 | 35 | 62% | 20 | 58 | acoplamiento alto×1 |
| `PincelArrecifes` | `src/main/kotlin/submarino/PincelArrecifes.kt` | 17 | 102 | 5 | 4 | 1 | 0 | 100% | 12 | 28 | método largo×1 |
| `AvatarSubmarino` | `src/main/kotlin/submarino/AvatarSubmarino.kt` | 19 | 91 | 1 | 0 | 1 | 0 | 100% | 1 | 12 | método largo×1 |
| `Issue7ComercioTest` | `src/test/kotlin/submarino/Issue7ComercioTest.kt` | 12 | 80 | 7 | 0 | 5 | 0 | 100% | 7 | 23 | muchos parámetros×1 |
| `Issue7ObservadoresTest` | `src/test/kotlin/submarino/Issue7ObservadoresTest.kt` | 11 | 74 | 4 | 0 | 7 | 0 | 100% | 13 | 24 | complejidad alta×1 |
| `SubmarinoApp` | `src/main/kotlin/submarino/SubmarinoApp.kt` | 14 | 73 | 6 | 10 | 7 | 2 | 80% | 13 | 28 | método largo×1 |
| `Issue6EstrategiasAscensoTest` | `src/test/kotlin/submarino/Issue6EstrategiasAscensoTest.kt` | 7 | 68 | 2 | 0 | 1 | 0 | 100% | 2 | 13 | método largo×1 |
| `RocaNodoMineralVectorial` | `src/main/kotlin/submarino/RocaNodoMineralVectorial.kt` | 13 | 66 | 1 | 0 | 1 | 0 | 100% | 1 | 11 | método largo×1 |
| `TorreEstacionVectorial` | `src/main/kotlin/submarino/TorreEstacionVectorial.kt` | 15 | 64 | 1 | 0 | 0 | 0 | 100% | 1 | 11 | método largo×1 |
| `PincelFormacionRocosa` | `src/main/kotlin/submarino/PincelFormacionRocosa.kt` | 15 | 58 | 1 | 5 | 3 | 0 | 100% | 8 | 31 | método largo×1 |
| `CapaFondoParallax` | `src/main/kotlin/submarino/CapaFondoParallax.kt` | 17 | 53 | 2 | 5 | 1 | 0 | 100% | 4 | 14 | método largo×1 |
| `ServicioComercio` | `src/main/kotlin/submarino/comercio/ServicioComercio.kt` | 117 | 53 | 9 | 2 | 9 | 3 | 50% | 17 | 31 | acoplamiento alto×1 |
| `CristalNodoMineralVectorial` | `src/main/kotlin/submarino/CristalNodoMineralVectorial.kt` | 12 | 52 | 1 | 0 | 1 | 0 | 100% | 1 | 10 | método largo×1 |
| `DemostradorNavegacionRecoleccion` | `src/main/kotlin/submarino/DemostradorNavegacionRecoleccion.kt` | 6 | 52 | 5 | 0 | 4 | 0 | 100% | 5 | 15 | muchos parámetros×1 |
| `Issue8ObservadoresTest` | `src/test/kotlin/submarino/Issue8ObservadoresTest.kt` | 12 | 52 | 3 | 0 | 4 | 0 | 100% | 7 | 20 | método largo×1 |
| `EstadoAscenso` | `src/main/kotlin/submarino/FisicaAscenso.kt` | 15 | 51 | 21 | 7 | 0 | 70 | 49% | 27 | 45 | baja cohesión×1 |
| `PintorPecesMarinos` | `src/main/kotlin/submarino/PintorPecesMarinos.kt` | 15 | 46 | 2 | 1 | 2 | 0 | 100% | 3 | 22 | método largo×1 |
| `PlataformaEstacionVectorial` | `src/main/kotlin/submarino/PlataformaEstacionVectorial.kt` | 12 | 46 | 2 | 0 | 1 | 0 | 100% | 2 | 11 | método largo×1 |
| `FondoAbisal` | `src/main/kotlin/submarino/FondoAbisal.kt` | 20 | 45 | 2 | 7 | 1 | 0 | 100% | 2 | 23 | método largo×1 |
| `ResultadoCompra` | `src/main/kotlin/submarino/upgrades/ResultadoCompra.kt` | 8 | 44 | 28 | 11 | 1 | 103 | 33% | 44 | 44 | interfaz grande×1 |
| `ResultadoRecoleccion` | `src/main/kotlin/submarino/ResultadoRecoleccion.kt` | 8 | 44 | 24 | 5 | 1 | 19 | 76% | 38 | 38 | interfaz grande×1 |
| `ConstructorFilasMejoraTienda` | `src/main/kotlin/submarino/ConstructorFilasMejoraTienda.kt` | 16 | 41 | 2 | 4 | 4 | 0 | 100% | 5 | 20 | método largo×1 |
| `FabricaDominio` | `src/main/kotlin/submarino/FabricaDominio.kt` | 10 | 38 | 22 | 0 | 67 | 0 | 100% | 22 | 118 | acoplamiento alto×1 |
| `ConstructorAccionesTienda` | `src/main/kotlin/submarino/ConstructorAccionesTienda.kt` | 11 | 37 | 1 | 4 | 2 | 0 | 100% | 1 | 13 | método largo×1 |
| `ConstructorExplosionSubmarino` | `src/main/kotlin/submarino/ConstructorExplosionSubmarino.kt` | 12 | 35 | 1 | 2 | 2 | 0 | 100% | 1 | 10 | método largo×1 |
| `EstacionAcople` | `src/main/kotlin/submarino/comercio/EstacionAcople.kt` | 101 | 33 | 12 | 5 | 6 | 29 | 36% | 15 | 32 | baja cohesión×1 |
| `ParticulasMarinas` | `src/main/kotlin/submarino/ParticulasMarinas.kt` | 14 | 33 | 4 | 15 | 3 | 4 | 33% | 7 | 22 | baja cohesión×1 |
| `PecesMarinos` | `src/main/kotlin/submarino/PecesMarinos.kt` | 14 | 33 | 4 | 15 | 3 | 4 | 33% | 7 | 22 | baja cohesión×1 |
| `Bodega` | `src/main/kotlin/submarino/Bodega.kt` | 13 | 31 | 11 | 6 | 3 | 35 | 36% | 13 | 31 | baja cohesión×1 |
| `HeliceSubmarinoVectorial` | `src/main/kotlin/submarino/HeliceSubmarinoVectorial.kt` | 8 | 31 | 1 | 1 | 1 | 0 | 100% | 1 | 7 | método largo×1 |
| `FabricaPecesMarinos` | `src/main/kotlin/submarino/FabricaPecesMarinos.kt` | 10 | 30 | 1 | 7 | 1 | 0 | 100% | 4 | 7 | método largo×1 |
| `FabricaVisual` | `src/main/kotlin/submarino/FabricaVisual.kt` | 15 | 29 | 22 | 0 | 52 | 0 | 100% | 22 | 118 | acoplamiento alto×1 |
| `ControladorAccionesTeclado` | `src/main/kotlin/submarino/ControladorTeclado.kt` | 107 | 28 | 5 | 9 | 4 | 7 | 30% | 9 | 23 | baja cohesión×1 |
| `ControladorMovimientoTeclado` | `src/main/kotlin/submarino/ControladorTeclado.kt` | 13 | 28 | 8 | 6 | 4 | 8 | 20% | 14 | 29 | baja cohesión×1 |
| `Mineral` | `src/main/kotlin/submarino/Mineral.kt` | 71 | 24 | 14 | 11 | 4 | 61 | 22% | 18 | 35 | baja cohesión×1 |
| `Mejora` | `src/main/kotlin/submarino/upgrades/Mejora.kt` | 49 | 21 | 12 | 9 | 6 | 30 | 17% | 17 | 30 | baja cohesión×1 |
| `TarjetaHUD` | `src/main/kotlin/submarino/TarjetaHUD.kt` | 13 | 21 | 5 | 4 | 2 | 7 | 30% | 7 | 14 | baja cohesión×1 |
| `FabricaServicios` | `src/main/kotlin/submarino/FabricaServicios.kt` | 7 | 19 | 10 | 0 | 37 | 0 | 100% | 10 | 72 | acoplamiento alto×1 |
| `NavegadorSubmarino` | `src/main/kotlin/org/example/issue1/NavegadorSubmarino.kt` | 9 | 18 | 7 | 6 | 4 | 9 | 10% | 7 | 20 | baja cohesión×1 |
| `VistaSubmarino` | `src/main/kotlin/submarino/VistaSubmarino.kt` | 23 | 14 | 3 | 15 | 8 | 2 | 33% | 3 | 21 | acoplamiento alto×1 |
| `EstadoHUD` | `src/main/kotlin/submarino/HUDView.kt` | 38 | 13 | 6 | 6 | 0 | 6 | 40% | 9 | 15 | baja cohesión×1 |
| `FabricaDatosPrueba` | `src/test/kotlin/submarino/FabricaDatosPrueba.kt` | 8 | 13 | 4 | 0 | 12 | 0 | 100% | 4 | 21 | acoplamiento alto×1 |
| `FabricaSubsistemasEstandar` | `src/main/kotlin/org/example/issue1/FabricaSubsistemasSubmarino.kt` | 69 | 12 | 10 | 0 | 18 | 0 | 100% | 10 | 42 | acoplamiento alto×1 |
| `GestorMejorasSubmarino` | `src/main/kotlin/org/example/issue1/GestorMejorasSubmarino.kt` | 10 | 12 | 4 | 5 | 5 | 6 | 0% | 4 | 13 | baja cohesión×1 |
| `HUDJuego` | `src/main/kotlin/submarino/HUDJuego.kt` | 15 | 12 | 2 | 22 | 11 | 1 | 0% | 2 | 18 | acoplamiento alto×1 |
| `EstadoNavegacion` | `src/main/kotlin/submarino/ModeloNavegacion.kt` | 33 | 10 | 5 | 6 | 0 | 9 | 10% | 6 | 14 | baja cohesión×1 |
| `FabricaMejorasEstandar` | `src/main/kotlin/submarino/upgrades/FabricaMejoras.kt` | 59 | 10 | 2 | 3 | 8 | 1 | 0% | 2 | 21 | acoplamiento alto×1 |
| `EstadoJuego` | `src/main/kotlin/submarino/EstadoJuego.kt` | 9 | 7 | 6 | 21 | 0 | 9 | 10% | 7 | 14 | baja cohesión×1 |
| `FabricaHUD` | `src/main/kotlin/submarino/FabricaHUD.kt` | 14 | 6 | 5 | 0 | 33 | 0 | 100% | 5 | 61 | acoplamiento alto×1 |

### Métodos y funciones señalados

> Filas ordenadas por complejidad, tamaño y parámetros. Una misma fila puede acumular más de una señal.

| Archivo | Línea | Método / función | LOC | Parámetros | Complejidad | Señales |
|---|---:|---|---:|---:|---:|---|
| `src/test/kotlin/submarino/Issue7ObservadoresTest.kt` | 46 | `Issue7ObservadoresTest.`test observador de comercio es notificado con recibo transaccional completo`` | — | — | 10 | complejidad alta |
| `src/main/kotlin/submarino/CuerpoSubmarinoVectorial.kt` | 19 | `CuerpoSubmarinoVectorial.crear` | 118 | — | — | método largo |
| `src/main/kotlin/submarino/AvatarSubmarino.kt` | 21 | `AvatarSubmarino.crear` | 91 | — | — | método largo |
| `src/main/kotlin/submarino/PincelCordillera.kt` | 26 | `PincelCordillera.dibujar` | 79 | 5 | — | muchos parámetros, método largo |
| `src/main/kotlin/submarino/RocaNodoMineralVectorial.kt` | 15 | `RocaNodoMineralVectorial.crear` | 66 | — | — | método largo |
| `src/main/kotlin/submarino/TorreEstacionVectorial.kt` | 17 | `TorreEstacionVectorial.crear` | 64 | — | — | método largo |
| `src/main/kotlin/submarino/PincelFormacionRocosa.kt` | 22 | `PincelFormacionRocosa.dibujar` | 58 | — | — | método largo |
| `src/main/kotlin/submarino/PincelArrecifes.kt` | 24 | `PincelArrecifes.dibujarEstructurasRocosas` | 55 | — | — | método largo |
| `src/main/kotlin/submarino/CristalNodoMineralVectorial.kt` | 14 | `CristalNodoMineralVectorial.crear` | 52 | — | — | método largo |
| `src/main/kotlin/submarino/CapaFondoParallax.kt` | 22 | `CapaFondoParallax.crear` | 44 | — | — | método largo |
| `src/main/kotlin/submarino/PintorPecesMarinos.kt` | 21 | `PintorPecesMarinos.dibujarPez` | 43 | — | — | método largo |
| `src/main/kotlin/submarino/upgrades/ServicioUpgrades.kt` | 30 | `ServicioUpgrades.comprarMejora` | 42 | — | — | método largo |
| `src/test/kotlin/submarino/Issue6EstrategiasAscensoTest.kt` | 43 | `Issue6EstrategiasAscensoTest.testPolimorfismoOCPConEstrategiaFisicaFlotabilidadArquimedes` | 41 | — | — | método largo |
| `src/main/kotlin/submarino/PlataformaEstacionVectorial.kt` | 14 | `PlataformaEstacionVectorial.crear` | 40 | — | — | método largo |
| `src/main/kotlin/submarino/EstadoPartida.kt` | 49 | `EstadoPartida.actualizar` | 38 | 5 | — | muchos parámetros, método largo |
| `src/main/kotlin/submarino/ConstructorAccionesTienda.kt` | 18 | `ConstructorAccionesTienda.crearFilaAcciones` | 37 | — | — | método largo |
| `src/main/kotlin/submarino/DetallesRoca.kt` | 102 | `DetallesRoca.dibujarSedimento` | 36 | — | — | método largo |
| `src/main/kotlin/submarino/ConstructorExplosionSubmarino.kt` | 17 | `ConstructorExplosionSubmarino.crear` | 35 | — | — | método largo |
| `src/main/kotlin/submarino/ConstructorFilasMejoraTienda.kt` | 30 | `ConstructorFilasMejoraTienda.crearFilaMejora` | 35 | — | — | método largo |
| `src/main/kotlin/submarino/PanelReglasMenuPrincipal.kt` | 28 | `PanelReglasMenuPrincipal.crearOverlayReglas` | 33 | — | — | método largo |
| `src/main/kotlin/submarino/GeneradorTerrenoSubmarino.kt` | 82 | `GeneradorTerrenoSubmarino.crearCrestas` | 32 | 6 | — | muchos parámetros, método largo |
| `src/main/kotlin/submarino/GeneradorTerrenoSubmarino.kt` | 29 | `GeneradorTerrenoSubmarino.formacionesAncla` | 32 | — | — | método largo |
| `src/main/kotlin/submarino/FondoAbisal.kt` | 35 | `FondoAbisal.dibujarCapaLejana` | 31 | — | — | método largo |
| `src/main/kotlin/submarino/HeliceSubmarinoVectorial.kt` | 12 | `HeliceSubmarinoVectorial.crear` | 31 | — | — | método largo |
| `src/main/kotlin/submarino/SubmarinoApp.kt` | 54 | `SubmarinoApp.iniciarJuego` | 31 | — | — | método largo |
| `src/main/kotlin/submarino/DetallesRoca.kt` | 50 | `DetallesRoca.dibujarRocasInteriores` | 30 | — | — | método largo |
| `src/main/kotlin/submarino/FabricaPecesMarinos.kt` | 16 | `FabricaPecesMarinos.crear` | 30 | — | — | método largo |
| `src/test/kotlin/submarino/Issue8ObservadoresTest.kt` | 15 | `Issue8ObservadoresTest.`test observador de tienda es notificado en compras exitosas y rechazos`` | 30 | — | — | método largo |
| `src/main/kotlin/submarino/DemostradorNavegacionRecoleccion.kt` | 8 | `DemostradorNavegacionRecoleccion.demostrar` | — | 5 | — | muchos parámetros |
| `src/test/kotlin/submarino/Issue5Test.kt` | 124 | `Issue5Test.verificarMineral` | — | 5 | — | muchos parámetros |
| `src/test/kotlin/submarino/Issue7ComercioTest.kt` | 111 | `Issue7ComercioTest.verificarVentaExitosa` | — | 5 | — | muchos parámetros |
| `src/test/kotlin/submarino/Issue8ComprasTest.kt` | 152 | `Issue8ComprasTest.verificarCompraExitosa` | — | 5 | — | muchos parámetros |

### Umbrales aplicados (perfil exigente)

| Señal | Regla |
|---|---|
| Clase grande (SRP) | ≥ 200 LOC lógico, o ≥ 15 métodos con lógica y ≥ 100 LOC lógico; no cuentan getters/setters. |
| Baja cohesión (LCOM) | < 50 % de pares de métodos comparten algún campo, con ≥ 4 métodos con lógica y ≥ 3 campos. |
| Complejidad alta | algún método con complejidad ciclomática ≥ 10. |
| Método largo | ≥ 30 líneas. |
| Muchos parámetros | > 4 parámetros (5 o más). |
| Acoplamiento alto (CBO) | CBO ≥ 8. |
| Herencia profunda | DIT ≥ 3. |
| Type-check / switch de tipos | ≥ 1 cadena de type-check. |
| Interfaz demasiado grande (ISP) | > 6 métodos (7 o más). |
| Override vacío (LSP) | método vacío o marcado como 'no implementado'. |
| Clase solo de datos | 0 métodos y ≥ 3 campos. |
| Campos mutables públicos | ≥ 4 campos mutables públicos y ≥ 4 campos. |
| Clase estática | ≥ 2 métodos y todos estáticos. |
| Duplicación lógica | ventana de 8 líneas; solo lógica; se informa desde 1 %. |

### Desglose de la columna S (SRP)

> La columna **S** no es un puntaje: cuenta **señales** de SRP. Aquí suma **19** entrada(s) sobre **19** clase(s) distinta(s).

| Señal | Entradas | Regla (perfil exigente) |
|---|---:|---|
| Cohesión baja | 18 | < 50 % de los pares de métodos comparten algún campo (con ≥ 4 métodos con lógica y ≥ 3 campos). |
| Complejidad alta | 1 | Algún método con complejidad ciclomática ≥ 10. |

**Clases con baja cohesión (18)**

- `GestorMejorasSubmarino`: cohesión baja (0% de pares comparten campos)
- `EstadoJuego`: cohesión baja (10% de pares comparten campos)
- `EstadoNavegacion`: cohesión baja (10% de pares comparten campos)
- `NavegadorSubmarino`: cohesión baja (10% de pares comparten campos)
- `Submarino`: cohesión baja (12% de pares comparten campos)
- `PanelReglasMenuPrincipal`: cohesión baja (17% de pares comparten campos)
- `Mejora`: cohesión baja (17% de pares comparten campos)
- `ControladorMovimientoTeclado`: cohesión baja (20% de pares comparten campos)
- `Mineral`: cohesión baja (22% de pares comparten campos)
- `ControladorAccionesTeclado`: cohesión baja (30% de pares comparten campos)
- `TarjetaHUD`: cohesión baja (30% de pares comparten campos)
- `VistaJuego`: cohesión baja (30% de pares comparten campos)
- `ParticulasMarinas`: cohesión baja (33% de pares comparten campos)
- `PecesMarinos`: cohesión baja (33% de pares comparten campos)
- `Bodega`: cohesión baja (36% de pares comparten campos)
- `EstacionAcople`: cohesión baja (36% de pares comparten campos)
- `EstadoHUD`: cohesión baja (40% de pares comparten campos)
- `EstadoAscenso`: cohesión baja (49% de pares comparten campos)

**Complejidad alta (1)**

- `Issue7ObservadoresTest`: complejidad alta (máx=10)

> Los **34** hallazgo(s) de métodos largos o con muchos parámetros se listan en «Métodos/funciones largos o con muchos parámetros» y **no suman** a la columna S.

> El análisis no excluye tests: una clase de test que supere los umbrales también suma.

## 5. Evolución por commit

Métricas calculadas sobre el estado de `main` en **cada commit** (41 commits con trabajo), del primero al último. La calidad usa el perfil `exigente`; el aporte de código lo mide RepoSense. El índice puede **bajar** al crecer el proyecto: se acumulan señales.

```mermaid
xychart-beta
    title "Índice POO/SOLID a lo largo de los commits"
    x-axis ["dbfd5be", "f3d175b", "493b71f", "ae28d33", "0e5cb85", "1120c1a", "086d3a6", "c792a2f", "8f4e517", "a3e5d7a", "24401a4", "b941d71", "6739160", "6a83318", "6aacd31", "1ed61bb", "ec547ac", "ebe5217", "7468e30", "bf43829", "23cb875", "7d876c5", "c7f1398", "46021c2", "6f8da22", "35025bb", "c86747c", "6dc2110", "77a0f17", "03fbc47", "ba904e3", "7628d73", "930e93d", "9b7fee6", "4efc72c", "e197922", "59781fc", "d0e1119", "e88b31e", "401fe94", "4be405d"]
    y-axis "Índice" 0 --> 100
    line [0, 0, 58, 60, 60, 76, 70, 73, 71, 78, 87, 88, 88, 89, 90, 90, 92, 92, 92, 92, 83, 83, 86, 86, 86, 85, 85, 85, 85, 85, 85, 85, 85, 85, 85, 85, 85, 85, 85, 85, 85]
```

| # | Fecha | Commit | Autor | LOC | Índice | %POO | Duplic. | Sueltas |
|---|---|---|---|---:|---:|---:|---:|---:|
| 1 | 2026-08-14 | `dbfd5be` | ibaldasso-elpepe | 0 | 0 | 0% | 0.0% | 0 |
| 2 | 2026-08-24 | `f3d175b` | dqmdz-etec | 0 | 0 | 0% | 0.0% | 0 |
| 3 | 2026-08-28 | `493b71f` | ibaldasso-elpepe | 180 | 58 | 44% | 0.0% | 0 |
| 4 | 2026-10-02 | `ae28d33` | ibaldasso-elpepe | 302 | 60 | 38% | 0.0% | 0 |
| 5 | 2026-10-05 | `0e5cb85` | ibaldasso-elpepe | 702 | 60 | 92% | 8.8% | 0 |
| 6 | 2026-10-09 | `1120c1a` | ibaldasso-elpepe | 1,469 | 76 | 83% | 4.3% | 0 |
| 7 | 2026-10-09 | `086d3a6` | ibaldasso-elpepe | 2,351 | 70 | 82% | 8.0% | 0 |
| 8 | 2026-10-16 | `c792a2f` | ibaldasso-elpepe | 3,625 | 73 | 88% | 8.7% | 0 |
| 9 | 2026-10-16 | `8f4e517` | ibaldasso-elpepe | 5,355 | 71 | 91% | 9.1% | 0 |
| 10 | 2026-10-23 | `a3e5d7a` | ibaldasso-elpepe | 4,849 | 78 | 98% | 7.6% | 0 |
| 11 | 2026-10-26 | `24401a4` | ibaldasso-elpepe | 5,221 | 87 | 98% | 2.4% | 0 |
| 12 | 2026-10-30 | `b941d71` | ibaldasso-elpepe | 5,534 | 88 | 98% | 2.3% | 0 |
| 13 | 2026-10-30 | `6739160` | ibaldasso-elpepe | 5,717 | 88 | 98% | 2.2% | 0 |
| 14 | 2026-10-30 | `6a83318` | ibaldasso-elpepe | 6,090 | 89 | 98% | 2.1% | 0 |
| 15 | 2026-10-30 | `6aacd31` | ibaldasso-elpepe | 6,207 | 90 | 98% | 2.1% | 0 |
| 16 | 2026-10-30 | `1ed61bb` | ibaldasso-elpepe | 6,388 | 90 | 98% | 2.0% | 0 |
| 17 | 2026-10-31 | `ec547ac` | dqmdz-etec | 6,426 | 92 | 98% | 0.9% | 0 |
| 18 | 2026-10-31 | `ebe5217` | dqmdz-etec | 6,426 | 92 | 98% | 0.9% | 0 |
| 19 | 2026-10-31 | `7468e30` | dqmdz-etec | 6,426 | 92 | 98% | 0.9% | 0 |
| 20 | 2026-10-31 | `bf43829` | dqmdz-etec | 6,426 | 92 | 98% | 0.9% | 0 |
| 21 | 2026-11-02 | `23cb875` | ibaldasso-elpepe | 11,156 | 83 | 98% | 3.0% | 0 |
| 22 | 2026-11-02 | `7d876c5` | ibaldasso-elpepe | 11,251 | 83 | 99% | 3.0% | 0 |
| 23 | 2026-11-04 | `c7f1398` | ibaldasso-elpepe | 12,849 | 86 | 99% | 2.9% | 0 |
| 24 | 2026-11-04 | `46021c2` | ibaldasso-elpepe | 12,948 | 86 | 99% | 2.7% | 0 |
| 25 | 2026-11-04 | `6f8da22` | ibaldasso-elpepe | 12,945 | 86 | 99% | 2.7% | 0 |
| 26 | 2026-11-06 | `35025bb` | ibaldasso-elpepe | 14,113 | 85 | 99% | 3.0% | 0 |
| 27 | 2026-11-07 | `c86747c` | dqmdz-etec | 14,113 | 85 | 99% | 3.0% | 0 |
| 28 | 2026-11-07 | `6dc2110` | dqmdz-etec | 14,113 | 85 | 99% | 3.0% | 0 |
| 29 | 2026-11-07 | `77a0f17` | dqmdz-etec | 14,113 | 85 | 99% | 3.0% | 0 |
| 30 | 2026-11-07 | `03fbc47` | dqmdz-etec | 14,113 | 85 | 99% | 3.0% | 0 |
| 31 | 2026-11-13 | `ba904e3` | dqmdz-etec | 14,113 | 85 | 99% | 3.0% | 0 |
| 32 | 2026-11-14 | `7628d73` | dqmdz-etec | 14,113 | 85 | 99% | 3.0% | 0 |
| 33 | 2026-11-16 | `930e93d` | dqmdz-etec | 14,113 | 85 | 99% | 3.0% | 0 |
| 34 | 2026-11-17 | `9b7fee6` | dqmdz-etec | 14,113 | 85 | 99% | 3.0% | 0 |
| 35 | 2026-11-20 | `4efc72c` | dqmdz-etec | 14,113 | 85 | 99% | 3.0% | 0 |
| 36 | 2026-11-20 | `e197922` | ibaldasso-elpepe | 14,120 | 85 | 99% | 3.0% | 0 |
| 37 | 2026-11-20 | `59781fc` | dqmdz-etec | 14,136 | 85 | 99% | 3.0% | 0 |
| 38 | 2026-11-20 | `d0e1119` | dqmdz-etec | 14,144 | 85 | 99% | 3.0% | 0 |
| 39 | 2026-11-20 | `e88b31e` | dqmdz-etec | 14,144 | 85 | 99% | 3.0% | 0 |
| 40 | 2026-11-20 | `401fe94` | dqmdz-etec | 14,144 | 85 | 99% | 3.0% | 0 |
| 41 | 2026-11-20 | `4be405d` | dqmdz-etec | 14,144 | 85 | 99% | 3.0% | 0 |

---
_Informe generado por la cátedra. Los criterios y umbrales de medición corresponden al perfil `exigente` de `calidad.py`; el semáforo de aprobación, a `CRITERIOS-CALIDAD.md` (ver `README.md`). Ante dudas, revisá el archivo señalado._
<!-- evaluacion:fin -->
