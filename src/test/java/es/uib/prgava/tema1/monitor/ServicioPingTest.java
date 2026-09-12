package es.uib.prgava.tema1.monitor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ejercicio 1.1.5. <strong>Solución comentada.</strong> Estas pruebas venían enteras comentadas
 * en el esqueleto, porque no compilan hasta que existe {@code ServicioPing} y está en la cláusula
 * {@code permits} de {@link Servicio}. Descomentarlas es el último paso del ejercicio.
 *
 * <p>Que el código de prueba <em>no compile</em> es, en este caso, la lección: el compilador
 * marca todos los sitios que hay que tocar al ampliar una jerarquía sellada.
 */
class ServicioPingTest {

    @Test
    void desdeReconoceLaFormaPing() {
        Servicio servicio = Servicio.desde("ping:www.uib.es");

        assertEquals("ping:www.uib.es", servicio.nombre());
        assertInstanceOf(ServicioPing.class, servicio);
    }

    @Test
    void describirContemplaElNuevoTipo() {
        String texto = Descripciones.describir(Servicio.desde("ping:www.uib.es"));

        assertTrue(texto.contains("www.uib.es"));
    }

    @Test
    void unHostVacioSeRechaza() {
        assertThrows(IllegalArgumentException.class, () -> Servicio.desde("ping:   "));
    }

    @Test
    void elHostSeNormaliza() {
        // Igual que ServicioDns: mayúsculas y espacios no deben crear servicios distintos.
        assertEquals(Servicio.desde("ping:www.uib.es"), Servicio.desde("ping:WWW.UIB.ES"));
    }
}
