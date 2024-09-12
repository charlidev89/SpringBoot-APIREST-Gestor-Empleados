package turnos_rotativos.api.service;

import turnos_rotativos.api.dto.ConceptoLaboralDTO;
import turnos_rotativos.api.model.ConceptoLaboral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import turnos_rotativos.api.repository.ConceptoLaboralRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConceptoLaboralService implements IConceptoLaboralService {

    @Autowired
    private ConceptoLaboralRepository conceptoLaboralRepository;

    @Override
    public ConceptoLaboralDTO addConcepto(ConceptoLaboralDTO conceptoLaboralDTO) {
        ConceptoLaboral concepto = conceptoLaboralDTO.toEntity();
        concepto = this.conceptoLaboralRepository.save(concepto);

        return concepto.toDto();


    }


    public List<ConceptoLaboralDTO> obtenerConceptos(Long id, String nombre) {
        List<ConceptoLaboral> conceptos;

        if (id != null && nombre != null && !nombre.isEmpty()) {
            conceptos = conceptoLaboralRepository.findByIdAndNombreContaining(id, nombre);
        } else if (id != null) {
            conceptos = conceptoLaboralRepository.findById(id).map(List::of).orElse(List.of());
        } else if (nombre != null && !nombre.isEmpty()) {
            conceptos = conceptoLaboralRepository.findByNombreContaining(nombre);
        } else {
            conceptos = conceptoLaboralRepository.findAll();
        }

        return conceptos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    private ConceptoLaboralDTO convertToDTO(ConceptoLaboral concepto) {
        ConceptoLaboralDTO dto = new ConceptoLaboralDTO();

        dto.setNombre(concepto.getNombre());
        dto.setLaborable(concepto.isLaborable());

        if (concepto.getHsMinimo() != null) {
            dto.setHsMinimo(concepto.getHsMinimo());
        }

        if (concepto.getHsMaximo() != null) {
            dto.setHsMaximo(concepto.getHsMaximo());
        }

        return dto;
    }


}