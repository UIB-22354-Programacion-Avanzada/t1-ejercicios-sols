package es.uib.prgava.tema1.ejercicios;

import java.util.List;

/**
 * Ejercicio 1.2.1. <strong>Solución comentada.</strong> La misma clase de antes, pero ya no hace
 * nada: delega.
 *
 * <p>La versión original hacía cuatro cosas —guardar lecturas, calcular la media, decidir si hay
 * que avisar y redactar el aviso— y por tanto tenía cuatro razones para cambiar. Ese es el criterio
 * del principio S, y no «hacer una sola cosa»: una clase que hace dos cosas que siempre cambian
 * juntas está bien; una que hace dos cosas que cambian por motivos distintos, y a peticición de
 * personas distintas, está mal, porque cada cambio obliga a volver a leer y a probar lo demás.
 *
 * <p>Las cuatro piezas son {@link LecturasSensor}, {@link MediaDeLecturas}, {@link DecisionDeAviso}
 * y {@link TextoDelAviso}. Esta clase se queda como <strong>fachada</strong>: mantiene los métodos
 * públicos de antes y los reenvía. Eso es lo que hace que {@code RegistroSensoresTest} siga pasando
 * sin tocar una línea, que es la definición de refactorizar: cambia la estructura por dentro y el
 * comportamiento observable no.
 *
 * <p>Y la respuesta al «Para pensar» —dónde va el cableado de las cuatro clases—: aquí, en la
 * fachada, y no dentro de ninguna de las cuatro. Si {@code DecisionDeAviso} construyera un
 * {@code MediaDeLecturas}, volveríamos a atarlas entre sí y no habríamos ganado nada. En el monitor
 * ese papel lo hace {@code Principal}, y en 1.2.5 se verá con su nombre propio: inyección de
 * dependencias.
 */
public class RegistroSensores {

    private final String sensor;
    private final LecturasSensor lecturas = new LecturasSensor();
    private final MediaDeLecturas calculo = new MediaDeLecturas();
    private final DecisionDeAviso decision;
    private final TextoDelAviso redaccion = new TextoDelAviso();

    public RegistroSensores(String sensor, double umbral) {
        this.sensor = sensor;
        this.decision = new DecisionDeAviso(umbral);
    }

    public void anadir(double lectura) {
        lecturas.anadir(lectura);
    }

    public List<Double> lecturas() {
        return lecturas.todas();
    }

    public double media() {
        return calculo.de(lecturas.todas());
    }

    public boolean hayQueAvisar() {
        return decision.hayQueAvisar(lecturas.cuantas(), media());
    }

    public String textoDelAviso() {
        return redaccion.para(sensor, media(), decision.umbral());
    }

    public void avisar() {
        if (hayQueAvisar()) {
            System.out.println(textoDelAviso());
        }
    }

    // ----------------------------------------------------------------------------------
    // LAS CUATRO RESPONSABILIDADES que pedía enumerar el enunciado, con quién pediría cada cambio:
    //
    //   1. guardar las lecturas y validarlas ......... quien instala los sensores
    //   2. calcular el estadístico ................... quien interpreta los datos
    //   3. decidir si la situación merece un aviso ... quien está de guardia
    //   4. redactar el texto del aviso ............... quien lo recibe
    //
    // Son cuatro personas distintas y cuatro calendarios distintos. Por eso son cuatro clases.
    //
    // Queda una quinta cosa que esta clase sigue haciendo y que en un diseño mejor tampoco sería
    // suya: EMITIR el aviso por System.out, en avisar(). Se ha dejado a propósito para no cambiar
    // el comportamiento observable, y es exactamente el defecto que el ejercicio 1.2.5 obliga a
    // corregir en InformeDiario.
    // ----------------------------------------------------------------------------------
}
