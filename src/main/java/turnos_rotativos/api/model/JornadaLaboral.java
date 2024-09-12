package turnos_rotativos.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import turnos_rotativos.api.dto.JornadaLaboralDTO;


import java.time.LocalDate;

@Data
@Entity
@Table(name = "jornada_laboral")
public class JornadaLaboral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    @NotNull(message = "'empleado' es obligatorio")
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name = "id_concepto", nullable = false)
    @NotNull(message = "'concepto' es obligatorio")
    private ConceptoLaboral concepto;

    @NotNull(message = "'fecha' es obligatorio")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fecha;
    @Column(name = "hs_trabajadas")
    private Integer horasTrabajadas;

    public JornadaLaboralDTO convertToDTO() {
        JornadaLaboralDTO dto = new JornadaLaboralDTO();
        dto.setId(this.id);
        dto.setNroDocumento(this.empleado.getNroDocumento());
        dto.setNombreCompleto(this.empleado.getNombre() + " " + this.empleado.getApellido());
        dto.setFecha(this.fecha);
        dto.setConcepto(this.concepto.getNombre());
        dto.setHorasTrabajadas(this.horasTrabajadas);
        return dto;
    }


}
