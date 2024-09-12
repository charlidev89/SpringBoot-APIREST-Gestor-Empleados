package turnos_rotativos.api.service;

import turnos_rotativos.api.dto.JornadaLaboralDTO;
import org.apache.coyote.BadRequestException;
import turnos_rotativos.api.model.JornadaLaboral;

import java.time.LocalDate;
import java.util.List;

public interface IJornadaLaboralService {

    List<JornadaLaboralDTO> obtenerJornadas(LocalDate fechaDesde, LocalDate fechaHasta, Long nroDocumento) throws BadRequestException;

    JornadaLaboral addJornada(JornadaLaboralDTO jornadaLaboralDTO) throws BadRequestException;
}