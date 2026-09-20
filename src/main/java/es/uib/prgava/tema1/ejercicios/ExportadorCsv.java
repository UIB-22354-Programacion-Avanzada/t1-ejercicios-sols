package es.uib.prgava.tema1.ejercicios;

import java.util.List;

/**
 * Ejercicio 1.2.4. Solo exporta: recibe las fichas ya hechas y las convierte a texto.
 *
 * <p>De los ocho métodos de la interfaz original le interesaba uno, y los otros siete los escribía
 * vacíos o lanzando excepciones. Ahora declara {@link ExportacionCsv} y nada más.
 *
 * <p>{@code cuantos()} sigue aquí, porque es información que esta clase sí tiene y alguien usa, pero
 * <strong>ya no forma parte de ninguna interfaz que declare</strong>: es un método propio. Eso es
 * otra parte de la lección: un método público no tiene por qué venir de una interfaz.
 */
public final class ExportadorCsv implements ExportacionCsv {

    private final List<FichaDispositivo> fichas;

    public ExportadorCsv(List<FichaDispositivo> fichas) {
        this.fichas = List.copyOf(fichas);
    }

    @Override
    public String exportarCsv() {
        var texto = new StringBuilder();
        for (var ficha : fichas) {
            texto.append(ficha.nombre()).append(';').append(ficha.direccionIp()).append('\n');
        }
        return texto.toString();
    }

    public int cuantos() {
        return fichas.size();
    }
}
