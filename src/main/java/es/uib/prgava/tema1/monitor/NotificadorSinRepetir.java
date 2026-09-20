package es.uib.prgava.tema1.monitor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Ejercicio 1.2.6. <strong>Solución comentada.</strong> Un notificador que filtra lo repetido: del
 * mismo servicio, solo el primer aviso.
 *
 * <p>Fíjate en la forma de la clase: <strong>recibe</strong> un {@code Notificador} y además
 * <strong>es</strong> un {@code Notificador}. Eso es un Decorator, el patrón de 1.2.4 que el monitor
 * «no necesitaba»: añade comportamiento a un objeto sin modificar su clase, sin herencia, y de forma
 * que se pueda apilar. Se puede envolver un {@code NotificadorSinRepetir} en otro filtro y este en
 * otro, y el orden en que se apilen cambia el resultado. El enunciado no lo nombra a propósito:
 * reconocer la estructura cuando aparece vale más que memorizar el nombre.
 *
 * <p>Que esté aquí y no en la política de alerta es una decisión de diseño con consecuencias. La
 * política decide <em>si la situación es grave</em>; el filtro decide <em>si hace falta volver a
 * decirlo</em>. Son dos preguntas distintas, cambian por motivos distintos, y mezclarlas convertiría
 * la política en algo con memoria.
 */
public final class NotificadorSinRepetir implements Notificador {

    private final Notificador destino;

    // Un Set es el tipo para «¿está este ya?». Funciona porque los servicios son registros y traen
    // equals y hashCode escritos por el compilador: dos ServicioHttp de la misma URL son el mismo.
    private final Set<Servicio> yaAvisados = new HashSet<>();

    public NotificadorSinRepetir(Notificador destino) {
        this.destino = Objects.requireNonNull(destino, "destino");
    }

    @Override
    public void notificar(Alerta alerta) {
        // add devuelve true si el elemento no estaba: una sola operación para consultar y recordar.
        // Escrito como contains seguido de add serían dos recorridos y, peor, dos sitios donde
        // equivocarse al negar la condición.
        if (yaAvisados.add(alerta.servicio())) {
            destino.notificar(alerta);
        }
    }

    /** Para que un servicio que se ha recuperado vuelva a poder avisar si cae otra vez. */
    public void olvidar(Servicio servicio) {
        yaAvisados.remove(servicio);
    }

    // ----------------------------------------------------------------------------------
    // LO QUE FALTA, y conviene ver que falta: nadie llama a olvidar(). El monitor no avisa de las
    // recuperaciones, así que el filtro no tiene forma de enterarse de que un servicio ha vuelto, y
    // en la práctica un servicio avisa una vez y calla para siempre. Arreglarlo de verdad pide que
    // el monitor notifique también las recuperaciones, y eso es un cambio de Monitor, no de esta
    // clase. Es un buen ejemplo de cómo una decisión de diseño limita lo que las clases de alrededor
    // pueden hacer, por bien escritas que estén.
    // ----------------------------------------------------------------------------------
}
