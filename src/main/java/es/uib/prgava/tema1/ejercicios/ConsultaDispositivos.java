package es.uib.prgava.tema1.ejercicios;

import java.util.List;
import java.util.Optional;

/**
 * Ejercicio 1.2.4. <strong>Solución comentada.</strong> Rol de <strong>consulta</strong>.
 *
 * <p>Un cliente que solo necesita mirar el catálogo —una pantalla de listado, un informe—
 * depende únicamente de esta interfaz. No se entera de que existen el alta o la administración,
 * y no hay que recompilarlo si aquellas cambian.
 */
public interface ConsultaDispositivos {

    Optional<FichaDispositivo> buscarPorNombre(String nombre);

    List<FichaDispositivo> listarTodos();

    int contar();
}
