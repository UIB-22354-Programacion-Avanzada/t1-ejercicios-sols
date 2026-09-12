package es.uib.prgava.tema1.ejercicios;

/**
 * Ejercicio 1.2.9. <strong>Solución comentada.</strong> Opciones de un informe, construidas con
 * un <em>builder</em>.
 *
 * <p>Por qué un Builder y no un constructor con cuatro parámetros: mira la diferencia entre
 *
 * <pre>new ConfiguracionInforme("Informe semanal", 3, true, true)</pre>
 *
 * y
 *
 * <pre>ConfiguracionInforme.builder().titulo("Informe semanal").maximoServicios(3)
 *         .soloConFallos(true).build()</pre>
 *
 * <p>En la primera forma, los dos {@code boolean} contiguos son indistinguibles al leer, y
 * cambiarlos de orden compila igual de bien y hace algo distinto. Es el «constructor
 * telescópico» que describe Bloch: cuantos más parámetros del mismo tipo, más fácil es
 * equivocarse sin que nadie avise. El builder pone un nombre a cada valor y permite indicar solo
 * los que se apartan del valor por defecto.
 *
 * <p>Y el objeto construido sigue siendo un {@code record} inmutable: el builder es mutable
 * durante la construcción y desaparece al terminar.
 */
public record ConfiguracionInforme(String titulo, int maximoServicios,
                                   boolean incluirLatencias, boolean soloConFallos) {

    /**
     * Toda la validación vive <strong>aquí</strong>, en el constructor compacto del record, y no
     * en {@code Builder.build()}. Es deliberado: así es imposible construir una configuración
     * inválida, se pase por el builder o se llame directamente al constructor. Si la validación
     * estuviera en el builder, el constructor sería una puerta trasera sin vigilar — y la prueba
     * {@code laValidacionEstaEnElConstructorDelRecord} comprueba justamente eso.
     */
    public ConfiguracionInforme {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        if (maximoServicios < 1) {
            throw new IllegalArgumentException(
                    "El máximo de servicios debe ser al menos 1, y llegó " + maximoServicios);
        }
        titulo = titulo.strip();      // en un constructor compacto se puede normalizar:
                                      // el valor asignado al campo es el de la variable al final
    }

    public static Builder builder() {
        return new Builder();
    }

    /** Valores por defecto: "Informe de disponibilidad", 10, true, false. */
    public static final class Builder {

        // Los valores por defecto viven en los campos del builder. Ponerlos aquí, y no
        // repartidos en sobrecargas del constructor, los deja documentados en un solo sitio.
        private String titulo = "Informe de disponibilidad";
        private int maximoServicios = 10;
        private boolean incluirLatencias = true;
        private boolean soloConFallos = false;

        // Constructor privado: al builder se llega por ConfiguracionInforme.builder(), que es
        // donde el lector lo busca.
        private Builder() { }

        // Cada método devuelve 'this'. Eso es lo que permite encadenar las llamadas, y es la
        // única razón por la que no devuelven void.
        public Builder titulo(String titulo) {
            this.titulo = titulo;
            return this;
        }

        public Builder maximoServicios(int maximo) {
            this.maximoServicios = maximo;
            return this;
        }

        public Builder incluirLatencias(boolean incluir) {
            this.incluirLatencias = incluir;
            return this;
        }

        public Builder soloConFallos(boolean solo) {
            this.soloConFallos = solo;
            return this;
        }

        /** Toda la validación la hace el constructor compacto del record. */
        public ConfiguracionInforme build() {
            return new ConfiguracionInforme(titulo, maximoServicios, incluirLatencias, soloConFallos);
        }
    }
}
