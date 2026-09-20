package es.uib.prgava.tema1.ejercicios;

/**
 * Ejercicio 1.1.6. <strong>Solución comentada.</strong> Clase abstracta: lo común está escrito una
 * vez, y lo que cambia se declara sin cuerpo.
 *
 * <p>El criterio para que una clase sea abstracta no es «tiene subclases», es que
 * <strong>instanciarla no tendría sentido</strong>. Un «sensor» genérico no puede leer nada,
 * porque leer significa una cosa distinta en cada sensor; declararla abstracta convierte
 * {@code new Sensor("sala-3")} en un error de compilación, y no en un objeto a medio hacer que
 * revienta en ejecución.
 *
 * <p>Hay dos métodos abstractos, y hacen falta los dos: {@code leer()} porque la medida cambia, y
 * {@code magnitud()} porque el texto de {@code describir()} tiene que nombrar lo que se mide. Sin
 * el segundo, {@code describir()} no se podría escribir aquí y habría que copiarlo en cada
 * subclase, que es exactamente lo que la clase abstracta viene a evitar.
 */
public abstract class Sensor {

    private final String ubicacion;

    /**
     * Constructor {@code protected}: no es {@code public} porque nadie de fuera va a construir un
     * {@code Sensor} —no se puede—, pero las subclases tienen que poder llamarlo con
     * {@code super(ubicacion)}.
     */
    protected Sensor(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String ubicacion() {
        return ubicacion;
    }

    /** Qué mide este sensor ahora mismo. Sin cuerpo: no hay una lectura genérica razonable. */
    public abstract double leer();

    /** Cómo se llama esta magnitud. Lo necesita {@link #describir()}. */
    protected abstract String magnitud();

    /**
     * Escrito <strong>una sola vez</strong>, y funciona para las dos subclases.
     *
     * <p>Llama a {@code magnitud()} y a {@code leer()}, que aquí no tienen cuerpo, y eso es
     * precisamente el mecanismo que hace útil la herencia: en tiempo de ejecución el objeto es un
     * {@code SensorTemperatura} o un {@code SensorHumedad}, y se ejecuta la versión de la subclase.
     * El código de la superclase llama «hacia abajo» sin conocer a nadie.
     */
    public String describir() {
        return magnitud() + " en " + ubicacion + ": " + leer();
    }
}
