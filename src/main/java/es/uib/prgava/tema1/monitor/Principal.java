// Principal.java (con observadores)
package es.uib.prgava.tema1.monitor;

import java.time.Duration;
import java.util.List;

/**
 * El cableado del programa, con los cambios que piden los ejercicios 1.2.2, 1.2.6 y 1.2.7.
 *
 * <p>Los tres se resuelven <strong>aquí</strong> y en una clase nueva, sin tocar
 * {@code Monitor}. Esta clase es el único sitio del programa que menciona clases concretas, y por eso
 * es el único que hay que cambiar cuando se cambia una decisión. Ese papel tiene nombre: es el
 * cableado, la composición de la aplicación.
 *
 * <p>El ejercicio 1.3.4 pide sustituir también la política por una lambda, y eso se ve en
 * {@code DemoPoliticasLambda}, en el paquete de ejercicios: no puede estar aquí, porque el paquete
 * del monitor no debe depender del de ejercicios cuando ese ya depende de este.
 */
public final class Principal {
    public static void main(String[] args) {
        List<Servicio> servicios = List.of(
                Servicio.desde("https://www.uib.es"),
                Servicio.desde("dns:uib.es"),
                Servicio.desde("tcp:mail.uib.es:25"),
                Servicio.desde("ping:uib.es"));          // 1.2.7: el tipo nuevo, en una línea

        var monitor = new Monitor(
                new SondaSimulada(42L, 0.25, Duration.ofMillis(300)),
                // 1.2.2: la política nueva sustituye a FallosConsecutivos(2). Monitor no cambia.
                new FallosEnVentana(2, 5),
                new RepositorioEnMemoria());

        var registro = new NotificadorRegistro();

        // 1.2.6: el filtro envuelve al notificador de consola. El orden importa: así el filtro
        // decide antes de imprimir. Envueltos al revés —consola por fuera del filtro— imprimiría
        // todo y filtraría después, que no es lo que queremos.
        monitor.suscribir(new NotificadorSinRepetir(new NotificadorConsola()));
        monitor.suscribir(registro);                      // el registro sí lo quiere todo

        for (int ronda = 0; ronda < 20; ronda++) {
            monitor.comprobar(servicios);
        }
        System.out.println("Alertas emitidas: " + registro.alertas().size());
        // Cambia FallosEnVentana(2, 5) por FallosConsecutivos(2) y vuelve a ejecutar: la cuenta
        // cambia, y Monitor sigue sin tocarse. Ese es el ejercicio.
    }
}
