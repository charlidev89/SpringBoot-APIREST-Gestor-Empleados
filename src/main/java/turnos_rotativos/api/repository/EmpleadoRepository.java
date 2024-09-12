package turnos_rotativos.api.repository;

import turnos_rotativos.api.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    boolean existsByNroDocumento(Long nroDocumento);
    boolean existsByEmail(String email);

    Optional<Empleado> findByNroDocumento(Long nroDocumento);
}
