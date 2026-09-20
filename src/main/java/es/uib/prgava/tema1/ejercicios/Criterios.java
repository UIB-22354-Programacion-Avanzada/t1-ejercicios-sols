package es.uib.prgava.tema1.ejercicios;

import es.uib.prgava.tema1.monitor.PoliticaDeAlerta;

/**
 * Ejercicio 1.3.4. <strong>Solución comentada.</strong> Las mismas políticas de 1.2.2, sin escribir
 * ni una clase.
 *
 * <p>{@code PoliticaDeAlerta} tiene un único método abstracto, así que es una <strong>interfaz
 * funcional</strong> y una lambda vale como implementación. Compara el tamaño de este fichero con el
 * de {@link es.uib.prgava.tema1.monitor.FallosEnVentana}: la lambda no aporta nada nuevo, ahorra la
 * ceremonia de declarar una clase cuando la lógica cabe en una línea.
 *
 * <p>Y la respuesta al «Para pensar» —cuándo preferir una clase con nombre—: cuando la estrategia
 * tiene parámetros que conviene ver ({@code new FallosEnVentana(2, 5)} dice más que
 * {@code alMenos(2)}), cuando hace falta un {@code toString} descriptivo —y aquí hace falta, porque
 * {@code Monitor} lo usa para el mensaje de la alerta, y el de una lambda es ilegible—, cuando la
 * lógica pasa de dos o tres líneas, y cuando se quiere probar la política por separado con su propio
 * nombre en el informe de pruebas.
 */
public final class Criterios {

    private Criterios() {
    }

    /** Útil de verdad: en una prueba, para forzar que el monitor avise. */
    public static final PoliticaDeAlerta SIEMPRE = historial -> true;

    /** Y su pareja: para comprobar que sin política no sale ninguna alerta. */
    public static final PoliticaDeAlerta NUNCA = historial -> false;

    /**
     * El historial llega del más antiguo al más reciente, así que el último es {@code getLast()}.
     * La comprobación de vacío va primero: sobre una lista vacía {@code getLast} lanza excepción.
     */
    public static final PoliticaDeAlerta ULTIMO_FALLO =
            historial -> !historial.isEmpty() && historial.getLast().esFallo();

    /**
     * Un método que <strong>devuelve</strong> una política. La lambda que devuelve captura el
     * parámetro {@code fallos}, y por eso cada llamada produce una política distinta: eso es un cierre
     * (<em>closure</em>), y es lo que permite parametrizar una lambda sin escribir un constructor.
     *
     * <p>{@code fallos} tiene que ser efectivamente final para poder capturarlo, y lo es porque no se
     * reasigna. Si el método lo modificara, el compilador rechazaría la captura.
     */
    public static PoliticaDeAlerta alMenos(int fallos) {
        return historial -> {
            int cuantos = 0;
            for (var resultado : historial) {
                if (resultado.esFallo()) {
                    cuantos++;
                }
            }
            return cuantos >= fallos;
        };
    }
}
