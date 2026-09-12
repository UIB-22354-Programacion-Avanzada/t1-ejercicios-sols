package es.uib.prgava.tema1.ejercicios;

/**
 * Ejercicio 1.1.6. <strong>Solución comentada.</strong> Error al interpretar el fichero de
 * umbrales.
 *
 * <p>Es una excepción <em>no comprobada</em> ({@code extends RuntimeException}) porque señala un
 * fichero de configuración mal escrito: no es algo de lo que el programa pueda recuperarse
 * razonablemente en el punto de la llamada, sino un error que hay que corregir en el fichero.
 * Obligar a cada llamador a capturarla solo produciría {@code catch} vacíos.
 *
 * <p>Guarda el número de línea como <strong>dato</strong>, no solo dentro del texto del mensaje:
 * quien la capture puede usarlo programáticamente (por ejemplo, para señalar la línea en un
 * editor) sin tener que analizar el mensaje con expresiones regulares.
 */
public class UmbralInvalidoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int linea;

    public UmbralInvalidoException(int linea, String mensaje) {
        // El prefijo se construye aquí, en un único sitio, y no en cada punto donde se lanza:
        // así todos los mensajes tienen exactamente el mismo formato.
        super("Línea " + linea + ": " + mensaje);
        this.linea = linea;
    }

    /**
     * Variante que <strong>encadena la causa</strong>. Es la pieza importante del ejercicio: al
     * pasar {@code causa} a {@code super}, el stack trace mostrará primero este error (con la
     * línea del fichero) y después un {@code Caused by:} con la excepción original — por ejemplo
     * el {@code NumberFormatException} que explica qué falló exactamente. Sin encadenar, esa
     * información se pierde y depurar se vuelve adivinar.
     */
    public UmbralInvalidoException(int linea, String mensaje, Throwable causa) {
        super("Línea " + linea + ": " + mensaje, causa);
        this.linea = linea;
    }

    public int linea() {
        return linea;
    }
}
