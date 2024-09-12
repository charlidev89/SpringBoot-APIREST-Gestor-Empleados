package turnos_rotativos.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Setter;
import turnos_rotativos.api.model.ConceptoLaboral;

@Setter
@Data
public class ConceptoLaboralDTO {
    // DTO LO RECIBE POR PARAMETRO EL CONTROLLER POR LO TANTO SE DEBE VALIDAR CON LAS ANOTACIONES
    // PARA QUE PREVIAMENTE SE VALIDADO Y LUUEGO SE PERSISTIDO POR LA BD


    @NotNull(message = "name is required")
    private String nombre;
    private Integer hsMinimo;
    private Integer hsMaximo;
    private boolean laborable;


    public ConceptoLaboral toEntity() {
        ConceptoLaboral concepto = new ConceptoLaboral();
        concepto.setNombre(this.nombre);
        concepto.setHsMinimo(this.hsMinimo);
        concepto.setHsMaximo(this.hsMaximo);
        concepto.setLaborable(this.isLaborable());

        return concepto;
    }

}


