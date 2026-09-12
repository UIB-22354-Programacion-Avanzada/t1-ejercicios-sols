package es.uib.prgava.tema1.ejercicios;

import java.util.Objects;
import java.util.function.Consumer;

import es.uib.prgava.tema1.monitor.LectorResultados;
import es.uib.prgava.tema1.monitor.Servicio;

/**
 * Ejercicio 1.2.5. <strong>Solución comentada.</strong> Misma funcionalidad que
 * {@link InformeDiarioAcoplado}, con las dependencias invertidas.
 *
 * <p>Las dos dependencias que la versión acoplada tenía escondidas, y por qué importaban:
 *
 * <ul>
 *   <li>{@code new RepositorioEnMemoria()} dentro de la clase: el informe se fabricaba su propia
 *       fuente de datos, siempre vacía y siempre la misma. Era imposible darle datos de prueba.</li>
 *   <li>{@code System.out.println}: la salida iba a un sitio fijo. Para comprobar lo que
 *       imprimía había que capturar la salida estándar, que es frágil y ensucia las pruebas.</li>
 * </ul>
 *
 * <p>Al recibirlas por constructor, la prueba le pasa un repositorio con datos conocidos y un
 * {@code lineas::add} como salida, y comprueba el resultado leyendo una lista. Ninguna prueba
 * escribe en la consola — que es lo que dice el comentario de {@code InformeDiarioTest}.
 *
 * <p>Fíjate en los <strong>tipos</strong> elegidos, que es la otra mitad del ejercicio. El
 * parámetro es {@link LectorResultados} y no {@code RepositorioResultados}: este informe solo
 * lee, y pedir la interfaz más estrecha que basta es aplicar la segregación de interfaces del
 * ejercicio 1.2.4. Y la salida es un {@link Consumer}{@code <String>}, la abstracción más simple
 * de «algo a lo que mandar líneas»: vale una lista, un fichero, la consola o un socket.
 */
public final class InformeDiario {

    private final LectorResultados lector;
    private final Consumer<String> salida;

    public InformeDiario(LectorResultados lector, Consumer<String> salida) {
        this.lector = Objects.requireNonNull(lector, "El lector no puede ser nulo");
        this.salida = Objects.requireNonNull(salida, "La salida no puede ser nula");
    }

    /** Emite una línea de cabecera y una línea por servicio. */
    public void emitir() {
        // La cabecera se emite siempre, incluso sin datos: por eso la prueba
        // unRepositorioVacioNoRompeElInforme espera al menos una línea. Un informe vacío del
        // todo es indistinguible de un informe que no se ha ejecutado.
        salida.accept("=== Informe diario ===");

        for (Servicio servicio : lector.servicios()) {
            long comprobaciones = lector.historial(servicio).size();
            salida.accept(servicio.nombre() + ": " + comprobaciones + " comprobaciones");
        }
    }
}
