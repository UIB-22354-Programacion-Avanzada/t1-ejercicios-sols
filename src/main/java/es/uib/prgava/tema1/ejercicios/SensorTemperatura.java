package es.uib.prgava.tema1.ejercicios;

/**
 * Ejercicio 1.1.6. La lectura entra por el constructor y no se inventa: una prueba no puede
 * afirmar nada sobre un valor aleatorio, y este es el mismo argumento que vuelve en 1.2.8 con
 * {@code SondaDeterminista} y en 1.4.4 con {@code DetectorDeCaidas}.
 */
public final class SensorTemperatura extends Sensor {

    private final double grados;

    public SensorTemperatura(String ubicacion, double grados) {
        super(ubicacion);
        this.grados = grados;
    }

    @Override
    public double leer() {
        return grados;
    }

    @Override
    protected String magnitud() {
        return "temperatura";
    }

    // Sensor roto = new Sensor("sala-3");
    //   ^ no compila: "Sensor is abstract; cannot be instantiated". El compilador lo impide
    //     porque el objeto que saldría no sabría responder a leer(), y eso no es un descuido
    //     que haya que recordar: es una imposibilidad del tipo.
}
