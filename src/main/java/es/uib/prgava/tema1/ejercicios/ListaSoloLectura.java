package es.uib.prgava.tema1.ejercicios;

import java.util.Iterator;
import java.util.List;

/**
 * Ejercicio 1.2.3. <strong>Solución comentada.</strong> Lista de solo lectura construida con
 * <strong>composición</strong>.
 *
 * <p>Compárala con {@link ListaSoloLecturaHeredada}, que hereda de {@code ArrayList} y redefine
 * {@code add} para lanzar una excepción. Aquella viola el principio de sustitución de Liskov:
 * declara ser una {@code List} —«en cualquier sitio donde valga una List, valgo yo»— y luego
 * incumple el contrato de {@code List}, que dice que {@code add} añade el elemento. Un método
 * escrito contra {@code List}, que no sabe nada de nuestra clase, revienta en ejecución. El
 * compilador no puede avisar, porque en el sistema de tipos la promesa está bien hecha; lo que
 * falla es la promesa <em>semántica</em>.
 *
 * <p>La composición resuelve el problema de raíz: esta clase <strong>no es</strong> una
 * {@code List}, así que nadie puede pasarla donde se espera una y sentirse engañado. Expone solo
 * lo que de verdad sabe hacer. Es la regla de Bloch: «prefiere la composición a la herencia».
 *
 * <p>Implementa {@code Iterable<String>}, y eso sí es honesto: prometemos que se puede recorrer,
 * y se puede. Además permite usarla en un {@code for} mejorado sin heredar nada.
 */
public final class ListaSoloLectura implements Iterable<String> {

    private final List<String> elementos;

    public ListaSoloLectura(List<String> elementos) {
        // Copia defensiva. Sin ella, quien nos pasó la lista conservaría una referencia por la
        // que seguir modificándola, y nuestra «lista de solo lectura» cambiaría a su espalda:
        // exactamente lo que comprueba la prueba copiaDefensivamenteLaListaRecibida.
        // List.copyOf copia Y devuelve algo inmutable; dos garantías por el precio de una.
        this.elementos = List.copyOf(elementos);
    }

    public String obtener(int indice) {
        return elementos.get(indice);
    }

    public int tamano() {
        return elementos.size();
    }

    public boolean contiene(Object elemento) {
        return elementos.contains(elemento);
    }

    /**
     * Devolvemos el iterador de la lista interna. Es seguro <em>porque la lista interna es
     * inmutable</em>: su {@code Iterator.remove} ya lanza {@code UnsupportedOperationException}
     * por sí solo. Si el campo fuera un {@code ArrayList} mutable, este método estaría regalando
     * un camino para borrar elementos, y habría que envolver el iterador.
     */
    @Override
    public Iterator<String> iterator() {
        return elementos.iterator();
    }
}
