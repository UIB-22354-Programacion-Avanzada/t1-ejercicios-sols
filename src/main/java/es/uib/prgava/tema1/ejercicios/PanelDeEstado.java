package es.uib.prgava.tema1.ejercicios;

import es.uib.prgava.tema1.poo.RegistroEventos;

/**
 * Ejercicio 1.1.5. <strong>Solución comentada.</strong> Lo mismo que
 * {@link PanelDeEstadoHeredado}, por composición en lugar de por herencia.
 *
 * <p>Compara las dos clases línea a línea. La heredada funciona <em>hoy</em>, y da la cuenta
 * correcta, pero solo porque {@code RegistroEventos.registrarVarios} está implementado llamando a
 * {@code registrar}. Ese detalle es interno: nadie lo ha prometido y nadie tiene obligación de
 * mantenerlo. El día que alguien reescriba {@code registrarVarios} para que escriba directamente,
 * la cuenta del panel heredado dejará de salir sin que nadie haya tocado el panel. Eso es la clase
 * base frágil.
 *
 * <p>Esta versión no depende de ningún detalle interno, porque quien decide qué se llama y cuándo
 * es ella: su {@code registrarVarios} llama a <em>su</em> {@code registrar}. El precio son los dos
 * métodos de delegación; a cambio, la clase es robusta y su interfaz pública es exactamente la que
 * queremos.
 */
public final class PanelDeEstado {

    // TIENE un registro, en lugar de SER uno. Esta línea es toda la diferencia.
    private final RegistroEventos interno = new RegistroEventos();
    private int registrados = 0;

    public void registrar(String evento) {
        registrados++;
        interno.registrar(evento);
    }

    /**
     * Llama a <strong>nuestro</strong> {@code registrar}, no al del objeto interno. Por eso la
     * cuenta no se puede duplicar ni quedarse corta: pasa una sola vez por el único sitio que
     * incrementa.
     */
    public void registrarVarios(String... eventos) {
        for (var evento : eventos) {
            registrar(evento);
        }
    }

    public int registrados() {
        return registrados;
    }

    public String resumen() {
        return registrados + " eventos registrados";
    }

    // ----------------------------------------------------------------------------------
    // LO QUE NO ESTÁ AQUÍ también es parte de la solución. RegistroEventos no ofrece hoy más
    // métodos públicos, pero si ofreciera diez, el panel seguiría teniendo exactamente estos
    // cuatro. Con herencia, los diez formarían parte de su interfaz pública quisiéramos o no, y
    // cualquiera podría saltarse la cuenta llamando al que no la incrementa. Con composición, lo
    // que no se delega no existe.
    //
    // Y la respuesta al «Para pensar»: con treinta métodos que hicieran falta todos, los treinta
    // métodos de delegación son un argumento serio a favor de la herencia, y es la primera vez en
    // este tema en que la balanza se inclina hacia ella. La pregunta que decide no es cuántos
    // métodos hay, sino si la subclase necesita CONTROLAR cuándo se llaman: si la respuesta es sí,
    // como aquí, la composición sigue ganando aunque cueste código; si es no —si de verdad solo
    // queremos ampliar—, heredar es razonable.
    // ----------------------------------------------------------------------------------
}
