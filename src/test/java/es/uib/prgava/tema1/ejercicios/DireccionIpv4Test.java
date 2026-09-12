package es.uib.prgava.tema1.ejercicios;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Ejercicio 1.4.2. <strong>Solución comentada.</strong> Pruebas exhaustivas de
 * {@link DireccionIpv4}.
 *
 * <p>Las pruebas mínimas están en {@link DireccionIpv4BasicoTest} y no se repiten aquí: esta
 * clase añade los casos límite y las pruebas parametrizadas.
 */
class DireccionIpv4Test {

    /**
     * Cada texto inválido por un motivo distinto: pocas partes, demasiadas, parte no numérica,
     * partes vacías por puntos consecutivos, cadena vacía y solo espacios. Buscar un motivo
     * diferente para cada caso, en vez de seis variantes del mismo, es lo que hace útil una
     * batería de pruebas.
     */
    @ParameterizedTest
    @ValueSource(strings = {"1.2.3", "1.2.3.4.5", "a.b.c.d", "1.2..4", "", "   ", "192.168.1"})
    void losTextosInvalidosSeRechazan(String texto) {
        assertThrows(IllegalArgumentException.class, () -> DireccionIpv4.desde(texto));
    }

    /**
     * {@code @CsvSource} permite varios parámetros por fila. Las cuatro primeras filas son los
     * <strong>bordes del rango 172.16/12</strong>, que es el que concentra los errores: es fácil
     * escribir {@code segundo == 16} y perder el resto, o pasarse hasta 172.32.
     */
    @ParameterizedTest
    @CsvSource({
            "172.15.0.1,     false",     // justo por debajo del rango
            "172.16.0.1,     true",      // primer valor del rango
            "172.31.255.255, true",      // último valor del rango
            "172.32.0.1,     false",     // justo por encima
            "10.0.0.1,       true",      // 10/8: cualquier segundo octeto vale
            "10.255.255.255, true",
            "192.168.0.1,    true",      // 192.168/16
            "192.167.0.1,    false",     // 192.167 NO es privada
            "8.8.8.8,        false",
            "172.16.0.0,     true"
    })
    void reconoceLosRangosPrivadosIncluidosLosBordes(String texto, boolean esperado) {
        assertEquals(esperado, DireccionIpv4.desde(texto).esPrivada(),
                "fallo con la dirección " + texto);
    }

    /**
     * Las tres propiedades que un record da hechas, comprobadas juntas con {@code assertAll}
     * porque forman un solo concepto: «esto se comporta como un valor».
     */
    @Test
    void seComportaComoUnValor() {
        var una = new DireccionIpv4(192, 168, 1, 1);
        var otra = new DireccionIpv4(192, 168, 1, 1);
        var distinta = new DireccionIpv4(192, 168, 1, 2);

        assertAll(
                () -> assertEquals("192.168.1.1", una.toString()),
                () -> assertEquals(una, otra),
                () -> assertEquals(una.hashCode(), otra.hashCode()),
                () -> assertNotEquals(una, distinta),
                () -> assertEquals(192, una.primero()),
                () -> assertEquals(1, una.cuarto()));
    }

    /**
     * Valores límite de un octeto: 0 y 255 son válidos, −1 y 256 no. Los cuatro en una sola
     * prueba porque comprueban la misma regla desde sus cuatro esquinas.
     */
    @Test
    void losValoresLimiteDeUnOcteto() {
        assertAll(
                () -> assertEquals("0.0.0.0", new DireccionIpv4(0, 0, 0, 0).toString()),
                () -> assertEquals("255.255.255.255", new DireccionIpv4(255, 255, 255, 255).toString()),
                () -> assertThrows(IllegalArgumentException.class, () -> new DireccionIpv4(-1, 0, 0, 0)),
                () -> assertThrows(IllegalArgumentException.class, () -> new DireccionIpv4(0, 0, 0, 256)));
    }

    @Test
    void elMensajeDeErrorDiceQueOctetoFalla() {
        var error = assertThrows(IllegalArgumentException.class,
                () -> new DireccionIpv4(1, 300, 3, 4));

        // El valor concreto en el mensaje es lo que convierte un error en un diagnóstico.
        assertEquals("Octeto fuera de rango: 300", error.getMessage());
    }
}
