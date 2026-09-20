package es.uib.prgava.tema1.ejercicios;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Ejercicio 1.4.2. <strong>Solución comentada.</strong> Las pruebas parametrizadas de
 * {@link DireccionIpv4}.
 *
 * <p>Una prueba parametrizada es un método con {@code @ParameterizedTest} y una fuente de datos, y se
 * ejecuta una vez por cada caso. Frente a cinco métodos casi idénticos gana en que los casos se leen
 * en una tabla —se ve de un golpe qué está cubierto y qué falta, y añadir uno es añadir una línea— y
 * en que la lógica de la comprobación está escrita una sola vez.
 *
 * <p>Y la respuesta al «Para pensar» —qué pierde cuando falla un caso—: nada, si se mira el informe.
 * JUnit numera y nombra cada invocación, así que el informe dice exactamente qué caso falló. Lo que
 * sí se pierde es el <em>nombre</em>: cinco métodos con nombres distintos documentan cinco
 * situaciones, y una tabla documenta una sola situación con cinco datos. Por eso aquí hay varias
 * pruebas parametrizadas y no una sola con todo dentro: cada una nombra una cosa.
 *
 * <p>No se repiten los casos de {@code DireccionIpv4BasicoTest} ni de {@code DireccionIpv4DesdeTest},
 * como pedía el enunciado.
 */
class DireccionIpv4Test {

    // ---------------------------------------------------------------- los octetos, con @ValueSource

    @ParameterizedTest
    @ValueSource(ints = { -1000, -1, 256, 300, 1000 })
    void unOctetoFueraDeRangoEsRechazadoEnCualquieraDeLasCuatroPosiciones(int malo) {
        // El mismo valor inválido en las cuatro posiciones: si la validación se olvidara de una, esta
        // prueba lo dice. Es el error que se comete al escribir cuatro if a mano en lugar de un
        // método auxiliar, y la razón de que la solución extraiga validarOcteto.
        assertThrows(IllegalArgumentException.class, () -> new DireccionIpv4(malo, 0, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> new DireccionIpv4(0, malo, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> new DireccionIpv4(0, 0, malo, 0));
        assertThrows(IllegalArgumentException.class, () -> new DireccionIpv4(0, 0, 0, malo));
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 254, 255 })
    void losCuatroValoresLimiteDelRangoSonValidos(int bueno) {
        // Los límites van siempre en pareja: 0 y 255 valen (se prueban aquí), -1 y 256 no (arriba).
        // Probar solo un lado de un límite deja pasar el < escrito como <=.
        assertEquals(bueno, new DireccionIpv4(bueno, bueno, bueno, bueno).primero());
    }

    @Test
    void elMensajeDelErrorNombraElOctetoCulpableYNoLaDireccionEntera() {
        var error = assertThrows(IllegalArgumentException.class,
                () -> new DireccionIpv4(192, 168, 1, 300));
        assertEquals("Octeto fuera de rango: 300", error.getMessage());
    }

    // ---------------------------------------------------------------- desde y toString, con @CsvSource

    @ParameterizedTest
    @CsvSource({
            "0.0.0.0,                0,   0,   0,   0",
            "10.0.0.1,              10,   0,   0,   1",
            "192.168.1.1,          192, 168,   1,   1",
            "255.255.255.255,      255, 255, 255, 255",
            "  172.16.0.1  ,       172,  16,   0,   1"      // con espacios alrededor
    })
    void desdeConstruyeLaDireccionQueDiceElTexto(String texto, int a, int b, int c, int d) {
        assertEquals(new DireccionIpv4(a, b, c, d), DireccionIpv4.desde(texto));
    }

    @ParameterizedTest
    @CsvSource({
            "0.0.0.0",
            "10.0.0.1",
            "192.168.1.1",
            "255.255.255.255"
    })
    void desdeYToStringSonInversosElUnoDelOtro(String texto) {
        // La propiedad que de verdad importa, y que una tabla de parejas texto-resultado no dice:
        // que las dos conversiones encajen. Si una cambiara de formato, esta prueba lo detecta.
        assertEquals(texto, DireccionIpv4.desde(texto).toString());
    }

    @ParameterizedTest
    @CsvSource({
            "10.0.0",                // tres partes
            "10.0.0.1.1",            // cinco
            "10.0.0.x",              // una que no es un número
            "10..0.1",               // una vacía
            "''",                    // cadena vacía
            "'   '",                 // en blanco
            "10.0.0.256"             // formato bien, rango mal
    })
    void desdeRechazaLosTextosQueNoSonUnaDireccion(String texto) {
        assertThrows(IllegalArgumentException.class, () -> DireccionIpv4.desde(texto));
    }

    // ----------------------------------------------------------------------------------
    // NOTA sobre el último caso de la tabla anterior, "10.0.0.256": el formato es correcto y lo que
    // falla es el rango, y aun así la excepción es la misma. No es casualidad: desde() no vuelve a
    // comprobar el rango, se lo deja al constructor canónico, y el constructor lanza
    // IllegalArgumentException. Esa es la ventaja de tener un único punto de validación, y aquí se
    // ve en la prueba: un solo assertThrows cubre los dos motivos.
    //
    // Nótese también que NumberFormatException, que es lo que lanza Integer.parseInt, ES una
    // IllegalArgumentException. La prueba pasaría incluso sin el bloque try/catch de la solución; lo
    // que ese bloque añade es un mensaje que menciona el texto completo, no solo la parte que falló.
    // ----------------------------------------------------------------------------------
}
