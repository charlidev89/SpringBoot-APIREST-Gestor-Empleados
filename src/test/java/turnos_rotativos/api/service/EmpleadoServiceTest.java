package turnos_rotativos.api.service;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import turnos_rotativos.api.dto.EmpleadoDTO;
import turnos_rotativos.api.model.Empleado;
import turnos_rotativos.api.repository.EmpleadoRepository;
import turnos_rotativos.api.repository.JornadaLaboralRepository;


import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private JornadaLaboralRepository jornadaLaboralRepository;

    @InjectMocks
    private EmpleadoService empleadoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearEmpleado() throws BadRequestException {
        // Arrange
        System.out.println("Iniciando testCrearEmpleado");
        EmpleadoDTO empleadoDTO = new EmpleadoDTO();
        empleadoDTO.setNombre("Carlos");
        empleadoDTO.setApellido("Arteaga");
        empleadoDTO.setEmail("carlos@gmail.com");
        empleadoDTO.setNroDocumento(12345678L);
        empleadoDTO.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        empleadoDTO.setFechaIngreso(LocalDate.now());

        Empleado empleadoGuardado = new Empleado();
        empleadoGuardado.setId(1L);
        empleadoGuardado.setNombre("Carlos");
        empleadoGuardado.setApellido("Arteaga");
        empleadoGuardado.setEmail("carlos@gmail.com");
        empleadoGuardado.setNroDocumento(12345678L);
        empleadoGuardado.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        empleadoGuardado.setFechaIngreso(LocalDate.now());

        when(empleadoRepository.existsByNroDocumento(empleadoDTO.getNroDocumento())).thenReturn(false);
        when(empleadoRepository.existsByEmail(empleadoDTO.getEmail())).thenReturn(false);
        when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleadoGuardado);

        // Act
        EmpleadoDTO resultado = empleadoService.crearEmpleado(empleadoDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Carlos", resultado.getNombre());
        assertEquals("Arteaga", resultado.getApellido());
        assertEquals("carlos@gmail.com", resultado.getEmail());
        assertEquals(12345678L, resultado.getNroDocumento());
    }

    @Test
    void testObtenerEmpleado() {
        // inicio
        System.out.println("Iniciando testObtenerEmpleado");
        Long empleadoId = 1L;
        Empleado empleado = new Empleado();
        empleado.setId(empleadoId);
        empleado.setNombre("Carlos");
        empleado.setApellido("Arteaga");
        empleado.setEmail("carlos@gmail.com");
        empleado.setNroDocumento(12345678L);
        empleado.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        empleado.setFechaIngreso(LocalDate.now());

        when(empleadoRepository.findById(empleadoId)).thenReturn(Optional.of(empleado));

        // Act
        EmpleadoDTO resultado = empleadoService.obtenerEmpleado(empleadoId);

        // Assert
        assertNotNull(resultado);
        assertEquals(empleadoId, resultado.getId());
        assertEquals("Carlos", resultado.getNombre());
        assertEquals("Arteaga", resultado.getApellido());
        assertEquals("carlos@gmail.com", resultado.getEmail());
        assertEquals(12345678L, resultado.getNroDocumento());
    }
    @Test
    void testEliminarEmpleado() {
        // inicio
        System.out.println("Iniciando testEliminarEmpleado");
        Long empleadoId = 1L;
        Empleado empleado = new Empleado();
        empleado.setId(empleadoId);

        when(empleadoRepository.findById(empleadoId)).thenReturn(Optional.of(empleado));
        when(jornadaLaboralRepository.existsByEmpleado(empleado)).thenReturn(false);

        // Act
        assertDoesNotThrow(() -> empleadoService.eliminarEmpleado(empleadoId));

        // Assert
        verify(empleadoRepository, times(1)).findById(empleadoId);
        verify(jornadaLaboralRepository, times(1)).existsByEmpleado(empleado);
        verify(empleadoRepository, times(1)).deleteById(empleadoId);
    }
}