package com.example.studentmanagement.controller;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void testGetAllStudents() throws Exception {
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
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(student1, student2));

        // Perform the GET request and assert the results
        mockMvc.perform(MockMvcRequestBuilders.get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].studentNumber").value("S123"))
                .andExpect(jsonPath("$[1].studentNumber").value("S124"));
    }

    @Test
    void testCreateStudent() throws Exception {
        // Prepare mock data
        Student student = new Student();
        student.setStudentNumber("S123");
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setCurrentScore(90.0);
        when(studentService.saveStudent(any(Student.class))).thenReturn(student);

        // Perform the POST request and assert the results
        mockMvc.perform(MockMvcRequestBuilders.post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentNumber\":\"S123\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"currentScore\":90.0}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentNumber").value("S123"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void testUpdateStudent() throws Exception {
        // Prepare mock data
        Student updatedStudent = new Student();
        updatedStudent.setStudentNumber("S123");
        updatedStudent.setFirstName("John");
        updatedStudent.setLastName("Doe");
        updatedStudent.setCurrentScore(90.0);
        when(studentService.updateStudent(eq("S123"), any(Student.class))).thenReturn(Optional.of(updatedStudent));

        // Perform the PUT request and assert the results
        mockMvc.perform(MockMvcRequestBuilders.put("/api/students/S123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentNumber\":\"S123\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"currentScore\":95.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentNumber").value("S123"))
                .andExpect(jsonPath("$.currentScore").value(90.0));
    }

    @Test
    void testUpdateStudentNotFound() throws Exception {
        // Prepare mock data for student not found
        when(studentService.updateStudent(eq("S123"), any(Student.class))).thenReturn(Optional.empty());

        // Perform the PUT request and assert the results
        mockMvc.perform(MockMvcRequestBuilders.put("/api/students/S123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentNumber\":\"S123\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"currentScore\":95.0}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Student with ID S123 not found."));
    }

    @Test
    void testDeleteStudent() throws Exception {
        // Prepare mock data for successful deletion
        when(studentService.deleteStudent("S123")).thenReturn(true);

        // Perform the DELETE request and assert the results
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/students/S123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Student with ID S123 deleted successfully."));
    }

    @Test
    void testDeleteStudentNotFound() throws Exception {
        // Prepare mock data for student not found
        when(studentService.deleteStudent("S123")).thenReturn(false);

        // Perform the DELETE request and assert the results
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/students/S123"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Student with ID S123 not found."));
    }
}
