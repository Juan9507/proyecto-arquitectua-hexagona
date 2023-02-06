package co.com.sofka.arquitectura.limpia.jpa.persona;

import co.com.sofka.arquitectura.limpia.jpa.viaje.ViajeData;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "PERSONA")
public class PersonaData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_persona")
    private Integer idPersona;

    private String nombre;

    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;

    @Column(name = "tipo_sangre")
    private String tipoSangre;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_persona")
    private List<ViajeData> viajes;

}
