package es.uib.prgava.tema1.ejercicios;

import java.util.Objects;

/**
 * Ejercicio 1.1.2. Servidor con nombre, dirección y puerto.
 *
 * <p><strong>Solución comentada.</strong> Clase tradicional y no un {@code record}, porque aquí sí
 * hay estado que cambia: un servidor se arranca y se para. Un record tiene todos sus campos
 * finales, así que no puede modelar eso.
 */
public class Servidor {

    /**
     * Atributo de <strong>clase</strong>: existe uno solo, compartido por todas las instancias,
     * y vive mientras viva la clase. No confundir con un atributo de instancia, del que hay una
     * copia por objeto. Es privado y solo se lee a través de {@link #getServidoresCreados()}:
     * si fuera público, cualquiera podría falsear la cuenta.
     */
    private static int servidoresCreados = 0;

    // El nombre no es final porque es lo único que tiene sentido cambiar (ver setNombre).
    private String nombre;

    // Dirección y puerto son finales: forman parte de la identidad del servidor (ver equals).
    private final DireccionIpv4 direccion;
    private final int puerto;

    private boolean enServicio;

    /**
     * Constructor principal. Es el único que toca los campos directamente; todos los demás
     * caminos de construcción deben pasar por aquí.
     */
    public Servidor(String nombre, DireccionIpv4 direccion, int puerto) {
        // Validar ANTES de asignar: si algo falla, el objeto no llega a existir en un estado
        // inconsistente. Este es el principio de «un objeto nace válido y muere válido».
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        // requireNonNull lanza NullPointerException, que es la excepción correcta para un
        // argumento nulo; IllegalArgumentException queda para valores presentes pero inválidos.
        Objects.requireNonNull(direccion, "La dirección no puede ser nula");
        if (puerto < 1 || puerto > 65535) {
            throw new IllegalArgumentException("Puerto fuera de rango: " + puerto);
        }

        this.nombre = nombre.strip();
        this.direccion = direccion;
        this.puerto = puerto;
        this.enServicio = false;          // un servidor recién creado aún no da servicio
        servidoresCreados++;              // se cuenta aquí, en el único constructor real
    }

    /**
     * Constructor sobrecargado que usa el puerto 443 (HTTPS).
     *
     * <p>{@code this(...)} delega en el constructor principal, y tiene que ser la primera
     * sentencia. Es la forma correcta de encadenar constructores: la validación y el incremento
     * del contador ocurren una sola vez, en un solo sitio. Copiar el cuerpo aquí habría
     * duplicado las reglas y, peor, habría contado dos veces el servidor.
     */
    public Servidor(String nombre, DireccionIpv4 direccion) {
        this(nombre, direccion, 443);
    }

    /**
     * Método de <strong>clase</strong>: se invoca como {@code Servidor.getServidoresCreados()},
     * sin necesidad de tener ningún objeto, porque la información que devuelve no pertenece a
     * ninguna instancia concreta.
     */
    public static int getServidoresCreados() {
        return servidoresCreados;
    }

    public void arrancar() {
        enServicio = true;
    }

    public void parar() {
        enServicio = false;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * Devolver {@code direccion} directamente es seguro <em>porque {@link DireccionIpv4} es
     * inmutable</em>: quien la reciba no puede modificarla. Si fuera una clase mutable, aquí
     * haría falta una copia defensiva, o estaríamos regalando acceso a nuestro estado interno.
     */
    public DireccionIpv4 getDireccion() {
        return direccion;
    }

    public int getPuerto() {
        return puerto;
    }

    /** Convención JavaBeans: los accesores de {@code boolean} se llaman {@code isXxx}. */
    public boolean isEnServicio() {
        return enServicio;
    }

    /** El nombre sí se puede cambiar: es una etiqueta, no parte de la identidad. */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre.strip();
    }

    /**
     * Igualdad <strong>por dirección y puerto</strong>, no por nombre: dos entradas de
     * configuración que apuntan al mismo extremo de red son el mismo servidor aunque se llamen
     * distinto («web» y «web-replica»).
     *
     * <p>{@code instanceof} con patrón (Java 16+) sustituye al clásico
     * {@code if (!(otro instanceof Servidor)) return false;} seguido de un cast: comprueba el
     * tipo y declara la variable ya convertida en una sola expresión.
     */
    @Override
    public boolean equals(Object otro) {
        return otro instanceof Servidor s
                && puerto == s.puerto
                && direccion.equals(s.direccion);
    }

    /**
     * Regla de oro: si dos objetos son iguales según {@code equals}, sus {@code hashCode} deben
     * coincidir. Por eso este {@code hashCode} usa <strong>los mismos campos</strong> que
     * {@code equals} —dirección y puerto— y ninguno más. Incluir el nombre rompería el contrato
     * y haría que un {@code HashSet} tratara como distintos a dos servidores iguales. El
     * ejercicio 1.1.3 muestra en vivo qué ocurre cuando esto se incumple.
     */
    @Override
    public int hashCode() {
        return Objects.hash(direccion, puerto);
    }

    @Override
    public String toString() {
        return "Servidor[" + nombre + ", " + direccion + ":" + puerto
                + ", " + (enServicio ? "en servicio" : "parado") + "]";
    }

    // Respuesta al TODO 1.1.2 (por qué no hay setDireccion ni setPuerto):
    //
    // Porque la identidad del servidor ES la pareja (dirección, puerto): es lo que compara
    // equals y lo que resume hashCode. Si se pudieran cambiar, el hashCode de un objeto ya
    // metido en un HashSet o usado como clave de un HashMap cambiaría bajo los pies de la
    // colección, y el objeto quedaría irrecuperable: seguiría en el cubo antiguo mientras las
    // búsquedas irían al nuevo. Dejarlos finales convierte ese error en imposible, en lugar de
    // en una regla que hay que recordar. Cambiar de dirección o de puerto significa que es
    // otro servidor: constrúyelo.
}
