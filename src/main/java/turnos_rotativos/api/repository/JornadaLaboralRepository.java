package turnos_rotativos.api.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import turnos_rotativos.api.model.ConceptoLaboral;
import turnos_rotativos.api.model.Empleado;
import turnos_rotativos.api.model.JornadaLaboral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface JornadaLaboralRepository extends JpaRepository<JornadaLaboral, Long> {
    @Query("SELECT j FROM JornadaLaboral j WHERE j.fecha BETWEEN :fechaDesde AND :fechaHasta AND j.empleado.nroDocumento = :nroDocumento")
    List<JornadaLaboral> findByFechaBetweenAndEmpleadoNroDocumento(LocalDate fechaDesde, LocalDate fechaHasta, Long nroDocumento);

    List<JornadaLaboral> findByFechaBetween(LocalDate fechaDesde, LocalDate fechaHasta);

    List<JornadaLaboral> findByEmpleadoNroDocumento(Long nroDocumento);

    List<JornadaLaboral> findByFechaLessThanEqual(LocalDate fechaHasta);

    List<JornadaLaboral> findByFechaGreaterThanEqual(LocalDate fechaDesde);

    // Nuevos métodos para las reglas de negocio
    boolean existsByEmpleadoAndFechaAndConceptoNombre(Empleado empleado, LocalDate fecha, String nombreConcepto);

    @Query("SELECT SUM(j.horasTrabajadas) FROM JornadaLaboral j WHERE j.empleado = :empleado AND j.fecha = :fecha")
    Integer sumHorasByEmpleadoAndFecha(@Param("empleado") Empleado empleado, @Param("fecha") LocalDate fecha);

    @Query("SELECT SUM(j.horasTrabajadas) FROM JornadaLaboral j WHERE j.empleado = :empleado AND j.fecha BETWEEN :fechaInicio AND :fechaFin")
    Integer sumHorasByEmpleadoAndFechaBetween(@Param("empleado") Empleado empleado, @Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

    boolean existsByEmpleado(Empleado empleado);

    long countByEmpleadoAndConceptoNombreAndFechaBetween(Empleado empleado, String turnoExtra, LocalDate inicioSemana, LocalDate finSemana);

    long countByConceptoAndFecha(ConceptoLaboral concepto, LocalDate fecha);

    boolean existsByEmpleadoAndFechaAndConcepto(Empleado empleado, LocalDate fecha, ConceptoLaboral concepto);
}
