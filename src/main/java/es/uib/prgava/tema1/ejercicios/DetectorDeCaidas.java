package es.uib.prgava.tema1.ejercicios;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import es.uib.prgava.tema1.monitor.Resultado;

/**
 * Ejercicio 1.4.4. <strong>Solución comentada.</strong> La misma clase, con el reloj fuera.
 *
 * <p>La versión de partida llamaba a {@code Instant.now()} por dentro, en dos métodos. Eso la hacía
 * imposible de probar: el resultado dependía del momento exacto de la ejecución, y una prueba que a
 * veces pasa y a veces no es peor que no tenerla, porque enseña al equipo a ignorar los fallos.
 *
 * <p>El arreglo es de una línea por método: <strong>el instante actual entra como parámetro</strong>.
 * Ni un reloj inyectado por constructor, ni una biblioteca de tiempo falso; el enunciado avisaba de
 * que basta con eso, y es cierto casi siempre. La clase deja de preguntar la hora y pasa a recibirla,
 * que es el mismo movimiento del ejercicio 1.2.5 con el repositorio y la salida.
 *
 * <p>Quien la use en el programa de verdad escribe {@code detector.fallosRecientes(historial,
 * Instant.now())}, y el {@code Instant.now()} queda en el cableado, que es su sitio: un solo lugar,
 * visible, y que nadie intenta probar.
 */
public class DetectorDeCaidas {

    private final Duration ventana;

    public DetectorDeCaidas(Duration ventana) {
        this.ventana = ventana;
    }

    /**
     * Fallos ocurridos dentro de la ventana que termina en {@code ahora}.
     *
     * @param ahora el instante que hace de «ahora»; en el programa, {@code Instant.now()}; en una
     *        prueba, un instante fijo elegido por ella
     */
    public int fallosRecientes(List<Resultado> historial, Instant ahora) {
        var desde = ahora.minus(ventana);
        int fallos = 0;
        for (var resultado : historial) {
            if (resultado.esFallo() && resultado.instante().isAfter(desde)) {
                fallos++;
            }
        }
        return fallos;
    }

    /** ¿Ha pasado ya la ventana desde el último aviso, visto desde {@code ahora}? */
    public boolean tocaAvisar(Instant ultimoAviso, Instant ahora) {
        return Duration.between(ultimoAviso, ahora).compareTo(ventana) >= 0;
    }

    // ----------------------------------------------------------------------------------
    // RESPUESTA al «Para pensar»: qué principio de 1.2 estaba violando esta clase.
    //
    // El D, la inversión de dependencias, y con el mismo síntoma que InformeDiarioAcoplado en 1.2.5:
    // se conseguía por dentro algo que debía recibir de fuera. `Instant.now()` es una llamada a un
    // colaborador —el reloj del sistema— igual que `new RepositorioEnMemoria()` era un colaborador; la
    // única diferencia es que el reloj no parece un colaborador porque no se escribe con `new`.
    //
    // De ahí la regla que cierra la sección 1.4: el tiempo y el azar son dependencias. Cuando una
    // clase es difícil de probar, casi nunca es un problema de las pruebas; es que la clase está
    // decidiendo cosas que no le tocan.
    // ----------------------------------------------------------------------------------
}
