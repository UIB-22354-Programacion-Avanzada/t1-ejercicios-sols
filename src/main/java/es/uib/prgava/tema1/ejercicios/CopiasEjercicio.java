package es.uib.prgava.tema1.ejercicios;

import java.util.List;

import es.uib.prgava.tema1.monitor.Servicio;

/**
 * Ejercicio 1.3.3. <strong>Solución comentada.</strong> PECS, y por qué la firma de partida no
 * servía.
 *
 * <p>Con {@code List<Servicio>} en los dos parámetros, el método solo acepta listas cuyo tipo sea
 * exactamente {@code Servicio}. Eso es la <strong>invarianza</strong> de los genéricos:
 * {@code List<ServicioDns>} no es un subtipo de {@code List<Servicio>}, aunque {@code ServicioDns}
 * lo sea de {@code Servicio}. Y con razón: si lo fuera, se podría meter un {@code ServicioHttp} en
 * una {@code List<ServicioDns>} a través de la referencia ancha, y el programa se rompería en otro
 * sitio. Los comodines son la forma de recuperar la flexibilidad <em>sin</em> abrir ese agujero.
 */
public final class CopiasEjercicio {

    private CopiasEjercicio() {
    }

    /**
     * Copia al final de {@code destino} todos los elementos de {@code origen}.
     *
     * <p><strong>PECS: Producer Extends, Consumer Super.</strong>
     *
     * <p>{@code origen} <em>produce</em> los elementos que el método lee, así que lleva
     * {@code ? extends Servicio}: acepta una lista de cualquier subtipo, porque todo lo que salga de
     * ella es seguro tratarlo como {@code Servicio}.
     *
     * <p>{@code destino} <em>consume</em> los elementos que el método escribe, así que lleva
     * {@code ? super Servicio}: acepta una lista de cualquier supertipo —{@code List<Object>}
     * incluida—, porque en un sitio donde caben cosas más generales cabe un {@code Servicio}.
     *
     * <p>Y la respuesta al «Para pensar»: no, dentro del método no se puede añadir nada a
     * {@code origen}. Con {@code ? extends Servicio} el compilador sabe que el tipo de los elementos
     * es <em>algún</em> subtipo de {@code Servicio}, pero no cuál; podría ser
     * {@code List<ServicioDns>}, y entonces añadir un {@code ServicioHttp} sería un error. Al no
     * poder saberlo, prohíbe añadir cualquier cosa. Esa es la simetría de PECS: de una lista
     * {@code extends} se lee y no se escribe; en una lista {@code super} se escribe, y lo que se lee
     * solo se puede tratar como {@code Object}.
     */
    public static void copiarTodo(List<? extends Servicio> origen, List<? super Servicio> destino) {
        for (Servicio servicio : origen) {
            destino.add(servicio);
        }
    }
}
