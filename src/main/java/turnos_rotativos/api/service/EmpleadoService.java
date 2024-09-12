package turnos_rotativos.api.service;

import turnos_rotativos.api.dto.EmpleadoDTO;
import turnos_rotativos.api.exceptions.ConflictException;
import turnos_rotativos.api.exceptions.NotFoundException;
import turnos_rotativos.api.model.Empleado;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import turnos_rotativos.api.repository.EmpleadoRepository;
import turnos_rotativos.api.repository.JornadaLaboralRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpleadoService implements IEmpleadoService {
    private  EmpleadoRepository empleadoRepository;
    private  JornadaLaboralRepository jornadaLaboralRepository;

    @Autowired
    public EmpleadoService(EmpleadoRepository empleadoRepository, JornadaLaboralRepository jornadaLaboralRepository) {
        this.empleadoRepository = empleadoRepository;
        this.jornadaLaboralRepository = jornadaLaboralRepository;
    }

    @Override
    public EmpleadoDTO crearEmpleado(EmpleadoDTO empleadoDTO) throws BadRequestException {
        validarEmpleado(empleadoDTO);

        if (empleadoRepository.existsByNroDocumento(empleadoDTO.getNroDocumento())) {
            throw new ConflictException("Ya existe un empleado con el documento ingresado.");
        }

        if (empleadoRepository.existsByEmail(empleadoDTO.getEmail())) {
            throw new ConflictException("Ya existe un empleado con el email ingresado.");
        }

        Empleado empleado = convertToEntity(empleadoDTO);
        empleado.setFechaCreacion(LocalDate.now());
        empleado = empleadoRepository.save(empleado);
        return convertToDTO(empleado);
    }

    @Override
    public List<EmpleadoDTO> obtenerEmpleados() {
        return empleadoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmpleadoDTO obtenerEmpleado(Long id) {
        return empleadoRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new NotFoundException("No se encontró el empleado con Id: " + id));
    }


    @Override
    public EmpleadoDTO actualizarEmpleado(Long id, EmpleadoDTO empleadoDTO) throws BadRequestException {
        Empleado empleadoExistente = empleadoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontró el empleado con Id: " + id));

        validarEmpleado(empleadoDTO);

        if (!empleadoExistente.getNroDocumento().equals(empleadoDTO.getNroDocumento()) &&
                empleadoRepository.existsByNroDocumento(empleadoDTO.getNroDocumento())) {
            throw new ConflictException("Ya existe un empleado con el documento ingresado.");
        }

        if (!empleadoExistente.getEmail().equals(empleadoDTO.getEmail()) &&
                empleadoRepository.existsByEmail(empleadoDTO.getEmail())) {
            throw new ConflictException("Ya existe un empleado con el email ingresado.");
        }

        updateEmpleadoFromDTO(empleadoDTO, empleadoExistente);
        empleadoExistente = empleadoRepository.save(empleadoExistente);
        return convertToDTO(empleadoExistente);
    }
    public Empleado findByNroDocumento(Long nroDocumento) {
        return (Empleado) empleadoRepository.findByNroDocumento(nroDocumento)
                .orElseThrow(() -> new NotFoundException("No existe el empleado con el número de documento ingresado."));
    }


    public boolean existeEmpleado(Long nroDocumento) {
        return empleadoRepository.existsByNroDocumento(nroDocumento);
    }

    public void eliminarEmpleado(Long id) throws BadRequestException {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontró el empleado con Id: " + id));

        if (jornadaLaboralRepository.existsByEmpleado(empleado)) {
            throw new BadRequestException("No es posible eliminar un empleado con jornadas asociadas.");
        }

        empleadoRepository.deleteById(id);
    }

    private void validarEmpleado(EmpleadoDTO empleadoDTO) throws BadRequestException {
        if (ChronoUnit.YEARS.between(empleadoDTO.getFechaNacimiento(), LocalDate.now()) < 18) {
            throw new BadRequestException("La edad del empleado no puede ser menor a 18 años.");
        }
    }

    private Empleado convertToEntity(EmpleadoDTO dto) {
        Empleado empleado = new Empleado();
        empleado.setNombre(dto.getNombre());
        empleado.setApellido(dto.getApellido());
        empleado.setEmail(dto.getEmail());
        empleado.setNroDocumento(dto.getNroDocumento());
        empleado.setFechaNacimiento(dto.getFechaNacimiento());
        empleado.setFechaIngreso(dto.getFechaIngreso());
        return empleado;
    }

    private EmpleadoDTO convertToDTO(Empleado empleado) {
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setId(empleado.getId());
        dto.setNombre(empleado.getNombre());
        dto.setApellido(empleado.getApellido());
        dto.setEmail(empleado.getEmail());
        dto.setNroDocumento(empleado.getNroDocumento());
        dto.setFechaNacimiento(empleado.getFechaNacimiento());
        dto.setFechaIngreso(empleado.getFechaIngreso());
        return dto;
    }

    private void updateEmpleadoFromDTO(EmpleadoDTO dto, Empleado empleado) {
        empleado.setNombre(dto.getNombre());
        empleado.setApellido(dto.getApellido());
        empleado.setEmail(dto.getEmail());
        empleado.setNroDocumento(dto.getNroDocumento());
        empleado.setFechaNacimiento(dto.getFechaNacimiento());
        empleado.setFechaIngreso(dto.getFechaIngreso());
    }
}