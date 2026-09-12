package es.uib.prgava.tema1.ejercicios;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

/**
 * Ejercicio 1.3.1. <strong>Solución comentada.</strong> Colección genérica que conserva como
 * máximo los últimos {@code capacidad} elementos añadidos.
 *
 * <p>El parámetro de tipo {@code <T>} es lo que hace que esta clase sirva para
 * {@code Historial<String>}, {@code Historial<Resultado>} o {@code Historial<Alerta>} con
 * comprobación de tipos completa en compilación. La alternativa antigua —guardar
 * {@code Object} y hacer <em>cast</em> al sacar— compila igual pero traslada los errores a la
 * ejecución, en forma de {@code ClassCastException}.
 *
 * <p>Por qué un {@code ArrayDeque} y no un {@code ArrayList}: la operación característica de esta
 * clase es «quita el más antiguo y añade al final». En un {@code ArrayList}, quitar el primer
 * elemento obliga a desplazar todos los demás: coste lineal. Un {@code ArrayDeque} añade y quita
 * por los dos extremos en tiempo constante amortizado. La estructura de datos se elige por las
 * operaciones que se van a hacer, no por costumbre — que es el tema del Tema 2.
 */
public final class Historial<T> {

    private final int capacidad;
    private final Deque<T> elementos;

    public Historial(int capacidad) {
        if (capacidad < 1) {
            throw new IllegalArgumentException("La capacidad debe ser al menos 1, y llegó " + capacidad);
        }
        this.capacidad = capacidad;
        this.elementos = new ArrayDeque<>(capacidad);
    }

    /** Si el historial está lleno, descarta el más antiguo antes de añadir. */
    public void anadir(T elemento) {
        if (elementos.size() == capacidad) {
            elementos.removeFirst();       // el más antiguo está al principio
        }
        elementos.addLast(elemento);       // el más reciente, al final
    }

    /**
     * Como máximo los {@code n} elementos más recientes, del más reciente al más antiguo, en una
     * lista inmutable.
     *
     * <p>Guardamos del más antiguo al más reciente porque es el orden natural para insertar, pero
     * al consultar interesa el orden contrario: lo reciente primero. {@code descendingIterator}
     * recorre el {@code Deque} del final al principio sin copiar nada previamente.
     */
    public List<T> ultimos(int n) {
        var resultado = new ArrayList<T>(Math.min(n, elementos.size()));
        Iterator<T> desdeElFinal = elementos.descendingIterator();
        // La condición combinada resuelve de un golpe el caso «me piden más de los que hay»:
        // el iterador se agota antes de llegar a n. Sin ella haría falta un Math.min y un
        // índice, con más sitios donde equivocarse.
        while (desdeElFinal.hasNext() && resultado.size() < n) {
            resultado.add(desdeElFinal.next());
        }
        return List.copyOf(resultado);
    }

    public int tamano() {
        return elementos.size();
    }

    public boolean vacio() {
        return elementos.isEmpty();
    }

    public boolean lleno() {
        return elementos.size() == capacidad;
    }

    // Respuesta al TODO 1.3.1 (por qué ultimos devuelve List.copyOf):
    //
    // Por dos motivos distintos que conviene no confundir.
    //
    // (1) Devolver la colección interna —o una vista de ella— rompería la encapsulación: quien
    //     recibiera la lista podría añadir o quitar elementos y estaría modificando el historial
    //     por la puerta de atrás, saltándose anadir() y su regla de capacidad. El invariante
    //     "nunca más de 'capacidad' elementos" dejaría de estar garantizado.
    //
    // (2) Aunque no la modificara, una vista cambiaría bajo sus pies cada vez que alguien
    //     llamara a anadir(): el llamante creería tener una foto y tendría una ventana. Peor
    //     aún, recorrerla mientras el historial cambia provoca ConcurrentModificationException.
    //     Y en el Tema 3, con varios hilos, esto pasa de ser un riesgo a ser una certeza.
    //
    // El precio es una copia por consulta. Para un historial acotado a unas pocas decenas de
    // elementos es irrelevante, y a cambio el contrato del método es trivial de razonar.
}
