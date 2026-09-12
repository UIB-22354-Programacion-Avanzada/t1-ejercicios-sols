package es.uib.prgava.tema1.ejercicios;

import java.time.Duration;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;

import es.uib.prgava.tema1.monitor.Estado;
import es.uib.prgava.tema1.monitor.Resultado;
import es.uib.prgava.tema1.monitor.Servicio;
import es.uib.prgava.tema1.monitor.Sonda;

/**
 * Ejercicio 1.3.5. <strong>Solución comentada.</strong> Comportamiento construido componiendo
 * funciones.
 *
 * <p>La restricción del enunciado —ningún {@code if}— no es un capricho. Obliga a expresar cada
 * criterio como un <em>valor</em> que se puede guardar, pasar como argumento y combinar con
 * otros, en vez de como una estructura de control enterrada dentro de un método. Un {@code if}
 * solo se puede leer; un {@code Predicate} se puede reutilizar, componer y probar por separado.
 *
 * <p>Es la misma idea que el patrón Strategy del Tema 1.2, pero sin escribir clases: las
 * interfaces funcionales y las lambdas hacen innecesaria buena parte del andamiaje que los
 * patrones clásicos necesitaban en los años noventa.
 */
public final class Criterios {

    private Criterios() { }

    /**
     * Una referencia a método, {@code Resultado::esFallo}, ya <em>es</em> un
     * {@code Predicate<Resultado>}: el compilador comprueba que la firma encaja con el único
     * método abstracto de la interfaz. No hace falta escribir {@code r -> r.esFallo()}.
     */
    public static final Predicate<Resultado> ES_FALLO = Resultado::esFallo;

    /**
     * Ordena por nombre de servicio ascendente y, a igualdad, por latencia descendente.
     *
     * <p>Construirlo con {@code comparing} y {@code thenComparing} en vez de escribir a mano un
     * {@code compare} con restas y {@code if} anidados elimina de golpe los errores clásicos de
     * los comparadores: el desbordamiento al restar enteros, el criterio no transitivo y el
     * olvido de un caso de empate.
     *
     * <p>El {@code reverseOrder()} en la segunda clave invierte solo esa clave, no el comparador
     * entero: primero el nombre de menor a mayor, y dentro de cada servicio la latencia de mayor
     * a menor.
     */
    public static final Comparator<Resultado> POR_SERVICIO_Y_LATENCIA =
            Comparator.comparing((Resultado r) -> r.servicio().nombre())
                      .thenComparing(Resultado::latencia, Comparator.reverseOrder());

    /**
     * {@code compareTo(umbral) > 0} es la forma correcta de decir «mayor que» entre
     * {@code Duration}: son objetos, así que {@code >} no compila, y comparar
     * {@code toMillis()} perdería precisión por debajo del milisegundo.
     */
    public static Predicate<Resultado> masLentoQue(Duration umbral) {
        return resultado -> resultado.latencia().compareTo(umbral) > 0;
    }

    /**
     * Compuesto, no reescrito: {@code or} combina dos predicados en uno. Si mañana cambia la
     * definición de fallo, {@code problematico} se entera sola, porque no repite la lógica.
     */
    public static Predicate<Resultado> problematico(Duration umbral) {
        return ES_FALLO.or(masLentoQue(umbral));
    }

    /**
     * Produce textos como {@code DNS:UIB.ES (123 ms)}, componiendo dos funciones con
     * {@code andThen}.
     *
     * <p>{@code f.andThen(g)} significa «primero f, después g». Existe también
     * {@code compose}, que es al revés. Aquí la primera función extrae el nombre del servicio y
     * la segunda lo pasa a mayúsculas; el resto se añade al final.
     */
    public static Function<Resultado, String> etiqueta() {
        Function<Resultado, String> nombre = r -> r.servicio().nombre();
        Function<String, String> mayusculas = String::toUpperCase;

        return resultado -> nombre.andThen(mayusculas).apply(resultado)
                + " (" + resultado.latencia().toMillis() + " ms)";
    }

    /**
     * Devuelve una lambda que implementa {@link Sonda} y responde siempre con ese estado.
     *
     * <p>Que esto se pueda escribir así depende de que {@code Sonda} sea una interfaz funcional
     * —un solo método abstracto—. La lambda <strong>es</strong> una sonda de pleno derecho: se
     * puede pasar a {@code Monitor} igual que {@code SondaSimulada}. Compara con el doble de
     * prueba escrito a mano del ejercicio 1.4.3: la lambda sirve cuando el comportamiento es
     * fijo; la clase, cuando hay que recordar estado entre llamadas.
     *
     * <p>El {@code switch} sobre el enum sustituye a la cadena de {@code if}, y al ser
     * exhaustivo el compilador avisará si algún día se añade un cuarto estado.
     */
    public static Sonda sondaConstante(Estado estado) {
        return (Servicio servicio) -> switch (estado) {
            case ACTIVO    -> Resultado.activo(servicio, Duration.ofMillis(100));
            case DEGRADADO -> Resultado.degradado(servicio, Duration.ofMillis(800));
            case CAIDO     -> Resultado.caido(servicio);
        };
    }
}
