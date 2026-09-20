package es.uib.prgava.tema1.ejercicios;

import java.util.List;

/**
 * Ejercicio 1.2.4, rol 2: <strong>consultar</strong> el inventario.
 *
 * <p>Es el rol que más clientes tiene, y el único que cumplen todas las implementaciones que
 * guardan fichas. Un cliente que solo mira debería pedir esto y no {@code GestorDispositivos}: así
 * su firma dice la verdad sobre lo que va a hacer, y acepta cualquier implementación que consulte.
 */
public interface ConsultaDeDispositivos {

    boolean existe(String nombre);

    List<String> nombres();

    int cuantos();
}
