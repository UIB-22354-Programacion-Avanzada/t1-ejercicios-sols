package es.uib.prgava.tema1.ejercicios;

/**
 * Ejercicio 1.2.4. <strong>Solución comentada.</strong> Rol de <strong>alta y baja</strong>.
 *
 * <p>Alta y baja van juntas porque son la misma responsabilidad vista en los dos sentidos: quien
 * puede registrar un dispositivo es quien puede quitarlo. Separarlas más sería trocear por
 * trocear.
 */
public interface AltaDispositivos {

    void registrar(FichaDispositivo ficha);

    void eliminar(String nombre);
}
