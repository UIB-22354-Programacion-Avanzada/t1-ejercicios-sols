package es.uib.prgava.tema1.ejercicios;

import java.util.List;

/**
 * Ejercicio 1.3.4. <strong>Solución comentada.</strong> Recuperar en ejecución el tipo que el
 * borrado elimina.
 *
 * <p>El <em>borrado de tipos</em> (<em>type erasure</em>) significa que los parámetros genéricos
 * existen solo en compilación: en ejecución, una {@code List<String>} y una {@code List<Integer>}
 * son la misma clase, {@code java.util.List}, y no hay forma de preguntarle a un objeto por su
 * parámetro de tipo. Fue el precio de introducir los genéricos en Java 5 sin romper el código
 * anterior — a diferencia de C#, que los reificó a costa de una máquina virtual nueva.
 *
 * <p>El <strong>testigo de tipo</strong> ({@code type token}) es el rodeo estándar: si el tipo no
 * sobrevive a la compilación, se pasa explícitamente como dato, en forma de {@code Class<T>}.
 * Lo verás en {@code Collections.checkedList}, en {@code EnumMap} y en casi toda biblioteca que
 * necesite el tipo en ejecución.
 */
public final class TestigoDeTipo {

    private TestigoDeTipo() { }

    /**
     * Elementos de la lista que son del tipo indicado.
     *
     * <p>{@code isInstance} es el {@code instanceof} en versión dinámica, y {@code cast} es el
     * <em>cast</em> en versión dinámica. La diferencia con {@code (T) elemento} es sustancial:
     * aquella conversión es <strong>no comprobada</strong> —el compilador avisa, y el error
     * saldría más tarde, en el punto donde alguien usara el elemento— mientras que
     * {@code tipo.cast} comprueba de verdad en ese mismo instante. Por eso este método no
     * necesita {@code @SuppressWarnings} ni produce ningún aviso: no hay nada que suprimir.
     */
    public static <T> List<T> filtrarPorTipo(List<?> lista, Class<T> tipo) {
        return lista.stream()
                .filter(tipo::isInstance)
                .map(tipo::cast)
                .toList();
    }

    // Respuesta al TODO 1.3.4 (por qué filtrarPorTipo(lista, List.class) no puede devolver
    // una List<List<String>> comprobada):
    //
    // Porque el literal 'List.class' tiene tipo Class<List>, no Class<List<String>>: el tipo
    // List<String>.class NI SIQUIERA SE PUEDE ESCRIBIR en Java, precisamente por el borrado.
    // Solo existe un objeto Class para List, compartido por List<String>, List<Integer> y
    // todas las demás.
    //
    // En consecuencia, tipo.cast solo puede comprobar "esto es una List", y no "esto es una
    // List cuyos elementos son String". El método devolvería una List<List> con elementos que
    // el compilador tratará como listas de cualquier cosa, y el primer intento de leer un
    // String de ahí sería un cast no comprobado, con su ClassCastException esperando en
    // ejecución.
    //
    // La comprobación de tipo solo es fiable para tipos NO genéricos —String.class,
    // PuertoTcp.class, Integer.class—, que es como lo usan las pruebas. Para tipos genéricos
    // anidados hay que recurrir a trucos como el "super type token" de Gafter, que captura el
    // tipo a través de una subclase anónima porque la firma genérica de una clase SÍ queda en
    // el fichero .class, aunque no la de sus instancias.
}
