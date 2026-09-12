package es.uib.prgava.tema1.monitor;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Ejercicio 1.4.4. <strong>Solución comentada.</strong> Pruebas de {@link ConfiguracionMonitor}.
 */
class ConfiguracionMonitorTest {

    private static final Servicio WEB = Servicio.desde("https://www.uib.es");

    /**
     * {@code assertAll} ejecuta <strong>todas</strong> las aserciones aunque alguna falle, y
     * después informa de todas las que fallaron a la vez. Con cuatro {@code assertEquals}
     * seguidos, la primera que fallara abortaría el método y ocultaría las otras tres: habría
     * que arreglar, volver a ejecutar, descubrir la siguiente, y así. Aquí interesa ver de una
     * vez el cuadro completo de los valores por defecto.
     */
    @Test
    void elBuilderAplicaLosCuatroValoresPorDefecto() {
        var configuracion = ConfiguracionMonitor.builder().servicio(WEB).build();

        assertAll(
                () -> assertEquals(Duration.ofSeconds(30), configuracion.intervalo()),
                () -> assertEquals(Duration.ofSeconds(2), configuracion.tiempoDeEspera()),
                () -> assertEquals(1, configuracion.reintentos()),
                () -> assertEquals(Duration.ofMillis(300), configuracion.umbralLatencia()));
    }

    @Test
    void construirSinServiciosEsUnError() {
        var error = assertThrows(IllegalArgumentException.class,
                () -> ConfiguracionMonitor.builder().build());

        // Comprobamos también el MENSAJE, no solo el tipo: una IllegalArgumentException puede
        // venir de cualquiera de las cuatro validaciones, y sin mirar el mensaje esta prueba
        // pasaría aunque el fallo detectado fuera otro.
        assertEquals("Hace falta al menos un servicio", error.getMessage());
    }

    /**
     * {@code @ValueSource} es la forma más simple de prueba parametrizada: un único parámetro
     * por ejecución. Dos ejecuciones, dos pruebas en el informe.
     */
    @ParameterizedTest
    @ValueSource(ints = {-1, -10})
    void losReintentosNegativosSeRechazan(int reintentos) {
        assertThrows(IllegalArgumentException.class,
                () -> ConfiguracionMonitor.builder().servicio(WEB).reintentos(reintentos).build());
    }

    @Test
    void laListaDeServiciosDevueltaEsInmutable() {
        var configuracion = ConfiguracionMonitor.builder().servicio(WEB).build();

        // El constructor compacto hace 'servicios = List.copyOf(servicios)'. Sin esa línea, el
        // record guardaría la lista mutable del builder y cualquiera podría añadir servicios
        // después de construirlo, saltándose la validación. Esta prueba defiende esa línea.
        assertThrows(UnsupportedOperationException.class,
                () -> configuracion.servicios().add(Servicio.desde("dns:uib.es")));
    }

    @Test
    void elBuilderAcumulaLosServiciosIndicados() {
        var configuracion = ConfiguracionMonitor.builder()
                .servicio(WEB)
                .servicios(List.of(Servicio.desde("dns:uib.es")))
                .build();

        assertEquals(2, configuracion.servicios().size());
    }
}
