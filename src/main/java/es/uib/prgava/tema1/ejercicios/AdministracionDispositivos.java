package es.uib.prgava.tema1.ejercicios;

/**
 * Ejercicio 1.2.4. <strong>Solución comentada.</strong> Rol de <strong>administración</strong>.
 *
 * <p>Estas tres operaciones actúan sobre el aparato físico, no sobre el catálogo. Ese es el
 * criterio para separarlas: no es «cuántos métodos caben en una interfaz», sino que responden a
 * clientes distintos y cambian por motivos distintos.
 */
public interface AdministracionDispositivos {

    void reiniciar(String nombre);

    void actualizarFirmware(String nombre, String version);

    void restaurarValoresDeFabrica(String nombre);
}
