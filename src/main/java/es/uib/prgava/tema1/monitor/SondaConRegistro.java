package es.uib.prgava.tema1.monitor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Ejercicio 1.2.7. <strong>Solución comentada.</strong> Decorador de {@link Sonda} que cuenta
 * cuántas veces se ha sondeado cada servicio.
 *
 * <p>Patrón <em>Decorator</em>: implementa la misma interfaz que decora y guarda una referencia a
 * otra instancia de esa interfaz. Así puede envolver cualquier sonda —la simulada, la de
 * reintentos, la de tiempo de espera, o la de sockets reales del Tema 4— sin conocerlas, y puede
 * a su vez ser envuelta por otras.
 *
 * <p>Las dos reglas del decorador, que la prueba comprueba explícitamente:
 * <strong>delegar siempre</strong> y <strong>no alterar el resultado</strong>. Este añade un
 * efecto lateral (contar) y devuelve intacto lo que le dio la sonda interna.
 */
public final class SondaConRegistro implements Sonda {

    private final Sonda interna;
    private final Map<Servicio, Integer> conteos = new HashMap<>();

    public SondaConRegistro(Sonda interna) {
        // Fallar aquí, y no en el primer sondeo, es la diferencia entre un error claro en el
        // cableado y un NullPointerException media hora después, lejos de su causa.
        this.interna = Objects.requireNonNull(interna, "La sonda interna no puede ser nula");
    }

    @Override
    public Resultado sondear(Servicio servicio) {
        // merge(clave, 1, Integer::sum): si no hay entrada, pone 1; si la hay, le suma 1.
        // Equivale a un getOrDefault seguido de un put, pero en una sola operación.
        conteos.merge(servicio, 1, Integer::sum);

        // Y se devuelve tal cual lo que diga la sonda interna: el decorador observa, no decide.
        return interna.sondear(servicio);
    }

    /** Copia inmutable de los recuentos por servicio. */
    public Map<Servicio, Integer> conteos() {
        // Devolver 'conteos' directamente dejaría que quien lo recibe modificara nuestro estado
        // interno, y además vería cambios posteriores sin esperarlos. Map.copyOf corta las dos
        // cosas: es una copia y es inmutable.
        return Map.copyOf(conteos);
    }

    // Nota sobre la segunda prueba, que es la parte interesante del ejercicio:
    //
    //   new SondaConRegistro(new SondaConReintentos(base, 3))   -> cuenta 1
    //   new SondaConReintentos(new SondaConRegistro(base), 3)   -> cuenta 3
    //
    // El mismo par de decoradores, en el orden contrario, mide cosas distintas. Por fuera se
    // cuentan las COMPROBACIONES LÓGICAS (una por llamada, con reintentos incluidos); por dentro
    // se cuentan los SONDEOS REALES (uno por intento). Ninguno es más correcto que el otro: hay
    // que decidir qué se quiere medir. Ese es el precio de la composición — flexible, pero el
    // orden importa y no es evidente a simple vista.
}
