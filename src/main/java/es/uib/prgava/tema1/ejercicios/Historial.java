package es.uib.prgava.tema1.ejercicios;

import java.util.ArrayList;
import java.util.List;

/**
 * Ejercicio 1.3.1. <strong>Solución comentada.</strong> Una clase genérica: el parámetro de tipo se
 * declara una vez, y a partir de ahí {@code T} es un tipo más.
 *
 * <p>Fíjate en que dentro de la clase no hay ni una conversión de tipo ni un {@code Object}. Eso es
 * todo lo que hacen los genéricos: mover al compilador una comprobación que si no habría que hacer en
 * ejecución. La versión con {@code Object} guardaría lo mismo, pero quien sacara elementos tendría
 * que convertirlos a mano, y un día convertiría al tipo equivocado y se enteraría al ejecutar.
 *
 * <p>Y la respuesta al «Para pensar»: se gana que el error de tipo lo dé el compilador y no la
 * ejecución, y que el código de quien usa la clase no tenga conversiones. Se pierde poco:
 * principalmente, que no se pueden usar tipos primitivos como {@code T} —hay que escribir
 * {@code Historial<Integer>}— y que, por el borrado de tipos, dentro de la clase no se puede
 * preguntar qué es {@code T} ni crear un {@code new T[...]}.
 */
public final class Historial<T> {

    private final int capacidad;
    private final List<T> elementos = new ArrayList<>();

    /** @throws IllegalArgumentException si la capacidad no es positiva */
    public Historial(int capacidad) {
        if (capacidad < 1) {
            throw new IllegalArgumentException("La capacidad debe ser positiva: " + capacidad);
        }
        this.capacidad = capacidad;
    }

    /** Al llegar a la capacidad se descarta el más antiguo, que es el primero de la lista. */
    public void anadir(T elemento) {
        if (elementos.size() == capacidad) {
            elementos.remove(0);
        }
        elementos.add(elemento);
    }

    /**
     * Los {@code cuantos} más recientes, del más antiguo al más nuevo.
     *
     * <p>{@code Math.max(0, ...)} evita tener que tratar aparte el caso de pedir más de los que hay:
     * si se piden diez y hay dos, se devuelven los dos, que es lo razonable y lo que espera quien
     * llama. Y {@code List.copyOf} devuelve una lista inmutable <em>y desconectada</em> de la interna:
     * sin la copia, quien la reciba vería cambiar su contenido con cada {@code anadir}.
     */
    public List<T> ultimos(int cuantos) {
        int desde = Math.max(0, elementos.size() - cuantos);
        return List.copyOf(elementos.subList(desde, elementos.size()));
    }

    public int tamano() {
        return elementos.size();
    }

    // Sobre el coste: remove(0) en un ArrayList desplaza todos los elementos, así que cada inserción
    // por encima de la capacidad cuesta lineal. Con capacidades pequeñas —las de un historial de
    // monitorización— no importa. La estructura adecuada si importara es ArrayDeque, y la del tema
    // que ya hace esto es VentanaDeslizante, en el paquete genericos.
}
