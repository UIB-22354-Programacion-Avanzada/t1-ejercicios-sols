package es.uib.prgava.tema1.ejercicios;

/**
 * Ejercicio 1.2.1, pieza 3 de 4: <strong>decidir</strong>.
 *
 * <p>Su única razón para cambiar es que cambie el criterio de aviso: el umbral, el mínimo de
 * lecturas, o la idea misma de qué es una situación anómala. Es el cambio que se pide más a menudo,
 * y por eso es el que más conviene tener aislado. Compárala con {@code PoliticaDeAlerta} del
 * monitor: es la misma responsabilidad, y en 1.2.2 se convertirá en una estrategia intercambiable.
 */
public final class DecisionDeAviso {

    private static final int LECTURAS_MINIMAS = 3;

    private final double umbral;

    public DecisionDeAviso(double umbral) {
        this.umbral = umbral;
    }

    public double umbral() {
        return umbral;
    }

    public boolean hayQueAvisar(int cuantasLecturas, double media) {
        return cuantasLecturas >= LECTURAS_MINIMAS && media > umbral;
    }
}
