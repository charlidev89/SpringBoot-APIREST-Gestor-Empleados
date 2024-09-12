package turnos_rotativos.api.dto;

import lombok.Data;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
public class JornadaLaboralDTO {
    private Long id;

    @NotNull(message = "'nroDocumento' es obligatorio")
    @Positive(message = "El número de documento debe ser positivo")
    private Long nroDocumento;

    @NotBlank(message = "'nombreCompleto' es obligatorio")
    private String nombreCompleto;

    @NotNull(message = "'fecha' es obligatorio")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fecha;

    @NotBlank(message = "'concepto' es obligatorio")
    private String concepto;


    @NotNull(message = "'horasTrabajadas' es obligatorio para algunos conceptos")
    @Min(value = 0, message = "Las horas trabajadas no pueden ser negativas")
    @Max(value = 24, message = "Las horas trabajadas no pueden exceder 24")
    private Integer horasTrabajadas;
}