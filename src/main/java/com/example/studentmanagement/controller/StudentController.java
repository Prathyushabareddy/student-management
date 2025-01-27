package com.example.studentmanagement.controller;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

	private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

	@Autowired
	private StudentService studentService;

	// Get all students
	@GetMapping
	public ResponseEntity<List<Student>> getAllStudents() {
		List<Student> students = studentService.getAllStudents();
		if (students.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(students);
	}

	// Create a student
	@PostMapping
	public ResponseEntity<?> createStudent(@Valid @RequestBody Student student) {
		try {
			Student createdStudent = studentService.saveStudent(student);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
		} catch (Exception e) {
			logger.error("Error creating student: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to create student: " + e.getMessage());
		}
	}

	// Update a student
	@PutMapping("/{id}")
	public ResponseEntity<?> updateStudent(@PathVariable String id, @Valid @RequestBody Student student) {
		try {
			Optional<Student> updatedStudent = studentService.updateStudent(id, student);
			if (updatedStudent.isPresent()) {
				return ResponseEntity.ok(updatedStudent.get());
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with ID " + id + " not found.");
			}
		} catch (Exception e) {
			logger.error("Error updating student: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to update student: " + e.getMessage());
		}
	}

	// Delete a student
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteStudent(@PathVariable String id) {
		try {
			boolean isDeleted = studentService.deleteStudent(id);
			if (isDeleted) {
				return ResponseEntity.ok("Student with ID " + id + " deleted successfully.");
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with ID " + id + " not found.");
			}
		} catch (Exception e) {
			logger.error("Error deleting student: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to delete student: " + e.getMessage());
		}
	}
}
