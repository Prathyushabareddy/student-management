package com.example.studentmanagement.model;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.studentmanagement.validationgroup.CreateGroup;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

@Entity
@Table(name = "students")
public class Student {
	@Id
	@Column(nullable = false, unique = true)
	private String studentNumber;

	@Column(nullable = false)
	@NotNull(message = "First name is mandatory", groups = CreateGroup.class)
	private String firstName;

	@Column(nullable = false)
	@NotNull(message = "Last name is mandatory", groups = CreateGroup.class)
	private String lastName;

	@Pattern(regexp = "^\\+\\d{2,3}\\d{10}$", message = "Phone number should have a country code and 10 digits")
	private String phoneNumber;

	@Email(message = "Email should be valid")
	private String email;

	@Column(nullable = false)
	@DecimalMin(value = "0.0", message = "Current score must be between 0.0 and 100.0")
	@DecimalMax(value = "100.0", message = "Current score must be between 0.0 and 100.0")
	private double currentScore;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate dateOfBirth;

	public Student() {
		// TODO Auto-generated constructor stub
	}

	@PrePersist
	public void generateStudentNumber() {
		char firstInitial = firstName.charAt(0);
		char lastInitial = lastName.charAt(0);
		int randomNum = (int) (Math.random() * 900000) + 100000; // Generates 6-digit number
		this.studentNumber = Character.toUpperCase(firstInitial) + String.valueOf(randomNum)
				+ Character.toUpperCase(lastInitial);
	}

	@Transient
	private String fullName;

	public String getFullName() {
		return firstName + " " + lastName;
	}

	public String getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getCurrentScore() {
		return currentScore;
	}

	public void setCurrentScore(double currentScore) {
		this.currentScore = currentScore;
	}

}
