# t1-ejercicios-sols — Soluciones del Tema 1

Soluciones comentadas de los **26 ejercicios** del Tema 1 de *Programación Avanzada (22354)*,
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
  colección interna, por qué `>=` y no `>`, por qué `Math.max(0, ...)`.
- **Bloques de respuesta al final de la clase** — las preguntas que el enunciado deja abiertas,
  contestadas. Incluidas las de **«Para pensar»**, que no tienen respuesta en el tema y son
  exactamente el tipo de pregunta del Examen Parcial.

## Comprobación

```bash
mvn test
```

Debe dar **172 pruebas, todas en verde**. Si alguna falla, la solución y su prueba se han
desincronizado: es el primer sitio donde mirar antes de tocar un enunciado.

De los 26 ejercicios, 22 traen sus pruebas escritas en la plantilla y los cuatro de la sección 1.4
son precisamente pruebas; aquí están todas, así que el proyecto cubre los 26.

Además hay cuatro programas que conviene ejecutar al menos una vez, porque su **salida** es parte
de la explicación:

```bash
mvn -q compile
java -cp target/classes es.uib.prgava.tema1.ejercicios.DemoEtiqueta         # 1.1.3
java -cp target/classes es.uib.prgava.tema1.ejercicios.DiagnosticoLiskov    # 1.2.3
java -cp target/classes es.uib.prgava.tema1.monitor.Principal               # 1.2.2, 1.2.6, 1.2.7
java -cp target/classes es.uib.prgava.tema1.ejercicios.DemoPoliticasLambda  # 1.3.4
```

## Dónde está cada ejercicio

| Ejercicio | Clases |
|---|---|
| 1.1.1 | `ejercicios/DireccionIpv4` (constructor compacto y `toString`) |
| 1.1.2 | `ejercicios/Servidor` |
| 1.1.3 | `ejercicios/Etiqueta`, `ejercicios/DemoEtiqueta` |
| 1.1.4 | `ejercicios/PuntoAcceso` |
| 1.1.5 | `ejercicios/PanelDeEstado` (y `PanelDeEstadoHeredado`, sin corregir, como testigo) |
| 1.1.6 | `ejercicios/Sensor`, `SensorTemperatura`, `SensorHumedad` |
| 1.1.7 | `ejercicios/Identificable`, `ejercicios/Inventario` |
| 1.1.8 | `ejercicios/DireccionIpv4` (`compareTo`) |
| 1.2.1 | `ejercicios/RegistroSensores` (fachada) + `LecturasSensor`, `MediaDeLecturas`, `DecisionDeAviso`, `TextoDelAviso` |
| 1.2.2 | `monitor/FallosEnVentana` |
| 1.2.3 | `ejercicios/ListaSoloLectura`, `ejercicios/DiagnosticoLiskov` |
| 1.2.4 | `ejercicios/GestorDispositivos` + `AltaDeDispositivos`, `ConsultaDeDispositivos`, `ExportacionCsv`, `ExportacionJson` |
| 1.2.5 | `ejercicios/InformeDiario`, `Salida`, `SalidaPorConsola` |
| 1.2.6 | `monitor/NotificadorSinRepetir` |
| 1.2.7 | `ejercicios/DireccionIpv4` (`desde`), `monitor/ServicioPing`, `monitor/Servicio` |
| 1.2.8 | `monitor/SondaDeterminista` |
| 1.3.1 | `ejercicios/Historial` |
| 1.3.2 | `ejercicios/UtilidadesEjercicio` |
| 1.3.3 | `ejercicios/CopiasEjercicio` |
| 1.3.4 | `ejercicios/Criterios`, `ejercicios/DemoPoliticasLambda` |
| 1.3.5 y 1.3.6 | `ejercicios/InformesEjercicio` |
| 1.4.1 | `monitor/FallosEnVentanaTest` |
| 1.4.2 | `ejercicios/DireccionIpv4Test` |
| 1.4.3 | `monitor/SondaProgramada`, `NotificadorEspia`, `MonitorEjercicioTest` |
| 1.4.4 | `ejercicios/DetectorDeCaidas`, `ejercicios/DetectorDeCaidasTest` |

Las clases de `poo`, `genericos` y el resto de `monitor` son copia exacta del código del tema, la
misma que hay en [`t1-ejemplos`](https://github.com/UIB-22354-Programacion-Avanzada/t1-ejemplos),
con dos excepciones que los ejercicios obligan a tocar: `monitor/Servicio` reconoce el prefijo
`ping:` (1.2.7) y `monitor/Principal` cablea la política y el notificador nuevos (1.2.2 y 1.2.6).

## Diferencias respecto a la plantilla

Lo que hay aquí y no en `t1-ejercicios`:

- los cuerpos de todos los métodos, comentados;
- las clases que el estudiante tiene que crear y que por tanto no podían estar en el esqueleto:
  las cuatro de la separación de `RegistroSensores`, las cuatro interfaces de la división de
  `GestorDispositivos`, `SalidaPorConsola`, `DiagnosticoLiskov` y `DemoPoliticasLambda`;
- las pruebas de la sección 1.4, que en la plantilla son el entregable;
- en `CopiasEjercicioTest`, las dos pruebas que allí están comentadas porque no compilan hasta
  aplicar PECS;
- en `GestorDispositivosTest`, la prueba que allí queda marcada con un `// TODO`.

Nada de lo demás cambia: los ficheros de configuración, el `pom.xml` y el flujo de trabajo son los
mismos, de modo que un `diff -r` entre los dos repositorios muestra exactamente el trabajo del
alumnado.

## Cómo usarlo en clase

Las soluciones no están pensadas para entregarse, sino para **corregir y discutir**. Los bloques de
respuesta al final de cada clase están escritos como se espera que las conteste el alumnado en el
Examen Parcial: en prosa, nombrando el principio y explicando la consecuencia práctica. Sirven de
rúbrica.

Conviene no publicar esta página ni este repositorio junto a los enunciados. La página de
soluciones del sitio web se mantiene vacía mientras los ejercicios estén en uso.

## Licencia

Mismo régimen que `t1-ejercicios`: el código, bajo licencia [MIT](LICENSE) — copyright © 2026
Alejandro Mesejo. El material docente del sitio web, bajo
[CC BY-NC-SA 4.0](https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en).
