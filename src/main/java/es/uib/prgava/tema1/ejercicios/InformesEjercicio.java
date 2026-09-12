package es.uib.prgava.tema1.ejercicios;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import es.uib.prgava.tema1.monitor.Estado;
import es.uib.prgava.tema1.monitor.LectorResultados;
import es.uib.prgava.tema1.monitor.Resultado;
import es.uib.prgava.tema1.monitor.Servicio;

/**
 * Ejercicios 1.3.6, 1.3.7 y 1.3.8. <strong>Solución comentada.</strong>
 *
 * <p>Todos los métodos siguen el mismo esqueleto: una <em>fuente</em> (el historial), una
 * <em>cadena de operaciones intermedias</em> ({@code filter}, {@code map}, {@code sorted}) y una
 * <em>operación terminal</em> que produce el resultado ({@code collect}, {@code count},
 * {@code average}). Reconocer ese patrón es lo que hace legibles los pipelines ajenos.
 */
public final class InformesEjercicio {

    private InformesEjercicio() { }

    // ---------- 1.3.6 ----------

    /**
     * Cuántos resultados hay de cada estado. El mapa se itera en el orden del enum.
     *
     * <p>{@code groupingBy} de tres argumentos: clasificador, <em>fábrica del mapa</em> y colector
     * aguas abajo. La fábrica es la pieza que resuelve el requisito del orden: un
     * {@code EnumMap} recorre sus claves en el orden de declaración del enum —ACTIVO, DEGRADADO,
     * CAIDO—, mientras que el {@code HashMap} por defecto las devolvería en un orden arbitrario.
     * La prueba comprueba el orden explícitamente, así que no es un detalle cosmético.
     */
    public static Map<Estado, Long> recuentoGlobal(List<Resultado> historial) {
        return historial.stream()
                .collect(Collectors.groupingBy(
                        Resultado::estado,
                        () -> new EnumMap<>(Estado.class),
                        Collectors.counting()));
    }

    /**
     * Latencia media en milisegundos de un servicio, excluyendo los fallos.
     *
     * <p>Excluirlos importa: {@code Resultado.caido} guarda {@code Duration.ZERO} como latencia,
     * de modo que contar los fallos hundiría la media hacia cero y daría una cifra que no
     * significa nada. Un caído no es «una respuesta de 0 ms», es «no hubo respuesta».
     *
     * <p>{@code mapToLong} produce un {@code LongStream} de primitivos, y su {@code average}
     * devuelve {@code OptionalDouble}: vacío si no queda ninguna comprobación correcta. Otra vez,
     * el tipo expresa la ausencia sin recurrir a un valor centinela.
     */
    public static OptionalDouble latenciaMediaDe(List<Resultado> historial, Servicio servicio) {
        return historial.stream()
                .filter(r -> r.servicio().equals(servicio))
                .filter(r -> !r.esFallo())
                .mapToLong(r -> r.latencia().toMillis())
                .average();
    }

    /**
     * Servicios con al menos ese número de fallos, de más a menos fallos y, a igualdad, por
     * nombre.
     *
     * <p>El desempate por nombre no es decorativo: sin él, dos servicios con el mismo número de
     * fallos saldrían en un orden que depende del recorrido interno del mapa, y la prueba
     * pasaría o fallaría según el día. Un orden total explícito hace la salida determinista.
     */
    public static List<Servicio> serviciosConAlMenos(List<Resultado> historial, int minimoFallos) {
        Map<Servicio, Long> fallosPorServicio = historial.stream()
                .filter(Resultado::esFallo)
                .collect(Collectors.groupingBy(Resultado::servicio, Collectors.counting()));

        return fallosPorServicio.entrySet().stream()
                .filter(e -> e.getValue() >= minimoFallos)
                .sorted(Map.Entry.<Servicio, Long>comparingByValue().reversed()
                        .thenComparing(e -> e.getKey().nombre()))
                .map(Map.Entry::getKey)
                .toList();
    }

    // ---------- 1.3.7 ----------

    /**
     * Con {@code partitioningBy}. La clave {@code true} agrupa los fallos.
     *
     * <p>Diferencia con {@code groupingBy} sobre un predicado: {@code partitioningBy} garantiza
     * que el mapa tiene <strong>siempre</strong> las dos claves, aunque una de las partes esté
     * vacía. Con {@code groupingBy}, si no hubiera ningún fallo, {@code get(true)} devolvería
     * {@code null} y habría que defenderse de ello en cada uso.
     */
    public static Map<Boolean, List<Resultado>> particionPorFallo(List<Resultado> historial) {
        return historial.stream().collect(Collectors.partitioningBy(Resultado::esFallo));
    }

    /**
     * Fracción de comprobaciones correctas por servicio, en [0, 1]. Con {@code groupingBy}.
     *
     * <p>{@code averagingDouble} sobre un 1/0 es un truco útil: la media de una secuencia de unos
     * y ceros es exactamente la proporción de unos. Evita agrupar dos veces (una para contar el
     * total y otra para contar las correctas) y dividir después.
     */
    public static Map<Servicio, Double> disponibilidades(List<Resultado> historial) {
        return historial.stream()
                .collect(Collectors.groupingBy(
                        Resultado::servicio,
                        Collectors.averagingDouble(r -> r.esFallo() ? 0.0 : 1.0)));
    }

    /** Estadísticas de latencia por servicio, excluyendo los fallos. */
    public static Map<Servicio, LongSummaryStatistics> estadisticasLatencia(List<Resultado> historial) {
        return historial.stream()
                .filter(r -> !r.esFallo())
                .collect(Collectors.groupingBy(
                        Resultado::servicio,
                        // summarizingLong calcula de una pasada recuento, suma, mínimo, máximo y
                        // media. Pedirlos por separado recorrería la colección cinco veces.
                        Collectors.summarizingLong(r -> r.latencia().toMillis())));
    }

    /**
     * Una línea por servicio, de peor a mejor disponibilidad, construida con {@code joining} y
     * {@code String.format}.
     *
     * <p>El {@code Locale} explícito es importante y se olvida siempre: sin él,
     * {@code String.format} usa el del sistema, y el mismo programa escribiría «66,7» en un
     * ordenador español y «66.7» en uno inglés. Un fichero de salida que cambia de formato según
     * dónde se ejecute el programa es una fuente inagotable de problemas.
     */
    public static String tabla(List<Resultado> historial) {
        var latencias = estadisticasLatencia(historial);

        return disponibilidades(historial).entrySet().stream()
                .sorted(Map.Entry.comparingByValue())          // de peor a mejor
                .map(e -> String.format(Locale.of("es", "ES"),
                        "%-28s %6.1f %%   latencia media %5.0f ms",
                        e.getKey().nombre(),
                        e.getValue() * 100,
                        latencias.containsKey(e.getKey())
                                ? latencias.get(e.getKey()).getAverage()
                                : 0.0))
                .collect(Collectors.joining(System.lineSeparator()));
    }

    // ---------- 1.3.8 ----------

    /**
     * Todos los resultados de todos los servicios, ordenados por instante. Con {@code flatMap}.
     *
     * <p>{@code flatMap} aplana un stream de streams en uno solo: aquí, de «un stream de
     * servicios, cada uno con su lista de resultados» a «un stream de resultados». Es la
     * operación que convierte una estructura anidada en una plana, y sustituye al bucle dentro
     * de otro bucle.
     */
    public static List<Resultado> todos(LectorResultados lector) {
        return lector.servicios().stream()
                .flatMap(servicio -> lector.historial(servicio).stream())
                .sorted(Comparator.comparing(Resultado::instante))
                .toList();
    }

    /**
     * Mayor número de fallos consecutivos. Implementado con un bucle.
     *
     * <p>Se lee de un vistazo: una racha actual, un máximo, y un recorrido. Cinco líneas.
     */
    public static int rachaMaximaDeFallos(List<Resultado> historial) {
        int maxima = 0;
        int actual = 0;
        for (Resultado resultado : historial) {
            actual = resultado.esFallo() ? actual + 1 : 0;
            maxima = Math.max(maxima, actual);
        }
        return maxima;
    }

    /**
     * La misma función con {@code reduce}. Se conserva aunque quede peor: la comparación es el
     * ejercicio.
     *
     * <p>El problema es que la racha máxima necesita <strong>dos</strong> acumuladores —la racha
     * actual y el máximo hasta ahora— y {@code reduce} solo ofrece uno. Hay que meter los dos en
     * un array de dos posiciones, y con eso se pierde exactamente lo que hacía atractivo el
     * estilo funcional: la ausencia de estado mutable.
     */
    public static int rachaMaximaConReduce(List<Resultado> historial) {
        int[] estado = historial.stream()
                .reduce(new int[] {0, 0},               // {racha actual, máxima}
                        (acumulado, resultado) -> {
                            int actual = resultado.esFallo() ? acumulado[0] + 1 : 0;
                            return new int[] {actual, Math.max(acumulado[1], actual)};
                        },
                        // Combinador, necesario para que la firma sea correcta aunque en un
                        // stream secuencial no llegue a usarse. Y ojo: este combinador NO es
                        // correcto en paralelo —dos mitades pueden tener una racha que cruce la
                        // frontera—, lo que ya es un aviso de que el problema no es paralelizable
                        // sin más cuidado.
                        (a, b) -> new int[] {b[0], Math.max(a[1], b[1])});
        return estado[1];
    }

    // Respuesta al TODO 1.3.8 (comparación de las dos versiones):
    //
    // En producción pondría la del bucle, sin dudarlo.
    //
    // 1. Se lee mejor. El bucle dice literalmente lo que hace: llevar una racha y un máximo.
    //    La versión con reduce obliga a descifrar qué significa cada posición del array.
    // 2. El array de dos enteros es estado mutable disfrazado. Usar un estilo funcional para
    //    acabar simulando dos variables locales es peor que usar dos variables locales.
    // 3. Asigna un array nuevo por cada elemento del historial, frente a dos enteros en la pila.
    // 4. El combinador no es correcto en paralelo, así que ni siquiera se gana la ventaja
    //    teórica que justificaría el estilo.
    //
    // La moraleja no es que los Streams sean peores: los métodos de 1.3.6 y 1.3.7 son mucho
    // mejores que sus equivalentes con bucles, porque son transformaciones y agregaciones, que
    // es para lo que se diseñaron. La racha máxima es un recorrido con memoria entre elementos,
    // y para eso el bucle es la herramienta adecuada. Elegir bien es saber cuándo cada una.
}
