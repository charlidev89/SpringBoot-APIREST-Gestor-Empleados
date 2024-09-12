package turnos_rotativos.api.model;

import jakarta.persistence.*;
import lombok.Data;
import turnos_rotativos.api.dto.ConceptoLaboralDTO;


@Data
@Entity
@Table(name = "concepto_laboral")
public class ConceptoLaboral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "hs_minimo")
    private Integer hsMinimo;

    @Column(name = "hs_maximo")
    private Integer hsMaximo;

    @Column(name = "laborable")
    private boolean laborable;

    public ConceptoLaboralDTO toDto() {
        ConceptoLaboralDTO conceptoLaboralDTO = new ConceptoLaboralDTO();
        conceptoLaboralDTO.setNombre(this.nombre);
        conceptoLaboralDTO.setHsMinimo(this.hsMinimo);
        conceptoLaboralDTO.setHsMaximo(this.hsMaximo);
        conceptoLaboralDTO.setLaborable(this.isLaborable());

        return conceptoLaboralDTO;
    }

}
