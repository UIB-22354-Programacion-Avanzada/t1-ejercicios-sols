package es.uib.prgava.tema1.ejercicios;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Ejercicio 1.1.6. <strong>Solución comentada.</strong> Lector del fichero de umbrales de
 * latencia por servicio.
 */
public final class LectorUmbrales {

    private LectorUmbrales() { }

    /**
     * Lee un fichero de líneas {@code nombreServicio=milisegundos}.
     *
     * <p>Ignora líneas vacías y comentarios ({@code #}). Devuelve un mapa inmutable.
     *
     * @throws UmbralInvalidoException si una línea es inválida, el valor no es positivo
     *                                 o una clave está repetida
     */
    public static Map<String, Duration> leer(Path fichero) {
        // LinkedHashMap, no HashMap: conserva el orden del fichero, que es el que el usuario
        // espera ver si algún día imprimimos el mapa. No cuesta nada y evita sorpresas.
        var umbrales = new LinkedHashMap<String, Duration>();

        // try-with-resources: Files.lines abre el fichero de forma perezosa y mantiene el
        // descriptor abierto mientras se consume el Stream. Si no se cierra, en un programa
        // largo se agotan los descriptores del sistema. El try-with-resources garantiza el
        // cierre incluso si el cuerpo lanza una excepción — incluida la nuestra.
        try (Stream<String> lineas = Files.lines(fichero)) {
            int[] numero = {0};          // contador mutable accesible desde la lambda:
                                         // una variable local normal no valdría, porque las
                                         // lambdas solo capturan variables efectivamente finales
            lineas.forEach(linea -> interpretar(umbrales, ++numero[0], linea));
        } catch (IOException e) {
            // Traducimos la excepción comprobada de E/S a una no comprobada. La firma del
            // método queda limpia, y quien llame no está obligado a capturar algo que no puede
            // arreglar. UncheckedIOException conserva la IOException original como causa.
            throw new UncheckedIOException("No se pudo leer " + fichero, e);
        }

        // Copia inmutable: el mapa que devolvemos no puede modificarse desde fuera, y como es
        // una copia, tampoco le afecta nada de lo que hagamos aquí después.
        return Map.copyOf(umbrales);
    }

    /** Interpreta una línea y la añade al mapa, o lanza la excepción con el número de línea. */
    private static void interpretar(Map<String, Duration> umbrales, int numero, String linea) {
        String limpia = linea.strip();
        if (limpia.isEmpty() || limpia.startsWith("#")) {
            return;                                  // línea vacía o comentario: se ignora
        }

        // limitamos a 2 partes: el nombre de un servicio puede contener '=' en teoría, y el
        // valor nunca lo contiene, así que partimos por el PRIMER '='.
        int igual = limpia.indexOf('=');
        if (igual < 1 || igual == limpia.length() - 1) {
            throw new UmbralInvalidoException(numero,
                    "se esperaba servicio=milisegundos, y llegó: " + limpia);
        }
        String servicio = limpia.substring(0, igual).strip();
        String valor = limpia.substring(igual + 1).strip();

        long milisegundos;
        try {
            milisegundos = Long.parseLong(valor);
        } catch (NumberFormatException e) {
            // Aquí está el encadenado que pide el enunciado: nuestra excepción explica QUÉ línea
            // del fichero falla, y la causa explica POR QUÉ. Las dos informaciones son
            // necesarias y ninguna sustituye a la otra.
            throw new UmbralInvalidoException(numero, "valor no numérico: " + valor, e);
        }
        if (milisegundos <= 0) {
            // Aquí no hay causa que encadenar: el valor es un número perfectamente válido,
            // simplemente no cumple nuestra regla de negocio.
            throw new UmbralInvalidoException(numero, "el umbral debe ser positivo: " + valor);
        }
        if (umbrales.putIfAbsent(servicio, Duration.ofMillis(milisegundos)) != null) {
            // putIfAbsent devuelve el valor anterior si la clave ya estaba: detectamos la
            // repetición y hacemos la inserción en una sola operación.
            throw new UmbralInvalidoException(numero, "clave repetida: " + servicio);
        }
    }

    public static void main(String[] args) {
        Path fichero = Path.of(args.length > 0 ? args[0] : "umbrales-con-error.txt");

        // Primero el fichero correcto, para ver la lectura normal.
        System.out.println("umbrales.txt -> " + leer(Path.of("umbrales.txt")));

        // Y ahora, deliberadamente, el que tiene el error: dejamos escapar la excepción.
        System.out.println();
        System.out.println("Leyendo " + fichero + " (debe fallar):");
        System.out.println(leer(fichero));
    }

    // Respuesta al TODO 1.1.6. Stack trace REAL producido por main con umbrales-con-error.txt
    // (copiado de la ejecución, no escrito de memoria):
    //
    // Exception in thread "main" es.uib.prgava.tema1.ejercicios.UmbralInvalidoException:
    //         Línea 3: valor no numérico: rapido
    //     at ...LectorUmbrales.interpretar(LectorUmbrales.java:78)     <- código propio
    //     at ...LectorUmbrales.lambda$leer$0(LectorUmbrales.java:41)   <- código propio
    //     at java.base/java.nio.file.FileChannelLinesSpliterator.forEachRemaining(...)
    //     at java.base/java.util.stream.ReferencePipeline$Head.forEach(...)
    //     at ...LectorUmbrales.leer(LectorUmbrales.java:41)            <- código propio
    //     at ...LectorUmbrales.main(LectorUmbrales.java:101)           <- código propio
    // Caused by: java.lang.NumberFormatException: For input string: "rapido"
    //     at java.base/java.lang.NumberFormatException.forInputString(...)
    //     at java.base/java.lang.Long.parseLong(Long.java:709)
    //     at java.base/java.lang.Long.parseLong(Long.java:832)
    //     at ...LectorUmbrales.interpretar(LectorUmbrales.java:73)     <- código propio
    //     ... 5 more
    //
    // Cómo leerlo, que es de lo que va el ejercicio:
    //
    //   - La PRIMERA línea es nuestra excepción, con la información de dominio: qué línea del
    //     fichero y qué valor. Es lo que le sirve a quien escribió el fichero de configuración.
    //
    //   - El bloque 'Caused by:' es la excepción técnica original, la que encadenamos al
    //     construir UmbralInvalidoException. Dice exactamente qué operación falló y con qué
    //     entrada. Sin el encadenado, esta mitad no existiría y habría que adivinarla.
    //
    //   - Las líneas 'at ...LectorUmbrales...' son NUESTRO código; las de 'java.base' son la
    //     biblioteca. Al depurar se empieza siempre por la primera línea propia: la 78 en la
    //     traza de arriba (donde lanzamos) y la 73 en la causa (donde falló parseLong).
    //
    //   - Las líneas de FileChannelLinesSpliterator y ReferencePipeline son el interior de
    //     Files.lines y del forEach: aparecen porque el recorrido lo hace el Stream, no un
    //     bucle nuestro. Un stack trace de código con Streams siempre tiene estos tramos de
    //     biblioteca intercalados, y conviene acostumbrarse a saltárselos.
    //
    //   - '... 5 more' no significa que falte información: son marcos idénticos a los de la
    //     traza de arriba, que la JVM abrevia porque las dos comparten la parte final.
}
