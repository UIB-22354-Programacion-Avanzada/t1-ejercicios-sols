package es.uib.prgava.tema1.ejercicios;

/**
 * Ejercicio 1.2.4. <strong>Solución comentada.</strong> La interfaz de ocho métodos, dividida.
 *
 * <p>El síntoma del principio I era el código que las implementaciones se veían obligadas a
 * escribir: métodos vacíos «porque no aplican» y métodos que lanzan
 * {@code UnsupportedOperationException}. Cada uno de esos cuerpos es una promesa incumplida, y el
 * problema no está en la clase que los escribe: está en la interfaz, que le pide cosas que no le
 * corresponden.
 *
 * <p>La división es por <strong>roles de cliente</strong>, no por tamaño: {@link AltaDeDispositivos}
 * para quien modifica, {@link ConsultaDeDispositivos} para quien mira, {@link ExportacionCsv} y
 * {@link ExportacionJson} para cada formato. Que las dos últimas tengan un solo método no es un
 * defecto: hay un cliente que quiere CSV y no JSON —{@link ExportadorCsv}—, y eso es lo que las
 * separa.
 *
 * <p>Esta interfaz sobrevive como la <strong>unión</strong> de los cuatro roles, exactamente igual
 * que {@code RepositorioResultados} en el monitor es la unión de {@code LectorResultados} y
 * {@code EscritorResultados}. Sirve para lo único para lo que debe servir: nombrar de una vez a una
 * implementación que de verdad cumple los cuatro papeles. Lo que ya no se puede hacer es
 * <em>pedirla</em> en la firma de un cliente que solo consulta.
 *
 * <p>Y la respuesta al «Para pensar» —qué criterio dice que ya se ha dividido bastante—: no es el
 * número de métodos, es que <strong>ninguna implementación tenga que escribir un método que no sabe
 * cumplir</strong>. Mientras quede un cuerpo vacío o un {@code UnsupportedOperationException} por
 * «no aplica», falta una división. Cuando no quede ninguno, sobra seguir: dividir más solo añade
 * nombres, y el cableado se vuelve ilegible sin ganar nada.
 */
public interface GestorDispositivos
        extends AltaDeDispositivos, ConsultaDeDispositivos, ExportacionCsv, ExportacionJson {
}
