package es.uib.prgava.tema1.ejercicios;

import es.uib.prgava.tema1.poo.DispositivoRed;

/**
 * Ejercicio 1.1.4. <strong>Solución comentada.</strong> Herencia, y el criterio para usarla.
 *
 * <p>La frase que autoriza la herencia es «un punto de acceso <em>es un</em> dispositivo de red»:
 * tiene nombre y dirección IP como cualquier otro, se activa y se desactiva igual, y además tiene
 * cosas propias. Cuando la frase que encaja es «<em>tiene un</em>», la herramienta es la
 * composición, y ese es el ejercicio siguiente.
 *
 * <p>Lo que se hereda no se reescribe: no hay aquí ni {@code nombre}, ni {@code direccionIp}, ni
 * {@code activar}, ni {@code isActivo}. Solo lo que un conmutador no tiene: el SSID y el canal.
 */
public class PuntoAcceso extends DispositivoRed {

    private final String ssid;
    private int canal;                 // cambia: hay un método para cambiarlo

    /**
     * La llamada a {@code super(...)} tiene que ser la primera instrucción del constructor, y esa
     * restricción del lenguaje es una ayuda: garantiza que la parte heredada del objeto queda
     * completa y válida antes de que esta clase empiece a tocar lo suyo.
     *
     * @param canal de 1 a 13
     * @throws IllegalArgumentException si el canal se sale de ese rango
     */
    public PuntoAcceso(String nombre, String direccionIp, String ssid, int canal) {
        super(nombre, direccionIp);
        this.ssid = ssid;
        this.canal = comprobado(canal);
    }

    /**
     * La validación, extraída a un método privado de clase. El enunciado preguntaba si compensa
     * escribirla dos veces —en el constructor y en {@code cambiarCanal}— y la respuesta es que no:
     * el día que el rango cambie, con las dos copias hay que acordarse de las dos.
     *
     * <p>Devuelve el valor comprobado en lugar de no devolver nada. Así se puede escribir
     * {@code this.canal = comprobado(canal)}, y no queda la posibilidad de llamar a la validación
     * y olvidarse de asignar.
     */
    private static int comprobado(int canal) {
        if (canal < 1 || canal > 13) {
            throw new IllegalArgumentException("Canal fuera de rango: " + canal);
        }
        return canal;
    }

    public String getSsid() {
        return ssid;
    }

    public int getCanal() {
        return canal;
    }

    public void cambiarCanal(int nuevo) {
        canal = comprobado(nuevo);
    }

    /**
     * {@code super.toString()} reutiliza lo que la superclase ya sabe hacer, en lugar de repetir
     * aquí el formato de nombre, IP y estado. Si mañana {@code DispositivoRed} añade un campo a su
     * {@code toString}, este lo hereda sin tocarlo.
     */
    @Override
    public String toString() {
        return super.toString() + " + PuntoAcceso[ssid=" + ssid + ", canal=" + canal + "]";
    }

    // ----------------------------------------------------------------------------------
    // RESPUESTA a la pregunta del paso 5: por qué getDispositivosCreados() cuenta también los
    // puntos de acceso, si el contador está declarado en la superclase y esta clase no lo toca.
    //
    // Porque `dispositivosCreados` es un atributo de CLASE: hay uno solo, y pertenece a
    // DispositivoRed, no a cada objeto. Todo constructor de PuntoAcceso empieza llamando a
    // super(...), y ese constructor es el que incrementa la cuenta. No hay un contador de
    // enrutadores y otro de puntos de acceso: hay un contador de dispositivos de red, y un punto
    // de acceso es uno de ellos. Si quisiéramos contarlos por separado, cada subclase necesitaría
    // su propio atributo de clase.
    // ----------------------------------------------------------------------------------
}
