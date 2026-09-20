package es.uib.prgava.tema1.ejercicios;

/**
 * Ejercicio 1.1.7. Un único método que funciona con un servidor y con una dirección IP a la vez.
 */
public final class Inventario {

    /** Clase de utilidades: constructor privado para que nadie la instancie. */
    private Inventario() {
    }

    /**
     * Recibe un <strong>parámetro variable</strong> de {@code Identificable}: dentro del método es
     * un array, y desde fuera se llama con tantos argumentos como se quiera.
     *
     * <p>El método no sabe qué clases recibe, y ahí está el polimorfismo de subtipo: lo único que
     * sabe es que cualquier cosa que llegue responde a {@code etiqueta()}. Nótese que ni siquiera
     * llama a {@code identificador()}: llama al método {@code default}, que a su vez llama al
     * abstracto, y quien decide qué se ejecuta es el objeto.
     *
     * <p>Y la respuesta al «Para pensar»: el día que añadas una tercera clase {@code Identificable}
     * no hay que cambiar <strong>nada</strong> aquí. Ese es todo el argumento de programar contra
     * la interfaz, y es el mismo que en 1.2 se llamará principio abierto/cerrado.
     */
    public static void mostrarTodos(Identificable... cosas) {
        for (var cosa : cosas) {
            System.out.println(cosa.etiqueta());
        }
    }
}
