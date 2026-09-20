package es.uib.prgava.tema1.ejercicios;

import java.util.ArrayList;
import java.util.List;

/**
 * Ejercicio 1.2.4. Inventario de solo consulta: se construye una vez y no cambia.
 *
 * <p>Antes declaraba {@code GestorDispositivos} y tenía que escribir cinco métodos que no sabía
 * cumplir: tres que lanzaban {@code UnsupportedOperationException} y dos exportaciones que
 * devolvían la cadena vacía. Ahora declara solo {@link ConsultaDeDispositivos} y
 * <strong>no le queda ni uno</strong>. Ese es el resultado que se buscaba, y se mide así: cuenta
 * los cuerpos que no hacen nada.
 */
public final class ConsultaDispositivos implements ConsultaDeDispositivos {

    private final List<FichaDispositivo> fichas;

    public ConsultaDispositivos(List<FichaDispositivo> fichas) {
        this.fichas = List.copyOf(fichas);
    }

    @Override
    public boolean existe(String nombre) {
        for (var ficha : fichas) {
            if (ficha.nombre().equals(nombre)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> nombres() {
        var todos = new ArrayList<String>();
        for (var ficha : fichas) {
            todos.add(ficha.nombre());
        }
        return List.copyOf(todos);
    }

    @Override
    public int cuantos() {
        return fichas.size();
    }
}
