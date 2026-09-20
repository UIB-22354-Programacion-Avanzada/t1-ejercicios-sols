package es.uib.prgava.tema1.ejercicios;

import java.util.Objects;

/**
 * Ejercicio 1.1.3. <strong>Solución comentada.</strong> La clase venía con dos defectos y aquí
 * están los dos corregidos.
 *
 * <p><strong>Defecto 1: faltaba {@code hashCode}.</strong> El {@code equals} original ya comparaba
 * los dos campos, así que la igualdad respondía bien; lo que fallaba era todo lo demás. Un
 * {@code HashSet} o un {@code HashMap} no comparan con {@code equals} el elemento buscado contra
 * todos los que guardan —eso sería tiempo lineal y perderían su razón de ser—: calculan primero el
 * código hash para saber <strong>en qué cubo</strong> mirar, y solo dentro de ese cubo comparan con
 * {@code equals}. Con el {@code hashCode} heredado de {@code Object}, que deriva de la identidad
 * del objeto, dos etiquetas iguales creadas por separado caían en cubos distintos y no llegaban ni
 * a compararse.
 *
 * <p><strong>Defecto 2: el {@code equals} no era seguro.</strong> Estaba escrito como
 * {@code Etiqueta esa = (Etiqueta) otro;}, sin comprobar nada. Con {@code null} lanzaba
 * {@code NullPointerException} y con un objeto de otra clase, {@code ClassCastException}; y el
 * contrato de {@code Object.equals} dice que en los dos casos hay que devolver {@code false}, no
 * reventar. El {@code instanceof} con patrón resuelve las dos cosas de una vez: comprueba el tipo,
 * descarta {@code null} —{@code null} no es {@code instanceof} de nada— y declara la variable ya
 * convertida.
 *
 * <p><strong>Y la pregunta del final del enunciado:</strong> si {@code Etiqueta} fuera un
 * {@code record Etiqueta(String texto, String color)}, ninguno de los tres pasos habría hecho
 * falta. El compilador genera {@code equals}, {@code hashCode} y {@code toString} coordinados por
 * construcción, y el defecto de este ejercicio no es que alguien escribiera mal el
 * {@code hashCode}: es que no lo escribió. Con un {@code record}, esa omisión es imposible.
 */
public class Etiqueta {

    private final String texto;
    private final String color;

    public Etiqueta(String texto, String color) {
        this.texto = texto;
        this.color = color;
    }

    public String texto() {
        return texto;
    }

    public String color() {
        return color;
    }

    @Override
    public boolean equals(Object otro) {
        // instanceof con patrón: comprueba el tipo, excluye null y declara `esa` ya convertida.
        return otro instanceof Etiqueta esa
                && texto.equals(esa.texto)
                && color.equals(esa.color);
    }

    @Override
    public int hashCode() {
        // LOS MISMOS dos campos que usa equals, ni uno más ni uno menos. Si aquí apareciera un
        // tercer campo, o faltara uno, el contrato se rompería y volveríamos al defecto original.
        return Objects.hash(texto, color);
    }

    @Override
    public String toString() {
        return "Etiqueta[" + texto + ", " + color + "]";
    }

    // ----------------------------------------------------------------------------------
    // CONSECUENCIA PRÁCTICA del fallo original, que el enunciado pedía explicar en tres líneas:
    // la etiqueta funcionaba mientras se comparara a mano y dejaba de funcionar en cuanto entraba
    // en un HashSet o en un HashMap: contains devolvía false para una etiqueta igual, el conjunto
    // admitía duplicados y una clave no recuperaba su valor. El error no aparece donde se escribe
    // la clase, sino mucho después y en otro fichero, que es lo que lo hace tan caro de encontrar.
    // ----------------------------------------------------------------------------------
}
