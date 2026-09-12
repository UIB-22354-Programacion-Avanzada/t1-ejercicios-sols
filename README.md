# t1-ejercicios-sols — Soluciones del Tema 1

Soluciones comentadas de los **28 ejercicios** del Tema 1 de *Programación Avanzada (22354)*,
Grado en Ingeniería Telemática, UIB-EPS.

> ⚠️ **Repositorio privado, uso docente.** Es la contrapartida de
> [`t1-ejercicios`](https://github.com/UIB-22354-Programacion-Avanzada/t1-ejercicios), que es el
> esqueleto que resuelve el alumnado. No debe hacerse público mientras los ejercicios estén en
> uso: los enunciados son la base de los Talleres y del Examen Parcial.

## Qué tiene de distinto

Mismo proyecto Maven, mismas clases, mismas pruebas — pero **todo resuelto y todas las pruebas en
verde**. Y, sobre todo, **comentado**: cada solución explica no solo qué hace el código, sino por
qué se ha elegido esa forma y qué habría pasado con las alternativas.

Los comentarios están escritos para ser leídos, no para adornar. Tres tipos:

- **Javadoc de la clase** — la decisión de diseño de fondo: por qué un `record` y no una clase,
  por qué composición y no herencia, qué principio SOLID está en juego.
- **Comentarios de línea** — el detalle que se pasa por alto: por qué `List.copyOf` y no la
  colección interna, por qué `>=` y no `>`, por qué `Locale` explícito.
- **Bloques de respuesta al final de la clase** — las preguntas que el enunciado pide contestar
  por escrito, contestadas. Son las que se preguntan en el examen.

## Comprobación

```bash
mvn test
```

Debe terminar en `BUILD SUCCESS` con **todas** las pruebas pasando. Compara con el esqueleto,
donde lo correcto es que fallen 86 de 88.

Las pruebas incluyen las que en el esqueleto son el entregable del alumnado —los ejercicios 1.4.1
a 1.4.5—, aquí escritas: `FallosEnVentanaTest`, `DireccionIpv4Test`, `MonitorTest`,
`ConfiguracionMonitorTest` y `DetectorDeCaidasTest`. También están descomentadas
`ServicioPingTest` (1.1.5) y la última prueba de `CopiasEjercicioTest` (1.3.3), que en el
esqueleto ni siquiera compilan hasta resolver el ejercicio.

Programas ejecutables, por si quieres enseñar la salida en clase:

```bash
mvn -q compile
java -cp target/classes es.uib.prgava.tema1.ejercicios.DemoEtiqueta
java -cp target/classes es.uib.prgava.tema1.ejercicios.LectorUmbrales
java -cp target/classes es.uib.prgava.tema1.monitor.Principal servicios.txt
```

`LectorUmbrales` termina lanzando una excepción **a propósito**: el ejercicio 1.1.6 consiste en
leer su stack trace, que está transcrito y comentado al final de esa clase.

## Diferencias respecto al esqueleto, una por una

| Ejercicio | Qué cambia respecto a `t1-ejercicios` |
|---|---|
| 1.1.1 | `DireccionIpv4` completa; validación en el constructor compacto |
| 1.1.2 | `Servidor` con contador estático, `equals`/`hashCode` por dirección y puerto |
| 1.1.3 | `Etiqueta` con `hashCode`; `DemoEtiqueta` con la salida antes y después |
| 1.1.4 | `PuntoAcceso` con validación de canal extraída a método privado |
| 1.1.5 | **Clase nueva** `ServicioPing`, más `permits` y `Descripciones` ampliados |
| 1.1.6 | `LectorUmbrales` con `try-with-resources` y causas encadenadas |
| 1.2.1 | `EstadisticasSensor` y `ExportadorCsv` separadas |
| 1.2.2 | `FallosEnVentana` implementada |
| 1.2.3 | `ListaSoloLectura` por composición |
| 1.2.4 | Las tres interfaces de rol, con `GestorDispositivos` como suma |
| 1.2.5 | `InformeDiario` con `LectorResultados` y `Consumer` inyectados |
| 1.2.6 | `NotificadorFichero` en modo APPEND |
| 1.2.7 | `SondaConRegistro`, con la nota sobre el orden de los decoradores |
| 1.2.8 | `PoliticaDeAlerta.desde` |
| 1.2.9 | `ConfiguracionInforme` con Builder e `InformeResumen` |
| 1.3.1–1.3.5 | `Historial`, `UtilidadesEjercicio`, `CopiasEjercicio` (PECS), `TestigoDeTipo`, `Borrado` documentado, `Criterios` |
| 1.3.6–1.3.8 | `InformesEjercicio` completa, con las dos versiones de la racha máxima |
| 1.4.1–1.4.5 | Las cinco clases de prueba escritas, más `NotificadorEspia`, `SondaProgramada` y `DetectorDeCaidas` refactorizado con `Clock` |

## Una corrección que conviene llevar también al esqueleto

Los ficheros `umbrales.txt` y `umbrales-con-error.txt` de `t1-ejercicios` se generaron con `\n`
literales en lugar de saltos de línea reales: son una sola línea. Aquí están corregidos. Las
pruebas no lo detectan porque `LectorUmbralesTest` crea sus propios ficheros temporales, pero
`LectorUmbrales.main` sí los usa.

## Licencia

Mismo copyright que el resto del material del curso. **No redistribuir**: este repositorio existe
para el profesorado de la asignatura.
