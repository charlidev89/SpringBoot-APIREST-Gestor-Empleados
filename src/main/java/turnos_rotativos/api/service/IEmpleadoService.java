package turnos_rotativos.api.service;

import turnos_rotativos.api.dto.EmpleadoDTO;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface IEmpleadoService {
    EmpleadoDTO crearEmpleado(EmpleadoDTO empleadoDto) throws BadRequestException;

    List<EmpleadoDTO> obtenerEmpleados();

    EmpleadoDTO obtenerEmpleado(Long id);

    EmpleadoDTO actualizarEmpleado(Long id, EmpleadoDTO empleadoDto) throws BadRequestException;

    void eliminarEmpleado(Long id) throws BadRequestException;
}