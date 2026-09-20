package es.uib.prgava.tema1.ejercicios;

/**
 * Ejercicio 1.2.5. Adónde va el texto de un informe.
 *
 * <p>Existe para que {@link InformeDiario} no dependa de {@code System.out}. Es una interfaz de un
 * solo método, es decir una interfaz funcional: se puede implementar con una lambda, y en 1.3 se
 * verá que {@code java.util.function.Consumer<String>} habría servido igual. Tener un nombre propio
 * tiene dos ventajas pequeñas: dice de qué va —una salida, no un consumidor de cadenas cualquiera— y
 * permite documentar aquí el contrato.
 */
public interface Salida {

    /** Escribe una línea. Quien la implementa decide adónde. */
    void escribir(String linea);
}
