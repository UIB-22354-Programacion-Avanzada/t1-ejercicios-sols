// Descripciones.java
package es.uib.prgava.tema1.monitor;

public final class Descripciones {
    private Descripciones() { }

    public static String describir(Servicio servicio) {
        return switch (servicio) {
            case ServicioHttp h        -> "Servidor web en " + h.url();
            case ServicioDns d         -> "Servidor DNS para " + d.dominio();
            case PuertoTcp(var host, var puerto) -> "Puerto " + puerto + " de " + host;
            // Ejercicio 1.1.5: sin esta rama el switch deja de ser exhaustivo y NO COMPILA.
            // Ese error de compilación es la red de seguridad que da 'sealed'.
            case ServicioPing p        -> "Eco ICMP a " + p.host();
        };
        // sin 'default': el compilador verifica que el switch es exhaustivo
    }
}
