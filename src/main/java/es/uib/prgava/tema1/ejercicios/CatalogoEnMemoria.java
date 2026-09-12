package es.uib.prgava.tema1.ejercicios;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Ejercicio 1.2.4. <strong>Solución comentada.</strong> Un catálogo consulta y da de alta, pero
 * <strong>no administra</strong>.
 *
 * <p>Y ahora puede decirlo en el sistema de tipos: implementa dos de las tres interfaces de rol.
 * Con la interfaz grande original habría tenido que implementar los ocho métodos y lanzar
 * {@code UnsupportedOperationException} en tres de ellos, prometiendo algo que no cumple. La
 * prueba {@code unCatalogoSirveComoConsultaYComoAlta} comprueba precisamente que se puede usar
 * en los dos papeles.
 */
public final class CatalogoEnMemoria implements ConsultaDispositivos, AltaDispositivos {

    // LinkedHashMap: búsqueda por nombre en tiempo constante y, además, listarTodos() devuelve
    // los dispositivos en el orden en que se registraron, que es lo que espera quien mira la
    // pantalla. Con un HashMap el orden sería impredecible y cambiaría entre ejecuciones.
    private final Map<String, FichaDispositivo> fichas = new LinkedHashMap<>();

    /**
     * {@code @Override} no es decorativo: si por error hubiéramos puesto este método en la
     * interfaz equivocada, o con otra firma, el compilador lo rechazaría aquí. Es la
     * comprobación que sugiere el enunciado.
     */
    @Override
    public Optional<FichaDispositivo> buscarPorNombre(String nombre) {
        // Optional en vez de null: el tipo de retorno ya avisa de que puede no haber resultado,
        // y quien llama no puede ignorarlo sin darse cuenta.
        return Optional.ofNullable(fichas.get(nombre));
    }

    @Override
    public List<FichaDispositivo> listarTodos() {
        // Copia: quien reciba la lista no debe poder alterar el catálogo a través de ella.
        return List.copyOf(new ArrayList<>(fichas.values()));
    }

    @Override
    public int contar() {
        return fichas.size();
    }

    @Override
    public void registrar(FichaDispositivo ficha) {
        Objects.requireNonNull(ficha, "La ficha no puede ser nula");
        fichas.put(ficha.nombre(), ficha);
    }

    @Override
    public void eliminar(String nombre) {
        fichas.remove(nombre);
        // remove de un nombre inexistente no hace nada y no lanza: eliminar algo que no está es
        // una operación idempotente, no un error. Si quisiéramos avisar, el método debería
        // devolver boolean en la interfaz.
    }
}
