package es.uib.prgava.tema1.ejercicios;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import es.uib.prgava.tema1.monitor.Resultado;
import es.uib.prgava.tema1.monitor.Servicio;
import es.uib.prgava.tema1.monitor.Sonda;

/**
 * Ejercicio 1.4.5. <strong>Solución comentada.</strong> Versión refactorizada para poder probarla.
 *
 * <p>La versión original tenía dos dependencias ocultas, y las dos son casos clásicos:
 *
 * <ul>
 *   <li><strong>El reloj.</strong> {@code Instant.now()} lee el reloj del sistema, que no se puede
 *       controlar desde una prueba. Para comprobar «lleva caído más de cinco minutos» habría que
 *       esperar cinco minutos de verdad, o manipular la hora del ordenador. Por eso Java ofrece
 *       {@link Clock} como abstracción: {@code Clock.systemUTC()} en producción y
 *       {@code Clock.fixed(...)} en las pruebas.</li>
 *   <li><strong>La sonda.</strong> {@code new SondaSimulada(System.nanoTime(), ...)} se fabricaba
 *       una sonda con semilla aleatoria: cada ejecución daba un resultado distinto. Una prueba
 *       sobre eso no puede afirmar nada.</li>
 * </ul>
 *
 * <p>La refactorización consiste exactamente en subir esas dos decisiones al constructor. El
 * comportamiento no cambia; lo que cambia es quién elige las piezas. Es la misma operación del
 * ejercicio 1.2.5, aplicada a dos dependencias que no parecen dependencias — y por eso este
 * ejercicio va al final: {@code Instant.now()} y {@code new} se cuelan en el código sin que nadie
 * los perciba como un acoplamiento.
 *
 * <p>Con las dependencias fuera, {@code DetectorDeCaidasTest} comprueba los valores límite
 * —justo por debajo y justo en el umbral— en milisegundos y de forma completamente determinista.
 */
public final class DetectorDeCaidas {

    private final Sonda sonda;
    private final Clock reloj;
    private final Duration umbral;
    private final Map<Servicio, Instant> primerFallo = new HashMap<>();

    /**
     * Constructor principal: recibe todo lo que necesita. No hay ningún {@code new} de
     * colaboradores ni ninguna llamada estática al entorno dentro de la clase.
     */
    public DetectorDeCaidas(Sonda sonda, Clock reloj, Duration umbral) {
        this.sonda = Objects.requireNonNull(sonda, "La sonda no puede ser nula");
        this.reloj = Objects.requireNonNull(reloj, "El reloj no puede ser nulo");
        this.umbral = Objects.requireNonNull(umbral, "El umbral no puede ser nulo");
    }

    /**
     * Constructor de conveniencia para producción: reloj del sistema.
     *
     * <p>Fíjate en que la comodidad se ofrece <em>además</em>, sin quitar la posibilidad de
     * inyectar. Es el equilibrio razonable: quien no necesita controlar el reloj no tiene que
     * pensar en él, y quien sí lo necesita —las pruebas— puede.
     */
    public DetectorDeCaidas(Sonda sonda, Duration umbral) {
        this(sonda, Clock.systemUTC(), umbral);
    }

    /**
     * Comprueba el servicio y devuelve {@code true} si lleva caído al menos el umbral.
     *
     * <p>Un servicio que responde deja de estar en seguimiento.
     */
    public boolean estaCaido(Servicio servicio) {
        Resultado resultado = sonda.sondear(servicio);

        if (!resultado.esFallo()) {
            primerFallo.remove(servicio);      // se recuperó: la cuenta vuelve a empezar
            return false;
        }

        // Instant.now(reloj), no Instant.now(): es el cambio de una palabra que hace la clase
        // comprobable. Con un Clock.fixed, esta llamada devuelve siempre el mismo instante.
        Instant ahora = Instant.now(reloj);

        // putIfAbsent: el primer fallo fija el inicio de la caída; los siguientes no lo tocan.
        primerFallo.putIfAbsent(servicio, ahora);

        Duration caido = Duration.between(primerFallo.get(servicio), ahora);
        // >= 0 y no > 0: "al menos el umbral" incluye el umbral exacto. Ese borde es uno de los
        // tres casos que pide probar el enunciado, y el sitio donde se cuelan los errores de
        // uno en uno.
        return caido.compareTo(umbral) >= 0;
    }
}
