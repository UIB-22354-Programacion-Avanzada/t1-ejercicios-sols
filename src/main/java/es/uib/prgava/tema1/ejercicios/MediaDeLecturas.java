package es.uib.prgava.tema1.ejercicios;

import java.util.List;

/**
 * Ejercicio 1.2.1, pieza 2 de 4: <strong>calcular</strong>.
 *
 * <p>Su única razón para cambiar es que cambie el estadístico: que en lugar de la media se quiera
 * la mediana, o la media de los últimos cinco. Quien pediría ese cambio es quien interpreta los
 * datos, y no tiene nada que ver con quien instala los sensores.
 *
 * <p>No guarda nada: recibe la lista y devuelve un número. Eso la hace trivial de probar y
 * reutilizable con lecturas que vengan de donde sea.
 */
public final class MediaDeLecturas {

    /** Sin lecturas no hay media; se devuelve 0.0 para conservar el comportamiento original. */
    public double de(List<Double> lecturas) {
        if (lecturas.isEmpty()) {
            return 0.0;
        }
        double suma = 0.0;
        for (double lectura : lecturas) {
            suma += lectura;
        }
        return suma / lecturas.size();
    }
}
