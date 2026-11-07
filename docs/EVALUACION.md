<!-- evaluacion:inicio -->
# Evaluación — nacho-baldasso-organizacion/Proyecto-Submarino-Prime

> Documento **generado automáticamente** por la cátedra de Programación II (RepoSense + análisis de calidad POO/SOLID). **No editar a mano**: se regenera en cada evaluación.
> Rama evaluada: `main`. Análisis **heurístico** (regex + métricas): es una criba para orientar la revisión, **no una nota**.
> Generado: 2026-11-06.

## 1. Veredicto

| Aspecto | Resultado |
|---|---|
| Índice de calidad POO/SOLID | **75/100** |
| Código (LOC) | 14,113 |
| Archivos de código | 192 |
| Clases (bien usadas) | 332 (126 OK) |
| Código dentro de clases (%POO) | 99% |
| Funciones fuera de clases | 0 |
| Duplicación lógica | 3.0% |
| Señales S/O/L/I/D | 38/0/0/0/47 |

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
| Señales POO/SOLID críticas | ninguna | Acoplamiento alto (CBO) | no |

**Criterios para destacar**

| Criterio | Requisito | Tu resultado | Cumple |
|---|---|---|---|
| Código dentro de clases (%POO) | ≥ 95% | 99% | sí |
| Duplicación lógica | ≤ 5% | 3.0% | sí |
| Funciones fuera de clases | ≤ 1 | 0 | sí |
| Señales POO/SOLID críticas | ninguna | Baja cohesión (LCOM), Acoplamiento alto (CBO) | no |
| Índice de calidad POO/SOLID | ≥ 60 | 75/100 | sí |

### Cómo se calcula el %POO (y por qué no sube solo)

`%POO = líneas de lógica dentro de clases ÷ líneas de lógica totales`.

Se cuentan las líneas de **lógica y campos**; no cuentan `package`/`import`/`using`/anotaciones ni comentarios, Javadoc o docstrings. Es una **proporción**: si agregás código nuevo dentro de una clase, el numerador y el denominador crecen juntos y el porcentaje casi no cambia.

Sube cuando el código que hoy está **fuera** de clases pasa a ser método de una clase (o desaparece): funciones sueltas, `main`, constantes de módulo, scripts y lógica de archivos sueltos. También importa que ese trabajo esté **integrado en `main`**: la evaluación siempre mide `main`, así que lo que sigue en otra rama no se refleja todavía.

## 2. Autoría (RepoSense)

| Autor | Commits | Código (LOC) | Tests (LOC) | Docs (LOC) | Días activos | % del código |
|---|---:|---:|---:|---:|---:|---:|
| ibaldasso-elpepe | 27 | 13,492 | 2,974 | 1 | 16 | 100% |

> _El LOC de autoría lo calcula RepoSense sobre el historial y puede diferir del LOC de código del análisis de calidad (que mira solo el estado de `main`)._

## 3. Fallas detectadas

Reúne lo que el análisis marcó y lo que todavía falta para cada semáforo. Cuando la señal se puede asociar a archivos, se indican para orientar la revisión. No es una nota.

| Falla | Tu resultado | Requisito | Archivos |
|---|---|---|---|
| Señales POO/SOLID críticas | Acoplamiento alto (CBO) | Aprobar: ninguna<br>Destacar: ninguna | `src/main/kotlin/org/example/issue1/FabricaSubsistemasSubmarino.kt`, `src/main/kotlin/org/example/issue1/GestorMejorasSubmarino.kt`, `src/main/kotlin/org/example/issue1/Submarino.kt`, `src/main/kotlin/submarino/ControladorTeclado.kt`, `src/main/kotlin/submarino/EstadoPartida.kt`, `src/main/kotlin/submarino/FabricaDominio.kt` (+15 más) |
| Métodos/funciones largos o con muchos parámetros | **25** método(s)/función(es) largos o con muchos parámetros | Sin señales | `src/main/kotlin/org/example/issue2/ReporteSimulacionSubmarino.kt`, `src/main/kotlin/submarino/AvatarSubmarino.kt`, `src/main/kotlin/submarino/CapaFondoParallax.kt`, `src/main/kotlin/submarino/ColorProfundidad.kt`, `src/main/kotlin/submarino/ConstructorAccionesTienda.kt`, `src/main/kotlin/submarino/ConstructorExplosionSubmarino.kt` (+18 más) |
| Dependencia de concretos y falta de abstracciones (DIP) | **47** dependencia(s) de concretos sin abstracciones | Sin señales | `src/main/kotlin/org/example/issue1/FabricaSubsistemasSubmarino.kt`, `src/main/kotlin/submarino/FabricaDominio.kt`, `src/main/kotlin/submarino/FabricaHUD.kt`, `src/main/kotlin/submarino/FabricaServicios.kt`, `src/main/kotlin/submarino/FabricaVisual.kt`, `src/main/kotlin/submarino/FondoParallaxManager.kt` (+40 más) |
| Clase sin métodos (solo datos) | **29** clase(s) sin métodos (solo datos) | Sin señales | `src/main/kotlin/org/example/issue2/CalculadoraHidrostatica.kt`, `src/main/kotlin/submarino/ColorProfundidad.kt`, `src/main/kotlin/submarino/ConfiguracionSesion.kt`, `src/main/kotlin/submarino/EstilosTienda.kt`, `src/main/kotlin/submarino/FabricaParticulasMarinas.kt`, `src/main/kotlin/submarino/FisicaAscenso.kt` (+19 más) |
| Campos públicos (encapsulación) | **58** clase(s) con campos públicos | Sin señales | `src/main/kotlin/org/example/issue1/EstadoSubmarino.kt`, `src/main/kotlin/org/example/issue1/Submarino.kt`, `src/main/kotlin/submarino/AvisosJuego.kt`, `src/main/kotlin/submarino/Bodega.kt`, `src/main/kotlin/submarino/CapaParallax.kt`, `src/main/kotlin/submarino/CatalogoMejorasSesion.kt` (+41 más) |

## 4. Aspectos evaluados

Cada aspecto indica **qué mide**, **por qué importa** y **tu resultado**. Cuando el análisis reportó una señal, se agregan preguntas para **investigar la causa** (no hay recetas: entender el porqué es parte de la evaluación).

### 1. Índice de calidad POO/SOLID (indice)

- **Qué mide:** Un resumen numérico (0-100) que combina la riqueza de clases bien usadas con la penalización por señales POO/SOLID y duplicación, y luego aplica topes duros.
- **Por qué importa:** Ordena y prioriza la revisión: un índice bajo indica varias señales que conviene mirar juntas.
- **Tu resultado:** índice **75/100**

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
- **Tu resultado:** **9** clase(s) con baja cohesión
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
- **Tu resultado:** **25** método(s)/función(es) largos o con muchos parámetros
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

### 12. Interfaz con demasiados métodos (ISP) (isp) — sin señales

- **Qué mide:** Interfaces o clases abstractas con muchos métodos declarados.
- **Por qué importa:** Obliga a implementar operaciones que no se usan y acopla a los clientes a métodos que no necesitan.
- **Tu resultado:** **0** interfaz(es) con demasiados métodos

### 13. Override vacío o 'no implementado' (LSP) (lsp) — sin señales

- **Qué mide:** Métodos sobrescritos que quedan vacíos o lanzan 'no implementado'.
- **Por qué importa:** Suele indicar que la jerarquía no representa bien el problema y rompe la sustitución entre clases.
- **Tu resultado:** **0** override(s) vacío(s) o 'no implementado'

### 14. Dependencia de concretos y falta de abstracciones (DIP) (dip) — con señales

- **Qué mide:** Instanciación directa de clases **propias** concretas y ausencia de interfaces o clases abstractas pese al acoplamiento. No cuentan los structs ni los nodos/tipos del motor.
- **Por qué importa:** Depender de implementaciones concretas dificulta cambiar o probar una parte sin arrastrar las demás.
- **Tu resultado:** **47** dependencia(s) de concretos sin abstracciones
- **Para investigar la causa:**
    - ¿La clase crea por dentro a sus colaboradores en vez de recibirlos?
    - ¿Hay puntos de variación que convendría expresar como abstracción?
    - ¿Cómo se probaría esa clase cambiando una implementación?

### 15. Clase sin métodos (solo datos) (data) — con señales

- **Qué mide:** Clases que solo acumulan campos y no tienen comportamiento.
- **Por qué importa:** Separar datos de comportamiento reparte por el proyecto la lógica que debería estar cerca de esos datos.
- **Tu resultado:** **29** clase(s) sin métodos (solo datos)
- **Para investigar la causa:**
    - ¿Dónde vive el comportamiento que opera sobre esos datos?
    - ¿La clase representa un concepto del dominio o un contenedor técnico?
    - ¿Se evita la lógica de negocio dentro de la clase por comodidad?

### 16. Campos públicos (encapsulación) (encapsulacion) — con señales

- **Qué mide:** Clases que exponen campos sin control de acceso.
- **Por qué importa:** Expone el estado interno y hace que cualquier cambio dependa de quién lo modifica desde afuera.
- **Tu resultado:** **58** clase(s) con campos públicos
- **Para investigar la causa:**
    - ¿Se accede al campo desde otras clases para evitar un método?
    - ¿Qué reglas deberían respetarse al modificar ese estado?
    - ¿Los campos públicos se usan más como configuración que como estado?

### 17. Clase solo con métodos estáticos (utilitaria) (static) — sin señales

- **Qué mide:** Clases donde todos los métodos son estáticos.
- **Por qué importa:** Es comportamiento procedural disfrazado de clase: no se instancia ni participa del modelo de objetos.
- **Tu resultado:** **0** clase(s) solo con métodos estáticos

## 5. Evolución por commit

Métricas calculadas sobre el estado de `main` en **cada commit** (22 commits con trabajo), del primero al último. La calidad usa el perfil `exigente`; el aporte de código lo mide RepoSense. El índice puede **bajar** al crecer el proyecto: se acumulan señales.

```mermaid
xychart-beta
    title "Índice POO/SOLID a lo largo de los commits"
    x-axis ["dbfd5be", "f3d175b", "493b71f", "ae28d33", "0e5cb85", "1120c1a", "086d3a6", "c792a2f", "8f4e517", "a3e5d7a", "24401a4", "b941d71", "6739160", "6a83318", "6aacd31", "1ed61bb", "ec547ac", "ebe5217", "7468e30", "bf43829", "23cb875", "7d876c5"]
    y-axis "Índice" 0 --> 100
    line [0, 0, 39, 47, 17, 9, 7, 6, 4, 5, 4, 4, 4, 4, 4, 2, 3, 3, 3, 3, 2, 2]
```

| # | Fecha | Commit | Autor | LOC | Índice | %POO | Duplic. | Sueltas |
|---|---|---|---|---:|---:|---:|---:|---:|
| 1 | 2026-08-14 | `dbfd5be` | ibaldasso-elpepe | 0 | 0 | 0% | 0.0% | 0 |
| 2 | 2026-08-24 | `f3d175b` | dqmdz-etec | 0 | 0 | 0% | 0.0% | 0 |
| 3 | 2026-08-28 | `493b71f` | ibaldasso-elpepe | 180 | 39 | 44% | 0.0% | 1 |
| 4 | 2026-10-02 | `ae28d33` | ibaldasso-elpepe | 302 | 47 | 38% | 0.0% | 1 |
| 5 | 2026-10-05 | `0e5cb85` | ibaldasso-elpepe | 702 | 17 | 93% | 8.8% | 0 |
| 6 | 2026-10-09 | `1120c1a` | ibaldasso-elpepe | 1,469 | 9 | 83% | 4.3% | 1 |
| 7 | 2026-10-09 | `086d3a6` | ibaldasso-elpepe | 2,351 | 7 | 83% | 5.9% | 1 |
| 8 | 2026-10-16 | `c792a2f` | ibaldasso-elpepe | 3,625 | 6 | 89% | 6.6% | 1 |
| 9 | 2026-10-16 | `8f4e517` | ibaldasso-elpepe | 5,355 | 4 | 91% | 7.2% | 1 |
| 10 | 2026-10-23 | `a3e5d7a` | ibaldasso-elpepe | 4,849 | 5 | 98% | 6.7% | 0 |
| 11 | 2026-10-26 | `24401a4` | ibaldasso-elpepe | 5,221 | 4 | 98% | 1.9% | 0 |
| 12 | 2026-10-30 | `b941d71` | ibaldasso-elpepe | 5,534 | 4 | 98% | 1.8% | 0 |
| 13 | 2026-10-30 | `6739160` | ibaldasso-elpepe | 5,717 | 4 | 98% | 1.8% | 0 |
| 14 | 2026-10-30 | `6a83318` | ibaldasso-elpepe | 6,090 | 4 | 98% | 1.7% | 0 |
| 15 | 2026-10-30 | `6aacd31` | ibaldasso-elpepe | 6,207 | 4 | 98% | 1.7% | 0 |
| 16 | 2026-10-30 | `1ed61bb` | ibaldasso-elpepe | 6,388 | 2 | 98% | 1.6% | 0 |
| 17 | 2026-10-31 | `ec547ac` | dqmdz-etec | 6,426 | 3 | 98% | 0.9% | 0 |
| 18 | 2026-10-31 | `ebe5217` | dqmdz-etec | 6,426 | 3 | 98% | 0.9% | 0 |
| 19 | 2026-10-31 | `7468e30` | dqmdz-etec | 6,426 | 3 | 98% | 0.9% | 0 |
| 20 | 2026-10-31 | `bf43829` | dqmdz-etec | 6,426 | 3 | 98% | 0.9% | 0 |
| 21 | 2026-11-02 | `23cb875` | ibaldasso-elpepe | 11,156 | 2 | 99% | 3.0% | 0 |
| 22 | 2026-11-02 | `7d876c5` | ibaldasso-elpepe | 11,251 | 2 | 99% | 3.0% | 0 |

<!-- actividad-clases:inicio -->
## Actividad en días de clase

> Período **2026-08-14 → 2026-10-29** · **17 clases** (lunes, viernes) · calendario oficial de la materia.
>
> `Commits` = commits de trabajo (no-merge); `Merges` = merges de PR (también son push). Los días sin actividad dicen **sin actividad**. El cuadro principal usa sólo las fechas de clase del calendario oficial.
>
> **Aportes fuera del calendario:** cada alumno tiene un cuadro aparte con las fechas no lectivas con actividad, desde **2026-08-14** hasta **2026-11-06** (ventana de aportes). Esas fechas no forman parte del cuadro principal, pero se muestran para no perder trabajo hecho en fines de semana, feriados o después del período.
>
> Se recorre `git log --all` sobre un mirror (todas las ramas y `refs/pull/*`), con deduplicación por commit. Fecha = fecha de autor en UTC-3. Los commits locales no pusheados no se ven. Si el trabajo fue en pareja y el commit es de otra persona, figura a nombre del autor del commit; para reflejarlo, usar `Co-authored-by` o una nota manual.
>
> Sección informativa: no modifica el semáforo de aprobación.

### ibaldasso-elpepe

**10/17 clases con actividad** · 14 commits · 9 merges

| Fecha | Marca | Repositorios | Commits / merges |
|---|:--:|---|--:|
| 2026-08-14 | **Sí** | Proyecto-Submarino-Prime | 1 / 0 |
| 2026-08-21 | No | sin actividad | 0 / 0 |
| 2026-08-24 | **Sí** | Proyecto-Submarino-Prime | 1 / 0 |
| 2026-08-28 | **Sí** | Proyecto-Submarino-Prime | 0 / 1 |
| 2026-08-31 | No | sin actividad | 0 / 0 |
| 2026-09-04 | No | sin actividad | 0 / 0 |
| 2026-09-07 | **Sí** | Proyecto-Submarino-Prime | 1 / 0 |
| 2026-09-14 | No | sin actividad | 0 / 0 |
| 2026-09-25 | No | sin actividad | 0 / 0 |
| 2026-09-28 | No | sin actividad | 0 / 0 |
| 2026-10-02 | **Sí** | Proyecto-Submarino-Prime | 1 / 1 |
| 2026-10-05 | **Sí** | Proyecto-Submarino-Prime | 0 / 1 |
| 2026-10-09 | **Sí** | Proyecto-Submarino-Prime | 5 / 2 |
| 2026-10-16 | **Sí** | Proyecto-Submarino-Prime | 3 / 2 |
| 2026-10-19 | No | sin actividad | 0 / 0 |
| 2026-10-23 | **Sí** | Proyecto-Submarino-Prime | 1 / 1 |
| 2026-10-26 | **Sí** | Proyecto-Submarino-Prime | 1 / 1 |

#### Aportes fuera del calendario de clases

**6 fechas fuera del calendario** · 15 commits · 13 merges

| Fecha | Repositorios | Commits / merges |
|---|---|--:|
| 2026-10-30 | Proyecto-Submarino-Prime | 9 / 6 |
| 2026-11-01 | Proyecto-Submarino-Prime | 1 / 1 |
| 2026-11-02 | Proyecto-Submarino-Prime | 1 / 2 |
| 2026-11-03 | Proyecto-Submarino-Prime | 1 / 0 |
| 2026-11-04 | Proyecto-Submarino-Prime | 2 / 3 |
| 2026-11-06 | Proyecto-Submarino-Prime | 1 / 1 |

<!-- actividad-clases:fin -->

---
_Informe generado por la cátedra. Los criterios y umbrales de medición corresponden al perfil `exigente` de `calidad.py`; el semáforo de aprobación, a `CRITERIOS-CALIDAD.md` (ver `README.md`). Ante dudas, revisá el archivo señalado._
<!-- evaluacion:fin -->
