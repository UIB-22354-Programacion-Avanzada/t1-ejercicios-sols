package es.uib.prgava.tema1.ejercicios;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ejercicio 1.2.4. Las pruebas usan cada clase por sus propios métodos, así que dividir la
 * interfaz no las rompe. Lo que tienen que dejar de hacer, cuando acabes, es llamar a un método
 * que la clase no debería haber tenido nunca.
 */
class GestorDispositivosTest {

    private static final List<FichaDispositivo> FICHAS = List.of(
            new FichaDispositivo("web01", "10.0.0.1"),
            new FichaDispositivo("dns01", "10.0.0.2"));

    @Test
    void elCatalogoEnMemoriaCumpleTodoLoQuePromete() {
        var catalogo = new CatalogoEnMemoria();
        catalogo.darDeAlta("web01", "10.0.0.1");
        catalogo.darDeAlta("dns01", "10.0.0.2");
        assertTrue(catalogo.existe("web01"));
        assertEquals(2, catalogo.cuantos());
        catalogo.renombrar("web01", "web02");
        assertTrue(catalogo.existe("web02"));
        catalogo.borrar("web02");
        assertFalse(catalogo.existe("web02"));
        assertEquals(1, catalogo.cuantos());
    }

    @Test
    void elInventarioDeConsultaConsulta() {
        var consulta = new ConsultaDispositivos(FICHAS);
        assertTrue(consulta.existe("dns01"));
        assertEquals(2, consulta.cuantos());
        assertEquals(List.of("web01", "dns01"), consulta.nombres());
    }

    @Test
    void elExportadorExporta() {
        var exportador = new ExportadorCsv(FICHAS);
        assertEquals("web01;10.0.0.1\ndns01;10.0.0.2\n", exportador.exportarCsv());
        assertEquals(2, exportador.cuantos());
    }

    /**
     * La prueba que el enunciado pedía añadir. Es la que demuestra que la división ha servido de
     * algo: un cliente escrito contra {@link ConsultaDeDispositivos} acepta las dos
     * implementaciones que consultan, y su firma no le da acceso a nada más.
     */
    @Test
    void unClienteQueSoloConsultaAceptaLasDosImplementacionesQueConsultan() {
        assertEquals(2, cuantosHay(new ConsultaDispositivos(FICHAS)));

        var catalogo = new CatalogoEnMemoria();
        catalogo.darDeAlta("web01", "10.0.0.1");
        catalogo.darDeAlta("dns01", "10.0.0.2");
        assertEquals(2, cuantosHay(catalogo));
    }

    /**
     * Un cliente cualquiera, escrito contra la interfaz más pequeña que le sirve. Dentro de este
     * método no se puede llamar a darDeAlta ni a exportarCsv: no están en el tipo, así que el
     * compilador lo impide. Antes de dividir la interfaz, la firma habría tenido que ser
     * GestorDispositivos y este método habría podido borrar el inventario que solo venía a contar.
     */
    private static int cuantosHay(ConsultaDeDispositivos inventario) {
        return inventario.cuantos();
    }
}
