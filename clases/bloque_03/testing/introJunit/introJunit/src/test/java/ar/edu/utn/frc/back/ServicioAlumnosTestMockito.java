package ar.edu.utn.frc.back;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


public class ServicioAlumnosTestMockito {

    private ServicioAlumnos servicio;
    private RepositorioAlumnos repositorio;

    @BeforeEach
    public void setup() {
        repositorio = Mockito.mock(RepositorioAlumnos.class);
        servicio = new ServicioAlumnos(repositorio);
    }

    @Test
    public void testAlumnoExistente() {
        Alumno alumnoEsperado = new Alumno("Pepe", 123);
        Mockito.when(repositorio.listar()).thenReturn(List.of(alumnoEsperado));
        Alumno x = servicio.obtenerAlumno(123);
        Assertions.assertEquals(alumnoEsperado, x);
    }

    @Test
    public void testAlumnoNoExistente() {
        Alumno alumnoEsperado = new Alumno("Laura", 234);
        Mockito.when(repositorio.listar()).thenReturn(List.of(alumnoEsperado));
        Alumno x = servicio.obtenerAlumno(123);
        Assertions.assertNull(x);
    }

}