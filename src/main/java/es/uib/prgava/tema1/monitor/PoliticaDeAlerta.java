// PoliticaDeAlerta.java (con combinadores)
package es.uib.prgava.tema1.monitor;

import java.util.List;

@FunctionalInterface
public interface PoliticaDeAlerta {
    boolean debeAlertar(List<Resultado> historial);

    /**
     * Ejercicio 1.2.8. Crea una política a partir de su descripción textual:
     * {@code simple}, {@code consecutivos:N}, {@code ventana:K:N} o {@code latencia:MS:N}.
     *
     * @throws IllegalArgumentException si el texto no describe una política válida
     */
    static PoliticaDeAlerta desde(String texto) {
        // Solución comentada del ejercicio 1.2.8.
        //
        // Es una FÁBRICA ESTÁTICA en la propia interfaz, igual que Servicio.desde. La ventaja
        // de ponerla aquí y no en una clase 'FabricaDePoliticas' aparte es que el punto de
        // entrada está donde el lector lo busca, junto al tipo que produce.
        //
        // Fíjate en lo que devuelve: el tipo declarado es PoliticaDeAlerta, no la clase
        // concreta. Quien llama recibe la abstracción, y mañana podemos cambiar qué clase
        // instanciamos para cada texto sin romper a nadie. Un constructor no permite eso.
        var partes = texto.strip().split(":");

        try {
            return switch (partes[0]) {
                case "simple" -> {
                    exigir(partes.length == 1, texto);
                    yield new FalloSimple();
                }
                case "consecutivos" -> {
                    exigir(partes.length == 2, texto);
                    yield new FallosConsecutivos(Integer.parseInt(partes[1]));
                }
                case "ventana" -> {
                    exigir(partes.length == 3, texto);
                    yield new FallosEnVentana(Integer.parseInt(partes[1]),
                                              Integer.parseInt(partes[2]));
                }
                case "latencia" -> {
                    exigir(partes.length == 3, texto);
                    yield new LatenciaExcesiva(java.time.Duration.ofMillis(Long.parseLong(partes[1])),
                                               Integer.parseInt(partes[2]));
                }
                // Aquí SÍ hace falta 'default': el switch es sobre un String, un tipo abierto,
                // no sobre un enum ni sobre una jerarquía sellada. El compilador no puede
                // saber qué textos son válidos, así que la exhaustividad la ponemos nosotros.
                default -> throw new IllegalArgumentException("Política desconocida: " + texto);
            };
        } catch (NumberFormatException e) {
            // "consecutivos:x" llega hasta parseInt y falla ahí. Traducimos a la excepción que
            // documenta la firma, encadenando la causa y —esto lo comprueba la prueba— metiendo
            // el TEXTO ORIGINAL en el mensaje: sin él, el error diría solamente
            // 'For input string: "x"', que no dice qué línea de configuración corregir.
            throw new IllegalArgumentException("Política mal formada: " + texto, e);
        } catch (IllegalArgumentException e) {
            // Las validaciones de los constructores (por ejemplo minimoFallos > tamanoVentana
            // en FallosEnVentana) también son IllegalArgumentException. Las volvemos a lanzar
            // añadiendo el texto de origen, por el mismo motivo.
            throw new IllegalArgumentException("Política mal formada: " + texto, e);
        }
    }

    /** Auxiliar privado: convierte una condición incumplida en el error estándar de esta fábrica. */
    private static void exigir(boolean condicion, String texto) {
        if (!condicion) {
            throw new IllegalArgumentException("Política mal formada: " + texto);
        }
    }

    // Los dos combinadores de abajo ya venían escritos, pero merecen una lectura: permiten
    // construir políticas compuestas —"alerta si hay dos fallos seguidos O la latencia se
    // dispara"— sin escribir ninguna clase nueva. Son posibles porque PoliticaDeAlerta es una
    // interfaz funcional: 'h -> ...' es ya una política. La prueba
    // seCombinaConOtrasPoliticasSinTocarMonitor los usa.
    default PoliticaDeAlerta o(PoliticaDeAlerta otra) {
        return h -> this.debeAlertar(h) || otra.debeAlertar(h);
    }

    default PoliticaDeAlerta y(PoliticaDeAlerta otra) {
        return h -> this.debeAlertar(h) && otra.debeAlertar(h);
    }
}
