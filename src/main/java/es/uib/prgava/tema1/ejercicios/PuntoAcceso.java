package es.uib.prgava.tema1.ejercicios;

import java.util.Objects;

import es.uib.prgava.tema1.poo.DispositivoRed;

/**
 * Ejercicio 1.1.4. <strong>Solución comentada.</strong> Punto de acceso inalámbrico, subclase de
 * {@link DispositivoRed}.
 *
 * <p>La herencia está justificada aquí porque un punto de acceso <em>es un</em> dispositivo de
 * red: tiene nombre, dirección IP y puede activarse o desactivarse, y además añade banda y canal.
 * No estamos heredando para reutilizar código suelto, sino porque la relación de subtipo es real:
 * en cualquier sitio donde se espere un {@code DispositivoRed}, un {@code PuntoAcceso} sirve.
 * Contrasta con el ejercicio 1.2.3, donde heredar de {@code ArrayList} rompe esa promesa.
 */
public class PuntoAcceso extends DispositivoRed {

    /** Las dos bandas posibles. Un enum, y no un {@code String}, porque el conjunto es cerrado. */
    public enum Banda { GHZ_24, GHZ_5 }

    private final Banda banda;
    private int canal;                 // el canal sí cambia en caliente; la banda, no

    public PuntoAcceso(String nombre, String direccionIp, Banda banda, int canal) {
        // super(...) debe ser la primera sentencia: la parte heredada del objeto se construye
        // antes que la propia. Hasta que no termine, los campos de esta clase no existen.
        super(nombre, direccionIp);

        this.banda = Objects.requireNonNull(banda, "La banda no puede ser nula");
        validarCanal(banda, canal);    // el invariante se comprueba antes de asignar
        this.canal = canal;
    }

    public Banda getBanda() {
        return banda;
    }

    public int getCanal() {
        return canal;
    }

    /**
     * Cambia el canal aplicando <strong>la misma</strong> validación que el constructor.
     *
     * <p>La clave está en que la regla vive en un solo sitio, {@link #validarCanal}. Si estuviera
     * copiada en los dos métodos, tarde o temprano alguien ampliaría el rango en uno y no en el
     * otro, y existiría un camino por el que el objeto acabaría en un estado inválido.
     */
    public void cambiarCanal(int nuevoCanal) {
        validarCanal(banda, nuevoCanal);
        this.canal = nuevoCanal;
    }

    /**
     * Método auxiliar privado y estático: privado porque es un detalle interno, y estático
     * porque no necesita el estado del objeto — de hecho se invoca desde el constructor, donde
     * el objeto aún no está del todo construido.
     */
    private static void validarCanal(Banda banda, int canal) {
        boolean valido = switch (banda) {
            case GHZ_24 -> canal >= 1 && canal <= 14;
            case GHZ_5  -> canal >= 36 && canal <= 165;
        };
        // Sin 'default': al ser un enum, el compilador comprueba que el switch es exhaustivo,
        // y si mañana se añade una banda nueva este switch dejará de compilar. Eso es una
        // ayuda, no un estorbo: te obliga a decidir qué canales son válidos en la banda nueva.
        if (!valido) {
            throw new IllegalArgumentException(
                    "Canal " + canal + " no válido en la banda " + banda);
        }
    }

    @Override
    public String toString() {
        return "PuntoAcceso[" + getNombre() + ", ip=" + getDireccionIp()
                + ", banda=" + banda + ", canal=" + canal + "]";
    }

    // Respuesta al TODO 1.1.4 (por qué toString usa getNombre() y no el campo nombre):
    //
    // Porque el campo 'nombre' de DispositivoRed es privado, y privado significa privado
    // también para las subclases: esta clase no lo ve. Solo puede llegar a él por el método
    // público getNombre(). Y está bien que sea así: si el campo fuera 'protected', cualquier
    // subclase podría escribir en él saltándose las validaciones de la superclase, y la
    // superclase no podría cambiar su representación interna sin romper a sus subclases. La
    // encapsulación protege a la clase base de sus propios herederos.
}
