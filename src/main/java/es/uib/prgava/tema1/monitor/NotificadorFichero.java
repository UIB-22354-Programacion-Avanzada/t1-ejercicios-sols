package es.uib.prgava.tema1.monitor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/**
 * Ejercicio 1.2.6. <strong>Solución comentada.</strong> Notificador que añade una línea por
 * alerta a un fichero de texto.
 *
 * <p>Es un observador más, al mismo nivel que {@link NotificadorConsola} y
 * {@link NotificadorRegistro}: {@code Monitor} no necesita enterarse de que existe, basta con
 * suscribirlo. La segunda prueba comprueba justamente eso — todos los suscriptores reciben las
 * mismas alertas, sin que el monitor sepa cuántos hay ni qué hacen.
 */
public final class NotificadorFichero implements Notificador {

    private final Path destino;

    public NotificadorFichero(Path destino) {
        this.destino = Objects.requireNonNull(destino, "El destino no puede ser nulo");
    }

    @Override
    public void notificar(Alerta alerta) {
        // try-with-resources: el writer se cierra solo, pase lo que pase dentro del bloque.
        // Cerrar importa aquí doblemente, porque además del descriptor de fichero hay un búfer
        // que solo se vuelca al cerrar: sin cierre, las líneas podrían no llegar al disco.
        //
        // CREATE + APPEND: crea el fichero si no existe (la prueba lo borra antes a propósito)
        // y añade al final si ya existe, en vez de truncarlo. Sin APPEND, cada alerta borraría
        // la anterior y el fichero acabaría con una sola línea.
        try (BufferedWriter escritor = Files.newBufferedWriter(destino,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {

            escritor.write(alerta.instante() + ";" + alerta.servicio().nombre()
                    + ";" + alerta.mensaje());
            escritor.newLine();

        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo escribir la alerta en " + destino, e);
        }
    }

    // Justificación de traducir IOException a UncheckedIOException (lo pide el enunciado):
    //
    // El método implementa Notificador.notificar, cuya firma es 'void notificar(Alerta)' y no
    // declara excepciones. No podemos añadir 'throws IOException' sin cambiar la interfaz, y
    // cambiarla obligaría a TODOS los notificadores —incluido el de consola, que no hace E/S—
    // a cargar con una excepción que no les corresponde, y a Monitor a capturarla. Es decir,
    // un detalle de implementación de un solo observador contaminaría el contrato de todos.
    //
    // Tampoco vale tragarse la excepción con un catch vacío: perder alertas en silencio es peor
    // que fallar. Envolverla en una no comprobada mantiene la interfaz limpia, conserva la causa
    // original y deja que el fallo se propague hasta quien pueda decidir qué hacer con él.
    //
    // Nota de eficiencia: abrir y cerrar el fichero en cada alerta es deliberadamente simple.
    // Con un volumen alto convendría mantener el writer abierto, pero entonces la clase pasaría
    // a gestionar un recurso de vida larga y tendría que implementar AutoCloseable, y alguien
    // tendría que cerrarla. Es el intercambio habitual entre simplicidad y rendimiento.
}
