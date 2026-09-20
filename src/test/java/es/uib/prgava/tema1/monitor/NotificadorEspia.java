// NotificadorEspia.java  (en src/test/java)
package es.uib.prgava.tema1.monitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Ejercicio 1.4.3. El otro doble: un notificador que en lugar de imprimir <strong>guarda</strong>,
 * para que la prueba pueda examinar lo que recibió.
 *
 * <p>Y aquí está la respuesta al «Para pensar» del enunciado —si esto es una implementación de
 * {@code Notificador} de pleno derecho o una trampa para probar—: es de pleno derecho, y la prueba
 * de que lo es está en el propio monitor. {@link NotificadorRegistro}, en {@code src/main/java}, hace
 * exactamente esto, y no se escribió para probar nada: se escribió porque los informes necesitan
 * saber qué alertas se emitieron.
 *
 * <p>Esa coincidencia dice algo sobre el diseño. Cuando un doble de prueba resulta ser una
 * implementación razonable del sistema real, es señal de que la interfaz está bien planteada: pide lo
 * justo. Los dobles que hay que retorcer —los que tienen que fingir estado interno, o adivinar en qué
 * orden se les va a llamar— suelen estar denunciando una interfaz que pide demasiado.
 *
 * <p>La diferencia con {@code NotificadorRegistro} es lo que añade para la prueba: contar por
 * servicio, que es lo que interesa comprobar y no tendría sentido en el programa.
 */
final class NotificadorEspia implements Notificador {

    private final List<Alerta> recibidas = new ArrayList<>();

    @Override
    public void notificar(Alerta alerta) {
        recibidas.add(alerta);
    }

    List<Alerta> recibidas() {
        return List.copyOf(recibidas);
    }

    int cuantas() {
        return recibidas.size();
    }

    int cuantasDe(Servicio servicio) {
        int cuantas = 0;
        for (var alerta : recibidas) {
            if (alerta.servicio().equals(servicio)) {
                cuantas++;
            }
        }
        return cuantas;
    }
}
