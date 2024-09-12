package turnos_rotativos.api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmpleadoDTO {

    private Long id;

    @NotBlank(message = "'nombre' es obligatorio")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Solo se permiten letras en el campo 'nombre'")
    private String nombre;

    @NotBlank(message = "'apellido' es obligatorio")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Solo se permiten letras en el campo 'apellido'")
    private String apellido;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @NotNull(message = "El número de documento no puede estar vacío")
    @Positive(message = "El número de documento debe ser positivo")
    private Long nroDocumento;

    @NotNull(message = "La fecha de nacimiento no puede estar vacía")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private LocalDate fechaNacimiento;

    @NotNull(message = "La fecha de ingreso no puede estar vacía")
    @PastOrPresent(message = "La fecha de ingreso no puede ser posterior al día de la fecha")
    private LocalDate fechaIngreso;
}
