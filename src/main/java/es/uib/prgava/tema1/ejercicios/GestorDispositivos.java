package es.uib.prgava.tema1.ejercicios;

/**
 * Ejercicio 1.2.4. <strong>Solución comentada.</strong> La interfaz grande original, ahora
 * reducida a la <em>suma</em> de los tres roles.
 *
 * <p>El principio de segregación de interfaces dice que ningún cliente debe depender de métodos
 * que no usa. La versión original obligaba a un simple catálogo a «implementar» el reinicio y la
 * actualización de firmware, normalmente lanzando excepciones — es decir, a violar Liskov, igual
 * que en el ejercicio 1.2.3.
 *
 * <p>Que esta interfaz siga existiendo no es una contradicción: sirve para quien de verdad
 * necesita los tres roles, como una consola de administración completa. Lo que ha cambiado es que
 * ahora es una <strong>opción</strong> y no una imposición: cada cliente declara el mínimo que
 * necesita. Compara con {@link es.uib.prgava.tema1.monitor.RepositorioResultados}, que hace
 * exactamente lo mismo con lectura y escritura.
 */
public interface GestorDispositivos
        extends ConsultaDispositivos, AltaDispositivos, AdministracionDispositivos {
}
