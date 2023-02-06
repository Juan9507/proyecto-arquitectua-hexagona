package co.com.sofka.arquitectura.limpia.model.persona;
import co.com.sofka.arquitectura.limpia.model.viaje.Viaje;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Persona {
    private Integer cedula;
    private String nombre;
    private Date fechaNacimiento;
    private String tipoSangre;
    private List<Viaje> viajes;
}
