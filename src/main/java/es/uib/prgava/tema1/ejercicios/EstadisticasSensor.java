package es.uib.prgava.tema1.ejercicios;

import java.util.List;

/**
 * Ejercicio 1.2.1. <strong>Solución comentada.</strong> Solo calcula: no toca el sistema de
 * ficheros ni la consola.
 *
 * <p>Esta clase es la mitad «cálculo» de {@link RegistroSensores}. La razón por la que la
 * separación merece la pena se ve en {@code EstadisticasSensorTest}: sus pruebas
 * <strong>no crean ningún fichero</strong>, no necesitan un directorio temporal ni limpiar nada
 * después. Con la clase original, probar la media obligaba a arrastrar todo el aparato de E/S.
 *
 * <p>Dicho en términos del principio de responsabilidad única: la versión original tenía dos
 * razones para cambiar —que cambie la fórmula de una métrica, o que cambie el formato del
 * fichero—. Ahora cada motivo afecta a una clase distinta.
 */
public final class EstadisticasSensor {

    private final List<Double> lecturas;

    public EstadisticasSensor(List<Double> lecturas) {
        // List.copyOf hace dos cosas a la vez: copia (así, si quien llama modifica su lista
        // después, a nosotros no nos afecta) y devuelve una lista inmutable (así nadie puede
        // modificarla a través de una referencia que se nos escape). Y rechaza null, de regalo.
        this.lecturas = List.copyOf(lecturas);
    }

    public int cuantas() {
        return lecturas.size();
    }

    /**
     * {@code mapToDouble} evita el <em>boxing</em>: trabajamos con un {@code DoubleStream} de
     * primitivos en vez de con objetos {@code Double}.
     *
     * <p>El {@code orElse(0.0)} merece un comentario: la media de una lista vacía no existe, y
     * {@code average()} lo refleja devolviendo un {@code OptionalDouble} vacío. Devolver 0.0 es
     * una decisión discutible —confunde «no hay datos» con «la media es cero»— pero es la que
     * tomaba el código original, y el ejercicio pide que la salida no cambie. En un diseño nuevo,
     * lo correcto sería devolver el {@code OptionalDouble} y que decida quien llama.
     */
    public double media() {
        return lecturas.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public double maximo() {
        return lecturas.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
    }

    public double minimo() {
        return lecturas.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
    }

    /**
     * Filas listas para exportar, empezando por la cabecera {@code metrica;valor}.
     *
     * <p>Este método es la <strong>frontera</strong> entre las dos clases: produce datos en
     * memoria, sin saber si acabarán en un fichero, en una consola o en una respuesta HTTP.
     * Gracias a él, {@link ExportadorCsv} no necesita saber nada de sensores.
     */
    public List<String> comoFilas() {
        return List.of(
                "metrica;valor",
                "cuantas;" + cuantas(),
                "media;" + media(),
                "maximo;" + maximo(),
                "minimo;" + minimo());
    }
}
