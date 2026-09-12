package es.uib.prgava.tema1.ejercicios;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * Ejercicio 1.3.3. <strong>Solución comentada.</strong> PECS: <em>producer extends, consumer
 * super</em>.
 *
 * <p>La regla de Bloch, en una línea: si un parámetro te <strong>da</strong> valores de tipo T
 * (lo lees), decláralo {@code ? extends T}; si los <strong>recibe</strong> (escribes en él),
 * decláralo {@code ? super T}; si hace las dos cosas, no pongas comodín.
 */
public final class CopiasEjercicio {

    private CopiasEjercicio() { }

    /**
     * Copia al destino los elementos del origen que cumplen el filtro.
     *
     * <p>Los tres comodines de la firma, uno a uno:
     * <ul>
     *   <li>{@code origen} es un <strong>productor</strong>: solo se lee de él, luego
     *       {@code ? extends T}. Así acepta una {@code List<Integer>} cuando T es {@code Number}.</li>
     *   <li>{@code destino} es un <strong>consumidor</strong>: solo se escribe en él, luego
     *       {@code ? super T}. Así acepta una {@code List<Object>} cuando T es {@code Resultado}
     *       — que es lo que comprueba la prueba {@code copiarSiAdmiteUnDestinoDeUnSupertipo}.</li>
     *   <li>{@code filtro} también consume: recibe elementos y devuelve un booleano. Por eso
     *       {@code Predicate<? super T>}, que permite pasar un predicado escrito para un
     *       supertipo. Un {@code Predicate<Object>} sabe juzgar cualquier cosa, y sería absurdo
     *       rechazarlo.</li>
     * </ul>
     *
     * @return cuántos elementos se copiaron
     */
    public static <T> int copiarSi(Collection<? extends T> origen,
                                   Collection<? super T> destino,
                                   Predicate<? super T> filtro) {
        int copiados = 0;
        for (T elemento : origen) {
            if (filtro.test(elemento)) {
                destino.add(elemento);
                copiados++;
            }
        }
        return copiados;
    }

    /**
     * Versión corregida de {@code fusionar} (ejercicio 1.3.3).
     *
     * <p>La firma original era {@code fusionar(List<T> a, List<T> b, List<T> destino)}, y obligaba
     * a que las tres listas fueran <strong>exactamente</strong> del mismo tipo. Con PECS, las dos
     * fuentes producen y el destino consume.
     *
     * <p>Tres llamadas que antes no compilaban y ahora sí:
     * <ol>
     *   <li>{@code fusionar(List<Integer>, List<Double>, List<Number>)} — dos fuentes de tipos
     *       distintos y un destino más general. Es la de la prueba que había que descomentar.</li>
     *   <li>{@code fusionar(List<Integer>, List<Integer>, List<Object>)} — mismo tipo de origen,
     *       destino mucho más general.</li>
     *   <li>{@code fusionar(List<ServicioHttp>, List<ServicioDns>, List<Servicio>)} — el caso
     *       realista en este proyecto: dos listas de tipos concretos de servicio en una lista de
     *       la interfaz común.</li>
     * </ol>
     *
     * <p>Y una que sigue sin compilar, con razón:
     * {@code fusionar(List<Number>, List<Number>, List<Integer>)} — meter números cualesquiera en
     * una lista de enteros corrompería la lista, y el compilador lo impide.
     */
    public static <T> void fusionar(List<? extends T> a, List<? extends T> b, List<? super T> destino) {
        destino.addAll(a);
        destino.addAll(b);
    }
}
