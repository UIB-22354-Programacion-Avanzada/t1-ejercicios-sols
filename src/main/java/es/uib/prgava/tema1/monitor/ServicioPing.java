package es.uib.prgava.tema1.monitor;

/**
 * Ejercicio 1.1.5. <strong>Solución comentada.</strong> Cuarto tipo de servicio: comprobación por
 * eco ICMP a un host.
 *
 * <p>Añadir esta clase obliga a tocar exactamente dos sitios más, y descubrir cuáles es el
 * objetivo del ejercicio:
 *
 * <ol>
 *   <li>la cláusula {@code permits} de {@link Servicio}, porque una interfaz sellada enumera sus
 *       implementaciones y esta clase no estaba en la lista;</li>
 *   <li><strong>todos</strong> los {@code switch} exhaustivos sobre {@code Servicio} —aquí,
 *       {@link Descripciones#describir}—, que dejan de compilar hasta contemplar el caso nuevo.</li>
 * </ol>
 *
 * <p>Y ahí está la ventaja del sellado frente a una interfaz abierta: el compilador te lleva de
 * la mano hasta cada punto del programa que hay que revisar. Con una interfaz normal, el
 * {@code switch} habría seguido compilando y habría caído en su rama {@code default}, es decir,
 * el fallo habría aparecido en ejecución y probablemente tarde.
 *
 * <p>El precio de esa ayuda es que los subtipos están cerrados: quien use la biblioteca no puede
 * añadir los suyos. Es el intercambio típico —extensibilidad por seguridad—, y para un catálogo
 * de tipos de servicio conocido y estable, compensa.
 */
public record ServicioPing(String host) implements Servicio {

    public ServicioPing {
        if (host == null || host.isBlank()) {
            throw new IllegalArgumentException("Host de ping vacío");
        }
        // Normalizamos igual que ServicioDns, para que "WWW.UIB.ES" y "www.uib.es " sean el
        // mismo servicio. Como el record genera equals sobre el campo ya normalizado, la
        // igualdad sale bien sin escribir nada más.
        host = host.strip().toLowerCase();
    }

    @Override
    public String nombre() {
        return "ping:" + host;
    }
}
