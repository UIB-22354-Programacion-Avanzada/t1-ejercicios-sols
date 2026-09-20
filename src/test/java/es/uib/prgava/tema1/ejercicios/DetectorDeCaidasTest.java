package es.uib.prgava.tema1.ejercicios;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

import es.uib.prgava.tema1.monitor.Estado;
import es.uib.prgava.tema1.monitor.Resultado;
import es.uib.prgava.tema1.monitor.Servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ejercicio 1.4.4. <strong>Solución comentada.</strong> Las pruebas que la versión de partida no
 * permitía escribir.
 *
 * <p>Toda la prueba gira alrededor de {@code AHORA}, un instante fijo e inventado. Con el reloj
 * dentro de la clase esto era imposible: los resultados del historial había que fabricarlos
 * relativos al momento de la ejecución y la ventana se medía desde otro momento ligeramente
 * posterior, así que un caso justo en el borde pasaba o fallaba según lo rápido que fuera la máquina.
 *
 * <p>Con el instante como parámetro, la prueba controla las dos puntas del intervalo y los casos
 * límite se pueden escribir exactamente en el borde, que es donde hay que escribirlos.
 */
class DetectorDeCaidasTest {

    private static final Servicio WEB = Servicio.desde("https://www.uib.es");
    private static final Instant AHORA = Instant.parse("2026-03-15T10:00:00Z");
    private static final Duration VENTANA = Duration.ofMinutes(10);

    private final DetectorDeCaidas detector = new DetectorDeCaidas(VENTANA);

    /** Un resultado con instante elegido: lo que no se podía hacer con el reloj dentro. */
    private static Resultado fallo(Instant cuando) {
        return new Resultado(WEB, cuando, Estado.CAIDO, Duration.ZERO);
    }

    private static Resultado activo(Instant cuando) {
        return new Resultado(WEB, cuando, Estado.ACTIVO, Duration.ofMillis(100));
    }

    @Test
    void sinHistorialNoHayFallosRecientes() {
        assertEquals(0, detector.fallosRecientes(List.of(), AHORA));
    }

    @Test
    void cuentaSoloLosFallosDentroDeLaVentana() {
        var historial = List.of(
                fallo(AHORA.minus(Duration.ofMinutes(30))),   // fuera
                fallo(AHORA.minus(Duration.ofMinutes(5))),    // dentro
                activo(AHORA.minus(Duration.ofMinutes(2))),   // dentro, pero no es fallo
                fallo(AHORA.minus(Duration.ofMinutes(1))));   // dentro
        assertEquals(2, detector.fallosRecientes(historial, AHORA));
    }

    @Test
    void elBordeExactoDeLaVentanaQuedaFuera() {
        // isAfter es estricto, así que un fallo justo en el instante límite no cuenta. Es una
        // decisión, y lo que importa es que esté probada: así el día que alguien cambie isAfter por
        // !isBefore, la prueba lo dice.
        var enElBorde = fallo(AHORA.minus(VENTANA));
        assertEquals(0, detector.fallosRecientes(List.of(enElBorde), AHORA));

        var unMilisegundoDentro = fallo(AHORA.minus(VENTANA).plusMillis(1));
        assertEquals(1, detector.fallosRecientes(List.of(unMilisegundoDentro), AHORA));
    }

    @Test
    void elMismoHistorialDaElMismoResultadoDentroDeUnAno() {
        // La prueba que justifica el ejercicio: el resultado no depende de cuándo se ejecute.
        var historial = List.of(fallo(AHORA.minus(Duration.ofMinutes(5))));
        assertEquals(1, detector.fallosRecientes(historial, AHORA));
        assertEquals(1, detector.fallosRecientes(historial, AHORA));   // idéntico, siempre
    }

    @Test
    void tocaAvisarCuandoHaPasadoLaVentanaDesdeElUltimoAviso() {
        assertFalse(detector.tocaAvisar(AHORA.minus(Duration.ofMinutes(9)), AHORA));
        assertTrue(detector.tocaAvisar(AHORA.minus(Duration.ofMinutes(11)), AHORA));
    }

    @Test
    void enElBordeExactoDelIntervaloEntreAvisosSiTocaAvisar() {
        // compareTo(ventana) >= 0: el borde cuenta. Otra decisión, otra prueba que la fija.
        assertTrue(detector.tocaAvisar(AHORA.minus(VENTANA), AHORA));
    }
}
