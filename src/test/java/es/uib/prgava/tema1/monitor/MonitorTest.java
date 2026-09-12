package es.uib.prgava.tema1.monitor;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Ejercicio 1.4.3. <strong>Solución comentada.</strong> Pruebas de {@link Monitor} con dobles.
 *
 * <p>Todo lo que hace {@code Monitor} depende de tres colaboradores que recibe por constructor, y
 * eso es lo que permite escribir estas pruebas. Con la versión del apartado 1.2 que se fabricaba
 * sus propias dependencias, ninguna de ellas se podría escribir: no habría forma de decidir qué
 * responde la sonda ni de ver qué alertas se emitieron.
 *
 * <p>Ninguna prueba usa números aleatorios ni espera tiempo real: todas terminan en
 * microsegundos y dan siempre el mismo resultado. Una prueba que a veces pasa y a veces no es
 * peor que no tenerla, porque enseña al equipo a ignorar el rojo.
 */
class MonitorTest {

    private static final Servicio WEB = Servicio.desde("https://www.uib.es");
    private static final Servicio DNS = Servicio.desde("dns:uib.es");

    @Test
    void dosServiciosDistintosAlertanDeFormaIndependiente() {
        // WEB falla dos veces seguidas -> debe alertar. DNS falla y se recupera -> no.
        var sonda = new SondaProgramada(Map.of(
                WEB, List.of(Estado.CAIDO, Estado.CAIDO),
                DNS, List.of(Estado.CAIDO, Estado.ACTIVO)));
        var espia = new NotificadorEspia();
        var monitor = new Monitor(sonda, new FallosConsecutivos(2), new RepositorioEnMemoria());
        monitor.suscribir(espia);

        monitor.comprobar(List.of(WEB, DNS));
        monitor.comprobar(List.of(WEB, DNS));

        // Una sola alerta, y de WEB: el historial de cada servicio se evalúa por separado.
        // Si Monitor mezclara los historiales, aquí saldrían dos.
        assertEquals(1, espia.cuantas());
        assertEquals(WEB, espia.alertas().get(0).servicio());
    }

    @Test
    void elOrdenDeLasAlertasCoincideConElOrdenDeComprobacion() {
        var sonda = new SondaProgramada(Map.of(
                WEB, List.of(Estado.CAIDO, Estado.CAIDO),
                DNS, List.of(Estado.CAIDO, Estado.CAIDO)));
        var espia = new NotificadorEspia();
        var monitor = new Monitor(sonda, new FallosConsecutivos(2), new RepositorioEnMemoria());
        monitor.suscribir(espia);

        // Comprobamos primero DNS y luego WEB: las alertas deben salir en ese mismo orden.
        monitor.comprobar(List.of(DNS, WEB));
        monitor.comprobar(List.of(DNS, WEB));

        assertEquals(List.of(DNS, WEB),
                espia.alertas().stream().map(Alerta::servicio).toList());
    }

    @Test
    void unServicioQueSeRecuperaVuelveAEmpezarLaCuenta() {
        // Guion: falla, se recupera, falla, falla. Con FallosConsecutivos(2) solo debe haber
        // UNA alerta, al final. Si Monitor contara los fallos acumulados en vez de mirar el
        // final del historial, habría alertado antes.
        var sonda = new SondaProgramada(Map.of(
                WEB, List.of(Estado.CAIDO, Estado.ACTIVO, Estado.CAIDO, Estado.CAIDO)));
        var espia = new NotificadorEspia();
        var monitor = new Monitor(sonda, new FallosConsecutivos(2), new RepositorioEnMemoria());
        monitor.suscribir(espia);

        for (int i = 0; i < 4; i++) {
            monitor.comprobar(WEB);
        }

        assertEquals(1, espia.cuantas());
    }

    @Test
    void todosLosSuscriptoresRecibenLaMismaAlerta() {
        // El patrón Observer en una línea: Monitor no sabe cuántos observadores hay.
        var sonda = new SondaProgramada(Map.of(WEB, List.of(Estado.CAIDO, Estado.CAIDO)));
        var primero = new NotificadorEspia();
        var segundo = new NotificadorEspia();
        var monitor = new Monitor(sonda, new FallosConsecutivos(2), new RepositorioEnMemoria());
        monitor.suscribir(primero);
        monitor.suscribir(segundo);

        monitor.comprobar(WEB);
        monitor.comprobar(WEB);

        assertEquals(1, primero.cuantas());
        assertEquals(primero.alertas(), segundo.alertas());
    }

    @Test
    void elResultadoDeCadaComprobacionQuedaEnElRepositorio() {
        var repositorio = new RepositorioEnMemoria();
        var sonda = new SondaProgramada(Map.of(WEB, List.of(Estado.ACTIVO, Estado.CAIDO)));
        var monitor = new Monitor(sonda, new FallosConsecutivos(2), repositorio);

        monitor.comprobar(WEB);
        monitor.comprobar(WEB);

        assertEquals(2, repositorio.historial(WEB).size());
        assertEquals(Estado.CAIDO, repositorio.ultimo(WEB).orElseThrow().estado());
    }
}
