package turnos_rotativos.api.controller;


import jakarta.validation.Valid;
import lombok.SneakyThrows;
import turnos_rotativos.api.dto.JornadaLaboralDTO;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import turnos_rotativos.api.model.JornadaLaboral;
import turnos_rotativos.api.service.IJornadaLaboralService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/jornada")
public class JornadaLaboralController {

    private final IJornadaLaboralService jornadaLaboralService;

    @Autowired
    public JornadaLaboralController(IJornadaLaboralService jornadaLaboralService) {
        this.jornadaLaboralService = jornadaLaboralService;
    }

    @SneakyThrows
    @PostMapping
    public ResponseEntity<JornadaLaboralDTO> addJornada(@Valid @RequestBody JornadaLaboralDTO jornadaLaboralDTO)  {
        JornadaLaboral jornadaLaboral = this.jornadaLaboralService.addJornada(jornadaLaboralDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(jornadaLaboral.convertToDTO());
    }



    @GetMapping
    public ResponseEntity<List<JornadaLaboralDTO>> obtenerJornadas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required = false) Long nroDocumento) throws BadRequestException {
        List<JornadaLaboralDTO> jornadas = jornadaLaboralService.obtenerJornadas(fechaDesde, fechaHasta, nroDocumento);
        return ResponseEntity.ok(jornadas);
    }
}
