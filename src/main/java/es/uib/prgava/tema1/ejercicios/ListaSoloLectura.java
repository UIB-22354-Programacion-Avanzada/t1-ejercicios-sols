package es.uib.prgava.tema1.ejercicios;

import java.util.List;

/**
 * Ejercicio 1.2.3. <strong>Solución comentada.</strong> Lista de solo lectura por composición.
 *
 * <p>Compárala con {@link ListaSoloLecturaHeredada}, que hereda de {@code ArrayList} y redefine
 * {@code add} para lanzar una excepción. Aquella viola el principio de sustitución: declara ser una
 * {@code List} —«en cualquier sitio donde valga una List, valgo yo»— y luego incumple el contrato
 * de {@code List}, que dice que {@code add} añade el elemento. Un método escrito contra
 * {@code List}, que no sabe nada de nuestra clase, revienta en ejecución. El compilador no puede
 * avisar, porque en el sistema de tipos la promesa está bien hecha: lo que falla es la promesa
 * <em>semántica</em>.
 *
 * <p>La composición resuelve el problema de raíz: esta clase <strong>no es</strong> una
 * {@code List}, así que nadie puede pasarla donde se espera una y sentirse engañado. Expone solo lo
 * que de verdad sabe hacer, y no hay un solo método que mienta. Es la regla de Bloch: «prefiere la
 * composición a la herencia».
 *
 * <p>Y la respuesta al «Para pensar» —si {@code List.of(...)} es entonces la misma violación en la
 * propia biblioteca de Java—: sí y no. El contrato de {@code List} contempla explícitamente las
 * listas inmutables y autoriza a {@code add} a lanzar {@code UnsupportedOperationException}; está
 * escrito en su documentación, forma parte de lo prometido, y quien recibe una {@code List} sabe
 * que puede tocarle una. La diferencia con {@code ListaSoloLecturaHeredada} es de honestidad
 * declarada: la biblioteca avisa por adelantado, y nuestra subclase no. Aun así es un diseño que
 * hoy se considera un error histórico —Java lo arrastra desde 1.2—, y la prueba es que el propio
 * Bloch lo cita como ejemplo de lo que no hay que hacer.
 */
public final class ListaSoloLectura {

    private final List<String> elementos;

    public ListaSoloLectura(List<String> elementos) {
        // Copia defensiva. Sin ella, quien nos pasó la lista conservaría una referencia por la que
        // seguir modificándola, y nuestra «lista de solo lectura» cambiaría a su espalda.
        // List.copyOf copia Y devuelve algo inmutable: dos garantías por el precio de una.
        this.elementos = List.copyOf(elementos);
    }

    public int tamano() {
        return elementos.size();
    }

    public String get(int indice) {
        return elementos.get(indice);
    }

    public boolean contiene(String elemento) {
        return elementos.contains(elemento);
    }

    /**
     * Devuelve la lista interna, y es seguro <em>porque es inmutable</em>: quien la reciba no puede
     * modificarla, y su iterador tampoco. Si el campo fuera un {@code ArrayList} mutable, este
     * método estaría regalando un camino para borrar elementos.
     */
    public List<String> comoLista() {
        return elementos;
    }
}
