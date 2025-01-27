package com.example.studentmanagement.service;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;

	// Get students list
	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}

	// Create student profile
	public Student saveStudent(Student student) {
		return studentRepository.save(student);
	}

	// Update a student profile
	public Optional<Student> updateStudent(String id, Student updatedStudent) {
		return studentRepository.findById(id).map(existingStudent -> {
			if (updatedStudent.getFirstName() != null && !updatedStudent.getFirstName().isBlank()) {
				existingStudent.setFirstName(updatedStudent.getFirstName());
			}
			if (updatedStudent.getLastName() != null && !updatedStudent.getLastName().isBlank()) {
				existingStudent.setLastName(updatedStudent.getLastName());
			}
			if (updatedStudent.getDateOfBirth() != null) {
				existingStudent.setDateOfBirth(updatedStudent.getDateOfBirth());
			}
			if (updatedStudent.getPhoneNumber() != null && !updatedStudent.getPhoneNumber().isBlank()) {
				existingStudent.setPhoneNumber(updatedStudent.getPhoneNumber());
			}
			if (updatedStudent.getEmail() != null && !updatedStudent.getEmail().isBlank()) {
				existingStudent.setEmail(updatedStudent.getEmail());
			}
			existingStudent.setCurrentScore(updatedStudent.getCurrentScore());
			return studentRepository.save(existingStudent);
		});
	}

	// Delete student profile
	public boolean deleteStudent(String id) {
		if (studentRepository.existsById(id)) {
			studentRepository.deleteById(id);
			return true;
		} else {
			return false;
		}
	}
}
