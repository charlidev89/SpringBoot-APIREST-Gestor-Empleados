package turnos_rotativos.api.service;

import turnos_rotativos.api.dto.JornadaLaboralDTO;
import turnos_rotativos.api.exceptions.NotFoundException;
import turnos_rotativos.api.model.ConceptoLaboral;
import turnos_rotativos.api.model.Empleado;
import turnos_rotativos.api.model.JornadaLaboral;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import turnos_rotativos.api.repository.ConceptoLaboralRepository;
import turnos_rotativos.api.repository.EmpleadoRepository;
import turnos_rotativos.api.repository.JornadaLaboralRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JornadaLaboralService implements IJornadaLaboralService {


    private final JornadaLaboralRepository jornadaLaboralRepository;
    private final EmpleadoRepository empleadoRepository;
    private final ConceptoLaboralRepository conceptoLaboralRepository;


    @Autowired
    public JornadaLaboralService(JornadaLaboralRepository jornadaLaboralRepository,
                                 EmpleadoRepository empleadoRepository,
                                 ConceptoLaboralRepository conceptoLaboralRepository) {
        this.jornadaLaboralRepository = jornadaLaboralRepository;
        this.empleadoRepository = empleadoRepository;
        this.conceptoLaboralRepository = conceptoLaboralRepository;
    }

    @Override
    public List<JornadaLaboralDTO> obtenerJornadas(LocalDate fechaDesde, LocalDate fechaHasta, Long nroDocumento) throws BadRequestException {
        if (fechaDesde == null && fechaHasta == null && nroDocumento == null) {
            return jornadaLaboralRepository.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }

        validarParametrosBusqueda(fechaDesde, fechaHasta, nroDocumento);

        List<JornadaLaboral> jornadas;

        if (fechaDesde != null && fechaHasta != null && nroDocumento != null) {
            jornadas = jornadaLaboralRepository.findByFechaBetweenAndEmpleadoNroDocumento(fechaDesde, fechaHasta, nroDocumento);
        } else if (fechaDesde != null && fechaHasta != null) {
            jornadas = jornadaLaboralRepository.findByFechaBetween(fechaDesde, fechaHasta);
        } else if (nroDocumento != null) {
            jornadas = jornadaLaboralRepository.findByEmpleadoNroDocumento(nroDocumento);
        } else if (fechaDesde != null) {
            jornadas = jornadaLaboralRepository.findByFechaGreaterThanEqual(fechaDesde);
        } else {
            jornadas = jornadaLaboralRepository.findByFechaLessThanEqual(fechaHasta);
        }

        return jornadas.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public JornadaLaboral addJornada(JornadaLaboralDTO jornadaLaboralDTO) throws BadRequestException {
        validarCamposObligatorios(jornadaLaboralDTO);

        Empleado empleado = empleadoRepository.findByNroDocumento(jornadaLaboralDTO.getNroDocumento())
                .orElseThrow(() -> new NotFoundException("No existe el empleado ingresado."));

        ConceptoLaboral concepto = conceptoLaboralRepository.findByNombreContaining(jornadaLaboralDTO.getConcepto())
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No existe el concepto ingresado."));
//
//        validarHorasTrabajadas(jornadaLaboralDTO, concepto);
//        validarReglasNegocio(jornadaLaboralDTO, empleado, concepto);

        JornadaLaboral jornada = jornadaLaboralRepository.save(convertToEntity(jornadaLaboralDTO, empleado, concepto));
        return jornada;
    }

    private void validarCamposObligatorios(JornadaLaboralDTO jornadaDTO) throws BadRequestException {
        if (jornadaDTO.getNroDocumento() == null) {
            throw new BadRequestException("'nroDocumento' es obligatorio.");
        }
        if (jornadaDTO.getFecha() == null) {
            throw new BadRequestException("'fecha' es obligatorio.");
        }
        if (jornadaDTO.getConcepto() == null) {
            throw new BadRequestException("'concepto' es obligatorio.");
        }
    }

    private void validarHorasTrabajadas(JornadaLaboralDTO jornadaDTO, ConceptoLaboral concepto) throws BadRequestException {
        if ((concepto.getNombre().equals("Turno Normal") || concepto.getNombre().equals("Turno Extra"))
                && jornadaDTO.getHorasTrabajadas() == null) {
            throw new BadRequestException("'horasTrabajadas' es obligatorio para el concepto ingresado.");
        }
        if (concepto.getNombre().equals("Día Libre") && jornadaDTO.getHorasTrabajadas() != null) {
            throw new BadRequestException("El concepto ingresado no requiere el ingreso de 'hsTrabajadas'");
        }
    }

    private void validarReglasNegocio(JornadaLaboralDTO jornadaDTO, Empleado empleado, ConceptoLaboral concepto) throws BadRequestException {
        LocalDate fecha = jornadaDTO.getFecha();
        Integer hsTrabajadas = jornadaDTO.getHorasTrabajadas();

        if (hsTrabajadas != null && (hsTrabajadas < concepto.getHsMinimo() || hsTrabajadas > concepto.getHsMaximo())) {
            throw new BadRequestException(String.format("El rango de horas que se puede cargar para este concepto es de %d - %d",
                    concepto.getHsMinimo(), concepto.getHsMaximo()));
        }

        int horasDiarias = jornadaLaboralRepository.sumHorasByEmpleadoAndFecha(empleado, fecha);
        if (horasDiarias + (hsTrabajadas != null ? hsTrabajadas : 0) > 14) {
            throw new BadRequestException("Un empleado no puede cargar más de 14 horas trabajadas en un día.");
        }

        LocalDate inicioSemana = fecha.with(DayOfWeek.MONDAY);
        LocalDate finSemana = fecha.with(DayOfWeek.SUNDAY);
        int horasSemana = jornadaLaboralRepository.sumHorasByEmpleadoAndFechaBetween(empleado, inicioSemana, finSemana);
        if (horasSemana + (hsTrabajadas != null ? hsTrabajadas : 0) > 52) {
            throw new BadRequestException("El empleado ingresado supera las 52 horas semanales.");
        }

        LocalDate inicioMes = fecha.withDayOfMonth(1);
        LocalDate finMes = fecha.withDayOfMonth(fecha.lengthOfMonth());
        int horasMes = jornadaLaboralRepository.sumHorasByEmpleadoAndFechaBetween(empleado, inicioMes, finMes);
        if (horasMes + (hsTrabajadas != null ? hsTrabajadas : 0) > 190) {
            throw new BadRequestException("El empleado ingresado supera las 190 horas mensuales.");
        }

        if (jornadaLaboralRepository.existsByEmpleadoAndFechaAndConceptoNombre(empleado, fecha, "Día Libre")) {
            throw new BadRequestException("El empleado ingresado cuenta con un día libre en esa fecha.");
        }

        if (concepto.getNombre().equals("Turno Extra")) {
            long turnosExtraSemana = jornadaLaboralRepository.countByEmpleadoAndConceptoNombreAndFechaBetween(
                    empleado, "Turno Extra", inicioSemana, finSemana);
            if (turnosExtraSemana >= 3) {
                throw new BadRequestException("El empleado ingresado ya cuenta con 3 turnos extra esta semana.");
            }
        }

        if (concepto.getNombre().equals("Turno Normal")) {
            long turnosNormalesSemana = jornadaLaboralRepository.countByEmpleadoAndConceptoNombreAndFechaBetween(
                    empleado, "Turno Normal", inicioSemana, finSemana);
            if (turnosNormalesSemana >= 5) {
                throw new BadRequestException("El empleado ingresado ya cuenta con 5 turnos normales esta semana.");
            }
        }

        if (concepto.getNombre().equals("Día Libre")) {
            long diasLibresSemana = jornadaLaboralRepository.countByEmpleadoAndConceptoNombreAndFechaBetween(
                    empleado, "Día Libre", inicioSemana, finSemana);
            if (diasLibresSemana >= 2) {
                throw new BadRequestException("El empleado no cuenta con más días libres esta semana.");
            }
        }

        if (concepto.getNombre().equals("Día Libre")) {
            long diasLibresMes = jornadaLaboralRepository.countByEmpleadoAndConceptoNombreAndFechaBetween(
                    empleado, "Día Libre", inicioMes, finMes);
            if (diasLibresMes >= 5) {
                throw new BadRequestException("El empleado no cuenta con más días libres este mes.");
            }
        }

        long empleadosPorConceptoDia = jornadaLaboralRepository.countByConceptoAndFecha(concepto, fecha);
        if (empleadosPorConceptoDia >= 2) {
            throw new BadRequestException("Ya existen 2 empleados registrados para este concepto en la fecha ingresada.");
        }

        if (jornadaLaboralRepository.existsByEmpleadoAndFechaAndConcepto(empleado, fecha, concepto)) {
            throw new BadRequestException("El empleado ya tiene registrado una jornada con este concepto en la fecha ingresada.");
        }
    }

    private void validarParametrosBusqueda(LocalDate fechaDesde, LocalDate fechaHasta, Long nroDocumento) throws BadRequestException {
        if (fechaDesde != null && fechaHasta != null && fechaDesde.isAfter(fechaHasta)) {
            throw new BadRequestException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
    }

    private JornadaLaboral convertToEntity(JornadaLaboralDTO dto, Empleado empleado, ConceptoLaboral concepto) {
        JornadaLaboral jornada = new JornadaLaboral();
        jornada.setEmpleado(empleado);
        jornada.setConcepto(concepto);
        jornada.setFecha(dto.getFecha());
        jornada.setHorasTrabajadas(dto.getHorasTrabajadas());
        return jornada;
    }

    private JornadaLaboralDTO convertToDTO(JornadaLaboral jornada) {
        JornadaLaboralDTO dto = new JornadaLaboralDTO();
        dto.setNombreCompleto(jornada.getEmpleado().getNombre() + " " + jornada.getEmpleado().getApellido());
        dto.setId(jornada.getId());
        dto.setNroDocumento(jornada.getEmpleado().getNroDocumento());
        dto.setFecha(jornada.getFecha());
        dto.setConcepto(jornada.getConcepto().getNombre());
        dto.setHorasTrabajadas(jornada.getHorasTrabajadas());
        return dto;
    }
}