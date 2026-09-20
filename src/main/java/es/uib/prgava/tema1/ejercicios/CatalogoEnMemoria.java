package es.uib.prgava.tema1.ejercicios;

import java.util.ArrayList;
import java.util.List;

/**
 * Ejercicio 1.2.4. La única de las tres implementaciones que cumple los cuatro roles, así que es la
 * única que puede declarar {@link GestorDispositivos}. Su código no ha cambiado ni una línea al
 * dividir la interfaz: no tenía nada que esconder.
 */
public final class CatalogoEnMemoria implements GestorDispositivos {

    private final List<FichaDispositivo> fichas = new ArrayList<>();

    @Override
    public void darDeAlta(String nombre, String direccionIp) {
        if (existe(nombre)) {
            throw new IllegalArgumentException("ya existe: " + nombre);
        }
        fichas.add(new FichaDispositivo(nombre, direccionIp));
    }

    @Override
    public void borrar(String nombre) {
        for (int i = 0; i < fichas.size(); i++) {
            if (fichas.get(i).nombre().equals(nombre)) {
                fichas.remove(i);
                return;
            }
        }
    }

    @Override
    public void renombrar(String nombre, String nuevoNombre) {
        for (int i = 0; i < fichas.size(); i++) {
            if (fichas.get(i).nombre().equals(nombre)) {
                fichas.set(i, new FichaDispositivo(nuevoNombre, fichas.get(i).direccionIp()));
                return;
            }
        }
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

    @Override
    public String exportarCsv() {
        var texto = new StringBuilder();
        for (var ficha : fichas) {
            texto.append(ficha.nombre()).append(';').append(ficha.direccionIp()).append('\n');
        }
        return texto.toString();
    }

    @Override
    public String exportarJson() {
        var texto = new StringBuilder("[");
        for (int i = 0; i < fichas.size(); i++) {
            if (i > 0) {
                texto.append(',');
            }
            texto.append("{\"nombre\":\"").append(fichas.get(i).nombre())
                 .append("\",\"ip\":\"").append(fichas.get(i).direccionIp()).append("\"}");
        }
        return texto.append(']').toString();
    }
}
