package com.example.studentmanagement.service;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {
    private MockMvc mockMvc;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(studentService).build();

        // Initialize the student object for tests
        student = new Student();
        student.setStudentNumber("S123");
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setCurrentScore(90.0);
    }

    @Test
    void testGetAllStudents() {
        // Prepare mock data
        Student student1 = new Student();
        student1.setStudentNumber("S123");
        student1.setFirstName("John");
        student1.setLastName("Doe");
        student1.setCurrentScore(90.0);
        
        Student student2 = new Student();
        student2.setStudentNumber("S124");
        student2.setFirstName("Jane");
        student2.setLastName("Doe");
        student2.setCurrentScore(95.0);
        
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));

        // Test the service method
        assertEquals(2, studentService.getAllStudents().size());
    }

    @Test
    void testSaveStudent() {
        // Initialize the student object properly
        student.setStudentNumber("S123");
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setCurrentScore(90.0);

        // Mock the save operation
        when(studentRepository.save(student)).thenReturn(student);

        // Test the service method
        Student savedStudent = studentService.saveStudent(student);
        assertNotNull(savedStudent); // This ensures the student is not null
        assertEquals("S123", savedStudent.getStudentNumber());
        assertEquals("John", savedStudent.getFirstName());
        assertEquals(90.0, savedStudent.getCurrentScore());
    }

    @Test
    void testUpdateStudent() {
        // Initialize the updated student object
        Student updatedStudent = new Student();
        updatedStudent.setStudentNumber("S123");
        updatedStudent.setFirstName("John");
        updatedStudent.setLastName("Doe");
        updatedStudent.setCurrentScore(95.0);

        // Mock the findById to return the student that needs to be updated
        when(studentRepository.findById("S123")).thenReturn(Optional.of(updatedStudent));
        
        // Mock the save operation to return the updated student
        when(studentRepository.save(updatedStudent)).thenReturn(updatedStudent);

        // Test the service method
        Optional<Student> result = studentService.updateStudent("S123", updatedStudent);

        // Check if the student was successfully updated
        assertTrue(result.isPresent(), "Student should be present after update.");
        assertEquals(95.0, result.get().getCurrentScore(), "The current score should be updated to 95.");
    }

    @Test
    void testUpdateStudentNotFound() {
        // Mock the scenario where the student is not found
        when(studentRepository.findById("S123")).thenReturn(Optional.empty());

        // Test the service method
        Optional<Student> result = studentService.updateStudent("S123", student);
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteStudent() {
        // Mock the existsById operation
        when(studentRepository.existsById("S123")).thenReturn(true);

        // Mock the deleteById operation
        doNothing().when(studentRepository).deleteById("S123");

        // Test the service method
        boolean isDeleted = studentService.deleteStudent("S123");
        assertTrue(isDeleted);
        verify(studentRepository, times(1)).deleteById("S123");
    }

    @Test
    void testDeleteStudentNotFound() {
        // Mock the scenario where the student is not found
        when(studentRepository.existsById("S123")).thenReturn(false);

        // Test the service method
        boolean isDeleted = studentService.deleteStudent("S123");
        assertFalse(isDeleted);
        verify(studentRepository, times(0)).deleteById("S123");
    }
}
