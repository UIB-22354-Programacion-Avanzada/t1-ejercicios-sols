package es.uib.prgava.tema1.monitor;

/**
 * Ejercicio 1.2.7. Un tipo de servicio más, y {@code Monitor} no cambia.
 *
 * <p>Es un {@code record} por la misma razón que los otros tres: un servicio es un valor, y su
 * {@code equals} y su {@code hashCode} generados son lo que permite usarlo como clave en el
 * repositorio y en el {@code Set} de {@link NotificadorSinRepetir}.
 */
public record ServicioPing(String host) implements Servicio {

    @Override
    public String nombre() {
        // El mismo formato que acepta Servicio.desde, de modo que nombre() y desde() sean inversos.
        return "ping:" + host;
    }
}
