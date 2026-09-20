package es.uib.prgava.tema1.monitor;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * Ejercicio 1.2.8. <strong>Solución comentada.</strong> Template Method visto desde el lado de
 * quien extiende.
 *
 * <p>Toda la clase son diez líneas útiles, y eso es el patrón: {@link SondaBase} ya tiene escrito el
 * algoritmo —medir, y según haya medida o no, clasificar por umbral o declarar el servicio caído— y
 * lo único que falta es el paso que cambia. No hay que acordarse del orden de los pasos ni de
 * devolver {@code Resultado.caido} cuando no hay respuesta: eso está en la superclase, escrito una
 * vez, y no se puede equivocar.
 *
 * <p>Compárala con {@link SondaSimulada}, que usa números aleatorios: esa es inútil para probar,
 * porque una prueba no puede afirmar nada sobre un resultado que cambia en cada ejecución. Es el
 * mismo argumento del {@code SensorTemperatura} de 1.1.6 y del reloj de 1.4.4, y es el argumento
 * central de toda la sección 1.4.
 */
public final class SondaDeterminista extends SondaBase {

    private final List<Duration> latencias;
    private int llamadas = 0;

    /**
     * @param umbral se lo pasa a {@code super}: la clasificación por umbral es trabajo de la
     *        superclase, no de esta clase
     * @param latencias las que devolverá {@code medir}, una por llamada y en orden
     */
    public SondaDeterminista(Duration umbral, List<Duration> latencias) {
        super(umbral);
        this.latencias = List.copyOf(latencias);
    }

    /**
     * El único paso variable. {@code Optional<Duration>} es lo que hace que «no responde» sea un
     * caso del tipo de retorno y no un número inventado ni un {@code null}: quien lo recibe —el
     * {@code sondear} de la superclase— no puede llegar al valor sin haber pasado por la
     * comprobación de vacío.
     *
     * <p>Al agotarse la lista se devuelve vacío, lo que da un final predecible: la prueba puede
     * comprobar que la sonda acaba declarando el servicio caído.
     */
    @Override
    protected Optional<Duration> medir(Servicio servicio) {
        if (llamadas >= latencias.size()) {
            return Optional.empty();
        }
        return Optional.of(latencias.get(llamadas++));
    }

    // ----------------------------------------------------------------------------------
    // RESPUESTA al «Para pensar»: qué dice la pareja `sondear` final + `medir` protected abstract.
    //
    // Dice exactamente dónde se puede tocar y dónde no. `final` en sondear es una prohibición: el
    // algoritmo no se redefine, porque si cada sonda pudiera reordenar los pasos no habría esqueleto
    // que compartir y la superclase no serviría de nada. `abstract` en medir es una obligación: hay
    // un hueco y hay que rellenarlo. Y `protected` es el alcance: medir existe para las subclases,
    // no para quien usa la sonda, que solo debe ver sondear().
    //
    // Los tres modificadores juntos son un contrato escrito en el lenguaje y no en un comentario.
    // Comparado con `clasificar`, que es protected pero NO abstract ni final: ese es un gancho, con
    // una implementación por defecto que una subclase puede afinar si quiere. Tres niveles de
    // apertura, cada uno dicho con una palabra.
    // ----------------------------------------------------------------------------------
}
