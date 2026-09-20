package es.uib.prgava.tema1.ejercicios;

/**
 * Ejercicio 1.2.4, rol 1: <strong>modificar</strong> el inventario.
 *
 * <p>Lo necesita quien da de alta equipos y corrige sus datos. Un inventario de solo consulta no lo
 * cumple, y ahora ni lo declara.
 */
public interface AltaDeDispositivos {

    void darDeAlta(String nombre, String direccionIp);

    void borrar(String nombre);

    void renombrar(String nombre, String nuevoNombre);
}
