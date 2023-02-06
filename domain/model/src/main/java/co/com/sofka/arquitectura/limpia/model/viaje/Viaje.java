package co.com.sofka.arquitectura.limpia.model.viaje;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Viaje {
    private Integer idViaje;
    private String tipoViaje;
    private String nombreViaje;
}
