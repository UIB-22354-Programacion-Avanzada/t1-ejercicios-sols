package es.uib.prgava.tema1.ejercicios;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import es.uib.prgava.tema1.monitor.Estado;
import es.uib.prgava.tema1.monitor.Resultado;
import es.uib.prgava.tema1.monitor.Servicio;

/**
 * Ejercicios 1.3.5 y 1.3.6. <strong>Solución comentada.</strong> Informes sobre un historial con la
 * Stream API.
 *
 * <p>Cada método es una tubería de tres partes que se lee de arriba abajo: de dónde salen los datos
 * ({@code stream}), qué se hace con ellos ({@code filter}, {@code map}) y cómo se recogen
 * ({@code count}, {@code average}, {@code toList}, {@code collect}). Escrita así, la intención está en
 * la primera lectura; con un bucle hay que reconstruirla a partir de acumuladores.
 */
public final class InformesEjercicio {

    private InformesEjercicio() {
    }

    /**
     * {@code count()} devuelve {@code long} y no {@code int}, porque un flujo puede tener más
     * elementos de los que cabe en un {@code int}. Conviene no convertirlo «para que quede más
     * limpio»: el tipo dice algo.
     */
    public static long fallos(List<Resultado> historial) {
        return historial.stream()
                .filter(Resultado::esFallo)
                .count();
    }

    /**
     * {@code mapToLong} produce un flujo de primitivos, y su {@code average()} devuelve un
     * {@code OptionalDouble}: la media de un conjunto vacío no existe, y el tipo lo dice.
     *
     * <p>Aquí se decide devolver {@code 0.0} cuando no hay datos, con {@code orElse(0.0)}, porque el
     * método promete un {@code double}. La alternativa honesta sería devolver {@code OptionalDouble} y
     * dejar la decisión a quien llama, que es lo que hace {@code Informes.disponibilidad} en el
     * monitor. Las dos son defendibles; lo que no lo es es devolver un cero sin darse cuenta de que se
     * está eligiendo, porque cero y «no hay datos» no son lo mismo en un informe de disponibilidad.
     */
    public static double latenciaMediaEnMilisegundos(List<Resultado> historial) {
        return historial.stream()
                .mapToLong(resultado -> resultado.latencia().toMillis())
                .average()
                .orElse(0.0);
    }

    /**
     * {@code distinct()} es lo que quita las repeticiones, y funciona porque los servicios son
     * registros y traen {@code equals} y {@code hashCode} escritos. Si fueran clases con la igualdad
     * por identidad, dos servicios «iguales» no se considerarían repetidos y este método devolvería
     * duplicados: es la misma dependencia del ejercicio 1.1.3, ahora en un sitio donde no se ve venir.
     *
     * <p>{@code toList()} devuelve una lista inmutable, que es lo que se quiere en un informe.
     */
    public static List<Servicio> serviciosConAlgunFallo(List<Resultado> historial) {
        return historial.stream()
                .filter(Resultado::esFallo)
                .map(Resultado::servicio)
                .distinct()
                .toList();
    }

    /**
     * Ejercicio 1.3.6. {@code groupingBy} con {@code counting}: el colector que cuenta por grupos.
     *
     * <p>Escrito a mano serían siete u ocho líneas con un {@code Map} y un {@code merge}; aquí es una.
     * Nótese que los estados sin ninguna comprobación <strong>no aparecen</strong> en el mapa: no hay
     * una entrada con valor cero, hay ausencia. Quien lo use tiene que contar con eso, y por eso la
     * prueba consulta las tres claves que sí existen.
     */
    public static Map<Estado, Long> comprobacionesPorEstado(List<Resultado> historial) {
        return historial.stream()
                .collect(Collectors.groupingBy(Resultado::estado, Collectors.counting()));
    }

    /**
     * Ejercicio 1.3.6, segunda parte, y la más interesante: no se agrupa por un campo, se agrupa por
     * algo que hay que calcular antes —el <em>último</em> estado de cada servicio—.
     *
     * <p>Son dos pasos y conviene verlos separados. El primero reduce el historial a «servicio →
     * último estado»: {@code toMap} con una función de fusión {@code (viejo, nuevo) -> nuevo}, que es
     * lo que hace que gane el más reciente, aprovechando que el historial llega en orden. El segundo
     * invierte ese mapa agrupando por el valor, con {@code mapping} para quedarse solo con las claves.
     *
     * <p>El {@code LinkedHashMap} del primer paso no es decorativo: sin él, el orden de los servicios
     * dentro de cada grupo dependería de la tabla hash y podría cambiar entre ejecuciones. Una prueba
     * que compare listas fallaría un día de cada cinco, que es la peor clase de prueba.
     */
    public static Map<Estado, List<Servicio>> serviciosPorUltimoEstado(List<Resultado> historial) {
        Map<Servicio, Estado> ultimoDeCadaUno = historial.stream()
                .collect(Collectors.toMap(
                        Resultado::servicio,
                        Resultado::estado,
                        (viejo, nuevo) -> nuevo,          // gana el más reciente
                        LinkedHashMap::new));             // orden estable

        return ultimoDeCadaUno.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())));
    }

    /**
     * Ejercicio 1.3.5, último paso: el mismo cálculo que {@link #fallos(List)} con un bucle, para
     * poder comparar.
     *
     * <p>Y la respuesta al «Para pensar» —cuál de las dos defender en una revisión—: la tubería, y no
     * porque sea más corta. Porque dice <em>qué</em> se calcula y no <em>cómo</em>: no hay acumulador
     * que inicializar, ni índice que equivocar, ni posibilidad de salirse del bucle antes de tiempo.
     * El bucle gana cuando hay que salir a la mitad, cuando hace falta el índice, cuando el cuerpo
     * tiene efectos o lanza excepciones comprobadas, y cuando la tubería necesitaría tres
     * {@code flatMap} anidados para expresar algo que en un bucle se lee. La regla del tema: si la
     * tubería se lee peor que el bucle, escribe el bucle.
     */
    public static long fallosConBucle(List<Resultado> historial) {
        long cuantos = 0;
        for (var resultado : historial) {
            if (resultado.esFallo()) {
                cuantos++;
            }
        }
        return cuantos;
    }
}
