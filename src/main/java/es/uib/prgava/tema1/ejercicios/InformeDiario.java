package es.uib.prgava.tema1.ejercicios;

import es.uib.prgava.tema1.monitor.LectorResultados;
import es.uib.prgava.tema1.monitor.Servicio;

/**
 * Ejercicio 1.2.5. <strong>Solución comentada.</strong> Las mismas cuentas que
 * {@link InformeDiarioAcoplado}, con las dependencias invertidas.
 *
 * <p>Los dos defectos de la versión acoplada eran el mismo defecto: <strong>se construía sus
 * colaboradores por dentro</strong>. Hacía {@code new RepositorioEnMemoria()} —con lo que siempre
 * informaba sobre un repositorio vacío, y nadie podía darle datos— y escribía por
 * {@code System.out}, con lo que la única forma de comprobar el resultado era ejecutar el programa y
 * mirar la consola con los ojos. Una clase que fabrica sus colaboradores decide con quién trabaja, y
 * eso no es decisión suya.
 *
 * <p>Los dos entran ahora por el constructor, y declarados con el tipo de la
 * <strong>interfaz</strong>: {@link LectorResultados} y no {@code RepositorioEnMemoria},
 * {@link Salida} y no {@code SalidaPorConsola}. La diferencia es que la clase concreta se puede
 * cambiar sin tocar este fichero, y que una prueba puede pasar lo que quiera.
 *
 * <p>Fíjate además en que pide {@code LectorResultados} y no {@code RepositorioResultados}: un
 * informe lee y no escribe, así que pedir la interfaz de escritura sería pedir un permiso que no
 * necesita. Eso es el principio I aplicado dos ejercicios después.
 */
public final class InformeDiario {

    private final LectorResultados repositorio;
    private final Salida salida;

    public InformeDiario(LectorResultados repositorio, Salida salida) {
        this.repositorio = repositorio;
        this.salida = salida;
    }

    /**
     * Escribe tres líneas en la salida: el nombre del servicio, cuántas comprobaciones hay y
     * cuántas fueron fallo.
     */
    public void emitir(Servicio servicio) {
        var historial = repositorio.historial(servicio);
        int fallos = 0;
        for (var resultado : historial) {
            if (resultado.esFallo()) {
                fallos++;
            }
        }
        salida.escribir("servicio: " + servicio.nombre());
        salida.escribir("comprobaciones: " + historial.size());
        salida.escribir("fallos: " + fallos);
    }

    // ----------------------------------------------------------------------------------
    // RESPUESTA al «Para pensar»: si el constructor llegara a tener siete parámetros, ¿sería un
    // problema de la inyección de dependencias o una señal sobre la clase?
    //
    // Una señal sobre la clase. La inyección no crea el acoplamiento: lo hace VISIBLE. Una clase
    // con siete colaboradores tenía siete colaboradores también cuando los fabricaba por dentro; la
    // diferencia es que entonces no se veían, y ahora están escritos en la primera línea del
    // constructor. Un constructor largo es el aviso de que la clase hace demasiadas cosas, y la
    // respuesta correcta es el principio S, no esconder los parámetros con un Builder. El Builder
    // resuelve constructores con muchos parámetros de CONFIGURACIÓN —valores— y no con muchos
    // colaboradores.
    // ----------------------------------------------------------------------------------
}
