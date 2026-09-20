package es.uib.prgava.tema1.ejercicios;

import java.util.HashSet;

/**
 * Ejercicio 1.1.3. El programa que pone el fallo en evidencia. Es el mismo que trae la plantilla:
 * lo interesante no es el código, sino la diferencia entre sus dos salidas.
 *
 * <p>Con la {@code Etiqueta} defectuosa:
 *
 * <pre>
 * true      &lt;- equals funciona
 * false     &lt;- pero el conjunto no la encuentra
 * 2         &lt;- y admite duplicados
 * </pre>
 *
 * <p>Con la {@code Etiqueta} corregida:
 *
 * <pre>
 * true
 * true
 * 1
 * </pre>
 *
 * <p>La primera línea es la clave del ejercicio: {@code equals} respondía bien en los dos casos.
 */
public final class DemoEtiqueta {
    public static void main(String[] args) {
        var a = new Etiqueta("rack-3", "rojo");
        var b = new Etiqueta("rack-3", "rojo");
        System.out.println(a.equals(b));

        var conjunto = new HashSet<Etiqueta>();
        conjunto.add(a);
        System.out.println(conjunto.contains(b));
        conjunto.add(b);
        System.out.println(conjunto.size());
    }
}
