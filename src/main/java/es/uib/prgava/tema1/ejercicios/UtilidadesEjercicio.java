package es.uib.prgava.tema1.ejercicios;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Ejercicio 1.3.2. <strong>Solución comentada.</strong> Métodos genéricos con cotas y comodines.
 *
 * <p>Las firmas son aquí más interesantes que los cuerpos: cada comodín está puesto por una razón
 * concreta, y las respuestas del final explican cuál.
 */
public final class UtilidadesEjercicio {

    private UtilidadesEjercicio() { }

    public static <T extends Comparable<? super T>> Optional<T> minimo(Collection<? extends T> elementos) {
        // Collections.min lanza NoSuchElementException si la colección está vacía, así que ese
        // caso lo tratamos aparte y lo convertimos en un Optional vacío, que es lo que pide el
        // contrato. Usar Collections.min y no un Stream evita tener que lidiar con la captura
        // del comodín en el tipo de retorno: su firma, min(Collection<? extends T>,
        // Comparator<? super T>), devuelve T directamente.
        if (elementos.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Collections.min(elementos, Comparator.<T>naturalOrder()));
    }

    public static <T> long contarSi(Collection<? extends T> elementos, Predicate<? super T> criterio) {
        return elementos.stream().filter(criterio).count();
    }

    /** Lista nueva e inmutable, de mayor a menor. */
    public static <T extends Comparable<? super T>> List<T> ordenadoDescendente(Collection<? extends T> elementos) {
        // Copiamos ANTES de ordenar: sort ordena en el sitio, y hacerlo sobre la colección
        // recibida modificaría un objeto ajeno. La prueba lo comprueba verificando que la lista
        // original conserva su orden.
        var copia = new ArrayList<T>(elementos);
        copia.sort(Collections.reverseOrder());
        return List.copyOf(copia);
    }

    // Respuestas al TODO 1.3.2:
    //
    // (a) Por qué la cota es Comparable<? super T> y no Comparable<T>.
    //     Porque la capacidad de compararse puede estar heredada. Imagina 'class Enrutador
    //     extends Dispositivo' donde el Comparable lo implementa Dispositivo: entonces
    //     Enrutador es Comparable<Dispositivo>, NO Comparable<Enrutador>. Con la cota estricta,
    //     minimo(listaDeEnrutadores) no compilaría. Con '? super T' aceptamos también a los
    //     tipos que saben compararse a un nivel más general, que es lo correcto: para ordenar
    //     enrutadores basta con saber comparar dispositivos. Es PECS aplicado a la cota: el
    //     comparador CONSUME elementos de tipo T.
    //
    // (b) Por qué el parámetro es Collection<? extends T> y no Collection<T>.
    //     Porque los genéricos en Java son INVARIANTES: List<Integer> no es subtipo de
    //     List<Number>, aunque Integer sí lo sea de Number. Con Collection<T> y T = Number, una
    //     List<Integer> sería rechazada. Con '? extends T' aceptamos la colección de cualquier
    //     subtipo, que es seguro porque solo LEEMOS de ella: todo lo que salga será un T.
    //
    // (c) Qué error aparecería al hacer elementos.add(...) dentro de minimo.
    //     No compila: "no suitable method found for add(T)". Y el compilador tiene razón. El
    //     tipo del parámetro es '? extends T': un comodín, un tipo concreto desconocido que es
    //     algún subtipo de T. Si la colección real fuera una List<Integer> y T fuera Number,
    //     añadir un Number cualquiera —pongamos un Double— corrompería la lista de enteros. Al
    //     no saber cuál es el subtipo real, el compilador solo admite añadir null. Eso es
    //     exactamente la P de PECS: un 'producer extends' produce elementos, no los consume.
}
