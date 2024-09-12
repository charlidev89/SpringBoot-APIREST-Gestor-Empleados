package turnos_rotativos.api.service;

import turnos_rotativos.api.dto.ConceptoLaboralDTO;

import java.util.List;

public interface IConceptoLaboralService {
    List<ConceptoLaboralDTO> obtenerConceptos(Long id, String nombre);


    ConceptoLaboralDTO addConcepto(ConceptoLaboralDTO conceptoLaboralDTO);
}