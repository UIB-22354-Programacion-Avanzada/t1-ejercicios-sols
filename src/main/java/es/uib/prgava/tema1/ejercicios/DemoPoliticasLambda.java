package es.uib.prgava.tema1.ejercicios;

import java.time.Duration;
import java.util.List;

import es.uib.prgava.tema1.monitor.Monitor;
import es.uib.prgava.tema1.monitor.PoliticaDeAlerta;
import es.uib.prgava.tema1.monitor.NotificadorRegistro;
import es.uib.prgava.tema1.monitor.RepositorioEnMemoria;
import es.uib.prgava.tema1.monitor.Servicio;
import es.uib.prgava.tema1.monitor.SondaSimulada;

/**
 * Ejercicio 1.3.4, paso 3. La misma ejecución de {@code Principal} con una política escrita como
 * lambda, para comprobar lo que dice el enunciado: al {@code Monitor} le da exactamente lo mismo de
 * dónde venga la política.
 */
public final class DemoPoliticasLambda {

    private DemoPoliticasLambda() {
    }

    public static void main(String[] args) {
        List<Servicio> servicios = List.of(
                Servicio.desde("https://www.uib.es"),
                Servicio.desde("dns:uib.es"));

        var politicas = new java.util.LinkedHashMap<String, PoliticaDeAlerta>();
        politicas.put("SIEMPRE", Criterios.SIEMPRE);
        politicas.put("NUNCA", Criterios.NUNCA);
        politicas.put("ULTIMO_FALLO", Criterios.ULTIMO_FALLO);
        politicas.put("alMenos(3)", Criterios.alMenos(3));

        for (var entrada : politicas.entrySet()) {
            var politica = entrada.getValue();

            var registro = new NotificadorRegistro();
            var monitor = new Monitor(
                    new SondaSimulada(42L, 0.25, Duration.ofMillis(300)),
                    politica,                          // una lambda donde antes iba una clase
                    new RepositorioEnMemoria());
            monitor.suscribir(registro);

            for (int ronda = 0; ronda < 10; ronda++) {
                monitor.comprobar(servicios);
            }
            System.out.printf("%-14s -> %d alertas%n", entrada.getKey(), registro.alertas().size());
        }
    }
}
