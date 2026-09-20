package es.uib.prgava.tema1.ejercicios;

/** Ejercicio 1.1.6. La segunda subclase, para comprobar que {@code describir()} sirve a las dos. */
public final class SensorHumedad extends Sensor {

    private final double porcentaje;

    public SensorHumedad(String ubicacion, double porcentaje) {
        super(ubicacion);
        this.porcentaje = porcentaje;
    }

    @Override
    public double leer() {
        return porcentaje;
    }

    @Override
    protected String magnitud() {
        return "humedad";
    }
}
