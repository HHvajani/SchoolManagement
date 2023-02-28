package com.school.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.school.dao.StudentRepository;
import com.school.dao.TeacherRepository;
import com.school.entity.Student;
import com.school.entity.Teacher;

@RestController
public class SearchController {

	@Autowired
	private TeacherRepository teacherRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal){
		
		System.out.println(query);
		
		Teacher teacher = this.teacherRepository.getTeacherByTeacherName(principal.getName());
		
		List<Student> student = this.studentRepository.findBysnameContainingAndTeacher(query, teacher);
		
		return ResponseEntity.ok(student);
	}
}







