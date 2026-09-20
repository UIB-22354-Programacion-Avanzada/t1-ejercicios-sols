package es.uib.prgava.tema1.ejercicios;

import java.util.ArrayList;
import java.util.List;

/**
 * Ejercicio 1.2.3, paso 1. El diagnóstico: un método escrito contra {@code List} que funciona con
 * una {@code ArrayList} y revienta con una {@code ListaSoloLecturaHeredada}.
 */
public final class DiagnosticoLiskov {

    private DiagnosticoLiskov() {
    }

    /**
     * Un método cualquiera escrito contra la interfaz {@code List}. No conoce a
     * {@code ListaSoloLecturaHeredada} ni tiene por qué: programa contra el contrato de
     * {@code List}, que dice que {@code add} añade.
     */
    public static int anadirYContar(List<String> lista, String elemento) {
        lista.add(elemento);
        return lista.size();
    }

    public static void main(String[] args) {
        System.out.println(anadirYContar(new ArrayList<>(List.of("a", "b")), "c"));   // 3

        try {
            anadirYContar(new ListaSoloLecturaHeredada(List.of("a", "b")), "c");
        } catch (UnsupportedOperationException e) {
            System.out.println("UnsupportedOperationException: " + e.getMessage());
        }
    }

    // ----------------------------------------------------------------------------------
    // POR QUÉ ES UNA VIOLACIÓN DEL PRINCIPIO DE SUSTITUCIÓN, que el enunciado pedía explicar:
    //
    // anadirYContar no tiene ningún defecto. Está escrito contra List y usa List como List promete
    // que se puede usar. El defecto está en ListaSoloLecturaHeredada, que al escribir
    // `extends ArrayList<String>` ha firmado un compromiso —«sirvo en cualquier sitio donde sirva
    // una List»— que a continuación incumple.
    //
    // Lo grave no es la excepción, es CUÁNDO aparece: el compilador no dice nada, porque en el
    // sistema de tipos todo cuadra. El error sale en ejecución, dentro de un método que alguien
    // escribió correctamente, y a menudo lejos de donde se creó la lista. Con la versión por
    // composición esto no puede pasar: ListaSoloLectura no es una List, así que ni siquiera compila
    // el intento de pasársela a este método. El error se adelanta de la ejecución a la compilación,
    // que es casi siempre el objetivo de una decisión de diseño.
    // ----------------------------------------------------------------------------------
}
