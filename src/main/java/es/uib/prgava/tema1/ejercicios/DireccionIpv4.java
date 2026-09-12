package es.uib.prgava.tema1.ejercicios;

/**
 * Ejercicio 1.1.1. Dirección IPv4 como cuatro octetos.
 *
 * <p><strong>Solución comentada.</strong>
 *
 * <p>Por qué un {@code record} y no una clase: una dirección IP es un <em>valor</em>, no una
 * entidad. Dos direcciones con los mismos cuatro octetos son la misma dirección, no hay estado
 * que cambie con el tiempo y no tiene identidad propia. Ese es exactamente el caso de uso de
 * {@code record}: el compilador genera el constructor canónico, los accesores, {@code equals},
 * {@code hashCode} y {@code toString}.
 *
 * <p>Por eso el enunciado pide no escribir {@code equals} ni {@code hashCode}: el {@code equals}
 * generado ya compara componente a componente, que es justo la igualdad que queremos, y el
 * {@code hashCode} generado es coherente con él. Escribirlos a mano sería repetir trabajo del
 * compilador e introducir la posibilidad de que se descoordinen (el defecto del ejercicio 1.1.3).
 */
public record DireccionIpv4(int primero, int segundo, int tercero, int cuarto) {

    /**
     * Constructor compacto: se ejecuta antes de asignar los campos y es el lugar donde se
     * establece el <strong>invariante</strong> de la clase — «los cuatro octetos están entre 0 y
     * 255»—. Al validar aquí, y como los campos de un record son finales, es imposible que exista
     * un objeto {@code DireccionIpv4} con un octeto inválido: no hay que volver a comprobarlo en
     * ningún otro sitio.
     */
    public DireccionIpv4 {
        validarOcteto(primero);
        validarOcteto(segundo);
        validarOcteto(tercero);
        validarOcteto(cuarto);
    }

    /**
     * Un único punto de validación, invocado cuatro veces. La alternativa —cuatro {@code if}
     * copiados— repite la condición y el mensaje, y es donde acaban apareciendo las erratas.
     */
    private static void validarOcteto(int valor) {
        if (valor < 0 || valor > 255) {
            throw new IllegalArgumentException("Octeto fuera de rango: " + valor);
        }
    }

    /**
     * Fábrica estática. Tiene dos ventajas sobre un constructor que recibiera un {@code String}:
     * el nombre dice qué hace ({@code desde} frente a otro constructor más), y permite validar y
     * traducir el formato textual antes de llegar al constructor canónico.
     *
     * @throws IllegalArgumentException si no hay cuatro partes o alguna no es un entero
     */
    public static DireccionIpv4 desde(String texto) {
        // -1 como límite conservaría las partes vacías finales; aquí basta el split normal,
        // pero comprobamos explícitamente que hay exactamente cuatro partes.
        String[] partes = texto.strip().split("\\.");
        if (partes.length != 4) {
            throw new IllegalArgumentException(
                    "Se esperaban cuatro octetos separados por puntos, y llegó: " + texto);
        }
        try {
            // El constructor canónico valida los rangos; aquí solo traducimos texto a números.
            return new DireccionIpv4(
                    Integer.parseInt(partes[0].strip()),
                    Integer.parseInt(partes[1].strip()),
                    Integer.parseInt(partes[2].strip()),
                    Integer.parseInt(partes[3].strip()));
        } catch (NumberFormatException e) {
            // Encadenamos la causa: quien depure verá también el NumberFormatException original.
            throw new IllegalArgumentException("Octeto no numérico en: " + texto, e);
        }
    }

    /** Devuelve la forma habitual, por ejemplo {@code 192.168.1.1}. */
    @Override
    public String toString() {
        // Redefinir toString en un record es legítimo: el generado sería
        // "DireccionIpv4[primero=192, ...]", útil para depurar pero no para mostrar.
        return primero + "." + segundo + "." + tercero + "." + cuarto;
    }

    /**
     * Indica si la dirección pertenece a un rango privado (RFC 1918): 10/8, 172.16/12 y
     * 192.168/16.
     *
     * <p>Fíjate en el rango de en medio: 172.16.0.0–172.31.255.255. Es el que concentra los
     * errores, porque es fácil escribir {@code segundo == 16} o pasarse hasta 172.32. Los cuatro
     * casos límite que pide probar el ejercicio 1.4.2 son exactamente los bordes de este rango.
     */
    public boolean esPrivada() {
        return primero == 10
                || (primero == 172 && segundo >= 16 && segundo <= 31)
                || (primero == 192 && segundo == 168);
    }
}
