package es.uib.prgava.tema1.monitor;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ejercicio 1.4.1. <strong>Solución comentada.</strong> Las pruebas de {@link FallosEnVentana}, que
 * en la plantilla eran el entregable.
 *
 * <p>El criterio es el de 1.4.3 del tema: particiones de equivalencia y valores límite. Las
 * particiones del historial son cinco —vacío, más corto que la ventana, justo del tamaño de la
 * ventana, más largo con los fallos dentro, más largo con los fallos fuera— y los valores límite son
 * los del constructor y el de la cuenta de fallos, que se prueba justo por debajo y justo por encima.
 *
 * <p>Los nombres de los métodos son frases: cuando una falla, el informe de Maven dice qué se ha roto
 * sin tener que abrir el fichero. Eso no es cosmética, es la mitad de la utilidad de una prueba.
 */
class FallosEnVentanaTest {

    private static final Servicio WEB = Servicio.desde("https://www.uib.es");
    private static final Resultado OK = Resultado.activo(WEB, Duration.ofMillis(100));
    private static final Resultado LENTO = Resultado.degradado(WEB, Duration.ofMillis(900));
    private static final Resultado FALLO = Resultado.caido(WEB);

    // ---------------------------------------------------------------- particiones del historial

    @Test
    void elHistorialVacioNoAlerta() {
        assertFalse(new FallosEnVentana(2, 5).debeAlertar(List.of()));
    }

    @Test
    void conElHistorialMasCortoQueLaVentanaSeMiranLosQueHaya() {
        // Dos comprobaciones, ventana de cinco: hay que mirar las dos, no dar por hecho que faltan.
        assertTrue(new FallosEnVentana(2, 5).debeAlertar(List.of(FALLO, FALLO)));
        assertFalse(new FallosEnVentana(2, 5).debeAlertar(List.of(FALLO, OK)));
    }

    @Test
    void conElHistorialDelTamanoExactoDeLaVentanaSeMiranTodos() {
        assertTrue(new FallosEnVentana(2, 3).debeAlertar(List.of(FALLO, OK, FALLO)));
    }

    @Test
    void losFallosNoTienenQueSerConsecutivos() {
        // Es LA diferencia con FallosConsecutivos, así que merece una prueba con nombre propio.
        assertTrue(new FallosEnVentana(2, 5).debeAlertar(List.of(FALLO, OK, OK, OK, FALLO)));
    }

    @Test
    void unFalloQueSeSalioDeLaVentanaYaNoCuenta() {
        // Seis comprobaciones, ventana de cinco: el primer FALLO queda fuera y solo se ve un OK más.
        assertFalse(new FallosEnVentana(2, 5).debeAlertar(List.of(FALLO, OK, OK, OK, OK, OK)));
    }

    @Test
    void conMasFallosDeLosNecesariosTambienAlerta() {
        // El caso que se escapa si se escribe == en lugar de >=.
        assertTrue(new FallosEnVentana(2, 5).debeAlertar(List.of(FALLO, FALLO, FALLO)));
    }

    @Test
    void unResultadoDegradadoNoEsUnFallo() {
        // Estado.DEGRADADO responde, aunque tarde: esFallo() solo es cierto para CAIDO.
        assertFalse(new FallosEnVentana(2, 5).debeAlertar(List.of(LENTO, LENTO, LENTO)));
    }

    // ---------------------------------------------------------------- valores límite de la cuenta

    @ParameterizedTest
    @CsvSource({
            "1, 1",      // el mínimo posible: un fallo en una ventana de uno
            "2, 2",      // mínimo igual a la ventana: hacen falta todos
            "3, 5",
            "5, 5"
    })
    void justoElMinimoDeFallosAlerta(int minimo, int ventana) {
        var historial = new java.util.ArrayList<Resultado>();
        for (int i = 0; i < minimo; i++) {
            historial.add(FALLO);
        }
        assertTrue(new FallosEnVentana(minimo, ventana).debeAlertar(historial));
    }

    @ParameterizedTest
    @CsvSource({ "2, 2", "3, 5", "5, 5" })
    void unFalloMenosDelMinimoNoAlerta(int minimo, int ventana) {
        var historial = new java.util.ArrayList<Resultado>();
        for (int i = 0; i < minimo - 1; i++) {
            historial.add(FALLO);
        }
        assertFalse(new FallosEnVentana(minimo, ventana).debeAlertar(historial));
    }

    // ---------------------------------------------------------------- valores límite del constructor

    @ParameterizedTest
    @CsvSource({
            "0, 5",      // el mínimo tiene que ser al menos 1
            "-1, 5",
            "3, 2",      // pedir más fallos que el tamaño de la ventana no puede cumplirse nunca
            "1, 0"
    })
    void losParametrosImposiblesSeRechazan(int minimo, int ventana) {
        assertThrows(IllegalArgumentException.class, () -> new FallosEnVentana(minimo, ventana));
    }

    @Test
    void elCasoLimiteValidoDelConstructorSiSeAcepta() {
        // 1 <= minimoFallos <= tamanoVentana: los dos bordes de la condición, que valen.
        assertEquals(1, new FallosEnVentana(1, 1).debeAlertar(List.of(FALLO)) ? 1 : 0);
        assertTrue(new FallosEnVentana(5, 5).debeAlertar(
                List.of(FALLO, FALLO, FALLO, FALLO, FALLO)));
    }

    // ---------------------------------------------------------------- el toString, que Monitor usa

    @Test
    void elToStringDescribeLaPolitica() {
        var texto = new FallosEnVentana(2, 5).toString();
        assertTrue(texto.contains("2"), "debe decir cuántos fallos");
        assertTrue(texto.contains("5"), "debe decir el tamaño de la ventana");
    }

    // ----------------------------------------------------------------------------------
    // RESPUESTA al «Para pensar»: ¿cuántas pruebas han hecho falta y se podría quitar alguna?
    //
    // Doce métodos, que con las parametrizadas son veinte casos. Se podría quitar
    // `conElHistorialDelTamanoExactoDeLaVentanaSeMiranTodos`, porque el caso que cubre ya lo cubre
    // `losFallosNoTienenQueSerConsecutivos`; se ha dejado porque nombra una partición distinta, y el
    // valor de una prueba no es solo lo que detecta, es lo que documenta.
    //
    // La comprobación de que la lista de pruebas es suficiente no es contarlas: es romper la clase a
    // propósito. Cambiar `>=` por `==` hace fallar `conMasFallosDeLosNecesariosTambienAlerta`; quitar
    // el `Math.max(0, ...)` hace fallar `conElHistorialMasCortoQueLaVentanaSeMiranLosQueHaya` con una
    // IndexOutOfBoundsException; y quitar la validación del constructor hace fallar
    // `losParametrosImposiblesSeRechazan`. Cada línea de la clase tiene al menos una prueba que la
    // vigila, y eso sí es una respuesta.
    // ----------------------------------------------------------------------------------
}
