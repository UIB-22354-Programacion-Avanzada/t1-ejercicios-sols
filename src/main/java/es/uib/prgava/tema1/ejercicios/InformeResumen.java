package es.uib.prgava.tema1.ejercicios;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import es.uib.prgava.tema1.monitor.LectorResultados;
import es.uib.prgava.tema1.monitor.Resultado;
import es.uib.prgava.tema1.monitor.Servicio;

/**
 * Ejercicio 1.2.9. <strong>Solución comentada.</strong> Informe que respeta las opciones de una
 * {@link ConfiguracionInforme}.
 *
 * <p>Dos dependencias por constructor, como en el ejercicio 1.2.5: de dónde salen los datos
 * ({@link LectorResultados}, la interfaz estrecha, porque aquí solo se lee) y cómo hay que
 * presentarlos (la configuración). Ninguna se fabrica dentro.
 */
public final class InformeResumen {

    private final LectorResultados lector;
    private final ConfiguracionInforme configuracion;

    public InformeResumen(LectorResultados lector, ConfiguracionInforme configuracion) {
        this.lector = Objects.requireNonNull(lector, "El lector no puede ser nulo");
        this.configuracion = Objects.requireNonNull(configuracion, "La configuración no puede ser nula");
    }

    /** Texto del informe, respetando título, número máximo de servicios y filtros. */
    public String texto() {
        var cuerpo = disponibilidades().entrySet().stream()
                // soloConFallos: si está activo, se descartan los servicios con disponibilidad 1.
                .filter(e -> !configuracion.soloConFallos() || e.getValue() < 1.0)
                // De peor a mejor disponibilidad: lo interesante va primero.
                .sorted(Map.Entry.comparingByValue())
                // maximoServicios: el límite se aplica DESPUÉS de ordenar, para que el recorte
                // deje los peores y no unos cualesquiera. El orden de las operaciones del
                // pipeline es parte de la especificación, no un detalle.
                .limit(configuracion.maximoServicios())
                .map(this::linea)
                .collect(Collectors.joining(System.lineSeparator()));

        return configuracion.titulo() + System.lineSeparator() + cuerpo;
    }

    private String linea(Map.Entry<Servicio, Double> entrada) {
        String base = String.format("%-28s %6.1f %%", entrada.getKey().nombre(), entrada.getValue() * 100);
        if (!configuracion.incluirLatencias()) {
            return base;
        }
        double media = lector.historial(entrada.getKey()).stream()
                .filter(r -> !r.esFallo())
                .mapToLong(r -> r.latencia().toMillis())
                .average()
                .orElse(0.0);
        return base + String.format("   latencia media %5.0f ms", media);
    }

    /**
     * Servicio con menor disponibilidad, o vacío si el repositorio no tiene datos.
     *
     * <p>Devolver {@code Optional} y no {@code null} no es una preferencia estética: el tipo
     * declara que el resultado puede no existir, y quien llama no puede olvidarse de ese caso
     * sin que el compilador se lo recuerde. {@code null} no avisa de nada y produce
     * {@code NullPointerException} lejos de su origen.
     *
     * <p>{@code min} sobre un Stream ya devuelve un {@code Optional} vacío si no hay elementos,
     * así que el caso «repositorio sin datos» sale gratis, sin ningún {@code if}.
     */
    public Optional<Servicio> peorServicio() {
        return disponibilidades().entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    /** Fracción de comprobaciones correctas por servicio, en [0, 1]. */
    private Map<Servicio, Double> disponibilidades() {
        return lector.servicios().stream()
                .collect(Collectors.toMap(
                        servicio -> servicio,
                        this::disponibilidadDe));
    }

    private double disponibilidadDe(Servicio servicio) {
        var historial = lector.historial(servicio);
        if (historial.isEmpty()) {
            return 0.0;
        }
        long correctas = historial.stream().filter(r -> !r.esFallo()).count();
        return (double) correctas / historial.size();
    }

    /** Comparador auxiliar, por si se quiere ordenar por latencia en vez de por disponibilidad. */
    static Comparator<Resultado> porLatencia() {
        return Comparator.comparing(Resultado::latencia);
    }
}
