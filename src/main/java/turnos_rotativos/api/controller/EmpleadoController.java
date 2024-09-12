package turnos_rotativos.api.controller;

import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import turnos_rotativos.api.dto.EmpleadoDTO;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import turnos_rotativos.api.service.IEmpleadoService;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@RestController
@RequestMapping("/empleado")
public class EmpleadoController {

    @Autowired
    private IEmpleadoService empleadoService;

    @PostMapping
    public ResponseEntity<EmpleadoDTO> crearEmpleado(@Valid @RequestBody EmpleadoDTO empleadoDTO, BindingResult result) throws BadRequestException {
        // verificamos si hay errores
        if (result.hasErrors()) {
            //Si hay errores, se recopilan todos los mensajes de error con result.getAllErrors().
            //Los mensajes de error se convierten a una cadena única separada por comas usando un stream y Collectors.joining(", ").
            String errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            throw new BadRequestException(errorMessages);
        }
        EmpleadoDTO empleado = empleadoService.crearEmpleado(empleadoDTO);
        return new ResponseEntity<>(empleado, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmpleadoDTO>> obtenerEmpleados() {
        return ResponseEntity.ok(empleadoService.obtenerEmpleados());
    }

    @GetMapping("/{empleadoId}")
    public ResponseEntity<EmpleadoDTO> obtenerEmpleado(@PathVariable Long empleadoId) {
        return ResponseEntity.ok(empleadoService.obtenerEmpleado(empleadoId));
    }

    @PutMapping("/{empleadoId}")
    public ResponseEntity<EmpleadoDTO> actualizarEmpleado(@PathVariable Long empleadoId,@Valid @RequestBody EmpleadoDTO empleadoDTO) throws BadRequestException {
        return ResponseEntity.ok(empleadoService.actualizarEmpleado(empleadoId, empleadoDTO));
    }

    @DeleteMapping("/{empleadoId}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Long empleadoId) throws BadRequestException {
        empleadoService.eliminarEmpleado(empleadoId);
        return ResponseEntity.noContent().build();
    }
}