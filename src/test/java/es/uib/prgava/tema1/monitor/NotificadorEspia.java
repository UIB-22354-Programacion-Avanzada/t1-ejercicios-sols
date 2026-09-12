package es.uib.prgava.tema1.monitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Ejercicio 1.4.3. <strong>Solución comentada.</strong> Doble de prueba del tipo <em>espía</em>
 * (<em>spy</em>): guarda en orden las alertas recibidas.
 *
 * <p>Vocabulario de dobles, que conviene no mezclar (Meszaros):
 * <ul>
 *   <li><em>dummy</em>: se pasa para rellenar un parámetro y nunca se usa;</li>
 *   <li><em>stub</em>: devuelve respuestas preparadas — es {@link SondaProgramada};</li>
 *   <li><em>spy</em>: además de responder, <strong>registra</strong> cómo se le ha llamado, para
 *       que la prueba pueda comprobarlo después — es esta clase;</li>
 *   <li><em>fake</em>: implementación de verdad pero simplificada, como un repositorio en memoria
 *       en lugar de una base de datos;</li>
 *   <li><em>mock</em>: espera por adelantado unas llamadas concretas y falla si no ocurren.</li>
 * </ul>
 *
 * <p>Escribirlos a mano, en vez de con una biblioteca como Mockito, tiene una ventaja didáctica:
 * se ve que un doble no es magia, es una clase de veinte líneas que implementa la interfaz. Y una
 * ventaja práctica: una prueba en la que el doble es difícil de escribir suele estar avisando de
 * que la interfaz que hay debajo es demasiado grande.
 */
public final class NotificadorEspia implements Notificador {

    private final List<Alerta> alertas = new ArrayList<>();

    @Override
    public void notificar(Alerta alerta) {
        // Una lista, y no un contador: guardar las alertas enteras permite comprobar también
        // QUÉ llegó y EN QUÉ ORDEN, no solo cuántas. Un doble que registra de menos obliga a
        // reescribirlo en cuanto la prueba siguiente quiere afinar más.
        alertas.add(alerta);
    }

    /** Alertas recibidas, en orden, como lista inmutable. */
    public List<Alerta> alertas() {
        return List.copyOf(alertas);
    }

    public int cuantas() {
        return alertas.size();
    }
}
