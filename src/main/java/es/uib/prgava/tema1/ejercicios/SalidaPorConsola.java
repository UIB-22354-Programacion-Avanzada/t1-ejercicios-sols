package es.uib.prgava.tema1.ejercicios;

/**
 * Ejercicio 1.2.5. La implementación que usa el programa de verdad. Es la única clase de todo el
 * ejercicio que menciona {@code System.out}, y por eso es la única que una prueba no puede usar.
 */
public final class SalidaPorConsola implements Salida {

    @Override
    public void escribir(String linea) {
        System.out.println(linea);
    }
}
