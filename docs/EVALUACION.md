<!-- evaluacion:inicio -->
# Evaluación — nacho-baldasso-organizacion/Proyecto-Submarino-Prime

> Documento **generado automáticamente** por la cátedra de Programación II (RepoSense + análisis de calidad POO/SOLID). **No editar a mano**: se regenera en cada evaluación.
> Rama evaluada: `main`. Análisis **heurístico** (regex + métricas): es una criba para orientar la revisión, **no una nota**.
> Generado: 2026-10-30.

## 1. Veredicto

| Aspecto | Resultado |
|---|---|
| Índice de calidad POO/SOLID | **4/100** |
| Código (LOC) | 5,717 |
| Archivos de código | 47 |
| Clases (bien usadas) | 151 (55 OK) |
| Código dentro de clases (%POO) | 90% |
| Funciones fuera de clases | 0 |
| Duplicación lógica | 1.8% |
| Señales S/O/L/I/D | 17/19/2/0/14 |

> Este informe **describe** lo que el análisis midió con los criterios ya establecidos de la materia. No es una nota.

### Semáforo de aprobación

Criterios de `CRITERIOS-CALIDAD.md`. **Aprobar** se juega con el perfil `estandar`; **destacar**, con el `exigente` (meta de excelencia).

| Semáforo | Resultado |
|---|---|
| Aprobar (`estandar`) | **no cumple** — 3/4 |
| Destacar (`exigente`) | **no cumple** — 2/5 |

**Criterios para aprobar**

| Criterio | Requisito | Tu resultado | Cumple |
|---|---|---|---|
| Código dentro de clases (%POO) | ≥ 85% | 90% | sí |
| Duplicación lógica | ≤ 10% | 1.8% | sí |
| Funciones fuera de clases | ≤ 1 | 0 | sí |
| Señales POO/SOLID críticas | ninguna | Baja cohesión (LCOM), Acoplamiento alto (CBO), Switch sobre tipos (OCP), Override vacío / 'no implementado' (LSP) | no |

**Criterios para destacar**

| Criterio | Requisito | Tu resultado | Cumple |
|---|---|---|---|
| Código dentro de clases (%POO) | ≥ 95% | 90% | no |
| Duplicación lógica | ≤ 5% | 1.8% | sí |
| Funciones fuera de clases | ≤ 1 | 0 | sí |
| Señales POO/SOLID críticas | ninguna | Clase demasiado grande (SRP), Baja cohesión (LCOM), Acoplamiento alto (CBO), Switch sobre tipos (OCP), Override vacío / 'no implementado' (LSP) | no |
| Índice de calidad POO/SOLID | ≥ 60 | 4/100 | no |

## 2. Autoría (RepoSense)

| Autor | Commits | Código (LOC) | Tests (LOC) | Docs (LOC) | Días activos | % del código |
|---|---:|---:|---:|---:|---:|---:|
| ibaldasso-elpepe | 14 | 4,543 | 2,296 | 1 | 11 | 100% |

> _El LOC de autoría lo calcula RepoSense sobre el historial y puede diferir del LOC de código del análisis de calidad (que mira solo el estado de `main`)._

## 3. Aspectos evaluados

Cada aspecto indica **qué mide**, **por qué importa** y **tu resultado**. Cuando el análisis reportó una señal, se agregan preguntas para **investigar la causa** (no hay recetas: entender el porqué es parte de la evaluación).

### 1. Índice de calidad POO/SOLID (indice)

- **Qué mide:** Un resumen numérico (0-100) que combina la riqueza de clases bien usadas con la penalización por señales POO/SOLID y duplicación, y luego aplica topes duros.
- **Por qué importa:** Ordena y prioriza la revisión: un índice bajo indica varias señales que conviene mirar juntas.
- **Tu resultado:** índice **4/100**

### 2. Duplicación lógica (DRY) (duplicacion) — con señales

- **Qué mide:** Porcentaje de bloques de LÓGICA repetidos. Quedan fuera imports/includes, anotaciones, declaraciones de campos y comentarios/Javadoc/docstrings: esos bloques son boilerplate, no lógica.
- **Por qué importa:** Cada copia de lógica se mantiene por separado: un arreglo hay que repetirlo y los defectos se multiplican.
- **Tu resultado:** **1.8%** de duplicación lógica
- **Para investigar la causa:**
    - ¿El trabajo se repartió por pantallas o archivos y se integró copiando y pegando bloques?
    - ¿Qué zonas del sistema cambiaron juntas más de una vez (señal de lógica compartida)?
    - ¿Hubo un lugar común acordado para la lógica repetida antes de empezar, o cada integrante resolvió lo suyo?

### 3. Adopción de POO (% de código en clases) (poo) — sin señales

- **Qué mide:** Qué porción del código vive dentro de clases (no de funciones sueltas).
- **Por qué importa:** El diseño orientado a objetos es el eje de la materia: el comportamiento debería vivir como método de una clase.
- **Tu resultado:** **90.3%** del código dentro de clases

### 4. Funciones fuera de clases (sueltas) — sin señales

- **Qué mide:** Funciones de nivel de módulo que no pertenecen a ninguna clase (en esta materia el comportamiento debe ser método de una clase).
- **Por qué importa:** Son la forma más directa de código procedural en un proyecto que debería ser orientado a objetos.
- **Tu resultado:** **0** función(es) fuera de clases

### 5. Clase demasiado grande (SRP) (god) — con señales

- **Qué mide:** Clases con demasiadas líneas o métodos de LÓGICA. No cuentan los getters/setters triviales ni la cantidad de campos por sí sola.
- **Por qué importa:** Una clase que hace de todo cambia por muchos motivos y cuesta entenderla y probarla.
- **Tu resultado:** **4** clase(s) demasiado grande(s)
- **Para investigar la causa:**
    - ¿La clase creció por acumulación de responsabilidades?
    - ¿Qué partes de la clase se usan siempre juntas y cuáles no?
    - ¿Se podría describir su responsabilidad en una sola frase?

### 6. Baja cohesión (LCOM) (cohesion) — con señales

- **Qué mide:** Métodos y campos de una clase que casi no se relacionan entre sí.
- **Por qué importa:** Una unidad que cambia por motivos distintos y comparte poca información suele esconder más de una responsabilidad.
- **Tu resultado:** **3** clase(s) con baja cohesión
- **Para investigar la causa:**
    - ¿Qué métodos de la clase no comparten estado con el resto?
    - ¿Cambian por motivos distintos según la tarea?
    - ¿Se pensó su responsabilidad antes de escribirla, o se fue agregando?

### 7. Complejidad alta (complejidad) — sin señales

- **Qué mide:** Complejidad ciclomática: cantidad de caminos posibles dentro de un método o función (condicionales, bucles, casos).
- **Por qué importa:** Muchos caminos = difícil de seguir y de probar; suelen esconderse casos sin cubrir.
- **Tu resultado:** **0** método(s)/función(es) con complejidad alta

### 8. Métodos/funciones largos o con muchos parámetros (tamano) — con señales

- **Qué mide:** Tamaño de los métodos/funciones y cantidad de parámetros.
- **Por qué importa:** Una función que hace varias cosas cuesta leerla, nombrarla y reutilizarla.
- **Tu resultado:** **4** método(s)/función(es) largos o con muchos parámetros
- **Para investigar la causa:**
    - ¿La función creció por acumulación de pasos?
    - ¿Agrupa operaciones que podrían describirse por separado?
    - ¿Los parámetros representan una misma idea (un objeto) o cosas sueltas?

### 9. Acoplamiento alto (CBO) (acoplamiento) — con señales

- **Qué mide:** Cuánto depende una clase de otros tipos del proyecto.
- **Por qué importa:** Piezas muy enlazadas hacen que un cambio se propague y que sea difícil reemplazar partes.
- **Tu resultado:** **15** clase(s) muy acoplada(s)
- **Para investigar la causa:**
    - ¿La clase conoce detalles internos de muchas otras?
    - ¿Se puede cambiar una parte sin tocar las demás?
    - ¿Los colaboradores llegan como parámetros o se crean por dentro?

### 10. Herencia profunda (DIT) (herencia) — sin señales

- **Qué mide:** Profundidad de la cadena de herencia de una clase.
- **Por qué importa:** Jerarquías muy profundas vuelven difícil saber de dónde viene cada comportamiento.
- **Tu resultado:** **0** clase(s) con herencia profunda

### 11. Switch sobre tipos (OCP) (ocp) — con señales

- **Qué mide:** Chequeos repetidos de tipo (`instanceof`/`is`/`typeof`) que equivalen a un switch sobre el tipo.
- **Por qué importa:** Cada tipo nuevo obliga a tocar varios lugares, en vez de extender con una implementación nueva.
- **Tu resultado:** **19** clase(s) con switch sobre tipos
- **Para investigar la causa:**
    - ¿Qué operación se resuelve preguntando el tipo?
    - ¿Qué se toca cada vez que aparece un tipo o caso nuevo?
    - ¿El comportamiento podría vivir en la propia jerarquía de tipos?

### 12. Interfaz con demasiados métodos (ISP) (isp) — sin señales

- **Qué mide:** Interfaces o clases abstractas con muchos métodos declarados.
- **Por qué importa:** Obliga a implementar operaciones que no se usan y acopla a los clientes a métodos que no necesitan.
- **Tu resultado:** **0** interfaz(es) con demasiados métodos

### 13. Override vacío o 'no implementado' (LSP) (lsp) — con señales

- **Qué mide:** Métodos sobrescritos que quedan vacíos o lanzan 'no implementado'.
- **Por qué importa:** Suele indicar que la jerarquía no representa bien el problema y rompe la sustitución entre clases.
- **Tu resultado:** **2** override(s) vacío(s) o 'no implementado'
- **Para investigar la causa:**
    - ¿Por qué ese método no tiene comportamiento en esa clase?
    - ¿La clase hereda algo que en realidad no le corresponde?
    - ¿Qué contrato se esperaba cumplir al heredar?

### 14. Dependencia de concretos y falta de abstracciones (DIP) (dip) — con señales

- **Qué mide:** Instanciación directa de clases concretas y ausencia de interfaces o clases abstractas pese al acoplamiento.
- **Por qué importa:** Depender de implementaciones concretas dificulta cambiar o probar una parte sin arrastrar las demás.
- **Tu resultado:** **14** dependencia(s) de concretos sin abstracciones
- **Para investigar la causa:**
    - ¿La clase crea por dentro a sus colaboradores en vez de recibirlos?
    - ¿Hay puntos de variación que convendría expresar como abstracción?
    - ¿Cómo se probaría esa clase cambiando una implementación?

### 15. Clase sin métodos (solo datos) (data) — con señales

- **Qué mide:** Clases que solo acumulan campos y no tienen comportamiento.
- **Por qué importa:** Separar datos de comportamiento reparte por el proyecto la lógica que debería estar cerca de esos datos.
- **Tu resultado:** **10** clase(s) sin métodos (solo datos)
- **Para investigar la causa:**
    - ¿Dónde vive el comportamiento que opera sobre esos datos?
    - ¿La clase representa un concepto del dominio o un contenedor técnico?
    - ¿Se evita la lógica de negocio dentro de la clase por comodidad?

### 16. Campos públicos (encapsulación) (encapsulacion) — con señales

- **Qué mide:** Clases que exponen campos sin control de acceso.
- **Por qué importa:** Expone el estado interno y hace que cualquier cambio dependa de quién lo modifica desde afuera.
- **Tu resultado:** **24** clase(s) con campos públicos
- **Para investigar la causa:**
    - ¿Se accede al campo desde otras clases para evitar un método?
    - ¿Qué reglas deberían respetarse al modificar ese estado?
    - ¿Los campos públicos se usan más como configuración que como estado?

### 17. Clase solo con métodos estáticos (utilitaria) (static) — sin señales

- **Qué mide:** Clases donde todos los métodos son estáticos.
- **Por qué importa:** Es comportamiento procedural disfrazado de clase: no se instancia ni participa del modelo de objetos.
- **Tu resultado:** **0** clase(s) solo con métodos estáticos

## 4. Anexo: dónde mirar

Archivos donde el análisis encontró cada señal (sin número de línea, a propósito):

**Clase demasiado grande (SRP) (god)** — 4 archivo(s):
- `src/main/kotlin/submarino/MainPruebaIssue5.kt`
- `src/test/kotlin/org/example/AppTest.kt`
- `src/test/kotlin/submarino/HUDViewTest.kt`
- `src/test/kotlin/submarino/Issue6Test.kt`

**Baja cohesión (LCOM) (cohesion)** — 3 archivo(s):
- `src/main/kotlin/org/example/issue1/Submarino.kt`
- `src/test/kotlin/submarino/Issue7Test.kt`
- `src/test/kotlin/submarino/Issue8Test.kt`

**Métodos/funciones largos o con muchos parámetros (tamano)** — 4 archivo(s):
- `src/main/kotlin/org/example/issue2/PruebaConjunta.kt`
- `src/main/kotlin/submarino/comercio/ServicioComercio.kt`
- `src/test/kotlin/org/example/AppTest.kt`
- `src/test/kotlin/submarino/Issue6Test.kt`

**Acoplamiento alto (CBO) (acoplamiento)** — 15 archivo(s):
- `src/main/kotlin/org/example/issue1/Submarino.kt`
- `src/main/kotlin/submarino/MainPruebaIssue5.kt`
- `src/main/kotlin/submarino/MainPruebaIssue6.kt`
- `src/main/kotlin/submarino/ServicioRecoleccion.kt`
- `src/main/kotlin/submarino/comercio/MainPruebaIssue7.kt`
- `src/main/kotlin/submarino/comercio/ServicioComercio.kt`
- `src/main/kotlin/submarino/upgrades/FabricaMejoras.kt`
- `src/main/kotlin/submarino/upgrades/MainPruebaIssue8.kt`
- `src/main/kotlin/submarino/upgrades/Mejora.kt`
- `src/main/kotlin/submarino/upgrades/ServicioUpgrades.kt`
- `src/test/kotlin/org/example/AppTest.kt`
- `src/test/kotlin/submarino/Issue5Test.kt`
- `src/test/kotlin/submarino/Issue6Test.kt`
- `src/test/kotlin/submarino/Issue7Test.kt`
- `src/test/kotlin/submarino/Issue8Test.kt`

**Switch sobre tipos (OCP) (ocp)** — 10 archivo(s):
- `src/main/kotlin/org/example/issue1/ModuloCasco.kt`
- `src/main/kotlin/submarino/FisicaAscenso.kt`
- `src/main/kotlin/submarino/Mineral.kt`
- `src/main/kotlin/submarino/Posicion.kt`
- `src/main/kotlin/submarino/ResultadoRecoleccion.kt`
- `src/main/kotlin/submarino/comercio/ServicioComercio.kt`
- `src/main/kotlin/submarino/upgrades/Mejora.kt`
- `src/main/kotlin/submarino/upgrades/ResultadoCompra.kt`
- `src/test/kotlin/submarino/Issue7Test.kt`
- `src/test/kotlin/submarino/Issue8Test.kt`

**Override vacío o 'no implementado' (LSP) (lsp)** — 1 archivo(s):
- `src/test/kotlin/submarino/Issue7Test.kt`

**Dependencia de concretos y falta de abstracciones (DIP) (dip)** — 14 archivo(s):
- `src/main/kotlin/org/example/issue1/Submarino.kt`
- `src/main/kotlin/submarino/MainPruebaIssue5.kt`
- `src/main/kotlin/submarino/MainPruebaIssue6.kt`
- `src/main/kotlin/submarino/ServicioRecoleccion.kt`
- `src/main/kotlin/submarino/comercio/MainPruebaIssue7.kt`
- `src/main/kotlin/submarino/comercio/ServicioComercio.kt`
- `src/main/kotlin/submarino/upgrades/FabricaMejoras.kt`
- `src/main/kotlin/submarino/upgrades/MainPruebaIssue8.kt`
- `src/main/kotlin/submarino/upgrades/ServicioUpgrades.kt`
- `src/test/kotlin/org/example/AppTest.kt`
- `src/test/kotlin/submarino/Issue5Test.kt`
- `src/test/kotlin/submarino/Issue6Test.kt`
- `src/test/kotlin/submarino/Issue7Test.kt`
- `src/test/kotlin/submarino/Issue8Test.kt`

**Clase sin métodos (solo datos) (data)** — 7 archivo(s):
- `src/main/kotlin/org/example/issue2/CalculadoraHidrostatica.kt`
- `src/main/kotlin/submarino/FisicaAscenso.kt`
- `src/main/kotlin/submarino/HUDView.kt`
- `src/main/kotlin/submarino/ServicioRecoleccion.kt`
- `src/main/kotlin/submarino/comercio/EstacionAcople.kt`
- `src/main/kotlin/submarino/comercio/ServicioComercio.kt`
- `src/main/kotlin/submarino/upgrades/FabricaMejoras.kt`

**Campos públicos (encapsulación) (encapsulacion)** — 16 archivo(s):
- `src/main/kotlin/org/example/issue1/Submarino.kt`
- `src/main/kotlin/submarino/Bodega.kt`
- `src/main/kotlin/submarino/FisicaAscenso.kt`
- `src/main/kotlin/submarino/HUDView.kt`
- `src/main/kotlin/submarino/Interfaces.kt`
- `src/main/kotlin/submarino/Mineral.kt`
- `src/main/kotlin/submarino/ResultadoRecoleccion.kt`
- `src/main/kotlin/submarino/TipoMineral.kt`
- `src/main/kotlin/submarino/comercio/EstacionAcople.kt`
- `src/main/kotlin/submarino/comercio/ServicioComercio.kt`
- `src/main/kotlin/submarino/upgrades/FabricaMejoras.kt`
- `src/main/kotlin/submarino/upgrades/Mejora.kt`
- `src/main/kotlin/submarino/upgrades/ResultadoCompra.kt`
- `src/main/kotlin/submarino/upgrades/ServicioUpgrades.kt`
- `src/test/kotlin/submarino/Issue7Test.kt`
- `src/test/kotlin/submarino/Issue8Test.kt`

## 5. Evolución por commit

Métricas calculadas sobre el estado de `main` en **cada commit** (13 commits con trabajo), del primero al último. La calidad usa el perfil `exigente`; el aporte de código lo mide RepoSense. El índice puede **bajar** al crecer el proyecto: se acumulan señales.

```mermaid
xychart-beta
    title "Índice POO/SOLID a lo largo de los commits"
    x-axis ["dbfd5be", "f3d175b", "493b71f", "ae28d33", "0e5cb85", "1120c1a", "086d3a6", "c792a2f", "8f4e517", "a3e5d7a", "24401a4", "b941d71", "6739160"]
    y-axis "Índice" 0 --> 100
    line [0, 0, 39, 35, 17, 9, 6, 5, 4, 5, 4, 4, 4]
```

| # | Fecha | Commit | Autor | LOC | Índice | %POO | Duplic. | Sueltas |
|---|---|---|---|---:|---:|---:|---:|---:|
| 1 | 2026-08-14 | `dbfd5be` | ibaldasso-elpepe | 0 | 0 | 0% | 0.0% | 0 |
| 2 | 2026-08-24 | `f3d175b` | dqmdz-etec | 0 | 0 | 0% | 0.0% | 0 |
| 3 | 2026-08-28 | `493b71f` | ibaldasso-elpepe | 180 | 39 | 46% | 0.0% | 1 |
| 4 | 2026-10-02 | `ae28d33` | ibaldasso-elpepe | 302 | 35 | 39% | 0.0% | 2 |
| 5 | 2026-10-05 | `0e5cb85` | ibaldasso-elpepe | 702 | 17 | 78% | 8.8% | 0 |
| 6 | 2026-10-09 | `1120c1a` | ibaldasso-elpepe | 1,469 | 9 | 90% | 4.3% | 1 |
| 7 | 2026-10-09 | `086d3a6` | ibaldasso-elpepe | 2,351 | 6 | 86% | 5.9% | 2 |
| 8 | 2026-10-16 | `c792a2f` | ibaldasso-elpepe | 3,625 | 5 | 87% | 6.6% | 2 |
| 9 | 2026-10-16 | `8f4e517` | ibaldasso-elpepe | 5,355 | 4 | 85% | 7.2% | 2 |
| 10 | 2026-10-23 | `a3e5d7a` | ibaldasso-elpepe | 4,849 | 5 | 87% | 6.7% | 0 |
| 11 | 2026-10-26 | `24401a4` | ibaldasso-elpepe | 5,221 | 4 | 90% | 1.9% | 0 |
| 12 | 2026-10-30 | `b941d71` | ibaldasso-elpepe | 5,534 | 4 | 89% | 1.8% | 0 |
| 13 | 2026-10-30 | `6739160` | ibaldasso-elpepe | 5,717 | 4 | 90% | 1.8% | 0 |

---
_Informe generado por la cátedra. Los criterios y umbrales de medición corresponden al perfil `exigente` de `calidad.py`; el semáforo de aprobación, a `CRITERIOS-CALIDAD.md` (ver `README.md`). Ante dudas, revisá el archivo señalado._
<!-- evaluacion:fin -->
