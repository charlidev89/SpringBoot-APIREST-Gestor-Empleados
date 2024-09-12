package turnos_rotativos.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "empleados")
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Solo se permiten letras en el campo 'nombre'")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Solo se permiten letras en el campo 'apellido'")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email ingresado no es correcto")
    private String email;

    @NotNull(message = "El número de documento es obligatorio")
    private Long nroDocumento;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento no puede ser posterior al día de la fecha")
    private LocalDate fechaNacimiento;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    @PastOrPresent(message = "La fecha de ingreso no puede ser posterior al día de la fecha")
    private LocalDate fechaIngreso;

    private LocalDate fechaCreacion;
}
