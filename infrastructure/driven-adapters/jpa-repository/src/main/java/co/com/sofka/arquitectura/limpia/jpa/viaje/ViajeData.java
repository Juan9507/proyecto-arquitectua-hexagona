package co.com.sofka.arquitectura.limpia.jpa.viaje;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "VIAJE")
public class ViajeData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_viaje")
    private Integer idViaje;

    @Column(name = "tipo_viaje")
    private String tipoViaje;

    @Column(name = "nombre_viaje")
    private String nombreViaje;
}
