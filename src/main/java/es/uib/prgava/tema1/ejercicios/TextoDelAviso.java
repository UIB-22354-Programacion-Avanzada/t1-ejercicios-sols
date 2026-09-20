package es.uib.prgava.tema1.ejercicios;

/**
 * Ejercicio 1.2.1, pieza 4 de 4: <strong>redactar</strong>.
 *
 * <p>Su única razón para cambiar es que cambie el formato del mensaje: que se quiera en inglés, con
 * dos decimales, o en JSON para enviarlo a otro sistema. Quien pediría ese cambio es quien lee los
 * avisos, y es asombroso lo a menudo que ese cambio llega solo.
 */
public final class TextoDelAviso {

    public String para(String sensor, double media, double umbral) {
        return "[AVISO] " + sensor + ": media " + String.format("%.1f", media)
                + ", por encima del umbral " + umbral;
    }
}
