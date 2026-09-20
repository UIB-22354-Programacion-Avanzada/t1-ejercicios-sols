// MonitorEjercicioTest.java  (en src/test/java)
package es.uib.prgava.tema1.monitor;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ejercicio 1.4.3. <strong>Solución comentada.</strong> El monitor probado con los dos dobles.
 *
 * <p>Sin dobles, esta prueba sería imposible de escribir: la sonda real sale a la red —lenta, y
 * depende de que la red funcione— y el notificador real imprime por consola, que no se puede
 * comprobar. Con {@link SondaProgramada} y {@link NotificadorEspia}, la prueba decide qué mide la
 * sonda y lee lo que recibió el notificador, y por eso tarda milisegundos y da siempre el mismo
 * resultado.
 *
 * <p>Fíjate en qué es lo que permite todo esto: que {@code Monitor} reciba sus cuatro colaboradores
 * por el constructor, declarados con el tipo de la interfaz. La testabilidad no es una propiedad que
 * se añada a una clase: es una consecuencia de la inversión de dependencias del ejercicio 1.2.5.
 */
class MonitorEjercicioTest {

    private static final Servicio WEB = Servicio.desde("https://www.uib.es");
    private static final Servicio DNS = Servicio.desde("dns:uib.es");

    private RepositorioEnMemoria repositorio;
    private NotificadorEspia espia;

    @BeforeEach
    void prepararColaboradores() {
        repositorio = new RepositorioEnMemoria();
        espia = new NotificadorEspia();
    }

    private Monitor monitorCon(Sonda sonda, PoliticaDeAlerta politica) {
        var monitor = new Monitor(sonda, politica, repositorio);
        monitor.suscribir(espia);
        return monitor;
    }

    @Test
    void guardaCadaResultadoQueLaSondaDevuelve() {
        var monitor = monitorCon(SondaProgramada.con(Estado.ACTIVO, Estado.CAIDO),
                new FallosEnVentana(5, 5));

        monitor.comprobar(WEB);
        monitor.comprobar(WEB);

        assertEquals(2, repositorio.historial(WEB).size());
        assertEquals(Estado.ACTIVO, repositorio.historial(WEB).get(0).estado());
        assertEquals(Estado.CAIDO, repositorio.historial(WEB).get(1).estado());
    }

    @Test
    void avisaExactamenteCuandoLaPoliticaLoPide() {
        // Dos fallos en una ventana de dos: la política pide alerta en la segunda comprobación.
        var monitor = monitorCon(SondaProgramada.con(Estado.CAIDO, Estado.CAIDO),
                new FallosEnVentana(2, 2));

        monitor.comprobar(WEB);
        assertEquals(0, espia.cuantas(), "con un solo fallo todavía no toca avisar");

        monitor.comprobar(WEB);
        assertEquals(1, espia.cuantas(), "con el segundo fallo sí");
    }

    @Test
    void conUnaPoliticaQueNuncaPideNadaNoSaleNingunAviso() {
        var monitor = monitorCon(SondaProgramada.con(Estado.CAIDO), historial -> false);
        monitor.comprobar(WEB);
        monitor.comprobar(WEB);
        assertEquals(0, espia.cuantas());
    }

    @Test
    void elHistorialDeCadaServicioEsElSuyo() {
        // La sonda devuelve CAIDO para los dos servicios, cada uno con su propio Resultado; lo que
        // se comprueba es que el repositorio no los mezcla.
        var monitor = monitorCon(SondaProgramada.con(Estado.CAIDO), new FallosEnVentana(2, 2));

        monitor.comprobar(List.of(WEB, DNS));
        monitor.comprobar(List.of(WEB, DNS));

        assertEquals(2, repositorio.historial(WEB).size());
        assertEquals(2, repositorio.historial(DNS).size());
        assertEquals(1, espia.cuantasDe(WEB));
        assertEquals(1, espia.cuantasDe(DNS));
    }

    @Test
    void laAlertaDiceDeQueServicioEsYPorQue() {
        var politica = new FallosEnVentana(1, 1);
        var monitor = monitorCon(SondaProgramada.con(Estado.CAIDO), politica);

        monitor.comprobar(DNS);

        var alerta = espia.recibidas().get(0);
        assertEquals(DNS, alerta.servicio());
        assertTrue(alerta.mensaje().contains(politica.toString()),
                "el mensaje debe nombrar la condición que se ha cumplido");
    }

    @Test
    void unNotificadorDesuscritoDejaDeRecibir() {
        var monitor = monitorCon(SondaProgramada.con(Estado.CAIDO), new FallosEnVentana(1, 1));

        monitor.comprobar(WEB);
        monitor.desuscribir(espia);
        monitor.comprobar(WEB);

        assertEquals(1, espia.cuantas());
    }
}
