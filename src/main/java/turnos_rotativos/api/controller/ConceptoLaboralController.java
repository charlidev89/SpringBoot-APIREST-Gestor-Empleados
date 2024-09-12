package turnos_rotativos.api.controller;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import turnos_rotativos.api.dto.ConceptoLaboralDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import turnos_rotativos.api.model.ConceptoLaboral;
import turnos_rotativos.api.service.IConceptoLaboralService;

import java.util.List;

@RestController
@RequestMapping("/concepto-laboral")
public class ConceptoLaboralController {

    @Autowired
    private IConceptoLaboralService conceptoLaboralService;


    @PostMapping("/crearConcepto")
    public ResponseEntity<ConceptoLaboral> addConcepto(@Valid @RequestBody ConceptoLaboralDTO conceptoLaboralDTO) {
        ConceptoLaboralDTO c = this.conceptoLaboralService.addConcepto(conceptoLaboralDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(c.toEntity());
    }


    @GetMapping
    public ResponseEntity<List<ConceptoLaboralDTO>> obtenerConceptos(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String nombre) {
        return ResponseEntity.ok(conceptoLaboralService.obtenerConceptos(id, nombre));
    }



}