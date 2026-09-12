package es.uib.prgava.tema1.monitor;

import java.util.List;

/**
 * Ejercicio 1.2.2. <strong>Solución comentada.</strong> Alerta cuando hay al menos
 * {@code minimoFallos} fallos entre las últimas {@code tamanoVentana} comprobaciones.
 *
 * <p>Lo importante de este ejercicio no es el algoritmo, que es corto, sino lo que
 * <strong>no</strong> hay que tocar: ni {@link Monitor} ni {@link PoliticaDeAlerta}. Esa es la
 * definición operativa del principio abierto-cerrado: el sistema queda abierto a comportamientos
 * nuevos (una política más) y cerrado a modificaciones (el código ya probado sigue intacto).
 * Se consigue porque {@code Monitor} depende de la <em>interfaz</em> y no de una implementación
 * concreta.
 */
public final class FallosEnVentana implements PoliticaDeAlerta {

    private final int minimoFallos;
    private final int tamanoVentana;

    public FallosEnVentana(int minimoFallos, int tamanoVentana) {
        // Las dos condiciones en una sola comprobación: el mínimo ha de ser al menos 1 (pedir
        // «cero fallos» alertaría siempre) y como mucho el tamaño de la ventana (pedir más
        // fallos de los que caben en la ventana no alertaría nunca). Ambos extremos son errores
        // de programación, no de datos, así que IllegalArgumentException es lo adecuado.
        if (minimoFallos < 1 || minimoFallos > tamanoVentana) {
            throw new IllegalArgumentException(
                    "Se esperaba 1 <= minimoFallos <= tamanoVentana, y llegó "
                            + minimoFallos + " y " + tamanoVentana);
        }
        this.minimoFallos = minimoFallos;
        this.tamanoVentana = tamanoVentana;
    }

    @Override
    public boolean debeAlertar(List<Resultado> historial) {
        // Tomamos las ÚLTIMAS tamanoVentana comprobaciones. Si el historial es más corto,
        // empezamos en 0: Math.max evita el índice negativo, que es el error clásico aquí.
        int desde = Math.max(0, historial.size() - tamanoVentana);

        // subList devuelve una vista, no una copia: no cuesta memoria. Solo la leemos.
        long fallos = historial.subList(desde, historial.size()).stream()
                .filter(Resultado::esFallo)
                .count();

        return fallos >= minimoFallos;
    }

    /**
     * El {@code toString} no es decorativo: {@link Monitor} lo usa para componer el mensaje de
     * la alerta («condición: 2 fallos en las últimas 5 comprobaciones»). Una política sin un
     * {@code toString} legible produce alertas ininteligibles.
     */
    @Override
    public String toString() {
        return minimoFallos + " fallos en las últimas " + tamanoVentana + " comprobaciones";
    }
}
