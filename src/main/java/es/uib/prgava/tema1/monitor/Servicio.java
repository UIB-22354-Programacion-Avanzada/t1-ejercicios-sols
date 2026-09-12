// Servicio.java (con fábrica estática)
package es.uib.prgava.tema1.monitor;

import java.net.URI;

public sealed interface Servicio permits ServicioHttp, ServicioDns, PuertoTcp, ServicioPing {

    String nombre();

    /**
     * Crea un servicio a partir de su forma textual:
     * {@code http://...}, {@code https://...}, {@code dns:dominio}, {@code tcp:host:puerto}
     * o {@code ping:host} (ejercicio 1.1.5).
     * @throws IllegalArgumentException si el texto no describe un servicio válido
     */
    static Servicio desde(String texto) {
        var t = texto.strip();
        if (t.startsWith("http://") || t.startsWith("https://")) {
            return new ServicioHttp(URI.create(t));
        }
        if (t.startsWith("dns:")) {
            return new ServicioDns(t.substring(4));
        }
        // Ejercicio 1.1.5: la fábrica también hay que ampliarla. No lo hace el compilador
        // por ti —aquí no hay switch exhaustivo—, así que es el punto que más se olvida.
        if (t.startsWith("ping:")) {
            return new ServicioPing(t.substring(5));
        }
        if (t.startsWith("tcp:")) {
            var partes = t.substring(4).split(":");
            if (partes.length != 2) {
                throw new IllegalArgumentException("se esperaba tcp:host:puerto, no " + t);
            }
            return new PuertoTcp(partes[0], Integer.parseInt(partes[1]));
        }
        throw new IllegalArgumentException("tipo de servicio desconocido: " + t);
    }
}
