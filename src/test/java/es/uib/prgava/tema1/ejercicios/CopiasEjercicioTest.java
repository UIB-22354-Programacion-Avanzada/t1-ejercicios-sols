package es.uib.prgava.tema1.ejercicios;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Ejercicio 1.3.3. */
class CopiasEjercicioTest {

    @Test
    void copiarSiFiltraYCuenta() {
        List<Integer> destino = new ArrayList<>();

        int copiados = CopiasEjercicio.copiarSi(List.of(1, 2, 3, 4), destino, n -> n % 2 == 0);

        assertEquals(2, copiados);
        assertEquals(List.of(2, 4), destino);
    }

    @Test
    void copiarSiAdmiteUnDestinoDeUnSupertipo() {
        List<Object> destino = new ArrayList<>();

        CopiasEjercicio.copiarSi(Datos.historialDeEjemplo(), destino, r -> r.esFallo());

        assertEquals(3, destino.size());
    }

    /**
     * Ejercicio 1.3.3: esta prueba venía comentada y se descomenta al corregir las firmas de
     * {@code fusionar}. Con la firma original —{@code fusionar(List<T>, List<T>, List<T>)}— no
     * compilaba, porque exigía que las tres listas fueran del mismo tipo exacto y aquí son
     * Integer, Double y Number.
     */
    @Test
    void fusionarAdmiteListasDeSubtiposEnUnDestinoDeSupertipo() {
        List<Integer> enteros = List.of(1, 2);
        List<Double> reales = List.of(3.0);
        List<Number> destino = new ArrayList<>();

        CopiasEjercicio.fusionar(enteros, reales, destino);

        assertEquals(3, destino.size());
    }

    @Test
    void fusionarAdmiteUnDestinoMuchoMasGeneral() {
        List<Integer> unos = List.of(1);
        List<Integer> dos = List.of(2);
        List<Object> destino = new ArrayList<>();

        CopiasEjercicio.fusionar(unos, dos, destino);

        assertEquals(List.of(1, 2), destino);
    }
}
