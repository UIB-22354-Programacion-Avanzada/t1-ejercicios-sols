package es.uib.prgava.tema1.ejercicios;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Ejercicio 1.1.3. <strong>Solución comentada.</strong> Programa que pone en evidencia el defecto
 * de {@link Etiqueta} cuando le falta {@code hashCode}.
 *
 * <p>Salida <em>antes</em> de añadir {@code hashCode} (la del enunciado):
 * <pre>
 * 1) contains con una etiqueta igual : false
 * 2) tamaño tras añadir dos iguales  : 2
 * 3) valor recuperado del mapa       : null
 * </pre>
 *
 * <p>Salida <em>después</em> de añadirlo (esta versión):
 * <pre>
 * 1) contains con una etiqueta igual : true
 * 2) tamaño tras añadir dos iguales  : 1
 * 3) valor recuperado del mapa       : edificio A
 * </pre>
 *
 * <p>Lo revelador es que {@code equals} funcionaba en los tres casos: el fallo solo aparece al
 * usar la clase dentro de una colección con tabla hash. Por eso este tipo de defecto sobrevive
 * a las pruebas superficiales y aparece mucho más tarde, en producción.
 */
public final class DemoEtiqueta {

    private DemoEtiqueta() { }

    public static void main(String[] args) {
        var original = new Etiqueta("entorno", "produccion");
        var igual = new Etiqueta("entorno", "produccion");

        // Comprobación previa: equals sí funciona, tenga o no hashCode.
        System.out.println("0) equals entre dos etiquetas iguales : " + original.equals(igual));

        // 1. Buscar en un conjunto una etiqueta igual pero distinta instancia.
        var conjunto = new HashSet<Etiqueta>();
        conjunto.add(original);
        System.out.println("1) contains con una etiqueta igual : " + conjunto.contains(igual));

        // 2. Añadir dos veces la misma etiqueta: un conjunto no debe admitir duplicados.
        var duplicados = new HashSet<Etiqueta>();
        duplicados.add(new Etiqueta("zona", "aulario"));
        duplicados.add(new Etiqueta("zona", "aulario"));
        System.out.println("2) tamaño tras añadir dos iguales  : " + duplicados.size());

        // 3. Usar la etiqueta como clave de un mapa y recuperar con una clave igual.
        var mapa = new HashMap<Etiqueta, String>();
        mapa.put(new Etiqueta("zona", "aulario"), "edificio A");
        System.out.println("3) valor recuperado del mapa       : "
                + mapa.get(new Etiqueta("zona", "aulario")));
    }
}
