package es.uib.prgava.tema1.ejercicios;

/**
 * Ejercicios 1.1.2 y 1.1.7. <strong>Solución comentada.</strong>
 *
 * <p>Lo contrario del ejercicio anterior: aquí sí hay <strong>identidad</strong> y sí hay estado
 * que cambia. Un servidor que entra en mantenimiento sigue siendo el mismo servidor, así que no
 * se puede modelar como un valor inmutable y un {@code record} no sirve. El precio de poder
 * cambiar de estado es todo lo que hay que escribir a mano: constructor, validación, accesores,
 * {@code toString}.
 *
 * <p>Fíjate en el reparto de {@code final}: el nombre lo es y el estado de mantenimiento no. Esa
 * línea es la decisión de diseño del ejercicio, y está contestada al final de la clase.
 */
public class Servidor implements Identificable {

    /**
     * Atributo de <strong>clase</strong>: existe uno solo, compartido por todos los objetos, y
     * vive mientras viva la clase. Es {@code private} como cualquier otro campo: que sea
     * {@code static} no es excusa para abrirlo, porque entonces cualquiera podría falsear la
     * cuenta.
     */
    private static int servidoresCreados = 0;

    private final String nombre;          // la identidad: no cambia
    private boolean enMantenimiento;      // el estado: cambia

    /**
     * Primero se valida, después se asigna e incrementa. Si algo falla, la excepción sale antes
     * de haber tocado ningún campo y antes de haber contado el servidor: el objeto simplemente no
     * llega a existir, y la cuenta sigue cuadrando.
     *
     * @throws IllegalArgumentException si el nombre es {@code null} o está en blanco
     */
    public Servidor(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre.strip();
        this.enMantenimiento = false;     // un servidor nace operativo
        servidoresCreados++;
    }

    /**
     * Método de clase: se invoca como {@code Servidor.servidoresCreados()}, sobre el tipo y no
     * sobre un objeto, porque la respuesta no depende de ningún servidor en concreto.
     */
    public static int servidoresCreados() {
        return servidoresCreados;
    }

    public void entrarEnMantenimiento() {
        enMantenimiento = true;
    }

    public void salirDeMantenimiento() {
        enMantenimiento = false;
    }

    public String nombre() {
        return nombre;
    }

    public boolean estaEnMantenimiento() {
        return enMantenimiento;
    }

    @Override
    public String toString() {
        return nombre + (enMantenimiento ? " (en mantenimiento)" : " (operativo)");
    }

    /** Ejercicio 1.1.7. El nombre es lo que identifica a un servidor en el inventario. */
    @Override
    public String identificador() {
        return nombre;
    }

    // ----------------------------------------------------------------------------------
    // RESPUESTA a lo que pedía el enunciado: por qué no hay un método para cambiar el nombre.
    //
    // Porque el nombre ES la identidad del servidor: es lo que devuelve identificador() y lo que
    // aparece en cualquier registro o informe que lo mencione. Un servidor que cambia de nombre
    // no es el mismo servidor con otra etiqueta; es otro servidor, y hay que construirlo.
    // Declarar el campo `final` convierte ese razonamiento en una garantía del compilador, en
    // lugar de en una regla que hay que recordar cada vez que alguien añade un método.
    //
    // Encapsular no es «poner private y añadir un get y un set por campo». Es decidir qué puede
    // cambiar y ofrecer solo esas operaciones: aquí, entrar y salir de mantenimiento, y nada más.
    // ----------------------------------------------------------------------------------
}
