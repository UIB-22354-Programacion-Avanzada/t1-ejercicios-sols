package es.uib.prgava.tema1.ejercicios;

import java.util.ArrayList;
import java.util.List;

/**
 * Ejercicio 1.2.1, pieza 1 de 4: <strong>guardar</strong> las lecturas.
 *
 * <p>Su única razón para cambiar es que cambien las reglas sobre qué lectura es admisible o cómo se
 * conservan. Quien pediría ese cambio es quien instala los sensores.
 */
public final class LecturasSensor {

    private final List<Double> lecturas = new ArrayList<>();

    public void anadir(double lectura) {
        if (lectura < -50 || lectura > 150) {
            throw new IllegalArgumentException("lectura fuera de rango: " + lectura);
        }
        lecturas.add(lectura);
    }

    /** Copia inmutable: nadie de fuera puede añadir lecturas saltándose la validación. */
    public List<Double> todas() {
        return List.copyOf(lecturas);
    }

    public int cuantas() {
        return lecturas.size();
    }
}
