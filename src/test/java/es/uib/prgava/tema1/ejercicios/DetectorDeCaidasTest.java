package es.uib.prgava.tema1.ejercicios;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import es.uib.prgava.tema1.monitor.Estado;
import es.uib.prgava.tema1.monitor.Servicio;
import es.uib.prgava.tema1.monitor.SondaProgramada;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ejercicio 1.4.5. <strong>Solución comentada.</strong> Pruebas de {@link DetectorDeCaidas},
 * posibles solo después de refactorizarlo para que reciba el reloj y la sonda por constructor.
 *
 * <p>Las tres pruebas comprueban una lógica temporal <strong>sin esperar ni un milisegundo</strong>:
 * el tiempo es un dato que la prueba controla, no algo que ocurra. Antes de la refactorización
 * habría hecho falta un {@code Thread.sleep} con el umbral real — lento, y además inestable,
 * porque el planificador del sistema no garantiza cuándo despierta.
 */
class DetectorDeCaidasTest {

    private static final Servicio WEB = Servicio.desde("https://www.uib.es");
    private static final Instant T0 = Instant.parse("2026-09-12T10:00:00Z");
    private static final Duration UMBRAL = Duration.ofMinutes(5);

    /**
     * Reloj de prueba que la prueba adelanta a voluntad.
     *
     * <p>{@code Clock.fixed} por sí solo no basta aquí: devuelve siempre el mismo instante, y
     * entonces el tiempo transcurrido entre el primer fallo y la comprobación siguiente sería
     * siempre cero. Lo que hace falta es un reloj <em>controlable</em>, no uno parado. Son
     * quince líneas, y es la forma estándar de probar lógica temporal.
     */
    private static final class RelojAjustable extends Clock {

        private Instant ahora;

        RelojAjustable(Instant inicio) {
            this.ahora = inicio;
        }

        void avanzar(Duration cuanto) {
            ahora = ahora.plus(cuanto);
        }

        @Override public Instant instant()          { return ahora; }
        @Override public ZoneId getZone()           { return ZoneOffset.UTC; }
        @Override public Clock withZone(ZoneId z)   { return this; }
    }

    @Test
    void unServicioQueRespondeNoSeMarcaComoCaido() {
        var reloj = new RelojAjustable(T0);
        var sonda = new SondaProgramada(Map.of(WEB, List.of(Estado.ACTIVO)));
        var detector = new DetectorDeCaidas(sonda, reloj, UMBRAL);

        assertFalse(detector.estaCaido(WEB));

        // Aunque pase muchísimo tiempo: si responde, no está caído. El tiempo solo cuenta
        // desde el primer fallo.
        reloj.avanzar(Duration.ofHours(3));
        assertFalse(detector.estaCaido(WEB));
    }

    @Test
    void caidoMenosTiempoQueElUmbralTodaviaNoSeMarca() {
        var reloj = new RelojAjustable(T0);
        var sonda = new SondaProgramada(Map.of(WEB, List.of(Estado.CAIDO)));  // y repite CAIDO
        var detector = new DetectorDeCaidas(sonda, reloj, UMBRAL);

        assertFalse(detector.estaCaido(WEB));                 // primer fallo: arranca la cuenta

        // VALOR LÍMITE: un milisegundo por debajo del umbral.
        reloj.avanzar(UMBRAL.minusMillis(1));
        assertFalse(detector.estaCaido(WEB));
    }

    @Test
    void caidoExactamenteElUmbralSiSeMarca() {
        var reloj = new RelojAjustable(T0);
        var sonda = new SondaProgramada(Map.of(WEB, List.of(Estado.CAIDO)));
        var detector = new DetectorDeCaidas(sonda, reloj, UMBRAL);

        assertFalse(detector.estaCaido(WEB));

        // VALOR LÍMITE: justo el umbral. Junto con la prueba anterior, fija la frontera con
        // precisión de un milisegundo. Si alguien cambiara el '>=' por un '>', esta fallaría.
        reloj.avanzar(UMBRAL);
        assertTrue(detector.estaCaido(WEB));
    }

    @Test
    void recuperarseReiniciaLaCuenta() {
        var reloj = new RelojAjustable(T0);
        // Guion: falla, se recupera, vuelve a fallar.
        var sonda = new SondaProgramada(Map.of(WEB,
                List.of(Estado.CAIDO, Estado.ACTIVO, Estado.CAIDO)));
        var detector = new DetectorDeCaidas(sonda, reloj, UMBRAL);

        detector.estaCaido(WEB);                       // arranca la cuenta en T0
        reloj.avanzar(Duration.ofMinutes(4));
        assertFalse(detector.estaCaido(WEB));           // responde: la cuenta se borra

        reloj.avanzar(Duration.ofMinutes(4));
        // Vuelve a fallar. Si la cuenta NO se hubiera reiniciado, llevaría 8 minutos "caído"
        // y esto sería true. Debe ser false: la caída empieza ahora.
        assertFalse(detector.estaCaido(WEB));
    }
}
