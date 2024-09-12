package turnos_rotativos.api.repository;

import turnos_rotativos.api.model.ConceptoLaboral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConceptoLaboralRepository extends JpaRepository<ConceptoLaboral, Long> {
    List<ConceptoLaboral> findByNombreContaining(String nombre);

    List<ConceptoLaboral> findByIdAndNombreContaining(Long id, String nombre);

}
