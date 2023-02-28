package com.school.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "student")
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@NotBlank(message = "Name Must Required..!!")
	private String sname;
	
	@NotNull(message = "Roll Number Must Required..!!")
	private Long rollNo;
	
	@NotNull(message = "Department Must Required..!!")
	private String department;
	
	@NotBlank(message = "Email Must Required..!!")
	private String email;
	
	@NotNull(message = "Standard Must Required..!!")
	private Long standard;
	
	@NotNull(message = "Age Must Between 3 to 15 Years")
	@Min(value = 3)
	@Max(value = 15)
	private Long age;
	
	
	private String gender;
	
	private String profileImage;

	@ManyToOne
	@JsonIgnore
	private Teacher teacher;

	public Student() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public Long getRollNo() {
		return rollNo;
	}

	public void setRollNo(Long rollNo) {
		this.rollNo = rollNo;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Long getStandard() {
		return standard;
	}

	public void setStandard(Long standard) {
		this.standard = standard;
	}

	public Long getAge() {
		return age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	@Override
	public boolean equals(Object obj) {
		
		return this.id == ((Student)obj).getId();
	}

	

	

}
