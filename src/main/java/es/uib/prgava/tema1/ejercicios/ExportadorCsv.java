package es.uib.prgava.tema1.ejercicios;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Ejercicio 1.2.1. <strong>Solución comentada.</strong> Solo escribe: no sabe nada de sensores
 * ni de estadísticas.
 *
 * <p>Esa ignorancia es justo lo que la hace reutilizable. Sirve para exportar las estadísticas
 * de un sensor, un informe de disponibilidad o cualquier otra cosa que sepa describirse como una
 * lista de líneas. Si recibiera un {@code EstadisticasSensor}, solo serviría para eso.
 */
public final class ExportadorCsv {

    /** Escribe una línea por fila. Traduce la {@code IOException} a {@code UncheckedIOException}. */
    public void escribir(Path destino, List<String> filas) {
        try {
            // Files.write con un Iterable<String> escribe una línea por elemento, con el
            // separador de línea de la plataforma y en UTF-8. No hace falta bucle ni Writer.
            Files.write(destino, filas);
        } catch (IOException e) {
            // Misma decisión que en el ejercicio 1.1.6: la firma del método queda libre de
            // excepciones comprobadas, y la causa original se conserva para depurar.
            throw new UncheckedIOException("No se pudo escribir " + destino, e);
        }
    }
}
