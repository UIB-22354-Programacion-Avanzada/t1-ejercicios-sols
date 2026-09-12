package es.uib.prgava.tema1.ejercicios;

import java.util.Objects;

/**
 * Ejercicio 1.1.3. <strong>Solución comentada.</strong>
 *
 * <p>El enunciado entregaba esta clase con {@code equals} redefinido y {@code hashCode} sin
 * redefinir. La corrección es añadir {@code hashCode}, y lo interesante es entender por qué
 * hacía falta.
 *
 * <p>El contrato de {@link Object} dice: si {@code a.equals(b)}, entonces
 * {@code a.hashCode() == b.hashCode()}. El {@code hashCode} heredado de {@code Object} deriva de
 * la <em>identidad</em> del objeto, así que dos etiquetas iguales pero distintas instancias
 * devolvían códigos distintos. Las colecciones basadas en tabla hash —{@code HashSet},
 * {@code HashMap}— buscan primero el cubo por {@code hashCode} y solo dentro de él comparan con
 * {@code equals}: si el código no coincide, ni siquiera llegan a comparar. De ahí que
 * {@code contains} devolviera falso, que el conjunto admitiera duplicados y que el mapa no
 * recuperara el valor. Ver {@link DemoEtiqueta}.
 *
 * <p>Comparación con un {@code record}: {@code record Etiqueta(String nombre, String valor)}
 * habría dado gratis, y coordinados, el constructor, los accesores, {@code equals},
 * {@code hashCode} y {@code toString}. Cuando la clase es un puro valor inmutable, el record no
 * es solo más corto: elimina por construcción la posibilidad de este error.
 */
public final class Etiqueta {

    private final String nombre;
    private final String valor;

    public Etiqueta(String nombre, String valor) {
        this.nombre = Objects.requireNonNull(nombre);
        this.valor = Objects.requireNonNull(valor);
    }

    public String nombre() { return nombre; }

    public String valor() { return valor; }

    @Override
    public boolean equals(Object otro) {
        return otro instanceof Etiqueta e
                && nombre.equals(e.nombre)
                && valor.equals(e.valor);
    }

    /**
     * La corrección del ejercicio. {@code Objects.hash} combina los campos en un único entero;
     * lo esencial es que use <strong>exactamente los mismos campos</strong> que {@code equals},
     * ni uno más ni uno menos.
     *
     * <p>Dos etiquetas distintas pueden compartir código hash (una colisión), y eso es correcto:
     * el contrato solo prohíbe lo contrario, que dos iguales tengan códigos distintos.
     */
    @Override
    public int hashCode() {
        return Objects.hash(nombre, valor);
    }

    @Override
    public String toString() {
        return nombre + "=" + valor;
    }
}
