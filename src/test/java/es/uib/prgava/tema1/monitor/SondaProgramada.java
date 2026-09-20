// SondaProgramada.java  (en src/test/java)
package es.uib.prgava.tema1.monitor;

import java.time.Duration;
import java.util.List;

/**
 * Ejercicio 1.4.3. Un <strong>doble de prueba</strong>: una sonda que devuelve los estados que la
 * prueba le diga, en el orden que le diga, para el servicio que se le pregunte.
 *
 * <p>Vive en {@code src/test/java} y no en {@code src/main/java}, y eso es una decisión, no un
 * detalle de organización: no forma parte del programa, forma parte de sus pruebas, y no debe acabar
 * en el artefacto que se entrega.
 *
 * <p>Sigue el modelo de {@link SondaFalsa}, del tema, con dos añadidos que las pruebas de
 * {@code MonitorEjercicioTest} necesitan: la latencia la elige la prueba, y la sonda cuenta cuántas
 * veces se le ha pedido un sondeo.
 *
 * <p>Detalle importante y fácil de equivocar: el {@code Resultado} se construye <strong>para el
 * servicio que se pregunta</strong>, no para uno fijado al crear la sonda. Un doble que devolviera
 * siempre resultados del mismo servicio haría que el repositorio los guardara todos bajo ese
 * servicio, y la prueba mediría algo que no es. Un doble mal hecho no falla: engaña.
 */
final class SondaProgramada implements Sonda {

    private static final Duration LATENCIA_POR_OMISION = Duration.ofMillis(100);

    private final List<Estado> estados;
    private final Duration latencia;
    private int sondeos = 0;

    SondaProgramada(List<Estado> estados, Duration latencia) {
        if (estados.isEmpty()) {
            throw new IllegalArgumentException("al menos un estado");
        }
        this.estados = List.copyOf(estados);
        this.latencia = latencia;
    }

    /** Fábrica estática para que la prueba se lea bien: {@code SondaProgramada.con(CAIDO, ACTIVO)}. */
    static SondaProgramada con(Estado... estados) {
        return new SondaProgramada(List.of(estados), LATENCIA_POR_OMISION);
    }

    static SondaProgramada con(Duration latencia, Estado... estados) {
        return new SondaProgramada(List.of(estados), latencia);
    }

    @Override
    public Resultado sondear(Servicio servicio) {
        var actual = estados.get(Math.min(sondeos, estados.size() - 1));   // el último se repite
        sondeos++;
        return switch (actual) {
            case ACTIVO    -> Resultado.activo(servicio, latencia);
            case DEGRADADO -> Resultado.degradado(servicio, latencia);
            case CAIDO     -> Resultado.caido(servicio);
        };
    }

    /** Cuántas veces se le ha pedido un sondeo. Sirve para comprobar que el monitor la llamó. */
    int sondeos() {
        return sondeos;
    }
}
