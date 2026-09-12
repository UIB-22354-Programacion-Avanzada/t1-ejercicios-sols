package es.uib.prgava.tema1.ejercicios;

import java.util.List;

/**
 * Ejercicio 1.3.4. <strong>Solución comentada.</strong> Las líneas siguen comentadas —como pide
 * el enunciado, para que el proyecto compile— y junto a cada una está el mensaje real del
 * compilador y su explicación.
 *
 * <p>Todas las prohibiciones tienen la misma raíz: en ejecución no queda rastro del parámetro de
 * tipo. El compilador prohíbe de antemano todo lo que, sin esa información, acabaría en un
 * {@code ClassCastException} inexplicable más adelante.
 */
public final class Borrado {

    private Borrado() { }

    public static <T> void ejemplos(List<T> lista, Object objeto) {

        // (1) T[] array = new T[10];
        //
        //     error: generic array creation
        //
        //     Crear un array exige conocer su tipo de componente en ejecución, porque el array
        //     lo guarda y lo comprueba en cada escritura (ArrayStoreException). Como T se borra,
        //     no hay tipo que guardar. El rodeo habitual es crear Object[] y hacer un cast no
        //     comprobado, o mejor, usar List<T> en lugar de arrays: los arrays son covariantes
        //     y los genéricos invariantes, y mezclarlos es fuente de problemas.

        // (2) if (objeto instanceof List<String> cadenas) { System.out.println(cadenas); }
        //
        //     error: illegal generic type for instanceof
        //
        //     instanceof pregunta por el tipo en EJECUCIÓN, y en ejecución solo existe "List".
        //     La pregunta "¿es una lista de cadenas?" no tiene respuesta. Lo legal es
        //     'objeto instanceof List<?>': con el comodín, la pregunta es solo "¿es una lista?",
        //     que sí se puede contestar.

        // (3) if (lista instanceof java.util.ArrayList<T> arreglo) { System.out.println(arreglo); }
        //
        //     Este caso es más sutil y, según la versión del compilador, PUEDE compilar: desde
        //     Java 16 se admite el instanceof genérico cuando la conversión es comprobable por
        //     el compilador a partir del tipo estático. Aquí 'lista' ya es List<T>, así que
        //     preguntar si es un ArrayList<T> no añade información que se haya borrado: el
        //     parámetro de tipo es el mismo. Si tu compilador lo acepta, la lección es la
        //     contraria y también útil: la prohibición no es "los genéricos y instanceof no se
        //     llevan", sino "no se puede preguntar por información que ya no existe".

        // (4) List<String>[] arrayDeListas = new List<String>[3];
        //
        //     error: generic array creation
        //
        //     Mismo motivo que (1), y aquí se ve el peligro concreto. Si se permitiera:
        //         Object[] objetos = arrayDeListas;     // los arrays son covariantes
        //         objetos[0] = List.of(1, 2, 3);        // una List<Integer>: el array solo
        //                                               // comprueba que es una List, y pasa
        //         String s = arrayDeListas[0].get(0);   // ClassCastException aquí, lejísimos
        //     El error aparecería en un sitio que no tiene ninguna relación con la causa.

        // (5) System.out.println(T.class);
        //
        //     error: cannot select from a type variable
        //
        //     'X.class' es un literal que el compilador resuelve a un objeto Class concreto.
        //     T no es una clase, es un nombre que desaparece al compilar; no hay ningún objeto
        //     Class que corresponda a T. Esta es exactamente la carencia que compensa el
        //     testigo de tipo de TestigoDeTipo: si necesitas T.class, pásalo como parámetro.

        // Uso trivial de los parámetros para que el método no dé avisos de no usarlos:
        assert lista != null || objeto != null;
    }

    // (6) Estas dos sobrecargas no pueden coexistir:
    //
    //     public static void procesar(List<String> cadenas) { }
    //     public static void procesar(List<Integer> enteros) { }
    //
    //     error: name clash: procesar(List<Integer>) and procesar(List<String>) have the same
    //            erasure
    //
    //     La sobrecarga se resuelve por la firma que queda tras el borrado, y ambas se borran a
    //     'procesar(List)'. Son el mismo método para la JVM, y no podrían distinguirse en el
    //     fichero .class. El rodeo, si de verdad hacen falta las dos, es darles nombres
    //     distintos —procesarCadenas / procesarEnteros—, que además se lee mejor.
}
