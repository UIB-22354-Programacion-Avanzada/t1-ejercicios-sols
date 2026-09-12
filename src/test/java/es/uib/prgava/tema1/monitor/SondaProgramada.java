package es.uib.prgava.tema1.monitor;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ejercicio 1.4.3. <strong>Solución comentada.</strong> Doble de prueba del tipo <em>stub</em>:
 * devuelve, para cada servicio, los estados de su lista en orden, y repite el último cuando se
 * agota.
 *
 * <p>Sustituye a {@code SondaSimulada} en las pruebas, y la diferencia es toda: la simulada usa
 * un generador aleatorio —reproducible con una semilla fija, pero opaco: para saber qué va a
 * devolver hay que ejecutarla—, mientras que esta hace exactamente lo que dice su guion. Una
 * prueba escrita con ella se lee como una historia: «responde bien, luego falla, luego falla».
 *
 * <p>Que repita el último estado en vez de agotarse evita que la prueba tenga que enumerar
 * exactamente tantos estados como comprobaciones vaya a hacer, que es un acoplamiento incómodo
 * y una fuente de fallos cuando se añade una comprobación más.
 *
 * <p>Vive en {@code src/test/java} porque solo sirve para probar: no se empaqueta con el
 * programa, y nada del código de producción puede depender de ella ni por accidente.
 */
public final class SondaProgramada implements Sonda {

    private final Map<Servicio, Deque<Estado>> pendientes = new HashMap<>();
    private final Map<Servicio, Estado> ultimo = new HashMap<>();

    public SondaProgramada(Map<Servicio, List<Estado>> programa) {
        // Copiamos el programa a colas propias: así la sonda puede consumirlo sin modificar los
        // datos que le pasó la prueba, y dos sondas construidas con el mismo mapa no se pisan.
        programa.forEach((servicio, estados) -> pendientes.put(servicio, new ArrayDeque<>(estados)));
    }

    @Override
    public Resultado sondear(Servicio servicio) {
        Deque<Estado> cola = pendientes.get(servicio);

        Estado estado;
        if (cola != null && !cola.isEmpty()) {
            estado = cola.removeFirst();
            ultimo.put(servicio, estado);          // recordamos por si hay que repetirlo
        } else {
            // Guion agotado, o servicio sin guion: repetimos el último, y si nunca hubo
            // ninguno, ACTIVO como comportamiento neutro por defecto.
            estado = ultimo.getOrDefault(servicio, Estado.ACTIVO);
        }

        // Latencias fijas: la prueba no depende de ellas, y así el resultado es reproducible
        // hasta el último detalle.
        return switch (estado) {
            case ACTIVO    -> Resultado.activo(servicio, Duration.ofMillis(100));
            case DEGRADADO -> Resultado.degradado(servicio, Duration.ofMillis(800));
            case CAIDO     -> Resultado.caido(servicio);
        };
    }
}
