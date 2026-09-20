package es.uib.prgava.tema1.ejercicios;

/**
 * Ejercicio 1.3.2. <strong>Solución comentada.</strong> Dos métodos genéricos, uno con cota y otro
 * sin ella, y la diferencia es exactamente lo que se pedía entender.
 */
public final class UtilidadesEjercicio {

    private UtilidadesEjercicio() {
    }

    /**
     * La cota {@code T extends Comparable<T>} es lo que permite llamar a {@code compareTo} dentro del
     * método. Sin ella, {@code T} sería un tipo del que no se sabe nada, y de un tipo del que no se
     * sabe nada solo se pueden llamar los métodos de {@code Object}: el código no compilaría.
     *
     * <p>Se devuelve {@code a} cuando empatan ({@code >= 0}). Es una decisión, no un detalle: con
     * elementos iguales según {@code compareTo} pero distintos según {@code equals}, cuál se devuelva
     * se nota. Documentarla es más útil que elegir bien.
     */
    public static <T extends Comparable<T>> T mayor(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    /**
     * Sin cota, y aun así llama a {@code equals}.
     *
     * <p>La respuesta al «Para pensar»: porque {@code equals} está declarado en {@code Object}, y
     * todo tipo de referencia hereda de {@code Object}. La cota superior implícita de un
     * {@code <T>} sin cota es {@code Object}, así que {@code equals}, {@code hashCode} y
     * {@code toString} están disponibles siempre. {@code compareTo} no, porque no es de
     * {@code Object}: es de una interfaz que hay que exigir.
     *
     * <p>Se llama {@code buscado.equals(elemento)} y no al revés para que un {@code null} en el array
     * no rompa el recorrido; si el buscado fuera {@code null} habría que decidir otra cosa, y este
     * método no lo admite.
     */
    public static <T> int contarIguales(T[] elementos, T buscado) {
        int cuantos = 0;
        for (var elemento : elementos) {
            if (buscado.equals(elemento)) {
                cuantos++;
            }
        }
        return cuantos;
    }
}
