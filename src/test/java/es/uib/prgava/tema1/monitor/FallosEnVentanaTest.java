package es.uib.prgava.tema1.monitor;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ejercicio 1.4.1. <strong>Solución comentada.</strong> Pruebas de {@link FallosEnVentana}.
 *
 * <p>Los casos no están elegidos al azar: son las <strong>particiones de equivalencia</strong> del
 * problema —los grupos de entradas que el programa trata igual— más los <strong>valores
 * límite</strong> de cada partición, que es donde se concentran los errores de programación
 * (el clásico «uno de más», {@code >} donde debía ir {@code >=}).
 *
 * <p>Una prueba por caso, y no un método largo con veinte aserciones: cuando algo se rompe,
 * el nombre del método que falla dice ya qué comportamiento se ha perdido, sin abrir el código.
 * Por eso los nombres son frases en vez de {@code test1}, {@code test2}.
 */
class FallosEnVentanaTest {

    private static final Servicio WEB = Servicio.desde("https://www.uib.es");
    private static final Resultado OK = Resultado.activo(WEB, Duration.ofMillis(100));
    private static final Resultado FALLO = Resultado.caido(WEB);

    private FallosEnVentana politica;

    /**
     * {@code @BeforeEach} se ejecuta antes de <strong>cada</strong> prueba, no una vez para
     * todas. Esa es la garantía de aislamiento: ninguna prueba hereda el estado que dejó otra,
     * y por tanto el resultado no depende del orden de ejecución.
     */
    @BeforeEach
    void prepararPolitica() {
        politica = new FallosEnVentana(2, 5);      // 2 fallos en las últimas 5 comprobaciones
    }

    @Test
    void unHistorialVacioNuncaAlerta() {
        // Caso degenerado. Es el que más veces se olvida y el que más veces peta en producción,
        // porque al arrancar el sistema TODOS los historiales están vacíos.
        assertFalse(politica.debeAlertar(List.of()));
    }

    @Test
    void unHistorialMasCortoQueLaVentanaSinFallosSuficientesNoAlerta() {
        // La ventana pide 5 y solo hay 3: hay que mirar lo que haya, sin salirse del índice.
        assertFalse(politica.debeAlertar(List.of(OK, FALLO, OK)));
    }

    @Test
    void exactamenteElMinimoDeFallosAlerta() {
        // VALOR LÍMITE: justo el mínimo. Si la comparación fuera '>' en vez de '>=',
        // esta prueba fallaría y la siguiente pasaría. Por eso hacen falta las dos.
        assertTrue(politica.debeAlertar(List.of(OK, OK, OK, FALLO, FALLO)));
    }

    @Test
    void unFalloMenosDelMinimoNoAlerta() {
        // VALOR LÍMITE: el otro lado de la frontera.
        assertFalse(politica.debeAlertar(List.of(OK, OK, OK, OK, FALLO)));
    }

    @Test
    void losFallosNoTienenPorQueSerConsecutivos() {
        // Esta es la diferencia con FallosConsecutivos, o sea, la razón de existir de la clase.
        // Si esta prueba desaparece, nada impide que alguien "simplifique" la implementación
        // volviéndola equivalente a la otra política sin que se note.
        assertTrue(politica.debeAlertar(List.of(FALLO, OK, OK, OK, FALLO)));
    }

    @Test
    void losFallosQueQuedanFueraDeLaVentanaNoCuentan() {
        // Seis comprobaciones: el primer fallo ya no está en la ventana de 5, así que solo
        // queda uno dentro. Aquí es donde se comprueba que la ventana se DESLIZA de verdad.
        assertFalse(politica.debeAlertar(List.of(FALLO, OK, OK, OK, OK, OK)));
    }

    @Test
    void elTextoDescribeLaCondicion() {
        // Monitor usa toString para componer el mensaje de la alerta: si cambia, las alertas
        // se vuelven ilegibles, y ninguna otra prueba se enteraría.
        assertTrue(politica.toString().contains("2"));
        assertTrue(politica.toString().contains("5"));
    }

    /**
     * Un {@code @ParameterizedTest} ejecuta el mismo método una vez por cada fila de datos, y
     * cada fila cuenta como una prueba independiente en el informe. Evita copiar cinco veces el
     * mismo cuerpo cambiando dos números, que es donde se cuelan las erratas.
     *
     * <p>Las cuatro filas cubren: mínimo cero, mínimo negativo, mínimo mayor que la ventana
     * (por uno, el valor límite) y ventana cero.
     */
    @ParameterizedTest
    @CsvSource({"0, 5", "-1, 5", "6, 5", "1, 0"})
    void losConstructoresInvalidosSeRechazan(int minimoFallos, int tamanoVentana) {
        assertThrows(IllegalArgumentException.class,
                () -> new FallosEnVentana(minimoFallos, tamanoVentana));
    }
}
